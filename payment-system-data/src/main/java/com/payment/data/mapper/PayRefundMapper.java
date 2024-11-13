package com.payment.data.mapper;

import com.framework.mybatis.core.mapper.BaseMapperX;
import com.framework.mybatis.core.pojo.PageParam;
import com.framework.mybatis.core.pojo.PageResult;
import com.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.payment.data.entity.PayRefundEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 * 支付退款 Mapper 接口
 * </p>
 *
 * @author sdy
 * @since 2024-04-10
 */
@Mapper
public interface PayRefundMapper extends BaseMapperX<PayRefundEntity> {

    default PageResult<PayRefundEntity> selectPage(PageParam pageParam, List<Long> channleIds, Long channelId, String merchantOrderNo,
                                                   String merchantRefundNo, String transactionId, String refundId,
                                                   Integer status, String[] successTime) {
        return selectPage(pageParam, new LambdaQueryWrapperX<PayRefundEntity>()
                .inIfPresent(PayRefundEntity::getChannelId, channleIds)
                .eqIfPresent(PayRefundEntity::getRefundId, channelId)
                .eqIfPresent(PayRefundEntity::getOutTradeNo, merchantOrderNo)
                .eqIfPresent(PayRefundEntity::getOutRefundNo, merchantRefundNo)
                .eqIfPresent(PayRefundEntity::getTransactionId, transactionId)
                .eqIfPresent(PayRefundEntity::getRefundId, refundId)
                .eqIfPresent(PayRefundEntity::getStatus, status)
                .betweenIfPresent(PayRefundEntity::getSuccessTime, successTime)
                .orderByDesc(PayRefundEntity::getUpdateTime));
    }

}
