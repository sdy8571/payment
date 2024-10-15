package com.payment.service;

/**
 * 回调通知 Service 接口
 *
 * @author 芋道源码
 */
public interface PayNotifyService {

    /**
     * 创建回调通知任务
     *
     * @param type 类型
     * @param dataId 数据编号
     */
    void createPayNotifyTask(Integer type, Long dataId);

}
