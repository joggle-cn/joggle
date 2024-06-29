package com.wuweibi.bullet.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wuweibi.bullet.system.entity.SysConfig;
import com.wuweibi.bullet.system.domain.SysConfigVO;
import com.wuweibi.bullet.system.domain.SysConfigParam;

/**
 * 系统配置表(SysConfig)表服务接口
 *
 * @author marker
 * @since 2024-06-23 20:35:30
 */
public interface SysConfigService extends IService<SysConfig> {


    /**
     * 分页查询数据
     * @param pageInfo 分页对象
     * @param params 参数
     * @return
     */
    Page<SysConfigVO> getPage(Page pageInfo, SysConfigParam params);


    /**
     * 根据key 获取配置
     * @param type 类型
     * @param key key
     * @return
     */
    String getConfigValue(String type, String key);



}
