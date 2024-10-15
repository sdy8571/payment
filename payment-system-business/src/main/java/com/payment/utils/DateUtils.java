package com.payment.utils;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;

import java.util.Date;

/**
 * @author sdy
 * @description
 * @date 2024/10/15
 */
public class DateUtils {

    public static DateTime offsetSecond(Date date, Integer offset) {
        return DateUtil.offsetSecond(date, Integer.parseInt(String.valueOf(offset)));
    }

}
