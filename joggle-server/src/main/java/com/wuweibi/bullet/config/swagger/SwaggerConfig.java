package com.wuweibi.bullet.config.swagger;

import com.wuweibi.bullet.config.swagger.annotation.AdminApi;
import com.wuweibi.bullet.config.swagger.annotation.WebApi;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 通过定制 SWAGGER_DOC_ENABLE 配置是否启用swagger
 *
 * @Desc Swagger文档配置 @Author marker
 */
@Slf4j
@Configuration
@ConditionalOnProperty(value = "SWAGGER_DOC_ENABLE", havingValue = "true")
public class SwaggerConfig {


    @Bean
    public GroupedOpenApi createRest2Api() {
        return GroupedOpenApi.builder()
                .group("网站接口")
                .addOpenApiMethodFilter(method -> method.getDeclaringClass().isAnnotationPresent(WebApi.class))
                .build();
    }

    @Bean
    public GroupedOpenApi createRestApi() {
        return GroupedOpenApi.builder()
                .group("后台接口")
                .addOpenApiMethodFilter(method -> method.getDeclaringClass().isAnnotationPresent(AdminApi.class))
                .build();
    }


    @Bean
    public GroupedOpenApi createRest3Api() {
        return GroupedOpenApi.builder()
                .group("所有接口")
                .packagesToScan("com.wuweibi.bullet")
                .build();
    }

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI().info(new Info()
                .title("接口文档")
                .description("提供网站的后台与前台的接口")
                .contact(new Contact()
                        .name("marker")
                        .url("https://github.com/wuweiit")
                        .email("admin@wuweibi.com"))
                .version("1.0.0"));
    }


}
