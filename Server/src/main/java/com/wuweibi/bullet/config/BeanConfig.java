package com.wuweibi.bullet.config;

import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import com.wuweibi.bullet.conn.CoonPool;
import com.wuweibi.bullet.filter.CrossDomainFilter;
import com.wuweibi.bullet.utils.SpringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author marker
 * Created by Administrator on 2019/5/30.
 */
@Slf4j
@Configuration
public class BeanConfig {


    /**
     * WebSocket链接池
     */
    @Bean
    public SpringUtils beanSpringUtils(){
        return new SpringUtils();
    }

    /**
     * WebSocket链接池
     */
    @Bean
    public CoonPool beanCoonPool(){
        return new CoonPool();
    }



    @Bean
    public TaskExecutor beanTaskExecutor() {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setCorePoolSize(5);
        taskExecutor.setMaxPoolSize(10);
        taskExecutor.setQueueCapacity(25);
        return taskExecutor;
    }



    @Bean
    public FilterRegistrationBean FilterRegistration() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(new CrossDomainFilter());//添加过滤器
        registration.addUrlPatterns("/*");//设置过滤路径，/*所有路径
        registration.setName("CrossDomainFilter");//设置优先级
        registration.setOrder(Integer.MAX_VALUE);//设置优先级
        return registration;
    }

    @Bean
    public FilterRegistrationBean WebStatFilter() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(new WebStatFilter());//添加过滤器
        registration.addInitParameter("exclusions", "*.js,*.gif,*.jpg,*.png,*.css,*.ico,/druid/*");
        registration.addUrlPatterns("/*");//设置过滤路径，/*所有路径
        registration.setName("WebStatFilter");//设置优先级
        return registration;
    }

    @Bean
    public ServletRegistrationBean druidServlet() {
        log.info("init Druid Servlet Configuration ");
        ServletRegistrationBean servletRegistrationBean = new ServletRegistrationBean();
        servletRegistrationBean.setServlet(new StatViewServlet());
        servletRegistrationBean.addUrlMappings("/druid/*");
        Map<String, String> initParameters = new HashMap<String, String>();
        initParameters.put("loginUsername", "admin");// 用户名
        initParameters.put("loginPassword", "123456");// 密码
        initParameters.put("resetEnable", "false");// 禁用HTML页面上的“Reset All”功能
//        initParameters.put("allow", ""); // IP白名单 (没有配置或者为空，则允许所有访问)
        //initParameters.put("deny", "192.168.20.38");// IP黑名单 (存在共同时，deny优先于allow)
        servletRegistrationBean.setInitParameters(initParameters);

        return servletRegistrationBean;
    }

}
