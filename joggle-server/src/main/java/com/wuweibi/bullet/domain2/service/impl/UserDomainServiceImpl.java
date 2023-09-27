package com.wuweibi.bullet.domain2.service.impl;

import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wuweibi.bullet.conn.WebsocketPool;
import com.wuweibi.bullet.domain2.domain.UserDomainParam;
import com.wuweibi.bullet.domain2.domain.UserDomainVO;
import com.wuweibi.bullet.domain2.domain.dto.DomainCertUpdate;
import com.wuweibi.bullet.domain2.domain.dto.UserDomainAddDTO;
import com.wuweibi.bullet.domain2.entity.UserDomain;
import com.wuweibi.bullet.domain2.mapper.UserDomainMapper;
import com.wuweibi.bullet.domain2.service.UserDomainService;
import com.wuweibi.bullet.entity.api.R;
import com.wuweibi.bullet.protocol.MsgDomainCert;
import com.wuweibi.bullet.service.UserService;
import com.wuweibi.bullet.system.entity.User;
import com.wuweibi.bullet.utils.SpringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.InitialDirContext;
import java.io.ByteArrayInputStream;
import java.security.cert.X509Certificate;
import java.util.Date;
import java.util.Hashtable;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 用户域名(UserDomain)表服务实现类
 *
 * @author marker
 * @since 2023-09-17 17:44:09
 */
@Slf4j
@Service
public class UserDomainServiceImpl extends ServiceImpl<UserDomainMapper, UserDomain> implements UserDomainService {


    @Override
    public Page<UserDomainVO> getPage(Page pageInfo, UserDomainParam params) {

        Page<UserDomainVO> page = this.baseMapper.selectListPage(pageInfo, params);

        Page<UserDomainVO> pageResult = new Page<>(pageInfo.getCurrent(), pageInfo.getSize(), pageInfo.getTotal());
        pageResult.setRecords(page.getRecords().stream().map(entity -> {
            UserDomainVO vo = new UserDomainVO();
            BeanUtils.copyProperties(entity, vo);
            return vo;
        }).collect(Collectors.toList()));
        return pageResult;
    }

    @Override
    public R<Boolean> saveUserDomain(UserDomainAddDTO addDTO) {
        // 校验域名重复
        if (this.baseMapper.selectCount(Wrappers.<UserDomain>lambdaQuery()
                .eq(UserDomain::getDomain, addDTO.getDomain())) > 0) {
            return R.fail("域名已经存在");
        }

        UserDomain userDomain = new UserDomain();
        BeanUtils.copyProperties(addDTO, userDomain);
        userDomain.setCreateTime(new Date());
        userDomain.setUpdateTime(new Date());
        return R.ok(this.baseMapper.insert(userDomain) > 0);
    }

    @Resource
    private UserService userService;

    @Override
    public boolean checkDomain(Long userId, String domain) {
        // 查询用户新
        User user = userService.getById(userId);
        if (user == null) {
            log.warn("checkDomain 用户信息不存在 {}", userId);
            return false;
        }
        String activeCode = user.getActivateCode();
        // 获取根域名
        String rootDomain = domain.replaceAll(".*\\.(?=.*\\.)", "");

        String url = "accept_joggle." + rootDomain;
        try {
            log.info("DNS TXT {}", url);
            Hashtable<String, String> env = new Hashtable<String, String>(1);
            env.put("java.naming.factory.initial", "com.sun.jndi.dns.DnsContextFactory");
            InitialDirContext dirContext = new InitialDirContext(env);
            Attributes attrs = dirContext.getAttributes(url, new String[]{"TXT"});
            Attribute txt = attrs.get("TXT");
            log.info("DNS TXT {} result:{}", url, txt.get());
            if (Objects.equals(activeCode, txt.get())) {
                return true;
            }
        } catch (NamingException e) {
            log.warn("{}", e.getMessage());
        }
        return false;
    }

    @Override
    public R<Boolean> updateDomainCert(DomainCertUpdate domainCertUpdate) {
        UserDomain userDomain = this.baseMapper.selectById(domainCertUpdate.getId());
        if (userDomain == null) {
            return R.fail("域名id不存在");
        }

        String domain = userDomain.getDomain();

        // 加载证书识别域名一致性
        String certPem = domainCertUpdate.getCertPem();
        try {
            X509Certificate certificate = (X509Certificate) SecureUtil.readX509Certificate(new ByteArrayInputStream(certPem.getBytes()));
            log.debug("证书验证通过 domain={}", domain);
            if (!certificate.getSubjectDN().getName().contains(domain)) {
                log.warn("域名证书不一致cert={} your={}", certificate.getSubjectDN().getName(), domain);
                return R.fail("域名证书不一致");
            }
            // 获取证书证书时间
            Date startTime = certificate.getNotBefore();
            Date endTime = certificate.getNotAfter();
            userDomain.setApplyTime(startTime);
            userDomain.setDueTime(endTime);
        } catch (Exception e) {
            log.warn("证书格式错误 domain={}", domain);
            return R.fail("证书格式错误");
        }

        userDomain.setCertKey(domainCertUpdate.getCertKey());
        userDomain.setCertPem(domainCertUpdate.getCertPem());
        userDomain.setUpdateTime(new Date());
        this.baseMapper.updateById(userDomain);

        // 通知所有节点更新证书。
        MsgDomainCert msgDomainCert = new MsgDomainCert(userDomain.getDomain(), userDomain.getCertKey(), userDomain.getCertPem());

        WebsocketPool pool = SpringUtils.getBean(WebsocketPool.class);
        pool.listStream().forEach(conn->{
            conn.sendMessageToServer(msgDomainCert);
        });
        return R.ok();
    }

    @Override
    public R<Boolean> removeDomain(Long id) {
        // TODO 通知所有节点删除证书


        this.baseMapper.deleteById(id);
        return R.ok();
    }

    @Override
    public boolean checkUserDomain(Long userId, Long userDomainId) {
        return this.baseMapper.selectCount(Wrappers.<UserDomain>lambdaQuery()
                .eq(UserDomain::getUserId, userId)
                .eq(UserDomain::getId, userDomainId)
        ) > 0;
    }
}
