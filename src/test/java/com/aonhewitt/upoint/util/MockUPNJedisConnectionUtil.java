package com.aonhewitt.upoint.util;

import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;

import com.aonhewitt.upoint.cache.config.RedisConfigurations;

public class MockUPNJedisConnectionUtil {

	//@Mock
	public static JedisConnectionFactory createJedisConnectionFactory(RedisConfigurations properties,
			String applicationName, String alias) {
		return new JedisConnectionFactory();
	}
}
