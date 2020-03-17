package com.wuweibi.bullet.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wuweibi.bullet.entity.Domain;
import com.wuweibi.bullet.mapper.DomainMapper;
import com.wuweibi.bullet.service.DomainService;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * <p>
 *  域名与端口服务实现类
 * </p>
 *
 * @author marker
 * @since 2017-12-09
 */
@Service
public class DomainServiceImpl extends ServiceImpl<DomainMapper, Domain> implements DomainService {

    @Override
    public List<JSONObject> getListByUserId(Long userId) {
        return this.baseMapper.selectByUserId(userId);
    }

    @Override
    public boolean checkDomain(Long userId, Long domainId) {
        return this.baseMapper.existDomainUserId(userId, domainId);
    }

    @Override
    public List<JSONObject> getListNotBindByUserId(Long userId) {
        return this.baseMapper.selectListNotBindByUserId(userId);
    }

    @Override
    public void updateDueTime(Long domainId, Long dueTime) {
        this.baseMapper.updateDueTime(domainId, new Date(dueTime));
    }
}
