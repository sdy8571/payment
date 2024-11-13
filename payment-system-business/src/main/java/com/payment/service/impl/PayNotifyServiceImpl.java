package com.payment.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.json.JSONUtil;
import com.payment.async.AsyncConfig;
import com.payment.contants.enums.PayNotifyStatusEnum;
import com.payment.data.entity.PayNotifyTaskEntity;
import com.payment.data.mapper.PayNotifyTaskMapper;
import com.payment.mq.producer.PayNotifyProducer;
import com.payment.service.PayNotifyService;
import com.payment.utils.PaymentUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;


/**
 * 支付通知 Core Service 实现类
 *
 * @author sdy
 */
@Service
@Valid
@Slf4j
public class PayNotifyServiceImpl implements PayNotifyService {

    @Resource
    private PayNotifyTaskMapper payNotifyTaskMapper;
    @Resource
    private PayNotifyProducer payNotifyProducer;

    @Override
    public void saveOrder(Integer type, Long dataId, Long channelId, String outOrderNo, String notifyUrl, Integer status) {
        // 创建通知任务
        PayNotifyTaskEntity task = PayNotifyTaskEntity.builder().type(type).dataId(dataId).channelId(channelId)
                .merchantOrderId(outOrderNo).notifyUrl(notifyUrl)
                .status(PayNotifyStatusEnum.WAITING.getStatus())
                .nextNotifyTime(DateUtil.date()).notifyTimes(1).maxNotifyTimes(AsyncConfig.NOTIFY_FREQUENCY.size())
                .notifyFrequency(AsyncConfig.NOTIFY_FREQUENCY)
                .notifyBody(genOrderNotifyReq(outOrderNo, status))
                .build();
        // 保存并发送通知
        saveAndSend(task);
    }

    @Override
    public void saveRefund(Integer type, Long dataId, Long channelId, String outOrderNo, String outRefundNo, String notifyUrl, Integer status, Integer notifyStatus) {
        // 创建通知任务
        PayNotifyTaskEntity task = PayNotifyTaskEntity.builder().type(type).dataId(dataId).channelId(channelId)
                .merchantOrderId(outOrderNo).merchantRefundId(outRefundNo).notifyUrl(notifyUrl)
                .status(PayNotifyStatusEnum.WAITING.getStatus())
                .nextNotifyTime(DateUtil.date()).notifyTimes(1).maxNotifyTimes(AsyncConfig.NOTIFY_FREQUENCY.size())
                .notifyFrequency(AsyncConfig.NOTIFY_FREQUENCY)
                .notifyBody(genRefundNotifyReq(outOrderNo, outRefundNo, status, notifyStatus))
                .build();
        // 保存并发送通知
        saveAndSend(task);
    }

    private void saveAndSend(PayNotifyTaskEntity task) {
        // 执行插入
        payNotifyTaskMapper.insert(task);
        // 推送通知消息
        payNotifyProducer.sendMsg(task);
    }

    private String genOrderNotifyReq(String outOrderNo, Integer status) {
        Map<String, Object> body = new HashMap<>(6);
        body.put("outOrderNo", outOrderNo);
        body.put("payStatus", status);
        return JSONUtil.toJsonStr(body);
    }

    private String genRefundNotifyReq(String outOrderNo, String outRefundNo, Integer status, Integer notifyStatus) {
        Map<String, Object> body = new HashMap<>(6);
        body.put("outOrderNo", outOrderNo);
        body.put("outRefundNo", outRefundNo);
        body.put("refundStatus", PaymentUtils.getStatus(status, notifyStatus));
        return JSONUtil.toJsonStr(body);
    }

}
