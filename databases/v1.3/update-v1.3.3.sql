ALTER TABLE `t_domain`
ADD COLUMN `concurrent_num` int(11) NULL COMMENT '并发连接数 ' AFTER `bandwidth`;

-- 统一并发连接数20
update t_domain set concurrent_num = 20;

