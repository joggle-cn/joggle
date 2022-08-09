package com.wuweibi.bullet.device.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wuweibi.bullet.device.contrast.DevicePeerStatusEnum;
import com.wuweibi.bullet.device.domain.DevicePeersDTO;
import com.wuweibi.bullet.device.domain.DevicePeersParam;
import com.wuweibi.bullet.device.domain.DevicePeersVO;
import com.wuweibi.bullet.device.entity.DevicePeers;
import com.wuweibi.bullet.device.mapper.DevicePeersMapper;
import com.wuweibi.bullet.device.service.DevicePeersService;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.stream.Collectors;

/**
 * (DevicePeers)表服务实现类
 *
 * @author marker
 * @since 2022-08-09 10:49:46
 */
@Service
public class DevicePeersServiceImpl extends ServiceImpl<DevicePeersMapper, DevicePeers> implements DevicePeersService {


    @Override
    public Page<DevicePeersVO> getPage(Page pageInfo, DevicePeersParam params) {

        Page<DevicePeers> page = this.baseMapper.selectListPage(pageInfo, params);

        Page<DevicePeersVO> pageResult = new Page<>(pageInfo.getCurrent(), pageInfo.getSize(), pageInfo.getTotal());
        pageResult.setRecords(page.getRecords().stream().map(entity->{
            DevicePeersVO vo = new DevicePeersVO();
            BeanUtils.copyProperties(entity, vo);
            vo.setStatusName(DevicePeerStatusEnum.toName(vo.getStatus()));
            return vo;
        }).collect(Collectors.toList()));
        return pageResult;
    }

    @Override
    @Transactional
    public Long savePeers(Long userId, DevicePeersDTO dto) {
        DevicePeers entity = new DevicePeers();
        BeanUtils.copyProperties(dto, entity);
        entity.setUserId(userId);
        entity.setCreateTime(new Date());
        entity.setUpdateTime(entity.getCreateTime());
        entity.setStatus(dto.getStatus());

        this.baseMapper.insert(entity);


        String appName = DigestUtils.md5Hex(String.valueOf(entity.getId()));
        entity.setAppName(appName);
        this.baseMapper.updateById(entity);
        return entity.getId();
    }
}
