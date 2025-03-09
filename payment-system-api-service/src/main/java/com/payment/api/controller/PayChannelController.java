package com.payment.api.controller;

import com.framework.common.pojo.Result;
import com.framework.mybatis.core.pojo.PageResult;
import org.springframework.web.bind.annotation.*;
import com.payment.domain.param.ModifyPayChannelReq;
import com.payment.domain.param.PagePayChannelReq;
import com.payment.domain.vo.PayChannelTypeVo;
import com.payment.domain.vo.PayChannelVo;
import com.payment.service.PayChannelService;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 支付渠道 前端控制器
 * </p>
 *
 * @author sdy
 * @since 2024-04-10
 */
@RestController
@RequestMapping("/pay/channel")
public class PayChannelController {

    @Resource
    private PayChannelService payChannelService;

    @PostMapping("/page")
    public Result<PageResult<PayChannelVo>> page(@RequestBody PagePayChannelReq req) {
        return Result.success(payChannelService.page(req));
    }

    @GetMapping("/detail/{id}")
    public Result<PayChannelVo> detail(@PathVariable("id") Long id) {
        return Result.success(payChannelService.detail(id));
    }

    @PostMapping("/create")
    public Result<Long> create(@RequestBody ModifyPayChannelReq req) {
        return Result.success(payChannelService.create(req));
    }

    @PostMapping("/edit")
    public Result<Long> edit(@RequestBody ModifyPayChannelReq req) {
        return Result.success(payChannelService.update(req));
    }

    @PostMapping("/delete/{id}")
    public Result<Boolean> delete(@PathVariable("id") Long id) {
        payChannelService.delete(id);
        return Result.success(true);
    }

    @GetMapping("/type-list")
    public Result<List<PayChannelTypeVo>> typeList() {
        return Result.success(payChannelService.getTypeList());
    }

    @PostMapping("/status/{id}")
    public Result<Boolean> status(@PathVariable("id") Long id) {
        payChannelService.status(id);
        return Result.success(true);
    }

    @GetMapping("/list")
    public Result<List<PayChannelVo>> getByApp(@RequestParam Long appId) {
        return Result.success(payChannelService.getByApp(appId));
    }

}

