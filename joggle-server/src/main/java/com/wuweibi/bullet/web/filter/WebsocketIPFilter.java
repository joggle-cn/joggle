package com.wuweibi.bullet.web.filter;

import com.wuweibi.bullet.utils.HttpUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;


public class WebsocketIPFilter implements Filter {


    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest req= (HttpServletRequest) servletRequest;

        req.setAttribute("ip", HttpUtils.getRemoteIP(req));
        req.getSession().setAttribute("ip", HttpUtils.getRemoteIP(req));
        filterChain.doFilter(servletRequest,servletResponse);
    }

}
