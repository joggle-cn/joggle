package com.wuweibi.bullet.res.domain;

import lombok.Data;

import java.util.Date;
import io.swagger.v3.oas.annotations.media.Schema;

@Data
public class UserPackageInfoVO {

    @Schema(description = "套餐名称")
    private String name;

    @Schema(description = "套餐等级")
    private Integer level;

    @Schema(description = "套餐状态 1生效中 0已过期 -1未开通")
    private Integer status;

    @Schema(description = "套餐状态文本")
    private String statusText;

    @Schema(description = "开通时间")
    private Date startTime;

    @Schema(description = "到期时间")
    private Date endTime;

    @Schema(description = "剩余天数")
    private Long remainingDays;

    @Schema(description = "套餐周期描述")
    private String periodDesc;

    @Schema(description = "自动续费 1开启 0关闭")
    private Integer autoRenew;

    @Schema(description = "数据更新时间")
    private Date updateTime;

    @Schema(description = "可绑设备总数")
    private Integer deviceNum;

    @Schema(description = "已使用设备数")
    private Integer deviceUse;

    @Schema(description = "P2P隧道总数")
    private Integer p2pNum;

    @Schema(description = "已使用P2P隧道数")
    private Integer peerUse;

    @Schema(description = "端口隧道总数")
    private Integer portNum;

    @Schema(description = "已使用端口隧道数")
    private Integer portUse;

    @Schema(description = "域名隧道总数")
    private Integer domainNum;

    @Schema(description = "已使用域名隧道数")
    private Integer domainUse;

    @Schema(description = "并发限制")
    private Integer concurrentNum;

    @Schema(description = "当前并发使用数")
    private Integer concurrentUse;

    @Schema(description = "宽带速率 Mbps")
    private Integer broadbandRate;

    @Schema(description = "已用流量(KB)")
    private Long flowUse;

    @Schema(description = "套餐总流量(KB)")
    private Long flowTotal;

    @Schema(description = "套餐剩余流量(KB)")
    private Long flow;

    @Schema(description = "使用百分比")
    private Integer flowPercent;

    @Schema(description = "网络唤醒 1支持 0不支持")
    private Integer wolEnable;

    @Schema(description = "代理支持 1支持 0不支持")
    private Integer proxyEnable;

    @Schema(description = "流量重置规则描述")
    private String flowResetRule;

    @Schema(description = "下次流量重置时间")
    private Date nextFlowResetTime;
}
