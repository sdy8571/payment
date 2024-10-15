package com.payment.domain.vo;

import lombok.Data;

import java.util.Date;

/**
 * @author sdy
 * @description
 * @date 2024/5/30
 */
@Data
public class PayAppVo {

    private Long id;
    private String name;
    private Integer status;
    private String remark;
    private String orderNotifyUrl;
    private String refundNotifyUrl;
    private String createBy;
    private Date createTime;
    private String updateBy;
    private Date updateTime;

}
