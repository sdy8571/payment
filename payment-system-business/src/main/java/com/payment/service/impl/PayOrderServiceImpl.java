package com.payment.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.json.JSONUtil;
import com.framework.base.exception.util.ServiceExceptionUtil;
import com.framework.pay.core.client.PayClient;
import com.framework.pay.core.client.dto.order.PayOrderRespDTO;
import com.framework.pay.core.client.dto.order.PayOrderUnifiedReqDTO;
import com.framework.pay.core.enums.order.PayOrderStatusRespEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.payment.contants.PayErrorCodeConstants;
import com.payment.contants.enums.PayNotifyChannelEnum;
import com.payment.contants.enums.PayNotifyStatusEnum;
import com.payment.contants.enums.PayNotifyTypeEnum;
import com.payment.contants.enums.PayOrderStatusEnum;
import com.payment.convert.PayOrderConvert;
import com.payment.data.entity.PayAppEntity;
import com.payment.data.entity.PayChannelEntity;
import com.payment.data.entity.PayChannelNotifyEntity;
import com.payment.data.entity.PayOrderEntity;
import com.payment.data.mapper.PayChannelNotifyMapper;
import com.payment.data.mapper.PayOrderMapper;
import com.payment.domain.param.GetPayOrderReq;
import com.payment.domain.param.PayOrderUnifiedReq;
import com.payment.domain.param.PayOrderUnifiedResp;
import com.payment.domain.vo.PayOrderVo;
import com.payment.service.PayAppService;
import com.payment.service.PayChannelService;
import com.payment.service.PayNotifyService;
import com.payment.service.PayOrderService;
import com.payment.utils.PaymentUtils;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @author sdy
 * @description
 * @date 2024/6/10
 */
@Slf4j
@Service
public class PayOrderServiceImpl implements PayOrderService {

    @Resource
    private PayOrderMapper payOrderMapper;
    @Resource
    private PayAppService payAppService;
    @Resource
    private PayChannelService payChannelService;
    @Resource
    private PayChannelNotifyMapper payChannelNotifyMapper;
    @Resource
    private PayNotifyService payNotifyService;
    @Value("${pay.baffle}")
    private boolean payBaffle;

    @Override
    public PayOrderEntity getById(Long id) {
        return payOrderMapper.selectById(id);
    }

    @Override
    public PayOrderEntity getByOutTradeNo(String outTradeNo) {
        return payOrderMapper.selectOne(PayOrderEntity::getOutTradeNo, outTradeNo);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public PayOrderUnifiedResp placeAnOrder(PayOrderUnifiedReq req) {
        // 1.校验流水号
        checkSerialNo(req.getSerialNo());
        // 查询订单
        PayOrderEntity checkOrder = getByOutTradeNo(req.getOutTradeNo());
        if (checkOrder != null) {
            throw ServiceExceptionUtil.exception(PayErrorCodeConstants.PAY_ORDER_EXISTS);
        }
        // 获取支付应用
        PayAppEntity app = payAppService.getById(req.getPayAppId());
        if (app == null) {
            throw ServiceExceptionUtil.exception(PayErrorCodeConstants.PAY_APP_NOT_FOUND);
        }
        // 获取支付参数
        PayChannelEntity channel = payChannelService.getByCode(req.getPayChannelCode(), app.getId());
        // 2 保存订单支付信息
        PayOrderEntity order = savePayOrder(req, channel.getId());
        // 3 统一下单
        PayOrderRespDTO payOrderResp = unifiedOrder(req, app, channel);
        // 返回结果
        return PayOrderConvert.INSTANCES.convert2Wx(order, payOrderResp);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String orderNotify(Long payChannelId, Map<String, String> params, String body) {
        // 加载客户端
        PayClient payClient = payChannelService.getPayClient(payChannelId);
        // 解析回调body
        PayOrderRespDTO response = payClient.parseOrderNotify(params, body);
        log.info("[notifyCallback][支付成功回调({})]", JSONUtil.toJsonStr(response));
        // 防重通知，防止多次通知重复调用问题
        if (checkRepeatNotify(response.getChannelOrderNo())) {
            log.info("重复通知回调，直接退出");
            return "success";
        }
        // 处理支付订单
        PayOrderEntity payOrder = payOrderMapper.selectOne(PayOrderEntity::getOutTradeNo, response.getOutTradeNo());
        if (payOrder == null) {
            log.info("支付订单不存在，直接退出");
            return "success";
        }
        if (PayOrderStatusRespEnum.isSuccess(response.getStatus())) {
            payOrder.setSuccessTime(DateUtil.date());
            payOrder.setStatus(PayOrderStatusEnum.SUCCESS.getStatus());
        } else {
            payOrder.setStatus(PayOrderStatusEnum.FAILURE.getStatus());
        }
        payOrder.setNotifyTime(DateUtil.date());
        payOrder.setTransactionId(response.getChannelOrderNo());
        payOrderMapper.updateById(payOrder);
        // 处理回调业务系统
        payNotifyService.createPayNotifyTask(PayNotifyTypeEnum.ORDER.getType(), payOrder.getId());
        // 记录渠道回调日志
        saveChannelNotify(params, body, response);
        // 返回结果
        return "success";
    }

    @Override
    public PayOrderVo getPayOrder(GetPayOrderReq req) {
        PayOrderEntity order = null;
        if (StringUtils.isNotBlank(req.getOutTradeNo())) {
            order = payOrderMapper.selectOne(PayOrderEntity::getOutTradeNo, req.getOutTradeNo());
        }
        if (order == null && StringUtils.isNotBlank(req.getTransactionId())) {
            order = payOrderMapper.selectOne(PayOrderEntity::getTransactionId, req.getTransactionId());
        }
        if (order == null && StringUtils.isNotBlank(req.getSerialNo())) {
            order = payOrderMapper.selectOne(PayOrderEntity::getSerialNo, req.getSerialNo());
        }
        if (order == null) {
            return null;
        }
        return PayOrderConvert.INSTANCES.convert2(order);
    }

    /************ 内部方法 ************/

    private boolean checkRepeatNotify(String notifyId) {
        PayChannelNotifyEntity notify = payChannelNotifyMapper.selectOne(PayChannelNotifyEntity::getNotifyId, notifyId,
                PayChannelNotifyEntity::getStatus, PayNotifyStatusEnum.SUCCESS.getStatus());
        return notify != null;
    }

    private void checkSerialNo(String serialNo) {
        long count = payOrderMapper.selectCount(PayOrderEntity::getSerialNo, serialNo);
        if (count > 0) {
            throw ServiceExceptionUtil.exception(PayErrorCodeConstants.PAY_SERIAL_REPEAT);
        }
    }

    private void saveChannelNotify(Map<String, String> params, String body, PayOrderRespDTO response) {
        PayChannelNotifyEntity notify = new PayChannelNotifyEntity();
        notify.setChannel(PayNotifyChannelEnum.WX.name());
        notify.setNotifyId(response.getChannelOrderNo());
        notify.setNotifyType(PayNotifyTypeEnum.ORDER.getType());
        notify.setNotifyRequestParams(JSONUtil.toJsonStr(params));
        notify.setNotifyRequest(body);
        notify.setNotifyResult("success");
        notify.setStatus(PayNotifyStatusEnum.SUCCESS.getStatus());
        payChannelNotifyMapper.insert(notify);
    }

    private PayOrderEntity savePayOrder(PayOrderUnifiedReq req, Long channelId) {
        PayOrderEntity payOrder = new PayOrderEntity();
        payOrder.setSerialNo(req.getSerialNo());
        payOrder.setChannelId(channelId);
        payOrder.setOutTradeNo(req.getOutTradeNo());
        payOrder.setSubject(req.getSubject());
        payOrder.setBody(req.getBody());
        payOrder.setNotifyUrl(req.getNotifyUrl());
        payOrder.setReturnUrl(req.getReturnUrl());
        payOrder.setPrice(req.getPrice());
        payOrder.setExpireTime(req.getExpireTime());
        payOrder.setChannelExtras(JSONUtil.toJsonStr(req.getChannelExtras()));
        payOrder.setStatus(PayOrderStatusEnum.WAITING.getStatus());
        payOrder.setCreateTime(DateUtil.date());
        payOrder.setUpdateTime(DateUtil.date());
        payOrderMapper.insert(payOrder);
        return payOrder;
    }

    private PayOrderRespDTO unifiedOrder(PayOrderUnifiedReq req, PayAppEntity app, PayChannelEntity channel) {
        // 加载客户端
        PayClient client = payChannelService.getPayClient(channel.getId());
        // 统一下单
        PayOrderUnifiedReqDTO unifiedOrderReqDTO = new PayOrderUnifiedReqDTO();
        //2.1 商户相关的字段
        unifiedOrderReqDTO.setOutTradeNo(req.getOutTradeNo());
        unifiedOrderReqDTO.setUserIp(req.getUserIp());
        unifiedOrderReqDTO.setSubject(req.getSubject());
        String notifyUrl = PaymentUtils.formatUrl(app.getOrderNotifyUrl()) + channel.getId();
        unifiedOrderReqDTO.setNotifyUrl(notifyUrl);
        //2.2 订单相关字段
        unifiedOrderReqDTO.setPrice(req.getPrice());
        unifiedOrderReqDTO.setChannelExtras(req.getChannelExtras());
        unifiedOrderReqDTO.setExpireTime(PaymentUtils.format2Date(req.getExpireTime()));
        PayOrderRespDTO payOrderResp = client.unifiedOrder(unifiedOrderReqDTO);
        if (!PayOrderStatusRespEnum.isUnifiedSuccess(payOrderResp.getStatus())) {
            log.error("下单失败!返回数据:{}", payOrderResp);
            throw ServiceExceptionUtil.exception(PayErrorCodeConstants.PAY_ORDER_UNIFIED_FAILED);
        }
        // 返回结果
        return payOrderResp;
    }

}
