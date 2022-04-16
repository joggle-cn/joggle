package com.wuweibi.bullet.feedback.domain;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.Date;

/**
 * 意见反馈(Feedback)实体类
 *
 * @author marker
 * @since 2022-04-01 17:32:35
 */
@Data
public class FeedbackDTO implements Serializable {
    private static final long serialVersionUID = -43555469426416459L;

    /**
    * 标题
    */
    @NotBlank(message = "标题不能为空")
    private String title;

    /**
    * 联系方式
    */
//    @NotBlank(message = "联系方式不能为空")
    private String contact;

    /**
    * 内容
    */
    @NotBlank(message = "内容不能为空")
    private String content;

}