package com.wuweibi.bullet.feedback.entity;

import java.util.Date;
import java.io.Serializable;

/**
 * 意见反馈(Feedback)实体类
 *
 * @author marker
 * @since 2022-04-01 17:32:35
 */
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
    /**
    * 创建时间
    */
    private Date createTime;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

}