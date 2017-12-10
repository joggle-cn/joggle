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

 Date: 12/10/2017 13:52:36 PM
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
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8;

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
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;

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
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

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
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

SET FOREIGN_KEY_CHECKS = 1;
