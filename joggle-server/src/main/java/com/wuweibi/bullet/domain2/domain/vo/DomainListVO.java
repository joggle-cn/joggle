package com.wuweibi.bullet.domain2.domain.vo;

import lombok.Data;

import java.util.Date;
import io.swagger.v3.oas.annotations.media.Schema;

@Data
public class DomainListVO {

    @Schema(description = "id")
    private Long id;
    @Schema(description = "域名")
    private String domain;
    @Schema(description = "域名全称")
    private String domainFull;
    @Schema(description = "类型")
    private Integer type;
    @Schema(description = "状态：1已售、0释放、-1 禁售")
    private Integer status;
    private String statusName;

    @Schema(description = "用户id")
    private Long userId;

    @Schema(description = "到期时间")
    private Date dueTime;

    @Schema(description = "创建时间")
    private Date createTime;

    @Schema(description = "购买时间")
    private Date buyTime;

    @Schema(description = "通道id")
    private Integer serverTunnelId;

    @Schema(description = "通道名称")
    private String serverTunnelName;

    @Schema(description = "设备信息")
    private String deviceInfo;


    @Schema(description = "宽带mbps")
    private Integer bandwidth;

    @Schema(description = "并发连接数 每秒")
    private Integer concurrentNum;


}
