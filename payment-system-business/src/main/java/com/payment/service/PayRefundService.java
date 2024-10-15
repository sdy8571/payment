package com.payment.service;

import com.payment.data.entity.PayRefundEntity;
import com.payment.domain.param.GetPayRefundReq;
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

    Integer getStatus(Integer status, Integer notifyStatus);

    PayRefundVo getPayRefund(GetPayRefundReq req);

}
