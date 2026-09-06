package com.wuweibi.bullet.oauth2.service;

import com.wuweibi.bullet.entity.api.R;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Map;


/**
 *
 * 权限服务
 *
 * @author marker
 */
public interface AuthenticationService {

    /**
     * Authorization认证开头是"bearer "
     */
    int BEARER_BEGIN_INDEX = 7;


    /**
     * 校验权限
     *
     * @param authRequest 权限请求
     * @return 是否有权限
     */
    boolean decide(HttpServletRequest authRequest);


    /**
     * 获取 access token 属性。
     * @param authentication
     * @return
     */
    Map<String, Object> getTokenAttributes(String authentication);

    /**
     * 获取 access token claims JSON。
     *
     * @param authentication Authorization请求头
     * @return claims JSON
     */
    String getClaims(String authentication);


    /**
     * 判断是否有权限
     * @param request
     * @return
     */
    R hasPermission(HttpServletRequest request);


    /**
     * 判断url是否在忽略的范围内
     * 只要是配置中的开头，即返回true
     *
     * @param url
     * @return
     */
    boolean ignoreAuthentication(String url);
}
