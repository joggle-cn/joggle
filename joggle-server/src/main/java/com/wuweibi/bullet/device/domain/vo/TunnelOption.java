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
public class TunnelOption {

 	@Schema(description = "通道id")
	private Long id;

	@Schema(description = "通道名称")
	private String name;





	@Override
	public String toString() {
		return "TunnelOption{" +
			", id=" + id +
			", name=" + name +
			"}";
	}
}
