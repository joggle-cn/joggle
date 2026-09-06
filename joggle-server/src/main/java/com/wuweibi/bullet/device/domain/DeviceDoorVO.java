package com.wuweibi.bullet.device.domain;

import java.util.Date;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.io.Serializable;
import lombok.Data; 
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 设备任意门(DeviceDoor)分页对象
 *
 * @author marker
 * @since 2022-08-03 21:21:27
 */
@SuppressWarnings("serial")
@Data
public class DeviceDoorVO {

    /**
     * id
     */        @Schema(description = "id")
  	private Long id;    

    /**
     * 设备id
     */        
    @Schema(description = "设备id")
 	private Long deviceId;

    /**
     * 本地服务路径
     */        
    @Schema(description = "本地服务路径")
 	private String localPath;

    /**
     * 服务端路径
     */        
    @Schema(description = "服务端路径")
 	private String serverPath;

    /**
     * 启用状态 1启用 0停用
     */        
    @Schema(description = "启用状态 1启用 0停用")
 	private Integer enable;

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
