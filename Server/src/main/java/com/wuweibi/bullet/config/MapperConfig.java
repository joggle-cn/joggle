package com.wuweibi.bullet.config;
/**
 * Created by marker on 2018/3/7.
 */

import com.baomidou.mybatisplus.plugins.PaginationInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 *
 * @author marker
 * @create 2018-03-07 下午1:58
 **/
@Configuration
/** 扫描有注解的包 */
//@ComponentScan({
//    "com.byit.drm.mapper",
//})
@MapperScan({
        "com.wuweibi.bullet.mapper*",
        "com.wuweibi.bullet.oauth2.dao*",
})/** Mapper扫描 */
public class MapperConfig {


    /**
     * 日志
     */
//    private Logger logger = LoggerFactory.getLogger(this.getClass());




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