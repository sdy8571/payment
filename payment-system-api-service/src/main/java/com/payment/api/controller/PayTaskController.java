package com.payment.api.controller;

import com.framework.base.pojo.Result;
import com.framework.mybatis.core.pojo.PageResult;
import com.payment.domain.param.PagePayTaskReq;
import com.payment.domain.vo.PayTaskVo;
import com.payment.service.PayTaskService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author sdy
 * @description
 * @date 2024/10/12
 */
@RestController
@RequestMapping("/pay/task/")
public class PayTaskController {

    @Resource
    private PayTaskService payTaskService;

    @PostMapping("/page")
    public Result<PageResult<PayTaskVo>> page(@RequestBody PagePayTaskReq req) {
        return Result.success(payTaskService.page(req));
    }

}
