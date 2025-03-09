package com.payment.contants;

import com.framework.common.pojo.ErrorCode;

/**
 * @author shen_dy@halcyonz.com
 * @date 2024/4/12
 */
public interface PayErrorCodeConstants {

	// 支付应用 1_002_001_000 段
	ErrorCode PAY_APP_NOT_FOUND = new ErrorCode(1_002_001_001, "支付应用不存在或已注销");
	// 支付渠道 1_002_002_000 段
	ErrorCode PAY_CHANNEL_NOT_FOUND = new ErrorCode(1_002_002_001, "支付渠道不存在或已关闭");
	ErrorCode PAY_CHANNEL_EXISTS = new ErrorCode(1_002_002_001, "支付渠道{}已存在");
	// 支付订单 1_002_003_000 段
	ErrorCode PAY_ORDER_NOT_EXISTS = new ErrorCode(1_002_003_001, "支付订单不存在");
	ErrorCode PAY_ORDER_EXISTS = new ErrorCode(1_002_003_002, "同一订单请勿重复提交");
	ErrorCode PAY_ORDER_UNIFIED_FAILED = new ErrorCode(1_002_003_003, "下单失败");
	// 退款订单 1_002_004_000 段
	ErrorCode PAY_REFUND_REPEAT_APPLY = new ErrorCode(1_002_004_001, "同一退款订单请勿重复提交");
	ErrorCode PAY_REFUND_NOT_EXISTS = new ErrorCode(1_002_004_002, "退款订单不存在");
	ErrorCode PAY_REFUND_REPEAT_CONFIRM = new ErrorCode(1_002_004_003, "退款已受理，请勿重复提交");
	// 退款订单 1_002_005_000 段
	ErrorCode PAY_SERIAL_REPEAT = new ErrorCode(1_002_005_001, "请求流水号重复");

}
