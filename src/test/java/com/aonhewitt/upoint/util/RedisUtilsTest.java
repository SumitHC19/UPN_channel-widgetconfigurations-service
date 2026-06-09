/*
 * package com.aonhewitt.upoint.util;
 * 
 * import static org.junit.jupiter.api.Assertions.assertEquals;
 * 
 * import java.io.ByteArrayInputStream; import java.io.ByteArrayOutputStream;
 * import java.io.IOException; import java.io.InputStream; import
 * java.io.ObjectOutputStream; import java.io.ObjectStreamClass; import
 * java.nio.charset.Charset; import java.util.ArrayList; import java.util.List;
 * import java.util.Map;
 * 
 * import org.junit.jupiter.api.Test; import org.mockito.Mockito; import
 * org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
 * import org.springframework.data.redis.core.RedisTemplate; import
 * org.springframework.data.redis.serializer.StringRedisSerializer; import
 * org.springframework.test.util.ReflectionTestUtils;
 * 
 * import com.aonhewitt.upoint.cache.config.RedisConfigurations; import
 * com.aonhewitt.upoint.cache.config.SessionDBMultiRedisConfiguration; import
 * com.aonhewitt.upoint.cache.config.UPointRedisProperties; import
 * com.aonhewitt.upoint.cache.config.UPointRedisProperties.Cluster; import
 * com.aonhewitt.upoint.cache.config.UPointRedisProperties.Pool; import
 * com.aonhewitt.upoint.cache.config.UPointRedisProperties.Sentinel; import
 * com.aonhewitt.upoint.serializer.NewConfigurableObjectInputStream; import
 * com.aonhewitt.upoint.serializer.UpointDeserializer;
 * 
 * import redis.clients.jedis.HostAndPort; import
 * redis.clients.jedis.JedisCluster;
 * 
 * //@SuppressFBWarnings({ "DM_DEFAULT_ENCODING","DM_STRING_CTOR"}) public class
 * RedisUtilsTest {
 * 
 * RedisUtils util = new RedisUtils();
 * 
 * JedisConnectionFactory jedisConnectionFactory = new JedisConnectionFactory();
 * 
 * UPointRedisProperties upointRedisProperties = new UPointRedisProperties();
 * 
 * SessionDBMultiRedisConfiguration sessionDBMultiRedisConfiguration=new
 * SessionDBMultiRedisConfiguration();
 * 
 * @Test public void testRedisTemplateForObject() {
 * 
 * jedisConnectionFactory = new JedisConnectionFactory();
 * upointRedisProperties.setPassword("123");
 * upointRedisProperties.setTimeout(10); upointRedisProperties.setSsl(true);
 * upointRedisProperties.setSentinelSetUp(true);
 * upointRedisProperties.setHost("localhost");
 * upointRedisProperties.setPort(8080); JedisConnectionFactory
 * jedisConnectionFactory = Mockito.mock(JedisConnectionFactory.class); //
 * Mockito.doReturn(null).when(ReflectionTestUtils.invokeMethod(
 * jedisConnectionFactory, "createPool"));
 * 
 * // fix for find bug Sentinel s=new Sentinel(); s.setMaster("m1");
 * s.setNodes("123:456"); upointRedisProperties.setSentinel(s);
 * upointRedisProperties.setPool(new Pool());
 * util.setUpointRedisProperties(upointRedisProperties);
 * //util.setConfigDBRedisProperties(configDBRedisProperties);
 * RedisTemplate<String, Object>
 * temp=util.redisTemplateForObject(jedisConnectionFactory); assertEquals(true,
 * temp.getKeySerializer() instanceof StringRedisSerializer);
 * 
 * RedisTemplate<String, Object>
 * configtemp=util.configRedisTemplateForObject(jedisConnectionFactory);
 * assertEquals(true, configtemp.getKeySerializer() instanceof
 * StringRedisSerializer);
 * 
 * util.redisTemplateForMapSecondary(); util.redisTemplateForObjectSecondary();
 * 
 * //Without Sentinel Test upointRedisProperties.setSentinelSetUp(false);
 * util.setUpointRedisProperties(upointRedisProperties);
 * util.redisTemplateForMapSecondary(); util.redisTemplateForObjectSecondary();
 * RedisTemplate<String, Map<Object, Object>>
 * temp1=util.redisTemplateforMap(jedisConnectionFactory); assertEquals(true,
 * temp1.getKeySerializer() instanceof StringRedisSerializer);
 * 
 * RedisTemplate<String, Map<Object, Object>>
 * configtemp1=util.configRedisTemplateforMap(jedisConnectionFactory);
 * assertEquals(true, configtemp1.getKeySerializer() instanceof
 * StringRedisSerializer);
 * 
 * 
 * util.redisTemplateForMapSecondary(); util.redisTemplateForObjectSecondary();
 * 
 * new MockUPNJedisConnectionUtil(); ReflectionTestUtils.setField(util,
 * "configRedisEnable" + "" + "", true); RedisTemplate<String, Object>
 * configtemp2=util.configRedisTemplateForObject(jedisConnectionFactory);
 * assertEquals(true, configtemp2.getKeySerializer() instanceof
 * StringRedisSerializer); RedisTemplate<String, Map<Object, Object>>
 * configtemp3=util.configRedisTemplateforMap(jedisConnectionFactory);
 * assertEquals(true, configtemp3.getKeySerializer() instanceof
 * StringRedisSerializer); }
 * 
 * @Test public void testMultiRedisTemplateForObject() {
 * List<RedisConfigurations> instances = new ArrayList<RedisConfigurations>();
 * instances.add(new RedisConfigurations());
 * sessionDBMultiRedisConfiguration.setInstances(instances);
 * util.setSessionDBMultiRedisConfiguration(sessionDBMultiRedisConfiguration);
 * new MockRedisCacheUtilMultiRedis();
 * 
 * Map<String, RedisTemplate<String, Object>> templateObject =
 * util.getRedisSecondaryTemplatesOfObjects(); Map<String, RedisTemplate<String,
 * Map<Object, Object>>> templateMap = util.getRedisSecondaryTemplatesOfMap();
 * assertEquals(true, templateObject.get("default").getKeySerializer()
 * instanceof StringRedisSerializer); assertEquals(true,
 * templateMap.get("default").getKeySerializer() instanceof
 * StringRedisSerializer);
 * 
 * Map<String, RedisTemplate<String, Object>> configemplateObject =
 * util.getSessionRedisSecondaryTemplatesOfObjects(); Map<String,
 * RedisTemplate<String, Map<Object, Object>>> configTemplateMap =
 * util.getSessionRedisSecondaryTemplatesOfMap(); assertEquals(true,
 * configemplateObject.get("default").getKeySerializer() instanceof
 * StringRedisSerializer); assertEquals(true,
 * configTemplateMap.get("default").getKeySerializer() instanceof
 * StringRedisSerializer);
 * 
 * ReflectionTestUtils.setField(util, """ sesionMultiRedisEnable\ """, true);
 * Map<String, RedisTemplate<String, Object>> configemplateObject1 =
 * util.getSessionRedisSecondaryTemplatesOfObjects(); Map<String,
 * RedisTemplate<String, Map<Object, Object>>> configTemplateMap1 =
 * util.getSessionRedisSecondaryTemplatesOfMap(); assertEquals(true,
 * configemplateObject1.get("default").getKeySerializer() instanceof
 * StringRedisSerializer); assertEquals(true,
 * configTemplateMap1.get("default").getKeySerializer() instanceof
 * StringRedisSerializer);
 * 
 * }
 * 
 * 
 * 
 * @Test public void testRedisTemplateForObjectWithAELockTrue() {
 * 
 * jedisConnectionFactory = new JedisConnectionFactory();
 * upointRedisProperties.setPassword("123");
 * upointRedisProperties.setTimeout(10); upointRedisProperties.setSsl(true);
 * upointRedisProperties.setSentinelSetUp(true);
 * upointRedisProperties.setHost("localhost");
 * upointRedisProperties.setPort(8080); JedisConnectionFactory
 * jedisConnectionFactory = Mockito.mock(JedisConnectionFactory.class);
 * Mockito.doReturn(null).when(ReflectionTestUtils.invokeMethod(
 * jedisConnectionFactory, "createPool"));
 * Mockito.doReturn(null).when(ReflectionTestUtils.invokeMethod(
 * jedisConnectionFactory, "createCluster"));
 * 
 * // fix for find bug Sentinel s=new Sentinel(); s.setMaster("m1");
 * s.setNodes("123:456"); upointRedisProperties.setSentinel(s);
 * upointRedisProperties.setPool(new Pool());
 * util.setUpointRedisProperties(upointRedisProperties);
 * 
 * upointRedisProperties.setSentinelSetUp(false);
 * 
 * ReflectionTestUtils.setField(util, "aeLock", true);
 * 
 * 
 * util.redisTemplateForMapSecondary(); util.redisTemplateForObjectSecondary();
 * 
 * }
 * 
 * @Test public void testRedisTemplateForObjectWithAELockTrueWithCluster() {
 * 
 * jedisConnectionFactory = new JedisConnectionFactory();
 * upointRedisProperties.setPassword("123");
 * upointRedisProperties.setTimeout(10); upointRedisProperties.setSsl(true);
 * upointRedisProperties.setSentinelSetUp(false);
 * upointRedisProperties.setHost("localhost");
 * upointRedisProperties.setPort(8080);
 * 
 * 
 * JedisConnectionFactory jedisConnectionFactory =
 * Mockito.mock(JedisConnectionFactory.class); Mockito.doReturn(new
 * JedisCluster(new HostAndPort("localhost",
 * 8080))).when(ReflectionTestUtils.invokeMethod(jedisConnectionFactory,
 * "createCluster")); // fix for find bug
 * 
 * upointRedisProperties.setPool(new Pool());
 * 
 * Cluster c = new Cluster(); List<String> nodes = new ArrayList<String>();
 * nodes.add("123:456"); c.setNodes(nodes); c.setMaxRedirects(0);
 * upointRedisProperties.setCluster(c);
 * util.setUpointRedisProperties(upointRedisProperties);
 * 
 * 
 * upointRedisProperties.setSentinelSetUp(false);
 * 
 * ReflectionTestUtils.setField(util, "aeLock", true);
 * 
 * 
 * util.redisTemplateForMapSecondary(); util.redisTemplateForObjectSecondary();
 * 
 * }
 * 
 * @Test public void testRedisTemplateForObjectWithAELockFalseWithCluster() {
 * 
 * jedisConnectionFactory = new JedisConnectionFactory();
 * upointRedisProperties.setPassword("123");
 * upointRedisProperties.setTimeout(10); upointRedisProperties.setSsl(true);
 * upointRedisProperties.setSentinelSetUp(true);
 * upointRedisProperties.setHost("localhost");
 * upointRedisProperties.setPort(8080);
 * 
 * JedisConnectionFactory jedisConnectionFactory =
 * Mockito.mock(JedisConnectionFactory.class); Mockito.doReturn(new
 * JedisCluster(new HostAndPort("localhost",
 * 8080))).when(ReflectionTestUtils.invokeMethod(jedisConnectionFactory,
 * "createCluster")); // fix for find bug
 * 
 * upointRedisProperties.setPool(new Pool());
 * 
 * Cluster c = new Cluster(); List<String> nodes = new ArrayList<String>();
 * nodes.add("123:456"); c.setNodes(nodes); c.setMaxRedirects(0);
 * upointRedisProperties.setCluster(c);
 * util.setUpointRedisProperties(upointRedisProperties);
 * 
 * util.redisTemplateForMapSecondary(); util.redisTemplateForObjectSecondary();
 * 
 * upointRedisProperties.setSentinelSetUp(false);
 * 
 * ReflectionTestUtils.setField(util, "aeLock", false);
 * 
 * 
 * util.redisTemplateForMapSecondary(); util.redisTemplateForObjectSecondary();
 * 
 * }
 * 
 * @Test public void testUpointDeserializer() {
 * 
 * ByteArrayOutputStream baos = new ByteArrayOutputStream(); boolean success =
 * true;
 * 
 * try {
 * 
 * ObjectOutputStream oos = new ObjectOutputStream(baos);
 * 
 * oos.writeObject("RedisUtilsTest");
 * 
 * oos.flush(); oos.close();
 * 
 * InputStream is = new ByteArrayInputStream(baos.toByteArray());
 * 
 * UpointDeserializer deserializer = new UpointDeserializer();
 * deserializer.deserialize(is);
 * 
 * } catch (IOException e) { success = false; }
 * 
 * assertEquals(true, success); }
 * 
 * @Test public void testUpointDeserializerFailed() {
 * 
 * ByteArrayOutputStream baos = new ByteArrayOutputStream(); boolean success =
 * true;
 * 
 * try {
 * 
 * baos.write("RedisUtilsTest".getBytes(Charset.defaultCharset()));
 * 
 * InputStream is = new ByteArrayInputStream(baos.toByteArray());
 * 
 * UpointDeserializer deserializer = new UpointDeserializer();
 * deserializer.deserialize(is);
 * 
 * } catch (IOException e) { success = false; }
 * 
 * assertEquals(false, success); }
 * 
 * @Test public void testNewConfigurableObject() {
 * 
 * ByteArrayOutputStream baos = new ByteArrayOutputStream(); boolean success =
 * true;
 * 
 * try {
 * 
 * ObjectOutputStream oos = new ObjectOutputStream(baos);
 * 
 * oos.writeObject("RedisUtilsTest");
 * 
 * oos.flush(); oos.close();
 * 
 * InputStream is = new ByteArrayInputStream(baos.toByteArray());
 * 
 * ClassLoader classLoader = this.getClass().getClassLoader();
 * 
 * NewConfigurableObjectInputStream objectInputStream = new
 * NewConfigurableObjectInputStream(is, classLoader, true);
 * 
 * objectInputStream.resolveClass(ObjectStreamClass.lookup(String.class));
 * 
 * objectInputStream.close();
 * 
 * } catch (IOException e) { success = false; } catch (ClassNotFoundException e)
 * { success = false; }
 * 
 * assertEquals(true, success);
 * 
 * } }
 */