package com.wuweibi.bullet.device.domain.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.util.Date;
import io.swagger.v3.oas.annotations.media.Schema;

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

	@Schema(description = "设备秘钥")
	@TableField("device_secret")
	private String deviceSecret;


	@Schema(description = "在线状态 -1离线  1在线")
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

	@Schema(description = "客户端版本")
	private String clientVersion;

	@Schema(description = "通道id")
	private Integer serverTunnelId;

	@Schema(description = "操作系统")
	private String os;

	@Schema(description = "CPU架构")
	private String arch;

	@Schema(description = "mac地址")
	private String macAddr;


	private String lineName;
	private Integer broadband; 

	@Schema(description = "设备并发连接数")
	private Integer concurrentNum;

	@Schema(description = "延迟(毫秒)")
	private Long latencyMs;

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
