package com.wuweibi.bullet.controller;


import com.wuweibi.bullet.annotation.JwtUser;
import com.wuweibi.bullet.domain.domain.session.Session;
import com.wuweibi.bullet.entity.SysMenu;
import com.wuweibi.bullet.entity.api.Result;
import com.wuweibi.bullet.service.ISysMenuService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 菜单表 前端控制器
 * </p>
 *
 * @author marker
 * @since 2020-03-16
 */
@RestController
@RequestMapping("/api/menu")
public class SysMenuController {

    @Resource
    private ISysMenuService sysMenuService;


    /**
     * 获取用户的一级菜单
     * （系统主界面菜单使用该接口）
     */
    @GetMapping("/list/one")
    public Object listOneLevel(@JwtUser Session session) {
        Long userId = session.getUserId();
        List<SysMenu> list = sysMenuService.getOneLevel(userId);
        return Result.success(list);
    }


    /**
     * 获取二级以后的菜单
     *
     * （系统主界面菜单使用该接口）
     *
     * @return
     */
    @GetMapping("/list/secondary")
    public Object listSecondaryLevel(@JwtUser Session session, @RequestParam Long pid) {
        Long userId = session.getUserId();
        List<SysMenu> list = sysMenuService.getSecondaryLevel(userId, pid);
        return Result.success(list);
    }
}
