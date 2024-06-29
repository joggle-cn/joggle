package com.wuweibi.bullet.domain2.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
public class DomainListVO {

    @ApiModelProperty("id")
    private Long id;
    @ApiModelProperty("域名")
    private String domain;
    @ApiModelProperty("域名全称")
    private String domainFull;
    @ApiModelProperty("类型")
    private Integer type;
    @ApiModelProperty("状态：1已售、0释放、-1 禁售")
    private Integer status;
    private String statusName;

    @ApiModelProperty("用户id")
    private Long userId;

    @ApiModelProperty("到期时间")
    private Date dueTime;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("购买时间")
    private Date buyTime;

    @ApiModelProperty("通道id")
    private Integer serverTunnelId;

    @ApiModelProperty("通道名称")
    private String serverTunnelName;

    @ApiModelProperty("设备信息")
    private String deviceInfo;


    @ApiModelProperty("宽带mbps")
    private Integer bandwidth;

    @ApiModelProperty("并发连接数 每秒")
    private Integer concurrentNum;


}
