package com.wuweibi.bullet.res.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.Date;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * (ResourcePackage)表实体类
 *
 * @author marker
 * @since 2022-10-30 15:48:45
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName
public class ResourcePackage {
    
    /**
     * id
     */
    @TableId
    @Schema(description = "id")
  	private Integer id;
    
    /**
     * 资源包名称
     */
    @Schema(description = "资源包名称")
 	private String name;
    
    /**
     * 等级
     */
    @Schema(description = "等级")
 	private Integer level;
    
    /**
     * 价格
     */
    @Schema(description = "价格")
 	private BigDecimal price;
    
    /**
     * 域名数量
     */
    @Schema(description = "域名数量")
 	private Integer domainNum;
    
    /**
     * 端口数量
     */
    @Schema(description = "端口数量")
 	private Integer portNum;
    
    /**
     * kb 流量
     */
    @Schema(description = "kb 流量")
 	private Long flowNum;
    
    /**
     * 设备数量
     */
    @Schema(description = "设备数量")
 	private Integer deviceNum;
    
    /**
     * p2p隧道数量
     */
    @Schema(description = "p2p隧道数量")
 	private Integer p2pNum;
    
    /**
     * 网络唤醒开关
     */
    @Schema(description = "网络唤醒开关")
 	private Integer wolEnable;
    
    /**
     * 代理开关
     */
    @Schema(description = "代理开关")
 	private Integer proxyEnable;
    
    /**
     * 状态 1正常 0禁用
     */
    @Schema(description = "状态 1正常 0禁用")
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
    
    /**
     * 购买后持有天数
     */
    @Schema(description = "购买后持有天数")
 	private Integer days;
    
    /**
     * 富文本说明
     */
    @Schema(description = "富文本说明")
 	private String content;

    @Schema(description = "并发数")
    private Integer concurrentNum;

    @Schema(description = "应用场景")
    private String sence;

    @Schema(description = "宽带速度 mbps")
    private Integer broadbandRate;

    @Schema(description = "Relay mode: 1 enabled, 0 disabled")
    private Integer relayMode;
}
