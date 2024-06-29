ALTER TABLE `device_peers`
    ADD COLUMN `server_mtu` int(4) NULL COMMENT '服务侧MTU' AFTER `server_local_host`,
    ADD COLUMN `client_mtu` int(4) NULL COMMENT '客户侧MTU' AFTER `client_proxy_host`;


ALTER TABLE `device_online_log`
    ADD COLUMN `user_id` bigint(22) NULL COMMENT '用户id' AFTER `id`;

ALTER TABLE `t_sys_users`
    ADD COLUMN `system_notice` tinyint(1) NULL DEFAULT 0  COMMENT '系统通知 1打开 0关闭' AFTER `user_certification`;

update t_sys_users set system_notice = 0;