package com.wuweibi.bullet.flow.domain;

import lombok.Data;

import java.util.Date;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 用户流量(UserFlow)分页对象
 *
 * @author marker
 * @since 2022-01-09 15:47:19
 */
@SuppressWarnings("serial")
@Data
public class UserFlowParam {


    @Schema(description = "")
 	private Long userId;

    /**
     * 流量 kb
     */
    @Schema(description = "流量 kb")
 	private Long flow;

    /**
     * 累计充值流量 kb
     */
    @Schema(description = "累计充值流量 kb")
 	private Long flowTotal;

    /**
     * 更新时间
     */
    @Schema(description = "更新时间")
 	private Date updatedTime;

}
