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

 Date: 12/25/2019 23:05:00 PM
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
--  Table structure for `t_server_tunnel`
-- ----------------------------
DROP TABLE IF EXISTS `t_server_tunnel`;
CREATE TABLE `t_server_tunnel` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(40) DEFAULT NULL COMMENT '通道名称',
  `server_addr` varchar(50) DEFAULT NULL COMMENT '线路通道地址',
  `create_time` datetime DEFAULT NULL COMMENT '上线时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
--  Records of `t_server_tunnel`
-- ----------------------------
BEGIN;
INSERT INTO `t_server_tunnel` VALUES ('1', '成都', 'joggle.cn:8083', '2019-12-25 22:45:22');
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
