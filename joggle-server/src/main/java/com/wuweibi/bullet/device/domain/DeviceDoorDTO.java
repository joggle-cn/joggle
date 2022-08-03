package com.wuweibi.bullet.device.domain;

import java.util.Date;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.io.Serializable;
import lombok.Data; 
import io.swagger.annotations.ApiModelProperty;
import com.baomidou.mybatisplus.annotation.TableId;

/**
 * 设备任意门(DeviceDoor)分页对象
 *
 * @author marker
 * @since 2022-08-03 21:21:27
 */
@SuppressWarnings("serial")
@Data
public class DeviceDoorDTO {

    /**
     * id
     */        @ApiModelProperty("id")
  	private Long id;    

    /**
     * 设备id
     */        
    @ApiModelProperty("设备id")
 	private Long deviceId;

    /**
     * 本地服务路径
     */        
    @ApiModelProperty("本地服务路径")
 	private String localPath;

    /**
     * 服务端路径
     */        
    @ApiModelProperty("服务端路径")
 	private String serverPath;

    /**
     * 启用状态 1启用 0停用
     */        
    @ApiModelProperty("启用状态 1启用 0停用")
 	private Integer enable;

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

}
