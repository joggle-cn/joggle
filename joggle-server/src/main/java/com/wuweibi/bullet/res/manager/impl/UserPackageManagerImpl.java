package com.wuweibi.bullet.res.manager.impl;

import com.wuweibi.bullet.common.exception.RException;
import com.wuweibi.bullet.entity.api.R;
import com.wuweibi.bullet.res.entity.ResourcePackage;
import com.wuweibi.bullet.res.entity.UserPackage;
import com.wuweibi.bullet.res.manager.UserPackageLimitEnum;
import com.wuweibi.bullet.res.manager.UserPackageManager;
import com.wuweibi.bullet.res.service.ResourcePackageService;
import com.wuweibi.bullet.res.service.UserPackageService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Service
public class UserPackageManagerImpl implements UserPackageManager {

    @Resource
    private UserPackageService userPackageService;

    @Resource
    private ResourcePackageService resourcePackageService;

    @Override
    public boolean checkLimit(Long userId, UserPackageLimitEnum limitEnum, int addNum) {
        UserPackage userPackage = userPackageService.getByUserId(userId);
        Integer resourcePackageId = userPackage.getResourcePackageId();
        ResourcePackage resourcePackage = resourcePackageService.getById(resourcePackageId);

        if (limitEnum.equals(UserPackageLimitEnum.DeviceNum)) {
            if (resourcePackage.getDeviceNum() < userPackage.getDeviceUse() + addNum) {
                return false;
            }
        }
        if (limitEnum.equals(UserPackageLimitEnum.PeerNum)) {
            if (resourcePackage.getP2pNum() < userPackage.getPeerUse() + addNum) {
                return false;
            }
        }
        return true;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public R usePackageAdd(Long userId, UserPackageLimitEnum limitEnum, int num) {
        UserPackage userPackage = userPackageService.getByUserId(userId);
        if (!checkLimit(userId, limitEnum, num)) {
            throw new RException("超出数量，请升级套餐");
        }
        if (limitEnum.equals(UserPackageLimitEnum.DeviceNum)) {
            userPackage.setDeviceUse(userPackage.getDeviceUse() + num);
        }
        if (limitEnum.equals(UserPackageLimitEnum.PeerNum)) {
            userPackage.setPeerUse(userPackage.getPeerUse() + num);
        }
        userPackageService.updateById(userPackage);

        return R.ok();
    }


}
