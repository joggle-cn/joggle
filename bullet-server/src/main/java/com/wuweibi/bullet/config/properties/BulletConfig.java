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
@ConfigurationProperties(prefix = "bullet")
public class BulletConfig {


    private String ngrokd;

}
