package com.wuweibi.bullet.feedback.domain;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
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
    @Length(max = 40, message = "标题过长，最长40字符")
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
    @Length(max = 400, message = "内容过长，最长400字符")
    private String content;

}