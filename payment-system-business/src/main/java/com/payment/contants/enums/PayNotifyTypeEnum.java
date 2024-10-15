package com.payment.contants.enums;

import cn.hutool.core.util.ArrayUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 支付通知类型
 *
 * @author sdy
 */
@Getter
@AllArgsConstructor
public enum PayNotifyTypeEnum {

    ORDER(1, "支付单"),
    REFUND(2, "退款单"),
    ;

    /**
     * 类型
     */
    private final Integer type;
    /**
     * 名字
     */
    private final String name;

    /**
     * 根据type获取name
     * @param type 类型
     * @return 返回名称
     */
    public static String getNameByType(Integer type) {
        PayNotifyTypeEnum typeEnum = getByType(type);
        if (typeEnum == null) {
            return "";
        }
        return typeEnum.getName();
    }

    /**
     * 根据type获取对象
     * @param type 类型
     * @return 返回对象
     */
    public static PayNotifyTypeEnum getByType(Integer type) {
        return ArrayUtil.firstMatch(o -> o.getType().equals(type), values());
    }

}
