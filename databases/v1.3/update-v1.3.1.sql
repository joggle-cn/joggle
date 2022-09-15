
-- 记录服务器到期时间，购买的资源不能超过到期时间
ALTER TABLE `t_server_tunnel`
    ADD COLUMN `server_end_time` datetime NULL COMMENT '服务器到期时间' AFTER `status`;

-- 服务器到期时间写入
update t_server_tunnel set server_end_time = '2022-11-17 23:59:59' where id = 1;


-- 实名 认证
CREATE TABLE `user_certification`  (
   `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
   `user_id` bigint(20) NULL DEFAULT NULL COMMENT '用户id',
   `type` int(2) NULL DEFAULT NULL COMMENT '类型1身份证',
   `real_name` varchar(20)  NULL DEFAULT NULL COMMENT '真实姓名',
   `idcard` varchar(20)  NULL DEFAULT NULL COMMENT '身份证号码',
   `phone` varchar(20)  NULL DEFAULT NULL COMMENT '手机号码',
   `birthday` date NULL DEFAULT NULL COMMENT '出生日期',
   `sex` varchar(2)  NULL DEFAULT NULL COMMENT '性别',
   `province` varchar(20)  NULL DEFAULT NULL COMMENT '省份',
   `city` varchar(50)  NULL DEFAULT NULL COMMENT '城市',
   `district` varchar(50)  NULL DEFAULT NULL COMMENT '区县',
   `area` varchar(255)  NULL DEFAULT NULL COMMENT '地区',
   `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
   `result` int(1) NULL DEFAULT NULL COMMENT '认证 结果 1通过 0等待审核  2未通过',
   `result_msg` varchar(255)  NULL DEFAULT NULL COMMENT '不通过原因',
   PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 COMMENT = '用户实名认证' ROW_FORMAT = Dynamic;

-- 新增 是否实名认证 1是 0否
ALTER TABLE `t_sys_users`
    ADD COLUMN `user_certification` tinyint(1) NULL DEFAULT 0 COMMENT '是否实名认证 1是 0否' AFTER `user_admin`;

update t_sys_users set user_certification = 0;

ALTER TABLE `user_certification`
    ADD COLUMN `examine_time` datetime NULL COMMENT '审核时间' AFTER `result_msg`;