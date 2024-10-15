package com.payment.domain.param;

import lombok.Data;

/**
 * @author sdy
 * @description
 * @date 2024/6/10
 */
@Data
public class PayRefundConfirmResp {

    /**
     * 申请编号
     */
    private Long id;
    /**
     * 流水号
     */
    private String serialNo;
    /**
     * 外部订单号
     */
    private String outTradeNo;
    /**
     * 外部退款单号
     */
    private String outRefundNo;
    /**
     * 退款状态
     */
    private Integer status;

}
