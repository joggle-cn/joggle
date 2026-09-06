package com.wuweibi.bullet.device.domain;

import java.util.Date;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.io.Serializable;
import lombok.Data; 
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 设备在线日志(DeviceOnlineLog)分页对象
 *
 * @author marker
 * @since 2023-01-23 19:46:35
 */
@SuppressWarnings("serial")
@Data
public class DeviceOnlineLogDTO {

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
     * mac地址
     */        
    @Schema(description = "mac地址")
 	private String macAddr;

    /**
     * 内网ip
     */        
    @Schema(description = "内网ip")
 	private String intranetIp;

    /**
     * 公网ip
     */        
    @Schema(description = "公网ip")
 	private String publicIp;

    /**
     * 通道id
     */        
    @Schema(description = "通道id")
 	private Integer serverTunnelId;

    /**
     * 状态 1 上线 0 下线
     */        
    @Schema(description = "状态 1 上线 0 下线")
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

}
