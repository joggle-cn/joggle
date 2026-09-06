package com.wuweibi.bullet.device.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * (DevicePeers)表实体类
 *
 * @author marker
 * @since 2022-08-09 10:49:46
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class DevicePeers {
    
    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    @Schema(description = "id")
  	private Long id;
    
    /**
     * 用户id
     */
    @Schema(description = "用户id")
 	private Long userId;

    @Schema(description = "p2p应用名称")
  	private String appName;

    @Schema(description = "映射别名")
    private String name;
    
    /**
     * 服务侧设备id
     */
    @Schema(description = "服务侧设备id")
 	private Long serverDeviceId;
    
    /**
     * 客户侧设备id
     */
    @Schema(description = "客户侧设备id")
 	private Long clientDeviceId;
    
    /**
     * 服务侧本地端口
     */
    @Schema(description = "服务侧本地端口")
 	private Integer serverLocalPort;

    @Schema(description = "服务侧MTU")
 	private Integer serverMtu;

    /**
     * 客户侧代理端口
     */
    @Schema(description = "客户侧代理端口")
 	private Integer clientProxyPort;


    @Schema(description = "服务侧本地Host 默认: 0.0.0.0")
    private String serverLocalHost;


    @Schema(description = "客户侧代理Host 默认: 127.0.0.1")
    private String clientProxyHost;

    @Schema(description = "客户侧MTU")
    private Integer clientMtu;


    @Schema(description = "备注")
    private String remark;

    /**
     * 状态 1启用 0禁用
     */
    @Schema(description = "状态 1启用 0禁用")
 	private Integer status;
    
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

    @Schema(description = "传输压缩 1启用 0禁用")
    private Integer configCompress;

    @Schema(description = "传输加密方式 none aes aes-128")
    private String configEncryption;

    @Schema(description = "循环周期(单位：ms) 10 20 30 40")
    private Integer configInterval;

    @Schema(description = "P2P传输策略：p2p、wss、auto")
    private String strategy;

    @Schema(description = "P2P/WSS带宽限制Mbps，0表示不限速")
    private Integer bandwidth;

}
