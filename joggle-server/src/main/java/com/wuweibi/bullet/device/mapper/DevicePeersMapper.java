package com.wuweibi.bullet.device.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wuweibi.bullet.device.domain.DevicePeersConfigDTO;
import com.wuweibi.bullet.device.domain.DevicePeersParam;
import com.wuweibi.bullet.device.domain.DevicePeersVO;
import com.wuweibi.bullet.device.entity.DevicePeers;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * (DevicePeers)表数据库访问层
 *
 * @author marker
 * @since 2022-08-09 10:49:46
 */
public interface DevicePeersMapper extends BaseMapper<DevicePeers> {

    @Insert("insert into device_peers (" +
            "id, user_id, app_name, name, server_device_id, client_device_id, server_local_port, server_mtu, " +
            "client_proxy_port, server_local_host, client_proxy_host, client_mtu, remark, status, create_time, update_time, " +
            "config_compress, config_encryption, config_interval, strategy, bandwidth" +
            ") values (" +
            "#{id}, #{userId}, #{appName}, #{name}, #{serverDeviceId}, #{clientDeviceId}, #{serverLocalPort}, #{serverMtu}, " +
            "#{clientProxyPort}, #{serverLocalHost}, #{clientProxyHost}, #{clientMtu}, #{remark}, #{status}, #{createTime}, #{updateTime}, " +
            "#{configCompress}, #{configEncryption}, #{configInterval}, #{strategy}, #{bandwidth}" +
            ")")
    int insertWithId(DevicePeers entity);

    Page<DevicePeersVO> selectListPage(Page pageInfo, @Param("params") DevicePeersParam params);

    DevicePeersConfigDTO selectPeersConfig( @Param("id") Long id);

    /**
     * 根据设备号查询设备p2p配置
     * @param deviceNo 设备号
     * @return
     */
    List<DevicePeersConfigDTO> selectListByDeviceNo(@Param("deviceNo") String deviceNo);

    /**
     * 根据服务端设备id查询端到端列表
     * @param deviceId 设备id
     * @return
     */
    List<DevicePeersVO> selectListByServerDeviceId(@Param("deviceId") Long deviceId);
}
