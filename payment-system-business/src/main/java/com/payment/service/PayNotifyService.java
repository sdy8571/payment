package com.payment.service;

/**
 * 回调通知 Service 接口
 *
 * @author 芋道源码
 */
public interface PayNotifyService {

    /**
     * 保存订单通知任务
     * @param type 类型
     * @param dataId 业务主键
     * @param channelId 渠道编号
     * @param outOrderNo 外部订单号
     * @param notifyUrl 通知地址
     * @param status 状态
     */
    void saveOrder(Integer type, Long dataId, Long channelId, String outOrderNo, String notifyUrl, Integer status);

    /**
     * 保存退款通知任务
     * @param type 类型
     * @param dataId 业务主键
     * @param channelId 渠道编号
     * @param outOrderNo 外部订单号
     * @param outRefundNo 外部退单号
     * @param notifyUrl 通知地址
     * @param status 状态
     * @param notifyStatus 通知状态
     */
    void saveRefund(Integer type, Long dataId, Long channelId, String outOrderNo, String outRefundNo, String notifyUrl, Integer status, Integer notifyStatus);

}
