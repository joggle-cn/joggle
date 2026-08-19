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

-- API Key 用户个人管理表
CREATE TABLE IF NOT EXISTS `t_user_api_key` (
                                                `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
                                                `user_id` bigint(20) NOT NULL COMMENT '用户ID',
                                                `api_key` varchar(64) NOT NULL COMMENT 'API Key (jgl_前缀)',
                                                `name` varchar(64) DEFAULT NULL COMMENT '名称标识',
                                                `enabled` tinyint(1) DEFAULT 1 COMMENT '启用状态 1启用 0禁用',
                                                `last_used_at` datetime DEFAULT NULL COMMENT '最后使用时间',
                                                `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                                `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                                PRIMARY KEY (`id`) USING BTREE,
                                                UNIQUE KEY `uk_api_key` (`api_key`) USING BTREE,
                                                KEY `idx_user_id` (`user_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户API Key表';


ALTER TABLE  `device_peers`
    ADD COLUMN `bandwidth` int(10) NULL DEFAULT 1 COMMENT '宽带限制 Mbps' AFTER `network_speed`,
    ADD COLUMN `strategy` varchar(30) NULL DEFAULT "p2p" COMMENT '策略 p2p、wss、auto' AFTER `bandwidth`;


ALTER TABLE `resource_package`
    ADD COLUMN `relay_mode` int(1) NULL DEFAULT 0 COMMENT '中继模式' AFTER `broadband_rate`;