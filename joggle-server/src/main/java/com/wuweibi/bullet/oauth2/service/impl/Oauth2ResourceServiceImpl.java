package com.wuweibi.bullet.oauth2.service.impl;


import com.wuweibi.bullet.oauth2.dao.Oauth2ResourceMapper;
import com.wuweibi.bullet.oauth2.domain.Resource;
import com.wuweibi.bullet.oauth2.service.Oauth2ResourceService;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Set;


/**
 * 资源服务
 *
 * @author marker
 *
 */
@Service
public class Oauth2ResourceServiceImpl implements Oauth2ResourceService {
    @Autowired
    private Oauth2ResourceMapper resourceMapper;

    @Override
    public Set<Resource> findAll() {
        return resourceMapper.findAll();
    }

    @Override
    public Set<Resource> queryByRoleCodes(String[] roleCodes) {
        if (ArrayUtils.isNotEmpty(roleCodes)) {
            return resourceMapper.queryByRoleCodes(roleCodes);
        }
        return Collections.emptySet();
    }

}
