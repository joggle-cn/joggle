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

 Date: 04/11/2019 10:41:00 AM
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
--  Table structure for `t_device`
-- ----------------------------
DROP TABLE IF EXISTS `t_device`;
CREATE TABLE `t_device` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) DEFAULT NULL,
  `deviceId` varchar(100) DEFAULT NULL,
  `createTime` datetime DEFAULT NULL,
  `userId` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;

-- ----------------------------
--  Records of `t_device`
-- ----------------------------
BEGIN;
INSERT INTO `t_device` VALUES ('6', '家里的设备', '0000000113', '2017-12-10 11:46:50', '1');
COMMIT;

-- ----------------------------
--  Table structure for `t_device_mapping`
-- ----------------------------
DROP TABLE IF EXISTS `t_device_mapping`;
CREATE TABLE `t_device_mapping` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `device_id` bigint(20) DEFAULT NULL,
  `domain` varchar(255) DEFAULT NULL,
  `port` int(11) DEFAULT NULL,
  `createTime` datetime DEFAULT NULL,
  `protocol` int(11) DEFAULT NULL,
  `description` varchar(100) DEFAULT NULL,
  `userId` bigint(20) DEFAULT NULL,
  `host` varchar(250) DEFAULT NULL COMMENT '服务器地址',
  `remote_port` int(11) DEFAULT NULL COMMENT '远端端口',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8;

-- ----------------------------
--  Records of `t_device_mapping`
-- ----------------------------
BEGIN;
INSERT INTO `t_device_mapping` VALUES ('16', '6', 'test', '8086', null, '1', 'GitLab', '1', '192.168.88.50', '0'), ('20', '6', 'test2', '22', null, '2', 'ssh', '1', 'localhost', '23');
COMMIT;

-- ----------------------------
--  Table structure for `t_device_online`
-- ----------------------------
DROP TABLE IF EXISTS `t_device_online`;
CREATE TABLE `t_device_online` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `deviceId` varchar(255) DEFAULT NULL,
  `updateTime` datetime DEFAULT NULL,
  `status` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;

-- ----------------------------
--  Records of `t_device_online`
-- ----------------------------
BEGIN;
INSERT INTO `t_device_online` VALUES ('1', '000004', '2017-12-09 19:43:09', '1'), ('2', '12345678', '2017-12-20 08:57:03', '-1'), ('4', '0000000113', '2019-04-11 10:29:06', '1'), ('5', '0001', '2017-12-20 14:09:02', '1'), ('6', '00000001131', '2018-01-14 22:41:51', '-1');
COMMIT;

-- ----------------------------
--  Table structure for `t_user`
-- ----------------------------
DROP TABLE IF EXISTS `t_user`;
CREATE TABLE `t_user` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `email` varchar(255) DEFAULT NULL,
  `nickname` varchar(40) DEFAULT NULL,
  `pass` varchar(40) DEFAULT NULL,
  `agree` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;

-- ----------------------------
--  Records of `t_user`
-- ----------------------------
BEGIN;
INSERT INTO `t_user` VALUES ('1', 'admin@qq.com', 'zhangsan', '123', 'true'), ('2', '903595558@qq.com', null, '123', 'true'), ('3', 'admin@wuweibi.com', null, '123', 'true'), ('4', '582608243@qq.com', null, '123', 'true');
COMMIT;

-- ----------------------------
--  Table structure for `t_user_forget`
-- ----------------------------
DROP TABLE IF EXISTS `t_user_forget`;
CREATE TABLE `t_user_forget` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `userId` bigint(20) DEFAULT NULL,
  `oldPass` varchar(40) DEFAULT NULL,
  `createTime` datetime DEFAULT NULL,
  `code` varchar(100) DEFAULT NULL,
  `status` int(11) DEFAULT NULL,
  `ip` varchar(100) DEFAULT NULL,
  `email` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8;

-- ----------------------------
--  Records of `t_user_forget`
-- ----------------------------
BEGIN;
INSERT INTO `t_user_forget` VALUES ('1', '2', '123', '2018-01-11 23:06:43', 'c6b0da83-00eb-487b-b10a-cbdde86acf6a', '1', '127.0.0.1', '903595558@qq.com'), ('3', '2', '123', '2018-01-11 23:31:16', '25b5b772-29ef-40dd-a584-568c2544101b', '1', '127.0.0.1', '903595558@qq.com'), ('4', '2', '1234', '2018-01-11 23:34:59', 'e613a677-275b-4437-8e89-e275a7216e02', '1', '127.0.0.1', '903595558@qq.com'), ('5', '2', '1234', '2018-01-11 23:38:07', '61e0788b-f87d-4a38-8c1d-b0916d13a11c', '1', '127.0.0.1', '903595558@qq.com'), ('6', '2', '123', '2018-01-12 11:59:43', 'a84eb2b2-6c29-4c36-80aa-7d8856cd0995', '1', '0:0:0:0:0:0:0:1', '903595558@qq.com'), ('7', '4', '123', '2018-08-05 10:52:13', '68b6a2e6-1a7f-4193-ac53-1a4425db580e', '0', '0:0:0:0:0:0:0:1', '582608243@qq.com'), ('8', '2', '123', '2018-08-05 10:52:49', 'e0e0e54f-5d5a-4d84-a96f-83320b3b114e', '0', '0:0:0:0:0:0:0:1', '903595558@qq.com');
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
