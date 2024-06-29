package com.wuweibi.bullet.res.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * (ResourcePackage)分页对象
 *
 * @author marker
 * @since 2022-10-30 15:48:50
 */
@SuppressWarnings("serial")
@Data
public class ResourcePackageListVO {

    /**
     * id
     */
    @ApiModelProperty("id")
  	private Integer id;    

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
     * 价格
     */        
    @ApiModelProperty("价格")
 	private BigDecimal price;

    /**
     * 域名数量
     */        
    @ApiModelProperty("域名数量")
 	private Integer domainNum;

    /**
     * 端口数量
     */        
    @ApiModelProperty("端口数量")
 	private Integer portNum;

    /**
     * kb 流量
     */        
    @ApiModelProperty("kb 流量")
 	private Long flowNum;

    /**
     * 设备数量
     */        
    @ApiModelProperty("设备数量")
 	private Integer deviceNum;

    /**
     * p2p隧道数量
     */        
    @ApiModelProperty("p2p隧道数量")
 	private Integer p2pNum;

 	private Integer concurrentNum;

 	private String sence;

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
     * 状态 1正常 0禁用
     */        
    @ApiModelProperty("状态 1正常 0禁用")
 	private Integer status;

    /**
     * 创建时间
     */        
    @ApiModelProperty("创建时间")
 	private Date createTime;

    /**
     * 更新时间
     */        
    @ApiModelProperty("更新时间")
 	private Date updateTime;

    /**
     * 购买后持有天数
     */        
    @ApiModelProperty("购买后持有天数")
 	private Integer days;

    @ApiModelProperty("宽带速度 mbps")
    private Integer broadbandRate;


}
