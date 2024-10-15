package com.payment.service;

import com.framework.mybatis.core.pojo.PageResult;
import com.framework.pay.core.client.PayClient;
import com.payment.data.entity.PayChannelEntity;
import com.payment.domain.param.ModifyPayChannelReq;
import com.payment.domain.param.PagePayChannelReq;
import com.payment.domain.vo.PayChannelTypeVo;
import com.payment.domain.vo.PayChannelVo;

import java.util.List;
import java.util.Map;

/**
 * @author sdy
 * @description
 * @date 2024/5/30
 */
public interface PayChannelService {

    PageResult<PayChannelVo> page(PagePayChannelReq req);

    PayChannelVo detail(Long id);

    Long create(ModifyPayChannelReq req);

    Long update(ModifyPayChannelReq req);

    void delete(Long id);

    PayChannelEntity getById(Long id);

    List<PayChannelTypeVo> getTypeList();

    void status(Long id);

    PayChannelEntity getByCode(String code, Long appId);

    /**
     * 获得指定编号的支付客户端
     *
     * @param id 编号
     * @return 支付客户端
     */
    PayClient getPayClient(Long id);

    Map<Long, PayChannelEntity> getAllList();

    List<Long> getChannelIdByAppName(String appName);

}
