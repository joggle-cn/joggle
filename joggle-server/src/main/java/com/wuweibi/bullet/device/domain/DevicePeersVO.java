package com.wuweibi.bullet.device.domain;

import lombok.Data;

import java.util.Date;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * (DevicePeers)分页对象
 *
 * @author marker
 * @since 2022-08-09 10:49:46
 */
@SuppressWarnings("serial")
@Data
public class DevicePeersVO {

    /**
     * id
     */
    @Schema(description = "id")
    private Long id;

    /**
     * 用户id
     */
    @Schema(description = "用户id")
    private Long userId;

    @Schema(description = "p2p应用名称")
    private String appName;

    /**
     * 服务侧设备id
     */
    @Schema(description = "服务侧设备id")
    private Long serverDeviceId;
    @Schema(description = "服务侧设备名称")
    private String serverDeviceNo;

    /**
     * 客户侧设备id
     */
    @Schema(description = "客户端侧设备id")
    private Long clientDeviceId;
    @Schema(description = "客户端侧设备名称")
    private String clientDeviceNo;

    /**
     * 服务侧本地端口
     */
    @Schema(description = "服务侧本地端口")
    private Integer serverLocalPort;

    /**
     * 客户侧代理端口
     */
    @Schema(description = "客户侧代理端口")
    private Integer clientProxyPort;


    @Schema(description = "服务侧本地Host 默认: 0.0.0.0")
    private String serverLocalHost;


    @Schema(description = "客户侧设备内网Ip")
    private String clientDeviceIp;


    @Schema(description = "客户侧代理Host 默认: 127.0.0.1")
    private String clientProxyHost;


    @Schema(description = "映射别名")
    private String name;

    @Schema(description = "备注")
    private String remark;

    /**
     * 状态 1启用 0禁用
     */
    @Schema(description = "状态 1启用 0禁用")
    private Integer status;

    @Schema(description = "状态名称")
    private String statusName;

    /**
     * 创建时间
     */
    @Schema(description = "创建时间")
    private Date createTime;

    /**
     * 更新时间
     */
    @Schema(description = "更新时间")
    private Date updateTime;


    @Schema(description = "通信MTU 默认：1350")
    private Integer mtu = 1350;

    @Schema(description = "P2P传输策略：p2p、wss、auto")
    private String strategy;

    @Schema(description = "P2P/WSS带宽限制Mbps，0表示不限速")
    private Integer bandwidth;

}
