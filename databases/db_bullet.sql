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

 Date: 07/07/2018 15:08:49 PM
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
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8;

-- ----------------------------
--  Records of `t_device`
-- ----------------------------
BEGIN;
INSERT INTO `t_device` VALUES ('1', '家里q', '000001', '2017-12-08 15:20:30', '1'), ('2', '公司机房', '000002', '2017-12-09 18:23:47', '1'), ('6', '家里的设备', '0000000113', '2017-12-10 11:46:50', '1'), ('7', 'default', '0001', '2017-12-10 14:09:12', '1'), ('8', 'default', '00000001131', '2018-01-14 21:35:21', '1');
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
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8;

-- ----------------------------
--  Records of `t_device_mapping`
-- ----------------------------
BEGIN;
INSERT INTO `t_device_mapping` VALUES ('1', '1', 'www', '8082', '2017-12-08 16:10:18', '1', '111', '1', null), ('4', '2', 'test1', '111', null, '1', '11', null, null), ('5', '2', 'test2', '11111', null, '1', '111123213213', null, null), ('10', '2', 'dsada', '23232', null, '1', '32323', '1', null), ('11', '2', 'dsada1', '21213', null, '1', '2121', '1', null), ('16', '6', 'test', '8080', null, '1', 'GitLab', '1', '192.168.1.4'), ('17', '6', 'one', '8089', null, '1', 'JENKINS', '1', '192.168.1.5');
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
INSERT INTO `t_device_online` VALUES ('1', '000004', '2017-12-09 19:43:09', '1'), ('2', '12345678', '2017-12-20 08:57:03', '-1'), ('4', '0000000113', '2018-07-07 15:06:48', '1'), ('5', '0001', '2017-12-20 14:09:02', '1'), ('6', '00000001131', '2018-01-14 22:41:51', '-1');
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
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- ----------------------------
--  Records of `t_user`
-- ----------------------------
BEGIN;
INSERT INTO `t_user` VALUES ('1', 'admin@qq.com', 'zhangsan', '123', 'true'), ('2', '903595558@qq.com', null, '123', 'true');
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
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;

-- ----------------------------
--  Records of `t_user_forget`
-- ----------------------------
BEGIN;
INSERT INTO `t_user_forget` VALUES ('1', '2', '123', '2018-01-11 23:06:43', 'c6b0da83-00eb-487b-b10a-cbdde86acf6a', '1', '127.0.0.1', '903595558@qq.com'), ('3', '2', '123', '2018-01-11 23:31:16', '25b5b772-29ef-40dd-a584-568c2544101b', '1', '127.0.0.1', '903595558@qq.com'), ('4', '2', '1234', '2018-01-11 23:34:59', 'e613a677-275b-4437-8e89-e275a7216e02', '1', '127.0.0.1', '903595558@qq.com'), ('5', '2', '1234', '2018-01-11 23:38:07', '61e0788b-f87d-4a38-8c1d-b0916d13a11c', '1', '127.0.0.1', '903595558@qq.com'), ('6', '2', '123', '2018-01-12 11:59:43', 'a84eb2b2-6c29-4c36-80aa-7d8856cd0995', '0', '0:0:0:0:0:0:0:1', '903595558@qq.com');
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
