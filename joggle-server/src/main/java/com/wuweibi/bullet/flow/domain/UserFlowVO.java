package com.wuweibi.bullet.flow.domain;

import java.util.Date;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.io.Serializable;
import lombok.Data;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 用户流量(UserFlow)分页对象
 *
 * @author marker
 * @since 2022-01-09 15:47:18
 */
@SuppressWarnings("serial")
@Data
public class UserFlowVO {


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
