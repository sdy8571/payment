package com.payment.service;

import com.framework.mybatis.core.pojo.PageResult;
import com.payment.domain.param.PagePayTaskReq;
import com.payment.domain.vo.PayTaskVo;

/**
 * @author sdy
 * @description
 * @date 2024/5/30
 */
public interface PayTaskService {

    PageResult<PayTaskVo> page(PagePayTaskReq req);

}
