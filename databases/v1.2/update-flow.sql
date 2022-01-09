
ALTER TABLE `client_version` ROW_FORMAT = Dynamic;

ALTER TABLE `data_metrics` ROW_FORMAT = Dynamic;

ALTER TABLE `oauth_access_token` ROW_FORMAT = Dynamic;

ALTER TABLE `oauth_approvals` ROW_FORMAT = Dynamic;

ALTER TABLE `oauth_client_details` ROW_FORMAT = Dynamic;

ALTER TABLE `oauth_client_token` ROW_FORMAT = Dynamic;

ALTER TABLE `oauth_code` ROW_FORMAT = Dynamic;

ALTER TABLE `oauth_refresh_token` ROW_FORMAT = Dynamic;

ALTER TABLE `t_device` AUTO_INCREMENT = 42, ROW_FORMAT = Dynamic;

ALTER TABLE `t_device_mapping` AUTO_INCREMENT = 173, ROW_FORMAT = Dynamic;

ALTER TABLE `t_device_online` AUTO_INCREMENT = 58, ROW_FORMAT = Dynamic;

ALTER TABLE `t_device_online` MODIFY COLUMN `public_ip` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '公网IP' AFTER `mac_addr`;

ALTER TABLE `t_domain` AUTO_INCREMENT = 52, ROW_FORMAT = Dynamic;

ALTER TABLE `t_server_tunnel` ROW_FORMAT = Dynamic;

ALTER TABLE `t_server_tunnel` ADD COLUMN `broadband` int(255) NULL DEFAULT NULL COMMENT '宽带 MB' AFTER `name`;

ALTER TABLE `t_sys_group` ROW_FORMAT = Dynamic;

ALTER TABLE `t_sys_menu` AUTO_INCREMENT = 7, ROW_FORMAT = Dynamic;

ALTER TABLE `t_sys_menu` MODIFY COLUMN `type` int(1) NULL DEFAULT NULL COMMENT '菜单类型（1运营）' AFTER `level`;

ALTER TABLE `t_sys_menu` MODIFY COLUMN `sort` int(11) NULL DEFAULT 100 COMMENT '排序' AFTER `description`;

ALTER TABLE `t_sys_menu` MODIFY COLUMN `created_time` datetime(0) NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '创建时间' AFTER `created_user_id`;

ALTER TABLE `t_sys_menu` MODIFY COLUMN `updated_time` datetime(0) NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间' AFTER `created_time`;

ALTER TABLE `t_sys_resources` ROW_FORMAT = Dynamic;

ALTER TABLE `t_sys_role_menu_relation` ROW_FORMAT = Dynamic;

ALTER TABLE `t_sys_roles` ROW_FORMAT = Dynamic;

ALTER TABLE `t_sys_roles_resources_relation` ROW_FORMAT = Dynamic;

ALTER TABLE `t_sys_users` AUTO_INCREMENT = 32, ROW_FORMAT = Dynamic;

ALTER TABLE `t_sys_users` MODIFY COLUMN `id` bigint(22) NOT NULL COMMENT 'id' FIRST;

ALTER TABLE `t_sys_users` MODIFY COLUMN `email` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '邮箱' AFTER `username`;

ALTER TABLE `t_sys_users` MODIFY COLUMN `nickname` varchar(40) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '昵称' AFTER `email`;

ALTER TABLE `t_sys_users` MODIFY COLUMN `agree` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '同意协议 1同意 0不同意' AFTER `password`;

ALTER TABLE `t_sys_users` MODIFY COLUMN `account_non_expired` int(1) NULL DEFAULT 1 COMMENT '账户过期' AFTER `enabled`;

ALTER TABLE `t_sys_users` MODIFY COLUMN `credentials_non_expired` int(1) NULL DEFAULT 1 COMMENT '密码过期' AFTER `account_non_expired`;

ALTER TABLE `t_sys_users` MODIFY COLUMN `account_non_locked` int(1) NULL DEFAULT 1 COMMENT '账户锁定' AFTER `credentials_non_expired`;

ALTER TABLE `t_sys_users` MODIFY COLUMN `id` bigint(22) NOT NULL AUTO_INCREMENT COMMENT 'id';

ALTER TABLE `t_sys_users_roles_relation` AUTO_INCREMENT = 29, ROW_FORMAT = Dynamic;

ALTER TABLE `t_user_forget` AUTO_INCREMENT = 9, ROW_FORMAT = Dynamic;

ALTER TABLE `t_user_forget` MODIFY COLUMN `oldPass` varchar(40) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL AFTER `userId`;

CREATE TABLE `user_flow`  (
  `user_id` bigint(255) NOT NULL,
  `flow` bigint(22) NULL DEFAULT NULL COMMENT '流量 kb',
  `updated_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`user_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户流量' ROW_FORMAT = Dynamic;



-- 每个用户赠送1G的流量
insert into user_flow (select id, 1048576, now() from t_sys_users);
