package com.wuweibi.bullet.config.properties;
/**
 * Created by marker on 2018/7/5.
 */

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 *
 * alipay 相关配置
 *
 * @author marker
 * @create 2022-07-22 11:51
 **/
@Data
@Configuration(proxyBeanMethods = false)
@ConfigurationProperties(prefix = "joggle.alipay")
public class AlipayProperties {


    /**
     * appId
     */
    private String appId;

    /**
     * 商户私钥
     */
    private String privateKey;

    /**
     * 支付宝公钥
     */
    private String alipayPublicKey;

    /**
     * 支付宝回调地址
     */
    private String notifyUrl;

    /**
     * PC网页回调地址
     */
    private String returnUrl;

}
