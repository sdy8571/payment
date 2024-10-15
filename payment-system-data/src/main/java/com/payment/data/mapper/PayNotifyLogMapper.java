package com.payment.data.mapper;

import com.framework.mybatis.core.mapper.BaseMapperX;
import org.apache.ibatis.annotations.Mapper;
import com.payment.data.entity.PayNotifyLogEntity;

/**
 * <p>
 * 支付通知 Mapper 接口
 * </p>
 *
 * @author sdy
 * @since 2024-04-10
 */
@Mapper
public interface PayNotifyLogMapper extends BaseMapperX<PayNotifyLogEntity> {
}
