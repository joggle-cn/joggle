package com.wuweibi.bullet.config;
/**
 * Created by marker on 2018/3/7.
 */

import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import javax.sql.DataSource;

/**
 * Mybatis MapperConfig
 *
 * @author marker
 * @create 2018-03-07 下午1:58
 **/
@Configuration
@MapperScan(basePackages={
    "com.wuweibi.bullet.mapper",
    "com.wuweibi.bullet.oauth2.dao",
})
public class MapperConfig {

    @Resource
    private DataSource dataSource;

    /**
     *  mybatis-plus分页插件
     */
    @Bean
    public PaginationInterceptor paginationInterceptor() {
        PaginationInterceptor page = new PaginationInterceptor();
        page.setDialectType("mysql");
        return page;
    }

//    @Bean
//    public MybatisSqlSessionFactoryBean newMybatisSqlSessionFactoryBean(){
//        MybatisSqlSessionFactoryBean sqlSessionFactory = new MybatisSqlSessionFactoryBean();
//        sqlSessionFactory.setDataSource(dataSource);
//        return sqlSessionFactory;
//    }


}