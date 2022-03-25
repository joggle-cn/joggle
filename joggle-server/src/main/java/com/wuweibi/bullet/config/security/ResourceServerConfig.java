package com.wuweibi.bullet.config.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.error.OAuth2AuthenticationEntryPoint;
import org.springframework.security.oauth2.provider.error.WebResponseExceptionTranslator;
import org.springframework.security.oauth2.provider.token.TokenStore;

import javax.annotation.Resource;
import javax.sql.DataSource;


/**
 * ResourceServerConfig
 * 资源服务
 * @author marker
 *
 */
@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

    @Value("${spring.security.oauth2.jwt.signingKey}")
    private String signingKey;

    @Resource(name = "dataSource")
    private DataSource dataSource;


    @Resource(name = "tokenStore")
    private TokenStore tokenStore;

    @Resource
    WebResponseExceptionTranslator webResponseExceptionTranslator;


    @Override
    public void configure(ResourceServerSecurityConfigurer resourceServerSecurityConfigurer) {

        OAuth2AuthenticationEntryPoint oAuth2AuthenticationEntryPoint = new OAuth2AuthenticationEntryPoint();
        oAuth2AuthenticationEntryPoint.setExceptionTranslator(webResponseExceptionTranslator);

        resourceServerSecurityConfigurer
                .authenticationEntryPoint(oAuth2AuthenticationEntryPoint)
                .tokenStore(tokenStore)
                .resourceId("WEBS");
    }

    /**
     * 资源的HttpSecurity 配置
     *
     * @param http
     * @throws Exception
     */
    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.cors();

        http.authorizeRequests()

                // 特殊接口
                .antMatchers("/","/api/open/**", "/logout", "/tunnel/**", "/_ws/log/**","/swagger-ui/**","/api/v2/api-docs").permitAll()

                // 放过静态资源
                .antMatchers("/lib/**", "/js/**","/css/**","/template/**","/resource/**","/view/**", "/index.html").permitAll()

                // 其他接口全部走认证
                .anyRequest().authenticated()

                // 关闭session
                .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).disable()
        ;
    }


}