package com.wuweibi.bullet.res.domain;

import java.util.Date;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.io.Serializable;
import lombok.Data; 
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 用户套餐(UserPackage)分页对象
 *
 * @author marker
 * @since 2022-10-31 09:15:53
 */
@SuppressWarnings("serial")
@Data
public class UserPackageVO {

    /**
     * id
     */        
    @Schema(description = "id")
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

    /**
     * 设备数量
     */        
    @Schema(description = "设备数量")
 	private Integer deviceUse;

    /**
     * 宽带速率
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
