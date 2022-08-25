package com.wuweibi.bullet.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wuweibi.bullet.device.domain.vo.MappingDeviceVO;
import com.wuweibi.bullet.domain.DeviceMappingDTO;
import com.wuweibi.bullet.entity.DeviceMapping;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;
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



    String selectDeviceNo(Long  deviceId);


    /**
     * 根据设备编号获取
     * @param deviceNo 设备编号
     * @return
     */
    List<DeviceMapping> selectListByDeviceNo(String deviceNo);

    /**
     * 判断域名是否被绑定
     * @param deviceId 设备ID
     * @param domainId 域名ID
     * @return
     */
    @Select("select count(1) from t_device_mapping where domain_id =#{domainId} and device_id=#{deviceId}")
    boolean existsDomainId(@Param("deviceId") Long deviceId, @Param("domainId") Long domainId);


    /**
     * 更新Mapping状态
     * @param mappingId 映射ID
     * @param status 状态
     */
    @Update("update t_device_mapping set status =#{status} where id = #{id}")
    void updateStatusById(@Param("id") Long mappingId, @Param("status") int status);


    @Select("select deviceId from t_device where id = (select device_id from t_device_mapping where id=#{mappingId})")
    String selectDeviceNoById(@Param("mappingId") Long mappingId);


    @Select("select \n" +
            "a.*,b.deviceId deviceNo\n" +
            "from t_device_mapping a \n" +
            "left join t_device b on a.device_id = b.id\n" +
            "where a.userId = #{userId} and status = #{status}")
    List<DeviceMappingDTO> selectAllByUserId(@Param("userId") Long userId, @Param("status") int status);

    /**
     * 根据设备id查询映射集合
     * @param deviceId 设备ID
     * @return
     */
    List<MappingDeviceVO> selectByDeviceId(@Param("deviceId") Long deviceId);
}
