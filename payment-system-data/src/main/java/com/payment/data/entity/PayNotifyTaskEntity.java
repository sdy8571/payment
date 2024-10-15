package com.payment.data.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.framework.mybatis.core.pojo.BaseEntity;
import lombok.*;

import java.util.Date;
import java.util.List;

/**
 * 支付通知
 * 在支付系统收到支付渠道的支付、退款的结果后，需要不断的通知到业务系统，直到成功。
 *
 * @author 芋道源码
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
@TableName(value = "pay_notify_task", autoResultMap = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PayNotifyTaskEntity extends BaseEntity {

    /**
     * 编号，自增
     */
    @TableId
    private Long id;
    /**
     * 应用编号
     */
    private Long channelId;
    /**
     * 通知类型
     */
    private Integer type;
    /**
     * 数据编号，根据不同 type 进行关联：
     */
    private Long dataId;
    /**
     * 商户订单编号
     */
    private String merchantOrderId;
    /**
     * 商户退款编号
     */
    private String merchantRefundId;
    /**
     * 通知状态
     */
    private Integer status;
    /**
     * 下一次通知时间
     */
    private Date nextNotifyTime;
    /**
     * 最后一次执行时间
     */
    private Date lastExecuteTime;
    /**
     * 当前通知次数
     */
    private Integer notifyTimes;
    /**
     * 最大可通知次数
     */
    private Integer maxNotifyTimes;
    /**
     * 通知频率
     */
    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<Integer> notifyFrequency;
    /**
     * 通知地址
     */
    private String notifyUrl;
    /**
     * 通知内容
     */
    private String notifyBody;

}
