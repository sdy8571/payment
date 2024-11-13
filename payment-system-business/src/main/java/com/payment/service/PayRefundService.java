package com.payment.service;

import com.framework.mybatis.core.pojo.PageResult;
import com.payment.data.entity.PayRefundEntity;
import com.payment.domain.param.GetPayRefundReq;
import com.payment.domain.param.PagePayRefundReq;
import com.payment.domain.param.PayRefundAppleReq;
import com.payment.domain.param.PayRefundConfirmResp;
import com.payment.domain.vo.PayRefundVo;

import java.util.Map;

/**
 * @author sdy
 * @description
 * @date 2024/5/30
 */
public interface PayRefundService {

    PayRefundEntity getById(Long id);

    Long applyRefund(PayRefundAppleReq req);

    PayRefundConfirmResp confirmRefund(Long id);

    String refundNotify(Long payChannelId, Map<String, String> params, String body);

    PayRefundVo getPayRefund(GetPayRefundReq req);

    PageResult<PayRefundVo> page(PagePayRefundReq req);

}
