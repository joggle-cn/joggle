package com.wuweibi.bullet.device.domain;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.io.Serializable;
import lombok.Data; 
import io.swagger.annotations.ApiModelProperty;
import com.baomidou.mybatisplus.annotation.TableId;

/**
 * (DeviceWhiteIps)分页对象
 *
 * @author marker
 * @since 2022-10-11 22:19:15
 */
@SuppressWarnings("serial")
@Data
public class DeviceWhiteIpsVO {

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
     * 分号间隔的ip地址
     */        
    @ApiModelProperty("分号间隔的ip地址")
 	private String ips;

}
