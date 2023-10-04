CREATE TABLE `user_domain`  (
    `id` bigint(20) NOT NULL AUTO_INCREMENT,
    `user_id` bigint(20) NULL DEFAULT NULL COMMENT '用户id',
    `domain` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '域名',
    `cert_key` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '证书私钥',
    `cert_pem` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '证书pem',
    `apply_time` datetime(0) NULL DEFAULT NULL COMMENT '证书颁发时间',
    `due_time` datetime(0) NULL DEFAULT NULL COMMENT '证书到期时间',
    `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
    `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '用户域名' ROW_FORMAT = Compact;


ALTER TABLE `user_domain`
    ADD COLUMN `is_cert` tinyint(1) NULL DEFAULT 0 COMMENT '是否配置证书 1已配置 0未配置' AFTER `domain`;

ALTER TABLE `t_server_tunnel`
    MODIFY COLUMN `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id' FIRST,
    ADD COLUMN `user_id` bigint NULL COMMENT '用户id' AFTER `id`;

ALTER TABLE `device_peers`
    ADD COLUMN `config_compress` tinyint(1) NULL DEFAULT 1 COMMENT '传输压缩 1是 0否' AFTER `update_time`;