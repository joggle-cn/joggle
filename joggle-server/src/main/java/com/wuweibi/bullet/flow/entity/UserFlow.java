package com.wuweibi.bullet.flow.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 用户流量(UserFlow)表实体类
 *
 * @author marker
 * @since 2022-01-09 15:47:06
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class UserFlow {


    @Schema(description = "用户ID")
    @TableId(type = IdType.INPUT)
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
