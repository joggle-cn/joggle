package com.wuweibi.bullet.web.filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;


public class WebsocketIPFilter implements Filter {


    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest req= (HttpServletRequest) servletRequest;
        req.getSession().setAttribute("ip", req.getRemoteHost());
        filterChain.doFilter(servletRequest,servletResponse);
    }

}
