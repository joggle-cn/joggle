package com.wuweibi.bullet.oauth2.service.impl;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.wuweibi.bullet.entity.api.Result;
import com.wuweibi.bullet.exception.BaseException;
import com.wuweibi.bullet.exception.type.AuthErrorType;
import com.wuweibi.bullet.oauth2.domain.Resource;
import com.wuweibi.bullet.oauth2.manager.ResourceManager;
import com.wuweibi.bullet.oauth2.service.AuthenticationService;
import com.wuweibi.bullet.oauth2.service.ResourceService;
import com.wuweibi.bullet.utils.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.jwt.Jwt;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.security.jwt.crypto.sign.InvalidSignatureException;
import org.springframework.security.jwt.crypto.sign.MacSigner;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.Date;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 * 权限服务
 *
 * @author marker
 */
@Slf4j
@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    /**
     * 未在资源库中的URL默认标识
     */
    public static final String NONEXISTENT_URL = "NONEXISTENT_URL";

    @javax.annotation.Resource
    private ResourceService resourceService;


    @javax.annotation.Resource
    private ResourceManager resourceManager;


    /**
     * Authorization认证开头是"bearer "
     */
    private static final int BEARER_BEGIN_INDEX = 7;

    /**
     * jwt token 密钥，主要用于token解析，签名验证
     */
    @Value("${spring.security.oauth2.jwt.signingKey}")
    private String signingKey;

    /**
     * 不需要网关签权的url配置(/oauth,/open)
     * 默认/oauth开头是不需要的
     */
    @Value("${gate.ignore.authentication.startWith}")
    private String ignoreUrls = "/oauth";


    /**
     * jwt验签
     */
    private MacSigner verifier;

    /**
     * @param authRequest 访问的url,method
     * @return 有权限true, 无权限或全局资源中未找到请求url返回否
     */
    @Override
    public boolean decide(HttpServletRequest authRequest) {
        log.debug("正在访问的url是:{}，method:{}", authRequest.getServletPath(), authRequest.getMethod());
        //获取用户认证信息
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        //获取此url，method访问对应的权限资源信息
        ConfigAttribute urlConfigAttribute = resourceManager.findConfigAttributesByUrl(authRequest);
        if (NONEXISTENT_URL.equals(urlConfigAttribute.getAttribute())) {
            log.debug("url未在资源池中找到，拒绝访问");
            throw new BaseException(AuthErrorType.ACCESS_DENIED);
        }
        //获取此访问用户所有角色拥有的权限资源
        Set<Resource> userResources = findResourcesByAuthorityRoles(authentication.getAuthorities());
        // 用户拥有权限资源 与 url要求的资源进行对比
        return isMatch(urlConfigAttribute, userResources);
    }

    /**
     * 忽略授权的地址
     *
     * @param url
     * @return
     */
    @Override
    public boolean ignoreAuthentication(String url) {
        return Stream.of(this.ignoreUrls.split(",")).anyMatch(ignoreUrl -> url.startsWith(StringUtils.trim(ignoreUrl)));
    }

    /**
     * url对应资源与用户拥有资源进行匹配
     *
     * @param urlConfigAttribute 配置属性
     * @param userResources      用户资源集合
     * @return boolean
     */
    public boolean isMatch(ConfigAttribute urlConfigAttribute, Set<Resource> userResources) {

        return userResources.stream().anyMatch(resource ->
                resource.getCode().equals(urlConfigAttribute.getAttribute()));
    }


    /**
     * 根据用户所被授予的角色，查询到用户所拥有的资源
     *
     * @param authorityRoles 角色集合
     * @return Set<Resource>
     */
    private Set<Resource> findResourcesByAuthorityRoles(Collection<? extends GrantedAuthority> authorityRoles) {
        //用户被授予的角色
        log.debug("用户的授权角色集合信息为:{}", authorityRoles);
        String[] authorityRoleCodes = authorityRoles.stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList())
                .toArray(new String[authorityRoles.size()]);
        Set<Resource> resources = resourceService.queryByRoleCodes(authorityRoleCodes);
        if (log.isDebugEnabled()) {
            log.debug("用户被授予角色的资源数量是:{}, 资源集合信息为:{}", resources.size(), resources);
        }
        return resources;
    }


    public Jwt getJwt(String authentication) {
        return JwtHelper.decode(StringUtils.substring(authentication, BEARER_BEGIN_INDEX));
    }


    /**
     * 无效jwttoken
     *
     * @param authentication
     * @return
     */
    public boolean invalidJwtAccessToken(String authentication) {
        verifier = Optional.ofNullable(verifier).orElse(new MacSigner(signingKey));
        //是否无效true表示无效
        boolean invalid = Boolean.TRUE;

        try {
            Jwt jwt = getJwt(authentication);
            jwt.verifySignature(verifier);
            // 校验有效期
            JSONObject res = JSON.parseObject(jwt.getClaims());

            long exp = res.getLong("exp");
            if (new Date().getTime() / 1000 > exp) { // 过期
                invalid = Boolean.TRUE;
            } else {
                invalid = Boolean.FALSE;
            }
        } catch (InvalidSignatureException | IllegalArgumentException ex) {
            log.warn("user token has expired or signature error ");
        }
        return invalid;
    }


    /**
     * 用户是否包含权限
     *
     * @param request 请求对象
     * @return
     */
    @Override
    public Result hasPermission(HttpServletRequest request) {
        String authentication = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (StringUtil.isBlank(authentication)) {
            return Result.fail(AuthErrorType.INVALID_TOKEN);
        }
        // token是否有效
        if (invalidJwtAccessToken(authentication)) {
            return Result.fail(AuthErrorType.INVALID_LOGIN);
        }
        //从认证服务获取是否有权限
        if (this.decide(request)) {
            return Result.success();
        } else {
            return Result.fail(AuthErrorType.UNAUTHORIZED);
        }
    }


}
