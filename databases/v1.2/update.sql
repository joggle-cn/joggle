ALTER TABLE `t_sys_menu`
ADD COLUMN `level` int(1) NULL COMMENT '层级' AFTER `parent_id`;

ALTER TABLE `t_sys_menu`
MODIFY COLUMN `type` int(1) NULL DEFAULT NULL COMMENT '菜单类型（1运营）' AFTER `level`,
MODIFY COLUMN `sort` int(11) NULL DEFAULT 100 COMMENT '排序' AFTER `description`,
MODIFY COLUMN `created_time` datetime(0) NULL ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '创建时间' AFTER `created_user_id`,
MODIFY COLUMN `updated_time` datetime(0) NULL ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间' AFTER `created_time`;



INSERT INTO `t_sys_resources`(`id`, `code`, `type`, `name`, `url`, `method`, `description`, `deleted`, `created_by`, `updated_by`, `updated_user_id`, `created_user_id`, `created_time`, `updated_time`) VALUES (19, 'POST:/api/user/device/wol', 'url', '接口', '/api/user/device/wol', 'POST', '网络唤醒', 0, 'system', 'system', 0, 0, '2019-06-25 15:00:20', '2020-09-12 20:44:56');

INSERT INTO `t_sys_roles_resources_relation`(`id`, `resource_id`, `role_id`) VALUES (138, 19, 2);
INSERT INTO `t_sys_roles_resources_relation`(`id`, `resource_id`, `role_id`) VALUES (139, 19, 1);

ALTER TABLE `t_user_forget`
    MODIFY COLUMN `oldPass` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL AFTER `userId`;


ALTER TABLE `t_device_online`
    ADD COLUMN `public_ip` varchar(20) NULL AFTER `mac_addr`;


ALTER TABLE `t_device_online`
    MODIFY COLUMN `intranet_ip` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '内网IP' AFTER `status`;

-- 客户端版本管理
CREATE TABLE `client_version` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `version` varchar(255) DEFAULT NULL COMMENT '版本号',
  `title` varchar(255) DEFAULT NULL COMMENT '标题',
  `description` varchar(255) DEFAULT NULL COMMENT '描述',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `download_url` varchar(255) DEFAULT NULL COMMENT '下载URL地址',
  `checksum` varchar(255) DEFAULT NULL COMMENT '检查信息',
  `status` tinyint(1) DEFAULT NULL COMMENT '状态 1上架 0下架',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;



-- 新增数据收集
CREATE TABLE `data_metrics`  (
    `id` bigint(20) NOT NULL COMMENT 'id',
    `user_id` bigint(20) NULL DEFAULT NULL COMMENT '用户ID',
    `device_id` bigint(20) NULL DEFAULT NULL COMMENT '设备ID',
    `mapping_id` bigint(20) NULL DEFAULT NULL COMMENT '设备映射ID',
    `bytes_in` bigint(22) NULL DEFAULT NULL COMMENT '进入流量',
    `bytes_out` bigint(22) NULL DEFAULT NULL COMMENT '出口流量',
    `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
    `create_date` date NULL DEFAULT NULL COMMENT '创建天 yyyy-mm-dd',
    `create_month` date NULL DEFAULT NULL COMMENT '创建月 yyyy-mm',
    `create_year` smallint(4) NULL DEFAULT NULL COMMENT '创建年 yyyy',
    PRIMARY KEY (`id`) USING BTREE,
    INDEX `idx_user_id`(`user_id`) USING BTREE,
    INDEX `idx_create_date`(`create_date`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '数据收集' ROW_FORMAT = Dynamic;


ALTER TABLE `t_device_online` ADD COLUMN `client_version` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '客户端版本' AFTER `public_ip`;




-- 2022-04-07 版本增加标识
ALTER TABLE  `client_version`
    ADD COLUMN `os` varchar(20) NULL COMMENT '操作系统' AFTER `title`,
ADD COLUMN `arch` varchar(20) NULL COMMENT 'CPU架构' AFTER `os`;

-- 2022-04-12 新增流量日统计表
CREATE TABLE `data_metrics_day`  (
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
    `user_id` bigint(20) NULL DEFAULT NULL COMMENT '用户ID',
    `device_id` bigint(20) NULL DEFAULT NULL COMMENT '设备ID',
    `mapping_id` bigint(20) NULL DEFAULT NULL COMMENT '设备映射ID',
    `create_date` date NULL DEFAULT NULL COMMENT '生成时间',
    `create_month` date NULL DEFAULT NULL COMMENT '生成月',
    `create_year` int(4) NULL DEFAULT NULL COMMENT '生成年',
    `flow` decimal(20, 0) NULL DEFAULT NULL COMMENT '流量',
    `link` bigint(22) NULL DEFAULT NULL COMMENT '连接数',
    `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `idx_mapping_id_date`(`mapping_id`, `device_id`, `create_date`) USING BTREE,
    INDEX `idx_user_id`(`user_id`) USING BTREE,
    INDEX `idx_date`(`create_date`) USING BTREE,
    INDEX `idx_month`(`create_month`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '流量日统计表' ROW_FORMAT = Dynamic;

-- fix old pass 长度
ALTER TABLE `t_user_forget`
    MODIFY COLUMN `oldPass` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL AFTER `userId`;


ALTER TABLE `t_server_tunnel`
    ADD COLUMN `broadband` int(11) NULL COMMENT '宽带 mb' AFTER `name`;



ALTER TABLE `client_version`
    ADD COLUMN `update_time` datetime NULL COMMENT '更新时间' AFTER `status`,
COMMENT = '客户端版本';


INSERT INTO `client_version`(`id`, `version`, `title`, `os`, `arch`, `description`, `create_time`, `download_url`, `checksum`, `status`) VALUES (1, '1.2.14', 'bullet', 'linux', 'amd64', 'linux 64位', '2021-08-12 20:36:45', 'https://www.joggle.cn/ngrok/', '241f52b99a155680382353e18f104ccc861ffe1d46f7e34bfef4c751becdaf20', 1);
INSERT INTO `client_version`(`id`, `version`, `title`, `os`, `arch`, `description`, `create_time`, `download_url`, `checksum`, `status`) VALUES (2, '1.2.14', 'bullet', 'linux', '386', 'linux x86 32位版本', '2021-08-12 20:36:45', 'https://www.joggle.cn/ngrok/', '160336d50dde5f8064008c05287c6f7f1c415ebe3acdc0e6c9fa0fb3e342c0c0', 1);
INSERT INTO `client_version`(`id`, `version`, `title`, `os`, `arch`, `description`, `create_time`, `download_url`, `checksum`, `status`) VALUES (3, '1.2.14', 'bullet', 'linux', 'arm', 'linux 32位', '2021-08-12 20:36:45', 'https://www.joggle.cn/ngrok/', '41ef9351094ae8c15c5101e5fea99f760059ab040715b176fe45e7b4fd8fb851', 1);
INSERT INTO `client_version`(`id`, `version`, `title`, `os`, `arch`, `description`, `create_time`, `download_url`, `checksum`, `status`) VALUES (4, '1.2.14', 'bullet', 'linux', 'arm64', 'linux arm 64位版本', '2021-08-12 20:36:45', 'https://www.joggle.cn/ngrok/', '99ead4d62b34a3db887f307e675f0a24bb079ab8e9b96c3429fb1590814d4183', 1);
INSERT INTO `client_version`(`id`, `version`, `title`, `os`, `arch`, `description`, `create_time`, `download_url`, `checksum`, `status`) VALUES (5, '1.2.14', 'bullet', 'windows', '386', 'windows 32位', '2021-08-12 20:36:45', 'https://www.joggle.cn/ngrok/', '404a9d10ea0ef981a77dbf9b98062fb1623479b6d37d00f4a8864a7f6282b6e8', 1);
INSERT INTO `client_version`(`id`, `version`, `title`, `os`, `arch`, `description`, `create_time`, `download_url`, `checksum`, `status`) VALUES (11, '1.2.14', 'bullet', 'windows', 'amd64', 'windows 64位', '2021-08-12 20:36:45', 'https://www.joggle.cn/ngrok/', '8bc5473917a053c695902eec9f236f879b7f8f9040745b07df9abd42f55e1d63', 1);
INSERT INTO `client_version`(`id`, `version`, `title`, `os`, `arch`, `description`, `create_time`, `download_url`, `checksum`, `status`) VALUES (12, '1.2.14', 'bullet', 'darwin', '386', 'mac 32位', '2021-08-12 20:36:45', 'https://www.joggle.cn/ngrok/', '346df20ae885da8e7200be44206fe1920f31891a75dbf5934bb262aba98aed4d', 1);
INSERT INTO `client_version`(`id`, `version`, `title`, `os`, `arch`, `description`, `create_time`, `download_url`, `checksum`, `status`) VALUES (13, '1.2.14', 'bullet', 'darwin', 'amd64', 'mac 64位', '2021-08-12 20:36:45', 'https://www.joggle.cn/ngrok/', '7f1e617115689abd588591a99f9eda5cdcd770e2d9ef61ca669fc90971bc546c', 1);


-- 多线路支持
ALTER TABLE `t_server_tunnel`
    ADD COLUMN `country` varchar(255) NULL COMMENT '国家' AFTER `name`,
    ADD COLUMN `area` varchar(255) NULL COMMENT '地区' AFTER `country`,
    MODIFY COLUMN `create_time` datetime(0) NULL ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '上线时间' AFTER `server_addr`;

ALTER TABLE `t_domain`
    ADD COLUMN `server_tunnel_id` int NULL COMMENT '服务器id' AFTER `domain`;

update `t_domain` set `server_tunnel_id` = 1;

-- 增加价格字段
ALTER TABLE `t_server_tunnel`
    MODIFY COLUMN `id` int(11) NOT NULL AUTO_INCREMENT FIRST,
    ADD COLUMN `price_type` int(1) NULL DEFAULT 1 COMMENT '价格类型 1免费 2包月 3包年 ' AFTER `create_time`,
    ADD COLUMN `sales_price` decimal(10, 2) NULL COMMENT '销售价格（元/周期）' AFTER `price_type`,
    ADD COLUMN `original_price` decimal(10, 2) NULL COMMENT '原价（元/周期）' AFTER `sales_price`,
    ADD COLUMN `buy_status` int(1) NULL DEFAULT 1 COMMENT '是否可购买 1可 0不可' AFTER `original_price`;


CREATE TABLE `user_tunnel`  (
     `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
     `user_id` bigint NULL COMMENT '用户id',
     `server_tunnel_id` int NULL COMMENT '通道id',
     `create_time` datetime NULL COMMENT '创建时间',
     `update_time` datetime NULL COMMENT '更新时间',
     PRIMARY KEY (`id`)
) COMMENT = '用户的通道';






CREATE TABLE `orders`  (
                           `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '订单id',
                           `order_no` varchar(22) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '订单号',
                           `user_id` bigint(20) NULL DEFAULT NULL COMMENT '用户id',
                           `resource_type` int(2) NULL DEFAULT NULL COMMENT '资源类型 1域名 2端口 3流量 4 充值',
                           `domain_id` bigint(22) NULL DEFAULT NULL COMMENT '资源id',
                           `amount` bigint(22) NULL DEFAULT NULL COMMENT '购买量 单位：秒、MB',
                           `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '资源名称',
                           `price_amount` decimal(12, 2) NULL DEFAULT NULL COMMENT '原价',
                           `discount_amount` decimal(12, 2) NULL DEFAULT NULL COMMENT '优惠金额',
                           `pay_amount` decimal(12, 2) NULL DEFAULT NULL COMMENT '支付价格',
                           `pay_type` int(1) NULL DEFAULT NULL COMMENT '支付方式 1余额 2支付宝',
                           `status` tinyint(2) NULL DEFAULT NULL COMMENT '订单状态 0待支付 1已支付 2 取消 3退款中 4已退款',
                           `trade_no` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '三方交易号',
                           `pay_time` datetime(0) NULL DEFAULT NULL COMMENT '支付时间',
                           `refund_time` datetime(0) NULL DEFAULT NULL COMMENT '退款时间',
                           `cancel_time` datetime(0) NULL DEFAULT NULL COMMENT '取消时间',
                           `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
                           `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
                           PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;


ALTER TABLE `t_domain`
    MODIFY COLUMN `sales_price` decimal(10, 2) NULL DEFAULT NULL COMMENT '销售价格（元/天）' AFTER `create_time`,
    MODIFY COLUMN `original_price` decimal(10, 2) NULL DEFAULT NULL COMMENT '原价（元/天）' AFTER `sales_price`;



ALTER TABLE `t_sys_users`
    ADD COLUMN `user_admin` tinyint(1) NULL DEFAULT 0 COMMENT '是否后台用户 1是 0否' AFTER `activate_code`;

update t_sys_users set user_admin = 0 ;

-- 2022-07-31 忘记密码增加索引
ALTER TABLE `t_user_forget`
    ADD INDEX `idx_code`(`code`),
    ADD INDEX `idx_email`(`email`);

-- 2022-08-03 设备任意门文件服务器
CREATE TABLE `t_device_door`  (
                                  `id` bigint(22) NOT NULL AUTO_INCREMENT COMMENT 'id',
                                  `device_id` bigint(20) NULL DEFAULT NULL COMMENT '设备id',
                                  `local_path` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '本地服务路径',
                                  `server_path` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '服务端路径',
                                  `enable` int(1) NULL DEFAULT 0 COMMENT '启用状态 1启用 0停用',
                                  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
                                  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
                                  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '设备任意门' ROW_FORMAT = Dynamic;



CREATE TABLE `device_peers`  (
                                 `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
                                 `user_id` bigint(20) NULL DEFAULT NULL COMMENT '用户id',
                                 `server_device_id` bigint(11) NULL DEFAULT NULL COMMENT '服务侧设备id',
                                 `client_device_id` bigint(11) NULL DEFAULT NULL COMMENT '客户侧设备id',
                                 `server_local_port` int(11) NULL DEFAULT NULL COMMENT '服务侧本地端口',
                                 `client_proxy_port` int(11) NULL DEFAULT NULL COMMENT '客户侧代理端口',
                                 `status` tinyint(1) NULL DEFAULT NULL COMMENT '状态 1启用 0禁用',
                                 `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
                                 `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
                                 PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;


ALTER TABLE  `device_peers`
    ADD COLUMN `app_name` varchar(200) NULL COMMENT 'p2pAppName' AFTER `user_id`;

-- 2022-08-12 marker 新增特定IP
ALTER TABLE `device_peers`
    ADD COLUMN `server_local_host` varchar(20) NULL COMMENT '服务侧本地host' AFTER `server_local_port`,
    ADD COLUMN `client_proxy_host` varchar(20) NULL COMMENT '客户侧代理host' AFTER `client_proxy_port`,
    ADD COLUMN `remark` varchar(220) NULL COMMENT '备注' AFTER `client_proxy_host`;


ALTER TABLE `device_peers` COMMENT = 'P2P映射';


-- 2022-08-21 marker 设备socks5代理
CREATE TABLE `device_proxy`  (
                                 `id` bigint(22) NOT NULL AUTO_INCREMENT COMMENT 'id',
                                 `device_id` bigint(22) NULL DEFAULT NULL COMMENT '设备id',
                                 `device_proxy_port` int(11) NULL DEFAULT NULL COMMENT '设备代理服务端口',
                                 `domain_id` int(11) NULL DEFAULT NULL COMMENT '端口id',
                                 `type` varchar(20) NULL DEFAULT NULL COMMENT '代理协议：http/https/socks5',
                                 `status` tinyint(1) NULL DEFAULT NULL COMMENT '状态 1启用 0禁用',
                                 `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
                                 `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
                                 PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 COMMENT = '设备代理' ROW_FORMAT = Dynamic;

-- 2022-08-30 marker 数据收集的扩展字段
ALTER TABLE `data_metrics`
    ADD COLUMN `duration` int(11) NULL COMMENT '链接占用时长' AFTER `bytes_out`,
    ADD COLUMN `open_time` datetime NULL COMMENT '打开链接时间' AFTER `duration`,
    ADD COLUMN `close_time` datetime NULL COMMENT '关闭链接时间' AFTER `open_time`,
    ADD COLUMN `remote_addr` varchar(24) NULL COMMENT '远端地址' AFTER `close_time`;
-- 历史数据处理
update data_metrics set duration = 0, open_time = create_time, close_time = create_time;


-- 2022-08-31 marker 任意门新增domainid字段
ALTER TABLE `t_device_door`
    ADD COLUMN `domain_id` bigint(22) NULL COMMENT '域名id' AFTER `device_id`;