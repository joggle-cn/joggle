package com.wuweibi.bullet.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wuweibi.bullet.system.domain.UserCertificationParam;
import com.wuweibi.bullet.system.domain.UserCertificationVO;
import com.wuweibi.bullet.system.domain.dto.UserCertAdminParam;
import com.wuweibi.bullet.system.domain.vo.UserCertificationAdminListVO;
import com.wuweibi.bullet.system.entity.UserCertification;
import com.wuweibi.bullet.system.mapper.UserCertificationMapper;
import com.wuweibi.bullet.system.service.UserCertificationService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

/**
 * 用户实名认证(UserCertification)表服务实现类
 *
 * @author marker
 * @since 2022-09-14 13:54:43
 */
@Service
public class UserCertificationServiceImpl extends ServiceImpl<UserCertificationMapper, UserCertification> implements UserCertificationService {


    @Override
    public Page<UserCertificationVO> getPage(Page pageInfo, UserCertificationParam params) {

        LambdaQueryWrapper<UserCertification> qw = Wrappers.lambdaQuery();
        Page<UserCertification> page = this.baseMapper.selectPage(pageInfo, qw);

        Page<UserCertificationVO> pageResult = new Page<>(pageInfo.getCurrent(), pageInfo.getSize(), pageInfo.getTotal());
        pageResult.setRecords(page.getRecords().stream().map(entity->{
            UserCertificationVO vo = new UserCertificationVO();
            BeanUtils.copyProperties(entity, vo);
            return vo;
        }).collect(Collectors.toList()));
        return pageResult;
    }

    @Override
    public int checkCertRepeatOk(Long userId) {
        LambdaQueryWrapper<UserCertification> lqw = Wrappers.<UserCertification>lambdaQuery()
                .eq(UserCertification::getUserId, userId)
                .orderByDesc(UserCertification::getId)
                .last("limit 1");
        UserCertification userCertification =  this.baseMapper.selectOne(lqw);
        if(userCertification == null){
            return 2; // 可提交
        }
        return userCertification.getResult();
    }

    @Override
    public boolean checkIdcardAndPhone(String phone, String idcard) {
        return this.baseMapper.checkIdcardAndPhone(phone, idcard);
    }

    @Override
    public UserCertification getLastResult(Long userId) {
        return this.baseMapper.selectLastResult(userId);
    }

    @Override
    public Page<UserCertificationAdminListVO> getAdminList(Page pageInfo, UserCertAdminParam params) {
        return this.baseMapper.selectAdminList(pageInfo, params);
    }
}
