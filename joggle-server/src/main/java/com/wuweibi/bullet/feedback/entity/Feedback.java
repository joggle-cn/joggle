package com.wuweibi.bullet.feedback.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.io.Serializable;

/**
 * 意见反馈(Feedback)实体类
 *
 * @author marker
 * @since 2022-04-01 17:32:35
 */

@Data
public class Feedback implements Serializable {
    private static final long serialVersionUID = -43555469426416459L;
    /**
    * id
    */
    private Integer id;
    /**
    * 用户id
    */
    private Long userId;
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

    @ApiModelProperty("回复内容")
    private String reply;


    /**
    * 创建时间
    */
    private Date createTime;


    @ApiModelProperty("回复时间")
    private Date replyTime;



}