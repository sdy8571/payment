package com.payment.api.controller;


import com.framework.base.pojo.Result;
import com.framework.mybatis.core.pojo.PageResult;
import org.springframework.web.bind.annotation.*;
import com.payment.domain.param.ModifyPayAppReq;
import com.payment.domain.param.PagePayAppReq;
import com.payment.domain.vo.PayAppVo;
import com.payment.service.PayAppService;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 支付应用 前端控制器
 * </p>
 *
 * @author sdy
 * @since 2024-04-10
 */
@RestController
@RequestMapping("/pay/app/")
public class PayAppController {

    @Resource
    private PayAppService payAppService;

    @PostMapping("page")
    public Result<PageResult<PayAppVo>> page(@RequestBody PagePayAppReq req) {
        return Result.success(payAppService.page(req));
    }

    @GetMapping("detail/{id}")
    public Result<PayAppVo> detail(@PathVariable("id") Long id) {
        return Result.success(payAppService.detail(id));
    }

    @PostMapping("create")
    public Result<Long> create(@RequestBody ModifyPayAppReq req) {
        return Result.success(payAppService.create(req));
    }

    @PostMapping("edit")
    public Result<Long> edit(@RequestBody ModifyPayAppReq req) {
        return Result.success(payAppService.update(req));
    }

    @PostMapping("delete/{id}")
    public Result<Boolean> delete(@PathVariable("id") Long id) {
        payAppService.delete(id);
        return Result.success(true);
    }

    @GetMapping("getAllList")
    public Result<List<PayAppVo>> getAllMap() {
        return Result.success(payAppService.getAllList());
    }

}

