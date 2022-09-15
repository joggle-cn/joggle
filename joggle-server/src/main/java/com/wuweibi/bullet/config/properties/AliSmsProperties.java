package com.wuweibi.bullet.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;


@Data
@Configuration
@ConfigurationProperties(prefix = "joggle.ali-sms")
public class AliSmsProperties {


    private String endpoint = "dysmsapi.aliyuncs.com";

    /**
     *  阿里云主账号AccessKey拥有所有API的访问权限，风险很高。强烈建议您创建并使用RAM账号进行API访问或日常运维，
     *  请登录 https://ram.console.aliyun.com 创建RAM账号。
     */
    private String accessKeyId;


    /**
     * accessKeySecret
     */
    private String accessKeySecret;




}
