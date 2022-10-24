package com.wuweibi.bullet.feedback.domain;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 意见反馈(Feedback)实体类
 *
 * @author marker
 * @since 2022-04-01 17:32:35
 */
@Data
public class FeedbackReplyDTO implements Serializable {
    private static final long serialVersionUID = -43555469426416459L;

    /**
    * 联系方式
    */
    @NotNull(message = "反馈id不能空")
    private Integer feedbackId;

    /**
    * 内容
    */
    @NotBlank(message = "回复不能为空")
    @Length(max = 400, message = "内容过长，最长400字符")
    private String reply;

}