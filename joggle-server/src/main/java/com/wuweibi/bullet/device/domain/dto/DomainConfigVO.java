package com.wuweibi.bullet.device.domain.dto;

import com.baomidou.mybatisplus.annotation.TableField;
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
public class DomainConfigVO {


	@ApiModelProperty("宽带mbps")
	@TableField(value = "bandwidth")
	private Integer bandwidth;

	@ApiModelProperty("并发连接数")
	private Integer concurrentNum;

}
