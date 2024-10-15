package com.payment.contants.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Objects;

/**
 * 支付订单的状态枚举
 *
 * @author sdy
 */
@Getter
@AllArgsConstructor
public enum PayOrderStatusEnum {

    WAITING(0, "未支付"),
    SUCCESS(10, "支付成功"),
    REFUND(20, "已退款"),
    CLOSED(30, "支付关闭"), // 注意：全部退款后，还是 REFUND 状态
    FAILURE(40, "支付失败"),
    ;

    private final Integer status;
    private final String name;

    /**
     * 判断是否支付成功
     *
     * @param status 状态
     * @return 是否支付成功
     */
    public static boolean isSuccess(Integer status) {
        return Objects.equals(status, SUCCESS.getStatus());
    }

    /**
     * 判断是否支付成功或者已退款
     *
     * @param status 状态
     * @return 是否支付成功或者已退款
     */
    public static boolean isSuccessOrRefund(Integer status) {
        return Arrays.asList(SUCCESS.getStatus(), REFUND.getStatus()).contains(status);
    }

    /**
     * 判断是否支付关闭
     *
     * @param status 状态
     * @return 是否支付关闭
     */
    public static boolean isClosed(Integer status) {
        return Objects.equals(status, CLOSED.getStatus());
    }

    /**
     * 判断是否支付关闭
     *
     * @param status 状态
     * @return 是否支付关闭
     */
    public static boolean isFailure(Integer status) {
        return Objects.equals(status, FAILURE.getStatus());
    }

}
