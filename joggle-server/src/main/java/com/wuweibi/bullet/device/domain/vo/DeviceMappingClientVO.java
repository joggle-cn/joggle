package com.wuweibi.bullet.device.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * C 端设备映射展示对象
 */
@Data
@ApiModel("设备映射展示信息")
public class DeviceMappingClientVO {

    private Long id;

    @ApiModelProperty("设备 id")
    private Long deviceId;

    @ApiModelProperty("购买的域名前缀")
    private String domain;

    @ApiModelProperty("映射名称")
    private String name;

    @ApiModelProperty("格式化的访问 URL")
    private String url;

    @ApiModelProperty("端口")
    private Integer port;

    @ApiModelProperty("域名或端口资源 id")
    private Long domainId;

    @ApiModelProperty("远端端口")
    private Integer remotePort;

    @ApiModelProperty("自定义域名")
    private String hostname;

    @ApiModelProperty("用户域名 id")
    private Long userDomainId;

    @ApiModelProperty("简单认证")
    private String auth;

    @ApiModelProperty("服务器地址")
    private String host;

    @ApiModelProperty("是否绑定 TLS")
    private Boolean bindTls;

    @ApiModelProperty("用户 id")
    private Long userId;

    @ApiModelProperty("协议 1 HTTP 2 TCP 3 HTTPS 4 HTTP/HTTPS 5 UDP")
    private Integer protocol;

    @ApiModelProperty("备注")
    private String description;

    @ApiModelProperty("映射状态 1 启用 0 停用")
    private Integer status;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("服务通道 ID")
    private Integer serverTunnelId;

    @ApiModelProperty("今日流量")
    private BigDecimal todayFlow;

    @ApiModelProperty("链接数")
    private Integer link;

    @ApiModelProperty("宽带 Mbps")
    private Integer bandwidth;

    @ApiModelProperty("最大并发连接数")
    private Integer concurrentNum;
}
