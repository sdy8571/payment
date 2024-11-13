package com.payment.domain.param;

import com.framework.mybatis.core.pojo.PageParam;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author sdy
 * @description
 * @date 2024/11/12
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class PagePayOrderReq extends PageParam {

    private Long appId;
    private Long channelId;
    private String merchantOrderNo;
    private String transactionId;
    private Integer status;
    private String[] successTime;

}
