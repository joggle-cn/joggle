
ALTER TABLE `orders`
    MODIFY COLUMN `pay_type` int(1) NULL DEFAULT NULL COMMENT '支付方式 1余额 2支付宝 3微信' AFTER `pay_amount`;




