-- v1.3.13 flow 数据迁移到 flow_use
-- 将 flow（可用流量）的值刷到 flow_use 字段，后续扣减流量改用 flow_use

UPDATE user_package SET flow_use = flow WHERE flow IS NOT NULL;
