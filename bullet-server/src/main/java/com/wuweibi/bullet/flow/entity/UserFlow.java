package com.wuweibi.bullet.flow.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 用户流量(UserFlow)表实体类
 *
 * @author marker
 * @since 2022-01-09 15:47:06
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class UserFlow {


    @ApiModelProperty("用户ID")
    @TableId(type = IdType.INPUT)
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
