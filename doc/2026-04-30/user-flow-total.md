# 用户流量累计充值字段需求

## 背景
在 `user_flow` 表中新增 `flow_total` 字段，用于记录用户的累计充值流量。

## 需求描述

### 字段变更
`user_flow` 表新增字段 `flow_total`（bigint, 默认0），位于 `flow` 字段之后，表示累计充值流量（单位 kb）。

### 购买流量逻辑
用户购买流量后，根据当前剩余流量（`flow` 字段）决定 `flow_total` 的处理方式：

1. **当前 `flow` > 0**：用户仍有剩余流量，将本次购买的流量**追加**到 `flow_total`（`flow_total = flow_total + 购买量`）
2. **当前 `flow` <= 0**：用户流量已用完，将 `flow_total` **直接设置**为本次购买的流量（`flow_total = 购买量`）

无论哪种情况，可用流量 `flow` 均正常增加（`flow = flow + 购买量`）。

### 涉及文件

| 文件 | 改动 |
|------|------|
| `databases/v1.3/update-v1.3.12.sql` | 新增 `flow_total` 字段 DDL |
| `entity/UserFlow.java` | 新增 `flowTotal` 属性 |
| `domain/UserFlowVO.java` | 新增 `flowTotal` 属性 |
| `domain/UserFlowParam.java` | 新增 `flowTotal` 属性 |
| `domain/UserFlowDTO.java` | 新增 `flowTotal` 属性 |
| `mapper/UserFlowMapper.xml` | 新增 `updateFlowTotal` SQL，更新 BaseResultMap |
| `mapper/UserFlowMapper.java` | 新增 `updateFlowTotal` 方法 |
| `service/UserFlowService.java` | 新增 `purchaseFlow` 方法 |
| `service/impl/UserFlowServiceImpl.java` | 实现 `purchaseFlow` 逻辑 |
| `business/impl/OrderPayBizImpl.java` | 购买流量 case 3 调用 `purchaseFlow` |
