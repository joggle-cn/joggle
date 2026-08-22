DROP TEMPORARY TABLE IF EXISTS `tmp_device_peer_mapping_ids`;
CREATE TEMPORARY TABLE `tmp_device_peer_mapping_ids` (
    `old_id` bigint(20) NOT NULL,
    `new_id` bigint(20) NOT NULL,
    PRIMARY KEY (`old_id`),
    UNIQUE KEY `uk_new_id` (`new_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

SET @peer_shared_base_id := (
    SELECT GREATEST(
        IFNULL((SELECT MAX(id) FROM `t_device_mapping`), 0),
        IFNULL((SELECT MAX(id) FROM `device_peers`), 0)
    ) + 1
);

INSERT INTO `tmp_device_peer_mapping_ids` (`old_id`, `new_id`)
SELECT dp.id, (@peer_shared_base_id := @peer_shared_base_id + 1) - 1
FROM `device_peers` dp
ORDER BY dp.id;

UPDATE `device_peers` dp
JOIN `tmp_device_peer_mapping_ids` m ON dp.id = m.old_id
SET dp.id = m.new_id;

INSERT INTO `t_device_mapping` (
    `id`,
    `name`,
    `device_id`,
    `port`,
    `port_protocol`,
    `userId`,
    `host`,
    `protocol`,
    `description`,
    `status`,
    `createTime`,
    `server_tunnel_id`,
    `is_del`,
    `update_time`
)
SELECT
    dp.id,
    IFNULL(NULLIF(dp.name, ''), IFNULL(NULLIF(dp.app_name, ''), CONCAT('Peer-', dp.id))),
    dp.server_device_id,
    dp.server_local_port,
    'kcp',
    dp.user_id,
    dp.server_local_host,
    6,
    IFNULL(NULLIF(dp.remark, ''), 'devicePeer-attached-mapping'),
    IFNULL(dp.status, 0),
    IFNULL(dp.create_time, NOW()),
    d.server_tunnel_id,
    0,
    IFNULL(dp.update_time, IFNULL(dp.create_time, NOW()))
FROM `device_peers` dp
LEFT JOIN `t_device` d ON d.id = dp.server_device_id
LEFT JOIN `t_device_mapping` dm ON dm.id = dp.id
WHERE dm.id IS NULL;

UPDATE `t_device_mapping` dm
JOIN `device_peers` dp ON dm.id = dp.id
LEFT JOIN `t_device` d ON d.id = dp.server_device_id
SET dm.`name` = IFNULL(NULLIF(dp.name, ''), IFNULL(NULLIF(dp.app_name, ''), CONCAT('Peer-', dp.id))),
    dm.`device_id` = dp.server_device_id,
    dm.`port` = dp.server_local_port,
    dm.`port_protocol` = 'kcp',
    dm.`userId` = dp.user_id,
    dm.`host` = dp.server_local_host,
    dm.`protocol` = 6,
    dm.`description` = IFNULL(NULLIF(dp.remark, ''), 'devicePeer-attached-mapping'),
    dm.`status` = IFNULL(dp.status, 0),
    dm.`createTime` = IFNULL(dp.create_time, IFNULL(dm.`createTime`, NOW())),
    dm.`server_tunnel_id` = d.server_tunnel_id,
    dm.`is_del` = 0,
    dm.`update_time` = IFNULL(dp.update_time, IFNULL(dp.create_time, NOW()));

SET @shared_next_id := (
    SELECT GREATEST(
        IFNULL((SELECT MAX(id) FROM `t_device_mapping`), 0),
        IFNULL((SELECT MAX(id) FROM `device_peers`), 0)
    ) + 1
);

SET @sql_device_peers := CONCAT('ALTER TABLE `device_peers` AUTO_INCREMENT = ', @shared_next_id);
PREPARE stmt_device_peers FROM @sql_device_peers;
EXECUTE stmt_device_peers;
DEALLOCATE PREPARE stmt_device_peers;

SET @sql_device_mapping := CONCAT('ALTER TABLE `t_device_mapping` AUTO_INCREMENT = ', @shared_next_id);
PREPARE stmt_device_mapping FROM @sql_device_mapping;
EXECUTE stmt_device_mapping;
DEALLOCATE PREPARE stmt_device_mapping;

DROP TEMPORARY TABLE IF EXISTS `tmp_device_peer_mapping_ids`;


ALTER TABLE `device_peers`
    MODIFY COLUMN `id` bigint(20) NOT NULL COMMENT 'id' FIRST;