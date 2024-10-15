DROP TABLE IF EXISTS `pay_app`;
CREATE TABLE `pay_app` (
    `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键编号',
    `name` varchar(128)  DEFAULT NULL COMMENT '支付 app 名称',
    `status` int NOT NULL COMMENT '状态 0-失效 1-有效',
    `remark` varchar(255)  DEFAULT NULL COMMENT '备注',
    `order_notify_url` varchar(512)  DEFAULT NULL COMMENT '支付通知回调',
    `refund_notify_url` varchar(512)  DEFAULT NULL COMMENT '退款通知回调',
    `create_by` varchar(128)  DEFAULT NULL COMMENT '创建人',
    `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_by` varchar(128)  DEFAULT NULL COMMENT '更新人',
    `update_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` bit(1) DEFAULT b'0' COMMENT '是否删除 0-否 1-是',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB COMMENT='支付应用';

DROP TABLE IF EXISTS `pay_channel`;
CREATE TABLE `pay_channel` (
    `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键编号',
    `app_id` bigint NOT NULL COMMENT '应用编号',
    `code` varchar(32)  NOT NULL COMMENT '支付渠道编号 唯一校验',
    `status` int NOT NULL COMMENT '状态 0-失效 1-有效',
    `remark` varchar(255)  DEFAULT NULL COMMENT '备注',
    `config` text  NOT NULL COMMENT '渠道配置',
    `fee_rate` decimal(20,4) DEFAULT '0.0000' COMMENT '费率',
    `create_by` varchar(128)  DEFAULT NULL COMMENT '创建人',
    `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_by` varchar(128)  DEFAULT NULL COMMENT '更新人',
    `update_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` bit(1) DEFAULT b'0' COMMENT '是否删除 0-否 1-是',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB COMMENT='支付渠道';

DROP TABLE IF EXISTS `SEQUENCE_CONFIG`;
CREATE TABLE `SEQUENCE_CONFIG` (
    `SEQ_NAME` varchar(255) NOT NULL COMMENT '序列名称',
    `MIN_VAL` decimal(35,0) NOT NULL COMMENT '最小值',
    `MAX_VAL` decimal(35,0) NOT NULL COMMENT '最大值',
    `CUR_VAL` decimal(35,0) NOT NULL COMMENT '当前使用值',
    `STEP_VAL` int(10) NOT NULL DEFAULT '1' COMMENT '递增值',
    `CYCLE` char(1) NOT NULL DEFAULT 'Y' COMMENT '是否循环,默认为Y表示循环',
    `UPDATE_TIME` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`SEQ_NAME`)
) ENGINE=InnoDB COMMENT='序列号';

DROP TABLE IF EXISTS `pay_order`;
CREATE TABLE `pay_order` (
    `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键编号',
    `serial_no` varchar(64) NOT NULL COMMENT '交易流水号',
    `channel_id` bigint NOT NULL COMMENT '支付渠道编号',
    `out_trade_no` varchar(64) NOT NULL COMMENT '外部订单编号',
    `transaction_id` varchar(64) DEFAULT NULL COMMENT '渠道交易单号',
    `subject` varchar(128) NOT NULL COMMENT '商品标题',
    `body` varchar(256) DEFAULT NULL COMMENT '商品描述',
    `notify_url` varchar(256) NOT NULL COMMENT '通知地址',
    `return_url` varchar(256) DEFAULT NULL COMMENT '通知地址 支付宝使用',
    `price` int NOT NULL COMMENT '支付金额',
    `expire_time` datetime DEFAULT NULL COMMENT '交易结束时间',
    `channel_extras` varchar(512) DEFAULT NULL COMMENT '扩展数据',
    `status` int NOT NULL COMMENT '支付状态 0-支付失败 1-支付成功 2-支付中',
    `remark` varchar(255)  DEFAULT NULL COMMENT '备注',
    `success_time` datetime DEFAULT NULL COMMENT '成功时间',
    `notify_time` datetime DEFAULT NULL COMMENT '通知时间',
    `create_by` varchar(128)  DEFAULT NULL COMMENT '创建人',
    `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_by` varchar(128)  DEFAULT NULL COMMENT '更新人',
    `update_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` bit(1) DEFAULT b'0' COMMENT '是否删除 0-否 1-是',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB COMMENT='支付订单信息';

DROP TABLE IF EXISTS `pay_refund`;
CREATE TABLE `pay_refund` (
    `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键编号',
    `serial_no` varchar(64) NOT NULL COMMENT '交易流水号',
    `channel_id` bigint NOT NULL COMMENT '支付渠道编号',
    `out_trade_no` varchar(64) NOT NULL COMMENT '外部订单编号',
    `out_refund_no` varchar(64) NOT NULL COMMENT '外部退款编号',
    `transaction_id` varchar(64) DEFAULT NULL COMMENT '渠道交易单号',
    `refund_id` varchar(64) DEFAULT NULL COMMENT '渠道退款单号',
    `pay_price` int NOT NULL COMMENT '支付金额',
    `refund_price` int NOT NULL COMMENT '退款金额',
    `reason` varchar(256) NOT NULL COMMENT '退款原因',
    `notify_url` varchar(256) NOT NULL COMMENT '通知地址',
    `success_time` datetime DEFAULT NULL COMMENT '成功时间',
    `notify_time` datetime DEFAULT NULL COMMENT '通知时间',
    `status` int NOT NULL COMMENT '退款状态',
    `notify_status` int DEFAULT NULL COMMENT '通知退款状态',
    `remark` varchar(255)  DEFAULT NULL COMMENT '备注',
    `create_by` varchar(128)  DEFAULT NULL COMMENT '创建人',
    `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_by` varchar(128)  DEFAULT NULL COMMENT '更新人',
    `update_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` bit(1) DEFAULT b'0' COMMENT '是否删除 0-否 1-是',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB COMMENT='退款订单信息';

DROP TABLE IF EXISTS `pay_channel_notify`;
CREATE TABLE `pay_channel_notify` (
    `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键编号',
    `channel` varchar(32) DEFAULT NULL COMMENT '渠道 wx-微信 ali-阿里',
    `notify_id` varchar(64) DEFAULT NULL COMMENT '通知编号',
    `notify_type` int DEFAULT NULL COMMENT '通知类型 1-支付 2-退款',
    `notify_request_params` longtext DEFAULT NULL COMMENT '通知参数 param',
    `notify_request` longtext DEFAULT NULL COMMENT '通知参数 body',
    `notify_result` text DEFAULT NULL COMMENT '通知结果',
    `status` int DEFAULT NULL COMMENT '状态 0-失败 1-成功',
    `remark` varchar(255)  DEFAULT NULL COMMENT '备注',
    `create_by` varchar(128)  DEFAULT NULL COMMENT '创建人',
    `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_by` varchar(128)  DEFAULT NULL COMMENT '更新人',
    `update_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` bit(1) DEFAULT b'0' COMMENT '是否删除 0-否 1-是',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB COMMENT='渠道通知回调';

DROP TABLE IF EXISTS `pay_notify_task`;
CREATE TABLE `pay_notify_task` (
    `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键编号',
    `channel_Id` bigint DEFAULT NULL COMMENT '渠道 wx-微信 ali-阿里',
    `type` int DEFAULT NULL COMMENT '通知类型',
    `data_id` bigint DEFAULT NULL COMMENT '数据编号，根据不同 type 进行关联：',
    `merchant_order_id` varchar(64) DEFAULT NULL COMMENT '商户订单编号',
    `merchant_refund_id` varchar(64) DEFAULT NULL COMMENT '商户退款编号',
    `status` varchar(64) DEFAULT NULL COMMENT '通知状态',
    `next_notify_time` datetime DEFAULT NULL COMMENT 'nextNotifyTime',
    `last_execute_time` datetime DEFAULT NULL COMMENT '最后一次执行时间',
    `notify_times` int DEFAULT NULL COMMENT '当前通知次数',
    `max_notify_times` int DEFAULT NULL COMMENT '最大可通知次数',
    `notify_frequency` varchar(256) DEFAULT NULL COMMENT '通知频率',
    `notify_url` varchar(256) DEFAULT NULL COMMENT '通知地址',
    `notify_body` text DEFAULT NULL COMMENT '通知内容',
    `remark` varchar(255)  DEFAULT NULL COMMENT '备注',
    `create_by` varchar(128)  DEFAULT NULL COMMENT '创建人',
    `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_by` varchar(128)  DEFAULT NULL COMMENT '更新人',
    `update_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` bit(1) DEFAULT b'0' COMMENT '是否删除 0-否 1-是',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB COMMENT='商家通知回调任务';

DROP TABLE IF EXISTS `pay_notify_log`;
CREATE TABLE `pay_notify_log` (
    `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键编号',
    `task_id` bigint DEFAULT NULL COMMENT '通知任务编号',
    `notify_times` int DEFAULT NULL COMMENT '第几次被通知',
    `response` text DEFAULT NULL COMMENT 'HTTP 响应结果',
    `status` int DEFAULT NULL COMMENT '状态',
    `remark` varchar(255)  DEFAULT NULL COMMENT '备注',
    `create_by` varchar(128)  DEFAULT NULL COMMENT '创建人',
    `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_by` varchar(128)  DEFAULT NULL COMMENT '更新人',
    `update_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` bit(1) DEFAULT b'0' COMMENT '是否删除 0-否 1-是',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB COMMENT='商家通知回调日志';
