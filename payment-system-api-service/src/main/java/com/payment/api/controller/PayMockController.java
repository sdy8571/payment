package com.payment.api.controller;

import cn.hutool.json.JSONUtil;
import com.framework.common.pojo.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * <p>
 * 支付应用 交易控制
 * </p>
 *
 * @author sdy
 * @since 2024-04-10
 */
@Slf4j
@RestController
@RequestMapping("/pay/mock")
public class PayMockController {

    /**
     * 测试模拟商家端回调服务
     * @param params 入参
     * @return 返回结果
     */
    @PostMapping("/merchant")
    public Result<String> orderNotifyMockMer(@RequestBody Map<String, String> params) {
        log.info("mock request params : {}", JSONUtil.toJsonStr(params));
        return Result.success("success");
    }

}

