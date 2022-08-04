package com.wuweibi.bullet.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;


@Data
@Configuration
@ConfigurationProperties(prefix = "joggle.ali-oss")
public class AliOssProperties {


    private String endpoint = "oss-cn-chengdu.aliyuncs.com";


    /**
     * oss公开访问的地址
     */
    private String publicServerUrl;


    /**
     *  阿里云主账号AccessKey拥有所有API的访问权限，风险很高。强烈建议您创建并使用RAM账号进行API访问或日常运维，
     *  请登录 https://ram.console.aliyun.com 创建RAM账号。
     */
    private String accessKeyId;


    /**
     * accessKeySecret
     */
    private String accessKeySecret;


    /**
     * 私有Bucket
     */
    private String publicBucketName;

    /**
     * 私有Bucket
     */
    private String privateBucketName;


    /**
     * STSToken使用
     */
    private String roleRan;





}
