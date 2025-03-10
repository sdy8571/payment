package com.payment.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.json.JSONUtil;
import com.framework.common.exception.util.ServiceExceptionUtil;
import com.framework.mybatis.core.pojo.PageResult;
import com.framework.pay.core.client.PayClient;
import com.framework.pay.core.client.dto.refund.PayRefundRespDTO;
import com.framework.pay.core.client.dto.refund.PayRefundUnifiedReqDTO;
import com.framework.pay.core.enums.order.PayOrderStatusRespEnum;
import com.framework.pay.core.enums.refund.PayRefundStatusRespEnum;
import com.payment.contants.PayErrorCodeConstants;
import com.payment.contants.enums.PayNotifyTypeEnum;
import com.payment.convert.PayRefundConvert;
import com.payment.data.entity.PayAppEntity;
import com.payment.data.entity.PayChannelEntity;
import com.payment.data.entity.PayOrderEntity;
import com.payment.data.entity.PayRefundEntity;
import com.payment.data.mapper.PayRefundMapper;
import com.payment.domain.param.GetPayRefundReq;
import com.payment.domain.param.PagePayRefundReq;
import com.payment.domain.param.PayRefundAppleReq;
import com.payment.domain.param.PayRefundConfirmResp;
import com.payment.domain.vo.PayBaseVo;
import com.payment.domain.vo.PayRefundVo;
import com.payment.service.*;
import com.payment.utils.PaymentUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author sdy
 * @description
 * @date 2024/6/15
 */
@Slf4j
@Service
public class PayRefundServiceImpl implements PayRefundService {

    @Resource
    private PayRefundMapper payRefundMapper;
    @Resource
    private PayOrderService payOrderService;
    @Resource
    private PayAppService payAppService;
    @Resource
    private PayChannelService payChannelService;
    @Resource
    private PayNotifyService payNotifyService;
    @Resource
    private PayNotifyChannelService payNotifyChannelService;

    @Override
    public PayRefundEntity getById(Long id) {
        return payRefundMapper.selectById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long applyRefund(PayRefundAppleReq req) {
        // 1.校验流水号
        checkSerialNo(req.getSerialNo());
        // 查询订单
        PayOrderEntity order = payOrderService.getByOutTradeNo(req.getOutTradeNo());
        if (order == null) {
            throw ServiceExceptionUtil.exception(PayErrorCodeConstants.PAY_ORDER_NOT_EXISTS);
        }
        // 校验退款订单
        PayRefundEntity refund = payRefundMapper.selectOne(PayRefundEntity::getOutRefundNo, req.getOutRefundNo());
        if (refund != null) {
            throw ServiceExceptionUtil.exception(PayErrorCodeConstants.PAY_REFUND_REPEAT_APPLY);
        }
        // 2 保存退款信息
        return savePayRefund(req, order);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public PayRefundConfirmResp confirmRefund(Long id) {
        // 查询退款数据
        PayRefundEntity refund = getById(id);
        if (refund == null) {
            throw ServiceExceptionUtil.exception(PayErrorCodeConstants.PAY_REFUND_NOT_EXISTS);
        }
        if (!PayRefundStatusRespEnum.isWaiting(refund.getStatus())) {
            throw ServiceExceptionUtil.exception(PayErrorCodeConstants.PAY_REFUND_REPEAT_CONFIRM);
        }
        // 获取支付参数
        PayChannelEntity channel = payChannelService.getById(refund.getChannelId());
        if (channel == null) {
            throw ServiceExceptionUtil.exception(PayErrorCodeConstants.PAY_CHANNEL_NOT_FOUND);
        }
        // 获取支付应用
        PayAppEntity app = payAppService.getById(channel.getAppId());
        if (app == null) {
            throw ServiceExceptionUtil.exception(PayErrorCodeConstants.PAY_APP_NOT_FOUND);
        }
        // 统一退单
        PayRefundRespDTO payRefundResp = unifiedRefund(refund, app, channel);
        // 更新退款状态
        refund.setStatus(payRefundResp.getStatus());
        refund.setUpdateTime(DateUtil.date());
        payRefundMapper.updateById(refund);
        // 组装返回结果
        PayRefundConfirmResp resp = new PayRefundConfirmResp();
        resp.setId(refund.getId());
        resp.setOutTradeNo(refund.getOutTradeNo());
        resp.setOutRefundNo(refund.getOutRefundNo());
        resp.setSerialNo(refund.getSerialNo());
        resp.setStatus(PaymentUtils.getStatus(refund.getStatus(), refund.getNotifyStatus()));
        return resp;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String refundNotify(Long payChannelId, Map<String, String> params, String body) {
        // 加载客户端
        PayClient payClient = payChannelService.getPayClient(payChannelId);
        // 解析微信回调body
        PayRefundRespDTO response = payClient.parseRefundNotify(params, body);
        log.info("[notifyCallback][退款成功回调({})]", JSONUtil.toJsonStr(response));
        // 防重通知
        if (payNotifyChannelService.checkRepeatChannelNotify(response.getChannelRefundNo())) {
            log.info("重复通知回调，直接退出");
            return "success";
        }
        // 处理退款订单
        PayRefundEntity payRefund = payRefundMapper.selectOne(PayRefundEntity::getOutRefundNo, response.getOutRefundNo());
        if (payRefund == null) {
            log.info("支付订单不存在，直接退出");
            return "success";
        }
        if (PayOrderStatusRespEnum.isSuccess(response.getStatus())) {
            payRefund.setSuccessTime(DateUtil.date());
            payRefund.setNotifyStatus(PayRefundStatusRespEnum.SUCCESS.getStatus());
        } else {
            payRefund.setNotifyStatus(response.getStatus());
        }
        payRefund.setNotifyTime(DateUtil.date());
        payRefund.setRefundId(response.getChannelRefundNo());
        payRefundMapper.updateById(payRefund);
        // 异步处理回调业务系统
        payNotifyService.saveRefund(PayNotifyTypeEnum.REFUND.getType(), payRefund.getId(), payRefund.getChannelId(),
                payRefund.getOutTradeNo(), payRefund.getOutRefundNo(), payRefund.getNotifyUrl(), payRefund.getStatus(), payRefund.getNotifyStatus());
        // 记录回调日志
        payNotifyChannelService.save(params, body, payClient.getId(), payClient.getChannelName(), response.getChannelRefundNo(), PayNotifyTypeEnum.REFUND.getType());
        // 返回结果
        return "success";
    }

    @Override
    public PayRefundVo getPayRefund(GetPayRefundReq req) {
        PayRefundEntity refund = null;
        if (req.getId() != null) {
            refund = getById(req.getId());
        }
        if (refund == null && StringUtils.isNotBlank(req.getOutRefundNo())) {
            refund = payRefundMapper.selectOne(PayRefundEntity::getOutRefundNo, req.getOutRefundNo());
        }
        if (refund == null && StringUtils.isNotBlank(req.getRefundId())) {
            refund = payRefundMapper.selectOne(PayRefundEntity::getRefundId, req.getRefundId());
        }
        if (refund == null && StringUtils.isNotBlank(req.getOutTradeNo())) {
            refund = payRefundMapper.selectOne(PayRefundEntity::getOutTradeNo, req.getOutTradeNo());
        }
        if (refund == null && StringUtils.isNotBlank(req.getTransactionId())) {
            refund = payRefundMapper.selectOne(PayRefundEntity::getTransactionId, req.getTransactionId());
        }
        if (refund == null && StringUtils.isNotBlank(req.getSerialNo())) {
            refund = payRefundMapper.selectOne(PayRefundEntity::getSerialNo, req.getSerialNo());
        }
        if (refund == null) {
            return null;
        }
        // 设置应用和渠道信息
        Map<Long, PayBaseVo> map = payChannelService.getChannelMap(Arrays.asList(refund.getChannelId()));
        // 返回结果
        return PayRefundConvert.INSTANCES.convert2(refund, map);
    }

    @Override
    public PageResult<PayRefundVo> page(PagePayRefundReq req) {
        // 查询
        List<Long> channleIds = null;
        if (req.getAppId() != null) {
            channleIds = payChannelService.getChannelIdByApp(req.getAppId());
            if (CollectionUtils.isEmpty(channleIds)) {
                return PageResult.empty();
            }
        }
        // 查询订单信息
        PageResult<PayRefundEntity> page = payRefundMapper.selectPage(req, channleIds, req.getChannelId(), req.getMerchantOrderNo(),
                req.getMerchantRefundNo(), req.getTransactionId(), req.getRefundId(), req.getStatus(), req.getSuccessTime());
        // 组装并返回结果
        Map<Long, PayBaseVo> appChannelMap = new HashMap<>(6);
        if (page.hasContent()) {
            List<Long> ids = page.getList().stream().map(PayRefundEntity::getChannelId).distinct().collect(Collectors.toList());
            appChannelMap = payChannelService.getChannelMap(ids);
        }
        // 返回结果
        return PayRefundConvert.INSTANCES.convert(page, appChannelMap);
    }

    /************ 内部方法 ************/

    private PayRefundRespDTO unifiedRefund(PayRefundEntity refund, PayAppEntity app, PayChannelEntity channel) {
        // 加载客户端
        PayClient client = payChannelService.getPayClient(channel.getId());
        // 统一下单
        PayRefundUnifiedReqDTO refundUnifiedReq = new PayRefundUnifiedReqDTO();
        //2.1 退款相关字段
        refundUnifiedReq.setOutTradeNo(refund.getOutTradeNo());
        refundUnifiedReq.setOutRefundNo(refund.getOutRefundNo());
        refundUnifiedReq.setReason(refund.getReason());
        refundUnifiedReq.setPayPrice(refund.getPayPrice());
        refundUnifiedReq.setRefundPrice(refund.getRefundPrice());
        String notifyUrl = PaymentUtils.formatUrl(app.getRefundNotifyUrl()) + channel.getId();
        refundUnifiedReq.setNotifyUrl(notifyUrl);
        PayRefundRespDTO payRefundResp = client.unifiedRefund(refundUnifiedReq);
        if (PayRefundStatusRespEnum.isFailure(payRefundResp.getStatus())) {
            log.error("退款失败!返回数据:{}", payRefundResp);
        }
        // 返回结果
        return payRefundResp;
    }

    private Long savePayRefund(PayRefundAppleReq req, PayOrderEntity order) {
        PayRefundEntity refund = new PayRefundEntity();
        refund.setSerialNo(req.getSerialNo());
        refund.setChannelId(order.getChannelId());
        refund.setOutTradeNo(req.getOutTradeNo());
        refund.setOutRefundNo(req.getOutRefundNo());
        refund.setTransactionId(order.getTransactionId());
        refund.setPayPrice(req.getPayPrice());
        refund.setRefundPrice(req.getRefundPrice());
        refund.setReason(req.getReason());
        refund.setNotifyUrl(req.getNotifyUrl());
        refund.setStatus(PayRefundStatusRespEnum.WAITING.getStatus());
        refund.setCreateTime(DateUtil.date());
        refund.setUpdateTime(DateUtil.date());
        payRefundMapper.insert(refund);
        return refund.getId();
    }

    private void checkSerialNo(String serialNo) {
        long count = payRefundMapper.selectCount(PayRefundEntity::getSerialNo, serialNo);
        if (count > 0) {
            throw ServiceExceptionUtil.exception(PayErrorCodeConstants.PAY_SERIAL_REPEAT);
        }
    }

}
