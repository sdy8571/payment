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
public class PagePayRefundReq extends PageParam {

    private Long appId;
    private Long channelId;
    private String merchantOrderNo;
    private String merchantRefundNo;
    private String transactionId;
    private String refundId;
    private Integer status;
    private String[] successTime;

}
