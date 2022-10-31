package com.wuweibi.bullet.res.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

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
    @ApiModelProperty("id")
    @TableId
 	private Long userId;
    
    /**
     * 资源包id
     */
    @ApiModelProperty("资源包id")
 	private Integer resourcePackageId;
    
    /**
     * 资源包名称
     */
    @ApiModelProperty("资源包名称")
 	private String name;
    
    /**
     * 等级
     */
    @ApiModelProperty("等级")
 	private Integer level;
    
    /**
     * 网络唤醒开关
     */
    @ApiModelProperty("网络唤醒开关")
 	private Integer wolEnable;
    
    /**
     * 代理开关
     */
    @ApiModelProperty("代理开关")
 	private Integer proxyEnable;
    
    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
 	private Date createTime;
    
    /**
     * 域名数量
     */
    @ApiModelProperty("域名数量")
 	private Integer domainUse;
    
    /**
     * 端口数量
     */
    @ApiModelProperty("端口数量")
 	private Integer portUse;
    
    /**
     * kb 流量
     */
    @ApiModelProperty("kb 流量")
 	private Long flowUse;
    
    /**
     * 设备数量
     */
    @ApiModelProperty("设备数量")
 	private Integer deviceUse;
    
    /**
     * 宽带速率
     */
    @ApiModelProperty("宽带速率")
 	private Integer broadbandRate;
    
    /**
     * 并发数
     */
    @ApiModelProperty("并发数")
 	private Integer concurrentNum;
    
    /**
     * 开始时间
     */
    @ApiModelProperty("开始时间")
 	private Date startTime;
    
    /**
     * 结束时间
     */
    @ApiModelProperty("结束时间")
 	private Date endTime;
    
    /**
     * p2p隧道数量
     */
    @ApiModelProperty("p2p隧道数量")
 	private Integer peerUse;

}
