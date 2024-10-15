package com.payment.convert;

import com.framework.mybatis.core.pojo.PageResult;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import com.payment.data.entity.PayAppEntity;
import com.payment.domain.param.ModifyPayAppReq;
import com.payment.domain.vo.PayAppVo;

import java.util.List;

/**
 * @author sdy
 * @description
 * @date 2024/5/1
 */
@Mapper
public interface PayAppConvert {

    PayAppConvert INSTANCES = Mappers.getMapper(PayAppConvert.class);

    PageResult<PayAppVo> convert(PageResult<PayAppEntity> page);

    PayAppVo convert(PayAppEntity bean);

    PayAppEntity convert(ModifyPayAppReq req);

    List<PayAppVo> convert(List<PayAppEntity> list);

}
