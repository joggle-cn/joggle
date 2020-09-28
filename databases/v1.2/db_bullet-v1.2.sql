/*
 Navicat MySQL Data Transfer

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 50731
 Source Host           : localhost:3306
 Source Schema         : db_bullet

 Target Server Type    : MySQL
 Target Server Version : 50731
 File Encoding         : 65001

 Date: 28/09/2020 14:30:37
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for oauth_access_token
-- ----------------------------
DROP TABLE IF EXISTS `oauth_access_token`;
CREATE TABLE `oauth_access_token` (
  `token_id` varchar(256) DEFAULT NULL COMMENT 'MD5加密的access_token的值',
  `token` blob COMMENT 'OAuth2AccessToken.java对象序列化后的二进制数据',
  `authentication_id` varchar(256) DEFAULT NULL COMMENT 'MD5加密过的username,client_id,scope',
  `user_name` varchar(50) NOT NULL COMMENT '登录的用户名',
  `client_id` varchar(50) NOT NULL COMMENT '客户端ID',
  `authentication` blob COMMENT 'OAuth2Authentication.java对象序列化后的二进制数据',
  `refresh_token` varchar(256) DEFAULT NULL COMMENT 'MD5加密果的refresh_token的值',
  PRIMARY KEY (`user_name`,`client_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='访问令牌表';

-- ----------------------------
-- Table structure for oauth_approvals
-- ----------------------------
DROP TABLE IF EXISTS `oauth_approvals`;
CREATE TABLE `oauth_approvals` (
  `userid` varchar(256) DEFAULT NULL COMMENT '登录的用户名',
  `clientid` varchar(256) DEFAULT NULL COMMENT '客户端ID',
  `scope` varchar(256) DEFAULT NULL COMMENT '申请的权限',
  `status` varchar(10) DEFAULT NULL COMMENT '状态（Approve或Deny）',
  `expiresat` datetime DEFAULT NULL COMMENT '过期时间',
  `lastmodifiedat` datetime DEFAULT NULL COMMENT '最终修改时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='授权记录表';

-- ----------------------------
-- Table structure for oauth_client_details
-- ----------------------------
DROP TABLE IF EXISTS `oauth_client_details`;
CREATE TABLE `oauth_client_details` (
  `client_id` varchar(200) NOT NULL COMMENT '客户端ID',
  `resource_ids` varchar(256) DEFAULT NULL COMMENT '资源ID集合,多个资源时用逗号(,)分隔',
  `client_secret` varchar(256) DEFAULT NULL COMMENT '客户端密匙',
  `scope` varchar(256) DEFAULT NULL COMMENT '客户端申请的权限范围',
  `authorized_grant_types` varchar(256) DEFAULT NULL COMMENT '客户端支持的grant_type',
  `web_server_redirect_uri` varchar(256) DEFAULT NULL COMMENT '重定向URI',
  `authorities` varchar(256) DEFAULT NULL COMMENT '客户端所拥有的Spring Security的权限值，多个用逗号(,)分隔',
  `access_token_validity` int(11) DEFAULT NULL COMMENT '访问令牌有效时间值(单位:秒)',
  `refresh_token_validity` int(11) DEFAULT NULL COMMENT '更新令牌有效时间值(单位:秒)',
  `additional_information` varchar(4096) DEFAULT NULL COMMENT '预留字段',
  `autoapprove` varchar(256) DEFAULT NULL COMMENT '用户是否自动Approval操作',
  PRIMARY KEY (`client_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='客户端信息';

-- ----------------------------
-- Records of oauth_client_details
-- ----------------------------
BEGIN;
INSERT INTO `oauth_client_details` VALUES ('client_app', NULL, '$2a$10$kN16pqEEhV281YdV7nehtelaBPaeAtXscj3FWbi7KxdeAA3EzOUHW', 'read', 'client_credentials,authorization_code,password,refresh_token', 'http://baidu.com', NULL, 7200, 108000, NULL, NULL);
INSERT INTO `oauth_client_details` VALUES ('client_manager', NULL, '$2a$10$kN16pqEEhV281YdV7nehtelaBPaeAtXscj3FWbi7KxdeAA3EzOUHW', 'read', 'client_credentials,authorization_code,password,refresh_token', 'http://baidu.com', NULL, 7200, 108000, NULL, NULL);
COMMIT;

-- ----------------------------
-- Table structure for oauth_client_token
-- ----------------------------
DROP TABLE IF EXISTS `oauth_client_token`;
CREATE TABLE `oauth_client_token` (
  `token_id` varchar(256) DEFAULT NULL COMMENT 'MD5加密的access_token值',
  `token` blob COMMENT 'OAuth2AccessToken.java对象序列化后的二进制数据',
  `authentication_id` varchar(256) DEFAULT NULL COMMENT 'MD5加密过的username,client_id,scope',
  `user_name` varchar(256) DEFAULT NULL COMMENT '登录的用户名',
  `client_id` varchar(256) DEFAULT NULL COMMENT '客户端ID'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='客户端授权令牌表';

-- ----------------------------
-- Table structure for oauth_code
-- ----------------------------
DROP TABLE IF EXISTS `oauth_code`;
CREATE TABLE `oauth_code` (
  `code` varchar(256) DEFAULT NULL COMMENT '授权码(未加密)',
  `authentication` blob COMMENT 'AuthorizationRequestHolder.java对象序列化后的二进制数据'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='授权码表';

-- ----------------------------
-- Table structure for oauth_refresh_token
-- ----------------------------
DROP TABLE IF EXISTS `oauth_refresh_token`;
CREATE TABLE `oauth_refresh_token` (
  `token_id` varchar(256) DEFAULT NULL COMMENT 'MD5加密过的refresh_token的值',
  `token` blob COMMENT 'OAuth2RefreshToken.java对象序列化后的二进制数据',
  `authentication` blob COMMENT 'OAuth2Authentication.java对象序列化后的二进制数据'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='更新令牌表';

-- ----------------------------
-- Table structure for t_device
-- ----------------------------
DROP TABLE IF EXISTS `t_device`;
CREATE TABLE `t_device` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) DEFAULT NULL,
  `deviceId` varchar(100) DEFAULT NULL,
  `createTime` datetime DEFAULT NULL,
  `userId` bigint(20) DEFAULT NULL,
  `intranet_ip` varchar(30) DEFAULT NULL COMMENT '内网IP',
  `mac_addr` varchar(20) DEFAULT NULL COMMENT 'mac地址',
  `server_tunnel_id` bigint(20) NOT NULL DEFAULT '1' COMMENT '设备通道',
  `device_secret` varchar(100) DEFAULT NULL COMMENT '设备秘钥',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=30 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_device
-- ----------------------------
BEGIN;
INSERT INTO `t_device` VALUES (29, 'default', '5ru8vv84ary8', '2020-09-27 21:23:22', 1, '192.168.1.110', '78-4f-43-82-97-04', 1, '2kJfd5s2$REQLhhK35I.yXjwegve84.');
COMMIT;

-- ----------------------------
-- Table structure for t_device_mapping
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
  `hostname` varchar(255) DEFAULT NULL COMMENT 'hostname',
  `bind_tls` int(11) DEFAULT NULL COMMENT '绑定https',
  `status` int(11) DEFAULT NULL COMMENT '映射状态 （1、启用；0、停用)',
  `auth` varchar(100) DEFAULT NULL COMMENT '基础认证',
  `server_tunnel_id` bigint(20) DEFAULT NULL COMMENT '服务通道ID',
  `domain_id` bigint(20) DEFAULT NULL COMMENT '域名ID',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=160 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for t_device_online
-- ----------------------------
DROP TABLE IF EXISTS `t_device_online`;
CREATE TABLE `t_device_online` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `deviceNo` varchar(255) NOT NULL COMMENT '设备编号',
  `updateTime` datetime DEFAULT NULL,
  `status` int(11) DEFAULT NULL,
  `intranet_ip` varchar(100) DEFAULT NULL COMMENT '内网IP',
  `mac_addr` varchar(20) DEFAULT NULL COMMENT 'Mac地址',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=37 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for t_domain
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
  PRIMARY KEY (`id`,`type`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=45 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_domain
-- ----------------------------
BEGIN;
INSERT INTO `t_domain` VALUES (1, 'www', 2, '2019-12-26 13:46:29', 0.00, 0.00, 1, 1, '2019-12-26 13:46:40', '2100-01-31 21:49:09');
COMMIT;

-- ----------------------------
-- Table structure for t_server_tunnel
-- ----------------------------
DROP TABLE IF EXISTS `t_server_tunnel`;
CREATE TABLE `t_server_tunnel` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(40) DEFAULT NULL COMMENT '通道名称',
  `server_addr` varchar(50) DEFAULT NULL COMMENT '线路通道地址',
  `create_time` datetime DEFAULT NULL COMMENT '上线时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_server_tunnel
-- ----------------------------
BEGIN;
INSERT INTO `t_server_tunnel` VALUES (1, '成都', 'joggle.cn:8083', '2019-12-25 22:45:22');
COMMIT;

-- ----------------------------
-- Table structure for t_sys_group
-- ----------------------------
DROP TABLE IF EXISTS `t_sys_group`;
CREATE TABLE `t_sys_group` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT 'id',
  `parent_id` bigint(20) DEFAULT NULL COMMENT '用户组父id',
  `name` varchar(200) DEFAULT NULL COMMENT '用户组名称',
  `description` varchar(500) DEFAULT NULL COMMENT '描述',
  `created_by` varchar(50) DEFAULT NULL COMMENT '创建人',
  `updated_by` varchar(50) DEFAULT NULL COMMENT '更新人',
  `updated_user_id` bigint(20) DEFAULT NULL COMMENT '更新人ID',
  `created_user_id` bigint(20) DEFAULT NULL COMMENT '创建人ID',
  `created_time` datetime DEFAULT NULL COMMENT '创建时间',
  `updated_time` datetime DEFAULT NULL COMMENT '更新时间',
  `deleted` int(1) DEFAULT '0' COMMENT '逻辑删除(1删除，0未删除)',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `id` (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8 COMMENT='用户组表（部门）';

-- ----------------------------
-- Records of t_sys_group
-- ----------------------------
BEGIN;
INSERT INTO `t_sys_group` VALUES (1, -1, '总公司', '总公司', 'N', '2019-06-25 15:46:48', 2019, 0, '2019-08-24 19:45:55', NULL, 0);
COMMIT;

-- ----------------------------
-- Table structure for t_sys_menu
-- ----------------------------
DROP TABLE IF EXISTS `t_sys_menu`;
CREATE TABLE `t_sys_menu` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT 'id',
  `parent_id` bigint(1) NOT NULL DEFAULT '0' COMMENT '父菜单id',
  `type` varchar(100) DEFAULT NULL COMMENT '菜单类型（1运营，2商户）',
  `href` varchar(200) DEFAULT NULL COMMENT '菜单路径',
  `icon` varchar(200) DEFAULT NULL COMMENT '菜单图标',
  `name` varchar(200) DEFAULT NULL COMMENT '菜单名称',
  `description` varchar(500) DEFAULT NULL COMMENT '描述',
  `sort` int(11) DEFAULT NULL COMMENT '排序',
  `deleted` int(1) DEFAULT '0' COMMENT '逻辑删除(1删除，0未删除)',
  `created_by` varchar(50) DEFAULT NULL COMMENT '创建人',
  `updated_by` varchar(50) DEFAULT NULL COMMENT '更新人',
  `updated_user_id` bigint(20) DEFAULT NULL COMMENT '更新人ID',
  `created_user_id` bigint(20) DEFAULT NULL COMMENT '创建人ID',
  `created_time` datetime DEFAULT NULL COMMENT '创建时间',
  `updated_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `id` (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1011 DEFAULT CHARSET=utf8 COMMENT='菜单表';

-- ----------------------------
-- Records of t_sys_menu
-- ----------------------------
BEGIN;
INSERT INTO `t_sys_menu` VALUES (1, 0, '2', '/opera/util/qualifications/delete', NULL, '人员管理', NULL, 1, 0, '', '', NULL, NULL, NULL, NULL);
COMMIT;

-- ----------------------------
-- Table structure for t_sys_resources
-- ----------------------------
DROP TABLE IF EXISTS `t_sys_resources`;
CREATE TABLE `t_sys_resources` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '资源id',
  `code` varchar(100) DEFAULT NULL COMMENT '资源code',
  `type` varchar(100) DEFAULT NULL COMMENT '资源类型',
  `name` varchar(200) DEFAULT NULL COMMENT '资源名称',
  `url` varchar(200) DEFAULT NULL COMMENT '资源url',
  `method` varchar(20) DEFAULT NULL COMMENT '资源方法',
  `description` varchar(500) DEFAULT NULL COMMENT '简介',
  `deleted` int(1) DEFAULT NULL COMMENT '逻辑删除(1删除，0未删除)',
  `created_by` varchar(50) DEFAULT NULL COMMENT '创建人',
  `updated_by` varchar(50) DEFAULT NULL COMMENT '更新人',
  `updated_user_id` bigint(20) DEFAULT NULL COMMENT '更新人ID',
  `created_user_id` bigint(20) DEFAULT NULL COMMENT '创建人ID',
  `created_time` datetime DEFAULT NULL COMMENT '创建时间',
  `updated_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `ux_resources_code` (`code`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8 COMMENT='接口资源表';

-- ----------------------------
-- Records of t_sys_resources
-- ----------------------------
BEGIN;
INSERT INTO `t_sys_resources` VALUES (1, 'GET:/api/user/login/info', 'url', '接口', '/api/user/login/info', 'GET', '获取用户基本信息', 0, 'system', 'system', 0, 1, '2019-06-23 15:00:21', '2020-09-12 20:44:56');
INSERT INTO `t_sys_resources` VALUES (2, 'GET:/api/user/domain/', 'url', '接口', '/api/user/domain/', 'GET', '获取我的域名', 0, 'system', 'system', 0, 0, '2019-06-24 15:00:20', '2020-09-12 20:44:56');
INSERT INTO `t_sys_resources` VALUES (3, 'GET:/api/user/device/', 'url', '接口', '/api/user/device/', 'GET', '获取我的设备', 0, 'system', 'system', 0, 0, '2019-06-25 15:00:20', '2020-09-12 20:44:56');
INSERT INTO `t_sys_resources` VALUES (4, 'GET:/api/user/device/info', 'url', '接口', '/api/user/device/info', 'GET', '获取设备详情', 0, 'system', 'system', 0, 0, '2019-06-26 15:00:20', '2020-09-12 20:44:56');
INSERT INTO `t_sys_resources` VALUES (5, 'POST:/api/user/device/', 'url', '接口', '/api/user/device/', 'POST', '修改我的设备', 0, 'system', 'system', 0, 0, '2019-06-25 15:00:20', '2020-09-12 20:44:56');
INSERT INTO `t_sys_resources` VALUES (6, 'DELETE:/api/user/device/', 'url', '接口', '/api/user/device/', 'DELETE', '删除设备', 0, 'system', 'system', 0, 0, '2019-06-25 15:00:20', '2020-09-12 20:44:56');
INSERT INTO `t_sys_resources` VALUES (7, 'GET:/api/user/device/validate', 'url', '接口', '/api/user/device/validate', 'GET', '校验设备编码', 0, 'system', 'system', 0, 0, '2019-06-25 15:00:20', '2020-09-12 20:44:56');
INSERT INTO `t_sys_resources` VALUES (8, 'POST:/api/user/device/mapping/', 'url', '接口', '/api/user/device/mapping/', 'POST', '编辑映射规则', 0, 'system', 'system', 0, 0, '2019-06-25 15:00:20', '2020-09-12 20:44:56');
INSERT INTO `t_sys_resources` VALUES (9, 'POST:/api/user/loginout', 'url', '接口', '/api/user/loginout', 'POST', '退出登录', 0, 'system', 'system', 0, 0, '2019-06-25 15:00:20', '2020-09-12 20:44:56');
INSERT INTO `t_sys_resources` VALUES (10, 'GET:/api/user/domain/info', 'url', '接口', '/api/user/domain/info', 'GET', '获取用户信息', 0, 'system', 'system', 0, 0, '2019-06-25 15:00:20', '2020-09-12 20:44:56');
INSERT INTO `t_sys_resources` VALUES (11, 'POST:/api/user/domain/calculate', 'url', '接口', '/api/user/domain/calculate', 'POST', '计算域名价格', 0, 'system', 'system', 0, 0, '2019-06-25 15:00:20', '2020-09-12 20:44:56');
INSERT INTO `t_sys_resources` VALUES (12, 'GET:/api/user/domain/nobind', 'url', '接口', '/api/user/domain/nobind', 'GET', '获取未绑定域名', 0, 'system', 'system', 0, 0, '2019-06-25 15:00:20', '2020-09-12 20:44:56');
INSERT INTO `t_sys_resources` VALUES (13, 'POST:/api/user/domain/bind', 'url', '接口', '/api/user/domain/bind', 'POST', '获取未绑定域名', 0, 'system', 'system', 0, 0, '2019-06-25 15:00:20', '2020-09-12 20:44:56');
INSERT INTO `t_sys_resources` VALUES (14, 'DELETE:/api/user/device/mapping/', 'url', '接口', '/api/user/device/mapping/', 'DELETE', '删除设备映射', 0, 'system', 'system', 0, 0, '2019-06-25 15:00:20', '2020-09-12 20:44:56');
INSERT INTO `t_sys_resources` VALUES (15, 'POST:/api/user/domain/pay', 'url', '接口', '/api/user/domain/pay', 'POST', '支付接口', 0, 'system', 'system', 0, 0, '2019-06-25 15:00:20', '2020-09-12 20:44:56');
COMMIT;

-- ----------------------------
-- Table structure for t_sys_role_menu_relation
-- ----------------------------
DROP TABLE IF EXISTS `t_sys_role_menu_relation`;
CREATE TABLE `t_sys_role_menu_relation` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT 'id',
  `menu_id` int(11) NOT NULL COMMENT '菜单id',
  `role_id` int(11) NOT NULL COMMENT '角色id',
  `created_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `created_by` varchar(100) COLLATE utf8_bin NOT NULL COMMENT '创建人',
  `updated_by` varchar(100) COLLATE utf8_bin NOT NULL COMMENT '更新人',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `id` (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=110 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='角色和菜单关系表';

-- ----------------------------
-- Records of t_sys_role_menu_relation
-- ----------------------------
BEGIN;
INSERT INTO `t_sys_role_menu_relation` VALUES (101, 101, 101, '2019-06-25 15:43:28', '2019-06-25 15:43:28', 'system', 'system');
INSERT INTO `t_sys_role_menu_relation` VALUES (102, 102, 101, '2019-06-25 15:43:28', '2019-06-25 15:43:28', 'system', 'system');
INSERT INTO `t_sys_role_menu_relation` VALUES (103, 103, 101, '2019-06-25 15:43:28', '2019-06-25 15:43:28', 'system', 'system');
INSERT INTO `t_sys_role_menu_relation` VALUES (104, 101, 102, '2019-06-25 15:43:28', '2019-06-25 15:43:28', 'system', 'system');
INSERT INTO `t_sys_role_menu_relation` VALUES (105, 102, 102, '2019-06-25 15:43:28', '2019-06-25 15:43:28', 'system', 'system');
INSERT INTO `t_sys_role_menu_relation` VALUES (106, 101, 103, '2019-06-25 15:43:28', '2019-06-25 15:43:28', 'system', 'system');
INSERT INTO `t_sys_role_menu_relation` VALUES (107, 102, 103, '2019-06-25 15:43:28', '2019-06-25 15:43:28', 'system', 'system');
INSERT INTO `t_sys_role_menu_relation` VALUES (108, 103, 103, '2019-06-25 15:43:28', '2019-06-25 15:43:28', 'system', 'system');
INSERT INTO `t_sys_role_menu_relation` VALUES (109, 105, 103, '2020-09-28 14:24:33', '2020-09-28 14:24:33', 'system', 'system');
COMMIT;

-- ----------------------------
-- Table structure for t_sys_roles
-- ----------------------------
DROP TABLE IF EXISTS `t_sys_roles`;
CREATE TABLE `t_sys_roles` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '角色id',
  `type` int(11) DEFAULT NULL COMMENT '角色类型（0隐式：api接口，1显式用户可操作）',
  `client_type` int(11) DEFAULT NULL COMMENT '客户端类型（0用户端、1商户端、2运营端）',
  `code` varchar(100) NOT NULL COMMENT '角色code',
  `name` varchar(200) DEFAULT NULL COMMENT '角色名称',
  `description` varchar(500) DEFAULT NULL COMMENT '简介',
  `deleted` int(1) DEFAULT '0' COMMENT '逻辑删除(1删除，0未删除)',
  `created_by` varchar(50) DEFAULT NULL COMMENT '创建人',
  `updated_by` varchar(50) DEFAULT NULL COMMENT '更新人',
  `updated_user_id` bigint(20) DEFAULT NULL COMMENT '更新人ID',
  `created_user_id` bigint(20) DEFAULT NULL COMMENT '创建人ID',
  `created_time` datetime DEFAULT NULL COMMENT '创建时间',
  `updated_time` datetime DEFAULT NULL COMMENT '更新时间',
  `supplier_id` bigint(20) DEFAULT NULL COMMENT '商户id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8 COMMENT='角色表';

-- ----------------------------
-- Records of t_sys_roles
-- ----------------------------
BEGIN;
INSERT INTO `t_sys_roles` VALUES (1, 1, 2, 'SuperAdmin', '超级管理员', '角色描述1', 0, 'zhoutaoo', 'zhoutaoo', 2, 2, '2019-08-28 07:50:31', '2019-08-28 07:50:31', 1);
INSERT INTO `t_sys_roles` VALUES (2, 0, 1, 'Consumer', '普通用户', '可以使用平台', 0, 'system', NULL, NULL, NULL, '2019-08-28 07:50:31', NULL, NULL);
INSERT INTO `t_sys_roles` VALUES (3, 0, 1, 'IT', 'IT角色', '', 0, 'system', 'zhoutaoo', NULL, NULL, '2019-08-28 07:50:31', NULL, NULL);
COMMIT;

-- ----------------------------
-- Table structure for t_sys_roles_resources_relation
-- ----------------------------
DROP TABLE IF EXISTS `t_sys_roles_resources_relation`;
CREATE TABLE `t_sys_roles_resources_relation` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `resource_id` bigint(20) NOT NULL COMMENT '角色id',
  `role_id` bigint(20) NOT NULL COMMENT '资源id',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `FK_Reference_16` (`resource_id`) USING BTREE,
  KEY `FK_Reference_17` (`role_id`) USING BTREE,
  CONSTRAINT `t_sys_roles_resources_relation_ibfk_1` FOREIGN KEY (`resource_id`) REFERENCES `t_sys_resources` (`id`),
  CONSTRAINT `t_sys_roles_resources_relation_ibfk_2` FOREIGN KEY (`role_id`) REFERENCES `t_sys_roles` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=135 DEFAULT CHARSET=utf8 COMMENT='角色和资源关系表';

-- ----------------------------
-- Records of t_sys_roles_resources_relation
-- ----------------------------
BEGIN;
INSERT INTO `t_sys_roles_resources_relation` VALUES (1, 1, 1);
INSERT INTO `t_sys_roles_resources_relation` VALUES (2, 2, 1);
INSERT INTO `t_sys_roles_resources_relation` VALUES (3, 3, 1);
INSERT INTO `t_sys_roles_resources_relation` VALUES (6, 5, 1);
INSERT INTO `t_sys_roles_resources_relation` VALUES (12, 4, 1);
INSERT INTO `t_sys_roles_resources_relation` VALUES (110, 6, 1);
INSERT INTO `t_sys_roles_resources_relation` VALUES (111, 7, 1);
INSERT INTO `t_sys_roles_resources_relation` VALUES (112, 8, 1);
INSERT INTO `t_sys_roles_resources_relation` VALUES (113, 9, 1);
INSERT INTO `t_sys_roles_resources_relation` VALUES (114, 10, 1);
INSERT INTO `t_sys_roles_resources_relation` VALUES (115, 11, 1);
INSERT INTO `t_sys_roles_resources_relation` VALUES (116, 12, 1);
INSERT INTO `t_sys_roles_resources_relation` VALUES (117, 13, 1);
INSERT INTO `t_sys_roles_resources_relation` VALUES (118, 14, 1);
INSERT INTO `t_sys_roles_resources_relation` VALUES (119, 15, 1);
INSERT INTO `t_sys_roles_resources_relation` VALUES (120, 6, 2);
INSERT INTO `t_sys_roles_resources_relation` VALUES (121, 14, 2);
INSERT INTO `t_sys_roles_resources_relation` VALUES (122, 3, 2);
INSERT INTO `t_sys_roles_resources_relation` VALUES (123, 4, 2);
INSERT INTO `t_sys_roles_resources_relation` VALUES (124, 7, 2);
INSERT INTO `t_sys_roles_resources_relation` VALUES (125, 2, 2);
INSERT INTO `t_sys_roles_resources_relation` VALUES (126, 10, 2);
INSERT INTO `t_sys_roles_resources_relation` VALUES (127, 12, 2);
INSERT INTO `t_sys_roles_resources_relation` VALUES (128, 1, 2);
INSERT INTO `t_sys_roles_resources_relation` VALUES (129, 5, 2);
INSERT INTO `t_sys_roles_resources_relation` VALUES (130, 8, 2);
INSERT INTO `t_sys_roles_resources_relation` VALUES (131, 13, 2);
INSERT INTO `t_sys_roles_resources_relation` VALUES (132, 11, 2);
INSERT INTO `t_sys_roles_resources_relation` VALUES (133, 15, 2);
INSERT INTO `t_sys_roles_resources_relation` VALUES (134, 9, 2);
COMMIT;

-- ----------------------------
-- Table structure for t_sys_users
-- ----------------------------
DROP TABLE IF EXISTS `t_sys_users`;
CREATE TABLE `t_sys_users` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(100) DEFAULT NULL COMMENT '用户名',
  `email` varchar(100) DEFAULT NULL,
  `nickname` varchar(40) DEFAULT NULL,
  `password` varchar(100) DEFAULT NULL COMMENT '密码字段',
  `agree` varchar(255) DEFAULT NULL,
  `icon` varchar(255) DEFAULT NULL COMMENT '头像',
  `loginTime` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '登录时间',
  `balance` decimal(10,2) DEFAULT '0.00' COMMENT '余额',
  `enabled` int(1) DEFAULT NULL COMMENT '是否启用 （1启用、0停用）',
  `account_non_expired` int(1) DEFAULT '1',
  `credentials_non_expired` int(1) DEFAULT '1',
  `account_non_locked` int(1) DEFAULT '1',
  `created_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  `created_by` varchar(100) DEFAULT NULL COMMENT '创建人',
  `updated_by` varchar(100) DEFAULT NULL COMMENT '更新人',
  `activate_code` varchar(50) DEFAULT NULL COMMENT '激活码',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=25 DEFAULT CHARSET=utf8 COMMENT='用户表';

-- ----------------------------
-- Records of t_sys_users
-- ----------------------------
BEGIN;
INSERT INTO `t_sys_users` VALUES (1, 'admin@joggle.cn', 'admin@joggle.cn', '管理员', '$2a$10$KUld8xUP0z883e.agsPOte3EQjg6MAHasro4i/u1Ig8efBtHZzBiO', 'true', 'https://wx.qlogo.cn/mmopen/vi_32/eTLd4AbjibPPVLG7Ns1j8Neu772myG2YaE6IGRHNmZPrWLd0kdupO1ea4BoqeXcU2Ruren8DdpmAOwXax6AcneA/132', '2020-09-28 14:07:55', 858300.00, 1, 1, 1, 1, '2019-12-30 15:19:04', '2019-12-30 15:19:04', NULL, NULL, NULL);
COMMIT;

-- ----------------------------
-- Table structure for t_sys_users_roles_relation
-- ----------------------------
DROP TABLE IF EXISTS `t_sys_users_roles_relation`;
CREATE TABLE `t_sys_users_roles_relation` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) NOT NULL COMMENT '用户id',
  `role_id` bigint(20) NOT NULL COMMENT '角色id',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `FK_Reference_12` (`user_id`) USING BTREE,
  KEY `FK_Reference_13` (`role_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8 COMMENT='用户和角色关系表';

-- ----------------------------
-- Records of t_sys_users_roles_relation
-- ----------------------------
BEGIN;
INSERT INTO `t_sys_users_roles_relation` VALUES (1, 1, 1);
COMMIT;

-- ----------------------------
-- Table structure for t_user_forget
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
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8;

SET FOREIGN_KEY_CHECKS = 1;
