package com.wuweibi.bullet.device.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;

import java.util.Date;

/**
 * 通道(ServerTunnel)表实体类
 *
 * @author makejava
 * @since 2022-04-28 21:27:32
 */
@SuppressWarnings("serial")
@Data
@TableName("t_server_tunnel")
public class ServerTunnel extends Model<ServerTunnel> {

    @TableId
    private Integer id;
    //通道名称
    private String name;
    //宽带 MB
    private Integer broadband;
    //线路通道地址
    private String serverAddr;
    //上线时间
    private Date createTime;

}

