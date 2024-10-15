package com.payment.domain.param;

import com.framework.mybatis.core.pojo.PageParam;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author sdy
 * @description
 * @date 2024/10/12
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class PagePayTaskReq extends PageParam {

    private String appName;
    private Integer type;

}
