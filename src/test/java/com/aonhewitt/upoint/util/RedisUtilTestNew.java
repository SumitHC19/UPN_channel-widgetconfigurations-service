package com.aonhewitt.upoint.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mockStatic;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamClass;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;

import com.aonhewitt.upoint.cache.config.LettuceRedisConfiguration;
import com.aonhewitt.upoint.cache.config.MultiRedisConfiguration;
import com.aonhewitt.upoint.cache.config.RedisConfigurations;
import com.aonhewitt.upoint.cache.config.SessionDBMultiRedisConfiguration;
import com.aonhewitt.upoint.cache.config.UPointRedisProperties;
import com.aonhewitt.upoint.cache.config.UPointRedisProperties.Cluster;
import com.aonhewitt.upoint.cache.config.UPointRedisProperties.Pool;
import com.aonhewitt.upoint.cache.config.UPointRedisProperties.Sentinel;
import com.aonhewitt.upoint.cache.util.RedisCacheConfigUtil;
import com.aonhewitt.upoint.serializer.NewConfigurableObjectInputStream;
import com.aonhewitt.upoint.serializer.UpointDeserializer;

@ExtendWith(SpringExtension.class)
public class RedisUtilTestNew {

	

	@Mock
	MultiRedisConfiguration multiRedisConfiguration;

	@Mock
	SessionDBMultiRedisConfiguration sesionDBMultiRedisConfiguration;

	@Mock
	private LettuceRedisConfiguration lettuceRedisConfiguration;

	@Mock
	UPointRedisProperties upointRedisProperties;
	
	@Mock
	private RedisProperties redisProperties;

	@InjectMocks
	RedisUtils util;

	@BeforeEach
	public void setup() throws Exception {
		ReflectionTestUtils.setField(util, "applicationName", "applicationName");
		ReflectionTestUtils.setField(util, "sesionMultiRedisEnable", false);
		ReflectionTestUtils.setField(util, "redisClientType", "jedis");
		
		 Mockito.when(upointRedisProperties.getPassword()).thenReturn("123");
		 Mockito.when(upointRedisProperties.isSsl()).thenReturn(true);
		 Mockito.when(upointRedisProperties.getTimeout()).thenReturn(1);
		 Mockito.when(upointRedisProperties.getHost()).thenReturn("locahost");
		 Mockito.when(upointRedisProperties.getPort()).thenReturn(8080);
		 
		 Mockito.when(upointRedisProperties.isSentinelSetUp()).thenReturn(false);
		 
		 
		 
		 Sentinel s = new Sentinel();
			s.setMaster("m1");
			s.setNodes("123:456");
		 Mockito.when(upointRedisProperties.getSentinel()).thenReturn(s);
		 
		 
	}

	@Test
	public void testRedisTemplateForObject() {

		JedisConnectionFactory connectionFactory = new JedisConnectionFactory();
		connectionFactory.afterPropertiesSet();
		connectionFactory.start();
		upointRedisProperties.setPassword("123");
		upointRedisProperties.setTimeout(10);
		upointRedisProperties.setSsl(true);
		upointRedisProperties.setSentinelSetUp(true);
		upointRedisProperties.setHost("localhost");
		upointRedisProperties.setPort(8080);
		JedisConnectionFactory jedisConnectionFactory = Mockito.mock(JedisConnectionFactory.class);

		Sentinel s = new Sentinel();
		s.setMaster("m1");
		s.setNodes("123:456");
		upointRedisProperties.setSentinel(s);
		upointRedisProperties.setPool(new Pool());
		util.setUpointRedisProperties(upointRedisProperties);
		Mockito.doNothing().when(jedisConnectionFactory).afterPropertiesSet();

		RedisTemplate<String, Object> temp = util.redisTemplateForObject(jedisConnectionFactory);
		assertEquals(true, temp.getKeySerializer() instanceof StringRedisSerializer);

		try (MockedStatic<RedisCacheConfigUtil> redisCacheConfigUtilMockedStatic = mockStatic(
				RedisCacheConfigUtil.class)) {
			redisCacheConfigUtilMockedStatic
					.when(() -> RedisCacheConfigUtil.getClientConnectionName(Mockito.anyString(), Mockito.anyString()))
					.thenReturn("connectionName");

			util.redisTemplateForMapSecondary();
			
			util.redisTemplateforMap(jedisConnectionFactory);
		}
	}
	@Test
	public void testNewConfigurableObject() {

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		boolean success = true;

		try {

			ObjectOutputStream oos = new ObjectOutputStream(baos);

			oos.writeObject("RedisUtilsTest");

			oos.flush();
			oos.close();

			InputStream is = new ByteArrayInputStream(baos.toByteArray());

			ClassLoader classLoader = this.getClass().getClassLoader();

			NewConfigurableObjectInputStream objectInputStream = new NewConfigurableObjectInputStream(is, classLoader,
					true);

			objectInputStream.resolveClass(ObjectStreamClass.lookup(String.class));

			objectInputStream.close();

		} catch (IOException e) {
			success = false;
		} catch (ClassNotFoundException e) {
			success = false;
		}

		assertEquals(true, success);

	}
	
	@Test
	public void testUpointDeserializerFailed() {

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		boolean success = true;

		try {

			baos.write("RedisUtilsTest".getBytes(Charset.defaultCharset()));

			InputStream is = new ByteArrayInputStream(baos.toByteArray());

			UpointDeserializer deserializer = new UpointDeserializer();
			deserializer.deserialize(is);

		} catch (IOException e) {
			success = false;
		}

		assertEquals(false, success);
	}
	@Test
	public void testUpointDeserializer() {

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		boolean success = true;

		try {

			ObjectOutputStream oos = new ObjectOutputStream(baos);

			oos.writeObject("RedisUtilsTest");

			oos.flush();
			oos.close();

			InputStream is = new ByteArrayInputStream(baos.toByteArray());

			UpointDeserializer deserializer = new UpointDeserializer();
			deserializer.deserialize(is);

		} catch (IOException e) {
			success = false;
		}

		assertEquals(true, success);
	}
	
	@Test
	public void testMultiRedisTemplateForObject() {
		List<RedisConfigurations> instances = new ArrayList<RedisConfigurations>();
		instances.add(new RedisConfigurations());
		sesionDBMultiRedisConfiguration.setInstances(instances);
		util.setSessionDBMultiRedisConfiguration(sesionDBMultiRedisConfiguration);
		// new MockRedisCacheUtilMultiRedis();
		try (MockedStatic<RedisCacheConfigUtil> redisCacheConfigUtilMockedStatic = mockStatic(
				RedisCacheConfigUtil.class)) {
			redisCacheConfigUtilMockedStatic
					.when(() -> RedisCacheConfigUtil.switchToSecondaryRedisInstance())
					.thenReturn(true);
			redisCacheConfigUtilMockedStatic
			.when(() -> RedisCacheConfigUtil.getAlias())
			.thenReturn("INT");
			redisCacheConfigUtilMockedStatic
			.when(() -> RedisCacheConfigUtil.getAlias())
			.thenReturn("INT");
			
			Map<String, JedisConnectionFactory> mapOfFactories = new HashMap<String, JedisConnectionFactory>();
			mapOfFactories.put("default", new JedisConnectionFactory());
			mapOfFactories.put("INT", new JedisConnectionFactory());
			
			redisCacheConfigUtilMockedStatic
			.when(() -> RedisCacheConfigUtil.getJedisConnectionFactories(Mockito.any(MultiRedisConfiguration.class)))
			.thenReturn(mapOfFactories);
			
			redisCacheConfigUtilMockedStatic
			.when(() -> RedisCacheConfigUtil.getJedisConnectionFactories(Mockito.anyString(), Mockito.anyList()))
			.thenReturn(mapOfFactories);
			
		Map<String, RedisTemplate<String, Object>> templateObject = util.getRedisSecondaryTemplatesOfObjects();
		Map<String, RedisTemplate<String, Map<Object, Object>>> templateMap = util.getRedisSecondaryTemplatesOfMap();
		assertEquals(true, templateObject.get("default").getKeySerializer() instanceof StringRedisSerializer);
		assertEquals(true, templateMap.get("default").getKeySerializer() instanceof StringRedisSerializer);
		
		Map<String, RedisTemplate<String, Object>> configemplateObject = util.getSessionRedisSecondaryTemplatesOfObjects();
		Map<String, RedisTemplate<String, Map<Object, Object>>> configTemplateMap = util.getSessionRedisSecondaryTemplatesOfMap();
		assertEquals(true, configemplateObject.get("default").getKeySerializer() instanceof StringRedisSerializer);
		assertEquals(true, configTemplateMap.get("default").getKeySerializer() instanceof StringRedisSerializer);
		
		ReflectionTestUtils.setField(util, """
				sesionMultiRedisEnable\
				""", true);
		Map<String, RedisTemplate<String, Object>> configemplateObject1 = util.getSessionRedisSecondaryTemplatesOfObjects();
		Map<String, RedisTemplate<String, Map<Object, Object>>> configTemplateMap1 = util.getSessionRedisSecondaryTemplatesOfMap();
		assertEquals(true, configemplateObject1.get("default").getKeySerializer() instanceof StringRedisSerializer);
		assertEquals(true, configTemplateMap1.get("default").getKeySerializer() instanceof StringRedisSerializer);
		
	}
	}
	
	//@Test
	public void testRedisTemplateForObjectWithAELockTrue() {
		
		JedisConnectionFactory jedisConnectionFactory = Mockito.mock(JedisConnectionFactory.class);
		upointRedisProperties.setPassword("123");
		upointRedisProperties.setTimeout(10);
		upointRedisProperties.setSsl(true);
		upointRedisProperties.setSentinelSetUp(true);
		upointRedisProperties.setHost("localhost");
		upointRedisProperties.setPort(8080);
		//Mockito.doReturn(null).when(ReflectionTestUtils.invokeMethod(jedisConnectionFactory, "createPool"));
		//Mockito.doReturn(null).when(ReflectionTestUtils.invokeMethod(jedisConnectionFactory, "createCluster"));
		
		// fix for find bug
		Sentinel s=new Sentinel();
		s.setMaster("m1");
		s.setNodes("123:456");
		upointRedisProperties.setSentinel(s);
		upointRedisProperties.setPool(new Pool());
		util.setUpointRedisProperties(upointRedisProperties);
		
		upointRedisProperties.setSentinelSetUp(false);
		
		ReflectionTestUtils.setField(util, "aeLock", true);
		
		
		util.redisTemplateForMapSecondary();
		util.redisTemplateForObjectSecondary();
		
	}
	//@Test
	public void testRedisTemplateForObjectWithAELockTrueWithCluster() {
		
		JedisConnectionFactory jedisConnectionFactory = new JedisConnectionFactory();
		upointRedisProperties.setPassword("123");
		upointRedisProperties.setTimeout(10);
		upointRedisProperties.setSsl(true);
		upointRedisProperties.setSentinelSetUp(false);
		upointRedisProperties.setHost("localhost");
		upointRedisProperties.setPort(8080);

		
		 jedisConnectionFactory = Mockito.mock(JedisConnectionFactory.class);
		//Mockito.doReturn(new JedisCluster(new HostAndPort("localhost", 8080))).when(ReflectionTestUtils.invokeMethod(jedisConnectionFactory, "createCluster"));
		// fix for find bug

		upointRedisProperties.setPool(new Pool());
		
		Cluster c = new Cluster();
		List<String> nodes = new ArrayList<String>();
		nodes.add("123:456");
		c.setNodes(nodes);
		c.setMaxRedirects(0);
		upointRedisProperties.setCluster(c);
		util.setUpointRedisProperties(upointRedisProperties);
		
		
		upointRedisProperties.setSentinelSetUp(false);
		
		ReflectionTestUtils.setField(util, "aeLock", true);
		
		
		util.redisTemplateForMapSecondary();
		util.redisTemplateForObjectSecondary();
		
	}
	
	@Test
	public void testRedisTemplateForObjectWithAELockFalseWithCluster() {
			upointRedisProperties.setPool(new Pool());
		
		Cluster c = new Cluster();
		List<String> nodes = new ArrayList<String>();
		nodes.add("123:456");
		c.setNodes(nodes);
		c.setMaxRedirects(0);
		upointRedisProperties.setCluster(c);
		util.setUpointRedisProperties(upointRedisProperties);
		
		util.redisTemplateForMapSecondary();
		util.redisTemplateForObjectSecondary();
		
		upointRedisProperties.setSentinelSetUp(false);
		
		ReflectionTestUtils.setField(util, "aeLock", false);
		
		
		util.redisTemplateForMapSecondary();
		util.redisTemplateForObjectSecondary();
		
	}
	
	@Test
	public void redisTemplateForMapSecondaryTest() {
		Pool pool= new Pool();
		pool.setEvictorshutdowntimeoutmillis(1);
		pool.setMinEvictableIdleTimeMillis(1);
		pool.setSoftMinEvictableIdleTimeMillis(2);
		pool.setTimeBetweenEvictionRunsMillis(5);
		pool.setNumTestsPerEvictionRun(4);
		pool.setEvictorshutdowntimeoutmillis(12);
		
		
		
		Mockito.when(upointRedisProperties.getPool()).thenReturn(pool);
		util.redisTemplateForMapSecondary();
	}
	
	@Test
	public void redisTemplateForObjectSecondaryExceptionTest() {
		Pool pool= new Pool();
		pool.setEvictorshutdowntimeoutmillis(1);
		pool.setMinEvictableIdleTimeMillis(1);
		pool.setSoftMinEvictableIdleTimeMillis(2);
		pool.setTimeBetweenEvictionRunsMillis(5);
		pool.setNumTestsPerEvictionRun(4);
		pool.setEvictorshutdowntimeoutmillis(12);
		
		try (MockedStatic<RedisCacheConfigUtil> redisCacheConfigUtilUtilMockedStatic = mockStatic(
				RedisCacheConfigUtil.class);) {

			redisCacheConfigUtilUtilMockedStatic.when(() -> RedisCacheConfigUtil.
					getClientConnectionName(Mockito.anyString(),Mockito.anyString()))
					.thenThrow(new NullPointerException());
		
		Mockito.when(upointRedisProperties.getPool()).thenReturn(pool);
		util.redisTemplateForObjectSecondary();
	}}

	
}