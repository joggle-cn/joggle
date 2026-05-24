package com.wuweibi.bullet.res.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
public class UserPackageInfoVO {

    @ApiModelProperty("套餐名称")
    private String name;

    @ApiModelProperty("套餐等级")
    private Integer level;

    @ApiModelProperty("套餐状态 1生效中 0已过期 -1未开通")
    private Integer status;

    @ApiModelProperty("套餐状态文本")
    private String statusText;

    @ApiModelProperty("开通时间")
    private Date startTime;

    @ApiModelProperty("到期时间")
    private Date endTime;

    @ApiModelProperty("剩余天数")
    private Long remainingDays;

    @ApiModelProperty("套餐周期描述")
    private String periodDesc;

    @ApiModelProperty("自动续费 1开启 0关闭")
    private Integer autoRenew;

    @ApiModelProperty("数据更新时间")
    private Date updateTime;

    @ApiModelProperty("可绑设备总数")
    private Integer deviceNum;

    @ApiModelProperty("已使用设备数")
    private Integer deviceUse;

    @ApiModelProperty("P2P隧道总数")
    private Integer p2pNum;

    @ApiModelProperty("已使用P2P隧道数")
    private Integer peerUse;

    @ApiModelProperty("端口隧道总数")
    private Integer portNum;

    @ApiModelProperty("已使用端口隧道数")
    private Integer portUse;

    @ApiModelProperty("域名隧道总数")
    private Integer domainNum;

    @ApiModelProperty("已使用域名隧道数")
    private Integer domainUse;

    @ApiModelProperty("并发限制")
    private Integer concurrentNum;

    @ApiModelProperty("当前并发使用数")
    private Integer concurrentUse;

    @ApiModelProperty("宽带速率 Mbps")
    private Integer broadbandRate;

    @ApiModelProperty("已用流量(KB)")
    private Long flowUse;

    @ApiModelProperty("套餐总流量(KB)")
    private Long flowTotal;

    @ApiModelProperty("套餐剩余流量(KB)")
    private Long flow;

    @ApiModelProperty("使用百分比")
    private Integer flowPercent;

    @ApiModelProperty("网络唤醒 1支持 0不支持")
    private Integer wolEnable;

    @ApiModelProperty("代理支持 1支持 0不支持")
    private Integer proxyEnable;

    @ApiModelProperty("流量重置规则描述")
    private String flowResetRule;

    @ApiModelProperty("下次流量重置时间")
    private Date nextFlowResetTime;
}
