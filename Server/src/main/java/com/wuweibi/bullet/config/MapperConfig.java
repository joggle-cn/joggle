package com.wuweibi.bullet.config;
/**
 * Created by marker on 2018/3/7.
 */

import com.baomidou.mybatisplus.plugins.PaginationInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Mybatis MapperConfig
 *
 * @author marker
 * @create 2018-03-07 下午1:58
 **/
@Configuration
@MapperScan({
    "com.wuweibi.bullet.mapper*",
    "com.wuweibi.bullet.oauth2.dao*",
})
public class MapperConfig {

    /**
     *  mybatis-plus分页插件
     */
    @Bean
    public PaginationInterceptor paginationInterceptor() {
        PaginationInterceptor page = new PaginationInterceptor();
        page.setDialectType("mysql");
        return page;
    }

}