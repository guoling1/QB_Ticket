/*
 Navicat Premium Data Transfer

 Source Server         : 192.168.1.21
 Source Server Type    : MySQL
 Source Server Version : 50709
 Source Host           : 192.168.1.21
 Source Database       : db_qb

 Target Server Type    : MySQL
 Target Server Version : 50709
 File Encoding         : utf-8

 Date: 11/18/2016 14:03:51 PM
*/

SET NAMES utf8;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
--  Table structure for `qb_contact_information`
-- ----------------------------
DROP TABLE IF EXISTS `qb_contact_information`;
CREATE TABLE `qb_contact_information` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) DEFAULT NULL,
  `user_name` varchar(20) DEFAULT NULL,
  `card_type` int(11) DEFAULT NULL,
  `card_no` varchar(20) DEFAULT NULL,
  `user_phone` varchar(20) DEFAULT NULL,
  `user_type` int(11) DEFAULT NULL,
  `status` int(2) DEFAULT NULL COMMENT '0.审核通过1.审核未通过',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `birth` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `qb_get_param_record`
-- ----------------------------
DROP TABLE IF EXISTS `qb_get_param_record`;
CREATE TABLE `qb_get_param_record` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `app_id` varchar(50) DEFAULT NULL COMMENT 'appid',
  `nonce_str` varchar(50) DEFAULT NULL COMMENT '不长于50位',
  `status` tinyint(4) DEFAULT NULL COMMENT '0.正常\r\n            1.删除',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=162 DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `qb_pay_result_record`
-- ----------------------------
DROP TABLE IF EXISTS `qb_pay_result_record`;
CREATE TABLE `qb_pay_result_record` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '编码',
  `pay_channel` varchar(20) DEFAULT NULL COMMENT '支付渠道(fastpay|alipay|weixin)',
  `req_sn` varchar(100) DEFAULT NULL COMMENT '流水号',
  `amount` varchar(100) DEFAULT NULL COMMENT '支付金额',
  `pay_result` char(2) DEFAULT NULL COMMENT '支付结果（1成功2失败）',
  `pay_params` text COMMENT '支付参数',
  `result_params` text COMMENT '支付结果参数',
  `status` tinyint(4) DEFAULT NULL COMMENT '0.正常\r\n            1.删除',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=103 DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `qb_pay_sequence`
-- ----------------------------
DROP TABLE IF EXISTS `qb_pay_sequence`;
CREATE TABLE `qb_pay_sequence` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '编码',
  `pay_channel` varchar(20) DEFAULT NULL COMMENT 'fastpay|alipay|weixin',
  `req_sn` varchar(50) DEFAULT NULL COMMENT '支付流水号',
  `amount` bigint(20) DEFAULT NULL COMMENT '支付金额(分)',
  `pay_params` text COMMENT '支付参数',
  `result_params` text COMMENT '返回参数',
  `pay_result` char(2) DEFAULT NULL COMMENT '支付结果\r\n            S-成功\r\n       E-异常     U-处理中\r\n            N-待支付\r\n            F-失败\r\n            ',
  `status` tinyint(4) DEFAULT '0' COMMENT '0.正常\r\n            1.删除',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `qb_refund_result_record`
-- ----------------------------
DROP TABLE IF EXISTS `qb_refund_result_record`;
CREATE TABLE `qb_refund_result_record` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '编码',
  `refund_channel` varchar(20) DEFAULT NULL COMMENT '退款渠道(fastpay|alipay|weixin)',
  `req_sn` varchar(100) DEFAULT NULL COMMENT '退款流水号',
  `amount` varchar(100) DEFAULT NULL COMMENT '退款金额',
  `refund_result` char(2) DEFAULT NULL COMMENT '退款结果(1成功 2失败)',
  `refund_params` text COMMENT '退款参数',
  `result_params` text COMMENT '退款结果参数',
  `status` tinyint(4) DEFAULT NULL COMMENT '0.正常\r\n            1.删除',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=37 DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `qb_refund_sequence`
-- ----------------------------
DROP TABLE IF EXISTS `qb_refund_sequence`;
CREATE TABLE `qb_refund_sequence` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '编码',
  `pay_channel` varchar(20) DEFAULT NULL COMMENT 'fastpay|alipay|weixin',
  `refund_sn` varchar(20) DEFAULT NULL COMMENT '退款流水号',
  `req_sn` varchar(100) DEFAULT NULL COMMENT '支付流水号',
  `amount` varchar(100) DEFAULT NULL COMMENT '退款金额',
  `refund_params` text COMMENT '退款参数',
  `result_params` text COMMENT '返回参数',
  `refund_result` char(2) DEFAULT NULL COMMENT '退款结果\r\n            S-成功\r\n   E-异常         U-已受理\r\n            F-失败',
  `status` tinyint(4) DEFAULT NULL COMMENT '0.正常\r\n            1.删除',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `sys_menu`
-- ----------------------------
DROP TABLE IF EXISTS `sys_menu`;
CREATE TABLE `sys_menu` (
  `id` bigint(20) NOT NULL,
  `parent_id` bigint(20) DEFAULT NULL,
  `menu_name` varchar(200) DEFAULT NULL,
  `menu_url` varchar(200) DEFAULT NULL,
  `descr` varchar(200) DEFAULT NULL,
  `type` char(2) DEFAULT NULL,
  `status` char(2) DEFAULT NULL,
  `create_time` timestamp NULL DEFAULT NULL,
  `update_time` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `sys_role`
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role` (
  `id` bigint(20) NOT NULL,
  `role_name` varchar(20) DEFAULT NULL,
  `descr` varchar(200) DEFAULT NULL,
  `type` char(2) DEFAULT NULL,
  `status` char(2) DEFAULT NULL,
  `create_time` timestamp NULL DEFAULT NULL,
  `update_time` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `sys_role_menu_rel`
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_menu_rel`;
CREATE TABLE `sys_role_menu_rel` (
  `id` bigint(20) NOT NULL,
  `role_id` bigint(20) DEFAULT NULL,
  `menu_id` bigint(20) DEFAULT NULL,
  `descr` varchar(200) DEFAULT NULL,
  `type` char(2) DEFAULT NULL,
  `state` char(2) DEFAULT NULL,
  `create_time` timestamp NULL DEFAULT NULL,
  `update_time` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `sys_user`
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user` (
  `id` bigint(20) NOT NULL,
  `phone` varchar(20) DEFAULT NULL,
  `user_account` varchar(100) DEFAULT NULL,
  `email` varchar(200) DEFAULT NULL,
  `user_name` varchar(200) DEFAULT NULL,
  `user_nickname` varchar(200) DEFAULT NULL,
  `user_pwd` varchar(50) DEFAULT NULL,
  `sex` char(2) DEFAULT NULL,
  `last_login_date` timestamp NULL DEFAULT NULL,
  `descr` varchar(200) DEFAULT NULL,
  `type` char(2) DEFAULT NULL,
  `user_level` int(255) DEFAULT NULL,
  `is_pass` int(255) DEFAULT NULL,
  `state` char(2) DEFAULT NULL,
  `create_time` timestamp NULL DEFAULT NULL,
  `update_time` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `sys_user_role_rel`
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_role_rel`;
CREATE TABLE `sys_user_role_rel` (
  `id` bigint(20) NOT NULL,
  `user_id` bigint(20) DEFAULT NULL,
  `role_id` bigint(20) DEFAULT NULL,
  `descr` varchar(200) DEFAULT NULL,
  `type` char(2) DEFAULT NULL,
  `state` char(2) DEFAULT NULL,
  `create_time` timestamp NULL DEFAULT NULL,
  `update_time` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `tb_bank_card_bin`
-- ----------------------------
DROP TABLE IF EXISTS `tb_bank_card_bin`;
CREATE TABLE `tb_bank_card_bin` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '基金渠道方id',
  `bank_code` varchar(20) DEFAULT NULL COMMENT '银行卡bin编码',
  `shorthand` varchar(255) DEFAULT NULL COMMENT '英文缩写',
  `bank_name` varchar(100) DEFAULT NULL COMMENT '银行标准名称',
  `full_name` varchar(255) DEFAULT NULL COMMENT '银行全称',
  `card_name` varchar(100) DEFAULT NULL COMMENT '银行卡名称',
  `card_length` varchar(100) DEFAULT NULL COMMENT '卡长度',
  `card_no` varchar(255) DEFAULT NULL COMMENT '银行卡主账号',
  `bin_length` varchar(100) DEFAULT NULL COMMENT '卡bin长度',
  `bin_no` varchar(20) DEFAULT NULL COMMENT '银行卡bin号',
  `card_type` varchar(255) DEFAULT NULL COMMENT '卡类型',
  `card_type_code` smallint(255) DEFAULT NULL COMMENT '卡类型编码 0借记卡 1借贷卡 2预付费卡',
  `status` tinyint(4) DEFAULT '0' COMMENT '状态',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3618 DEFAULT CHARSET=utf8 COMMENT='银行卡bin';

-- ----------------------------
--  Table structure for `tb_bind_card`
-- ----------------------------
DROP TABLE IF EXISTS `tb_bind_card`;
CREATE TABLE `tb_bind_card` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `uid` varchar(50) DEFAULT NULL COMMENT '三方用户编码',
  `card_no` varchar(300) DEFAULT NULL COMMENT '卡号',
  `bank_code` varchar(20) DEFAULT NULL COMMENT '卡bin',
  `account_name` varchar(50) DEFAULT NULL COMMENT '身份证名',
  `card_type` varchar(10) DEFAULT NULL COMMENT '00：身份证,1: 户口簿，2：护照,3.军官证,4.士兵证，5. 港澳居民来往内地通行证,6. 台湾同胞来往内地通行证,7. 临时身份证,8. 外国人居留证,9. 警官证, X.其他证件',
  `card_id` varchar(300) DEFAULT NULL COMMENT '身份证号',
  `phone` varchar(20) DEFAULT NULL COMMENT '手机号',
  `pay_pwd` varchar(100) DEFAULT NULL COMMENT '支付密码',
  `status` tinyint(2) DEFAULT NULL COMMENT '0.正常\r\n 1.删除',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `tb_charge_money_order`
-- ----------------------------
DROP TABLE IF EXISTS `tb_charge_money_order`;
CREATE TABLE `tb_charge_money_order` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `order_form_id` bigint(20) unsigned DEFAULT '0' COMMENT '订单ID',
  `grab_ticket_form_id` bigint(20) unsigned DEFAULT '0' COMMENT '抢票单id',
  `total_amount` decimal(10,2) unsigned DEFAULT '0.00' COMMENT '用户总付款金额(每个订单)',
  `buy_ticket_package` decimal(10,2) unsigned DEFAULT '0.00' COMMENT '出票套餐价格',
  `grab_ticket_package` decimal(10,2) unsigned DEFAULT '0.00' COMMENT '抢票套餐价格',
  `status` tinyint(2) unsigned DEFAULT '0',
  `createTime` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updateTime` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=152 DEFAULT CHARSET=utf8 COMMENT='收款单';

-- ----------------------------
--  Table structure for `tb_confirm_order_exception_record`
-- ----------------------------
DROP TABLE IF EXISTS `tb_confirm_order_exception_record`;
CREATE TABLE `tb_confirm_order_exception_record` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `order_form_id` bigint(20) DEFAULT '0' COMMENT '订单id',
  `payment_sn` varchar(255) DEFAULT '' COMMENT '支付流水号',
  `amount` decimal(10,2) DEFAULT '0.00' COMMENT '支付金额',
  `remark` varchar(255) DEFAULT '' COMMENT '备注',
  `status` tinyint(2) DEFAULT '0' COMMENT '状态（0：未处理，1：已处理）',
  `createTime` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updateTime` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='确认出票异常记录';

-- ----------------------------
--  Table structure for `tb_contact_info`
-- ----------------------------
DROP TABLE IF EXISTS `tb_contact_info`;
CREATE TABLE `tb_contact_info` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '编码id',
  `uid` varchar(100) DEFAULT NULL COMMENT '三方账号（如万年历）',
  `birthday` varchar(50) DEFAULT NULL COMMENT '格式 1990-01-21',
  `sex` int(11) DEFAULT NULL COMMENT '0男1女',
  `identy` varchar(300) DEFAULT NULL COMMENT '证件号',
  `tel` varchar(15) DEFAULT NULL COMMENT '手机号',
  `identy_type` char(2) DEFAULT NULL COMMENT '1:二代身份证，2:一代身份证，C:港澳通行证，G:台湾通行证，B:护照',
  `country` varchar(10) DEFAULT NULL COMMENT '国家编码',
  `check_status` int(11) DEFAULT NULL COMMENT '0.审核通过 1.审核未通过',
  `email` varchar(50) DEFAULT NULL COMMENT '邮箱',
  `address` varchar(500) DEFAULT NULL COMMENT '地址',
  `name` varchar(100) DEFAULT NULL COMMENT '名字',
  `person_type` int(11) DEFAULT NULL COMMENT '1.成人票 2.儿童票 3.学生票 4.残军票',
  `is_user_self` int(11) DEFAULT NULL COMMENT '是否为自己 0.是 1.不是',
  `province_name` varchar(100) DEFAULT NULL COMMENT '省份名称',
  `province_code` varchar(100) DEFAULT NULL COMMENT '省份编码',
  `school_code` varchar(100) DEFAULT NULL COMMENT '学校代码',
  `school_name` varchar(100) DEFAULT NULL COMMENT '学校名称',
  `student_no` varchar(20) DEFAULT NULL COMMENT '学号',
  `school_system` varchar(50) DEFAULT NULL COMMENT '学制',
  `preference_from_station_name` varchar(100) DEFAULT NULL COMMENT '起始地名称',
  `preference_from_station_code` varchar(100) DEFAULT NULL COMMENT '起始地代码',
  `preference_to_station_name` varchar(100) DEFAULT NULL COMMENT '到达地名称',
  `preference_to_station_code` varchar(100) DEFAULT NULL COMMENT '到达地代码',
  `enter_year` varchar(100) DEFAULT NULL COMMENT '入学年份',
  `status` tinyint(2) DEFAULT NULL COMMENT '0正常 1删除',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=165 DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `tb_fastpay_record`
-- ----------------------------
DROP TABLE IF EXISTS `tb_fastpay_record`;
CREATE TABLE `tb_fastpay_record` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `uid` varchar(50) DEFAULT NULL COMMENT '三方用户编码',
  `card_no` varchar(300) DEFAULT NULL COMMENT '卡号',
  `bank_code` varchar(20) DEFAULT NULL COMMENT '卡bin',
  `account_name` varchar(50) DEFAULT NULL COMMENT '身份证名',
  `card_type` varchar(10) DEFAULT NULL COMMENT '00：身份证,1: 户口簿，2：护照,3.军官证,4.士兵证，5. 港澳居民来往内地通行证,6. 台湾同胞来往内地通行证,7. 临时身份证,8. 外国人居留证,9. 警官证, X.其他证件',
  `card_id` varchar(300) DEFAULT NULL COMMENT '身份证号',
  `phone` varchar(20) DEFAULT NULL COMMENT '手机号',
  `pay_pwd` varchar(100) DEFAULT NULL COMMENT '支付密码',
  `status` tinyint(2) DEFAULT NULL COMMENT '0.正常\r\n 1.删除',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `tb_grab_ticket_form`
-- ----------------------------
DROP TABLE IF EXISTS `tb_grab_ticket_form`;
CREATE TABLE `tb_grab_ticket_form` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `uid` varchar(255) DEFAULT NULL COMMENT '用户id',
  `order_id` varchar(255) DEFAULT '' COMMENT 'jkm使用方订单号',
  `transaction_id` varchar(255) DEFAULT '' COMMENT '交易订单号(对方提供)',
  `phone` varchar(255) DEFAULT '' COMMENT '联系手机,通知出票信息',
  `payment_sn` varchar(255) DEFAULT '' COMMENT '支付流水号',
  `ticket_num` int(11) DEFAULT '0' COMMENT '抢票数量',
  `grab_ticket_total_price` decimal(20,2) DEFAULT '0.00' COMMENT '抢票火车票的总价格',
  `grab_total_price` decimal(20,2) DEFAULT '0.00' COMMENT '抢票订单总价格',
  `ticket_total_price` decimal(20,2) DEFAULT '0.00' COMMENT '实际收取火车票的总价格',
  `total_price` decimal(20,2) DEFAULT '0.00' COMMENT '实际收取订单总价格',
  `grab_start_time` varchar(255) DEFAULT '' COMMENT '抢票出发日期 2015-09-09',
  `grab_time_type` tinyint(4) DEFAULT '0' COMMENT '抢票时效类型',
  `first_start_time` varchar(255) DEFAULT '' COMMENT '最早发车时间 2015-09-10 13:00',
  `from_station_name` varchar(255) DEFAULT '' COMMENT '出发站名称',
  `from_station_code` varchar(255) DEFAULT '' COMMENT '出发站简码',
  `to_station_name` varchar(255) DEFAULT '' COMMENT '到达站名称',
  `to_station_code` varchar(255) DEFAULT '' COMMENT '到达站简码',
  `train_codes` varchar(255) DEFAULT '' COMMENT '抢票车次',
  `seat_types` varchar(255) DEFAULT '' COMMENT '座位类型 (字符串拼接, 以,隔离)',
  `grab_ticket_package` tinyint(4) DEFAULT '0' COMMENT '抢票套餐',
  `buy_ticket_package` tinyint(4) DEFAULT NULL COMMENT '出票套餐',
  `start_date` varchar(255) DEFAULT '' COMMENT ' 抢票成功发车日期 2016-10-08',
  `end_date` varchar(255) DEFAULT '' COMMENT '抢票成功到达日期 2016-10-09',
  `start_time` varchar(255) DEFAULT '' COMMENT '抢票成功发车时间 21:35',
  `end_time` varchar(255) DEFAULT '' COMMENT '抢票成功到达时间 10:04',
  `run_time` varchar(255) DEFAULT '' COMMENT '运行时间',
  `checi` varchar(255) DEFAULT '' COMMENT '抢票成功车次',
  `order_number` varchar(255) DEFAULT '' COMMENT ' 取票单号（电子单号）',
  `passenger_info` varchar(255) DEFAULT '' COMMENT '乘客信息 (id, 姓名, 身份证号, 乘客类型(成人, 儿童) )',
  `remark` varchar(255) DEFAULT '' COMMENT '备注',
  `status` tinyint(2) unsigned DEFAULT '0',
  `createTime` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updateTime` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=99 DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `tb_hy_channel_request_record`
-- ----------------------------
DROP TABLE IF EXISTS `tb_hy_channel_request_record`;
CREATE TABLE `tb_hy_channel_request_record` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '基金渠道方id',
  `order_id` varchar(50) NOT NULL DEFAULT '' COMMENT '使用方订单号',
  `method` varchar(30) DEFAULT '' COMMENT '业务类型',
  `ret_code` varchar(32) DEFAULT '' COMMENT '返回码',
  `request` text COMMENT '请求',
  `response` text COMMENT '返回',
  `remark` varchar(255) DEFAULT '' COMMENT '备注',
  `time` bigint(20) unsigned DEFAULT '0' COMMENT '请求耗时,单位毫秒',
  `status` tinyint(4) unsigned DEFAULT '0' COMMENT '状态',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`),
  KEY `idx_sn` (`order_id`) USING BTREE,
  KEY `idx_business_type` (`method`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=3945 DEFAULT CHARSET=utf8 COMMENT='银盛渠道方请求记录';

-- ----------------------------
--  Table structure for `tb_message_template`
-- ----------------------------
DROP TABLE IF EXISTS `tb_message_template`;
CREATE TABLE `tb_message_template` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '消息模板id',
  `notice_type` int(10) DEFAULT NULL COMMENT '消息类型',
  `message_template` varchar(255) DEFAULT '' COMMENT '消息模板',
  `status` tinyint(4) DEFAULT '0' COMMENT '状态',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uniq_notice_type` (`notice_type`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1751 DEFAULT CHARSET=utf8 COMMENT='短信模板';

-- ----------------------------
--  Table structure for `tb_order_form`
-- ----------------------------
DROP TABLE IF EXISTS `tb_order_form`;
CREATE TABLE `tb_order_form` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `uid` varchar(255) DEFAULT '0' COMMENT '订单所有人id',
  `payment_sn` varchar(60) DEFAULT '' COMMENT '支付流水号',
  `mobile` varchar(20) DEFAULT '' COMMENT '订票人手机号',
  `order_id` varchar(255) DEFAULT '' COMMENT '商户订单号',
  `out_order_id` varchar(255) DEFAULT '' COMMENT '外部订单号',
  `order_number` varchar(255) DEFAULT '' COMMENT '取票单号',
  `price` decimal(10,2) DEFAULT '0.00' COMMENT '车票正常价格',
  `ticket_total_price` decimal(10,2) DEFAULT '0.00' COMMENT '火车票总价格',
  `total_price` decimal(10,2) DEFAULT '0.00' COMMENT '订单总票价',
  `buy_ticket_package_price` decimal(10,2) DEFAULT '0.00' COMMENT '出票套餐价格',
  `grab_ticket_package_price` decimal(10,2) DEFAULT '0.00' COMMENT '抢票套餐金额',
  `buy_ticket_package_id` int(2) DEFAULT '0' COMMENT '出票套餐类型',
  `grab_ticket_package_id` int(2) DEFAULT '0' COMMENT '抢票套餐类型',
  `from_station_name` varchar(255) DEFAULT '' COMMENT '出发站名称',
  `from_station_code` varchar(255) DEFAULT '' COMMENT '出发站简码',
  `to_station_name` varchar(255) DEFAULT '' COMMENT '到达站名称',
  `to_station_code` varchar(255) DEFAULT '' COMMENT '到达站简码',
  `zw_code` varchar(255) DEFAULT '' COMMENT '坐席code',
  `zw_name` varchar(255) DEFAULT '' COMMENT '坐席名称',
  `start_date` varchar(255) DEFAULT '' COMMENT '发车日期',
  `end_date` varchar(255) DEFAULT '' COMMENT '到达日期',
  `start_time` varchar(255) DEFAULT '' COMMENT '发车时间',
  `end_time` varchar(255) DEFAULT '' COMMENT '到达时间',
  `run_time` varchar(255) DEFAULT '' COMMENT '历时',
  `expire_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '支付过期时间',
  `checi` varchar(255) DEFAULT '' COMMENT '车次',
  `login_user_name` varchar(255) DEFAULT '' COMMENT '1230用户名',
  `login_user_password` varchar(255) DEFAULT '' COMMENT '12306密码',
  `remark` varchar(255) DEFAULT '' COMMENT '备注',
  `status` tinyint(2) DEFAULT '0' COMMENT '订单状态（1, "订单初始化"；2, "订单请求中"， 3, "订单请求成功"， 4, "订单购买成功"， 5, "订单购买失败"）',
  `createTime` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updateTime` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=305 DEFAULT CHARSET=utf8 COMMENT='订单';

-- ----------------------------
--  Table structure for `tb_order_form_detail`
-- ----------------------------
DROP TABLE IF EXISTS `tb_order_form_detail`;
CREATE TABLE `tb_order_form_detail` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `is_grab` tinyint(3) unsigned DEFAULT '0' COMMENT '0否1是  是否是抢票单',
  `mobile` varchar(20) DEFAULT '' COMMENT '手机号',
  `order_form_id` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '订单ID',
  `grab_ticket_form_id` bigint(20) DEFAULT '0',
  `order_form_detail_id` bigint(20) DEFAULT '0' COMMENT '出票订单id',
  `passenger_id` varchar(255) DEFAULT '' COMMENT '联系人ID',
  `passenger_name` varchar(255) DEFAULT '' COMMENT '乘客名字',
  `passport_se_no` varchar(50) DEFAULT '' COMMENT '乘客证件号码',
  `passport_type_se_id` varchar(2) DEFAULT '' COMMENT '乘客证件类型id',
  `passport_type_se_name` varchar(255) DEFAULT '' COMMENT '乘客证件类型名称',
  `piao_type` char(2) DEFAULT '' COMMENT '票的类型',
  `ticket_no` varchar(255) DEFAULT '' COMMENT '票号',
  `checi` varchar(255) DEFAULT '' COMMENT '车次',
  `cxin` varchar(255) DEFAULT '' COMMENT '几车厢几号',
  `price` decimal(20,2) unsigned DEFAULT '0.00' COMMENT '火车站出票价格',
  `status` tinyint(3) unsigned DEFAULT '0',
  `remark` varchar(50) DEFAULT NULL,
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=256 DEFAULT CHARSET=utf8 COMMENT='订单对应的票的信息';

-- ----------------------------
--  Table structure for `tb_order_form_refund_exception_record`
-- ----------------------------
DROP TABLE IF EXISTS `tb_order_form_refund_exception_record`;
CREATE TABLE `tb_order_form_refund_exception_record` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `payment_sn` varchar(30) DEFAULT '' COMMENT '支付流水号',
  `refund_amount` decimal(10,2) DEFAULT '0.00' COMMENT '退款金额',
  `order_form_id` bigint(20) DEFAULT '0' COMMENT '代购单id',
  `grab_order_form_id` bigint(20) DEFAULT '0' COMMENT '抢票的id',
  `is_grab` tinyint(2) DEFAULT '0' COMMENT '是否是抢票单（0：表示代购单， 1：表示抢票单）',
  `remark` varchar(100) DEFAULT '' COMMENT '备注',
  `status` tinyint(2) DEFAULT '0' COMMENT '状态（0：表示未处理的， 1：表示已经处理的）',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COMMENT='订单退款异常记录';

-- ----------------------------
--  Table structure for `tb_passengerse_info`
-- ----------------------------
DROP TABLE IF EXISTS `tb_passengerse_info`;
CREATE TABLE `tb_passengerse_info` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `uid` varchar(255) DEFAULT NULL COMMENT '订单所有人id',
  `order_id` varchar(255) DEFAULT '' COMMENT '商户订单号',
  `out_order_id` varchar(255) DEFAULT '' COMMENT '外部订单号',
  `train_date` date DEFAULT NULL COMMENT '发车日期',
  `price` decimal(10,2) DEFAULT '0.00' COMMENT '车票正常价格',
  `total_price` decimal(10,2) DEFAULT '0.00' COMMENT '订单总票价',
  `from_station_name` varchar(255) DEFAULT '' COMMENT '出发站名称',
  `from_station_code` varchar(255) DEFAULT '' COMMENT '出发站简码',
  `to_station_name` varchar(255) DEFAULT '' COMMENT '到达站名称',
  `to_station_code` varchar(255) DEFAULT '' COMMENT '到达站简码',
  `checi` varchar(255) DEFAULT '' COMMENT '车次',
  `login_user_name` varchar(255) DEFAULT '' COMMENT '1230用户名',
  `login_user_password` varchar(255) DEFAULT '' COMMENT '12306密码',
  `remark` varchar(255) DEFAULT '' COMMENT '备注',
  `status` tinyint(2) DEFAULT '0' COMMENT '订单状态（1, "订单初始化"；2, "订单请求中"， 3, "订单请求成功"， 4, "订单购买成功"， 5, "订单购买失败"）',
  `createTime` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updateTime` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=36 DEFAULT CHARSET=utf8 COMMENT='订单';

-- ----------------------------
--  Table structure for `tb_policy_order`
-- ----------------------------
DROP TABLE IF EXISTS `tb_policy_order`;
CREATE TABLE `tb_policy_order` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `order_form_detail_id` bigint(20) unsigned DEFAULT '0' COMMENT '订单详情ID',
  `ins_product_no` varchar(255) DEFAULT '' COMMENT '保险产品代码',
  `flight_date` datetime DEFAULT NULL,
  `flight_number` varchar(255) DEFAULT '',
  `serial_no` varchar(255) DEFAULT '',
  `contract_name` varchar(255) DEFAULT '',
  `contract_type` tinyint(3) unsigned DEFAULT NULL,
  `gender` varchar(255) DEFAULT NULL,
  `card_type` tinyint(3) unsigned DEFAULT NULL,
  `card_no` varchar(255) DEFAULT NULL,
  `birthday` date DEFAULT NULL,
  `phone` varchar(255) DEFAULT NULL,
  `policy_no` varchar(255) DEFAULT NULL,
  `apply_no` varchar(255) DEFAULT NULL,
  `print_no` varchar(255) DEFAULT NULL,
  `remark` varchar(255) DEFAULT '' COMMENT 'beizhu',
  `status` tinyint(4) unsigned DEFAULT '0',
  `createTime` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updateTime` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=53 DEFAULT CHARSET=utf8 COMMENT='保险单';

-- ----------------------------
--  Table structure for `tb_query_history`
-- ----------------------------
DROP TABLE IF EXISTS `tb_query_history`;
CREATE TABLE `tb_query_history` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `uid` varchar(100) DEFAULT NULL COMMENT '用户编码',
  `from_station` varchar(500) DEFAULT NULL COMMENT '出发站简码',
  `to_station` varchar(500) DEFAULT NULL COMMENT '到达站简码',
  `from_station_name` varchar(500) DEFAULT NULL COMMENT '出发站',
  `to_station_name` varchar(500) DEFAULT NULL COMMENT '到达站',
  `status` tinyint(4) DEFAULT NULL COMMENT '0.正常 1.删除',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1039 DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `tb_refund_order_flow`
-- ----------------------------
DROP TABLE IF EXISTS `tb_refund_order_flow`;
CREATE TABLE `tb_refund_order_flow` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `order_form_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '对应订单id',
  `grab_ticket_form_id` bigint(20) DEFAULT '0' COMMENT '抢票单id',
  `payment_sn` varchar(60) DEFAULT '' COMMENT '订单支付流水号',
  `orderDate` varchar(20) DEFAULT '' COMMENT '订单日期',
  `refund_amount` decimal(10,2) DEFAULT '0.00' COMMENT '退款金额',
  `original_amount` decimal(10,2) DEFAULT '0.00' COMMENT '原订单金额',
  `refund_reason` varchar(200) CHARACTER SET utf8mb4 DEFAULT '' COMMENT '理由',
  `msg` varchar(255) DEFAULT '' COMMENT '退款结果',
  `status` tinyint(2) DEFAULT '0' COMMENT '状态',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `un_order_from_id` (`id`,`order_form_id`) USING BTREE COMMENT '一个订单对应一个退款单'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='订单--退款单';

-- ----------------------------
--  Table structure for `tb_refund_ticket_flow`
-- ----------------------------
DROP TABLE IF EXISTS `tb_refund_ticket_flow`;
CREATE TABLE `tb_refund_ticket_flow` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `req_token` varchar(255) DEFAULT '' COMMENT '流水号唯一',
  `return_type` tinyint(4) DEFAULT '1' COMMENT '退票类型【0：表示线下退票退款； 1：表示线上退票退款；2：线下改签退款；',
  `order_form_id` bigint(20) unsigned DEFAULT '0' COMMENT '订单ID',
  `grab_ticket_form_id` bigint(20) DEFAULT '0' COMMENT '抢票单id',
  `order_form_detail_id` bigint(20) DEFAULT NULL,
  `ticket_no` varchar(255) DEFAULT '' COMMENT '火车票号',
  `apply_time` datetime DEFAULT NULL,
  `success_time` datetime DEFAULT NULL,
  `returnmoney` varchar(255) DEFAULT '' COMMENT '退款金额',
  `remark` varchar(255) DEFAULT '',
  `status` tinyint(2) unsigned DEFAULT '0',
  `createTime` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updateTime` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=33 DEFAULT CHARSET=utf8 COMMENT='退票流水';

-- ----------------------------
--  Table structure for `tb_return_money_order`
-- ----------------------------
DROP TABLE IF EXISTS `tb_return_money_order`;
CREATE TABLE `tb_return_money_order` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `order_form_detail_id` bigint(20) unsigned DEFAULT '0' COMMENT '订单ID',
  `order_form_sn` varchar(255) DEFAULT '' COMMENT '大订单支付流水号',
  `remark` varchar(255) DEFAULT '' COMMENT '退款说明(出票失败退款, 抢票失败退款, 线上退票退款, 线下退票退款)',
  `org_date` datetime DEFAULT NULL COMMENT '原商户订单日期(订单购买成功时间) *',
  `return_total_money` decimal(10,2) unsigned DEFAULT '0.00' COMMENT '实际退款总金额',
  `org_money` decimal(10,2) unsigned DEFAULT '0.00' COMMENT '原订单金额',
  `return_ticket_money` decimal(10,2) unsigned DEFAULT '0.00' COMMENT '乘客票款实退金额',
  `return_buy_ticket_package` decimal(10,0) unsigned DEFAULT '0' COMMENT '退还出票套餐金额',
  `return_grab_ticket_package` decimal(10,0) unsigned DEFAULT '0' COMMENT '退还抢票套餐金额',
  `status` tinyint(2) unsigned DEFAULT '0',
  `createTime` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updateTime` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=51 DEFAULT CHARSET=utf8 COMMENT='退款单';

-- ----------------------------
--  Table structure for `tb_send_message_record`
-- ----------------------------
DROP TABLE IF EXISTS `tb_send_message_record`;
CREATE TABLE `tb_send_message_record` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '消息记录id',
  `uid` varchar(255) DEFAULT '' COMMENT '被通知用户id',
  `user_type` tinyint(3) unsigned DEFAULT '0' COMMENT '用户类型',
  `mobile` varchar(100) DEFAULT '' COMMENT '被通知用户手机号',
  `message_template_id` bigint(20) DEFAULT NULL COMMENT '消息模板id',
  `content` varchar(255) DEFAULT '' COMMENT '短信内容',
  `sn` varchar(255) DEFAULT '' COMMENT ' 短信通道返回序列号',
  `send_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '发送时间',
  `status` tinyint(4) DEFAULT '1' COMMENT '状态',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=463 DEFAULT CHARSET=utf8 COMMENT='短信消息记录';

-- ----------------------------
--  Table structure for `tb_user_info`
-- ----------------------------
DROP TABLE IF EXISTS `tb_user_info`;
CREATE TABLE `tb_user_info` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `uid` varchar(50) DEFAULT NULL COMMENT '三方用户id',
  `app_id` varchar(50) DEFAULT NULL COMMENT 'appid',
  `account` varchar(50) DEFAULT NULL COMMENT '12306账户',
  `card_no` varchar(300) DEFAULT NULL,
  `card_id` varchar(300) DEFAULT NULL,
  `real_name` varchar(50) DEFAULT NULL,
  `phone` varchar(20) DEFAULT NULL COMMENT '手机号',
  `pwd` varchar(25) DEFAULT NULL COMMENT '12306密码',
  `status` int(11) DEFAULT NULL,
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `tb_verification_code`
-- ----------------------------
DROP TABLE IF EXISTS `tb_verification_code`;
CREATE TABLE `tb_verification_code` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `mobile` varchar(255) DEFAULT '' COMMENT '手机号',
  `code` varchar(255) DEFAULT '' COMMENT '验证码',
  `type` tinyint(4) DEFAULT '0' COMMENT '验证码类型',
  `retry_count` int(11) DEFAULT '0' COMMENT '重试次数',
  `status` tinyint(4) DEFAULT '0' COMMENT '状态（1：有效，0：无效）',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=254 DEFAULT CHARSET=utf8 COMMENT='验证码生成';

-- ----------------------------
--  Table structure for `tb_ys_channel_request_record`
-- ----------------------------
DROP TABLE IF EXISTS `tb_ys_channel_request_record`;
CREATE TABLE `tb_ys_channel_request_record` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '基金渠道方id',
  `sn` varchar(50) NOT NULL DEFAULT '' COMMENT '流水号',
  `business_type` varchar(30) DEFAULT '' COMMENT '业务类型',
  `ret_code` varchar(32) DEFAULT '' COMMENT '返回码',
  `request` text COMMENT '请求',
  `response` text COMMENT '返回',
  `remark` varchar(255) DEFAULT '' COMMENT '备注',
  `time` bigint(20) unsigned DEFAULT '0' COMMENT '请求耗时,单位毫秒',
  `status` tinyint(4) unsigned DEFAULT '0' COMMENT '状态',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`),
  KEY `idx_sn` (`sn`) USING BTREE,
  KEY `idx_business_type` (`business_type`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=3193 DEFAULT CHARSET=utf8 COMMENT='银盛渠道方请求记录';

SET FOREIGN_KEY_CHECKS = 1;
