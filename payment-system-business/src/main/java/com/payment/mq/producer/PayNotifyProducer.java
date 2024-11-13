package com.payment.mq.producer;

import com.framework.mq.core.producer.RedissonDelayProducer;
import com.payment.async.AsyncConfig;
import com.payment.contants.enums.PayNotifyTypeEnum;
import com.payment.data.entity.PayNotifyTaskEntity;
import com.payment.mq.domain.AbstractPayNotifyMessage;
import com.payment.mq.domain.PayOrderMessage;
import com.payment.mq.domain.PayRefundMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author sdy
 * @description
 * @date 2024/9/13
 */
@Slf4j
@Component
public class PayNotifyProducer {

    @Resource
    private RedissonDelayProducer redissonDelayProducer;

    @Async(AsyncConfig.NOTIFY_THREAD_POOL_TASK_EXECUTOR)
    public void sendMsg(PayNotifyTaskEntity task) {
        AbstractPayNotifyMessage msg = create(PayNotifyTypeEnum.getByType(task.getType()));
        if (msg == null) {
            log.info("未匹配到通知消息类型直接返回");
            return;
        }
        // 设置参数
        msg.setNotifyTaskId(task.getId());
        msg.setDelayTime(task.getNotifyFrequency().get(task.getNotifyTimes()));
        redissonDelayProducer.send(msg);
        log.info("发送业务系统通知成功");
    }

    private AbstractPayNotifyMessage create(PayNotifyTypeEnum type) {
        AbstractPayNotifyMessage msg = null;
        switch (type) {
            case ORDER: msg = new PayOrderMessage();
                break;
            case REFUND: msg = new PayRefundMessage();
                break;
            default:
                break;
        }
        return msg;
    }

}
