ALTER TABLE `device_peers`
    ADD COLUMN `server_mtu` int(4) NULL COMMENT '服务侧MTU' AFTER `server_local_host`,
    ADD COLUMN `client_mtu` int(4) NULL COMMENT '客户侧MTU' AFTER `client_proxy_host`;