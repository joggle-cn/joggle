ALTER TABLE `t_sys_menu`
ADD COLUMN `level` int(1) NULL COMMENT '层级' AFTER `parent_id`;

ALTER TABLE `t_sys_menu`
MODIFY COLUMN `type` int(1) NULL DEFAULT NULL COMMENT '菜单类型（1运营）' AFTER `level`,
MODIFY COLUMN `sort` int(11) NULL DEFAULT 100 COMMENT '排序' AFTER `description`,
MODIFY COLUMN `created_time` datetime(0) NULL ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '创建时间' AFTER `created_user_id`,
MODIFY COLUMN `updated_time` datetime(0) NULL ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间' AFTER `created_time`;