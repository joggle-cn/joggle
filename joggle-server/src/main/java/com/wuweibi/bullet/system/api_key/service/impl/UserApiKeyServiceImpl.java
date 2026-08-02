package com.wuweibi.bullet.system.api_key.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wuweibi.bullet.system.api_key.entity.UserApiKey;
import com.wuweibi.bullet.system.api_key.mapper.UserApiKeyMapper;
import com.wuweibi.bullet.system.api_key.service.UserApiKeyService;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Date;
import java.util.HexFormat;

@Service
public class UserApiKeyServiceImpl extends ServiceImpl<UserApiKeyMapper, UserApiKey> implements UserApiKeyService {

    private static final String KEY_PREFIX = "jgl_";
    private static final int KEY_LENGTH = 32;
    private static final SecureRandom RANDOM = new SecureRandom();

    @Override
    public Long authenticate(String apiKey) {
        LambdaQueryWrapper<UserApiKey> qw = new LambdaQueryWrapper<UserApiKey>()
                .eq(UserApiKey::getApiKey, apiKey)
                .eq(UserApiKey::getEnabled, true);
        UserApiKey key = baseMapper.selectOne(qw);
        if (key == null) {
            return null;
        }
        key.setLastUsedAt(new Date());
        baseMapper.updateById(key);
        return key.getUserId();
    }

    @Override
    public UserApiKey generateKey(Long userId, String name) {
        UserApiKey key = new UserApiKey();
        key.setUserId(userId);
        key.setName(name);
        key.setApiKey(generateApiKey());
        key.setEnabled(true);
        key.setCreateTime(new Date());
        baseMapper.insert(key);
        return key;
    }

    private String generateApiKey() {
        byte[] bytes = new byte[KEY_LENGTH / 2];
        RANDOM.nextBytes(bytes);
        return KEY_PREFIX + HexFormat.of().formatHex(bytes);
    }
}
