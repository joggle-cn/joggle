package com.wuweibi.bullet.system.api_key.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wuweibi.bullet.system.api_key.entity.UserApiKey;

public interface UserApiKeyService extends IService<UserApiKey> {

    /**
     * 验证 API Key 并返回用户ID
     */
    Long authenticate(String apiKey);

    /**
     * 生成新的 API Key
     */
    UserApiKey generateKey(Long userId, String name);
}
