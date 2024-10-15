package com.payment.domain.param;

import lombok.Data;
import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * @author sdy
 * @description
 * @date 2024/6/10
 */
@Data
public class PayRefundAppleReq {

    /**
     * 流水号
     */
    @NotEmpty(message = "交易流水号")
    private String serialNo;
    /**
     * 外部订单号
     */
    @NotEmpty(message = "外部订单编号不能为空")
    private String outTradeNo;
    /**
     * 外部退款单号
     */
    @NotEmpty(message = "退款请求单号不能为空")
    private String outRefundNo;
    /**
     * 退款原因
     */
    @NotEmpty(message = "退款原因不能为空")
    private String reason;
    /**
     * 支付金额
     */
    @NotNull(message = "支付金额不能为空")
    @DecimalMin(value = "0", inclusive = false, message = "支付金额必须大于零")
    private Integer payPrice;
    /**
     * 退款金额
     */
    @NotNull(message = "退款金额不能为空")
    @DecimalMin(value = "0", inclusive = false, message = "支付金额必须大于零")
    private Integer refundPrice;
    /**
     * 支付结果的 notify 回调地址
     */
    @NotEmpty(message = "支付结果的回调地址不能为空")
    @URL(message = "支付结果的 notify 回调地址必须是 URL 格式")
    private String notifyUrl;

}
