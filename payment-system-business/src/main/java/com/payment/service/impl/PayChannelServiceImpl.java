package com.payment.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONUtil;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.framework.base.constants.BaseConstants;
import com.framework.base.exception.util.ServiceExceptionUtil;
import com.framework.mybatis.core.pojo.PageResult;
import com.framework.pay.core.client.PayClient;
import com.framework.pay.core.client.PayClientConfig;
import com.framework.pay.core.client.PayClientFactory;
import com.framework.pay.core.enums.channel.PayChannelEnum;
import com.framework.pay.utils.PayUtils;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import com.payment.contants.PayErrorCodeConstants;
import com.payment.convert.PayChannelConvert;
import com.payment.data.entity.PayAppEntity;
import com.payment.data.entity.PayChannelEntity;
import com.payment.data.mapper.PayChannelMapper;
import com.payment.domain.param.ModifyPayChannelReq;
import com.payment.domain.param.PagePayChannelReq;
import com.payment.domain.vo.PayChannelTypeVo;
import com.payment.domain.vo.PayChannelVo;
import com.payment.service.PayAppService;
import com.payment.service.PayChannelService;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import javax.validation.Validator;
import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author sdy
 * @description
 * @date 2024/5/30
 */
@Slf4j
@Service
public class PayChannelServiceImpl implements PayChannelService {

    @Resource
    private PayChannelMapper payChannelMapper;
    @Resource
    private PayAppService payAppService;
    @Resource
    private PayClientFactory payClientFactory;
    @Resource
    private Validator validator;

    /**
     * {@link PayClient} 缓存，通过它异步清空 smsClientFactory
     */
    @Getter
    private final LoadingCache<Long, PayClient> clientCache = PayUtils.buildAsyncReloadingCache(Duration.ofSeconds(10L),
            new CacheLoader<Long, PayClient>() {
                @Override
                public PayClient load(Long id) {
                    // 查询，然后尝试清空
                    PayChannelEntity channel = payChannelMapper.selectById(id);
                    if (channel != null) {
                        PayClientConfig config = parseConfig(channel.getCode(), channel.getConfig());
                        payClientFactory.createOrUpdatePayClient(channel.getId(), channel.getCode(), config);
                    }
                    return payClientFactory.getPayClient(id);
                }
            });

    @Override
    public PageResult<PayChannelVo> page(PagePayChannelReq req) {
        PageResult<PayChannelEntity> page = payChannelMapper.selectPage(req, req.getCode());
        Map<Long, String> appMap = payAppService.getAllMap();
        return PayChannelConvert.INSTANCES.convert(page, appMap);
    }

    @Override
    public PayChannelVo detail(Long id) {
        PayChannelVo vo = PayChannelConvert.INSTANCES.convert(getById(id));
        // 设置app名称
        PayAppEntity app = payAppService.getById(vo.getAppId());
        if (app != null) {
            vo.setAppName(app.getName());
        }
        // 设置 channelName
        PayChannelEnum channel = PayChannelEnum.getByCode(vo.getCode());
        if (channel != null) {
            vo.setChannelName(PayChannelEnum.getByCode(vo.getCode()).getName());
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
    public void status(Long id) {
        PayChannelEntity check = getById(id);
        if (check == null) {
            throw ServiceExceptionUtil.exception(PayErrorCodeConstants.PAY_CHANNEL_NOT_FOUND);
        }
        if (BaseConstants.BASE_STATUS.isYes(check.getStatus())) {
            check.setStatus(BaseConstants.BASE_STATUS.NO.getStatus());
        } else {
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
    public PayClient getPayClient(Long id) {
        PayClient payClient = clientCache.getUnchecked(id);
        if (payClient == null) {
            log.error("[notifyCallback][渠道编号({}) 找不到对应的支付客户端]", id);
            throw ServiceExceptionUtil.exception(PayErrorCodeConstants.PAY_CHANNEL_NOT_FOUND);
        }
        return payClient;
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
                throw ServiceExceptionUtil.exception(PayErrorCodeConstants.PAY_CHANNEL_EXISTS, getPayChannelName(code));
            } else {
                // 修改
                if (!channel.getId().equals(id)) {
                    throw ServiceExceptionUtil.exception(PayErrorCodeConstants.PAY_CHANNEL_EXISTS, getPayChannelName(code));
                }
            }
        }
    }

    private String getPayChannelName(String code) {
        PayChannelEnum payChannelEnum = PayChannelEnum.getByCode(code);
        if (payChannelEnum == null) {
            throw ServiceExceptionUtil.exception(PayErrorCodeConstants.PAY_CHANNEL_NOT_FOUND);
        }
        return payChannelEnum.getName();
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

    /**
     * 解析并校验配置
     *
     * @param code      渠道编码
     * @param configStr 配置
     * @return 支付配置
     */
    private PayClientConfig parseConfig(String code, String configStr) {
        // 解析配置
        Class<? extends PayClientConfig> payClass = PayChannelEnum.getByCode(code).getConfigClass();
        if (ObjectUtil.isNull(payClass) || StringUtils.isBlank(configStr)) {
            throw ServiceExceptionUtil.exception(PayErrorCodeConstants.PAY_CHANNEL_NOT_FOUND);
        }
        PayClientConfig config = JSONUtil.toBean(configStr, payClass);
        Assert.notNull(config);

        // 验证参数
        config.validate(validator);
        return config;
    }
}
