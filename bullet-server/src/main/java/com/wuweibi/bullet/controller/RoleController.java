package com.wuweibi.bullet.controller;
/**
 * Created by marker on 2018/3/6.
 */

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wuweibi.bullet.alias.Var;
import com.wuweibi.bullet.core.builder.MapBuilder;
import com.wuweibi.bullet.domain.domain.LayPage;
import com.wuweibi.bullet.entity.Role;
import com.wuweibi.bullet.entity.api.Result;
import com.wuweibi.bullet.service.RoleService;
import com.wuweibi.bullet.utils.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * 角色管理
 *
 * @author marker
 *         create 2018-03-06 上午11:39
 **/

@Slf4j
@RestController
@RequestMapping("/api/role")
public class RoleController {


    @InitBinder
    protected void initBinder(HttpServletRequest request,
                              ServletRequestDataBinder binder) throws Exception {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        CustomDateEditor dateEditor = new CustomDateEditor(format, true);
        binder.registerCustomEditor(Date.class, dateEditor);
    }


    /**
     * RoleService
     */
    @Autowired
    private RoleService roleService;




//    /**
//     * 获取角色权限
//     */
//    @GetMapping("/{id}/permissions")
//    public Object getPermissions(@PathVariable("id") int id) {
//        return MessageResult.success(roleService.selectRolePermissions(id));
//    }
//
//
//    /**
//     * 获取
//     *
//     * @param id id
//     * @return
//     */
//    @GetMapping("/{id}")
//    public Object get(@PathVariable("id") int id) {
//        return MessageResult.success(roleService.selectById(id));
//    }
//
//
//
//    /**
//     * 添加
//     */
//    @PostMapping(value = "/{id}")
//    public Object save(HttpSession session
//            ,Role entity, @RequestParam("permissions") String permissions) {
//        entity.setCreateTime(new Date());
//        return saveOrUpdate(session, entity, permissions);
//    }
//
//    /**
//     * 更新
//     */
//    @PutMapping(value = "/{id}")
//    public Object update(HttpSession session
//            ,Role entity, @RequestParam("permissions") String permissions) {
//        return saveOrUpdate(session, entity, permissions);
//    }
//
//    /**
//     * 保存或者更新
//     */
//    private Object saveOrUpdate(HttpSession session
//              ,Role role, @RequestParam("permissions") String permissions) {
//        // 获取用户的公司ID
//        Long userId       = SessionHelper.getUserId(session);
//
//        role.setCompanyId(companyId);
//        role.setOptUserId(userId);// 操作人
//
//        role.setUpdateTime(new Date());
//        if (role.getId() == 0) { // 新建
//            role.setCreateTime(new Date());
//            roleService.saveEntity(role);
//        } else {
//            roleService.updateEntity(role);
//        }
//        roleService.updatePermission(role.getId(), permissions);
//        return MessageResult.success();
//    }


    /**
     * 删除
     *
     * @param ids
     * @return
     */
//    @DeleteMapping("/{ids}")
//    public Object delete(@PathVariable("ids") String ids) {
//
//        List<Integer> idList = StringUtil.splitInt(ids, ",");
//
//        if (idList.size() == 0) {
//            return MessageResult.warpper(CodeStatus.ERROR_DELETE_ADMIN_ROLE);
//        }
//
//        try{
//            roleService.batchDelete(idList);
//        } catch (RoleExistUserException e){
//            return MessageResult.warpper(CodeStatus.ROLE_HAS_USER_NOT_DELETE);
//        } catch (ServiceException e){
//            return MessageResult.warpper(CodeStatus.ROLE_HAS_USER_NOT_DELETE);
//        }
//        return MessageResult.success();
//    }


    /**
     * 角色列表
     */
    @RequestMapping("/list")
    public LayPage list(
            HttpSession session,
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "page", defaultValue = Var.PAGE_FIRST) int page,
            @RequestParam(value = "limit", defaultValue = Var.PAGE_SIZE) int limit) {

        if (StringUtil.isBlank(keyword)) {// 数据校准
            keyword = "";
        }

        String keywordNew = StringUtil.decodeSpecialCharsWhenLikeUseBackslash(keyword);

        Page<Role> pageInfo = new Page<>(page, limit);
        log.info("load role list data !");
        pageInfo = roleService.selectCustomPage(pageInfo, MapBuilder.newMap(2)
                .setParam("keyword", '%' + keywordNew + '%')
                .build());

        return Result.layPage(pageInfo);
    }


}
