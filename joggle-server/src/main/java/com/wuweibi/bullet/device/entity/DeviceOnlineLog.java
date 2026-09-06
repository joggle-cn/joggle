package com.wuweibi.bullet.device.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 设备在线日志(DeviceOnlineLog)表实体类
 *
 * @author marker
 * @since 2023-01-23 19:46:35
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class DeviceOnlineLog {
    
    /**
     * id
     */
    @TableId
    @Schema(description = "id")
  	private Long id;
    
    /**
     * userId
     */
    @Schema(description = "用户id")
 	private Long userId;

    /**
     * 设备id
     */
    @Schema(description = "设备id")
 	private Long deviceId;

    @Schema(description = "设备名称")
 	private String deviceName;
    
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
     * 状态 1 上线 -1 下线
     */
    @Schema(description = "状态 1 上线 -1 下线")
 	private Integer status;

    @Schema(description = "操作系统")
    private String os;

    @Schema(description = "CPU架构")
    private String arch;
    
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
