package com.wuweibi.bullet.controller;


import com.alibaba.fastjson.JSONObject;
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
    public Object listSecondaryLevel(@JwtUser Session session, @RequestParam Integer pid) {
        Long userId = session.getUserId();
        List<SysMenu> list = sysMenuService.getSecondaryLevel(userId, pid);
        return Result.success(list);
    }




    /**
     * 寻找url的菜单信息
     * @param url 地址
     * @return
     */
    @GetMapping("/lookup")
    public Object lookup(@RequestParam("url") String url) {



        SysMenu module = sysMenuService.selectByUrl(url);
        if(module == null){
            module = new SysMenu();
            module.setId(0);
            module.setParentId(0);
        }

        int pid = module.getParentId();
        int id = module.getId();


        JSONObject data = new JSONObject();
        data.put("id", module.getId());
        data.put("pid", pid);
        data.put("level", module.getLevel());


        // 刷新菜单级别
        List<SysMenu> allList = sysMenuService.getAll();

        // 获取一级菜单
        if(pid == 0){
            data.put("oneid", id);
        }else{

            SysMenu onelevel = getOneLevel(allList, pid);
            if(onelevel != null){
                data.put("onelevel", onelevel.getLevel());
                data.put("onepid", onelevel.getParentId());
                data.put("oneid", onelevel.getId());
            }else {
                data.put("oneid", id);
            }

        }



        return Result.success(data);
    }



    /**
     * 获取一级菜单（递归）
     * @param allList 全部数据
     * @param pid 父级ID
     * @return
     */
    private SysMenu getOneLevel(List<SysMenu> allList, int pid){
        for(SysMenu m : allList){
            if(m.getId() == pid){
                if(m.getLevel() == 1){
                    return m;
                } else{
                    return getOneLevel(allList, m.getParentId());
                }
            }
        }
        return null;
    }


}
