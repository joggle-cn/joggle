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
public class DevicePeersConfigDTO {

    /**
     * id
     */
    @Schema(description = "id")
    private Long id;

    @Schema(description = "设备映射ID")
    private Long mappingId;

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

    @Schema(description = "服务侧设备No")
    private String serverDeviceNo;
    private Integer serverDeviceTunnelId;
    private Integer serverMtu;

    /**
     * 客户侧设备id
     */
    @Schema(description = "客户侧设备id")
    private Long clientDeviceId;

    @Schema(description = "客户侧设备No")
    private String clientDeviceNo;
    private Integer clientDeviceTunnelId;
    private Integer clientMtu;

    @Schema(description = "传输压缩 1启用 0禁用")
    private Integer configCompress;

    @Schema(description = "传输加密方式 none aes aes-128")
    private String configEncryption;

    @Schema(description = "循环周期(单位：ms) 10 20 30 40")
    private Integer configInterval;

    @Schema(description = "P2P/WSS bandwidth limit in Mbps; 0 means unlimited")
    private Integer bandwidth;

    @Schema(description = "P2P transport strategy: p2p, wss, or auto")
    private String strategy;

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

}
