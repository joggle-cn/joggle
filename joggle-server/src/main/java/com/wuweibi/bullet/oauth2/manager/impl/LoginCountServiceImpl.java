package com.wuweibi.bullet.oauth2.manager.impl;

import com.wuweibi.bullet.oauth2.config.SystemAuthStrategyConfig;
import com.wuweibi.bullet.oauth2.manager.LoginCountService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

@Service
public class LoginCountServiceImpl implements LoginCountService {

	@Resource()
	private RedisTemplate<String, Integer> redisTemplate;

	String KEY = "login:count:%s";

	@Resource
	private SystemAuthStrategyConfig systemAuthStrategyConfig;

	@Override
	public void countPlus1(String username) {
		String key = String.format(KEY, username);
		redisTemplate.opsForValue().increment(key);
		redisTemplate.expire(key, getFaildWaitTime(), TimeUnit.MINUTES);
	}

	@Override
	public int getFaildWaitTime() { // 单位分钟
		return systemAuthStrategyConfig.getFaildWaitTime();
	}

	@Override
	public int getLoginTotalCount() {
		return systemAuthStrategyConfig.getLoginTotalCount();
	}

	@Override
	public int getCount(String username) {
		String key = String.format(KEY, username);
		Integer count = redisTemplate.opsForValue().get(key);
		if(count == null){
			return 0;
		}
		return count;
	}

	@Override
	public int getRemainLoginCount(String username) {
		String key = String.format(KEY, username);
		Integer count = redisTemplate.opsForValue().get(key);
		if(count == null){
			return 0;
		}
		return systemAuthStrategyConfig.getLoginTotalCount() - count;
	}

	@Override
	public int getWaitedTime(String username) {
		String key = String.format(KEY, username);
		Long expire = redisTemplate.getExpire(key, TimeUnit.SECONDS);
		if(expire == null){
			return 0;
		}
		return expire.intValue();
	}

	@Override
	public void clean(String username) {
		String key = String.format(KEY, username);
		redisTemplate.delete(key);
	}
}
