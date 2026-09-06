package com.wuweibi.bullet.device.domain;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.io.Serializable;
import lombok.Data; 
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * (DeviceWhiteIps)分页对象
 *
 * @author marker
 * @since 2022-10-11 22:19:15
 */
@SuppressWarnings("serial")
@Data
public class DeviceWhiteIpsParam {

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
     * 分号间隔的ip地址
     */        
    @Schema(description = "分号间隔的ip地址")
 	private String ips;

}
