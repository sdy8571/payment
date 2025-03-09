package com.payment.api.controller;

import com.framework.common.pojo.Result;
import com.payment.service.PaySerialService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * <p>
 * 支付应用 交易控制
 * </p>
 *
 * @author sdy
 * @since 2024-04-10
 */
@RestController
@RequestMapping("/pay/serial")
public class PaySerialController {

    @Resource
    private PaySerialService paySerialService;

    @GetMapping("/get-serial-no")
    public Result<String> getSerialNo() {
        return Result.success(paySerialService.getSerialNo());
    }

}

