
ALTER TABLE `user_flow`
    ADD COLUMN `flow_total` bigint(22) NULL DEFAULT 0 COMMENT '累计充值流量 kb' AFTER `flow`;


update user_flow  set flow_total = flow ;