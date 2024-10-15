package com.payment.test;

import cn.hutool.core.util.RandomUtil;
import com.payment.service.PaySerialService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author sdy
 * @description
 * @date 2024/5/30
 */
@Slf4j
public class PayOrderTest extends BaseTest {

    private static String app_uri_perfix = "/pay/order/";

    @Resource
    private PaySerialService paySerialService;

    @Test
    public void getSerialNo() {
        get(app_uri_perfix + "get-serial-no");
    }

    @Test
    public void orderUnified() {
        String serialNo = paySerialService.getSerialNo();
        String ourTradeNo = "OD" + RandomUtil.randomNumbers(15);
        // 请求参数
        Map<String, Object> params = new HashMap<>(6);
        params.put("serialNo", serialNo);
        params.put("payAppId", 2);
        params.put("payChannelCode", "mock");
        params.put("outTradeNo", ourTradeNo);
        params.put("subject", "测试支付");
        params.put("notifyUrl", "http://127.0.0.1:29007/pay/order/merchant/mock");
        params.put("price", 1);
        params.put("expireTime", new Date());
        Map<String, String> channelExtras = new HashMap<>(6);
        channelExtras.put("openId", "wx_29akdkloiwlsdjfkad_76");
        params.put("channelExtras", channelExtras);
        post(app_uri_perfix + "unified", params);
    }

    @Test
    public void orderNotify() {
        post(app_uri_perfix + "notify/4?channelOrderNo=MOCK-P-OD502426347155846&outTradeNo=OD502426347155846", null);
    }

}
