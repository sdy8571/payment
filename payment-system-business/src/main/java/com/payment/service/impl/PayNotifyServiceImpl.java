package com.payment.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.exceptions.ExceptionUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.framework.base.pojo.Result;
import com.payment.async.AsyncConfig;
import com.payment.contants.enums.PayNotifyStatusEnum;
import com.payment.contants.enums.PayNotifyTypeEnum;
import com.payment.data.entity.PayNotifyLogEntity;
import com.payment.data.entity.PayNotifyTaskEntity;
import com.payment.data.entity.PayOrderEntity;
import com.payment.data.entity.PayRefundEntity;
import com.payment.data.mapper.PayNotifyLogMapper;
import com.payment.data.mapper.PayNotifyTaskMapper;
import com.payment.mq.producer.PayNotifyProducer;
import com.payment.service.PayNotifyService;
import com.payment.service.PayOrderService;
import com.payment.service.PayRefundService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;


/**
 * 支付通知 Core Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Valid
@Slf4j
public class PayNotifyServiceImpl implements PayNotifyService {

    @Resource
    @Lazy // 循环依赖，避免报错
    private PayOrderService payOrderService;
    @Resource
    @Lazy // 循环依赖，避免报错
    private PayRefundService payRefundService;
    @Resource
    private PayNotifyTaskMapper payNotifyTaskMapper;
    @Resource
    private PayNotifyLogMapper payNotifyLogMapper;
    @Resource
    private PayNotifyProducer payNotifyProducer;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createPayNotifyTask(Integer type, Long dataId) {
        PayNotifyTaskEntity task = PayNotifyTaskEntity.builder().type(type).dataId(dataId)
                .status(PayNotifyStatusEnum.WAITING.getStatus())
                .nextNotifyTime(DateUtil.date()).notifyTimes(1).maxNotifyTimes(AsyncConfig.NOTIFY_FREQUENCY.size())
                .notifyFrequency(AsyncConfig.NOTIFY_FREQUENCY)
                .build();
        // 补充 channelId + notifyUrl 字段
        if (Objects.equals(task.getType(), PayNotifyTypeEnum.ORDER.getType())) {
            PayOrderEntity order = payOrderService.getById(task.getDataId()); // 不进行非空判断，有问题直接异常
            task.setChannelId(order.getChannelId());
            task.setMerchantOrderId(order.getOutTradeNo());
            task.setNotifyUrl(order.getNotifyUrl());
            task.setNotifyBody(JSONUtil.toJsonStr(genOrderNotifyReq(task, order)));
        } else if (Objects.equals(task.getType(), PayNotifyTypeEnum.REFUND.getType())) {
            PayRefundEntity refund = payRefundService.getById(task.getDataId());
            task.setChannelId(refund.getChannelId());
            task.setMerchantOrderId(refund.getOutTradeNo());
            task.setMerchantRefundId(refund.getOutRefundNo());
            task.setNotifyUrl(refund.getNotifyUrl());
            task.setNotifyBody(JSONUtil.toJsonStr(genRefundNotifyReq(task, refund)));
        }
        // 执行插入
        payNotifyTaskMapper.insert(task);
        // 异步通知业务系统
        payNotifyProducer.sendMsg(task);
        log.info("同步处理结束，异步处理服务商回调开始");
    }

    /**
     * todo 后续可修改为定时任务，视情况而定
     * 立即执行一次通知，后续根据循环时间多次通知，确保通知到商家端
     * @param taskId 任务号
     */
    private void handleNotify(Long taskId) {
        List<Integer> frequency = AsyncConfig.NOTIFY_FREQUENCY;
        for (int i = 0; i < frequency.size() + 1; i++) {
            try {
                if (i > 0) {
                    // 计时器
                    countdown(frequency.get(i - 1));
                }
                // 立即执行一次通知
                boolean res = notify(taskId, i);
                if (res) {
                    log.info("通知第{}次成功", i + 1);
                    break;
                }
            } catch (Exception ex) {
                log.error("通知第{}次失败", i + 1, ex);
            }
        }
    }

    private void countdown(Integer seconds) throws InterruptedException {
        final CountDownLatch latch = new CountDownLatch(seconds);
        for (int i = 0; i < seconds; i++) {
            latch.countDown();
            latch.await(1L, TimeUnit.SECONDS);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public boolean notify(Long taskId, int i) {
        log.info("开始第{}次通知", i + 1);
        // 查询任务
        PayNotifyTaskEntity task = payNotifyTaskMapper.selectById(taskId);
        if (task == null || !PayNotifyStatusEnum.WAITING.getStatus().equals(task.getStatus())) {
            log.info("任务不存在，或非进行中任务，直接退出通知");
            return true;
        }
        // 执行通知
        Result<?> invokeResult = null;
        Throwable invokeException = null;
        try {
            invokeResult = notifyInvoke(task.getNotifyUrl(), task.getNotifyBody());
        } catch (Exception ex) {
            invokeException = ex;
        }
        // 处理通知结果
        Integer result = processNotifyResult(task, invokeResult, invokeException);
        // 记录并保存日志 PayNotifyLog 日志
        String response = invokeException != null ? ExceptionUtil.getRootCauseMessage(invokeException) :
                 JSONUtil.toJsonStr(invokeResult);
        payNotifyLogMapper.insert(PayNotifyLogEntity.builder().taskId(task.getId())
                .notifyTimes(task.getNotifyTimes()).response(response).status(task.getStatus()).build());
        // 返回结果
        log.info("结束第{}次通知", i + 1);
        return PayNotifyStatusEnum.isEnd(result);
    }

    /**
     //     * 处理并更新通知结果
     //     * @param task 通知任务
     //     * @param invokeResult 通知结果
     //     * @param invokeException 通知异常
     //     * @return 最终任务的状态
     //     */
    private Integer processNotifyResult(PayNotifyTaskEntity task, Result<?> invokeResult, Throwable invokeException) {
        // 设置通用的更新 PayNotifyTaskEntity 的字段
        PayNotifyTaskEntity updateTask = PayNotifyTaskEntity.builder().id(task.getId()).lastExecuteTime(DateUtil.date())
                .notifyTimes(task.getNotifyTimes() + 1).build();
        String resultData = String.valueOf(invokeResult.getData());
        // 情况一：调用成功
        if (invokeResult != null && invokeResult.isSuccess() && StringUtils.isNotBlank(resultData) && "success".equals(resultData)) {
            updateTask.setStatus(PayNotifyStatusEnum.SUCCESS.getStatus());
            payNotifyTaskMapper.updateById(updateTask);
            return updateTask.getStatus();
        }
        // 情况二：调用失败、调用异常
        // 2.1 超过最大回调次数
        if (updateTask.getNotifyTimes() >= task.getMaxNotifyTimes()) {
            updateTask.setStatus(PayNotifyStatusEnum.FAILURE.getStatus());
            payNotifyTaskMapper.updateById(updateTask);
            return updateTask.getStatus();
        }
        // 2.2 未超过最大回调次数
        long offset = task.getNotifyFrequency().get(task.getNotifyTimes() - 1);
        updateTask.setNextNotifyTime(DateUtil.offsetSecond(task.getLastExecuteTime(), Integer.valueOf(String.valueOf(offset))));
        updateTask.setStatus(invokeException != null ? PayNotifyStatusEnum.REQUEST_FAILURE.getStatus()
                : PayNotifyStatusEnum.REQUEST_SUCCESS.getStatus());
        payNotifyTaskMapper.updateById(updateTask);
        return updateTask.getStatus();
    }

    private Map<String, Object> genOrderNotifyReq(PayNotifyTaskEntity task, PayOrderEntity order) {
        Map<String, Object> body = new HashMap<>(6);
        body.put("notifyId", task.getId());
        body.put("outOrderNo", order.getOutTradeNo());
        body.put("payStatus", order.getStatus());
        return body;
    }

    private Map<String, Object> genRefundNotifyReq(PayNotifyTaskEntity task, PayRefundEntity refund) {
        Map<String, Object> body = new HashMap<>(6);
        body.put("notifyId", task.getId());
        body.put("outOrderNo", refund.getOutTradeNo());
        body.put("outRefundNo", refund.getOutRefundNo());
        body.put("refundStatus", payRefundService.getStatus(refund.getStatus(), refund.getNotifyStatus()));
        return body;
    }

    private Result<?> notifyInvoke(String url, String body) {
        // 调用业务系统
        log.info("通知业务系统 url:{}, request:{}", url, body);
        String resp = HttpUtil.post(url, body);
        log.info("通知业务系统结果 response:{}", resp);
        return JSONUtil.toBean(resp, Result.class);
    }

    public static void main(String[] args) {
        Map<String, String> map =  new HashMap<>();
        map.put("abc", "abc");
    }

}
