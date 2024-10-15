package com.payment.domain.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author sdy
 * @description
 * @date 2024/5/30
 */
@Data
public class PayChannelVo {

    private Long id;
    private Long appId;
    private String appName;
    private String code;
    private String channelName;
    private Integer status;
    private String remark;
    private BigDecimal feeRate;
    private String config;
    private String createBy;
    private Date createTime;
    private String updateBy;
    private Date updateTime;

}
