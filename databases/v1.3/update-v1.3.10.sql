
ALTER TABLE `t_device_mapping`
    ADD COLUMN `port_protocol` varchar(20) NULL COMMENT '协议 （rdp|http|tcp）' AFTER `port`;

ALTER TABLE `t_device_mapping`
    ADD COLUMN `name` varchar(50) NULL COMMENT '服务名称' AFTER `id`;
ALTER TABLE `t_device_mapping`
    MODIFY COLUMN `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '服务名称' AFTER `id`;