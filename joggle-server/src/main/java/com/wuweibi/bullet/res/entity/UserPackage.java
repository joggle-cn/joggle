package com.wuweibi.bullet.res.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 用户套餐(UserPackage)表实体类
 *
 * @author marker
 * @since 2022-10-31 09:15:53
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName
public class UserPackage {
    
    /**
     * id
     */
    @Schema(description = "id")
    @TableId
 	private Long userId;
    
    /**
     * 资源包id
     */
    @Schema(description = "资源包id")
 	private Integer resourcePackageId;
    
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
     * 创建时间
     */
    @Schema(description = "创建时间")
 	private Date createTime;
    
    /**
     * 域名数量
     */
    @Schema(description = "域名数量")
 	private Integer domainUse;
    
    /**
     * 端口数量
     */
    @Schema(description = "端口数量")
 	private Integer portUse;
    
    /**
     * kb 流量
     */
    @Schema(description = "kb 流量")
 	private Long flowUse;

    @Schema(description = "套餐剩余流量KB")
 	private Long flow;

    @Schema(description = "套餐总流量KB")
 	private Long flowTotal;

    /**
     * 设备数量
     */
    @Schema(description = "设备数量")
 	private Integer deviceUse;
    
    /**
     * 宽带速率 mbps
     */
    @Schema(description = "宽带速率")
 	private Integer broadbandRate;
    
    /**
     * 并发数
     */
    @Schema(description = "并发数")
 	private Integer concurrentNum;
    
    /**
     * 开始时间
     */
    @Schema(description = "开始时间")
 	private Date startTime;
    
    /**
     * 结束时间
     */
    @Schema(description = "结束时间")
 	private Date endTime;
    
    /**
     * p2p隧道数量
     */
    @Schema(description = "p2p隧道数量")
 	private Integer peerUse;

}
