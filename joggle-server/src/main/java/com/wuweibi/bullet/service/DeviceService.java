package com.wuweibi.bullet.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wuweibi.bullet.device.domain.DeviceDetail;
import com.wuweibi.bullet.device.domain.dto.DeviceAdminParam;
import com.wuweibi.bullet.device.domain.vo.DeviceDetailVO;
import com.wuweibi.bullet.device.domain.vo.DeviceListVO;
import com.wuweibi.bullet.device.domain.vo.DeviceOption;
import com.wuweibi.bullet.device.entity.Device;
import com.wuweibi.bullet.domain.dto.DeviceDto;
import com.wuweibi.bullet.entity.DeviceOnline;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author marker
 * @since 2017-12-09
 */
public interface DeviceService extends IService<Device> {


    /**
     * 更新设备名称
     * @param id Id
     * @param name 名称
     */
    void updateName(Long id, String name);

    /**
     * 判断用户是否存在设备
     * @param userId 用户Id
     * @param deviceId 设备ID
     * @return
     */
    boolean exists(Long userId, Long deviceId);


    /**
     * 判断设备是否存在
     * @param deviceId
     * @return
     */
    boolean existsNoBindDevice(String deviceId);

    void wakeUp(Long userId, String mac);

    /**
     * 根据设备编号获取设备信息
     * @param deviceNo 设备编号
     * @return
     */
    Device getByDeviceNo(String deviceNo);

    /**
     * 设备发现（同样出口IP的设备)
     * @param ip
     * @return
     */
    List<DeviceOnline> getDiscoveryDevice(String ip);


    /**
     * 获取设备详情
     * @param deviceId 设备id
     * @return
     */
    DeviceDetailVO getDeviceInfoById(Long deviceId);

    /**
     * 获取用户绑定的设备数量
     * @param userId 用户id
     * @return 绑定设备的数量
     */
    Long getCountByUserId(Long userId);

    /**
     * 获取设备下拉列表
     * @param userId 用户id
     * @return
     */
    List<DeviceOption> getOptionListByUserId(Long userId);

    /**
     * 设备id是否存在判断
     * @param deviceId 设备id
     * @return
     */
    boolean existsDeviceId(Long deviceId);


    /**
     * 用户的设备id是否存在判断
     * @param deviceId 设备id
     * @return
     */
    boolean existsUserDeviceId(Long userId, Long deviceId);

    /**
     * 根据设备id 删除设备的用户id
     * @param deviceId 设备id
     * @return
     */
    boolean removeUserId(Long deviceId);

    /**
     * 根据设备编号 删除设备的用户id
     * @param deviceNo 设备编号
     * @return
     */
    Boolean removeUserIdByDeviceNo(String deviceNo);

    List<DeviceDto> getWebListByUserId(Long userId);

    /**
     * 设备详情
     * @param deviceId 设备Id
     * @return
     */
    DeviceDetail getDetail(Long deviceId);


    DeviceDetail getDetailByDeviceNo(String deviceNo);


    /**
     * 设备分页查询
     * @param pageInfo
     * @param params
     * @return
     */
    Page<DeviceListVO> getAdminList(Page pageInfo, DeviceAdminParam params);


    /**
     * 用户绑定设备
     * @param userId 用户id
     * @param deviceNo 设备编号
     * @return
     */
    Device bindDevice(Long userId, String deviceNo);
}
