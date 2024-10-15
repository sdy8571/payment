package com.payment.domain.param;

import lombok.Data;

/**
 * @author sdy
 * @description
 * @date 2024/6/10
 */
@Data
public class GetPayRefundReq {

    /**
     * 交易流水号
     */
    private String serialNo;
    /**
     * 外部交易单号
     */
    private String outTradeNo;
    /**
     * 外部退款单号
     */
    private String outRefundNo;
    /**
     * 渠道交易单号
     */
    private String transactionId;
    /**
     * 渠道退款单号
     */
    private String refundId;

}
