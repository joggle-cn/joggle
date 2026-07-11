-- v1.3.14 user_package 新增 flow_total 字段

ALTER TABLE `user_package`
    ADD COLUMN `flow_total` bigint(20) NULL DEFAULT 0 COMMENT 'kb 总流量' AFTER `flow_use`;

UPDATE user_package SET flow_total = flow ;


ALTER TABLE  `device_peers`
    ADD COLUMN `network_latency` int(11) NULL COMMENT '网络延迟ms' AFTER `config_interval`,
    ADD COLUMN `network_speed` double(11, 2) NULL COMMENT '网络速度kb/s' AFTER `network_latency`;

ALTER TABLE `client_version`
    MODIFY COLUMN `checksum` varchar(1024) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '检查信息' AFTER `download_url`;

ALTER TABLE `client_version`
    ADD COLUMN `signature` varchar(1024) DEFAULT NULL COMMENT 'Tauri 签名' AFTER `checksum`;
ALTER TABLE `client_version`
    MODIFY COLUMN `type` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '类型  CLIENT SERVER' AFTER `id`;