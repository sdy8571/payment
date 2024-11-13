package com.payment.convert;

import com.framework.mybatis.core.pojo.PageResult;
import com.payment.data.entity.PayChannelEntity;
import com.payment.domain.param.ModifyPayChannelReq;
import com.payment.domain.vo.PayChannelVo;
import com.payment.utils.PaymentUtils;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author sdy
 * @description
 * @date 2024/5/1
 */
@Mapper
public interface PayChannelConvert {

    PayChannelConvert INSTANCES = Mappers.getMapper(PayChannelConvert.class);

    PageResult<PayChannelVo> convert(PageResult<PayChannelEntity> page);

    PayChannelVo convert(PayChannelEntity bean);

    default PayChannelVo convert1(PayChannelEntity bean) {
        PayChannelVo vo = convert(bean);
        // 设置 channelName
        vo.setChannelName(PaymentUtils.getChannelName(vo.getCode()));
        return vo;
    }

    PayChannelEntity convert(ModifyPayChannelReq req);

    default PageResult<PayChannelVo> convert(PageResult<PayChannelEntity> page, Map<Long, String> map) {
        PageResult<PayChannelVo> result = convert(page);
        // 设置
        if (result.hasContent()) {
            result.getList().forEach(r -> {
                // 设置appName
                if (!CollectionUtils.isEmpty(map)) {
                    r.setAppName(map.get(r.getAppId()));
                }
                // 设置 channelName
                r.setChannelName(PaymentUtils.getChannelName(r.getCode()));
            });
        }
        // 设置渠道名称
        return result;
    }

    default List<PayChannelVo> convert(List<PayChannelEntity> lists) {
        List<PayChannelVo> result = new ArrayList<>();
        lists.forEach(r -> result.add(convert1(r)));
        return result;
    }

}
