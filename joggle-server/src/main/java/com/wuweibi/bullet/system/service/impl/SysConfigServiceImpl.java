package com.wuweibi.bullet.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wuweibi.bullet.system.domain.SysConfigParam;
import com.wuweibi.bullet.system.domain.SysConfigVO;
import com.wuweibi.bullet.system.entity.SysConfig;
import com.wuweibi.bullet.system.mapper.SysConfigMapper;
import com.wuweibi.bullet.system.service.SysConfigService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 系统配置表(SysConfig)表服务实现类
 *
 * @author marker
 * @since 2024-06-23 20:35:30
 */
@Service
public class SysConfigServiceImpl extends ServiceImpl<SysConfigMapper, SysConfig> implements SysConfigService {


    @Override
    public Page<SysConfigVO> getPage(Page pageInfo, SysConfigParam params) {

        LambdaQueryWrapper<SysConfig> qw = Wrappers.lambdaQuery();
        Page<SysConfig> page = this.baseMapper.selectPage(pageInfo, qw);

        Page<SysConfigVO> pageResult = new Page<>(pageInfo.getCurrent(), pageInfo.getSize(), pageInfo.getTotal());
        pageResult.setRecords(page.getRecords().stream().map(entity->{
            SysConfigVO vo = new SysConfigVO();
            BeanUtils.copyProperties(entity, vo);
            return vo;
        }).collect(Collectors.toList()));
        return pageResult;
    }


//    @Cacheable(cacheNames="systemConfig")
    @Override
    public String getConfigValue(String type, String key) {
        SysConfig sysConfig = this.baseMapper.selectOne(Wrappers.<SysConfig>lambdaQuery()
                .eq(SysConfig::getType, type)
                .eq(SysConfig::getKey, key));
        if(Objects.isNull(sysConfig)){
            throw new RuntimeException("配置不存在: " + key);
        }
        return sysConfig.getValue();
    }
}
