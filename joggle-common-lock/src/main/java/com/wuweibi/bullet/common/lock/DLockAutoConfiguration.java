package com.wuweibi.bullet.common.lock;

import com.wuweibi.bullet.common.lock.config.DLockConfig;
import com.wuweibi.bullet.common.lock.core.BusinessKeyProvider;
import com.wuweibi.bullet.common.lock.core.DLockAspectHandler;
import com.wuweibi.bullet.common.lock.core.LockInfoProvider;
import com.wuweibi.bullet.common.lock.lock.LockFactory;
import io.netty.channel.nio.NioEventLoopGroup;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.client.codec.Codec;
import org.redisson.config.Config;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.util.ClassUtils;

import javax.annotation.Resource;

/**
 * @author kl
 * @date 2017/12/29
 * Content :dlock自动装配
 */
@Configuration
@ConditionalOnProperty(prefix = DLockConfig.PREFIX, name = "enable", havingValue = "true", matchIfMissing = true)
@AutoConfigureAfter(RedisAutoConfiguration.class)
@EnableConfigurationProperties(DLockConfig.class)
@Import({DLockAspectHandler.class})
public class DLockAutoConfiguration {

    @Resource
    private DLockConfig lockConfig;




    @Bean(destroyMethod = "shutdown")
    @ConditionalOnMissingBean
    RedissonClient redisson() throws Exception {
        Config config = new Config();
        if (lockConfig.getClusterServer() != null) {
            config.useClusterServers().setPassword(lockConfig.getPassword())
                    .addNodeAddress(lockConfig.getClusterServer().getNodeAddresses());
        } else {
            String address = "redis://"+lockConfig.getHost() + ":" + lockConfig.getPort();
            config.useSingleServer().setAddress(address)
                    .setDatabase(lockConfig.getDatabase())
                    .setPassword(lockConfig.getPassword())
                    .setClientName("dlock")

            ;
        }
        Codec codec = (Codec) ClassUtils.forName(lockConfig.getCodec(), ClassUtils.getDefaultClassLoader()).newInstance();
        config.setCodec(codec);
        config.setEventLoopGroup(new NioEventLoopGroup());
        return Redisson.create(config);
    }

    @Bean
    public LockInfoProvider lockInfoProvider() {
        return new LockInfoProvider();
    }

    @Bean
    public BusinessKeyProvider businessKeyProvider() {
        return new BusinessKeyProvider();
    }

    @Bean
    public LockFactory lockFactory() {
        return new LockFactory();
    }
}
