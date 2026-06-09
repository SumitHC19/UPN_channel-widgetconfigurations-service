package com.aonhewitt.upoint.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;

import com.aonhewitt.upoint.cache.config.MultiRedisConfiguration;
import com.aonhewitt.upoint.cache.config.RedisConfigurations;

public class MockRedisCacheUtilMultiRedis {

	
	public boolean switchToSecondaryRedisInstance() {
		return true;
	}

	
	public static String getAlias() {
		return "INT";
	}

	
	public static Map<String, JedisConnectionFactory> getJedisConnectionFactories(
			MultiRedisConfiguration multiRedisConfiguration) {
		Map<String, JedisConnectionFactory> mapOfFactories = new HashMap<String, JedisConnectionFactory>();
		mapOfFactories.put("default", new JedisConnectionFactory());
		mapOfFactories.put("INT", new JedisConnectionFactory());
		return mapOfFactories;
	}
	
	public static Map<String, JedisConnectionFactory> getJedisConnectionFactories(String applicationName,
			List<RedisConfigurations> redisConfigurations) {
		Map<String, JedisConnectionFactory> mapOfFactories = new HashMap<String, JedisConnectionFactory>();
		mapOfFactories.put("default", new JedisConnectionFactory());
		mapOfFactories.put("INT", new JedisConnectionFactory());
		return mapOfFactories;
	}

}
