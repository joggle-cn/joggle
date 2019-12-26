package com.wuweibi.bullet.mapper;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.wuweibi.bullet.entity.Device;
import com.wuweibi.bullet.entity.Domain;

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
public interface DomainMapper extends BaseMapper<Domain> {


    /**
     * 根据用户ID查询归属域名
     * @param userId
     * @return
     */
    List<JSONObject> selectByUserId(Long userId);
}