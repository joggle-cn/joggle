
ALTER TABLE `t_device_mapping`
    ADD COLUMN `port_protocol` varchar(20) NULL COMMENT '协议 （rdp|http|tcp）' AFTER `port`;

ALTER TABLE `t_device_mapping`
    ADD COLUMN `name` varchar(50) NULL COMMENT '服务名称' AFTER `id`;
ALTER TABLE `t_device_mapping`
    MODIFY COLUMN `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '服务名称' AFTER `id`;


update t_device_mapping set port_protocol = 'http' where protocol = 1 and port_protocol is null;
update t_device_mapping set port_protocol = 'tcp' where protocol = 2 and port_protocol is null;
update t_device_mapping set port_protocol = 'http' where protocol = 3 and port_protocol is null;
update t_device_mapping set port_protocol = 'https' where protocol = 4 and port_protocol is null;
update t_device_mapping set port_protocol = 'udp' where protocol = 5 and port_protocol is null;

update t_device_mapping set name = description where name is null;



ALTER TABLE `user_domain`
    ADD COLUMN `domain_key` text NULL COMMENT '域名私钥' AFTER `cert_key`;

ALTER TABLE `user_domain`
    ADD COLUMN `is_auto_renewal` tinyint(1) NULL  DEFAULT 0 COMMENT '是否自动续期 1自动 0关闭' AFTER `user_id`;

ALTER TABLE `user_domain`
    ADD COLUMN `apply_error` varchar(255) NULL COMMENT '颁发失败原因' AFTER `apply_time`;