package com.wuweibi.bullet.device.domain.dto;

import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;

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

	@Schema(description = "设备编号")
	private String deviceNo;

	@Schema(description = "通道id")
	private Integer serverTunnelId;


	@Schema(description = "设备状态 1在线 ")
	private Integer status;

	@Schema(description = "用户id")
	private Long userId;

}
