package com.wuweibi.bullet.config;

import com.alipay.easysdk.kernel.Config;
import com.wechat.pay.java.core.RSAAutoCertificateConfig;
import com.wuweibi.bullet.config.properties.AlipayProperties;
import com.wuweibi.bullet.config.properties.WechatpayProperties;
import com.wuweibi.bullet.utils.SpringUtils;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import jakarta.annotation.Resource;

/**
 * @author marker
 * Created by Administrator on 2019/5/30.
 */
@Slf4j
@Configuration
public class PayConfig {


    @Resource
    private WechatpayProperties wechatpayProperties;


    @Resource
    private AlipayProperties alipayProperties;

    @Resource
    private SpringUtils springUtils;

    /**
     * 微信配置
     * @return
     */
    @Bean
    @ConditionalOnProperty(prefix = "joggle.wechatpay", value = "enable", havingValue = "true")
    @SneakyThrows
    public com.wechat.pay.java.core.Config wechatpayConfig() {
        // 使用自动更新平台证书的RSA配置
        // 建议将 config 作为单例或全局静态对象，避免重复的下载浪费系统资源

        com.wechat.pay.java.core.Config config = null;

             config =
                    new RSAAutoCertificateConfig.Builder()
                            .merchantId(wechatpayProperties.getMerchantId())
//                        .privateKeyFromPath("D:\\Users\\marker\\apiclient_key.pem")
                            .privateKey(wechatpayProperties.getPrivateKey())
                            .merchantSerialNumber(wechatpayProperties.getMerchantSerialNumber())
                            .apiV3Key(wechatpayProperties.getApiV3key())
                            .build();


        return config;
    }

    /**
     * 支付宝配置
     * @return
     */
    @Bean
    public Config alipayConfig() {
        Config config = new Config();
        config.protocol = "https";
        config.gatewayHost = "openapi-sandbox.dl.alipaydev.com";
        if (springUtils.isProduction()) {
            config.gatewayHost = "openapi.alipay.com";
        }
        config.signType = "RSA2";
        config.appId = alipayProperties.getAppId();
        // 为避免私钥随源码泄露，推荐从文件中读取私钥字符串而不是写入源码中
        config.merchantPrivateKey = alipayProperties.getPrivateKey();
        //注：证书文件路径支持设置为文件系统中的路径或CLASS_PATH中的路径，优先从文件系统中加载，加载失败后会继续尝试从CLASS_PATH中加载
//        config.merchantCertPath = "<-- 请填写您的应用公钥证书文件路径，例如：/foo/appCertPublicKey_2019051064521003.crt -->";
//        config.alipayCertPath = "<-- 请填写您的支付宝公钥证书文件路径，例如：/foo/alipayCertPublicKey_RSA2.crt -->";
//        config.alipayRootCertPath = "<-- 请填写您的支付宝根证书文件路径，例如：/foo/alipayRootCert.crt -->";
        //注：如果采用非证书模式，则无需赋值上面的三个证书路径，改为赋值如下的支付宝公钥字符串即可
        config.alipayPublicKey = alipayProperties.getAlipayPublicKey();
        //可设置异步通知接收服务地址（可选）
        config.notifyUrl = alipayProperties.getNotifyUrl();
        //可设置AES密钥，调用AES加解密相关接口时需要（可选）
//        config.encryptKey = "";
        return config;
    }

}
