package com.wuweibi.bullet.config.security;

import de.codecentric.boot.admin.server.config.AdminServerProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import javax.annotation.Resource;


/**
 * WebServerSecurityConfig
 *
 * @author marker
 */
@Slf4j
@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    /**
     * userDetailsService
     */
    @Resource
    private UserDetailsService userDetailsService;


    /**
     * springbootAdmin server 配置项
     */
    @Resource
    private AdminServerProperties adminServerProperties;



    // 当使用Oauth 资源服务器 该配置无效
    @Override
    public void configure(HttpSecurity http) throws Exception {
        // 关闭HTTP Basic认证
//        http.httpBasic().disable();
//        http.csrf().disable();
//        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).disable();
//        http .anonymous().disable();

//        // 允许跨域
//        http .addFilterBefore(corsFilter(), WebAsyncManagerIntegrationFilter.class)
//            .authorizeRequests()
//            //处理跨域请求中的Preflight请求
//            .antMatchers(HttpMethod.OPTIONS).permitAll()
//            .requestMatchers(CorsUtils::isPreFlightRequest).permitAll()
//
//
//
//
//
//            .antMatchers("/oauth/**", "/actuator/**", "/logout", "/error","/api/open/**","/inner/open/**", "/swagger-ui/**","/api/v2/api-docs")
//            .permitAll()
//            .anyRequest().authenticated() // 剩下的所有请求登录后就能访问
//        ;

        String monitorContextPath = adminServerProperties.getContextPath();

        // @formatter:off
        SavedRequestAwareAuthenticationSuccessHandler successHandler = new SavedRequestAwareAuthenticationSuccessHandler();
        successHandler.setTargetUrlParameter("redirectTo");
        successHandler.setDefaultTargetUrl(monitorContextPath + "/");

        http
                .headers().frameOptions().disable()
                .and().authorizeRequests()
//                .antMatchers(monitorContextPath + "/assets/**"
//                        , monitorContextPath + "/login"
//                        , monitorContextPath + "/instances" // 注册入口
//                        , monitorContextPath + "/actuator/**"
//                        , monitorContextPath + "/redis/info"
//                        ,"/actuator/**"
//                ).permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin().loginPage(monitorContextPath + "/login")
                .successHandler(successHandler).and()
                .logout().logoutUrl(monitorContextPath + "/logout")
                .and().httpBasic().and()
                .csrf()
                .disable();
    }

    /**
     * 将 AuthenticationManager 注册为 bean , 方便配置 oauth server 的时候使用
     */
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        AuthenticationManager authenticationManager = super.authenticationManagerBean();
        return authenticationManager;
    }


    /**
     * 注入自定义的userDetailsService实现，获取用户信息，设置密码加密方式
     *
     * @param authenticationManagerBuilder
     * @throws Exception
     */
    @Override
    protected void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        authenticationManagerBuilder
                .userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder());

    }

    /**
     * 密码编码器
     * @return
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

//    @Bean
//    JwtDecoder jwtDecoder() {
//        return NimbusJwtDecoder.withPublicKey(this.key).build();
//    }




    @Bean
    public CorsFilter corsFilter() {
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        final CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true); // 允许cookies跨域
        config.addAllowedOriginPattern("*");// 允许向该服务器提交请求的URI，*表示全部允许。。这里尽量限制来源域，比如http://xxxx:8080 ,以降低安全风险。。
        config.addAllowedHeader("*");// 允许访问的头信息,*表示全部
        config.setMaxAge(0L);// 预检请求的缓存时间（秒），即在这个时间段里，对于相同的跨域请求不会再预检了
        config.addAllowedMethod("*");// 允许提交请求的方法，*表示全部允许，也可以单独设置GET、PUT等
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }

}
