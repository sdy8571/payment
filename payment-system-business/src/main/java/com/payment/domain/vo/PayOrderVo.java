package com.payment.domain.vo;

import lombok.Data;

import java.util.Date;

/**
 * <p>
 * 交易订单
 * </p>
 *
 * @author sdy
 * @since 2024-04-10
 */
@Data
public class PayOrderVo {

  /**
   * 主键编号
   */
  private Long id;
  /**
   * 交易流水号
   */
  private String serialNo;
  /**
   * 外部交易单号
   */
  private String outTradeNo;
  /**
   * 渠道交易单号
   */
  private String transactionId;
  /**
   * 交易描述
   */
  private String subject;
  /**
   * 附加数据
   */
  private String body;
  /**
   * 支付金额
   */
  private Integer price;
  /**
   * 成功时间
   */
  private Date successTime;
  /**
   * 支付状态
   */
  private Integer status;
  /**
   * 备注
   */
  private String remark;
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
