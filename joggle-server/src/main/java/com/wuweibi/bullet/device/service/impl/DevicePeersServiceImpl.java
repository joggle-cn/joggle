package com.wuweibi.bullet.device.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wuweibi.bullet.conn.WebsocketPool;
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
import com.wuweibi.bullet.utils.StringUtil;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
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
    public DevicePeers savePeers(Long userId, DevicePeersDTO dto, Integer bandwidth) {
        DevicePeers entity = new DevicePeers();
        BeanUtils.copyProperties(dto, entity);
        entity.setUserId(userId);
        entity.setCreateTime(new Date());
        entity.setUpdateTime(entity.getCreateTime());
        entity.setStatus(dto.getStatus());
        entity.setServerMtu(dto.getClientMtu());
        entity.setClientMtu(dto.getClientMtu());
        entity.setConfigCompress(dto.getConfigCompress());//传输压缩
        entity.setConfigEncryption(dto.getConfigEncryption());// 传输加密方式
        entity.setConfigInterval(dto.getConfigInterval());// 配置循环周期ms
        entity.setStrategy(dto.getStrategy());
        entity.setBandwidth(bandwidth);

        String appName = DigestUtils.md5Hex(String.valueOf(new Date().getTime()));
        entity.setAppName(appName);
        this.baseMapper.insert(entity);
        return entity;
    }

    @Override
    public DevicePeersConfigDTO getPeersConfig(Long id) {
        return this.baseMapper.selectPeersConfig(id);
    }



    @Resource
    private WebsocketPool coonPool;



    public void sendMsgPeerConfig(DevicePeersConfigDTO dto) {
        if(StringUtil.isBlank(dto.getClientDeviceNo()) || StringUtil.isBlank(dto.getServerDeviceNo())){
            log.debug("P2P Conf Error AppName={}, ClientDeviceNo={}, ServerDeviceNo={}", dto.getAppName(), dto.getClientDeviceNo(), dto.getServerDeviceNo() );
           return;
        }
        if (dto.getClientDeviceTunnelId() == null || dto.getServerDeviceTunnelId() == null) {
            return;
        }

        String clientDeviceNo = dto.getClientDeviceNo();
        String serverDeviceNo = dto.getServerDeviceNo();

        // 服务器端
        PeerConfig peerConfig = new PeerConfig();
        peerConfig.setAppName(dto.getAppName());
        peerConfig.setName(dto.getName());
        peerConfig.setPort(dto.getServerLocalPort());
        peerConfig.setHost(dto.getServerLocalHost());
        peerConfig.setType(PeerConfig.SERVER);
        peerConfig.setEnable(dto.getStatus());
        peerConfig.setMtu(dto.getServerMtu());
        peerConfig.setCompress(dto.getConfigCompress());
        peerConfig.setEncryption(dto.getConfigEncryption());
        peerConfig.setInterval(dto.getConfigInterval());
        applyTransportConfig(peerConfig, dto);
        JSONObject data = (JSONObject) JSON.toJSON(peerConfig);
        MsgPeer msg = new MsgPeer(data.toJSONString());
        coonPool.sendMessage(dto.getServerDeviceTunnelId(), serverDeviceNo, msg);


        PeerConfig clientPeerConfig = new PeerConfig();
        clientPeerConfig.setAppName(dto.getAppName());
        clientPeerConfig.setName(dto.getName());
        clientPeerConfig.setPort(dto.getClientProxyPort());
        clientPeerConfig.setHost(dto.getClientProxyHost());
        clientPeerConfig.setType(PeerConfig.CLIENT);
        clientPeerConfig.setEnable(dto.getStatus());
        clientPeerConfig.setMtu(dto.getClientMtu());
        clientPeerConfig.setCompress(dto.getConfigCompress());
        clientPeerConfig.setEncryption(dto.getConfigEncryption());
        clientPeerConfig.setInterval(dto.getConfigInterval());
        applyTransportConfig(clientPeerConfig, dto);
        JSONObject clientData = (JSONObject) JSON.toJSON(clientPeerConfig);
        MsgPeer clientMsgPeer = new MsgPeer(clientData.toJSONString());
        coonPool.sendMessage(dto.getClientDeviceTunnelId(), clientDeviceNo, clientMsgPeer);
    }

    private void applyTransportConfig(PeerConfig peerConfig, DevicePeersConfigDTO dto) {
        peerConfig.setBandwidth(dto.getBandwidth());
        peerConfig.setStrategy(dto.getStrategy());
    }

    @Override
    public List<DevicePeersConfigDTO> getListByDeviceNo(String deviceNo) {
        return this.baseMapper.selectListByDeviceNo(deviceNo);
    }

    @Override
    public boolean checkLocalPortDuplicate(Long clientDeviceId, Integer clientProxyPort, Long id) {
        LambdaQueryWrapper<DevicePeers> lmq = Wrappers.<DevicePeers>lambdaQuery()
                .eq(DevicePeers::getClientDeviceId, clientDeviceId)
                .eq(DevicePeers::getClientProxyPort, clientProxyPort);
        if (id != null) {
            lmq.ne(DevicePeers::getId, id);
        }
        return this.baseMapper.selectCount(lmq) > 0;
    }

    @Override
    public List<DevicePeersVO> getListByServerDeviceId(Long deviceId) {
        List<DevicePeersVO> list = this.baseMapper.selectListByServerDeviceId(deviceId);
        list.stream().forEach(entity->{
            entity.setStatusName(DevicePeerStatusEnum.toName(entity.getStatus()));
        });
        return list;
    }
}
