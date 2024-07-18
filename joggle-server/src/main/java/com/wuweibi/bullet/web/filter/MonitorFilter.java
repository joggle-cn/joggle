package com.wuweibi.bullet.web.filter;

import com.wuweibi.bullet.utils.HttpUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * MonitorFilter
 * @author marker
 */
public class MonitorFilter implements Filter {


    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) servletRequest;
        req.getSession(true).setAttribute("ip", HttpUtils.getRemoteIP(req));
        filterChain.doFilter(servletRequest, servletResponse);
    }

}
