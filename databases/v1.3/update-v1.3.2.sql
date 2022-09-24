
ALTER TABLE `orders`
    MODIFY COLUMN `pay_type` int(1) NULL DEFAULT NULL COMMENT '支付方式 1余额 2支付宝 3微信' AFTER `pay_amount`;

-- 2022-09-24 marker 新增设备并发链接数量
ALTER TABLE `t_device`
    ADD COLUMN `update_time` datetime NULL COMMENT '更新时间' AFTER `device_secret`,
    ADD COLUMN `concurrent_num` int NULL DEFAULT 5 COMMENT '设备的并发连接数' AFTER `update_time`;