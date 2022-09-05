ALTER TABLE `t_device_online`
    ADD COLUMN `server_tunnel_id` int(11) NULL COMMENT '通道id' AFTER `client_version`,
    MODIFY COLUMN `updateTime` datetime(0) NULL DEFAULT NULL COMMENT '更新时间' AFTER `client_version`;

update t_device_online set  server_tunnel_id =1;


ALTER TABLE `t_device_online`
    ADD COLUMN `os` varchar(20) NULL COMMENT '操作系统' AFTER `server_tunnel_id`,
    ADD COLUMN `arch` varchar(20) NULL COMMENT 'CPU架构' AFTER `os`;