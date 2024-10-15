package com.payment.test;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureJdbc;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import com.payment.PaymentApplication;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@SpringBootTest(classes = {PaymentApplication.class})
@AutoConfigureMockMvc
@AutoConfigureJdbc
@ActiveProfiles("dbg")
public class BaseTest {

    @Autowired
    private WebApplicationContext wac;

    public void get(String uri) {
        get(uri, null);
    }

    public void get(String uri, Map<String, Object> headers) {
        exec("get", uri, null, headers);
    }

    public void post(String uri, Object data) {
        post(uri, data, null);
    }

    public void post(String uri, Object data, Map<String, Object> headers) {
        exec("post", uri, data, headers);
    }

    public void put(String uri, Object data) {
        put(uri, data, null);
    }

    public void put(String uri, Object data, Map<String, Object> headers) {
        exec("put", uri, data, headers);
    }

    public void delete(String uri, Object body) {
        delete(uri, body, null);
    }

    public void delete(String uri, Object body, Map<String, Object> headers) {
        exec("delete", uri, body, headers);
    }

    public Map<String, String> getParams(boolean isPage) {
        Map<String, String> params = new HashMap<>(6);
        if (isPage) {
            params.put("pageNo", "1");
            params.put("pageSize", "10");
        }
        return params;
    }

    public void exec(String method, String uri, Object body, Map<String, Object> headers) {
        try {
            MockHttpServletRequestBuilder request;
            switch (method) {
                case "post":
                    request = MockMvcRequestBuilders.post(uri);
                    break;
                case "get":
                    request = MockMvcRequestBuilders.get(uri);
                    break;
                case "put":
                    request = MockMvcRequestBuilders.put(uri);
                    break;
                case "delete":
                    request = MockMvcRequestBuilders.delete(uri);
                    break;
                default:
                    return;
            }

            if (headers != null) {
                headers.forEach(request::header);
            }

            request.contentType(MediaType.APPLICATION_JSON);
            request.content(JSON.toJSONString(body));

            MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();

            ResultActions perform = mockMvc.perform(request);

            perform.andExpect(MockMvcResultMatchers.status().isOk());

            MvcResult mvcResult = perform.andReturn();
            MockHttpServletResponse response = mvcResult.getResponse();
            response.setCharacterEncoding("UTF-8");
            log.info("=============response===============");
            log.info(response.getContentAsString());
            log.info("=============response===============");
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
        }
    }

}