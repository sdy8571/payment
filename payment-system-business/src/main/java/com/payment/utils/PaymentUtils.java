package com.payment.utils;

import org.apache.commons.lang3.StringUtils;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

/**
 * @author sdy
 * @description
 * @date 2024/6/11
 */
public class PaymentUtils {

    public static String formatUrl(String url) {
        if (StringUtils.isBlank(url)) {
            return "/";
        }
        if (url.endsWith("/")) {
            return url;
        }
        return url + "/";
    }

    public static LocalDateTime format2Date(Date date) {
        if (date == null) {
            return null;
        }
        Instant instant = date.toInstant();
        return instant.atZone(ZoneId.systemDefault()).toLocalDateTime();
    }
}
