package com.wuweibi.bullet.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.service.IService;
import com.wuweibi.bullet.entity.Device;
import com.wuweibi.bullet.entity.Domain;

import java.util.List;

/**
 * <p>
 *  域名与端口服务类
 * </p>
 *
 * @author marker
 * @since 2017-12-09
 */
public interface DomainService extends IService<Domain> {


    /**
     * 根据用户ID查询归属域名
     * @param userId
     * @return
     */
    List<JSONObject> getListByUserId(Long userId);
}
