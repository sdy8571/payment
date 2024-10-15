package com.payment.service;

import java.util.Map;

/**
 * 回调通知 Service 接口
 *
 * @author 芋道源码
 */
public interface PayNotifyChannelService {

    boolean checkRepeatChannelNotify(String notifyId);

    void saveChannelNotifyRecord(Map<String, String> params, String body, String notifyId, String channel, Integer type);

}
