//package com.wuweibi.bullet.config.swagger;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.Profile;
//import springfox.documentation.builders.ApiInfoBuilder;
//import springfox.documentation.service.ApiInfo;
//import springfox.documentation.service.Contact;
//import springfox.documentation.spi.DocumentationType;
//import springfox.documentation.spring.web.plugins.Docket;
//import springfox.documentation.swagger2.annotations.EnableSwagger2;
//
//@Configuration//配置类
//@EnableSwagger2 //swagger注解
//@Profile("dev")
//public class SwaggerConfig {
//
//
//    @Bean
//    public Docket webApiConfig(){
//        Docket webApi = new Docket(DocumentationType.SWAGGER_2)
//                .groupName("webApi")
//                .apiInfo(webApiInfo())
//                .select()
////                .paths(Predicates.not(regex("/api/.*")))
////                .paths(Predicates.not(regex("/error.*")))
//                .build();
//        return webApi;
//
//    }
//
//    private ApiInfo webApiInfo(){
//
//        return new ApiInfoBuilder()
//                .title("Bullet API文档")
//                .description("本文档描述了课程中心服务接口定义")
//                .version("1.0")
//                .contact(new Contact("java", "http://xxx.com", "xxx@qq.com"))
//                .build();
//    }
//}
