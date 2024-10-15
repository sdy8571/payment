package com.payment.data.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.framework.mybatis.core.pojo.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 通知记录
 * </p>
 *
 * @author sdy
 * @since 2024-04-10
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("pay_channel_notify")
public class PayChannelNotifyEntity extends BaseEntity {

  /**
   * 主键编号
   */
  @TableId(value = "id")
  private Long id;
  /**
   * 渠道 wx-微信 ali-阿里
   */
  private String channel;
  /**
   * 通知编号
   */
  private String notifyId;
  /**
   * 通知类型 1-支付 2-退款
   */
  private Integer notifyType;
  /**
   * 通知参数
   */
  private String notifyRequestParams;
  /**
   * 通知参数
   */
  private String notifyRequest;
  /**
   * 通知结果
   */
  private String notifyResult;
  /**
   * 状态 0-失败 1-成功
   */
  private Integer status;

}
