
ALTER TABLE `t_device_online` ADD COLUMN `intranetIp` varchar(30) COMMENT '内网IP' AFTER `status`;

ALTER TABLE `t_device_mapping`
ADD COLUMN `hostname`  varchar(255) NULL AFTER `remote_port`;

ALTER TABLE `t_device_mapping`
ADD COLUMN `bind_tls`  int(1) NULL AFTER `hostname`;

