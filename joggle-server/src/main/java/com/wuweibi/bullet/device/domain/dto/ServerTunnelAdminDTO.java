package com.wuweibi.bullet.device.domain.dto;

import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class ServerTunnelAdminDTO {

    @TableId
    private Integer id;
    //通道名称
    private String name;
    private String country;
    private String area;
    //宽带 MB
    private Integer broadband;
    //线路通道地址
    private String serverAddr;

    // 价格类型 1免费 2包月 3包年
    @ApiModelProperty("价格类型 1免费 2包月 3包年")
    private Integer priceType;

    // 销售价格（元/周期）
    @ApiModelProperty("销售价格（元/周期）")
    private BigDecimal salesPrice;

    // 原价（元/周期）
    @ApiModelProperty("原价（元/周期）")
    private BigDecimal originalPrice;

    // 是否可购买 1可 0不可
    @ApiModelProperty("是否可购买 1可 0不可")
    private Integer buyStatus;


    @ApiModelProperty("服务器到期时间")
    private Date serverEndTime;

    @ApiModelProperty("通道Token")
    @NotBlank(message = "通道Token不能为空")
    private String token;




}
