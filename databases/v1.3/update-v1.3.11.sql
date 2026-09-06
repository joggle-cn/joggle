
ALTER TABLE  `device_peers`
    ADD COLUMN `name` varchar(255) NULL COMMENT '映射别名' AFTER `user_id`;