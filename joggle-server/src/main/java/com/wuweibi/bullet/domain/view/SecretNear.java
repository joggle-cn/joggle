package com.wuweibi.bullet.domain.view;

import java.util.Date;

/**
 * Created by marker on 16/8/12.
 *
 * 附近的秘密(渲染前端json)
 *
 */
public class SecretNear {


    private long id;
    // 用户Id
    private long userId;

    // 用户名
    private String username;

    // 用户头像
    private String usericon;

    // 内容
    private String content;

    // 距离
    private double distance;

    // 创建时间
    private Date createTime;



    public SecretNear(){

    }


    public SecretNear(long id, long userId, String username, String usericon, String content, double distance, Date createTime) {
        this.id = id;
        this.userId = userId;
        this.username = username;
        this.usericon = usericon;
        this.content = content;
        this.distance = distance;
        this.createTime = createTime;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsericon() {
        return usericon;
    }

    public void setUsericon(String usericon) {
        this.usericon = usericon;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
