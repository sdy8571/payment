package com.payment.data.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.framework.mybatis.core.pojo.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * <p>
 * 支付渠道
 * </p>
 *
 * @author sdy
 * @since 2024-04-10
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("pay_channel")
public class PayChannelEntity extends BaseEntity {

  /**
   * 主键编号
   */
  @TableId(value = "id")
  private Long id;

  /**
   * 应用编号
   */
  private Long appId;

  /**
   * 支付渠道编号 唯一校验
   */
  private String code;

  /**
   * 状态 0-失效 1-有效
   */
  private Integer status;

  /**
   * 备注
   */
  private String remark;

  /**
   * 费率
   */
  private BigDecimal feeRate;

  /**
   * 渠道配置
   */
  private String config;

}
