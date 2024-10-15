package com.payment.data.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.framework.mybatis.core.pojo.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * <p>
 * 退款订单
 * </p>
 *
 * @author sdy
 * @since 2024-04-10
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("pay_refund")
public class PayRefundEntity extends BaseEntity {

  /**
   * 主键编号
   */
  @TableId(value = "id")
  private Long id;
  /**
   * 交易流水号
   */
  private String serialNo;
  /**
   * 渠道编号
   */
  private Long channelId;
  /**
   * 外部交易单号
   */
  private String outTradeNo;
  /**
   * 外部退款单号
   */
  private String outRefundNo;
  /**
   * 渠道交易单号
   */
  private String transactionId;
  /**
   * 渠道退款单号
   */
  private String refundId;
  /**
   * 订单支付金额
   */
  private Integer payPrice;
  /**
   * 退款金额
   */
  private Integer refundPrice;
  /**
   * 退款原因
   */
  private String reason;
  /**
   * 退款通知地址
   */
  private String notifyUrl;
  /**
   * 成功时间
   */
  private Date successTime;
  /**
   * 通知时间
   */
  private Date notifyTime;
  /**
   * 退款状态
   */
  private Integer status;
  /**
   * 退款通知状态
   */
  private Integer notifyStatus;

}
