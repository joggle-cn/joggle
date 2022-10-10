package com.wuweibi.bullet.config;

import com.alipay.easysdk.kernel.Config;
import com.wuweibi.bullet.config.properties.AliSmsProperties;
import com.wuweibi.bullet.config.properties.AlipayProperties;
import com.wuweibi.bullet.conn.WebsocketPool;
import com.wuweibi.bullet.utils.SpringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import javax.annotation.Resource;

/**
 * @author marker
 * Created by Administrator on 2019/5/30.
 */
@Slf4j
@Configuration
@ComponentScan(
        basePackages = {
                "com.wuweibi.bullet.service",
                "com.wuweibi.bullet.client",
                "com.wuweibi.bullet.oauth2.service",
                "com.wuweibi.bullet.oauth2.manager",
                "com.wuweibi.bullet.business",
                "com.wuweibi.bullet.controller"
        }
)
public class BeanConfig {

    @Resource
    private AliSmsProperties aliSmsProperties;

    /**
     * WebSocket链接池
     */
    @Bean
    public SpringUtils beanSpringUtils() {
        return new SpringUtils();
    }

    /**
     * WebSocket链接池
     */
    @Bean
    public WebsocketPool beanC2oonPool() {
        return new WebsocketPool();
    }


    @Bean
    public TaskExecutor beanTaskExecutor() {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setCorePoolSize(5);
        taskExecutor.setMaxPoolSize(10);
        taskExecutor.setQueueCapacity(25);
        return taskExecutor;
    }


    @Resource
    private AlipayProperties alipayProperties;

    @Bean
    public Config getOptions() {
        Config config = new Config();
        config.protocol = "https";
        config.gatewayHost = "openapi.alipaydev.com";
        if (beanSpringUtils().isProduction()) {
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




    /**
     * 使用AK&SK初始化账号Client
     * @return Client
     * @throws Exception
     */
    @Bean
    public com.aliyun.dysmsapi20170525.Client createClient( ) throws Exception {
        com.aliyun.teaopenapi.models.Config config = new com.aliyun.teaopenapi.models.Config()
                .setAccessKeyId(aliSmsProperties.getAccessKeyId())
                .setAccessKeySecret(aliSmsProperties.getAccessKeySecret());
        // 访问的域名
        config.endpoint = "dysmsapi.aliyuncs.com";
        return new com.aliyun.dysmsapi20170525.Client(config);
    }

}
