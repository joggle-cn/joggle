package com.wuweibi.bullet.web.filter;

import com.wuweibi.bullet.utils.HttpUtils;
import org.springframework.http.HttpHeaders;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Websocket ip 处理 （可能废弃）
 * @author marker
 */
public class WebsocketIPFilter implements Filter {


    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) servletRequest;

        String headUpgrade = req.getHeader(HttpHeaders.UPGRADE);
        if ("websocket".equalsIgnoreCase(headUpgrade)) {
            req.setAttribute("ip", HttpUtils.getRemoteIP(req));
            req.getSession(true).setAttribute("ip", HttpUtils.getRemoteIP(req));
        }

        filterChain.doFilter(servletRequest, servletResponse);
    }

}
