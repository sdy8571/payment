package com.payment.data.mapper;

import com.framework.base.constants.BaseConstants;
import com.framework.mybatis.core.mapper.BaseMapperX;
import com.framework.mybatis.core.pojo.PageParam;
import com.framework.mybatis.core.pojo.PageResult;
import com.framework.mybatis.core.query.LambdaQueryWrapperX;
import org.apache.ibatis.annotations.Mapper;
import com.payment.data.entity.PayChannelEntity;

/**
 * <p>
 * 支付渠道 Mapper 接口
 * </p>
 *
 * @author sdy
 * @since 2024-04-10
 */
@Mapper
public interface PayChannelMapper extends BaseMapperX<PayChannelEntity> {

    default PageResult<PayChannelEntity> selectPage(PageParam pageParam, String code) {
        return selectPage(pageParam, new LambdaQueryWrapperX<PayChannelEntity>()
                .eqIfPresent(PayChannelEntity::getCode, code)
                .orderByDesc(PayChannelEntity::getUpdateTime));
    }

    default PayChannelEntity checkChannelCode(Long appId, String code) {
        return selectOne(new LambdaQueryWrapperX<PayChannelEntity>()
                .eq(PayChannelEntity::getAppId, appId)
                .eq(PayChannelEntity::getCode, code)
                .eq(PayChannelEntity::getStatus, BaseConstants.BASE_STATUS.YES.getStatus()));
    }


}
