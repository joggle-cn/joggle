package com.wuweibi.bullet.feedback.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 意见反馈(Feedback)实体类
 *
 * @author marker
 * @since 2022-04-01 17:32:35
 */
@Data
public class FeedbackListVO implements Serializable {
    private static final long serialVersionUID = -43555469426416459L;

    /**
     * id
     */
    private Integer id;
    /**
     * 用户id
     */
    private Long userId;

    @ApiModelProperty("用户名")
    private String username;

    /**
     * 标题
     */
    private String title;
    /**
     * 联系方式
     */
    private String contact;
    /**
     * 内容
     */
    private String content;
    /**
     * 创建时间
     */
    private Date createTime;

}