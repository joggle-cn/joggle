package com.wuweibi.bullet.config.swagger;

import com.wuweibi.bullet.config.swagger.annotation.AdminApi;
import com.wuweibi.bullet.config.swagger.annotation.WebApi;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.oas.annotations.EnableOpenApi;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

/**
 * 通过定制 SWAGGER_DOC_ENABLE 配置是否启用swagger
 *
 * @Desc Swagger文档配置 @Author marker
 */
@Slf4j
@Configuration
@EnableOpenApi
@ConditionalOnProperty(value = "SWAGGER_DOC_ENABLE", havingValue = "true")
public class SwaggerConfig {


    @Bean
    public Docket createRest2Api() {
        return new Docket(DocumentationType.OAS_30)
                .groupName("网站接口")
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.withClassAnnotation(WebApi.class))
                .paths(PathSelectors.any())
                .build();
    }

    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.OAS_30)
                .groupName("后台接口")
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.withClassAnnotation(AdminApi.class))
                .paths(PathSelectors.any())
                .build();
    }


    @Bean
    public Docket createRest3Api() {
        return new Docket(DocumentationType.OAS_30)
                .groupName("所有接口")
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.wuweibi.bullet"))
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("接口文档")
                .description("提供网站的后台与前台的接口")
                .contact(new Contact("marker", "https://github.com/wuweiit", "admin@wuweibi.com"))
                .version("1.0.0")
                .build();
    }


}
