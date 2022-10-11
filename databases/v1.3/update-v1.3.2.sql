
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
