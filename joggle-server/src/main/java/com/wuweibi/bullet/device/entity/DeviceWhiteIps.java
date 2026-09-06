package com.wuweibi.bullet.device.entity;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.io.Serializable;
import lombok.Data; 
import lombok.EqualsAndHashCode;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * (DeviceWhiteIps)表实体类
 *
 * @author marker
 * @since 2022-10-11 22:19:15
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class DeviceWhiteIps {
    
    /**
     * id
     */@TableId
    @Schema(description = "id")
  	private Long id;
    
    /**
     * 设备id
     */
    @Schema(description = "设备id")
 	private Long deviceId;
    
    /**
     * 分号间隔的ip地址
     */
    @Schema(description = "分号间隔的ip地址")
 	private String ips;

}
