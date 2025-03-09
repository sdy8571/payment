package com.payment.service.impl;

import com.framework.common.constatnts.BaseConstants;
import com.framework.common.exception.util.ServiceExceptionUtil;
import com.framework.mybatis.core.pojo.PageResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import com.payment.contants.PayErrorCodeConstants;
import com.payment.convert.PayAppConvert;
import com.payment.data.entity.PayAppEntity;
import com.payment.data.mapper.PayAppMapper;
import com.payment.domain.param.ModifyPayAppReq;
import com.payment.domain.param.PagePayAppReq;
import com.payment.domain.vo.PayAppVo;
import com.payment.service.PayAppService;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author sdy
 * @description
 * @date 2024/5/30
 */
@Slf4j
@Service
public class PayAppServiceImpl implements PayAppService {

    @Resource
    private PayAppMapper payAppMapper;

    @Override
    public PageResult<PayAppVo> page(PagePayAppReq req) {
        PageResult<PayAppEntity> page = payAppMapper.selectPage(req, req.getName());
        return PayAppConvert.INSTANCES.convert(page);
    }

    @Override
    public PayAppVo detail(Long id) {
        return PayAppConvert.INSTANCES.convert(getById(id));
    }

    @Override
    public Long create(ModifyPayAppReq req) {
        PayAppEntity entity = PayAppConvert.INSTANCES.convert(req);
        entity.setStatus(BaseConstants.BASE_STATUS.YES.getStatus());
        // todo 创建人更新人需要处理
        payAppMapper.insert(entity);
        return entity.getId();
    }

    @Override
    public Long update(ModifyPayAppReq req) {
        // 查询原数据
        PayAppEntity check = getById(req.getId());
        if (check == null) {
            throw ServiceExceptionUtil.exception(PayErrorCodeConstants.PAY_APP_NOT_FOUND);
        }
        PayAppEntity entity = PayAppConvert.INSTANCES.convert(req);
        entity.setUpdateTime(new Date());
        // todo 创建人更新人需要处理
        payAppMapper.updateById(entity);
        return entity.getId();
    }

    @Override
    public void delete(Long id) {
        PayAppEntity check = getById(id);
        if (check == null) {
            return;
        }
        check.setStatus(BaseConstants.BASE_STATUS.NO.getStatus());
        check.setUpdateTime(new Date());
        payAppMapper.updateById(check);
    }

    @Override
    public PayAppEntity getById(Long id) {
        return payAppMapper.selectOne(PayAppEntity::getId, id);
    }

    @Override
    public Map<Long, String> getAllMap() {
        List<PayAppVo> list = getAllList();
        if (CollectionUtils.isEmpty(list)) {
            return Collections.emptyMap();
        }
        return list.stream().collect(Collectors.toMap(PayAppVo::getId, PayAppVo::getName, (v1, v2) -> v2));
    }

    @Override
    public List<PayAppVo> getAllList() {
        List<PayAppEntity> list = payAppMapper.selectList();
        return PayAppConvert.INSTANCES.convert(list.stream().filter(a -> BaseConstants.BASE_STATUS.isYes(a.getStatus()))
                .collect(Collectors.toList()));
    }

    @Override
    public List<Long> getAppIdByName(String name) {
        if (StringUtils.isBlank(name)) {
            return Collections.emptyList();
        }
        List<PayAppEntity> appList = payAppMapper.selectList(PayAppEntity::getName, name);
        return appList.stream().map(PayAppEntity::getId).collect(Collectors.toList());
    }
}
