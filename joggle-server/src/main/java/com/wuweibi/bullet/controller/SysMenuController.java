package com.wuweibi.bullet.controller;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.wuweibi.bullet.annotation.JwtUser;
import com.wuweibi.bullet.domain.domain.session.Session;
import com.wuweibi.bullet.entity.Button;
import com.wuweibi.bullet.entity.SysMenu;
import com.wuweibi.bullet.entity.api.R;
import com.wuweibi.bullet.service.ISysMenuService;
import com.wuweibi.bullet.system.domain.SysMenuDTO;
import com.wuweibi.bullet.utils.StringUtil;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;
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
@RequestMapping("/admin/menu")
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
        return R.success(list);
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
        return R.success(list);
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



        return R.success(data);
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



    /**
     * 获取所有菜单
     */
    @GetMapping("/list")
    public Object list() {
        List<SysMenu> treeList = sysMenuService.getAllToTree();
        return R.success(treeList);
    }


    /**
     * 获取
     * @param id id
     * @return
     */
    @GetMapping("/{id}")
    public Object list(@PathVariable("id") int id) {
        return R.success(sysMenuService.getById(id));
    }



    /**
     * 删除
     * @param ids
     * @return
     */
    @DeleteMapping("/{ids}")
    public Object delete(@PathVariable("ids") String ids) {

        List<Integer> idList = StringUtil.splitInt(ids, ",");

        if (idList.size() == 0) {
            return R.fail( );
        }
        // 校验是否为子菜单
        int moduleId = idList.get(0);
//        boolean status = sysMenuService.existsChild(moduleId);
//        if (status) {
//            return MessageResult.warpper(CodeStatus.ERROR_DELETE_MODULE_EXISTS_CHILD);
//        }
//        sysMenuService.deleteBatchIds(idList);
//
//        // 删除按钮
//        sysMenuService.deleteByModuleId(moduleId);


        return R.success();
    }


    /**
     * 保存或者更新
     *
     * @param entity 实体
     * @return
     */
    @PutMapping("/{id}")
    public Object saveOrUpdate(SysMenuDTO entity) {
        entity.setUpdatedTime(new Date());
        if(StringUtil.isBlank(entity.getUrl())){
            entity.setUrl("");
        }
        if(StringUtil.isBlank(entity.getIcon())){
            entity.setIcon("");
        }
        List<Button> buttons = JSON.parseArray(entity.getButtons(), Button.class);
//
        sysMenuService.save(entity, buttons);

        return R.success();
    }



    /**
     * 获取二级菜单树
     *
     */
    @GetMapping("/options")
    public Object level2() {
        List<SysMenu> treeList = sysMenuService.getTwoLevelToTree();
        return R.success(treeList);
    }


//    /**
//     * 获取
//     *
//     * @param id id
//     * @return
//     */
//    @GetMapping("/{id}/buttons")
//    public Object getButtons(@PathVariable("id") int id) {
//        List<Button> list = buttonService.selectByModuleId(id);
//        return MessageResult.success(list);
//    }
//
//    @Resource
//    private ButtonService buttonService;

}
