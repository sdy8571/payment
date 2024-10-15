package com.payment.service.impl;

import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import com.payment.contants.enums.PayNotifyStatusEnum;
import com.payment.data.entity.PayChannelNotifyEntity;
import com.payment.data.mapper.PayChannelNotifyMapper;
import com.payment.service.PayNotifyChannelService;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @author sdy
 * @description
 * @date 2024/6/17
 */
@Slf4j
@Service
public class PayNotifyChannelServiceImpl implements PayNotifyChannelService {

    @Resource
    private PayChannelNotifyMapper payChannelNotifyMapper;

    @Override
    public boolean checkRepeatChannelNotify(String notifyId) {
        PayChannelNotifyEntity notify = payChannelNotifyMapper.selectOne(PayChannelNotifyEntity::getNotifyId, notifyId,
                PayChannelNotifyEntity::getStatus, PayNotifyStatusEnum.SUCCESS.getStatus());
        return notify != null;
    }

    @Override
    public void saveChannelNotifyRecord(Map<String, String> params, String body, String notifyId, String channel, Integer type) {
        PayChannelNotifyEntity notify = new PayChannelNotifyEntity();
        notify.setChannel(channel);
        notify.setNotifyType(type);
        notify.setNotifyId(notifyId);
        notify.setNotifyRequestParams(JSONUtil.toJsonStr(params));
        notify.setNotifyRequest(body);
        notify.setNotifyResult("success");
        notify.setStatus(PayNotifyStatusEnum.SUCCESS.getStatus());
        payChannelNotifyMapper.insert(notify);
    }

}
