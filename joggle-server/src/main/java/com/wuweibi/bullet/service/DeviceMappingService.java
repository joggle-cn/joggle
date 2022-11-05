package com.wuweibi.bullet.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wuweibi.bullet.device.domain.dto.DeviceMappingProtocol;
import com.wuweibi.bullet.device.domain.vo.MappingDeviceVO;
import com.wuweibi.bullet.domain.DeviceMappingDTO;
import com.wuweibi.bullet.domain.dto.DeviceMappingDto;
import com.wuweibi.bullet.entity.DeviceMapping;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author marker
 * @since 2017-12-09
 */
public interface DeviceMappingService extends IService<DeviceMapping> {


    /**
     * 判断域名是否被使用
     * @param domain 域名
     * @return
     */
    boolean existsDomain(String domain);


    /**
     * 判断用户是否拥有映射数据
     * @param userId
     * @param mappingId
     * @return
     */
    boolean exists(Long userId, Long mappingId);


    /**
     * 根据域名前缀获取映射信息
     * @param host host
     * @return
     */
    DeviceMappingDto getMapping(String host);


    /**
     * 根据设备Id 删除映射数据
     * @param deviceId
     */
    void deleteByDeviceId(Long deviceId);


    /**
     * 根据设备ID获取设备编号
     * @param deviceId
     * @return
     */
    String getDeviceNo(Long deviceId);


    /**
     * 获取所有的映射数据
     * @return
     */
    List<DeviceMapping> getAll();


    /**
     * 根据设备编号获取
     * @param deviceNo 设备编号
     * @return
     */
    List<DeviceMapping> getDeviceAll(String deviceNo);

    /**
     * 判断域名是否被绑定
     * @param deviceId 设备ID
     * @param domainId 域名ID
     * @return
     */
    boolean existsDomainId(Long deviceId, Long domainId);

    /**
     * 域名id是否被使用
     * @param domainId 域名id
     * @return
     */
    boolean existsDomainId( Long domainId);

    /**
     *
     * @param mappingId 映射id
     * @return
     */
    String getDeviceNoByMappingId(Long mappingId);

    /**
     * 判断是否有开启的映射
     * @param userId 用户Id
     * @return
     */
    boolean hasOkMapping(Long userId);

    List<DeviceMappingDTO> getAllByUserId(Long userId);

    /**
     * 批量下线映射 仅更变设备映射状态
     * @param userId 用户Id
     * @return
     */
    boolean updateDownByUserId(Long userId);

    /**
     * 统计设备下的映射数量
     * @param deviceId 设备id
     * @return
     */
    Long countByDeviceId(Long deviceId);

    /**
     * 根据设备id查询映射集合
     * @param deviceId 设备ID
     * @return
     */
    List<MappingDeviceVO> getByDeviceId(Long deviceId);

    DeviceMapping getByDomainId(Long deviceId, Long domainId);

    List<DeviceMappingProtocol> getMapping4ProtocolByDeviceNo(String deviceNo);

    DeviceMappingProtocol getMapping4ProtocolByMappingId(Long mappingId);
}
