package com.payment.data.dto;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author sdy
 * @description
 * @date 2024/10/12
 */
@Data
public class PayNotifyTaskDTO {

    private Long id;
    private String appName;
    private Long channelId;
    private Integer type;
    private Long dataId;
    private String merchantOrderId;
    private String merchantRefundId;
    private Integer status;
    private Date nextNotifyTime;
    private Date lastExecuteTime;
    private Integer notifyTimes;
    private Integer maxNotifyTimes;
    private List<Integer> notifyFrequency;
    private String notifyUrl;
    private String notifyBody;
    private String createBy;
    private Date createTime;
    private String updateBy;
    private Date updateTime;

}
