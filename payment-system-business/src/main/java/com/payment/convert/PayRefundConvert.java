package com.payment.convert;

import com.framework.base.util.DecimalUtils;
import com.framework.mybatis.core.pojo.PageResult;
import com.payment.data.entity.PayRefundEntity;
import com.payment.domain.vo.PayBaseVo;
import com.payment.domain.vo.PayRefundVo;
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
public interface PayRefundConvert {

    PayRefundConvert INSTANCES = Mappers.getMapper(PayRefundConvert.class);

    PayRefundVo convert(PayRefundEntity refund);

    default PayRefundVo convert2(PayRefundEntity refund, Map<Long, PayBaseVo> appChannelMap) {
        PayRefundVo vo = convert(refund);
        vo.setPayPriceY(DecimalUtils.moneyF2YStr(refund.getPayPrice()));
        vo.setRefundPriceY(DecimalUtils.moneyF2YStr(refund.getRefundPrice()));
        vo.setRefundStatus(PaymentUtils.getStatus(refund.getStatus(), refund.getNotifyStatus()));
        // 设置应用和渠道名称
        PayBaseVo baseVo = appChannelMap.get(refund.getChannelId());
        if (baseVo != null) {
            vo.setAppName(baseVo.getAppName());
            vo.setChannelName(baseVo.getChannelName());
        }
        // 返回结果
        return vo;
    }

    default PageResult<PayRefundVo> convert(PageResult<PayRefundEntity> page, Map<Long, PayBaseVo> appChannelMap) {
        List<PayRefundVo> list = new ArrayList<>();
        if (page.hasContent()) {
            page.getList().forEach(r -> list.add(convert2(r, appChannelMap)));
        }
        return new PageResult<>(list, page.getTotal(), page.getCurrentPage(), page.getTotalPage());
    }

}
