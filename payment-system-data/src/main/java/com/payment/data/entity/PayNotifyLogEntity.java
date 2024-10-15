package com.payment.data.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.framework.mybatis.core.pojo.BaseEntity;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 商户支付、退款等的通知 Log
 * 每次通知时，都会在该表中，记录一次 Log，方便排查问题
 *
 * @author sdy
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("pay_notify_log")
@Builder
public class PayNotifyLogEntity extends BaseEntity {
    /**
     * 日志编号，自增
     */
    private Long id;
    /**
     * 通知任务编号
     *
     * 关联 {@link PayNotifyTaskEntity#getId()}
     */
    private Long taskId;
    /**
     * 第几次被通知
     *
     * 对应到 {@link PayNotifyTaskEntity#getNotifyTimes()}
     */
    private Integer notifyTimes;
    /**
     * HTTP 响应结果
     */
    private String response;
    /**
     * 支付通知状态
     */
    private Integer status;

}
