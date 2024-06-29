package com.wuweibi.bullet.res.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wuweibi.bullet.res.domain.UserPackageRightsDTO;
import com.wuweibi.bullet.res.domain.UserPackageRightsParam;
import com.wuweibi.bullet.res.domain.UserPackageRightsVO;
import com.wuweibi.bullet.res.entity.UserPackageRights;
import com.wuweibi.bullet.res.mapper.UserPackageRightsMapper;
import com.wuweibi.bullet.res.service.UserPackageRightsService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.stream.Collectors;

/**
 * 用户套餐权益(UserPackageRights)表服务实现类
 *
 * @author marker
 * @since 2022-11-01 12:41:56
 */
@Service
public class UserPackageRightsServiceImpl extends ServiceImpl<UserPackageRightsMapper, UserPackageRights> implements UserPackageRightsService {


    @Override
    public Page<UserPackageRightsVO> getPage(Page pageInfo, UserPackageRightsParam params) {

        LambdaQueryWrapper<UserPackageRights> qw = Wrappers.lambdaQuery();
        Page<UserPackageRights> page = this.baseMapper.selectPage(pageInfo, qw);

        Page<UserPackageRightsVO> pageResult = new Page<>(pageInfo.getCurrent(), pageInfo.getSize(), pageInfo.getTotal());
        pageResult.setRecords(page.getRecords().stream().map(entity->{
            UserPackageRightsVO vo = new UserPackageRightsVO();
            BeanUtils.copyProperties(entity, vo);
            return vo;
        }).collect(Collectors.toList()));
        return pageResult;
    }

    @Override
    public void addPackageRights(UserPackageRightsDTO dto) {
        UserPackageRights entity = new UserPackageRights();
        BeanUtils.copyProperties(dto, entity);
        entity.setCreateTime(new Date());
        entity.setUpdateTime(new Date());
        this.baseMapper.insert(entity);
    }

    @Override
    public boolean checkResource(Integer resourceType, Long resourceId) {
        return this.baseMapper.checkByResourceTypeAndId(resourceType, resourceId);
    }

    @Override
    public boolean removeResourceByUserId(Long userId) {
        return this.baseMapper.delete(Wrappers.<UserPackageRights>lambdaQuery()
                .eq(UserPackageRights::getUserId, userId)) > 0;
    }
}
