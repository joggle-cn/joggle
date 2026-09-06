package com.wuweibi.bullet.device.domain.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * C 端设备映射展示对象
 */
@Data
@Schema(description = "设备映射展示信息")
public class DeviceMappingClientVO {

    private Long id;

    @Schema(description = "设备 id")
    private Long deviceId;

    @Schema(description = "购买的域名前缀")
    private String domain;

    @Schema(description = "映射名称")
    private String name;

    @Schema(description = "格式化的访问 URL")
    private String url;

    @Schema(description = "端口")
    private Integer port;

    @Schema(description = "域名或端口资源 id")
    private Long domainId;

    @Schema(description = "远端端口")
    private Integer remotePort;

    @Schema(description = "自定义域名")
    private String hostname;

    @Schema(description = "用户域名 id")
    private Long userDomainId;

    @Schema(description = "简单认证")
    private String auth;

    @Schema(description = "服务器地址")
    private String host;

    @Schema(description = "是否绑定 TLS")
    private Boolean bindTls;

    @Schema(description = "用户 id")
    private Long userId;

    @Schema(description = "协议 1 HTTP 2 TCP 3 HTTPS 4 HTTP/HTTPS 5 UDP")
    private Integer protocol;

    @Schema(description = "备注")
    private String description;

    @Schema(description = "映射状态 1 启用 0 停用")
    private Integer status;

    @Schema(description = "创建时间")
    private Date createTime;

    @Schema(description = "服务通道 ID")
    private Integer serverTunnelId;

    @Schema(description = "今日流量")
    private BigDecimal todayFlow;

    @Schema(description = "链接数")
    private Integer link;

    @Schema(description = "宽带 Mbps")
    private Integer bandwidth;

    @Schema(description = "最大并发连接数")
    private Integer concurrentNum;
}
