package com.wuweibi.bullet.oauth2.filter;

import cn.hutool.core.net.NetUtil;
import cn.hutool.core.util.ArrayUtil;
import com.wuweibi.bullet.oauth2.consts.ClientScope;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.AbstractOAuth2TokenAuthenticationToken;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Objects;


/**
 * 用于过滤Token整合后的交叉访问情况
 *
 * @author marker
 */
@Slf4j
public class ApiAuthTokenFilter implements Filter, InitializingBean {

    // 采集内网ip清单
    private static List<String> LOCAL_IP_LIST = new ArrayList<>();

    static {
        try {
            Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
            while (networkInterfaces.hasMoreElements()) {
                NetworkInterface networkInterface = networkInterfaces.nextElement();
                Enumeration<InetAddress> inetAddresses = networkInterface.getInetAddresses();
                while (inetAddresses.hasMoreElements()) {
                    InetAddress inetAddress = inetAddresses.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        String ipAddress = inetAddress.getHostAddress();
                        LOCAL_IP_LIST.add(ipAddress);
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof AbstractOAuth2TokenAuthenticationToken<?>) {
            boolean isAdminToken = hasAdminScope(authentication);
            String uri = request.getRequestURI();
            if (uri.startsWith("/error")) { // 当请求出现404时转发到这个路径的
                return;
            }
            // 内部接口直接放开
            if (uri.startsWith("/inner")) {
                filterChain.doFilter(servletRequest, servletResponse);
                return;
            }
            if (isAdminToken && uri.startsWith("/admin")) {
                filterChain.doFilter(servletRequest, servletResponse);
                return;
            }
            if (isAdminToken && !uri.startsWith("/admin")) {
                responseUnAuth(authentication, response, uri);
                return;
            }
            if (!isAdminToken && uri.startsWith("/admin")) {
                responseUnAuth(authentication, response, uri);
                return;
            }

        }

        filterChain.doFilter(servletRequest, servletResponse);
    }

    private boolean hasAdminScope(Authentication authentication) {
        if (authentication instanceof AbstractOAuth2TokenAuthenticationToken<?> tokenAuthentication) {
            Object scopeClaim = tokenAuthentication.getTokenAttributes().get("scope");
            if (scopeClaim instanceof Iterable<?> scopes) {
                for (Object scope : scopes) {
                    if (ClientScope.SCOPE_ADMIN.equals(String.valueOf(scope))) {
                        return true;
                    }
                }
            } else if (scopeClaim instanceof String scopes
                    && List.of(scopes.split("\\s+")).contains(ClientScope.SCOPE_ADMIN)) {
                return true;
            }
        }
        return authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .filter(Objects::nonNull)
                .anyMatch(authority -> authority.equals(ClientScope.SCOPE_ADMIN)
                        || authority.equals("SCOPE_" + ClientScope.SCOPE_ADMIN));
    }


    /**
     * 判断是否内部地址
     *
     * @param request 请求
     * @return
     */
    private static boolean isInnerIP(HttpServletRequest request) {
        String remoteAddr = request.getRemoteHost();
        if ("localhost".equals(remoteAddr)) {
            return true;
        }
        if ("0:0:0:0:0:0:0:1".equals(remoteAddr)) {
            return true;
        }
        // 内网的IP清单判断
        if (ArrayUtil.contains(LOCAL_IP_LIST.toArray(), remoteAddr)) {

            return true;
        }
        return NetUtil.isInnerIP(remoteAddr);
    }


    /**
     * 相应无权限
     *
     * @param authentication
     * @param response
     * @param uri
     * @throws IOException
     */
    private void responseUnAuth(Authentication authentication, HttpServletResponse response, String uri) throws IOException {
        log.warn("user:{} 无权限访问: {}", authentication.getName(), uri);
        response.setCharacterEncoding("utf-8");
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.getWriter().write("无权限访问");
    }

    @Override
    public void afterPropertiesSet() {

    }
}
