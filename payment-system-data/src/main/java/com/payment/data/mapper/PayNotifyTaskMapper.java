package com.payment.data.mapper;

import com.framework.mybatis.core.mapper.BaseMapperX;
import com.framework.mybatis.core.pojo.PageParam;
import com.framework.mybatis.core.pojo.PageResult;
import com.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.payment.data.entity.PayNotifyTaskEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 * 支付通知 Mapper 接口
 * </p>
 *
 * @author sdy
 * @since 2024-04-10
 */
@Mapper
public interface PayNotifyTaskMapper extends BaseMapperX<PayNotifyTaskEntity> {

    default PageResult<PayNotifyTaskEntity> selectPage(PageParam pageParam, List<Long> channelIds, Integer type) {
        return selectPage(pageParam, new LambdaQueryWrapperX<PayNotifyTaskEntity>()
                .inIfPresent(PayNotifyTaskEntity::getChannelId, channelIds)
                .eqIfPresent(PayNotifyTaskEntity::getType, type)
                .orderByDesc(PayNotifyTaskEntity::getUpdateTime));
    }

}
