ALTER TABLE `t_sys_menu`
ADD COLUMN `level` int(1) NULL COMMENT '层级' AFTER `parent_id`;

ALTER TABLE `t_sys_menu`
MODIFY COLUMN `type` int(1) NULL DEFAULT NULL COMMENT '菜单类型（1运营）' AFTER `level`,
MODIFY COLUMN `sort` int(11) NULL DEFAULT 100 COMMENT '排序' AFTER `description`,
MODIFY COLUMN `created_time` datetime(0) NULL ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '创建时间' AFTER `created_user_id`,
MODIFY COLUMN `updated_time` datetime(0) NULL ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间' AFTER `created_time`;



INSERT INTO `t_sys_resources`(`id`, `code`, `type`, `name`, `url`, `method`, `description`, `deleted`, `created_by`, `updated_by`, `updated_user_id`, `created_user_id`, `created_time`, `updated_time`) VALUES (19, 'POST:/api/user/device/wol', 'url', '接口', '/api/user/device/wol', 'POST', '网络唤醒', 0, 'system', 'system', 0, 0, '2019-06-25 15:00:20', '2020-09-12 20:44:56');

INSERT INTO `t_sys_roles_resources_relation`(`id`, `resource_id`, `role_id`) VALUES (138, 19, 2);
INSERT INTO `t_sys_roles_resources_relation`(`id`, `resource_id`, `role_id`) VALUES (139, 19, 1);

ALTER TABLE `t_user_forget`
    MODIFY COLUMN `oldPass` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL AFTER `userId`;


ALTER TABLE `t_device_online`
    ADD COLUMN `public_ip` varchar(20) NULL AFTER `mac_addr`;


ALTER TABLE `t_device_online`
    MODIFY COLUMN `intranet_ip` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '内网IP' AFTER `status`;

-- 客户端版本管理
CREATE TABLE `client_version` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `version` varchar(255) DEFAULT NULL COMMENT '版本号',
  `title` varchar(255) DEFAULT NULL COMMENT '标题',
  `description` varchar(255) DEFAULT NULL COMMENT '描述',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `download_url` varchar(255) DEFAULT NULL COMMENT '下载URL地址',
  `checksum` varchar(255) DEFAULT NULL COMMENT '检查信息',
  `status` tinyint(1) DEFAULT NULL COMMENT '状态 1上架 0下架',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;



-- 新增数据收集
CREATE TABLE `data_metrics`  (
    `id` bigint(20) NOT NULL COMMENT 'id',
    `user_id` bigint(20) NULL DEFAULT NULL COMMENT '用户ID',
    `device_id` bigint(20) NULL DEFAULT NULL COMMENT '设备ID',
    `mapping_id` bigint(20) NULL DEFAULT NULL COMMENT '设备映射ID',
    `bytes_in` bigint(22) NULL DEFAULT NULL COMMENT '进入流量',
    `bytes_out` bigint(22) NULL DEFAULT NULL COMMENT '出口流量',
    `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
    `create_date` date NULL DEFAULT NULL COMMENT '创建天 yyyy-mm-dd',
    `create_month` date NULL DEFAULT NULL COMMENT '创建月 yyyy-mm',
    `create_year` smallint(4) NULL DEFAULT NULL COMMENT '创建年 yyyy',
    PRIMARY KEY (`id`) USING BTREE,
    INDEX `idx_user_id`(`user_id`) USING BTREE,
    INDEX `idx_create_date`(`create_date`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '数据收集' ROW_FORMAT = Dynamic;


ALTER TABLE `t_device_online` ADD COLUMN `client_version` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '客户端版本' AFTER `public_ip`;




-- 2022-04-07 版本增加标识
ALTER TABLE  `client_version`
    ADD COLUMN `os` varchar(20) NULL COMMENT '操作系统' AFTER `title`,
ADD COLUMN `arch` varchar(20) NULL COMMENT 'CPU架构' AFTER `os`;