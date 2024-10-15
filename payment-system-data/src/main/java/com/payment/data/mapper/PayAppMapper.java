package com.payment.data.mapper;

import com.framework.mybatis.core.mapper.BaseMapperX;
import com.framework.mybatis.core.pojo.PageParam;
import com.framework.mybatis.core.pojo.PageResult;
import com.framework.mybatis.core.query.LambdaQueryWrapperX;
import org.apache.ibatis.annotations.Mapper;
import com.payment.data.entity.PayAppEntity;

/**
 * <p>
 * 支付应用 Mapper 接口
 * </p>
 *
 * @author sdy
 * @since 2024-04-10
 */
@Mapper
public interface PayAppMapper extends BaseMapperX<PayAppEntity> {

    default PageResult<PayAppEntity> selectPage(PageParam pageParam, String name) {
        return selectPage(pageParam, new LambdaQueryWrapperX<PayAppEntity>()
                .eq(PayAppEntity::getStatus, 1)
                .likeIfPresent(PayAppEntity::getName, name)
                .orderByDesc(PayAppEntity::getUpdateTime));
    }

}
