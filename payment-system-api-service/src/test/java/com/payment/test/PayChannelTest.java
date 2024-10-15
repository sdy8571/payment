package com.payment.test;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import com.payment.domain.param.ModifyPayChannelReq;
import com.payment.domain.param.PagePayChannelReq;

import java.math.BigDecimal;

/**
 * @author sdy
 * @description
 * @date 2024/5/30
 */
@Slf4j
public class PayChannelTest extends BaseTest {

    private static String app_uri_perfix = "/pay/channel/";

    @Test
    public void page() {
        PagePayChannelReq req = new PagePayChannelReq();
        req.setPageNo(1);
        req.setPageSize(10);
        post(app_uri_perfix + "page", req);
    }

    @Test
    public void detail() {
        get(app_uri_perfix + "detail/1");
    }

    @Test
    public void create() {
        ModifyPayChannelReq req = new ModifyPayChannelReq();
        req.setAppId(1L);
        req.setCode("wx_pub");
        req.setConfig("{abc: \"1234\"}");
        req.setStatus(1);
        req.setRemark("测试添加");
        req.setFeeRate(new BigDecimal(0.05));
        post(app_uri_perfix + "create", req);
    }

    @Test
    public void edit() {
        ModifyPayChannelReq req = new ModifyPayChannelReq();
        req.setId(2L);
        req.setAppId(1L);
        req.setCode("wx_pub");
        req.setConfig("{abc: \"12345\"}");
        req.setStatus(1);
        req.setRemark("测试添加");
        req.setFeeRate(new BigDecimal(0.06));
        post(app_uri_perfix + "edit", req);
    }

    @Test
    public void delete() {
        ModifyPayChannelReq req = new ModifyPayChannelReq();
        req.setId(1L);
        post(app_uri_perfix + "delete", req);
    }

    @Test
    public void getTypeList() {
        get(app_uri_perfix + "type-list");
    }

}
