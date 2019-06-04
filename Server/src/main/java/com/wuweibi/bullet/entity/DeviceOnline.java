package com.wuweibi.bullet.entity;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;

import java.io.Serializable;
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

    /**
     *
     */
	private Integer status;
	/**
	 * 内网IP
	 */
    @TableField(value="intranetIp" )
	private String intranetIp;

    public DeviceOnline() {

    }

    public DeviceOnline(String deviceNo) {
        this.deviceNo = deviceNo;
    }


    @Override
	protected Serializable pkVal() {
		return this.id;
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
