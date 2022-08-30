package com.wuweibi.bullet.ratelimiter.properties;

import lombok.Data;
import org.redisson.api.RateType;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

import static com.wuweibi.bullet.ratelimiter.properties.RateLimiterProperties.PREFIX;

/**
 * 配置更新后自动刷新到配置对象
 * <p>
 * @author marker
 * Created by marker on 2021/07/12.
 */
@Data
@Configuration
@ConfigurationProperties(prefix = PREFIX)
public class RateLimiterProperties {

    /**
     * 配置前缀
     */
    public static final String PREFIX = "spring.rate-limiter";


    /**
     * 是否启用限流
     */
    private Boolean enable;

    /**
     * 周期内的令牌量
     */
    private Integer rate;

    /**
     * 周期 单位 秒
     */
    private Integer interval;

    /**
     * 类型
     */
    private RateType type = RateType.PER_CLIENT;



    private List<String> ignoreUrls = new ArrayList<>();


    /**
     * redis服务器IP地址
     */
    private String host = "localhost";

    /**
     * Redis 密码
     */
    private String password;

    /**
     * 数据库
     */
    private int database = 14;

    /**
     * Redis端口
     */
    private int port = 3306;


    private String codec = "org.redisson.codec.JsonJacksonCodec";

    /**
     * 连接池配置
     */
    private RedisProperties.Lettuce lettuce = new RedisProperties.Lettuce();


    private ClusterServer clusterServer;

    /**
     * 构造
     */
    public RateLimiterProperties() {

    }


    public static class ClusterServer{

        private String[] nodeAddresses;

        public String[] getNodeAddresses() {
            return nodeAddresses;
        }

        public void setNodeAddresses(String[] nodeAddresses) {
            this.nodeAddresses = nodeAddresses;
        }
    }

}
