package com.payment.test;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import com.payment.domain.param.ModifyPayAppReq;
import com.payment.domain.param.PagePayAppReq;

import java.util.HashMap;
import java.util.Map;

/**
 * @author sdy
 * @description
 * @date 2024/5/30
 */
@Slf4j
public class PayAppTest extends BaseTest {

    private static String app_uri_perfix = "/pay/app/";

    @Test
    public void page() {
        PagePayAppReq req = new PagePayAppReq();
        req.setPageNo(1);
        req.setPageSize(10);
        Map<String, Object> header = new HashMap<>();
        header.put("shopId", "189");
        post(app_uri_perfix + "page", req, header);
    }

    @Test
    public void detail() {
        get(app_uri_perfix + "detail/1");
    }

    @Test
    public void create() {
        ModifyPayAppReq req = new ModifyPayAppReq();
        req.setName("时汇客");
        req.setOrderNotifyUrl("https://test.jxzpg.com/app/order/callback/");
        req.setRefundNotifyUrl("https://test.jxzpg.com/app/refund/callback/");
        req.setRemark("测试环境");
        post(app_uri_perfix + "create", req);
    }

    @Test
    public void edit() {
        ModifyPayAppReq req = new ModifyPayAppReq();
        req.setId(1L);
        req.setName("时汇客");
        req.setOrderNotifyUrl("https://test.jxzpg.com/app/order/callback/1");
        req.setRefundNotifyUrl("https://test.jxzpg.com/app/refund/callback/1");
        req.setRemark("测试环境");
        post(app_uri_perfix + "edit", req);
    }

    @Test
    public void delete() {
        ModifyPayAppReq req = new ModifyPayAppReq();
        req.setId(1L);
        post(app_uri_perfix + "delete", req);
    }

}
