package com.wuweibi.bullet.service;

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

    List<SysMenu> getSecondaryLevel(Long userId, Long pid);
}
