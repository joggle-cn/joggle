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
	@TableField(value = "deviceId")
	private String deviceId;
	@TableField(value = "updateTime")
	private Date updateTime;

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

    public DeviceOnline(String deviceCode) {
        this.deviceId = deviceCode;
    }


    @Override
	protected Serializable pkVal() {
		return this.id;
	}

	@Override
	public String toString() {
		return "DeviceOnline{" +
			", id=" + id +
			", deviceId=" + deviceId +
			", updateTime=" + updateTime +
			", status=" + status +
			"}";
	}
}
