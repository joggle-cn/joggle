package com.wuweibi.bullet.device.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 *
 * @author marker
 * @since 2022-10-09
 */
@Data
public class DeviceListVO  {

	private Long id;

	@ApiModelProperty("设备名称")
	private String name;

	@ApiModelProperty("用户名")
	private String username;

	@ApiModelProperty("设备编号")
	private String deviceNo;

	@ApiModelProperty("创建时间")
	private Date createTime;

	private Long userId;

	@ApiModelProperty("设备并发连接数")
	private Integer concurrentNum;

	@ApiModelProperty("操作系统")
	private String os;
	@ApiModelProperty("CPU平台")
	private String arch;

	@ApiModelProperty("公网IP")
	private String publicIp;
	@ApiModelProperty("内网IP")
	private String intranetIp;
	@ApiModelProperty("mac地址")
	private String macAddr;
	@ApiModelProperty("状态")
	private Integer status;
	@ApiModelProperty("通道id")
	private Integer serverTunnelId;
	@ApiModelProperty("通道名称")
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
