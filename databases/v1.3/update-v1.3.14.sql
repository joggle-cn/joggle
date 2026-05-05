-- v1.3.14 user_package 新增 flow_total 字段

ALTER TABLE `user_package`
    ADD COLUMN `flow_total` bigint(20) NULL DEFAULT 0 COMMENT 'kb 总流量' AFTER `flow_use`;

UPDATE user_package SET flow_total = flow ;


ALTER TABLE  `device_peers`
    ADD COLUMN `network_latency` int(11) NULL COMMENT '网络延迟ms' AFTER `config_interval`,
    ADD COLUMN `network_speed` double(11, 2) NULL COMMENT '网络速度kb/s' AFTER `network_latency`;