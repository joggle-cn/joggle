package com.wuweibi.bullet.device.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 *
 * </p>
 *
 * @author marker
 * @since 2017-12-09
 */
@Data
public class DeviceAdminParam {

	@ApiModelProperty("设备编号")
	private String deviceNo;

	@ApiModelProperty("通道id")
	private Integer serverTunnelId;

	@ApiModelProperty("用户id")
	private Long userId;

}
