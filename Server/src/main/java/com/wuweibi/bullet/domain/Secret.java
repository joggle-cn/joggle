package com.wuweibi.bullet.domain;

import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.core.index.GeoSpatialIndexed;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by marker on 16/8/3.
 */
@Document(collection = "user_secret")
public class Secret implements Serializable{


    private long id;

    // 经度
    @Indexed(name="index_longitude")
    private double longitude;
    // 纬度
    @Indexed(name="index_latitude")
    private double latitude;


    @GeoSpatialIndexed(name = "index_location")
    private Point location;

    // 内容
    private String content;

    // 用户Id
    private long userId;
    // 创建时间
    private Date createTime;


    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Point getLocation() {
        return location;
    }

    public void setLocation(Point location) {
        this.location = location;
    }
}
