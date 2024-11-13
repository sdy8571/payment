package com.payment.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author sdy
 * @description
 * @date 2024/11/13
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PayBaseVo {
    /**
     * 应用名称
     */
    private String appName;
    /**
     * 渠道名称
     */
    private String channelName;
}
