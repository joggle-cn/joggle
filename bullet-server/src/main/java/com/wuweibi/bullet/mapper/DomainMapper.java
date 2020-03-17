package com.wuweibi.bullet.mapper;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wuweibi.bullet.entity.Domain;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.Date;
import java.util.List;

/**
 * <p>
  *  Mapper 接口
 * </p>
 *
 * @author marker
 * @since 2017-12-09
 */
public interface DomainMapper extends BaseMapper<Domain> {


    /**
     * 根据用户ID查询归属域名
     * @param userId
     * @return
     */
    List<JSONObject> selectByUserId(Long userId);

    /**
     * 检查域名是否和用户绑定
     * @param userId 用户ID
     * @param domainId 域名ID
     * @return
     */
    @Select("select count(1) from t_domain where id = #{domainId} and user_id = #{userId}")
    boolean existDomainUserId(@Param("userId") Long userId, @Param("domainId") Long domainId);


    /**
     * 获取未绑定的域名列表
     * @param userId 用户ID
     * @return
     */
    @Select("select id,domain, type from t_domain where id not in (select DISTINCT IFNULL(domain_id,0) from t_device_mapping where userId = #{userId})")
    List<JSONObject> selectListNotBindByUserId(Long userId);

    /**
     * 更新域名有效期
     * @param domainId
     * @param dueTime
     */
    @Update("update t_domain set due_time = #{dueTime} where id = #{domainId}")
    void updateDueTime(@Param("domainId") Long domainId, @Param("dueTime") Date dueTime);
}