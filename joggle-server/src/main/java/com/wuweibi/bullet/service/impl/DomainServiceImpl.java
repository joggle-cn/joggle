package com.wuweibi.bullet.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wuweibi.bullet.config.properties.BulletConfig;
import com.wuweibi.bullet.conn.WebsocketPool;
import com.wuweibi.bullet.domain2.domain.DomainAdminParam;
import com.wuweibi.bullet.domain2.domain.DomainBuyListVO;
import com.wuweibi.bullet.domain2.domain.DomainDetail;
import com.wuweibi.bullet.domain2.domain.DomainSearchParam;
import com.wuweibi.bullet.domain2.domain.dto.ReleaseResourceDTO;
import com.wuweibi.bullet.domain2.domain.vo.DomainListVO;
import com.wuweibi.bullet.domain2.domain.vo.DomainOptionVO;
import com.wuweibi.bullet.domain2.domain.vo.DomainReleaseVO;
import com.wuweibi.bullet.domain2.domain.vo.DomainVO;
import com.wuweibi.bullet.domain2.entity.Domain;
import com.wuweibi.bullet.domain2.enums.DomainStatusEnum;
import com.wuweibi.bullet.domain2.enums.DomainTypeEnum;
import com.wuweibi.bullet.entity.DeviceMapping;
import com.wuweibi.bullet.mapper.DeviceMappingMapper;
import com.wuweibi.bullet.mapper.DomainMapper;
import com.wuweibi.bullet.protocol.MsgUnMapping;
import com.wuweibi.bullet.res.entity.ResourcePackage;
import com.wuweibi.bullet.res.service.ResourcePackageService;
import com.wuweibi.bullet.service.DeviceMappingService;
import com.wuweibi.bullet.service.DomainService;
import com.wuweibi.bullet.service.MailService;
import com.wuweibi.bullet.utils.CodeHelper;
import com.wuweibi.bullet.websocket.Bullet3Annotation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.ibatis.cursor.Cursor;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * <p>
 * 域名与端口服务实现类
 * </p>
 *
 * @author marker
 * @since 2017-12-09
 */

@Slf4j
@Service
public class DomainServiceImpl extends ServiceImpl<DomainMapper, Domain> implements DomainService {


    @Resource
    private WebsocketPool websocketPool;


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

    @Resource
    private WebsocketPool coonPool;

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
            Bullet3Annotation annotation = websocketPool.getByTunnelId(entity.getServerTunnelId());
            if (annotation != null) { // 发送控制消息关闭映射
                JSONObject data = (JSONObject) JSON.toJSON(entity);
                MsgUnMapping msg = new MsgUnMapping(data.toJSONString());
                annotation.sendMessage(deviceNo, msg);
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

        ResourcePackage resourcePackage = resourcePackageService.getByLevel(1);
        if (resourcePackage == null) {
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
                domain.setSalesPrice(BigDecimal.valueOf(0.35)); //
                domain.setStatus(DomainStatusEnum.BUY.getStatus());
                domain.setServerTunnelId(dto.getServerTunnelId());// 默认通道
                domain.setBandwidth(resourcePackage.getBroadbandRate());// 宽带
                this.baseMapper.insert(domain);
            }
        }


        if (dto.getType() == DomainTypeEnum.DOMAIN.getType()) {
            for (int i = 0; i < 10; i++) {
                Domain domain = new Domain();
                domain.setType(DomainTypeEnum.DOMAIN.getType());
                domain.setCreateTime(new Date());
                domain.setDomain(CodeHelper.makeNewCode());
                domain.setOriginalPrice(BigDecimal.ONE);
                domain.setSalesPrice(BigDecimal.valueOf(0.25));
                domain.setStatus(DomainStatusEnum.BUY.getStatus());
                domain.setServerTunnelId(dto.getServerTunnelId());// 默认通道
                domain.setBandwidth(resourcePackage.getBroadbandRate());// 宽带
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

    @Override
    public boolean releaseById(ResourcePackage resourcePackageLevel1, Long domainId) {
        // 存在映射释放映射
        deviceMappingMapper.removeByDomainId(domainId);

        // 如果该域名是VIP权益的，扣除使用权益数量
        return this.update(Wrappers.<Domain>lambdaUpdate()
                .eq(Domain::getId, domainId)
                .set(Domain::getBandwidth, resourcePackageLevel1.getBroadbandRate())
                .set(Domain::getUserId, null)
                .set(Domain::getDueTime, null)
                .set(Domain::getBuyTime, null)
                .set(Domain::getStatus, DomainStatusEnum.BUY.getStatus())
        );
    }



    @Resource
    private MailService mailService;

    @Resource
    private SqlSessionFactory sqlSessionFactory;

    @Resource
    private BulletConfig bulletConfig;

    @Resource
    private ResourcePackageService resourcePackageService;

    @Override
    public boolean resourceDueTimeRelease() {
        log.debug("[资源到期释放] 开始");

        // 基础套餐 level 0
        ResourcePackage resourcePackageLevel1 = resourcePackageService.getByLevel(0);

        SqlSession sqlSession = sqlSessionFactory.openSession();
        Map<String, Object> params = new HashMap<>(1);
        params.put("days", 2);
        Cursor<DomainReleaseVO> cursor = sqlSession.selectCursor(DomainMapper.class.getName() + ".selectByDueDay", params);
        Iterator<DomainReleaseVO> iter = cursor.iterator();
        int count = 0;
        while (iter.hasNext()) {
            DomainReleaseVO domain = iter.next();
            log.debug("user domain[{}] release", domain.getDomainFull());
            Map<String, Object> param = new HashMap<>(3);
            param.put("domain", domain.getDomainFull());
            param.put("url", bulletConfig.getServerUrl() );
            param.put("dueTimeStr", DateFormatUtils.format(domain.getDueTime(),"yyyy-MM-dd HH:mm:ss"));
            String subject = String.format("%s到期释放提醒", domain.getDomainFull());

            this.releaseById(resourcePackageLevel1, domain.getId());
            mailService.send(domain.getUserEmail(), subject, param, "domain_release.htm");
        }
        try {
            cursor.close();
        } catch (IOException e) {
            log.error("", e);
        } finally {
            sqlSession.close();
        }

        log.debug("[资源到期释放] 结束 处理数据量：{}", count);
        return true;
    }

    @Override
    public Page<DomainListVO> getAdminList(Page pageInfo, DomainAdminParam params) {
        Page<DomainListVO> page = this.baseMapper.selectAdminList(pageInfo, params);
        page.getRecords().forEach(item->{
            item.setStatusName(DomainStatusEnum.toName(item.getStatus()));
        });
        return page;
    }

    @Override
    public boolean updateUserDueTime(Long userId, Integer bandwidth, Date endTime) {
        return this.baseMapper.updateUserDueTime(userId, bandwidth, endTime);
    }

    @Override
    public Domain getByMappingId(Long mappingId) {
        return baseMapper.selectByMappingId(mappingId);
    }

    @Override
    public boolean exists(Long domainId) {
        return this.baseMapper.selectCount(Wrappers.<Domain>lambdaQuery()
                .eq(Domain::getId, domainId)) > 0;
    }

}
