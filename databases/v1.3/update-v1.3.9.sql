
ALTER TABLE `client_version`
    ADD COLUMN `type` varchar(10) NULL COMMENT '类型  CLIENT SERVER' AFTER `id`;


update client_version set type = 'CLIENT'  where title like 'JoggleClient%';



ALTER TABLE `t_server_tunnel`
    ADD COLUMN `version` varchar(20) NULL COMMENT '通道版本' AFTER `server_addr`;


ALTER TABLE `user_package`
    ADD INDEX `idx_packageId`(`resource_package_id`);

ALTER TABLE `orders`
    ADD COLUMN `user_ip` varchar(64) NULL COMMENT '用户下单IP' AFTER `update_time`;

ALTER TABLE `device_online_log`
    ADD COLUMN `device_name` varchar(100) NULL COMMENT '设备名称' AFTER `device_id`;

update device_online_log dol, t_device d
set dol.device_name = d.name
where dol.device_id = d.id;

-- 新增平台级短信通知，支持多手机号
INSERT INTO `sys_config` (`id`, `type`, `key`, `value`, `create_time`, `update_time`) VALUES (4, 'SystemConfigEnum', 'NOTICE_PHONES', '', '2024-09-24 21:41:46', '2024-09-24 21:41:48');
INSERT INTO `sys_config` (`id`, `type`, `key`, `value`, `create_time`, `update_time`) VALUES (5, 'SystemConfigEnum', 'NOTICE_ENABLE', 'false', '2024-09-24 21:41:46', '2024-09-24 21:41:48');


ALTER TABLE `orders`
    ADD COLUMN `trade_type` varchar(50) NULL COMMENT '三方交易类型 JSAPI：公众号支付NATIVE：扫码支付APP：APP支付MICROPAY：付款码支付MWEB：H5支付FACEPAY：刷脸支付' AFTER `trade_no`;


INSERT INTO `sys_config` (`id`, `type`, `key`, `value`, `create_time`, `update_time`) VALUES (6, 'SmsTypeEnum', 'VIP_EXPIRATION_NOTICE', 'SMS_475645154', '2024-11-23 17:35:35', '2024-11-23 17:37:41');

ALTER TABLE `t_device_mapping`
    ADD COLUMN `is_del` tinyint(1) NULL DEFAULT 0 COMMENT '是否删除 1删除 ' AFTER `user_domain_id`;
ALTER TABLE  `t_device_mapping`
    ADD COLUMN `update_time` datetime NULL COMMENT '更新时间' AFTER `is_del`;

ALTER TABLE `t_device_mapping`
    ADD INDEX `idx_userId_domainId`(`userId`, `domain_id`);


