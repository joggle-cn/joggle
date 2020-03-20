package com.wuweibi.bullet.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.IService;
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
     * @param userId 用户ID
     * @return
     */
    List<JSONObject> getListByUserId(Long userId);

    /**
     * 检查域名是否和用户绑定
     * @param userId 用户ID
     * @param domainId 域名ID
     * @return
     */
    boolean checkDomain(Long userId, Long domainId);


    /**
     * 获取未绑定的域名列表
     * @param userId 用户ID
     * @return
     */
    List<JSONObject> getListNotBindByUserId(Long userId);


    /**
     * 更新域名有效期
     * @param domainId
     * @param dueTime
     */
    void updateDueTime(Long domainId, Long dueTime);

    /**
     * 检查所有域名的状态
     */
    void checkStatus();

}
