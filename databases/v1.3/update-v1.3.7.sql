
ALTER TABLE `device_peers`
    ADD COLUMN `config_encryption` varchar(50) NULL DEFAULT 'none' COMMENT '传输加密方式 none aes aes-128' AFTER `config_compress`,
    ADD COLUMN `config_interval` int(11) NULL DEFAULT 40 COMMENT '循环周期 10 20 30 40' AFTER `config_encryption`;


update device_peers set config_encryption = "aes";
update device_peers set config_interval = 40;


ALTER TABLE `t_device_mapping`
    ADD COLUMN `user_domain_id` bigint NULL COMMENT '用户域名id' AFTER `domain_id`;

-- 存量数据刷到自定义域名表中
INSERT INTO  `user_domain`(`id`, `user_id`, `domain`, `is_cert`, `cert_key`, `cert_pem`,
     `apply_time`, `due_time`, `create_time`, `update_time`) (
    select null,userId, hostname, 0,null,null, null, null,now(),now() from t_device_mapping where hostname is not null and hostname != ''
      and hostname not in (select domain from user_domain)
);

update t_device_mapping a , user_domain b
set a.user_domain_id = b.id
where a.hostname = b.domain;


