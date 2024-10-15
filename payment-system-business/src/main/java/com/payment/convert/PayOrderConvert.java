package com.payment.convert;

import com.framework.mybatis.core.pojo.PageResult;
import com.framework.pay.core.client.dto.order.PayOrderRespDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import com.payment.data.entity.PayAppEntity;
import com.payment.data.entity.PayOrderEntity;
import com.payment.domain.param.PayOrderUnifiedResp;
import com.payment.domain.vo.PayAppVo;
import com.payment.domain.vo.PayOrderVo;

/**
 * @author sdy
 * @description
 * @date 2024/5/1
 */
@Mapper
public interface PayOrderConvert {

    PayOrderConvert INSTANCES = Mappers.getMapper(PayOrderConvert.class);

    PageResult<PayAppVo> convert(PageResult<PayAppEntity> page);

    PayOrderUnifiedResp convert(PayOrderEntity order);

    default PayOrderUnifiedResp convert2Wx(PayOrderEntity order, PayOrderRespDTO payOrderResp) {
        PayOrderUnifiedResp resp = convert(order);
        resp.setAppId(payOrderResp.getWxRawData().getAppId());
        resp.setTimeStamp(payOrderResp.getWxRawData().getTimeStamp());
        resp.setNonceStr(payOrderResp.getWxRawData().getNonceStr());
        resp.setPackageValue(payOrderResp.getWxRawData().getPackageValue());
        resp.setSignType(payOrderResp.getWxRawData().getSignType());
        resp.setPaySign(payOrderResp.getWxRawData().getPaySign());
        return resp;
    }

    PayOrderVo convert2(PayOrderEntity order);

}
