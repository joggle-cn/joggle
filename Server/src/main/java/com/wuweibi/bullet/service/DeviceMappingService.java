package com.wuweibi.bullet.service;

import com.wuweibi.bullet.domain.dto.DeviceMappingDto;
import com.wuweibi.bullet.entity.DeviceMapping;
import com.baomidou.mybatisplus.service.IService;

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
     * @param id
     * @return
     */
    boolean exists(Long userId, Long id);


    /**
     * 根据域名前缀获取映射信息
     * @param host host
     * @return
     */
    DeviceMappingDto getMapping(String host);
}
