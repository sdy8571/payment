package com.payment.domain.param;

import lombok.Data;

/**
 * @author sdy
 * @description
 * @date 2024/5/30
 */
@Data
public class ModifyPayAppReq {

    private Long id;
    private String name;
    private String orderNotifyUrl;
    private String refundNotifyUrl;
    private String remark;

}
