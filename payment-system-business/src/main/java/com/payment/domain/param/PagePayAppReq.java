package com.payment.domain.param;

import com.framework.mybatis.core.pojo.PageParam;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author sdy
 * @description
 * @date 2024/5/30
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class PagePayAppReq extends PageParam {

    private String name;

}
