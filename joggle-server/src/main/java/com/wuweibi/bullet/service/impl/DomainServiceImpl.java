package com.wuweibi.bullet.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wuweibi.bullet.conn.CoonPool;
import com.wuweibi.bullet.domain.vo.DomainVO;
import com.wuweibi.bullet.domain2.domain.DomainBuyListVO;
import com.wuweibi.bullet.domain2.domain.DomainDetail;
import com.wuweibi.bullet.domain2.domain.DomainSearchParam;
import com.wuweibi.bullet.domain2.domain.dto.ReleaseResourceDTO;
import com.wuweibi.bullet.domain2.domain.vo.DomainOptionVO;
import com.wuweibi.bullet.domain2.entity.Domain;
import com.wuweibi.bullet.domain2.enums.DomainStatusEnum;
import com.wuweibi.bullet.domain2.enums.DomainTypeEnum;
import com.wuweibi.bullet.entity.DeviceMapping;
import com.wuweibi.bullet.mapper.DeviceMappingMapper;
import com.wuweibi.bullet.mapper.DomainMapper;
import com.wuweibi.bullet.protocol.MsgUnMapping;
import com.wuweibi.bullet.service.DeviceMappingService;
import com.wuweibi.bullet.service.DomainService;
import com.wuweibi.bullet.utils.CodeHelper;
import com.wuweibi.bullet.websocket.BulletAnnotation;
import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.ByteBuffer;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * <p>
 * 域名与端口服务实现类
 * </p>
 *
 * @author marker
 * @since 2017-12-09
 */
@Service
public class DomainServiceImpl extends ServiceImpl<DomainMapper, Domain> implements DomainService {


    @Resource
    private CoonPool coonPool;


    @Override
    public List<DomainVO> getListByUserId(Long userId) {
        return this.baseMapper.selectByUserId(userId);
    }

    @Override
    public boolean checkDomain(Long userId, Long domainId) {
        return this.baseMapper.existDomainUserId(userId, domainId);
    }

    @Override
    public List<DomainOptionVO> getListNotBindByUserId(Long userId, Integer serverTunnelId, Integer type) {
        return this.baseMapper.selectListNotBindByUserId(userId, serverTunnelId, type);
    }

    @Override
    public void updateDueTime(Long domainId, Long dueTime) {
        this.baseMapper.updateDueTime(domainId, new Date(dueTime));
    }

    @Resource
    private DeviceMappingService deviceMappingService;
    @Resource
    private DeviceMappingMapper deviceMappingMapper;


    @Override
    public void checkStatus() {
        // 查询过期的域名 TODO 优化为游标形式
        List<JSONObject> list = this.baseMapper.selectDueDomain();
        if (list.size() == 0) {
            return;
        }
        log.debug("check domain status ....");
        // 强行停止ngrok客户端线程
        Iterator<JSONObject> it = list.iterator();
        while (it.hasNext()) {
            JSONObject item = it.next();
            Long mappingId = item.getLong("mappingId");
            String deviceNo = item.getString("deviceNo");

            DeviceMapping entity = deviceMappingService.getById(mappingId);
            BulletAnnotation annotation = coonPool.getByDeviceNo(deviceNo);
            if (annotation != null) {
                JSONObject data = (JSONObject) JSON.toJSON(entity);
                MsgUnMapping msg = new MsgUnMapping(data.toJSONString());
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                try {
                    msg.write(outputStream);
                    // 包装了Bullet协议的
                    byte[] resultBytes = outputStream.toByteArray();
                    ByteBuffer buf = ByteBuffer.wrap(resultBytes);
                    annotation.getSession().getBasicRemote().sendBinary(buf);
                } catch (IOException e) {
                    log.error("", e);
                } finally {
                    IOUtils.closeQuietly(outputStream);
                }
            }
            // 更新Mapping状态
            deviceMappingMapper.updateStatusById(mappingId, 0);


        }

    }

    @Override
    public boolean updateUserId(Long domainId, Long userId, int status) {
        return this.update(Wrappers.<Domain>lambdaUpdate()
                .eq(Domain::getId, domainId)
                .set(Domain::getUserId, userId)
                .set(Domain::getStatus, status)
        );
    }

    @Override
    public Page<DomainBuyListVO> getBuyList(Page pageParams, DomainSearchParam params) {
        Page<DomainBuyListVO> page = this.baseMapper.selectBuyList(pageParams, params);
        page.getRecords().forEach(item->{
            item.setTypeName(DomainTypeEnum.toName(item.getType()));
        });
        return page;
    }

    @Override
    @Transactional
    public synchronized boolean release(@Valid ReleaseResourceDTO dto) {
        if (dto.getServerTunnelId() == null) {
            log.warn("release resource error tunId == null");
            return false;
        }

        if (dto.getType() == DomainTypeEnum.PORT.getType()) {
            Integer port = this.baseMapper.selectMaxPort(dto.getServerTunnelId());
            if (port == null) {
                port = 1999;
            }
            AtomicInteger atomicInteger = new AtomicInteger(port);
            for (int i = 0; i < 10; i++) {
                Domain domain = new Domain();
                domain.setType(DomainTypeEnum.PORT.getType());
                domain.setCreateTime(new Date());
                domain.setDomain(String.valueOf(atomicInteger.incrementAndGet()));
                domain.setOriginalPrice(BigDecimal.valueOf(2));
                domain.setSalesPrice(BigDecimal.valueOf(0.25)); //
                domain.setStatus(DomainStatusEnum.BUY.getStatus());
                domain.setServerTunnelId(dto.getServerTunnelId());// 默认通道
                this.baseMapper.insert(domain);
            }
        }


        if(dto.getType() == DomainTypeEnum.DOMAIN.getType()){
            for (int i=0 ;i< 10;i++){
                Domain domain = new Domain();
                domain.setType(DomainTypeEnum.DOMAIN.getType());
                domain.setCreateTime(new Date());
                domain.setDomain(CodeHelper.makeNewCode());
                domain.setOriginalPrice(BigDecimal.ONE);
                domain.setSalesPrice(BigDecimal.valueOf(0.15));
                domain.setStatus(DomainStatusEnum.BUY.getStatus());
                domain.setServerTunnelId(dto.getServerTunnelId());// 默认通道
                this.baseMapper.insert(domain);
            }
        }
        return true;
    }


    @Override
    public boolean exists(Long userId, Long domainId) {
        return this.baseMapper.selectCount(Wrappers.<Domain>lambdaQuery()
                .eq(Domain::getUserId, userId)
                .eq(Domain::getId, domainId)) > 0;
    }

    @Override
    public DomainDetail getDetail(Long domainId) {
        return this.baseMapper.selectDetail(domainId);
    }

    @Override
    public boolean checkServerTunnelUse(Integer serverTunnelId) {
        return this.baseMapper.selectCount(Wrappers.<Domain>lambdaQuery()
                .eq(Domain::getServerTunnelId, serverTunnelId)) > 0;
    }

    @Override
    public boolean updateDueTimeById(Long domainId, Date dueTime) {
        return this.baseMapper.updateDueTimeById(domainId, dueTime);
    }
}
