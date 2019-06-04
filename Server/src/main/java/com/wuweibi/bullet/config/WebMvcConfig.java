package com.wuweibi.bullet.config;
/**
 * Created by marker on 2018/3/16.
 */

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.wuweibi.bullet.controller.interceptor.AllInterceptor;
import com.wuweibi.bullet.controller.interceptor.RequestParamsInterceptor;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 *
 *
 *
 * @author marker
 * @create 2018-03-16 上午11:19
 **/


@Configuration
@Order(Ordered.HIGHEST_PRECEDENCE)
public class WebMvcConfig implements WebMvcConfigurer {

    @Bean
    public HttpMessageConverter<String> responseBodyConverter() {
        StringHttpMessageConverter converter = new StringHttpMessageConverter(Charset.forName("UTF-8"));
        return converter;
    }

    @Override
    public void configurePathMatch(PathMatchConfigurer configurer) {
        //开启路径后缀匹配
        configurer.setUseRegisteredSuffixPatternMatch(true);
    }

    @Bean
    public HttpMessageConverters fastJsonHttpMessageConverters(){
        //1、先定义一个convert转换消息的对象
        FastJsonHttpMessageConverter fastConverter=new FastJsonHttpMessageConverter();
        //2、添加fastjson的配置信息，比如是否要格式化返回的json数据；
        FastJsonConfig fastJsonConfig=new FastJsonConfig();
        fastJsonConfig.setSerializerFeatures(
                SerializerFeature.PrettyFormat,
                SerializerFeature.WriteMapNullValue,
                SerializerFeature.WriteNullStringAsEmpty
                );
        //附加：处理中文乱码
        List<MediaType> fastMedisTypes = new ArrayList<MediaType>();
        fastMedisTypes.add(MediaType.APPLICATION_JSON_UTF8);
        fastConverter.setSupportedMediaTypes(fastMedisTypes);
        //3、在convert中添加配置信息
        fastConverter.setFastJsonConfig(fastJsonConfig);
        HttpMessageConverter<?> converter=fastConverter;
        return new HttpMessageConverters(converter);
    }


    /**
     * 参数定义
     * @param argumentResolvers
     */
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {

//        argumentResolvers.add(new JwtUserHandlerMethodArgumentResolver());
    }



//
//    @Bean
//    @ConditionalOnMissingBean
//    public AuthenticationProvider authenticationProvider() {
//        return new BasicAuthenticationProvider();
//    }
//
//
//
//    protected void configure(HttpSecurity http) throws Exception {
//        http
//                .authenticationProvider(authenticationProvider())
//                .csrf().disable()
//                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
//                .authorizeRequests()
//                .antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
//                .antMatchers(HttpMethod.GET, "/**").permitAll()
//                .antMatchers(HttpMethod.POST, "/**").permitAll()
//                .antMatchers(HttpMethod.PUT, "/**").permitAll()
//                .antMatchers(HttpMethod.DELETE, "/**").permitAll()
//                .anyRequest().authenticated()
//                .and()
//                .httpBasic();
//    }


//    /**
//     * 请求参数打印拦截器
//     * @return
//     */
//    @Bean
//    public HandlerInterceptor beanRequestParamsInterceptor(){
//        return new RequestParamsInterceptor();
//    }
//
//    /**
//     * 请求参数打印拦截器
//     * @return
//     */
//    @Bean
//    public HandlerInterceptor beanSignInterceptor(){
//        return new SignInterceptor();
//    }
    @Bean
    public RequestParamsInterceptor beanRequestParamsInterceptor(){
        return new RequestParamsInterceptor();
    }

    @Bean
    public AllInterceptor beanAllInterceptor(){
        return new AllInterceptor();
    }


    /**
     * 拦截器
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // addPathPatterns 用于添加拦截规则, 这里假设拦截 /url 后面的全部链接
        // excludePathPatterns 用户排除拦截
//        registry.addInterceptor(beanRequestParamsInterceptor()).addPathPatterns("/**");

        registry.addInterceptor(beanRequestParamsInterceptor()).addPathPatterns("/**");
        registry.addInterceptor(beanAllInterceptor()).addPathPatterns("/**");
    }

//    @Bean
//    public HandlerInterceptor authInterceptor(){
//        return new AuthInterceptor();
//    }

//    @Value("${server.port}")
//    private Integer port;

//    @Bean
//    public ServletWebServerFactory servletContainer() {
//        TomcatServletWebServerFactory tomcat = new TomcatServletWebServerFactory();
//        tomcat.addAdditionalTomcatConnectors(createStandardConnector()); // 添加http
//        return tomcat;
//    }
//
//    private Connector createStandardConnector() {
//        Connector connector = new Connector("org.apache.coyote.http11.Http11NioProtocol");
//        connector.setPort(port);
//        return connector;
//    }


}
