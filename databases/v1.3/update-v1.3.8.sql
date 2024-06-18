

ALTER TABLE `t_sys_users`
    ADD COLUMN `phone` varchar(12) NULL COMMENT '手机号' AFTER `nickname`;


ALTER TABLE `t_sys_users`
    MODIFY COLUMN `system_notice` tinyint(1) NOT NULL DEFAULT 0 COMMENT '系统通知 1打开 0关闭' AFTER `user_certification`,
    ADD COLUMN `sms_notice` tinyint(1) NOT NULL DEFAULT 0 COMMENT '短信通知 1打开 0关闭' AFTER `system_notice`;