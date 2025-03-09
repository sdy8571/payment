package com.payment.api.netty;

import com.framework.common.pojo.Result;
import com.framework.socket.core.domain.BaseSocketReq;
import com.framework.socket.core.service.MsgHandleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author sdy
 * @description
 * @date 2024/12/20
 */
@Slf4j
@Service
public class BusinessHandler implements MsgHandleService {

    @Override
    public int getCode() {
        return 10000;
    }

    @Override
    public Result<String> handle(BaseSocketReq req) {
        // 返回结果
        return Result.success("收到客户端消息：" + req.getData() + "，并响应客户端结果成功");
    }

}
