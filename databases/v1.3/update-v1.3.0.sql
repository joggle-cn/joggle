

-- 2022-09-05 marker 在线设备新增通道id
ALTER TABLE `t_device_online`
    ADD COLUMN `server_tunnel_id` int(11) NULL COMMENT '通道id' AFTER `client_version`,
    MODIFY COLUMN `updateTime` datetime(0) NULL DEFAULT NULL COMMENT '更新时间' AFTER `client_version`;

update t_device_online set  server_tunnel_id =1;

-- 2022-09-05 marker 在线设备新增设备信息
ALTER TABLE `t_device_online`
    ADD COLUMN `os` varchar(20) NULL COMMENT '操作系统' AFTER `server_tunnel_id`,
    ADD COLUMN `arch` varchar(20) NULL COMMENT 'CPU架构' AFTER `os`;



ALTER TABLE `t_server_tunnel`
    ADD COLUMN `status` int(2) NULL COMMENT '在线状态 1在线 0不在线' AFTER `buy_status`;