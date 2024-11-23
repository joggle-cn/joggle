package com.wuweibi.bullet.config;
/**
 * Created by marker on 2018/3/7.
 */

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
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
    "com.wuweibi.bullet.*.mapper",
    "com.wuweibi.bullet.mapper",
    "com.wuweibi.bullet.**.mapper",
    "com.wuweibi.bullet.oauth2.dao",
})
public class MapperConfig {

    @Resource
    private DataSource dataSource;

    /**
     *  （最新版)mybatis-plus分页插件
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
        return interceptor;
    }

//    @Bean
//    public MybatisSqlSessionFactoryBean newMybatisSqlSessionFactoryBean(){
//        MybatisSqlSessionFactoryBean sqlSessionFactory = new MybatisSqlSessionFactoryBean();
//        sqlSessionFactory.setDataSource(dataSource);
//        return sqlSessionFactory;
//    }


}
