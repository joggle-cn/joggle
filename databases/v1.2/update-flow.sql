

CREATE TABLE `user_flow`  (
  `user_id` bigint(255) NOT NULL,
  `flow` bigint(22) NULL DEFAULT NULL COMMENT '流量 kb',
  `updated_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`user_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户流量' ROW_FORMAT = Dynamic;



-- 每个用户赠送1G的流量
insert into user_flow (select id, 1048576, now() from t_sys_users);
