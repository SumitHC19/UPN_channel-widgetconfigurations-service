package com.aonhewitt.upoint.util;

import org.springframework.data.redis.core.RedisTemplate;

import com.aonhewitt.upoint.cache.config.provider.ICache;
import com.aonhewitt.upoint.cache.config.provider.impl.RedisCacheObjectProviderImpl;
import com.aonhewitt.upoint.cache.exception.BaseCacheException;
import com.aonhewitt.upoint.cache.util.DistributedCacheUtil;

public class MockDistributedCacheUtil {
	private static DistributedCacheUtil  distriburedCacheUtil = null;
	DistributedCacheUtil getInstance() {
		 ICache redisCacheProvider = new RedisCacheObjectProviderImpl(new RedisTemplate<>());
		// RedisTemplate redisTemplateForObject =  new RedisTemplate<>();
		distriburedCacheUtil = new DistributedCacheUtil(redisCacheProvider, null);
		return distriburedCacheUtil;
	}

	
	String getDCKeyNamespace(String aLineage, String aMdlId) {

		return aLineage + ":" + aMdlId + ":";
	}
	
	
	Object getCachedObject(String aCacheKey, String aTargetId) throws BaseCacheException {
			return "hasbro resp object";

	}
}


	
