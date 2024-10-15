package com.payment.data.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.framework.mybatis.core.pojo.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

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
@EqualsAndHashCode(callSuper = false)
@TableName("pay_order")
public class PayOrderEntity extends BaseEntity {

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
   * 支付渠道编号
   */
  private Long channelId;
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
   * 通知地址
   */
  private String notifyUrl;
  /**
   * 通知地址 支付宝使用
   */
  private String returnUrl;
  /**
   * 支付金额
   */
  private Integer price;
  /**
   * 过期时间
   */
  private Date expireTime;
  /**
   * 扩展字段
   */
  private String channelExtras;
  /**
   * 成功时间
   */
  private Date successTime;
  /**
   * 通知时间
   */
  private Date notifyTime;
  /**
   * 支付状态
   */
  private Integer status;
  /**
   * 备注
   */
  private String remark;

}
