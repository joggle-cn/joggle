
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
