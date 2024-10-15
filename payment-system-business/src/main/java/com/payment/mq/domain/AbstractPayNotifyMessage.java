package com.payment.mq.domain;

import com.framework.mq.core.message.AbstractDelayTaskMessage;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author sdy
 * @description
 * @date 2024/10/14
 */
@Data
@EqualsAndHashCode(callSuper = true)
public abstract class AbstractPayNotifyMessage extends AbstractDelayTaskMessage {

    private Long notifyTaskId;

    protected abstract String getType();

}
