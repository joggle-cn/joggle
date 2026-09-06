package com.wuweibi.bullet.ertificate.service;


import com.wuweibi.bullet.alias.CacheCode;
import com.wuweibi.bullet.config.properties.JoggleProperties;
import com.wuweibi.bullet.config.swagger.annotation.WebApi;
import com.wuweibi.bullet.entity.api.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import jakarta.annotation.Resource;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 *  acme验证接口
 * @author marker
 * @since 2021-11-07 14:17:51
 */
@Slf4j
@WebApi
@Tag(name = "acme验证接口")
@RestController
@RequestMapping("/inner/open/.well-known/acme-challenge")
public class AcmeOpenInnerController {
    /**
     * 服务对象
     */
    @Resource
    private RedisTemplate<String,String> redisTemplate;

    @Resource
    private JoggleProperties joggleProperties;


    /**
     * 验证内部接口
     * @return 新增结果
     */
    @PostMapping("/{token}")
    public R validToken(@PathVariable("token") String token , @RequestHeader String authorization) {
        if (!joggleProperties.getAdminApiToken().equals(authorization)) {
            log.warn("[上报流量]无接口调用权限token:{}", authorization);
            return R.fail("无接口调用权限");
        }
        String key = String.format(CacheCode.ACME_CHALLENGE_KEY,token);
        String content = redisTemplate.opsForValue().get(key);
        return R.ok(content);
    }




}
