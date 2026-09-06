package com.wuweibi.bullet.device.domain.vo;

import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;

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

 	@Schema(description = "设备id")
	private Long id;

	@Schema(description = "设备名称")
	private String name;

	@Schema(description = "设备编号")
	private String deviceNo;

	@Schema(description = "设备内网IP地址")
	private String intranetIp;

	@Schema(description = "设备外网IP地址")
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
