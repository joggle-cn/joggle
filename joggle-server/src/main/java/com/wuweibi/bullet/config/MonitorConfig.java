package com.wuweibi.bullet.config;

import com.wuweibi.bullet.web.filter.MonitorFilter;
import de.codecentric.boot.admin.server.config.AdminServerProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;

/**
 * 监控配置
 * @author marker
 */
@Configuration
public class MonitorConfig {

    @Bean
    public FilterRegistrationBean filterRegistrationMonitorFilter(AdminServerProperties adminServerProperties) {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(new MonitorFilter());
        registration.addUrlPatterns(adminServerProperties.path("/**")); // oauth token
        registration.setName("MonitorFilter");
        registration.setOrder(Ordered.LOWEST_PRECEDENCE);//设置最高优先级
        return registration;
    }




}
