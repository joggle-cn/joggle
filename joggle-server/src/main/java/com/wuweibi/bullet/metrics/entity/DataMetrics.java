package com.wuweibi.bullet.metrics.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;

import java.util.Calendar;
import java.util.Date;

/**
 * 数据收集(DataMetrics)表实体类
 *
 * @author marker
 * @since 2021-11-07 14:17:46
 */
@SuppressWarnings("serial")
@Data
public class DataMetrics extends Model<DataMetrics> {
    /**
     * id
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;
    /**
     * 用户ID
     */
    private Long userId;
    /**
     * 设备ID
     */
    private Long deviceId;
    /**
     * 设备映射ID
     */
    private Long mappingId;
    /**
     * 进入流量
     */
    private Long bytesIn;
    /**
     * 出口流量
     */
    private Long bytesOut;
    /**
     * 创建时间
     */
    private Date createTime;


    /**
     * 创建天
     */
    private Date createDate;

    /**
     * 创建月
     */
    private Date createMonth;

    /**
     * 创建年
     */
    private Integer createYear;




    public void setCreateTime(Date time){
        this.createTime = time;
        this.createDate = time;

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(time);
        calendar.set(Calendar.DATE, 1);
        this.createMonth = calendar.getTime();
        this.createYear = calendar.get(Calendar.YEAR);

    }


}
