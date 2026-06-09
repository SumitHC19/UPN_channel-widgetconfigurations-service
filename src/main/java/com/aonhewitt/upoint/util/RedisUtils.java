package com.aonhewitt.upoint.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.serializer.support.DeserializingConverter;
import org.springframework.core.serializer.support.SerializingConverter;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisSentinelConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.util.ObjectUtils;

import com.alight.logging.helpers.InfoTypeLogEventHelper;
import com.aonhewitt.logging.events.ErrorLogEvent;
import com.aonhewitt.logging.helpers.ErrorLogEventHelper;
import com.aonhewitt.upoint.cache.config.LettuceRedisConfiguration;
import com.aonhewitt.upoint.cache.config.MultiRedisConfiguration;
import com.aonhewitt.upoint.cache.config.RedisCacheConstants;
import com.aonhewitt.upoint.cache.config.SessionDBMultiRedisConfiguration;
import com.aonhewitt.upoint.cache.config.UPointRedisProperties;
import com.aonhewitt.upoint.cache.util.RedisCacheConfigUtil;
import com.aonhewitt.upoint.cache.util.UPNLettuceConnectionUtil;
import com.aonhewitt.upoint.serializer.UpointDeserializer;

import redis.clients.jedis.JedisPoolConfig;
/**
 * 
 * @author Yogesh Mittal RedisUtil provides jedisConnectionFactory and different
 *         RedisTemplate beans required for distributed caching
 */

@Configuration
@EnableConfigurationProperties({UPointRedisProperties.class,MultiRedisConfiguration.class,SessionDBMultiRedisConfiguration.class,
	LettuceRedisConfiguration.class,RedisProperties.class})

public class RedisUtils {
	
	 @Value("${AE_LOCK:true}")
	 private boolean aeLock;
	 
	 @Value("${spring.application.name}")
	 private String applicationName;
	 
	 
	 @Value("${upoint.session-redis.enabled:false}")
	 private boolean sesionMultiRedisEnable;
	 
	 @Value("${spring.data.redis.client-type:jedis}")
     private String redisClientType;
	
	@Autowired
	@Deprecated
	UPointRedisProperties upointRedisProperties;
	
	@Autowired(required = false)
	MultiRedisConfiguration multiRedisConfiguration;
	
	
	@Autowired(required = false)
	SessionDBMultiRedisConfiguration sesionDBMultiRedisConfiguration;
	
	@Autowired(required=false)
    private LettuceRedisConfiguration lettuceRedisConfiguration;
	
	@Autowired
	private RedisProperties redisProperties;

	private static final String REDIS_TEMPLATE_OBJECT_SECONDARY= "redisTemplateforObjectSecondary"; 
	private static final String REDIS_TEMPLATE_MAP_SECONDARY= "redisTemplateforMapSecondary"; 
	private static final String REDIS_TEMPLATE_OBJECT= "redisTemplateforObject"; 
	private static final String REDIS_TEMPLATE_MAP= "redisTemplateforMap"; 

	private static final String SECONDARY_REDIS_SETUP_ENABLED= "upoint.redis.enabled";


	public void setUpointRedisProperties(UPointRedisProperties upointRedisProperties) {
		this.upointRedisProperties = upointRedisProperties;
	}
	
	public void setMultiRedisConfiguration(MultiRedisConfiguration multiRedisConfiguration) {
		this.multiRedisConfiguration = multiRedisConfiguration;
	}
	
	
	public void setSessionDBMultiRedisConfiguration(SessionDBMultiRedisConfiguration sessionDBMultiRedisConfiguration) {
		this.sesionDBMultiRedisConfiguration = sessionDBMultiRedisConfiguration;
	}

	@Primary
	@Bean(REDIS_TEMPLATE_OBJECT)
	public RedisTemplate<String, Object> redisTemplateForObject(RedisConnectionFactory redisConnectionFactory) {
		RedisTemplate<String, Object> template = new RedisTemplate<String, Object>();
		template.setConnectionFactory(createRedisConnectionFactory(redisConnectionFactory, lettuceRedisConfiguration,redisProperties));
		template.setKeySerializer(new StringRedisSerializer());
		
		ClassLoader classloader = this.getClass().getClassLoader();
		
		Converter<Object, byte[]> serializer = new SerializingConverter();
		Converter<byte[], Object> deserializer = new DeserializingConverter(new UpointDeserializer(classloader));
		
		template.setValueSerializer(new JdkSerializationRedisSerializer(serializer , deserializer));
		
		return template;
	}

	@Primary
	@Bean(REDIS_TEMPLATE_MAP)
	public RedisTemplate<String, Map<Object, Object>> redisTemplateforMap(RedisConnectionFactory redisConnectionFactory) {
		RedisTemplate<String, Map<Object, Object>> template = new RedisTemplate<String, Map<Object, Object>>();
		template.setConnectionFactory(createRedisConnectionFactory(redisConnectionFactory, lettuceRedisConfiguration,redisProperties));
		template.setKeySerializer(new StringRedisSerializer());
		
		ClassLoader classloader = this.getClass().getClassLoader();
		
		Converter<Object, byte[]> serializer = new SerializingConverter();
		Converter<byte[], Object> deserializer = new DeserializingConverter(new UpointDeserializer(classloader));
		
		template.setValueSerializer(new JdkSerializationRedisSerializer(serializer , deserializer));
		return template;
	}
	
	
	@Deprecated
	@Bean(REDIS_TEMPLATE_OBJECT_SECONDARY)
	@ConditionalOnProperty(SECONDARY_REDIS_SETUP_ENABLED)
	public RedisTemplate<String, Object> redisTemplateForObjectSecondary() {
		
		JedisConnectionFactory jedisConnectionFactory= getJedisConnectionFactorySecondary();
		// create redis template
		RedisTemplate<String, Object> template = new RedisTemplate<String, Object>();
		template.setConnectionFactory(jedisConnectionFactory);
		template.setKeySerializer(new StringRedisSerializer());
		
		ClassLoader classloader = this.getClass().getClassLoader();
		
		Converter<Object, byte[]> serializer = new SerializingConverter();
		Converter<byte[], Object> deserializer = new DeserializingConverter(new UpointDeserializer(classloader));
		
		template.setValueSerializer(new JdkSerializationRedisSerializer(serializer , deserializer));
		
		return template;
	}


	@Deprecated
	@Bean(REDIS_TEMPLATE_MAP_SECONDARY)
	@ConditionalOnProperty(SECONDARY_REDIS_SETUP_ENABLED)
	public RedisTemplate<String, Map<Object, Object>> redisTemplateForMapSecondary() {
		
		JedisConnectionFactory jedisConnectionFactory= getJedisConnectionFactorySecondary();
		// create redis template
		RedisTemplate<String, Map<Object, Object>> template = new RedisTemplate<String, Map<Object, Object>>();
		template.setConnectionFactory(jedisConnectionFactory);
		template.setKeySerializer(new StringRedisSerializer());
		
		ClassLoader classloader = this.getClass().getClassLoader();
		
		Converter<Object, byte[]> serializer = new SerializingConverter();
		Converter<byte[], Object> deserializer = new DeserializingConverter(new UpointDeserializer(classloader));
		
		template.setValueSerializer(new JdkSerializationRedisSerializer(serializer , deserializer));
		
		return template;
	}
	
	
	@Bean(RedisCacheConfigUtil.REDIS_TEMPLATE_OBJECTS_OF_SECONDARY)
	@ConditionalOnProperty(SECONDARY_REDIS_SETUP_ENABLED)
	public Map<String, RedisTemplate<String, Object>> getRedisSecondaryTemplatesOfObjects() {
		
		Map<String, JedisConnectionFactory> mapOfFactories = RedisCacheConfigUtil.getJedisConnectionFactories(multiRedisConfiguration);
		Map<String, RedisTemplate<String, Object>> redisSecondaryTemplatesOfObjects = new HashMap();

		 List<String> aliasList = new ArrayList<String>();	
		mapOfFactories.entrySet().forEach( entry->{
			String alias = entry.getKey();
			aliasList.add(alias.toLowerCase());
			JedisConnectionFactory jedisConnectionFactory = entry.getValue();

			ClassLoader classloader = this.getClass().getClassLoader();
			Converter<Object, byte[]> serializer = new SerializingConverter();
			Converter<byte[], Object> deserializer = new DeserializingConverter(
					new UpointDeserializer(classloader));

			RedisTemplate<String, Object> template = new RedisTemplate<String, Object>();
			template.setConnectionFactory(jedisConnectionFactory);
			template.setKeySerializer(new StringRedisSerializer());
			template.setValueSerializer(new JdkSerializationRedisSerializer(serializer, deserializer));
			template.afterPropertiesSet();
			redisSecondaryTemplatesOfObjects.put(alias, template);
			
		});	
		CommonUtils.setAliasList(aliasList);
		return redisSecondaryTemplatesOfObjects;
	}

	@Bean(RedisCacheConfigUtil.REDIS_TEMPLATE_MAPS_OF_SECONDARY)
	@ConditionalOnProperty(SECONDARY_REDIS_SETUP_ENABLED)
	public Map<String, RedisTemplate<String, Map<Object, Object>>> getRedisSecondaryTemplatesOfMap() {

		Map<String, JedisConnectionFactory> mapOfFactories = RedisCacheConfigUtil.getJedisConnectionFactories(multiRedisConfiguration);
		Map<String, RedisTemplate<String, Map<Object, Object>>> redisSecondaryTemplatesOfMap = new HashMap();
		 List<String> aliasList = new ArrayList<String>();	
		 
				
		mapOfFactories.entrySet().forEach( entry->{
			
			String alias = entry.getKey();
			aliasList.add(alias.toLowerCase());
			JedisConnectionFactory jedisConnectionFactory = entry.getValue();

			ClassLoader classloader = this.getClass().getClassLoader();
			Converter<Object, byte[]> serializer = new SerializingConverter();
			Converter<byte[], Object> deserializer = new DeserializingConverter(
					new UpointDeserializer(classloader));

			RedisTemplate<String, Map<Object, Object>> mapTemplate = new RedisTemplate<String, Map<Object, Object>>();
			mapTemplate.setConnectionFactory(jedisConnectionFactory);
			mapTemplate.setKeySerializer(new StringRedisSerializer());
			mapTemplate.setValueSerializer(new JdkSerializationRedisSerializer(serializer, deserializer));
			mapTemplate.afterPropertiesSet();
			redisSecondaryTemplatesOfMap.put(alias, mapTemplate);
			
			
		});		
		CommonUtils.setAliasList(aliasList);
		return redisSecondaryTemplatesOfMap;
	}

	@Bean(RedisCacheConstants.SESSION_REDIS_TEMPLATE_OBJECTS_OF_SECONDARY)
	public Map<String, RedisTemplate<String, Object>> getSessionRedisSecondaryTemplatesOfObjects() {
		Map<String, JedisConnectionFactory> mapOfFactories=null;
		if(sesionMultiRedisEnable){
			mapOfFactories = RedisCacheConfigUtil
					.getJedisConnectionFactories(applicationName,sesionDBMultiRedisConfiguration.getInstances());
		}else{
			mapOfFactories = RedisCacheConfigUtil.getJedisConnectionFactories(multiRedisConfiguration);
		}
		Map<String, RedisTemplate<String, Object>> redisSecondaryTemplatesOfObjects = new HashMap();

		List<String> aliasList = new ArrayList<String>();
		mapOfFactories.entrySet().forEach(entry -> {
			String alias = entry.getKey();
			aliasList.add(alias.toLowerCase());
			JedisConnectionFactory jedisConnectionFactory = entry.getValue();

			ClassLoader classloader = this.getClass().getClassLoader();
			Converter<Object, byte[]> serializer = new SerializingConverter();
			Converter<byte[], Object> deserializer = new DeserializingConverter(new UpointDeserializer(classloader));

			RedisTemplate<String, Object> template = new RedisTemplate<String, Object>();
			template.setConnectionFactory(jedisConnectionFactory);
			template.setKeySerializer(new StringRedisSerializer());
			template.setValueSerializer(new JdkSerializationRedisSerializer(serializer, deserializer));
			template.afterPropertiesSet();
			redisSecondaryTemplatesOfObjects.put(alias, template);

		});
		CommonUtils.setAliasList(aliasList);
		return redisSecondaryTemplatesOfObjects;
	}

	@Bean(RedisCacheConstants.SESSION_REDIS_TEMPLATE_MAPS_OF_SECONDARY)
	public Map<String, RedisTemplate<String, Map<Object, Object>>> getSessionRedisSecondaryTemplatesOfMap() {
		Map<String, JedisConnectionFactory> mapOfFactories=null;
		if(sesionMultiRedisEnable){
			mapOfFactories = RedisCacheConfigUtil
					.getJedisConnectionFactories(applicationName,sesionDBMultiRedisConfiguration.getInstances());
		}else{
			mapOfFactories = RedisCacheConfigUtil.getJedisConnectionFactories(multiRedisConfiguration);
		}
		Map<String, RedisTemplate<String, Map<Object, Object>>> redisSecondaryTemplatesOfMap = new HashMap();
		List<String> aliasList = new ArrayList<String>();

		mapOfFactories.entrySet().forEach(entry -> {

			String alias = entry.getKey();
			aliasList.add(alias.toLowerCase());
			JedisConnectionFactory jedisConnectionFactory = entry.getValue();

			ClassLoader classloader = this.getClass().getClassLoader();
			Converter<Object, byte[]> serializer = new SerializingConverter();
			Converter<byte[], Object> deserializer = new DeserializingConverter(new UpointDeserializer(classloader));

			RedisTemplate<String, Map<Object, Object>> mapTemplate = new RedisTemplate<String, Map<Object, Object>>();
			mapTemplate.setConnectionFactory(jedisConnectionFactory);
			mapTemplate.setKeySerializer(new StringRedisSerializer());
			mapTemplate.setValueSerializer(new JdkSerializationRedisSerializer(serializer, deserializer));
			mapTemplate.afterPropertiesSet();
			redisSecondaryTemplatesOfMap.put(alias, mapTemplate);

		});
		CommonUtils.setAliasList(aliasList);
		return redisSecondaryTemplatesOfMap;
	}

	@Deprecated
	private JedisConnectionFactory getJedisConnectionFactorySecondary(){
		// create jedis connection factory
		JedisConnectionFactory jedisConnectionFactory= createJedisConnectionFactory();
	  if(!aeLock)
		{
			if (!upointRedisProperties.isSentinelSetUp() && !ObjectUtils.isEmpty(upointRedisProperties.getHost())) {
				jedisConnectionFactory.setHostName(this.upointRedisProperties.getHost());
			}

			if (!upointRedisProperties.isSentinelSetUp() && upointRedisProperties.getPort() != 0) {
				jedisConnectionFactory.setPort(this.upointRedisProperties.getPort());
			}
		}

		if (!ObjectUtils.isEmpty(this.upointRedisProperties.getPassword())) {
			jedisConnectionFactory.setPassword(this.upointRedisProperties.getPassword());
		}
		
		if (this.upointRedisProperties.isSsl()) {
			jedisConnectionFactory.setUseSsl(true);
		}
		jedisConnectionFactory.setDatabase(this.upointRedisProperties.getDatabase());
		if (this.upointRedisProperties.getTimeout() > 0) {
			jedisConnectionFactory.setTimeout(this.upointRedisProperties.getTimeout());
		}
		 jedisConnectionFactory.setUsePool(true);
		 //Set client name as application name
		 try{
		   jedisConnectionFactory.setClientName(RedisCacheConfigUtil.getClientConnectionName(applicationName,"secondary"));
		 
		 }
		 catch(Exception e){
			 ErrorLogEventHelper.logErrorEvent(RedisUtils.class.getName(), "NO FUNCTIONAL IMPACT: unable to set client name in Jedis Connection Factory under service: channel-widgetconfigurations ", 
						"getJedisConnectionFactorySecondary()",
						"", "", ErrorLogEvent.ERROR_SEVERITY);
		 }
		 jedisConnectionFactory.afterPropertiesSet();
		 return jedisConnectionFactory;
	}
	
	@Deprecated
	private JedisConnectionFactory createJedisConnectionFactory() {
	
		JedisPoolConfig poolConfig = !ObjectUtils.isEmpty(this.upointRedisProperties.getPool())
				? jedisPoolConfig() : new JedisPoolConfig();
		if (!aeLock) {
			if (upointRedisProperties.isSentinelSetUp()) {

				if (!ObjectUtils.isEmpty(getSentinelConfig())) {
					return new JedisConnectionFactory(getSentinelConfig(), poolConfig);
				}
				if (!ObjectUtils.isEmpty(getClusterConfiguration())) {
					return new JedisConnectionFactory(getClusterConfiguration(), poolConfig);
				}

			} else {
				InfoTypeLogEventHelper.logInfoEvent(RedisUtils.class.getName(), "Sentinel Set Up is not enabled !!");
				
			}
		} else {
			if (!ObjectUtils.isEmpty(getSentinelConfig())) {
				return new JedisConnectionFactory(getSentinelConfig(), poolConfig);
			}
			if (!ObjectUtils.isEmpty(getClusterConfiguration())) {
				return new JedisConnectionFactory(getClusterConfiguration(), poolConfig);
			}

		}

		return new JedisConnectionFactory(poolConfig);
	}
	@Deprecated
	private RedisSentinelConfiguration getSentinelConfig() {

		if (!ObjectUtils.isEmpty(this.upointRedisProperties.getSentinel())) {
			
			String master = this.upointRedisProperties.getSentinel().getMaster();
			String nodesArr[]=this.upointRedisProperties.getSentinel().getNodes().split(",");
			// create sentinel configuration
			Set<String> sentinelHostAndPorts = new HashSet<String>(Arrays.asList(nodesArr));
			RedisSentinelConfiguration sentinelConfig = new RedisSentinelConfiguration(master, sentinelHostAndPorts);
			return sentinelConfig;
		}
		return null;
	}
	@Deprecated
	private RedisClusterConfiguration getClusterConfiguration() {

		if (ObjectUtils.isEmpty(this.upointRedisProperties.getCluster())) {
			return null;
		}
		RedisClusterConfiguration config = new RedisClusterConfiguration(
				this.upointRedisProperties.getCluster().getNodes());

		if (!ObjectUtils.isEmpty(this.upointRedisProperties.getCluster().getMaxRedirects())) {
			config.setMaxRedirects(this.upointRedisProperties.getCluster().getMaxRedirects());
		}
		return config;
	}
	@Deprecated	
	private JedisPoolConfig jedisPoolConfig() {
		JedisPoolConfig config = new JedisPoolConfig();
		config.setMaxTotal(this.upointRedisProperties.getPool().getMaxActive());
		config.setMaxIdle(this.upointRedisProperties.getPool().getMaxIdle());
		config.setMinIdle(this.upointRedisProperties.getPool().getMinIdle());
		config.setMaxWaitMillis(this.upointRedisProperties.getPool().getMaxWait());
		
		if(this.upointRedisProperties.getPool().getMinEvictableIdleTimeMillis() !=0){
		config.setMinEvictableIdleTimeMillis(this.upointRedisProperties.getPool().getMinEvictableIdleTimeMillis());
		}
		if(this.upointRedisProperties.getPool().getSoftMinEvictableIdleTimeMillis() !=0){
		config.setSoftMinEvictableIdleTimeMillis(this.upointRedisProperties.getPool().getSoftMinEvictableIdleTimeMillis());
		}
		if(this.upointRedisProperties.getPool().getTimeBetweenEvictionRunsMillis() !=0){
		config.setTimeBetweenEvictionRunsMillis(this.upointRedisProperties.getPool().getTimeBetweenEvictionRunsMillis());
		}
		if(this.upointRedisProperties.getPool().getNumTestsPerEvictionRun() !=0){
		config.setNumTestsPerEvictionRun(this.upointRedisProperties.getPool().getNumTestsPerEvictionRun());
		}
		if(this.upointRedisProperties.getPool().getEvictorshutdowntimeoutmillis() !=0){
		config.setEvictorShutdownTimeoutMillis(this.upointRedisProperties.getPool().getEvictorshutdowntimeoutmillis());
		}
		return config;
	}
	
	// new method to create letttuce connection factory 
    private RedisConnectionFactory createRedisConnectionFactory(RedisConnectionFactory redisConnectionFactory, 
    		LettuceRedisConfiguration lettuceRedisConfiguration, RedisProperties springDataRedisProperties)
    {
	    if(redisClientType.isEmpty() || "jedis".equalsIgnoreCase(redisClientType)) {
	    	return redisConnectionFactory;
	    }else {
	        return UPNLettuceConnectionUtil.createPrimaryLettuceConnectionFactory(redisConnectionFactory, lettuceRedisConfiguration,springDataRedisProperties,applicationName);
	    }
                    
    }

}
