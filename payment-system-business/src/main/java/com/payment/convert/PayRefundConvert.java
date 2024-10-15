package com.payment.convert;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import com.payment.data.entity.PayRefundEntity;
import com.payment.domain.vo.PayRefundVo;

/**
 * @author sdy
 * @description
 * @date 2024/5/1
 */
@Mapper
public interface PayRefundConvert {

    PayRefundConvert INSTANCES = Mappers.getMapper(PayRefundConvert.class);

    PayRefundVo convert(PayRefundEntity refund);

}
