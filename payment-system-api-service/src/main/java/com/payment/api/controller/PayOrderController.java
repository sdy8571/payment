package com.payment.api.controller;

import com.framework.base.annotation.NotRepeatSubmit;
import com.framework.base.pojo.Result;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;
import com.payment.domain.param.GetPayOrderReq;
import com.payment.domain.param.PayOrderUnifiedReq;
import com.payment.domain.param.PayOrderUnifiedResp;
import com.payment.domain.vo.PayOrderVo;
import com.payment.interceptor.domain.RequestHeaderContext;
import com.payment.service.PayOrderService;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.Map;

/**
 * <p>
 * 支付应用 交易控制
 * </p>
 *
 * @author sdy
 * @since 2024-04-10
 */
@RestController
@RequestMapping("/pay/order")
public class PayOrderController {

    @Resource
    private PayOrderService payOrderService;

    /**
     * 统一下单
     * @param req 请求入参
     * @return 返回结果
     */
    @NotRepeatSubmit
    @PostMapping("/unified")
    public Result<PayOrderUnifiedResp> placeAnOrder(@RequestBody @Valid PayOrderUnifiedReq req) {
        // 设置用户 ip
        if (StringUtils.isBlank(req.getUserIp())) {
            req.setUserIp(RequestHeaderContext.get().getRemoteAddr());
        }
        return Result.success(payOrderService.placeAnOrder(req));
    }

    /**
     * 渠道支付回调
     * @param payChannelId 支付渠道
     * @param params 回调参数
     * @param body 回调参数
     * @return 返回结果
     */
    @NotRepeatSubmit
    @PostMapping("/notify/{payChannelId}")
    public String orderNotify(@PathVariable("payChannelId") Long payChannelId,
                         @RequestParam(required = false) Map<String, String> params,
                         @RequestBody(required = false) String body) {
        return payOrderService.orderNotify(payChannelId, params, body);
    }

    /**
     * 查询支付订单
     * @param req 请求参数
     * @return 返回结果
     */
    @GetMapping("/get")
    public Result<PayOrderVo> getPayOrder(@RequestBody GetPayOrderReq req) {
        return Result.success(payOrderService.getPayOrder(req));
    }

}

