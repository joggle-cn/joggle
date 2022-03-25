package com.wuweibi.bullet.flow.domain;

import java.util.Date;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.io.Serializable;
import lombok.Data;
import io.swagger.annotations.ApiModelProperty;
import com.baomidou.mybatisplus.annotation.TableId;

/**
 * 用户流量(UserFlow)分页对象
 *
 * @author marker
 * @since 2022-01-09 15:47:19
 */
@SuppressWarnings("serial")
@Data
public class UserFlowDTO {


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
