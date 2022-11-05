package com.wuweibi.bullet.res.manager.impl;

import com.wuweibi.bullet.common.exception.RException;
import com.wuweibi.bullet.config.properties.BulletConfig;
import com.wuweibi.bullet.entity.api.R;
import com.wuweibi.bullet.res.domain.UserPackageExpireVO;
import com.wuweibi.bullet.res.domain.UserPackageFowVO;
import com.wuweibi.bullet.res.entity.ResourcePackage;
import com.wuweibi.bullet.res.entity.UserPackage;
import com.wuweibi.bullet.res.manager.UserPackageLimitEnum;
import com.wuweibi.bullet.res.manager.UserPackageManager;
import com.wuweibi.bullet.res.mapper.UserPackageMapper;
import com.wuweibi.bullet.res.service.ResourcePackageService;
import com.wuweibi.bullet.res.service.UserPackageRightsService;
import com.wuweibi.bullet.res.service.UserPackageService;
import com.wuweibi.bullet.service.MailService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.ibatis.cursor.Cursor;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.*;

@Slf4j
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
            int cur = userPackage.getDeviceUse() + addNum;
            if (cur > 0 && resourcePackage.getDeviceNum() < cur || cur < 0) {
                return false;
            }
        }
        if (limitEnum.equals(UserPackageLimitEnum.PeerNum)) {
            int cur = userPackage.getPeerUse() + addNum;
            if (cur > 0 && resourcePackage.getP2pNum() < cur || cur < 0) {
                return false;
            }
        }
        if (limitEnum.equals(UserPackageLimitEnum.PortNum)) {
            int cur = userPackage.getPortUse() + addNum;
            if (cur > 0 && resourcePackage.getPortNum() < cur || cur < 0) {
                return false;
            }
        }
        if (limitEnum.equals(UserPackageLimitEnum.DomainNum)) {
            int cur = userPackage.getDomainUse() + addNum;
            if (cur > 0 && resourcePackage.getDomainNum() < cur || cur < 0) {
                return false;
            }
        }
        return true;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public R usePackageAdd(Long userId, UserPackageLimitEnum limitEnum, int num) {
        if (Objects.isNull(limitEnum)) {
            return R.fail("参数错误");
        }
        UserPackage userPackage = userPackageService.getByUserId(userId);
        if (num >=0 && !checkLimit(userId, limitEnum, num)) {
            throw new RException("超出数量，请升级套餐");
        }
        if (limitEnum.equals(UserPackageLimitEnum.DeviceNum)) {
            userPackage.setDeviceUse(userPackage.getDeviceUse() + num);
        }
        if (limitEnum.equals(UserPackageLimitEnum.PeerNum)) {
            userPackage.setPeerUse(userPackage.getPeerUse() + num);
        }
        if (limitEnum.equals(UserPackageLimitEnum.PortNum)) {
            userPackage.setPortUse(userPackage.getPortUse() + num);
        }
        if (limitEnum.equals(UserPackageLimitEnum.DomainNum)) {
            userPackage.setDomainUse(userPackage.getDomainUse() + num);
        }
        userPackageService.updateById(userPackage);


        // 权益使用记录 TODO

        return R.ok();
    }

    @Override
    public R<UserPackage> openService(Long userId, Integer packageId, Integer amount) {
        ResourcePackage resourcePackage = resourcePackageService.getById(packageId);

        UserPackage userPackage = userPackageService.getByUserId(userId);
        boolean isInsert = false;
        if (userPackage == null) {
            userPackage = new UserPackage();
            userPackage.setUserId(userId);
            userPackage.setLevel(resourcePackage.getLevel());
            userPackage.setName(resourcePackage.getName());
            userPackage.setResourcePackageId(resourcePackage.getId());
            userPackage.setConcurrentNum(resourcePackage.getConcurrentNum());
            userPackage.setBroadbandRate(resourcePackage.getBroadbandRate());
            userPackage.setWolEnable(resourcePackage.getWolEnable());
            userPackage.setProxyEnable(resourcePackage.getProxyEnable());
            userPackage.setStartTime(new Date());
            isInsert = true;

            ResourcePackage resourcePackageLevel0 = resourcePackageService.getByLevel(0);
            userPackage.setFlow(resourcePackageLevel0.getFlowNum());
        } else {
            if (userPackage.getLevel() > resourcePackage.getLevel()) {
                return R.fail("套餐升级失败，原因不支持降级。");
            }
            if (userPackage.getStartTime() == null) {
                userPackage.setStartTime(new Date());
            }
            userPackage.setLevel(resourcePackage.getLevel());
            userPackage.setName(resourcePackage.getName());
            userPackage.setResourcePackageId(resourcePackage.getId());
            userPackage.setConcurrentNum(resourcePackage.getConcurrentNum());
            userPackage.setBroadbandRate(resourcePackage.getBroadbandRate());
            userPackage.setWolEnable(resourcePackage.getWolEnable());
            userPackage.setProxyEnable(resourcePackage.getProxyEnable());
        }

        if (userPackage.getEndTime() == null) {
            userPackage.setEndTime(new Date());
        }
        if (amount != null) { // 购买数量空代表永久
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(userPackage.getEndTime());
            calendar.add(Calendar.DATE, amount * 30);
            userPackage.setEndTime(calendar.getTime());
        }

        if (isInsert) {
            userPackageService.save(userPackage);
        } else {
            userPackageService.updateById(userPackage);
        }

        return R.ok(userPackage);
    }


    @Resource
    private SqlSessionFactory sqlSessionFactory;

    @Resource
    private BulletConfig bulletConfig;

    @Resource
    private MailService mailService;


    @Override
    public R expireFree() {
        log.debug("[资源包到期释放] 开始");
        SqlSession sqlSession = sqlSessionFactory.openSession();
        Map<String, Object> params = new HashMap<>(1);
        Cursor<UserPackageExpireVO> cursor = sqlSession.selectCursor(UserPackageMapper.class.getName() + ".selectByExpireDay", params);
        Iterator<UserPackageExpireVO> iter = cursor.iterator();
        int count = 0;
        while (iter.hasNext()) {
            UserPackageExpireVO userPackage = iter.next();
            log.info("user[{}] package[{}] expireFree", userPackage.getUserId(), userPackage.getResourcePackageId());
            Map<String, Object> param = new HashMap<>(3);
            param.put("packageName", userPackage.getName());
            param.put("url", bulletConfig.getServerUrl());
            param.put("dueTimeStr", DateFormatUtils.format(userPackage.getEndTime(), "yyyy-MM-dd HH:mm:ss"));
            String subject = String.format("%s到期提醒", userPackage.getName());
            this.free(userPackage);

            mailService.send(userPackage.getUserEmail(), subject, param, "package_release.htm");
        }
        try {
            cursor.close();
        } catch (IOException e) {
            log.error("", e);
        } finally {
            sqlSession.close();
        }

        log.debug("[资源包到期释放] 结束 处理数据量：{}", count);

        return R.ok();
    }

    @Override
    public void resetPackageFlow() {
        log.debug("[资源包每月发放流量] 开始");
        SqlSession sqlSession = sqlSessionFactory.openSession();
        Map<String, Object> params = new HashMap<>(1);
        Cursor<UserPackageFowVO> cursor = sqlSession.selectCursor(UserPackageMapper.class.getName() + ".selectByRestPackageFlow", params);
        Iterator<UserPackageFowVO> iter = cursor.iterator();
        int count = 0;
        while (iter.hasNext()) {
            UserPackageFowVO userPackage = iter.next();
            log.info("user[{}] package[{}] resetflow", userPackage.getUserId(), userPackage.getResourcePackageId());

            this.userPackageService.updateRestFLow(userPackage.getUserId(), userPackage.getResourcePackageFlow());



//            Map<String, Object> param = new HashMap<>(3);
//            param.put("packageName", userPackage.getName());
//            param.put("url", bulletConfig.getServerUrl());
//            param.put("dueTimeStr", DateFormatUtils.format(userPackage.getEndTime(), "yyyy-MM-dd HH:mm:ss"));
//            String subject = String.format("%s到期提醒", userPackage.getName());
//            this.free(userPackage);

//            mailService.send(userPackage.getUserEmail(), subject, param, "package_release.htm");
        }
        try {
            cursor.close();
        } catch (IOException e) {
            log.error("", e);
        } finally {
            sqlSession.close();
        }

        log.debug("[资源包每月发放流量] 结束 处理数据量：{}", count);

    }


    /**
     * 释放用户购买的资源
     *
     * @param dto
     */
    private void free(UserPackageExpireVO dto) {
        Integer packageId = dto.getResourcePackageId();
        Long userId = dto.getUserId();

        ResourcePackage resourcePackage = resourcePackageService.getByLevel(0);

        UserPackage userPackage = userPackageService.getByUserId(userId);
        if (userPackage.getResourcePackageId() != packageId) {
            log.error("释放资源失败，资源包不一致 userId={}", userId);
            throw new RException("释放资源失败，资源包不一致 userId=" + userId);
        }

        userPackage.setLevel(resourcePackage.getLevel());
        userPackage.setName(resourcePackage.getName());
        userPackage.setResourcePackageId(resourcePackage.getId());
        userPackage.setConcurrentNum(resourcePackage.getConcurrentNum());
        userPackage.setProxyEnable(resourcePackage.getProxyEnable());
        userPackage.setWolEnable(resourcePackage.getWolEnable());
        userPackage.setEndTime(null); // 这个设置无效
        userPackageService.updateToLevel0ByUserId(userId, userPackage);

        // 删除用户的所有权益
        userPackageRightsService.removeResourceByUserId(userId);


    }

    @Resource
    private UserPackageRightsService userPackageRightsService;

}
