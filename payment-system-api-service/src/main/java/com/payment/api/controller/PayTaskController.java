package com.payment.api.controller;

import cn.hutool.json.JSONUtil;
import com.framework.common.pojo.Result;
import com.framework.mybatis.core.pojo.PageResult;
import com.framework.socket.core.handle.PushMessageHandler;
import com.payment.domain.param.PagePayTaskReq;
import com.payment.domain.vo.PayTaskVo;
import com.payment.service.PayTaskService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

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

    @GetMapping("/push")
    public Result<String> push() {
        // 推送消息
        Map<String, Object> map = new HashMap<>();
        map.put("push", "客户端推送的消息");
        PushMessageHandler.push("1234", map);
        return Result.success("success");
    }

}
