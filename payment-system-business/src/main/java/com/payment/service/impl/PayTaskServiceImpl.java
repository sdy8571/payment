package com.payment.service.impl;

import com.framework.mybatis.core.pojo.PageResult;
import com.payment.convert.PayTaskConvert;
import com.payment.data.entity.PayChannelEntity;
import com.payment.data.entity.PayNotifyTaskEntity;
import com.payment.data.mapper.PayNotifyTaskMapper;
import com.payment.domain.param.PagePayTaskReq;
import com.payment.domain.vo.PayTaskVo;
import com.payment.service.PayAppService;
import com.payment.service.PayChannelService;
import com.payment.service.PayTaskService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @author sdy
 * @description
 * @date 2024/10/12
 */
@Slf4j
@Service
public class PayTaskServiceImpl implements PayTaskService {

    @Resource
    private PayNotifyTaskMapper payNotifyTaskMapper;
    @Resource
    private PayAppService payAppService;
    @Resource
    private PayChannelService payChannelService;

    @Override
    public PageResult<PayTaskVo> page(PagePayTaskReq req) {
        // 查询
        List<Long> channleIds = payChannelService.getChannelIdByAppName(req.getAppName());
        PageResult<PayNotifyTaskEntity> page = payNotifyTaskMapper.selectPage(req, channleIds, req.getType());
        Map<Long, PayChannelEntity> channelMap = payChannelService.getAllList();
        Map<Long, String> appMap = payAppService.getAllMap();
        return PayTaskConvert.INSTANCES.convert(page, appMap, channelMap);
    }

}
