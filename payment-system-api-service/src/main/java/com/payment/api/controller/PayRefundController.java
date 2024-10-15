package com.payment.api.controller;

import com.framework.base.annotation.NotRepeatSubmit;
import com.framework.base.pojo.Result;
import org.springframework.web.bind.annotation.*;
import com.payment.domain.param.GetPayRefundReq;
import com.payment.domain.param.PayRefundAppleReq;
import com.payment.domain.param.PayRefundConfirmResp;
import com.payment.domain.vo.PayRefundVo;
import com.payment.service.PayRefundService;

import javax.annotation.Resource;
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
@RequestMapping("/pay/refund")
public class PayRefundController {

    @Resource
    private PayRefundService payRefundService;

    @NotRepeatSubmit
    @PostMapping("/apply")
    public Result<Long> applyRefund(@RequestBody PayRefundAppleReq req) {
        return Result.success(payRefundService.applyRefund(req));
    }

    @NotRepeatSubmit
    @PostMapping("/confirm/{id}")
    public Result<PayRefundConfirmResp> confirmRefund(@PathVariable("id") Long id) {
        return Result.success(payRefundService.confirmRefund(id));
    }

    @NotRepeatSubmit
    @PostMapping("/notify/{payChannelId}")
    public String orderNotify(@PathVariable("payChannelId") Long payChannelId,
                         @RequestParam(required = false) Map<String, String> params,
                         @RequestBody(required = false) String body) {
        return payRefundService.refundNotify(payChannelId, params, body);
    }

    /**
     * 查询退款
     * @param req 请求参数
     * @return 返回结果
     */
    @GetMapping("/get")
    public Result<PayRefundVo> getPayRefund(@RequestBody GetPayRefundReq req) {
        return Result.success(payRefundService.getPayRefund(req));
    }

}

