package com.wuweibi.bullet.device.domain.vo;

import lombok.Data;

import java.util.Date;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 *
 * @author marker
 * @since 2022-10-09
 */
@Data
public class DeviceListVO  {

	private Long id;

	@Schema(description = "设备名称")
	private String name;

	@Schema(description = "用户名")
	private String username;

	@Schema(description = "设备编号")
	private String deviceNo;

	@Schema(description = "创建时间")
	private Date createTime;

	private Long userId;

	@Schema(description = "设备并发连接数")
	private Integer concurrentNum;

	@Schema(description = "操作系统")
	private String os;
	@Schema(description = "CPU平台")
	private String arch;

	@Schema(description = "公网IP")
	private String publicIp;
	@Schema(description = "内网IP")
	private String intranetIp;
	@Schema(description = "mac地址")
	private String macAddr;
	@Schema(description = "状态")
	private Integer status;
	@Schema(description = "通道id")
	private Integer serverTunnelId;
	@Schema(description = "通道名称")
	private String serverTunnelName;




	@Override
	public String toString() {
		return "Device{" +
			", id=" + id +
			", name=" + name +
			", deviceNo=" + deviceNo +
			", createTime=" + createTime +
			", userId=" + userId +
			"}";
	}
}
