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
public class TunnelOption {

 	@ApiModelProperty("通道id")
	private Long id;

	@ApiModelProperty("通道名称")
	private String name;





	@Override
	public String toString() {
		return "TunnelOption{" +
			", id=" + id +
			", name=" + name +
			"}";
	}
}
