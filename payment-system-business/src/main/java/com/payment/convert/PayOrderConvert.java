package com.payment.convert;

import com.framework.base.util.DecimalUtils;
import com.framework.mybatis.core.pojo.PageResult;
import com.framework.pay.core.client.dto.order.PayOrderRespDTO;
import com.payment.data.entity.PayChannelEntity;
import com.payment.data.entity.PayOrderEntity;
import com.payment.domain.param.PayOrderUnifiedResp;
import com.payment.domain.vo.PayBaseVo;
import com.payment.domain.vo.PayOrderVo;
import com.payment.utils.PaymentUtils;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author sdy
 * @description
 * @date 2024/5/1
 */
@Mapper
public interface PayOrderConvert {

    PayOrderConvert INSTANCES = Mappers.getMapper(PayOrderConvert.class);

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

    PayOrderVo convert1(PayOrderEntity order);

    default PayOrderVo convert2(PayOrderEntity order, Map<Long, PayBaseVo> map) {
        PayOrderVo vo = convert1(order);
        vo.setPriceY(DecimalUtils.moneyF2YStr(order.getPrice()));
        // 设置渠道和应用名称
        PayBaseVo baseVo = map.get(order.getChannelId());
        if (baseVo != null) {
            vo.setAppName(baseVo.getAppName());
            vo.setChannelName(baseVo.getChannelName());
        }
        return vo;
    }

    default PageResult<PayOrderVo> convert(PageResult<PayOrderEntity> page, Map<Long, PayBaseVo> appChannelMap) {
        List<PayOrderVo> list = new ArrayList<>();
        if (page.hasContent()) {
            page.getList().forEach(r -> list.add(convert2(r, appChannelMap)));
        }
        return new PageResult<>(list, page.getTotal(), page.getCurrentPage(), page.getTotalPage());
    };

}
