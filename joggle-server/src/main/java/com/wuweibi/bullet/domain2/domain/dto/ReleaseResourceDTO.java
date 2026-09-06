package com.wuweibi.bullet.domain2.domain.dto;
/**
 * Created by marker on 2018/3/15.
 */

import lombok.Data;

import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 角色
 *
 * @author marker
 * @create 2018-03-15 上午10:39
 **/
@Data
public class ReleaseResourceDTO implements Serializable {


    @Schema(description = "服务提通道ID")
    @NotNull(message = "服务提通道ID不能空")
    private Integer serverTunnelId;

    @Schema(description = "释放资源类型 1 端口 2 域名")
    @NotNull(message = "释放资源类型不能为空")
    private Integer type;


}
