package com.wuweibi.bullet.device.domain.dto;

import com.baomidou.mybatisplus.annotation.TableField;
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
public class DomainConfigVO {


	@Schema(description = "宽带mbps")
	@TableField(value = "bandwidth")
	private Integer bandwidth;

	@Schema(description = "并发连接数")
	private Integer concurrentNum;

}
