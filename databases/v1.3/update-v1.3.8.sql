

ALTER TABLE `t_sys_users`
    ADD COLUMN `phone` varchar(12) NULL COMMENT '手机号' AFTER `nickname`;


ALTER TABLE `t_sys_users`
    MODIFY COLUMN `system_notice` tinyint(1) NOT NULL DEFAULT 0 COMMENT '系统通知 1打开 0关闭' AFTER `user_certification`,
    ADD COLUMN `sms_notice` tinyint(1) NOT NULL DEFAULT 0 COMMENT '短信通知 1打开 0关闭' AFTER `system_notice`;



-- ----------------------------
-- Table structure for sys_config
-- ----------------------------
CREATE TABLE `sys_config`  (
   `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
   `type` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '类型',
   `key` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '配置key',
   `value` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '配置值',
   `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
   `update_time` datetime(0) NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
   PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '系统配置表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_config
-- ----------------------------
INSERT INTO `sys_config` VALUES (1, 'SmsTypeEnum', 'AUTH', 'SMS_468430684', '2024-06-23 20:29:55', '2024-06-23 20:57:28');
INSERT INTO `sys_config` VALUES (2, 'SmsTypeEnum', 'DEVICE_DOWN', 'SMS_468420666', '2024-06-23 20:30:47', '2024-06-23 20:57:26');

