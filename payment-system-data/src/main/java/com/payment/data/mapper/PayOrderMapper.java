package com.payment.data.mapper;

import com.framework.mybatis.core.mapper.BaseMapperX;
import com.framework.mybatis.core.pojo.PageParam;
import com.framework.mybatis.core.pojo.PageResult;
import com.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.payment.data.entity.PayOrderEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 * 支付订单 Mapper 接口
 * </p>
 *
 * @author sdy
 * @since 2024-04-10
 */
@Mapper
public interface PayOrderMapper extends BaseMapperX<PayOrderEntity> {

    default PageResult<PayOrderEntity> selectPage(PageParam pageParam, List<Long> channleIds, Long channelId, String merchantOrderNo,
                                                  String transactionId, Integer status, String[] successTime) {
        return selectPage(pageParam, new LambdaQueryWrapperX<PayOrderEntity>()
                .inIfPresent(PayOrderEntity::getChannelId, channleIds)
                .eqIfPresent(PayOrderEntity::getChannelId, channelId)
                .eqIfPresent(PayOrderEntity::getOutTradeNo, merchantOrderNo)
                .eqIfPresent(PayOrderEntity::getTransactionId, transactionId)
                .eqIfPresent(PayOrderEntity::getStatus, status)
                .betweenIfPresent(PayOrderEntity::getSuccessTime, successTime)
                .orderByDesc(PayOrderEntity::getUpdateTime));
    }

}
