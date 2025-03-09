package com.payment.api.controller.auth;

import cn.hutool.core.lang.UUID;
import com.framework.common.pojo.Result;
import com.payment.api.controller.auth.domain.LoginInfo;
import org.springframework.web.bind.annotation.*;

/**
 * @author sdy
 * @description
 * @date 2024/9/21
 */
@RestController
@RequestMapping("/auth")
public class AuthController {

    @PostMapping("login")
    public Result<String> login(@RequestBody LoginInfo loginInfo) {
        String uuid = UUID.randomUUID().toString();
        return Result.success(uuid);
    }
}
