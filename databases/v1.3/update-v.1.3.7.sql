
ALTER TABLE `device_peers`
    ADD COLUMN `config_encryption` varchar(50) NULL DEFAULT 'none' COMMENT '传输加密方式 none aes aes-128' AFTER `config_compress`,
    ADD COLUMN `config_interval` int(11) NULL DEFAULT 40 COMMENT '循环周期 10 20 30 40' AFTER `config_encryption`;


update device_peers set config_encryption = "aes";
update device_peers set config_interval = 40;