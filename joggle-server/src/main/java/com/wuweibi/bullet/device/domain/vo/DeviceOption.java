package com.wuweibi.bullet.device.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 *  设备下拉列表
 * </p>
 *
 * @author marker
 * @since 2017-12-09
 */
@Data
public class DeviceOption  {

 	@ApiModelProperty("设备id")
	private Long id;

	@ApiModelProperty("设备名称")
	private String name;

	@ApiModelProperty("设备编号")
	private String deviceNo;

	@ApiModelProperty("设备内网IP地址")
	private String intranetIp;

	@ApiModelProperty("设备外网IP地址")
	private String publicIp;




	@Override
	public String toString() {
		return "Device{" +
			", id=" + id +
			", name=" + name +
			", deviceNo=" + deviceNo +
			"}";
	}
}
