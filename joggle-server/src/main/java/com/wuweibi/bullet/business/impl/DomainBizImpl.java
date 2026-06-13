package com.wuweibi.bullet.business.impl;

import com.wuweibi.bullet.business.DomainBiz;
import com.wuweibi.bullet.domain2.entity.Domain;
import com.wuweibi.bullet.domain2.enums.DomainStatusEnum;
import com.wuweibi.bullet.domain2.enums.DomainTypeEnum;
import com.wuweibi.bullet.domain2.mapper.DomainMapper;
import com.wuweibi.bullet.entity.DeviceMapping;
import com.wuweibi.bullet.entity.api.R;
import com.wuweibi.bullet.protocol.consts.ProtocolType;
import com.wuweibi.bullet.protocol.consts.UserPackageLimitEnum;
import com.wuweibi.bullet.res.domain.UserPackageRightsDTO;
import com.wuweibi.bullet.res.entity.UserPackage;
import com.wuweibi.bullet.res.manager.UserPackageManager;
import com.wuweibi.bullet.res.service.ResourcePackageService;
import com.wuweibi.bullet.res.service.UserPackageRightsService;
import com.wuweibi.bullet.service.DomainService;
import com.wuweibi.bullet.utils.CodeHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;


/**
 *
 */
@Service
public class DomainBizImpl implements DomainBiz {


    @Resource
    private UserPackageRightsService userPackageRightsService;
    @Resource
    private ResourcePackageService resourcePackageService;
    @Resource
    private UserPackageManager userPackageManager;
    @Resource
    private DomainService domainService;
    @Resource
    private DomainMapper domainMapper;

    @Override
    @Transactional
    public R<Domain> getAvailableDomainByDeviceMapping(DeviceMapping deviceMapping) {
        UserPackageLimitEnum enumObj = ProtocolType.toPackageEnum(deviceMapping.getProtocol());
        Long userId = deviceMapping.getUserId();

        // 套餐扣除
        R<UserPackage> r1 = userPackageManager.usePackageAdd(userId, enumObj, 1);
        if (r1.isFail()) {
            return R.fail(r1.getMsg());
        }
        UserPackage userPackage = r1.getData();

        //生成域名
        Domain domain = new Domain();
        domain.setUserId(userId);
        Integer serverTunnelId = deviceMapping.getServerTunnelId();
        int type = DomainTypeEnum.PORT.getType();
        if(UserPackageLimitEnum.DomainNum.equals(enumObj)){
            type = DomainTypeEnum.DOMAIN.getType();
            domain.setType(type);
            domain.setCreateTime(new Date());
            domain.setDomain(CodeHelper.makeNewCode());
            domain.setOriginalPrice(BigDecimal.ONE);
            domain.setSalesPrice(BigDecimal.valueOf(0.25));
            domain.setStatus(DomainStatusEnum.BUY.getStatus());
            domain.setServerTunnelId(serverTunnelId);// 默认通道
            domain.setBandwidth(userPackage.getBroadbandRate());// 宽带
        }else{
            type = DomainTypeEnum.PORT.getType();
            domain.setType(type);
            Integer port = this.domainMapper.selectMaxPort(serverTunnelId);
            if (port == null) {
                port = 1999;
            }
            AtomicInteger atomicInteger = new AtomicInteger(port);
            domain.setCreateTime(new Date());
            domain.setDomain(String.valueOf(atomicInteger.incrementAndGet()));
            domain.setOriginalPrice(BigDecimal.ONE);
            domain.setSalesPrice(BigDecimal.valueOf(0.25)); // TODO 修正架构
            domain.setStatus(DomainStatusEnum.BUY.getStatus());
            domain.setServerTunnelId(serverTunnelId);// 默认通道
            domain.setBandwidth(userPackage.getBroadbandRate());// 宽带
        }
        domainService.save(domain);

        // 权益使用增加
        UserPackageRightsDTO packageRightsDTO = new UserPackageRightsDTO();
        packageRightsDTO.setUserId(userId);
        packageRightsDTO.setResourceType(domain.getType());
        packageRightsDTO.setResourceId(domain.getId());
        packageRightsDTO.setStatus(1);
        userPackageRightsService.addPackageRights(packageRightsDTO);

        return R.ok(domain);
    }
}
