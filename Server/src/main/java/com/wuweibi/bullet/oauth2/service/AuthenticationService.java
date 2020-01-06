package com.wuweibi.bullet.oauth2.service;

import com.wuweibi.bullet.entity.api.Result;
import org.springframework.security.jwt.Jwt;

import javax.servlet.http.HttpServletRequest;


/**
 *
 * 权限服务
 *
 * @author marker
 */
public interface AuthenticationService {


    /**
     * 校验权限
     *
     * @param authRequest 权限请求
     * @return 是否有权限
     */
    boolean decide(HttpServletRequest authRequest);


    Jwt getJwt(String authentication);


    public Result hasPermission(HttpServletRequest request);


    /**
     * 判断url是否在忽略的范围内
     * 只要是配置中的开头，即返回true
     *
     * @param url
     * @return
     */
    boolean ignoreAuthentication(String url);
}
