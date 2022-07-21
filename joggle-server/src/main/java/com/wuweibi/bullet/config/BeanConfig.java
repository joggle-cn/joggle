package com.wuweibi.bullet.config;

import com.alipay.easysdk.kernel.Config;
import com.wuweibi.bullet.conn.CoonPool;
import com.wuweibi.bullet.utils.SpringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 *
 * @author marker
 * Created by Administrator on 2019/5/30.
 */
@Slf4j
@Configuration
@ComponentScan(
    basePackages={
        "com.wuweibi.bullet.service",
        "com.wuweibi.bullet.client",
        "com.wuweibi.bullet.oauth2.service",
        "com.wuweibi.bullet.oauth2.manager",
        "com.wuweibi.bullet.business",
        "com.wuweibi.bullet.controller"
    }
)
public class BeanConfig {


    /**
     * WebSocket链接池
     */
    @Bean
    public SpringUtils beanSpringUtils(){
        return new SpringUtils();
    }

    /**
     * WebSocket链接池
     */
    @Bean
    public CoonPool beanCoonPool(){
        return new CoonPool();
    }



    @Bean
    public TaskExecutor beanTaskExecutor() {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setCorePoolSize(5);
        taskExecutor.setMaxPoolSize(10);
        taskExecutor.setQueueCapacity(25);
        return taskExecutor;
    }



    @Bean
    public Config getOptions() {
        Config config = new Config();
        config.protocol = "https";
//        config.gatewayHost = "openapi.alipay.com";
        config.gatewayHost = "openapi.alipaydev.com";
        config.signType = "RSA2";
        config.appId = "2021000121630337";
        // 为避免私钥随源码泄露，推荐从文件中读取私钥字符串而不是写入源码中
        config.merchantPrivateKey = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCOAwr+awPpTMdosLZJPx5LoVnJJu3QDuoz/NkouWuWvw/Jvyj0ALJs1apwwVZbi1uht4tgCHToXtC6uUGMzBKER8V0dL0Cdqqa/yzKtjtTiDrGKOdXwDnkY1ZOUS1o/FTcFHiesEFFqxAdF4YhZ4u3+ZkMhQt02WW1ulnYhzHKU0G2wGdMYbBXDP628SbiYgwqdnkqCUtMY9PFoXD5dmGKAaKOKglzrOCM2GeshbFQIkeji6U0vuNrFRSxlg6zM62CnhZSVEFcHRVrIUkW8dNeh0fJ7jfExB165ya65b2fhNmfdd+lNYqu36npqiIr/gwxfqJxb8AepnaSE3tWmE/bAgMBAAECggEAJ5q3pS5pH9Y2wn6Nx62foE32QT/nEKAoNqlsUowrchYErCnwnOPY3rtT1jBFCjC6NEaAyx5wpCFVKs3hdweunk95xLmNwLMhbyp87meV8fzLD1p28iT8wDHn7t16wY2liN74qKDVAz3qt4JI+OXstDkassAj6sl6MQ0wY0+I1jipU/qkRr1ZdbyT63vqnsTUn1QQbo93HA3X0pNIXXbxFQhh4UIcxeqThCokaF7MvTPl2JbRxdzxvru0ICLOYRwydc+NMzo9cKWFH6gt6Mml50GCp1RyyzwUaA6z0iSdf/pkXwM3QhTv9QmikbfPRA0baHy6cUn4Ub0drEoJrPCYaQKBgQDJ31TDyO26tPHWCnJd/2oMQjGWn16n2LVIYbGIbDdwG0hjcqoZh7vWTL5SE6tVfgZg8hWAhwiXhhb63QFPTfzKE1mjbT5DPNQbImwmzv8w91u6Tb0T7noS3sLiaekVMuXKdvpYoZQn10LT4xrBAN0XzNFA983/PfAHDxpYPqQChwKBgQC0FtfWMsQIajuhlBynCXBTVC5PPRzwBOPB8MXVS81pTU+qWPmkCHfgUWs+uj3x0XSsvaNS4IStIiOyYRxouSX5cGNDalNDNGherm8M8wgRpTbCYGa6ULIBBwZrm3RO1Ezc92/bJeqAGj+D24a5QiaF27fUqnB4LFez4vNzbBMZDQKBgCl5EFkIto5dDjIDA2v8RTUvhDCFnVSuWM/+AhMc6GbHtB96Nyf3ksd8DL2f/NsbgQP5uCEnnMREcAQp01MPmpeJUikZEGmP3aOVdtKiRBszNk0YG3g0zi+CRFdvmssYIc1ulBARYfatarVNOvnby5BQK81FG7UMbHuGxsrKU7eRAoGAW9+TeQkpDQn1JfdDrsuX7Erz3STWUHSOJPDKXIhxYJk6XTNLGC3hyiR/S98Dnww2M7IDUlKMHS9feq8kKSDbiKeXXFsv4wcnf7YJnVeW9DmO+BbqEX79cJy+38LM0TJpjAkHnYt4WKYLZR9nWbevo3Z2EB5rhkNI8tKDVIz8KTkCgYEAxj31ffT3rb8c9TBl1uePyzBNo23mPkfHoWw1n5GP/kDnx7LdvEIlZFSfoUrcd0m8hMp10RDAY0jjAPOGAtqGGxZvaQPtrKA0bg5htGnMByfHK9z5L7efnk1O50RcFRhSirE170ymrl/nK0EaaIx8YBIVBfq2voH4Cy3ZcsNq4nE=";
        //注：证书文件路径支持设置为文件系统中的路径或CLASS_PATH中的路径，优先从文件系统中加载，加载失败后会继续尝试从CLASS_PATH中加载
//        config.merchantCertPath = "<-- 请填写您的应用公钥证书文件路径，例如：/foo/appCertPublicKey_2019051064521003.crt -->";
//        config.alipayCertPath = "<-- 请填写您的支付宝公钥证书文件路径，例如：/foo/alipayCertPublicKey_RSA2.crt -->";
//        config.alipayRootCertPath = "<-- 请填写您的支付宝根证书文件路径，例如：/foo/alipayRootCert.crt -->";
        //注：如果采用非证书模式，则无需赋值上面的三个证书路径，改为赋值如下的支付宝公钥字符串即可
         config.alipayPublicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAg0gjxN9/7SItPIgLQx+h0rUOw4ribQBJOdRdH1BK46ks1oQ6svlpEtXeLcc3W2bvjgps4FQFKoX3m4hyUXCSyKvhYDcb/s109mi+Cy4WQtTuF9c+F++JyI/whp1mUBx7v8Eif6aV5n4Hnsfsz2sCqADdo77QDTtUfJF6vxbm5GI7/fBGKEYG4p15DUFlc6U6KbYZJkY91w89t9WNO4IXr0GAa6AJcLUCoIr64CGOseIxWC1xjdRjBbRShDOver7yuj43Inu88zZ3W7LFm35dQEiSqBZTxgG5OQybD5zvy0HJL85HOYLkro4NmFbSeba7WFMreU0f36+cZboMSOPU6QIDAQAB";
        //可设置异步通知接收服务地址（可选）
        config.notifyUrl = "https://pay.joggle.cn/api/open/orders/alipay/callback";
        //可设置AES密钥，调用AES加解密相关接口时需要（可选）
//        config.encryptKey = "";
        return config;
    }


}
