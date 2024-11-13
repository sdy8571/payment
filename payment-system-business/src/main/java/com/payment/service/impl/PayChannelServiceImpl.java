package com.payment.service.impl;

import cn.hutool.core.date.DateUtil;
import com.framework.base.constants.BaseConstants;
import com.framework.base.exception.util.ServiceExceptionUtil;
import com.framework.mybatis.core.pojo.PageResult;
import com.framework.pay.core.client.PayClient;
import com.framework.pay.core.client.cache.ChannelClientCache;
import com.framework.pay.core.client.cache.ChannelPO;
import com.framework.pay.core.enums.channel.PayChannelEnum;
import com.payment.contants.PayErrorCodeConstants;
import com.payment.convert.PayChannelConvert;
import com.payment.data.entity.PayAppEntity;
import com.payment.data.entity.PayChannelEntity;
import com.payment.data.mapper.PayChannelMapper;
import com.payment.domain.param.ModifyPayChannelReq;
import com.payment.domain.param.PagePayChannelReq;
import com.payment.domain.vo.PayBaseVo;
import com.payment.domain.vo.PayChannelTypeVo;
import com.payment.domain.vo.PayChannelVo;
import com.payment.service.PayAppService;
import com.payment.service.PayChannelService;
import com.payment.utils.PaymentUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author sdy
 * @description 需要继承 channelClientCache 实现本地缓存load方法
 * @date 2024/5/30
 */
@Slf4j
@Service
public class PayChannelServiceImpl extends ChannelClientCache implements PayChannelService {

    @Resource
    private PayChannelMapper payChannelMapper;
    @Resource
    private PayAppService payAppService;

    @Override
    protected ChannelPO loadChannel(Long id) {
        PayChannelEntity channel = getById(id);
        if (channel == null) {
            return new ChannelPO();
        }
        return new ChannelPO(channel.getCode(), channel.getConfig());
    }

    @Override
    public PageResult<PayChannelVo> page(PagePayChannelReq req) {
        PageResult<PayChannelEntity> page = payChannelMapper.selectPage(req, req.getCode());
        Map<Long, String> appMap = payAppService.getAllMap();
        return PayChannelConvert.INSTANCES.convert(page, appMap);
    }

    @Override
    public PayChannelVo detail(Long id) {
        PayChannelVo vo = PayChannelConvert.INSTANCES.convert1(getById(id));
        // 设置app名称
        PayAppEntity app = payAppService.getById(vo.getAppId());
        if (app != null) {
            vo.setAppName(app.getName());
        }
        // 返回结果
        return vo;
    }

    @Override
    public Long create(ModifyPayChannelReq req) {
        // 校验 app应用
        checkPayApp(req.getAppId());
        // 校验 code 码值
        checkPayChannelCode(req.getAppId(), req.getCode(), null);
        // 保存渠道配置
        PayChannelEntity entity = PayChannelConvert.INSTANCES.convert(req);
        entity.setStatus(BaseConstants.BASE_STATUS.YES.getStatus());
        // todo 创建人更新人需要处理
        payChannelMapper.insert(entity);
        return entity.getId();
    }

    @Override
    public Long update(ModifyPayChannelReq req) {
        // 校验 app应用
        checkPayApp(req.getAppId());
        // 查询原数据
        PayChannelEntity check = getById(req.getId());
        if (check == null || BaseConstants.BASE_STATUS.NO.getStatus().equals(check.getStatus())) {
            throw ServiceExceptionUtil.exception(PayErrorCodeConstants.PAY_CHANNEL_NOT_FOUND);
        }
        // 校验 code 码值
        checkPayChannelCode(req.getAppId(), req.getCode(), req.getId());
        // 转换对象
        PayChannelEntity entity = PayChannelConvert.INSTANCES.convert(req);
        entity.setUpdateTime(new Date());
        // todo 创建人更新人需要处理
        payChannelMapper.updateById(entity);
        return entity.getId();
    }

    @Override
    public void delete(Long id) {
        PayChannelEntity check = getById(id);
        if (check == null) {
            return;
        }
        payChannelMapper.deleteById(check);
    }

    @Override
    public PayChannelEntity getById(Long id) {
        return payChannelMapper.selectOne(PayChannelEntity::getId, id);
    }

    @Override
    public List<PayChannelTypeVo> getTypeList() {
        PayChannelEnum[] enums = PayChannelEnum.values();
        if (enums.length == 0) {
            return Collections.emptyList();
        }
        return Arrays.stream(enums).map(this::genType).filter(Objects::nonNull).collect(Collectors.toList());
    }

    @Override
    public List<PayChannelVo> getByApp(Long appId) {
        List<PayChannelEntity> list;
        if (appId == null) {
            list = payChannelMapper.selectList();
        } else {
            list = payChannelMapper.selectList(PayChannelEntity::getAppId, appId);
        }
        if (CollectionUtils.isEmpty(list)) {
            return Collections.emptyList();
        }
        return PayChannelConvert.INSTANCES.convert(list);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void status(Long id) {
        PayChannelEntity check = getById(id);
        if (check == null) {
            throw ServiceExceptionUtil.exception(PayErrorCodeConstants.PAY_CHANNEL_NOT_FOUND);
        }
        if (BaseConstants.BASE_STATUS.isYes(check.getStatus())) {
            // 当前状态为启用，执行停用
            check.setStatus(BaseConstants.BASE_STATUS.NO.getStatus());
        } else {
            // 当前状态为停用，执行启用。
            // 启用时，确保app对应渠道只有一个可用状态
            // 停用应用渠道下所有配置
            List<PayChannelEntity> channels = payChannelMapper.selectList(check.getAppId(), check.getCode());
            if (!CollectionUtils.isEmpty(channels)) {
                channels.forEach(c -> c.setStatus(BaseConstants.BASE_STATUS.NO.getStatus()));
                payChannelMapper.updateBatch(channels);
            }
            // 启用当前渠道
            check.setStatus(BaseConstants.BASE_STATUS.YES.getStatus());
        }
        check.setUpdateTime(DateUtil.date());
        payChannelMapper.updateById(check);
    }

    @Override
    public PayChannelEntity getByCode(String code, Long appId) {
        // 校验 app应用
        checkPayApp(appId);
        // 查询原数据
        PayChannelEntity channel = payChannelMapper.selectOne(PayChannelEntity::getCode, code, PayChannelEntity::getAppId, appId);
        if (channel == null || BaseConstants.BASE_STATUS.NO.getStatus().equals(channel.getStatus())) {
            throw ServiceExceptionUtil.exception(PayErrorCodeConstants.PAY_CHANNEL_NOT_FOUND);
        }
        // 返回结果
        return channel;
    }

    @Override
    public List<Long> getChannelIdByApp(Long appId) {
        List<PayChannelEntity> list = payChannelMapper.selectList(PayChannelEntity::getAppId, appId);
        if (CollectionUtils.isEmpty(list)) {
            return Collections.emptyList();
        }
        return list.stream().map(PayChannelEntity::getId).collect(Collectors.toList());
    }

    @Override
    public Map<Long, PayBaseVo> getChannelMap(List<Long> channelIds) {
        if (CollectionUtils.isEmpty(channelIds)) {
            return Collections.emptyMap();
        }
        // 查询渠道列表
        List<PayChannelEntity> channels = payChannelMapper.selectList(PayChannelEntity::getId, channelIds);
        if (CollectionUtils.isEmpty(channels)) {
            return Collections.emptyMap();
        }
        // 查询应用列表
        Map<Long, String> appMaps = payAppService.getAllMap();
        // 组装返回结果
        Map<Long, PayBaseVo> result = new HashMap<>(6);
        channels.forEach(c -> result.put(c.getId(), new PayBaseVo(appMaps.get(c.getAppId()), PaymentUtils.getChannelName(c.getCode()))));
        return result;
    }

    @Override
    public PayClient getPayClient(Long id) {
        return  clientCache.getUnchecked(id);
    }

    @Override
    public Map<Long, PayChannelEntity> getAllList() {
        List<PayChannelEntity> channelList = payChannelMapper.selectList();
        return channelList.stream().collect(Collectors.toMap(PayChannelEntity::getId, v -> v));
    }

    @Override
    public List<Long> getChannelIdByAppName(String appName) {
        if (StringUtils.isBlank(appName)) {
            return Collections.emptyList();
        }
        //
        List<Long> appIds = payAppService.getAppIdByName(appName);
        if (CollectionUtils.isEmpty(appIds)) {
            return Collections.emptyList();
        }
        List<PayChannelEntity> channels = payChannelMapper.selectList(PayChannelEntity::getAppId, appIds);
        return channels.stream().map(PayChannelEntity::getId).collect(Collectors.toList());
    }

    /***************** 内部方法 ****************/

    private void checkPayChannelCode(Long appId, String code, Long id) {
        // 校验统一 app 下，支付渠道唯一
        PayChannelEntity channel = payChannelMapper.checkChannelCode(appId, code);
        if (channel != null) {
            if (id == null) {
                // 新增
                throw ServiceExceptionUtil.exception(PayErrorCodeConstants.PAY_CHANNEL_EXISTS, PaymentUtils.getChannelName(code));
            } else {
                // 修改
                if (!channel.getId().equals(id)) {
                    throw ServiceExceptionUtil.exception(PayErrorCodeConstants.PAY_CHANNEL_EXISTS, PaymentUtils.getChannelName(code));
                }
            }
        }
    }

    private PayChannelTypeVo genType(PayChannelEnum payChannelEnum) {
        if (payChannelEnum == null) {
            return null;
        }
        // 处理对象
        PayChannelTypeVo vo = new PayChannelTypeVo();
        vo.setCode(payChannelEnum.getCode());
        vo.setName(payChannelEnum.getName());
        vo.setChannel(payChannelEnum.getChannel());
        return vo;
    }

    private void checkPayApp(Long appId) {
        // 校验 app应用
        PayAppEntity payApp = payAppService.getById(appId);
        if (payApp == null || BaseConstants.BASE_STATUS.NO.getStatus().equals(payApp.getStatus())) {
            throw ServiceExceptionUtil.exception(PayErrorCodeConstants.PAY_APP_NOT_FOUND);
        }
    }


}
