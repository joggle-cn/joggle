package com.wuweibi.bullet.device.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
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
@TableName("t_device")
public class Device extends Model<Device> {

    private static final long serialVersionUID = 1L;

	@TableId(value="id", type= IdType.AUTO)
	private Long id;
	private String name;
	@TableField(value = "deviceId")
	private String deviceNo;


    @TableField(value = "createTime")
	private Date createTime;
    @TableField(value = "userId")
	private Long userId;

	@ApiModelProperty("设备秘钥")
	@TableField("device_secret")
	private String deviceSecret;



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
