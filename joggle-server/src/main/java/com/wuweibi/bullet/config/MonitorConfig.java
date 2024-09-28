package com.wuweibi.bullet.config;

import com.wuweibi.bullet.web.filter.MonitorFilter;
import de.codecentric.boot.admin.server.config.AdminServerProperties;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.trace.http.HttpTraceRepository;
import org.springframework.boot.actuate.trace.http.InMemoryHttpTraceRepository;
import org.springframework.boot.actuate.web.trace.servlet.HttpTraceFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;

import java.util.Arrays;

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
//
    @Value("${management.trace.http.capacity:100}")
    private Integer capacity;

    @Bean
    public FilterRegistrationBean<HttpTraceFilter> traceFilterRegistration(HttpTraceFilter filter) {
        FilterRegistrationBean registration = new FilterRegistrationBean(filter);
        registration.setUrlPatterns(Arrays.asList("/**"));
        return registration;
    }

    @Bean
    public HttpTraceRepository httpTraceRepository() {
        InMemoryHttpTraceRepository repo = new InMemoryHttpTraceRepository();
        repo.setCapacity(capacity);
        return repo;
    }



}
