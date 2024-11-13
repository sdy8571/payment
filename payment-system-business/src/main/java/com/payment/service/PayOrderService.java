package com.payment.service;

import com.framework.mybatis.core.pojo.PageResult;
import com.payment.data.entity.PayOrderEntity;
import com.payment.domain.param.GetPayOrderReq;
import com.payment.domain.param.PagePayOrderReq;
import com.payment.domain.param.PayOrderUnifiedReq;
import com.payment.domain.param.PayOrderUnifiedResp;
import com.payment.domain.vo.PayOrderVo;

import java.util.Map;

/**
 * @author sdy
 * @description
 * @date 2024/5/30
 */
public interface PayOrderService {

    PayOrderEntity getById(Long id);

    PayOrderEntity getByOutTradeNo(String outTradeNo);

    PayOrderUnifiedResp placeAnOrder(PayOrderUnifiedReq req);

    String orderNotify(Long payChannelId, Map<String, String> params, String body);

    PayOrderVo getPayOrder(GetPayOrderReq req);

    PageResult<PayOrderVo> page(PagePayOrderReq req);

}
