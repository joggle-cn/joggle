package com.wuweibi.bullet.config.properties;
/**
 * Created by marker on 2018/7/5.
 */

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 *
 * app 相关配置
 *
 * @author marker
 * @create 2018-07-05 11:51
 **/
@Data
@Configuration
@ConfigurationProperties(prefix = "joggle")
public class BulletConfig {


    /**
     * ngrokd home
     */
    private String ngrokd;

    /**
     * 前端访问地址
     */
    private String serverUrl;

    /**
     * 调用接口的Token
     */
    private String adminApiToken;

    /**
     * 阿里云市场实名认证appcode
     */
    private String aliAppcode;

}
