package com.wuweibi.bullet.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wuweibi.bullet.entity.Button;
import com.wuweibi.bullet.entity.SysMenu;
import com.wuweibi.bullet.mapper.SysMenuMapper;
import com.wuweibi.bullet.service.ISysMenuService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public List<SysMenu> getSecondaryLevel(Long userId, Integer pid) {
        List<SysMenu> list = this.baseMapper.selectByUserId(userId, pid);

        // 排序
        Collections.sort(list, comparator);

        // 构造树（从二级开始）
        List<SysMenu> treeList = new LinkedList<>();
        forEachInvoke(treeList, list, pid, 2);

        return treeList;
    }

    @Override
    public List<SysMenu> getAll() {
        QueryWrapper ew = new QueryWrapper();
        ew.setEntity(new SysMenu());
        return this.baseMapper.selectList(ew);
    }

    @Override
    public SysMenu selectByUrl(String url) {
        return this.baseMapper.selectByUrl(url);
    }

    @Override
    public List<SysMenu> getAllToTree() {

        List<SysMenu> list = this.baseMapper.getEnableAll();
        // 排序
        Collections.sort(list, comparator);

        // 转换tree
        List<SysMenu> treeList = new LinkedList<>();
        forEachInvoke(treeList, list, 0, 1);

        return treeList;
    }

    @Override
    public List<SysMenu> getTwoLevelToTree() {

        List<SysMenu> list = this.baseMapper.getTwoLevelList();
        // 排序
        Collections.sort(list, comparator);

        // 转换tree
        List<SysMenu> treeList = new ArrayList<>(list.size());
        forEachInvoke(treeList, list, 0, 1);

        return treeList;
    }

    @Override
    @Transactional
    public void save(SysMenu entity, List<Button> buttons) {
        if (entity.getId() == 0) { // 新建
            entity.setCreatedTime(new Date());
        } else {// 更新
            entity.setCreatedTime(null);
            entity.setUpdatedTime(new Date());

            // 刷新菜单级别
            List<SysMenu> allList = this.getAll();
            forEachUpdate(allList, entity.getId(), entity.getLevel() + 1);
        }
        this.saveOrUpdate(entity);


        //  刷新buttons;
//        int moduleId = entity.getId();
//        buttonService.deleteByModuleId(moduleId);
//        Iterator<Button> it = buttons.iterator();
//        while (it.hasNext()) {
//            Button button = it.next();
//            button.setModuleId(moduleId);
//            buttonService.insert(button);
//        }
    }

    /**
     * 更新数据级别
     *
     * @param list
     * @param id
     * @param level
     */
    private void forEachUpdate(List<SysMenu> list, Integer id, Integer level) {
        // 查询出一级
        Iterator<SysMenu> it = list.iterator();
        while (it.hasNext()) {
            SysMenu module = it.next();
            if (id.compareTo(module.getParentId()) == 0) {
                if (!(level.compareTo(module.getLevel()) == 0)) {
                    // 更新级别
                    this.updateLevel(module.getId(), level);
                }
                forEachUpdate(list, module.getId(), level + 1);
            }
        }
    }



    /**
     * 更新级别
     */
    public void updateLevel(Integer id, Integer level) {
        SysMenu module = new SysMenu();
        module.setId(id);
        module.setLevel(level);
        module.setUpdatedTime(new Date());
        this.baseMapper.updateById(module);
    }

    /**
     * 递归查询数据
     *
     * @param treeList 结果集
     * @param list 所有菜单
     * @param id 父级ID
     * @param level 级别
     */
    private void forEachInvoke(List<SysMenu> treeList, List<SysMenu> list, Integer id, Integer level) {
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
