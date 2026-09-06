package com.wuweibi.bullet.device.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 设备任意门(DeviceDoor)表实体类
 *
 * @author marker
 * @since 2022-08-03 21:21:26
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("t_device_door")
public class DeviceDoor {

    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    @Schema(description = "id")
    private Long id;

    /**
     * 设备id
     */
    @Schema(description = "设备id")
    private Long deviceId;

    @Schema(description = "域名id")
    private Long domainId;

    /**
     * 本地服务路径
     */
    @Schema(description = "本地服务路径")
    private String localPath;

    /**
     * 服务端路径
     */
    @Schema(description = "服务端路径")
    private String serverPath;

    /**
     * 启用状态 1启用 0停用
     */
    @Schema(description = "启用状态 1启用 0停用 [参考 DeviceDoorEnum]")
    private Integer enable;

    /**
     * 创建时间
     */
    @Schema(description = "创建时间")
    private Date createTime;

    /**
     * 更新时间
     */
    @Schema(description = "更新时间")
    private Date updateTime;

}
