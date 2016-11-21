/*
Navicat MySQL Data Transfer

Source Server         : 192.168.1.21
Source Server Version : 50709
Source Host           : 192.168.1.21:3306
Source Database       : db_qb

Target Server Type    : MYSQL
Target Server Version : 50709
File Encoding         : 65001

Date: 2016-11-21 16:23:33
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for qb_merchant_app_info
-- ----------------------------
DROP TABLE IF EXISTS `qb_merchant_app_info`;
CREATE TABLE `qb_merchant_app_info` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT 'app_id',
  `merchant_id` bigint(20) DEFAULT NULL COMMENT '商户编码',
  `open_id` varchar(200) DEFAULT NULL COMMENT 'OPENID',
  `secret_key` varchar(200) DEFAULT NULL COMMENT 'SECRETKEY',
  `app_name` varchar(200) DEFAULT NULL COMMENT '应用名称',
  `sn` int(11) DEFAULT NULL COMMENT '排序',
  `type` varchar(20) DEFAULT NULL COMMENT '应用类型\r\n            wap|web|app',
  `status` tinyint(2) DEFAULT NULL COMMENT '0.正常\r\n            1.删除',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of qb_merchant_app_info
-- ----------------------------
INSERT INTO `qb_merchant_app_info` VALUES ('1', '1', '1', '1', '万年历', '1', '1', '0', '2016-11-21 15:37:21', '2016-11-21 15:37:23');

-- ----------------------------
-- Table structure for qb_merchant_info
-- ----------------------------
DROP TABLE IF EXISTS `qb_merchant_info`;
CREATE TABLE `qb_merchant_info` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '编码',
  `company_name` varchar(200) DEFAULT NULL COMMENT '公司名称',
  `reg_address` varchar(200) DEFAULT NULL COMMENT '注册地址',
  `business_license_pic` varchar(200) DEFAULT NULL COMMENT '营业执照',
  `org_cert_pic` varchar(200) DEFAULT NULL COMMENT '组织机构代码',
  `in_charge_name` varchar(30) DEFAULT NULL COMMENT '负责人姓名',
  `in_charget_email` varchar(30) DEFAULT NULL COMMENT '负责人邮箱',
  `in_charget_qq` varchar(15) DEFAULT NULL COMMENT '负责人qq',
  `in_charget_phone` varchar(11) DEFAULT NULL COMMENT '负责人电话',
  `type` varchar(20) DEFAULT NULL COMMENT '类型',
  `status` tinyint(2) DEFAULT NULL COMMENT '0.正常1.删除 2.通过审核',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of qb_merchant_info
-- ----------------------------
INSERT INTO `qb_merchant_info` VALUES ('1', '万年历', '北京', '001', '001', '001', '001', '001', '001', '1', '0', '2016-11-21 15:36:56', '2016-11-21 15:36:59');
