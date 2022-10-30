package com.wuweibi.bullet.res.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
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
public class ResourcePackageDTO {

    /**
     * id
     */
    @ApiModelProperty("id")
  	private Integer id;    

    /**
     * 资源包名称
     */        
    @ApiModelProperty("资源包名称")
    @NotBlank(message = "名称不能为空")
 	private String name;

    /**
     * 等级
     */        
    @ApiModelProperty("等级")
    @NotNull(message = "等级不能为空")
 	private Integer level;

    /**
     * 价格
     */        
    @ApiModelProperty("价格")
    @NotNull(message = "价格不能为空")
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
    @ApiModelProperty("KB 流量")
 	private Long flowNum;

    /**
     * 设备数量
     */        
    @ApiModelProperty("设备数量")
 	private Integer deviceNum;

    @ApiModelProperty("并发数量")
 	private Integer concurrentNum;

    @ApiModelProperty("应用场景")
    private String sence;

    /**
     * p2p隧道数量
     */        
    @ApiModelProperty("p2p隧道数量")
 	private Integer p2pNum;

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

    /**
     * 富文本说明
     */        
    @ApiModelProperty("富文本说明")
 	private String content;


    @ApiModelProperty("宽带速度 mbps")
    private Integer broadbandRate;

}
