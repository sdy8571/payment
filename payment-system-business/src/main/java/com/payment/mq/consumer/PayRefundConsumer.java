package com.payment.mq.consumer;

import cn.hutool.json.JSONUtil;
import com.framework.mq.core.listener.AbstractRedissonDelayMessageListener;
import com.payment.mq.domain.PayRefundMessage;
import com.payment.notify.PayNotifyProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author sdy
 * @description
 * @date 2024/9/20
 */
@Slf4j
@Component
public class PayRefundConsumer extends AbstractRedissonDelayMessageListener<PayRefundMessage> {

    @Resource
    private PayNotifyProvider payNotifyProvider;

    @Override
    protected void onMessage(PayRefundMessage message) {
        log.info("===========【处理退款消息】开始===========");
        log.info("[onMessage][消息内容({})]", JSONUtil.toJsonStr(message));
        payNotifyProvider.onMessage(message.getNotifyTaskId());
        log.info("===========【处理退款消息】结束===========");
    }



}
