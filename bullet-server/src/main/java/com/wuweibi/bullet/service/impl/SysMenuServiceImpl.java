package com.wuweibi.bullet.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wuweibi.bullet.entity.SysMenu;
import com.wuweibi.bullet.mapper.SysMenuMapper;
import com.wuweibi.bullet.service.ISysMenuService;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * <p>
 * 菜单表 服务实现类
 * </p>
 *
 * @author marker
 * @since 2020-03-16
 */
@Service
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenu> implements ISysMenuService {

    /**
     * 排序规则
     */
    private static Comparator comparator;

    /**
     * 静态代码块
     */
    static {
        comparator = (Comparator<SysMenu>) (o1, o2) -> {
            if(o1.getSort() == null){
                o1.setSort(0);
            }
            if(o2.getSort() == null){
                o2.setSort(0);
            }
            return o1.getSort().compareTo(o2.getSort());
        };
    }


    @Override
    public List<SysMenu> getOneLevel(Long userId) {
        List<SysMenu> list = this.baseMapper.getOneLevelByUserId(userId);
        Collections.sort(list, comparator);
        return list;
    }

    @Override
    public List<SysMenu> getSecondaryLevel(Long userId, Long pid) {
        List<SysMenu> list = this.baseMapper.selectByUserId(userId, pid);

        // 排序
        Collections.sort(list, comparator);

        // 构造树（从二级开始）
        List<SysMenu> treeList = new LinkedList<>();
        forEachInvoke(treeList, list, pid, 2);

        return treeList;
    }


    /**
     * 递归查询数据
     *
     * @param treeList 结果集
     * @param list 所有菜单
     * @param id 父级ID
     * @param level 级别
     */
    private void forEachInvoke(List<SysMenu> treeList, List<SysMenu> list, Long id, Integer level) {
        Iterator<SysMenu> it = list.iterator();
        while (it.hasNext()) {
            SysMenu module = it.next();
            if ((level.compareTo(module.getLevel()) == 0) && (id.compareTo(module.getParentId()) == 0)) {
                treeList.add(module);
                forEachInvoke(treeList, list, module.getId(), level + 1);
            }
        }
    }
}
