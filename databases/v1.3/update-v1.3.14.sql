-- v1.3.14 user_package 新增 flow_total 字段

ALTER TABLE `user_package`
    ADD COLUMN `flow_total` bigint(20) NULL DEFAULT 0 COMMENT 'kb 总流量' AFTER `flow_use`;

UPDATE user_package SET flow_total = flow ;
