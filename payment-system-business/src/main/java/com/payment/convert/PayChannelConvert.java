package com.payment.convert;

import com.framework.mybatis.core.pojo.PageResult;
import com.framework.pay.core.enums.channel.PayChannelEnum;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import com.payment.data.entity.PayChannelEntity;
import com.payment.domain.param.ModifyPayChannelReq;
import com.payment.domain.vo.PayChannelVo;
import org.springframework.util.CollectionUtils;

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
                PayChannelEnum channel = PayChannelEnum.getByCode(r.getCode());
                if (channel != null) {
                    r.setChannelName(PayChannelEnum.getByCode(r.getCode()).getName());
                }
            });
        }
        // 设置渠道名称
        return result;
    }

}
