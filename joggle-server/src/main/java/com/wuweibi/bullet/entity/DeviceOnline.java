package com.wuweibi.bullet.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;

import java.util.Date;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * <p>
 *  在线设备表
 * </p>
 *
 * @author marker
 * @since 2017-12-09
 */
@Data
@TableName("t_device_online")
public class DeviceOnline extends Model<DeviceOnline> {

    private static final long serialVersionUID = 1L;

	@TableId(value="id", type= IdType.AUTO)
	private Long id;

    /**
     * 设备ID
     */
	@TableField(value = "deviceNo")
	private String deviceNo;

    /**
     * 更新时间
     */
	@TableField(value = "updateTime")
	private Date updateTime;

    /**
     * mac地址
     */
	@TableField(value = "mac_addr")
	private String macAddr;

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


	/**
	 * 默认构造
	 */
	public DeviceOnline() {  }


	/**
	 * 构造
	 * @param deviceNo
	 */
    public DeviceOnline(String deviceNo) {
        this.deviceNo = deviceNo;
    }


	@Override
	public String toString() {
		return "DeviceOnline{" +
			", id=" + id +
			", deviceNo=" + deviceNo +
			", updateTime=" + updateTime +
			", status=" + status +
			"}";
	}
}
