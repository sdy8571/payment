package com.payment.data.mapper;

import com.framework.mybatis.core.mapper.BaseMapperX;
import org.apache.ibatis.annotations.Mapper;
import com.payment.data.entity.PayRefundEntity;

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
}
