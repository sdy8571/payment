package com.payment.service;

import com.framework.mybatis.core.pojo.PageResult;
import com.payment.data.entity.PayAppEntity;
import com.payment.domain.param.ModifyPayAppReq;
import com.payment.domain.param.PagePayAppReq;
import com.payment.domain.vo.PayAppVo;

import java.util.List;
import java.util.Map;

/**
 * @author sdy
 * @description
 * @date 2024/5/30
 */
public interface PayAppService {

    PageResult<PayAppVo> page(PagePayAppReq req);

    PayAppVo detail(Long id);

    PayAppEntity getById(Long id);

    Long create(ModifyPayAppReq req);

    Long update(ModifyPayAppReq req);

    void delete(Long id);

    Map<Long, String> getAllMap();

    List<PayAppVo> getAllList();

    List<Long> getAppIdByName(String name);
}
