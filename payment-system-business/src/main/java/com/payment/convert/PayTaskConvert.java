package com.payment.convert;

import com.framework.mybatis.core.pojo.PageResult;
import com.payment.data.entity.PayChannelEntity;
import com.payment.data.entity.PayNotifyTaskEntity;
import com.payment.domain.vo.PayTaskVo;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.Map;

/**
 * @author sdy
 * @description
 * @date 2024/5/1
 */
@Mapper
public interface PayTaskConvert {

    PayTaskConvert INSTANCES = Mappers.getMapper(PayTaskConvert.class);

    PageResult<PayTaskVo> convert(PageResult<PayNotifyTaskEntity> page);

    default PageResult<PayTaskVo> convert(PageResult<PayNotifyTaskEntity> page, Map<Long, String> appMap, Map<Long, PayChannelEntity> channelMap) {
        PageResult<PayTaskVo> result = convert(page);
        if (result.hasContent()) {
            result.getList().forEach(r -> {
                PayChannelEntity channel = channelMap.get(r.getChannelId());
                if (channel != null) {
                    r.setAppName(appMap.get(channel.getAppId()));
                }
            });
        }
        return result;
    }

}
