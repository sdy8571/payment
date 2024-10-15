package com.payment.domain.param;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * @author sdy
 * @description
 * @date 2024/5/30
 */
@Data
public class ModifyPayChannelReq {

    private Long id;
    @NotNull(message = "应用编号不能为空")
    private Long appId;
    @NotNull(message = "渠道编码不能为空")
    private String code;
    @NotBlank(message = "渠道配置不能为空")
    private String config;
    @NotNull(message = "开启状态不能为空")
    private Integer status;
    private String remark;
    private BigDecimal feeRate;

}
