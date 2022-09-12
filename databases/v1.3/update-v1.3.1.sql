
-- 记录服务器到期时间，购买的资源不能超过到期时间
ALTER TABLE `t_server_tunnel`
    ADD COLUMN `server_end_time` datetime NULL COMMENT '服务器到期时间' AFTER `status`;

-- 服务器到期时间写入
update t_server_tunnel set server_end_time = '2022-11-17 23:59:59' where id = 1;