package com.wuweibi.bullet.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wuweibi.bullet.entity.SysMenu;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 菜单表 Mapper 接口
 * </p>
 *
 * @author marker
 * @since 2020-03-16
 */
public interface SysMenuMapper extends BaseMapper<SysMenu> {

    /**
     * 查询用户的一级菜单
     * @param userId 用户ID
     * @return
     */
    List<SysMenu> getOneLevelByUserId(@Param("userId") Long userId);


    /**
     * 查询二级菜单树
     * @param userId
     * @param pid
     * @return
     */
    List<SysMenu> selectByUserId(@Param("userId") Long userId, @Param("pid")  Integer pid);

    /**
     * 根据url查询菜单
     * @param url 地址
     * @return
     */
    SysMenu selectByUrl(String url);

    List<SysMenu> getEnableAll();


    List<SysMenu> getTwoLevelList();
}
