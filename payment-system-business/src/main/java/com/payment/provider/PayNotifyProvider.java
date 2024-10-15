package com.payment.provider;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.exceptions.ExceptionUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.framework.base.pojo.Result;
import com.payment.contants.enums.PayNotifyStatusEnum;
import com.payment.data.entity.PayNotifyLogEntity;
import com.payment.data.entity.PayNotifyTaskEntity;
import com.payment.data.mapper.PayNotifyLogMapper;
import com.payment.data.mapper.PayNotifyTaskMapper;
import com.payment.mq.producer.PayNotifyProducer;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @author sdy
 * @description
 * @date 2024/9/20
 */
@Slf4j
@Component
public class PayNotifyProvider {

    @Resource
    private PayNotifyTaskMapper payNotifyTaskMapper;
    @Resource
    private PayNotifyLogMapper payNotifyLogMapper;
    @Resource
    private PayNotifyProducer payNotifyProducer;

    public void onMessage(Long taskId) {
        // 查询任务
        PayNotifyTaskEntity task = getNotifyTask(taskId);
        if (task == null) {
            return;
        }
        // 通知业务系统
        boolean bool = notify(task);
        if (bool) {
            log.info("通知成功，结束消息发送");
            return;
        }
        // 通知失败，重新发送消息
        payNotifyProducer.sendMsg(task);
    }

    private boolean notify(PayNotifyTaskEntity task) {
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
        return PayNotifyStatusEnum.isEnd(result);
    }

    private PayNotifyTaskEntity getNotifyTask(Long taskId) {
        // 查询任务
        PayNotifyTaskEntity task = payNotifyTaskMapper.selectById(taskId);
        // 任务为空，或者任务不是待通知状态，直接返回空值
        if (task == null || !PayNotifyStatusEnum.isWait(task.getStatus())) {
            log.info("任务不存在，或非进行中任务，直接退出通知");
            return null;
        }
        return task;
    }


    private Result<?> notifyInvoke(String url, String body) {
        // 调用业务系统
        log.info("通知业务系统 url:{}, request:{}", url, body);
        String resp = HttpUtil.post(url, body);
        log.info("通知业务系统结果 response:{}", resp);
        return JSONUtil.toBean(resp, Result.class);
    }

    /**
     //     * 处理并更新通知结果
     //     * @param task 通知任务
     //     * @param invokeResult 通知结果
     //     * @param invokeException 通知异常
     //     * @return 最终任务的状态
     //     */
    private Integer processNotifyResult(PayNotifyTaskEntity task, Result<?> invokeResult, Throwable invokeException) {
        // 通知次数
        Integer nextNotifyTimes = task.getNotifyTimes() + 1;
        // 设置通用的更新 PayNotifyTaskEntity 的字段
        PayNotifyTaskEntity updateTask = PayNotifyTaskEntity.builder().id(task.getId()).lastExecuteTime(DateUtil.date())
                .notifyTimes(nextNotifyTimes).build();
        String resultData = String.valueOf(invokeResult.getData());
        // 情况一：调用成功
        if (invokeResult != null && invokeResult.isSuccess() && StringUtils.isNotBlank(resultData) && "success".equals(resultData)) {
            updateTask.setStatus(PayNotifyStatusEnum.SUCCESS.getStatus());
            payNotifyTaskMapper.updateById(updateTask);
            return updateTask.getStatus();
        }
        // 情况二：调用失败、调用异常
        // 2.1 超过最大回调次数
        if (nextNotifyTimes > task.getMaxNotifyTimes()) {
            updateTask.setStatus(PayNotifyStatusEnum.FAILURE.getStatus());
            payNotifyTaskMapper.updateById(updateTask);
            return updateTask.getStatus();
        }
        // 2.2 未超过最大回调次数
        Integer offset = task.getNotifyFrequency().get(nextNotifyTimes - 1);
        updateTask.setNextNotifyTime(DateUtil.offsetSecond(new Date(), offset));
        updateTask.setStatus(invokeException != null ? PayNotifyStatusEnum.REQUEST_FAILURE.getStatus()
                : PayNotifyStatusEnum.REQUEST_SUCCESS.getStatus());
        payNotifyTaskMapper.updateById(updateTask);
        return updateTask.getStatus();
    }

}
