package com.wuweibi.bullet.ratelimiter.autoconfigure;


import com.wuweibi.bullet.ratelimiter.properties.RateLimiterProperties;
import com.wuweibi.bullet.ratelimiter.util.RateSpringUtils;
import io.netty.channel.nio.NioEventLoopGroup;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.client.codec.Codec;
import org.redisson.config.Config;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ClassUtils;

import javax.annotation.Resource;


/**
 * RateLimiterAutoConfiguration 自动装配配置类
 *
 * @author marker
 */
@Configuration
@ConditionalOnProperty(prefix = RateLimiterProperties.PREFIX, value = "enabled", havingValue = "true")
public class RateLimiterAutoConfiguration {


    @Resource
    private RateLimiterProperties properties;

    @Resource
    private RedisProperties redisProperties;


    @Bean(name = "redissonClient", destroyMethod = "shutdown")
    public RedissonClient redissonClient() throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        Config config = new Config();
        if (properties.getClusterServer() != null) {
            config.useClusterServers().setPassword(redisProperties.getPassword())
                    .addNodeAddress(properties.getClusterServer().getNodeAddresses());
        } else {
            String address = "redis://"+ redisProperties.getHost() + ":" + redisProperties.getPort();
            config.useSingleServer().setAddress(address)
                    .setDatabase(redisProperties.getDatabase())
                    .setPassword(redisProperties.getPassword())
                    .setClientName("rateLimiter");
        }

        System.out.println("------------- redisson -----------------------");
        Codec codec = (Codec) ClassUtils.forName(properties.getCodec(), ClassUtils.getDefaultClassLoader()).newInstance();
        config.setCodec(codec);
        config.setEventLoopGroup(new NioEventLoopGroup());
        return Redisson.create(config);
    }



    @Bean
    public RateSpringUtils newRateSpringUtils(){
        return new RateSpringUtils();
    }



    // 自动装配Globalfilter


}
