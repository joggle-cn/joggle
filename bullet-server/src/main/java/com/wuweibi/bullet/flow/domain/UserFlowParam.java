package com.wuweibi.bullet.flow.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 用户流量(UserFlow)分页对象
 *
 * @author marker
 * @since 2022-01-09 15:47:19
 */
@SuppressWarnings("serial")
@Data
public class UserFlowParam {


    @ApiModelProperty("")
 	private Long userId;

    /**
     * 流量 kb
     */
    @ApiModelProperty("流量 kb")
 	private Long flow;

    /**
     * 更新时间
     */
    @ApiModelProperty("更新时间")
 	private Date updatedTime;

}
