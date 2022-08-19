
CREATE TABLE `orders`  (
   `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '订单id',
   `order_no` varchar(22) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '订单号',
   `user_id` bigint(20) NULL DEFAULT NULL COMMENT '用户id',
   `resource_type` int(2) NULL DEFAULT NULL COMMENT '资源类型 1域名 2端口 3流量 4 充值',
   `domain_id` bigint(22) NULL DEFAULT NULL COMMENT '资源id',
   `amount` bigint(22) NULL DEFAULT NULL COMMENT '购买量 单位：秒、MB',
   `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '资源名称',
   `price_amount` decimal(12, 2) NULL DEFAULT NULL COMMENT '原价',
   `discount_amount` decimal(12, 2) NULL DEFAULT NULL COMMENT '优惠金额',
   `pay_amount` decimal(12, 2) NULL DEFAULT NULL COMMENT '支付价格',
   `pay_type` int(1) NULL DEFAULT NULL COMMENT '支付方式 1余额 2支付宝',
   `status` tinyint(2) NULL DEFAULT NULL COMMENT '订单状态 0待支付 1已支付 2 取消 3退款中 4已退款',
   `trade_no` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '三方交易号',
   `pay_time` datetime(0) NULL DEFAULT NULL COMMENT '支付时间',
   `refund_time` datetime(0) NULL DEFAULT NULL COMMENT '退款时间',
   `cancel_time` datetime(0) NULL DEFAULT NULL COMMENT '取消时间',
   `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
   `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
   PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;


ALTER TABLE `t_domain`
    MODIFY COLUMN `sales_price` decimal(10, 2) NULL DEFAULT NULL COMMENT '销售价格（元/天）' AFTER `create_time`,
    MODIFY COLUMN `original_price` decimal(10, 2) NULL DEFAULT NULL COMMENT '原价（元/天）' AFTER `sales_price`;



ALTER TABLE `t_sys_users`
    ADD COLUMN `user_admin` tinyint(1) NULL DEFAULT 0 COMMENT '是否后台用户 1是 0否' AFTER `activate_code`;

update t_sys_users set user_admin = 0 ;

-- 2022-07-31 忘记密码增加索引
ALTER TABLE `t_user_forget`
    ADD INDEX `idx_code`(`code`),
ADD INDEX `idx_email`(`email`);

-- 2022-08-03 设备任意门文件服务器
CREATE TABLE `t_device_door`  (
  `id` bigint(22) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `device_id` bigint(20) NULL DEFAULT NULL COMMENT '设备id',
  `local_path` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '本地服务路径',
  `server_path` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '服务端路径',
  `enable` int(1) NULL DEFAULT 0 COMMENT '启用状态 1启用 0停用',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '设备任意门' ROW_FORMAT = Dynamic;



CREATE TABLE `device_peers`  (
     `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
     `user_id` bigint(20) NULL DEFAULT NULL COMMENT '用户id',
     `server_device_id` bigint(11) NULL DEFAULT NULL COMMENT '服务侧设备id',
     `client_device_id` bigint(11) NULL DEFAULT NULL COMMENT '客户侧设备id',
     `server_local_port` int(11) NULL DEFAULT NULL COMMENT '服务侧本地端口',
     `client_proxy_port` int(11) NULL DEFAULT NULL COMMENT '客户侧代理端口',
     `status` tinyint(1) NULL DEFAULT NULL COMMENT '状态 1启用 0禁用',
     `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
     `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
     PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;


ALTER TABLE  `device_peers`
    ADD COLUMN `app_name` varchar(200) NULL COMMENT 'p2pAppName' AFTER `user_id`;

-- 2022-08-12 marker 新增特定IP
ALTER TABLE `device_peers`
    ADD COLUMN `server_local_host` varchar(20) NULL COMMENT '服务侧本地host' AFTER `server_local_port`,
    ADD COLUMN `client_proxy_host` varchar(20) NULL COMMENT '客户侧代理host' AFTER `client_proxy_port`,
    ADD COLUMN `remark` varchar(220) NULL COMMENT '备注' AFTER `client_proxy_host`;


ALTER TABLE `device_peers` COMMENT = 'P2P映射';