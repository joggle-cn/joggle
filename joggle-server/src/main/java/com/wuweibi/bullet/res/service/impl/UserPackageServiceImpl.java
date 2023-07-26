package com.wuweibi.bullet.res.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wuweibi.bullet.res.domain.UserPackageParam;
import com.wuweibi.bullet.res.domain.UserPackageVO;
import com.wuweibi.bullet.res.entity.UserPackage;
import com.wuweibi.bullet.res.mapper.UserPackageMapper;
import com.wuweibi.bullet.res.service.UserPackageService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

/**
 * 用户套餐(UserPackage)表服务实现类
 *
 * @author marker
 * @since 2022-10-31 09:15:53
 */
@Service
public class UserPackageServiceImpl extends ServiceImpl<UserPackageMapper, UserPackage> implements UserPackageService {


    @Override
    public Page<UserPackageVO> getPage(Page pageInfo, UserPackageParam params) {

        LambdaQueryWrapper<UserPackage> qw = Wrappers.lambdaQuery();
        Page<UserPackage> page = this.baseMapper.selectPage(pageInfo, qw);

        Page<UserPackageVO> pageResult = new Page<>(pageInfo.getCurrent(), pageInfo.getSize(), pageInfo.getTotal());
        pageResult.setRecords(page.getRecords().stream().map(entity->{
            UserPackageVO vo = new UserPackageVO();
            BeanUtils.copyProperties(entity, vo);
            return vo;
        }).collect(Collectors.toList()));
        return pageResult;
    }

    @Override
    public UserPackage getByUserId(Long userId) {
        return this.baseMapper.selectOne(Wrappers.<UserPackage>lambdaQuery()
                .eq(UserPackage::getUserId, userId));
    }

    @Override
    public boolean updateToLevel0ByUserId(Long userId, UserPackage userPackage) {
        return this.baseMapper.updateToLevel0ByUserId(userId);
    }

    @Override
    public boolean checkPackageId(Integer packageId) {
        return this.baseMapper.selectCount(Wrappers.<UserPackage>lambdaQuery()
                .eq(UserPackage::getResourcePackageId, packageId)) > 0;
    }

    @Override
    public boolean updateFLow(Long userId, long bytes) {
        boolean status = false;
        synchronized(userId){
            status = this.baseMapper.updateFlow(userId, bytes);
        }
        return status;
    }

    @Override
    public boolean updateRestFLow(Long userId, long flow) {
        return this.baseMapper.updateRestFLow(userId, flow);
    }
}
