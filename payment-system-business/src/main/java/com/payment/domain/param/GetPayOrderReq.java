package com.payment.domain.param;

import lombok.Data;

/**
 * @author sdy
 * @description
 * @date 2024/6/10
 */
@Data
public class GetPayOrderReq {

    /**
     * 流水号
     */
    private String serialNo;
    /**
     * 外部订单号
     */
    private String outTradeNo;
    /**
     * 渠道交易单号
     */
    private String transactionId;

}
