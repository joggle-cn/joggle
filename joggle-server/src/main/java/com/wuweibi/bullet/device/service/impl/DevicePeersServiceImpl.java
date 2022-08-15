package com.wuweibi.bullet.device.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wuweibi.bullet.conn.CoonPool;
import com.wuweibi.bullet.device.contrast.DevicePeerStatusEnum;
import com.wuweibi.bullet.device.domain.DevicePeersConfigDTO;
import com.wuweibi.bullet.device.domain.DevicePeersDTO;
import com.wuweibi.bullet.device.domain.DevicePeersParam;
import com.wuweibi.bullet.device.domain.DevicePeersVO;
import com.wuweibi.bullet.device.entity.DevicePeers;
import com.wuweibi.bullet.device.mapper.DevicePeersMapper;
import com.wuweibi.bullet.device.service.DevicePeersService;
import com.wuweibi.bullet.protocol.MsgPeer;
import com.wuweibi.bullet.protocol.domain.PeerConfig;
import com.wuweibi.bullet.websocket.BulletAnnotation;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
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

        Page<DevicePeersVO> page = this.baseMapper.selectListPage(pageInfo, params);

        Page<DevicePeersVO> pageResult = new Page<>(pageInfo.getCurrent(), pageInfo.getSize(), pageInfo.getTotal());
        pageResult.setRecords(page.getRecords().stream().map(entity->{
            entity.setStatusName(DevicePeerStatusEnum.toName(entity.getStatus()));
            return entity;
        }).collect(Collectors.toList()));
        return pageResult;
    }

    @Override
    @Transactional
    public DevicePeers savePeers(Long userId, DevicePeersDTO dto) {
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
        return entity;
    }

    @Override
    public DevicePeersConfigDTO getPeersConfig(Long id) {
        return this.baseMapper.selectPeersConfig(id);
    }



    @Resource
    private CoonPool coonPool;


    public void sendMsgPeerConfig(DevicePeersConfigDTO dto) {

        String clientDeviceNo = dto.getClientDeviceNo();
        String serverDeviceNo = dto.getServerDeviceNo();

        BulletAnnotation annotation = coonPool.getByDeviceNo(clientDeviceNo);
        if (annotation != null) {
            PeerConfig doorConfig = new PeerConfig();
            doorConfig.setAppName(dto.getAppName());
            doorConfig.setPort(dto.getClientProxyPort());
            doorConfig.setHost(dto.getClientProxyHost());
            doorConfig.setType(PeerConfig.CLIENT);
            doorConfig.setEnable(dto.getStatus());
            JSONObject data = (JSONObject) JSON.toJSON(doorConfig);
            MsgPeer msg = new MsgPeer(data.toJSONString());
            annotation.sendMessage(msg);
        }
        annotation = coonPool.getByDeviceNo(serverDeviceNo);
        if (annotation != null) {
            PeerConfig doorConfig = new PeerConfig();
            doorConfig.setAppName(dto.getAppName());
            doorConfig.setPort(dto.getServerLocalPort());
            doorConfig.setHost(dto.getServerLocalHost());
            doorConfig.setType(PeerConfig.SERVER);
            doorConfig.setEnable(dto.getStatus());
            JSONObject data = (JSONObject) JSON.toJSON(doorConfig);
            MsgPeer msg = new MsgPeer(data.toJSONString());
            annotation.sendMessage(msg);
        }
    }

    @Override
    public List<DevicePeersConfigDTO> getListByDeviceNo(String deviceNo) {
        return this.baseMapper.selectListByDeviceNo(deviceNo);
    }
}
