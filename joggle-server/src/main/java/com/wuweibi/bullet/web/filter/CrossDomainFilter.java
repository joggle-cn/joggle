package com.wuweibi.bullet.web.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 跨域请求过滤器
 * 通过加载动态的跨域配置实现安全过滤
 * @author marker Created by ROOT on 2016/12/6.
 */

public class CrossDomainFilter implements Filter {

    /**
     * 日志记录
     */
    private Logger logger = LoggerFactory.getLogger(CrossDomainFilter.class);

    /** 页面找不到 */
    private static final int PAGE_NOT_FOUND = 404;



    /**
     * 初始化 (没使用)
     * @param filterConfig 过滤器配置
     * @throws ServletException
     */
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
                         FilterChain filterChain) throws IOException, ServletException {
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        HttpServletRequest request = (HttpServletRequest) servletRequest;

        String method = request.getMethod();

        String origin = request.getHeader("Origin");
        if (null == origin) {
            origin = "localhost";
        }

        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Allow-Origin", origin);
        response.setHeader("Access-Control-Allow-Methods", "POST,GET,PUT,DELETE");
        response.setHeader("Access-Control-Allow-Headers", "sign_key,authorization,content-type");
        response.setHeader("Access-Control-Max-Age", "0"); // 单位秒
//        logger.debug("response add cross-domain headers");

        if (method.equals("OPTIONS")) {
            response.setStatus(200);
            return;
        }
        filterChain.doFilter(servletRequest, response);
    }



    @Override
    public void destroy() {

    }
}
