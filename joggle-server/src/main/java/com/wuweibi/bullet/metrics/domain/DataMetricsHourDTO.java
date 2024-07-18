package com.wuweibi.bullet.metrics.domain;

import java.util.Date;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.io.Serializable;
import lombok.Data; 
import io.swagger.annotations.ApiModelProperty;
import com.baomidou.mybatisplus.annotation.TableId;

/**
 * 数据收集(小时)(DataMetricsHour)分页对象
 *
 * @author marker
 * @since 2024-07-17 18:02:16
 */
@SuppressWarnings("serial")
@Data
public class DataMetricsHourDTO {

/**
     * id
     */    @ApiModelProperty("id")
  	private Long id;

/**
     * 用户ID
     */
    @ApiModelProperty("用户ID")
 	private Long userId;

/**
     * 通道id
     */
    @ApiModelProperty("通道id")
 	private Integer serverTunnelId;

/**
     * 设备ID
     */
    @ApiModelProperty("设备ID")
 	private Long deviceId;

/**
     * 设备映射ID
     */
    @ApiModelProperty("设备映射ID")
 	private Long mappingId;

/**
     * 生成时间
     */
    @ApiModelProperty("生成时间")
 	private Date createDate;

/**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
 	private Date createTime;

/**
     * 0-1小时
     */
    @ApiModelProperty("0-1小时")
 	private String h01;

/**
     * 1-2小时
     */
    @ApiModelProperty("1-2小时")
 	private String h02;

/**
     * 2-3小时
     */
    @ApiModelProperty("2-3小时")
 	private String h03;

/**
     * 3-4小时
     */
    @ApiModelProperty("3-4小时")
 	private String h04;

/**
     * 4-5小时
     */
    @ApiModelProperty("4-5小时")
 	private String h05;

/**
     * 5-6小时
     */
    @ApiModelProperty("5-6小时")
 	private String h06;

/**
     * 6-7小时
     */
    @ApiModelProperty("6-7小时")
 	private String h07;

/**
     * 7-8小时
     */
    @ApiModelProperty("7-8小时")
 	private String h08;

/**
     * 8-9小时
     */
    @ApiModelProperty("8-9小时")
 	private String h09;

/**
     * 9-10小时
     */
    @ApiModelProperty("9-10小时")
 	private String h10;

/**
     * 10-11小时
     */
    @ApiModelProperty("10-11小时")
 	private String h11;

/**
     * 11-12小时
     */
    @ApiModelProperty("11-12小时")
 	private String h12;

/**
     * 12-13小时
     */
    @ApiModelProperty("12-13小时")
 	private String h13;

/**
     * 13-14小时
     */
    @ApiModelProperty("13-14小时")
 	private String h14;

/**
     * 14-15小时
     */
    @ApiModelProperty("14-15小时")
 	private String h15;

/**
     * 15-16小时
     */
    @ApiModelProperty("15-16小时")
 	private String h16;

/**
     * 16-17小时
     */
    @ApiModelProperty("16-17小时")
 	private String h17;

/**
     * 17-18小时
     */
    @ApiModelProperty("17-18小时")
 	private String h18;

/**
     * 18-19小时
     */
    @ApiModelProperty("18-19小时")
 	private String h19;

/**
     * 19-20小时
     */
    @ApiModelProperty("19-20小时")
 	private String h20;

/**
     * 20-21小时
     */
    @ApiModelProperty("20-21小时")
 	private String h21;

/**
     * 21-22小时
     */
    @ApiModelProperty("21-22小时")
 	private String h22;

/**
     * 22-23小时
     */
    @ApiModelProperty("22-23小时")
 	private String h23;

/**
     * 23-24小时
     */
    @ApiModelProperty("23-24小时")
 	private String h24;

}
