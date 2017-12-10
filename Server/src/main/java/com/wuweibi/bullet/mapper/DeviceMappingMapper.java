package com.wuweibi.bullet.mapper;

import com.wuweibi.bullet.entity.DeviceMapping;
import com.baomidou.mybatisplus.mapper.BaseMapper;

import java.util.Map;

/**
 * <p>
  *  Mapper 接口
 * </p>
 *
 * @author marker
 * @since 2017-12-09
 */
public interface DeviceMappingMapper extends BaseMapper<DeviceMapping> {


    /**
     * 判断域名是否存在
     * @param domain
     * @return
     */
    boolean existsDomain(Map<String, Object> domain);


    /**
     *
     * @param build
     * @return
     */
    boolean exists(Map<String, Object> build);
}