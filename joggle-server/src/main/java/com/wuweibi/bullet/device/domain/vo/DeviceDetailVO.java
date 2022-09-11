package com.wuweibi.bullet.device.domain.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * <p>
 *
 * </p>
 *
 * @author marker
 * @since 2017-12-09
 */
@Data
public class DeviceDetailVO {

    private static final long serialVersionUID = 1L;

	private Long id;
	private String name;
	private String deviceNo;
	private String serverAddr;


    @TableField(value = "createTime")
	private Date createTime;
    @TableField(value = "userId")
	private Long userId;

	@ApiModelProperty("设备秘钥")
	@TableField("device_secret")
	private String deviceSecret;


	@ApiModelProperty("在线状态 -1离线  1在线")
	private Integer status;

	/**
	 * 内网IP
	 */
	@TableField(value="intranet_ip" )
	private String intranetIp;
	/**
	 * 公网IP
	 */
	@TableField()
	private String publicIp;

	@ApiModelProperty("客户端版本")
	private String clientVersion;

	@ApiModelProperty("通道id")
	private Integer serverTunnelId;

	@ApiModelProperty("操作系统")
	private String os;

	@ApiModelProperty("CPU架构")
	private String arch;

	@ApiModelProperty("mac地址")
	private String macAddr;


	private String lineName;
	private Integer broadband;


	@Override
	public String toString() {
		return "Device{" +
			", id=" + id +
			", name=" + name +
			", deviceNo=" + deviceNo +
			", createTime=" + createTime +
			", userId=" + userId +
			"}";
	}
}
