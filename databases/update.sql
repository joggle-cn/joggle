
ALTER TABLE `t_device_online` ADD COLUMN `intranetIp` varchar(30) COMMENT '内网IP' AFTER `status`;

ALTER TABLE `t_device_mapping`
ADD COLUMN `hostname`  varchar(255) NULL AFTER `remote_port`;

ALTER TABLE `t_device_mapping`
ADD COLUMN `bind_tls`  int(1) NULL AFTER `hostname`;




-- 2019年6月4日 更新

ALTER TABLE `t_device_online` CHANGE COLUMN `deviceId` `deviceNo` varchar(255) NOT NULL COMMENT '设备编号';
ALTER TABLE `t_device_online` ADD COLUMN `mac_addr` varchar(20) COMMENT 'Mac地址' AFTER `intranetIp`;
ALTER TABLE  `t_device_online` CHANGE COLUMN `intranetIp` `intranetIp` varchar(100) DEFAULT NULL COMMENT '内网IP';
ALTER TABLE  `t_device` ADD COLUMN `mac_addr` varchar(20) COMMENT 'mac地址' AFTER `intranetIp`;


ALTER TABLE `t_device` CHANGE COLUMN `intranetIp` `intranet_ip` varchar(30) DEFAULT NULL COMMENT '内网IP';