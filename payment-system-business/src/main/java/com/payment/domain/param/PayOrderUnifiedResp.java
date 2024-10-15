package com.payment.domain.param;

import lombok.Data;

/**
 * @author sdy
 * @description
 * @date 2024/6/10
 */
@Data
public class PayOrderUnifiedResp {

    private Long id;
    private String outTradeNo;
    private String appId;
    private String timeStamp;
    private String nonceStr;
    private String packageValue;
    private String signType;
    private String paySign;

}
