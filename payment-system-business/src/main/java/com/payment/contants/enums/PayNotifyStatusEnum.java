package com.payment.contants.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Objects;

/**
 * 支付通知状态枚举
 *
 * @author sdy
 */
@Getter
@AllArgsConstructor
public enum PayNotifyStatusEnum {

    WAITING(0, "等待通知"),
    SUCCESS(10, "通知成功"),
    FAILURE(20, "通知失败"), // 多次尝试，彻底失败
    REQUEST_SUCCESS(21, "请求成功，但是结果失败"),
    REQUEST_FAILURE(22, "请求失败"),

    ;

    /**
     * 状态
     */
    private final Integer status;
    /**
     * 名字
     */
    private final String name;

    /**
     * 判断是否通知结束
     *
     * @param status 状态
     * @return 是否支付关闭
     */
    public static boolean isEnd(Integer status) {
        return Arrays.asList(SUCCESS.getStatus(), FAILURE.getStatus()).contains(status);
    }

    public static boolean isWait(Integer status) {
        return Objects.equals(WAITING.getStatus(), status)
                || Objects.equals(REQUEST_SUCCESS.getStatus(), status)
                || Objects.equals(REQUEST_FAILURE.getStatus(), status);
    }

}
