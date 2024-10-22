package com.wuweibi.bullet.config.properties;
/**
 * Created by marker on 2018/7/5.
 */

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 *
 * 微信支付 相关配置
 *
 * @author marker
 * @create 2022-07-22 11:51
 **/
@Data
@Configuration(proxyBeanMethods = false)
@ConfigurationProperties(prefix = "joggle.wechatpay")
public class WechatpayProperties {
    private String enable;


    /**
     * 商户号
     */
    private String merchantId;

    /**
     * 公众号appid
     */
    private String appId;

    /**
     * 商户私钥
     */
    private String privateKey;

    /**
     * 商户证书序列号
     */
    private String merchantSerialNumber;

    /**
     * 商户APIV3密钥
     */
    private String apiV3key;

    /**
     * 回调地址
     */
    private String notifyUrl;

    /**
     * PC网页回调地址
     */
    private String returnUrl;

}
