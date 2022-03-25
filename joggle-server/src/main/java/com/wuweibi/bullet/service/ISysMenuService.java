package com.wuweibi.bullet.service;

import com.wuweibi.bullet.entity.Button;
import com.wuweibi.bullet.entity.SysMenu;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 菜单表 服务类
 * </p>
 *
 * @author marker
 * @since 2020-03-16
 */
public interface ISysMenuService extends IService<SysMenu> {



    List<SysMenu> getOneLevel(Long userId);

    List<SysMenu> getSecondaryLevel(Long userId, Integer pid);
    /**
     * 获取所有
     */
    List<SysMenu> getAll();


    /**
     * 根据URL查询菜单
     * @param url 地址
     * @return
     */
    SysMenu selectByUrl(String url);

    /**
     * 获取所有菜单
     * @return
     */
    List<SysMenu> getAllToTree();

    List<SysMenu> getTwoLevelToTree();

    void save(SysMenu entity, List<Button> buttons);
}
