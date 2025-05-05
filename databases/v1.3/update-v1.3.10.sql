
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