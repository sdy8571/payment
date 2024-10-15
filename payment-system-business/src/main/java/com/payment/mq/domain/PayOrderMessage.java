package com.payment.mq.domain;

import com.framework.mq.core.message.AbstractDelayTaskMessage;
import com.payment.contants.enums.PayNotifyTypeEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author sdy
 * @description
 * @date 2024/9/13
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class PayOrderMessage extends AbstractPayNotifyMessage {

    @Override
    protected String getType() {
        return PayNotifyTypeEnum.ORDER.getName();
    }

    @Override
    public String getQueueName() {
        return "pay.order.notify";
    }

}
