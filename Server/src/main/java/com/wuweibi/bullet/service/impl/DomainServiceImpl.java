package com.wuweibi.bullet.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.wuweibi.bullet.entity.Domain;
import com.wuweibi.bullet.mapper.DomainMapper;
import com.wuweibi.bullet.service.DomainService;
import org.springframework.stereotype.Service;

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
}
