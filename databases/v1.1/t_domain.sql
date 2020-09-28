/*
 Navicat Premium Data Transfer

 Source Server         : localhost3307
 Source Server Type    : MySQL
 Source Server Version : 50626
 Source Host           : localhost
 Source Database       : db_bullet

 Target Server Type    : MySQL
 Target Server Version : 50626
 File Encoding         : utf-8

 Date: 12/26/2019 15:52:10 PM
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
--  Table structure for `t_domain`
-- ----------------------------
DROP TABLE IF EXISTS `t_domain`;
CREATE TABLE `t_domain` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `domain` varchar(100) DEFAULT NULL COMMENT '二级域名前缀或端口',
  `type` int(11) NOT NULL DEFAULT '1' COMMENT '类型： 1 端口 2域名',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `sales_price` decimal(10,2) DEFAULT NULL COMMENT '销售价格（元/月）',
  `original_price` decimal(10,2) DEFAULT NULL COMMENT '原价（元/月）',
  `status` int(1) DEFAULT NULL COMMENT '状态：1已售、0释放、-1 禁售',
  `user_id` bigint(20) DEFAULT NULL COMMENT '所属用户ID',
  `buy_time` datetime DEFAULT NULL COMMENT '购买时间',
  `due_time` datetime DEFAULT NULL COMMENT '到期时间',
  PRIMARY KEY (`id`,`type`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;

-- ----------------------------
--  Records of `t_domain`
-- ----------------------------
BEGIN;
INSERT INTO `t_domain` VALUES ('1', 'www', '2', '2019-12-26 13:46:29', '0.00', '0.00', '1', '1', '2019-12-26 13:46:40', null), ('2', 'pay', '2', '2019-12-26 13:48:50', '0.00', '0.00', '0', '1', null, null), ('3', 'test', '2', '2019-12-26 13:49:20', '10.00', '10.00', '0', '1', null, null), ('4', '8080', '1', '2019-12-26 14:56:44', '10.00', '10.00', '1', '1', '2019-12-26 14:56:30', '2019-12-31 15:02:33');
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
