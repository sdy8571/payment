package com.payment.domain.vo;

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
@EqualsAndHashCode(callSuper = true)
public class PayRefundVo extends PayBaseVo {

  /**
   * 主键编号
   */
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
  private String payPriceY;
  /**
   * 退款金额
   */
  private Integer refundPrice;
  private String refundPriceY;
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
  private Integer refundStatus;
  /**
   * 创建人
   */
  private String createBy;
  /**
   * 创建时间
   */
  private Date createTime;
  /**
   * 更新人
   */
  private String updateBy;
  /**
   * 更新时间
   */
  private Date updateTime;

}
