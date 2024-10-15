package com.payment.data.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.framework.mybatis.core.pojo.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 支付应用
 * </p>
 *
 * @author sdy
 * @since 2024-04-10
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("pay_app")
public class PayAppEntity extends BaseEntity {

  /**
   * 主键编号
   */
  @TableId(value = "id")
  private Long id;

  /**
   * 支付 app 名称
   */
  private String name;

  /**
   * 状态 0-失效 1-有效
   */
  private Integer status;

  /**
   * 备注
   */
  private String remark;

  /**
   * 支付通知回调
   */
  private String orderNotifyUrl;

  /**
   * 退款通知回调
   */
  private String refundNotifyUrl;

}
