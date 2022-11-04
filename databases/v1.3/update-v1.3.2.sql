
ALTER TABLE `orders`
    MODIFY COLUMN `pay_type` int(1) NULL DEFAULT NULL COMMENT '支付方式 1余额 2支付宝 3微信' AFTER `pay_amount`;

-- 2022-09-24 marker 新增设备并发链接数量
ALTER TABLE `t_device`
    ADD COLUMN `update_time` datetime NULL COMMENT '更新时间' AFTER `device_secret`,
    ADD COLUMN `concurrent_num` int NULL DEFAULT 5 COMMENT '设备的并发连接数' AFTER `update_time`;


ALTER TABLE `t_server_tunnel`
    ADD COLUMN `token` varchar(100) NULL COMMENT '服务器Token' AFTER `server_end_time`;

ALTER TABLE `t_server_tunnel`
    ADD COLUMN `enable_flow` tinyint(1) NULL COMMENT '启用扣量 1 是 0否' AFTER `token`;
update t_server_tunnel set enable_flow = 1;

ALTER TABLE `data_metrics`
    ADD COLUMN `server_tunnel_id` int NULL COMMENT '通道id' AFTER `user_id`;
update data_metrics set server_tunnel_id = 1;

ALTER TABLE `data_metrics_day`
    ADD COLUMN `server_tunnel_id` int NULL COMMENT '通道id' AFTER `user_id`;
update data_metrics_day set server_tunnel_id = 1;


ALTER TABLE `orders`
    ADD COLUMN `refund_amount` bigint(22) NULL COMMENT '退款数量' AFTER `pay_type`,
    ADD COLUMN `refund_money` decimal(12,2) NULL COMMENT '退款金额' AFTER `refund_amount`;

update orders set refund_amount =0, refund_money=0 ;


-- 2022-10-11 marker 设备ip白名单
CREATE TABLE `device_white_ips`  (
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
    `device_id` bigint(22) NULL DEFAULT NULL COMMENT '设备id',
    `ips` text  NULL COMMENT '分号间隔的ip地址',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1   COMMENT = '设备ip白名单' ;

-- 线上已执

ALTER TABLE `feedback`
    ADD COLUMN `reply` varchar(500) NULL COMMENT '回复内容' AFTER `create_time`;
ALTER TABLE `feedback`
    ADD COLUMN `reply_time` datetime NULL COMMENT '回复时间' AFTER `reply`;

-- 记录明细进入流量
ALTER TABLE `data_metrics_day`
    ADD COLUMN `bytes_in` bigint(22) NULL COMMENT '流入' AFTER `flow`,
    ADD COLUMN `bytes_out` bigint(22) NULL COMMENT '流出' AFTER `bytes_in`;

ALTER TABLE `t_server_tunnel`
    ADD COLUMN `server_up_time` datetime NULL COMMENT '服务器上线时间' AFTER `server_end_time`;



CREATE TABLE `resource_package`  (
     `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
     `name` varchar(50) NULL DEFAULT NULL COMMENT '资源包名称',
     `level` int(1) NULL DEFAULT NULL COMMENT '等级',
     `price` decimal(12, 2) NULL DEFAULT NULL COMMENT '价格',
     `domain_num` int(11) NULL DEFAULT NULL COMMENT '域名数量',
     `port_num` int(11) NULL DEFAULT NULL COMMENT '端口数量',
     `flow_num` bigint(20) NULL DEFAULT NULL COMMENT 'kb 流量',
     `device_num` int(11) NULL DEFAULT NULL COMMENT '设备数量',
     `p2p_num` int(11) NULL DEFAULT NULL COMMENT 'p2p隧道数量',
     `wol_enable` int(1) NULL DEFAULT NULL COMMENT '网络唤醒开关',
     `proxy_enable` int(1) NULL DEFAULT NULL COMMENT '代理开关',
     `status` tinyint(1) NULL DEFAULT NULL COMMENT '状态 1正常 0禁用',
     `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
     `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
     `days` int(11) NULL DEFAULT NULL COMMENT '购买后持有天数',
     `content` longtext NULL COMMENT '富文本说明',
     `sence` varchar(255) NULL COMMENT '使用场景',
     PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1  ROW_FORMAT = Dynamic;

ALTER TABLE `resource_package`
    ADD COLUMN `concurrent_num` int NULL COMMENT '并发限制' AFTER `proxy_enable`;
ALTER TABLE  `resource_package` COMMENT = '套餐';

ALTER TABLE `orders`
    MODIFY COLUMN `resource_type` int(2) NULL DEFAULT NULL COMMENT '资源类型 1域名 2端口 3流量 4 充值 5套餐' AFTER `user_id`;


ALTER TABLE  `resource_package`
    ADD COLUMN `broadband_rate` int(11) NULL COMMENT '宽带速率' AFTER `concurrent_num`;




CREATE TABLE `user_package`  (
     `user_id` bigint(20) NOT NULL COMMENT 'id',
     `resource_package_id` int(11) NULL DEFAULT NULL COMMENT '资源包id',
     `name` varchar(50)  NULL DEFAULT NULL COMMENT '资源包名称',
     `level` int(1) NULL DEFAULT NULL COMMENT '等级',
     `wol_enable` int(1) NULL DEFAULT 0 COMMENT '网络唤醒开关',
     `proxy_enable` int(1) NULL DEFAULT 0 COMMENT '代理开关',
     `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
     `domain_use` int(11) NULL DEFAULT 0 COMMENT '域名数量',
     `port_use` int(11) NULL DEFAULT 0 COMMENT '端口数量',
     `flow_use` bigint(20) NULL DEFAULT 0 COMMENT 'kb 流量',
     `device_use` int(11) NULL DEFAULT 0 COMMENT '设备数量',
     `peer_use` int(11) NULL DEFAULT 0 COMMENT 'p2p隧道数量',
     `broadband_rate` int(11) NULL DEFAULT 0 COMMENT '宽带速率',
     `concurrent_num` int(11) NULL DEFAULT 5 COMMENT '并发数',
     `start_time` datetime(0) NULL DEFAULT NULL COMMENT '开始时间',
     `end_time` datetime(0) NULL DEFAULT NULL COMMENT '结束时间',
     PRIMARY KEY (`user_id`) USING BTREE
) ENGINE = InnoDB  COMMENT = '用户套餐' ROW_FORMAT = Dynamic;


INSERT INTO `resource_package`(`id`, `name`, `level`, `price`, `domain_num`, `port_num`, `flow_num`, `device_num`, `p2p_num`, `wol_enable`, `proxy_enable`, `concurrent_num`, `broadband_rate`, `status`, `create_time`, `update_time`, `days`, `content`, `sence`)
VALUES (1, '普通用户', 0, 0.00, 0, 0, 1048576, 2, 1, 1, 0, 5, 1, 1, '2022-10-30 13:29:11', '2022-10-30 22:37:13', -1, NULL, '打算');



insert into user_package (`user_id`, `resource_package_id`, `name`, `level`, create_time,device_use, peer_use) (
    select
        su.id ,
        1,
        rp.`name`,
        rp.`level`,
        sysdate(),
        (select count(1) from t_device d where d.userId = su.id),
        (select count(1) from device_peers dp where dp.user_id = su.id)
    from t_sys_users su
             left join resource_package rp on rp.id = 1
);


CREATE TABLE `user_package_rights`  (
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
    `user_id` bigint(20) NULL DEFAULT NULL COMMENT '用户id',
    `resource_type` int(1) NULL DEFAULT NULL COMMENT '资源类型 1域名 2端口 ',
    `resource_id` bigint(20) NULL DEFAULT NULL COMMENT '资源id',
    `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
    `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
    `status` int(1) NULL DEFAULT NULL COMMENT '状态 1正常 0禁用',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1  COMMENT = '用户套餐权益' ROW_FORMAT = Dynamic;

ALTER TABLE `user_package`
    ADD COLUMN `flow` bigint(22) NULL COMMENT 'kb 可用流量' AFTER `flow_use`;

update user_package up ,user_flow uf
set up.flow =0
where up.user_id = uf.user_id;




ALTER TABLE `t_domain`
    ADD COLUMN `bandwidth` int(10) NULL COMMENT '宽带限制 Mbps' AFTER `original_price`;
    
update t_domain set bandwidth = 3;
update user_package set broadband_rate = 1 where broadband_rate =0;
ALTER TABLE `t_server_tunnel`
    ADD COLUMN `server_down_time` datetime NULL COMMENT '通道服务器下线时间' AFTER `server_up_time`;

update t_server_tunnel set server_down_time = server_up_time;

-- 线上已执