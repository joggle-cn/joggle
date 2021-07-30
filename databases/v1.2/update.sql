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
