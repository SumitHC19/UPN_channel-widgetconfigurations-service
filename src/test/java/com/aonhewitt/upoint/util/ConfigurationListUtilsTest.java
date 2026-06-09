package com.aonhewitt.upoint.util;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedConstruction;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.alight.cloud.data.util.HROCacheMap;
import com.alight.dynamodb.DynamoDbHelper;
import com.alight.logging.constant.UpointLogConstants;
import com.alight.logging.dto.RedisCallCountStorage;
import com.alight.logging.events.CommonTypeLogEvent;
import com.alight.logging.helpers.LogInheritableThreadLocal;
import com.alight.logging.logbook.LogbookPropertiesConfiguration;
import com.aonhewitt.beans.CacheKeys;
import com.aonhewitt.beans.GenericRequestBean;
import com.aonhewitt.beans.UPCKey;
import com.aonhewitt.beans.ValueHolder;
import com.aonhewitt.portal.adapter.AdapterService;
import com.aonhewitt.portal.core.advocacy.token.ASGSessionToken;
import com.aonhewitt.portal.core.service.util.CmServicesUtil;
import com.aonhewitt.portal.hasbro.rest.utils.UDPUriResourceHandler;
import com.aonhewitt.portal.hasbro.udp.base.service.UDPBaseServices;
import com.aonhewitt.portal.hasbro.udp.base.service.pojo.UDPResponse;
import com.aonhewitt.portal.linkres.runtime.html.AnchorTag;
import com.aonhewitt.portal.linkres.runtime.html.Tag;
import com.aonhewitt.portal.linkres.runtime.link.Link;
import com.aonhewitt.portal.service.upc.util.UPCMemoryManagerClientCacheUtil;
import com.aonhewitt.portal.udp.util.UDPBrokerHeaderUtil;
import com.aonhewitt.portal.util.AppContainerProvider;
import com.aonhewitt.portal.util.MessageAppContainer;
import com.aonhewitt.portal.util.RestServiceInvocationResult;
import com.aonhewitt.portal.util.SessionTokenParserUtil;
import com.aonhewitt.upoint.cache.config.provider.ICache;
import com.aonhewitt.upoint.cache.config.provider.impl.RedisCacheMapProviderImpl;
import com.aonhewitt.upoint.cache.config.provider.impl.RedisCacheObjectProviderImpl;
import com.aonhewitt.upoint.cache.config.provider.impl.SessionRedisCacheObjectProviderImpl;
import com.aonhewitt.upoint.cache.exception.BaseCacheException;
import com.aonhewitt.upoint.cache.util.AppConfigReader;
import com.aonhewitt.upoint.cache.util.DistributedCacheUtil;
import com.aonhewitt.upoint.cache.util.ShortCacheUtil;
import com.aonhewitt.upoint.core.service.util.ExpressionDebugAssistUtil;
import com.aonhewitt.upoint.db.JNDIClientMapping;
import com.aonhewitt.upoint.db.JNDIRepository;
import com.fasterxml.jackson.core.JsonProcessingException;

import redis.clients.jedis.JedisPoolConfig;

//import redis.clients.jedis.JedisShardInfo;
@ExtendWith(SpringExtension.class)
public class ConfigurationListUtilsTest {

	/*
	 * PreparedStatement stm; DataSource ds; Connection conn;
	 */
	// HttpServletRequest request;

	// @Mock()
	// @Qualifier("redisCacheMapProvider")

	// @MockBean
	// @Qualifier("redisCacheMapProvider")

	// @Mock(name="redisCacheMapProvider")
	@MockBean
	@Qualifier("redisCacheMapProvider")
	private RedisCacheMapProviderImpl redisCompleteCacheMapProviderMock;

	@InjectMocks
	private DistributedCacheUtil distributedCacheUtil;

	@InjectMocks
	private AppConfigReader appConfigReader;

	private ICache redisCacheProvider = new RedisCacheObjectProviderImpl(new DummyRedisTemplate());

	ICache redisCompleteCacheMapProvider = new RedisCacheMapProviderImpl(new DummyRedisTemplate());
	private RedisTemplate redisTemplateForObject = new DummyRedisTemplate();
	ICache sessionRedisCacheProvider = new SessionRedisCacheObjectProviderImpl(new SessionDummyRedisTemplate());
	RedisTemplate sessionRedisTemplateForObject = new SessionDummyRedisTemplate();
//	DistributedCacheUtil distriburedCacheUtil = new DistributedCacheUtil(redisCacheProvider, null);

	private String idMapping = "[ { \"platformTyp\" : \"TBA_4X\", \"domain\" : \"DC,DB\", \"clientId\" : \"19920\", \"normalizedClientid\" : \"19920\", \"systemInstanceId\" : \"\", \"platformInternalId\" : \"184000044\", \"platformExternalId\" : \"140626002\", \"roleType\" : \"rkp\" }]";
	private ConfigurationListUtils configurationListUtils = new ConfigurationListUtils(redisCacheProvider,
			redisTemplateForObject);
	private ConfigurationListUtils configurationListUtils1 = new ConfigurationListUtils(redisCacheProvider,
			new RedisTemplate<>());
	// private ConfigurationListUtils configurationListUtil2 = new
	// ConfigurationListUtils(sessionRedisCacheProvider,sessionRedisTemplateForObject);
//	private ConfigurationListUtils configurationListUtils2;
	private MessageAppContainer messageAppContainer = new MessageAppContainer();

	private CmServicesUtil cmServicesUtil = mock(CmServicesUtil.class);
	LogbookPropertiesConfiguration logbookPropertiesConfiguration = mock(LogbookPropertiesConfiguration.class);
	DynamoDbHelper dynamoDbHelper = mock(DynamoDbHelper.class);

	private static MockedStatic<UDPBaseServices> uDPBaseServicesMockedStatic;
	private static MockedStatic<UPCMemoryManagerClientCacheUtil> uPCMemoryManagerClientCacheUtilMockedStatic;
	private static MockedStatic<UDPBrokerHeaderUtil> uDPBrokerHeaderUtilMockedStatic;
	private static MockedStatic<UDPUriResourceHandler> uDPUriResourceHandlerMockedStatic;
	
	@Mock
	CmServicesUtil cmServicesUtil2;

	@BeforeAll
	public static void init() {
		Mockito.clearAllCaches();
		uDPBaseServicesMockedStatic = mockStatic(UDPBaseServices.class);
		uPCMemoryManagerClientCacheUtilMockedStatic = mockStatic(UPCMemoryManagerClientCacheUtil.class);
		uDPBrokerHeaderUtilMockedStatic = mockStatic(UDPBrokerHeaderUtil.class);
		uDPUriResourceHandlerMockedStatic = mockStatic(UDPUriResourceHandler.class);

	}

	@AfterAll
	public static void close() {
		uDPBaseServicesMockedStatic.close();
		uPCMemoryManagerClientCacheUtilMockedStatic.close();
		uDPBrokerHeaderUtilMockedStatic.close();
		uDPUriResourceHandlerMockedStatic.close();

	}

	@AfterEach
	public void tear() {

		System.setProperty("baseDBUrl", "");
		System.setProperty("geolocationDBUrl", "");
		System.setProperty("uccebaseDBUrl", "");

	}

	@BeforeEach
	public void setup() {
		// configurationListUtils.uDPBaseServices=udpBaseServices;
		configurationListUtils.sessionRedisCacheProvider = sessionRedisCacheProvider;
		configurationListUtils.sessionRedisTemplateForObject = sessionRedisTemplateForObject;
		configurationListUtils.redisCompleteCacheMapProvider = (RedisCacheMapProviderImpl) redisCompleteCacheMapProvider;
		messageAppContainer.getAhUser().setFName("Steve");
		messageAppContainer.setIdMapping(idMapping);

		System.setProperty("uccebaseDBUrl", "jdbc:oracle:thin:@localhost:1521:xe");
		System.setProperty("baseDBUrl", "jdbc:oracle:thin:@localhost:1521:xe");

		System.setProperty("geolocationDBUrl", "jdbc:oracle:thin:@localhost:1521:xe");

		MockHttpServletRequest request1 = new MockHttpServletRequest();
		RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request1));
		/*
		 * new MockUp <RedisTemplate<String, Object>>() {
		 * 
		 * @Mock public Set<String> keys(String keys ) { return new HashSet<String>(); }
		 * };
		 */
		// new MockRedisTemplate();
		new MockJNDIRepository();

		messageAppContainer.getAhUser().setLocale("en_US");
		messageAppContainer.setAsgSessionToken(new ASGSessionToken());
		messageAppContainer.getAsgSessionToken().setBrokerUserId("portal");
		messageAppContainer.setLineage("19920_portal");
		messageAppContainer.setPrimaryClientId("19920");
		AppContainerProvider.setContainer(messageAppContainer);

		// new MockUDPBrokerHeaderUtil();
		// new MockDistributedCacheUtil();
		// new MockUDPBaseServices();

		// new MockAELockFalse();
		// new MockURIResorceLocator();

		// new MockUDPUtil();

		/*
		 * new MockUp<com.alight.upoint.udp.base.service.UDPBaseServices>() {
		 * 
		 * @Mock public com.alight.upoint.udp.beans.UDPInvocationResult
		 * invoke(com.alight.upoint.udp.beans.UDPServiceObject object) {
		 * com.alight.upoint.udp.beans.UDPInvocationResult invoResult=new
		 * com.alight.upoint.udp.beans.UDPInvocationResult();
		 * invoResult.setStatusCode(2);
		 * invoResult.setHttpStatusMessage("Udp Response returned successfully"); return
		 * invoResult; } };
		 */
		new MockUDPNextBaseServices();

		configurationListUtils.cmServicesUtil = cmServicesUtil;
		configurationListUtils.logbookPropertiesConfiguration = logbookPropertiesConfiguration;
		configurationListUtils.dynamoDbHelper = dynamoDbHelper;
	}

	@Test
	public void testAHUSer() {

		Map<String, Object> map = configurationListUtils.getAhUserObject();
		assertTrue(map.get("AhUserObject") != null);
		assertTrue(map.get("IdMapping") != null);

		map = configurationListUtils.getRequestPayload();
		assertTrue(map.get("AhUserObject") != null);
		assertTrue(map.get("IdMapping") != null);

		messageAppContainer.resetAhUser();
		map = configurationListUtils.getRequestPayload();

		messageAppContainer.resetAhUser();
		map = configurationListUtils.getAhUserObject();
	}

	@Test
	public void testUdp() {
		UDPResponse response = new UDPResponse();
		response.setResponseCode("200");
		uDPBaseServicesMockedStatic.when(() -> UDPBaseServices.getUdpPersonSchemaObject()).thenReturn(response);
		UDPResponse map = configurationListUtils.getUdpData();
		assertTrue(map.getResponseCode() != null);
		// assertTrue(map.get("IdMapping") != null);

	}

	@Test
	public void testgetBusinessDomainObject() {

		Object businessObject = configurationListUtils.getBusinessDomainObject();
		assertFalse(businessObject != null);

	}

	@Test
	public void testGetUpdatedARHForSharedAccess() throws Exception {
		Object businessServiceAcct = configurationListUtils.getUpdatedARHForSharedAccess();
		assertFalse(businessServiceAcct != null);
	}
	
	@Test
	public void testGetUpdatedARHForSharedAccessException() throws Exception {
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.addHeader("domain", "CM");
		request.addHeader("alightRequestHeader",
				"{\"locale\":\"en_US\",\"roleId\":\"19920_09754_1.0-E:19920_1.0-E:@PPT@\"}");
		request.addHeader("alightPersonSessionToken",
				"bOiAh9QDExxcLlAEl5s2AtpviYozDv+gjaPyaJb5MeEsvzbpdcklLNzKZEXWR9yZ0QdHvPJ4MG2kZTP5ZSq146m3BA9z8DLTwogMpEY6YViZcGB4K7ww793sqS2NSalp1jP252MnHCwOOvrHtVCndGPSCBDsESxiv9vDH8R0RMhHfA5XSky8AGhJVVMRF0hEQQl7a5cCKkBpsbOmGFebj5YWFdPttCa5GSjDL3AyoKSr/xrocv67ta5WibVMsR9I3t4AdpTjNFbm7Wha5rRz+s3nU+MrDJPU/83cXTpzq97msqPkiauYjLYLmizoGNWj9ol0J9IVyBGQxiHU8bHPxiBHY3/k+aV1k3etwMJYLqrhpuG7xkuhfUxoCUDZIBdBNV/q2khsRW9Ny224mlaW+BcsVrbHHY7Nm+E87L6yBOKSD1S2IKZtTSPRM/jl53wzt6mXRnj/ciblLZX1ND3Le6dJFI4fUBba1vVPTcIdj8VJy53I6wzuTbF+VZ1xLmqmhTmnNxRxyAFqWSUbjxvOEFefVWPGhI8z+MpervVuCtHbRpq/Lm5CuZDEmbiE+T9mk8+waVDme7xvGpPG1hf/P2YK+zxQkt17v2gwFlN6DXWChPh3Dst5madir2JwApRXiBirGgSwpvEy40EVH1PeGPxaKo+efbuWeRRIqlzYB7fZgeWc/1K2xvwLBLzW9k2jYBmqOdcV/2yitCVrmkWkFwANItX+O/L0gUsodVqJYxw/dMl6KokXizjmYUqeO4OP67XcuE4USHwAWuAyuxZ2p5kaaTFKpHPbuj20m7y3nz1ShQvycyvagraAEcvmlqLth/8xkJ/zRPRFo3qDeJzEysSUBfPsBcmmhLSGdFf4J1slshYCmPuKi87nRF9sksI1v8WJU4SxnmYMsQVpelGDM9RL6+RUVIDu1xtAxxkr82c9bUb2sZY4xpeuflYkaf5XK8ANgJDBejpiZFpmvLa+rV0hqs2IhcIq5KltXsvtzYdPyQT3vedYnbGo1o+K7y0U2x6KIA17CuGMNZgJWM99cNTlkh+kwY9b2lkXuZvtBqk3/KWl3uudJ0uB15yXUfmW/rMHQbuKu9hcOv/S+LyF0YtBI/XiyDz/wzQBxZGyUb1icGp3ug+JiVHyn1Gk7VkMYUwIzjG4i9Wvhp/Gdx37ivagAvypWRbNzNx3itoI6ScvJBA4fSr3p+covPJCjbgowDKCXHnMwwY4zNxzjq/HWR2g3NIDc+Yzmv+xSlYcGmQEVU0usU/fjc/m7hnDX9psGGW5+GUPtMxMXPv9HlxLbZffBzdizT8JAwdb8Voyh8gHRw2nGv40mxUDFci92OntBcmxgkXh70BEY6NRQLPbUWqv7tnoDnLuqpdbTEYvEX22pGPlchXzwgCpcue5O7Rw9mkqfY9J2qe1nWxuv8PFXUjIAzlSkxh7XMzQOqs54TVePqeO1ZgmWrANOBW/g5VXKv0MrsfBZzhOZ45ujAMiPvFqTreL/eW7qpYmiwkmm+i8WTsOly8ONga93lBo72I+LkfqNou/zFngwFQIoStaJ/Gp763kET5WTDNa9SyxKSpv3B15XecvCoT2o78I8J4cfLEGGz6AbmitQgDS1De8Pe7dU6FuhEza8cetQyfo4vClLKByH8TLKa9/aX0frpsjmKCbQxt4UoCpn2Xc4+osLtGotUsAQNnuBW7hI9lNLa+l7ZdF9GMf8w06TX700f42ww7rNxHZpx+qBsVWeoXxgCs3e3oXcG/2GxhYECVVC6Ln9r+I/bm7FuEFkH0NhzMimHdhtzbw0vHG0Ei56scwALr5gQH5pnycrA98CHvvndA=");
		RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
		
		try (MockedStatic<SessionTokenParserUtil> sessionTokenParserUtilMockedStatic = mockStatic(
				SessionTokenParserUtil.class);)
				{
			sessionTokenParserUtilMockedStatic.when(() -> SessionTokenParserUtil
					.getupdatedAlightRequestHeaderForSharedAccess(Mockito.anyString(), Mockito.anyString(), Mockito.anyString()))
					.thenThrow(new NullPointerException());
		Object businessServiceAcct = configurationListUtils.getUpdatedARHForSharedAccess();
		assertFalse(businessServiceAcct != null);
	}
	}

	@Test
	public void testUdpNext() {
		// new MockApplicationContextProvider();
		UDPBaseServices baseServices = new UDPBaseServices();
		try (MockedStatic<ApplicationContextProvider> applicationContextProviderMockedStatic = mockStatic(
				ApplicationContextProvider.class);) {
			applicationContextProviderMockedStatic
					.when(() -> ApplicationContextProvider.getBean(Mockito.any(String.class), Mockito.any()))
					.thenReturn(baseServices);
			uDPBrokerHeaderUtilMockedStatic.when(() -> UDPBrokerHeaderUtil.createBrokerHeaderMapFromAHUser())
					.thenReturn(new HashMap<String, Object>());
			uDPUriResourceHandlerMockedStatic.when(() -> UDPUriResourceHandler.getResourceURI(Mockito.anyString()))
					.thenReturn("");

			Object resp = configurationListUtils.getUdpNextPersonSchemaObject();
			// com.alight.upoint.udp.beans.UDPInvocationResult
			// invoResult=(com.alight.upoint.udp.beans.UDPInvocationResult)resp;
			// assertTrue(invoResult.getHttpStatusMessage().equals("Udp Response returned
			// successfully"));
		}
	}
	
	@Test
	public void testUdpNextWithElseCase() {
		UDPBaseServices baseServices = new UDPBaseServices();
		com.alight.upoint.udp.base.service.UDPBaseServices baseServices2= new com.alight.upoint.udp.base.service.UDPBaseServices(null);
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.addHeader("UseSecondaryDataCache", "UseSecondaryDataCache");
		request.addHeader("alightRequestHeader",
				"{\"locale\":\"en_US\",\"roleId\":\"19920_09754_1.0-E:19920_1.0-E:@PPT@\"}");
		request.addHeader("alightSessionToken",
				"bOiAh9QDExxcLlAEl5s2AtpviYozDv+gjaPyaJb5MeEsvzbpdcklLNzKZEXWR9yZ0QdHvPJ4MG2kZTP5ZSq146m3BA9z8DLTwogMpEY6YViZcGB4K7ww793sqS2NSalp1jP252MnHCwOOvrHtVCndGPSCBDsESxiv9vDH8R0RMhHfA5XSky8AGhJVVMRF0hEQQl7a5cCKkBpsbOmGFebj5YWFdPttCa5GSjDL3AyoKSr/xrocv67ta5WibVMsR9I3t4AdpTjNFbm7Wha5rRz+s3nU+MrDJPU/83cXTpzq97msqPkiauYjLYLmizoGNWj9ol0J9IVyBGQxiHU8bHPxiBHY3/k+aV1k3etwMJYLqrhpuG7xkuhfUxoCUDZIBdBNV/q2khsRW9Ny224mlaW+BcsVrbHHY7Nm+E87L6yBOKSD1S2IKZtTSPRM/jl53wzt6mXRnj/ciblLZX1ND3Le6dJFI4fUBba1vVPTcIdj8VJy53I6wzuTbF+VZ1xLmqmhTmnNxRxyAFqWSUbjxvOEFefVWPGhI8z+MpervVuCtHbRpq/Lm5CuZDEmbiE+T9mk8+waVDme7xvGpPG1hf/P2YK+zxQkt17v2gwFlN6DXWChPh3Dst5madir2JwApRXiBirGgSwpvEy40EVH1PeGPxaKo+efbuWeRRIqlzYB7fZgeWc/1K2xvwLBLzW9k2jYBmqOdcV/2yitCVrmkWkFwANItX+O/L0gUsodVqJYxw/dMl6KokXizjmYUqeO4OP67XcuE4USHwAWuAyuxZ2p5kaaTFKpHPbuj20m7y3nz1ShQvycyvagraAEcvmlqLth/8xkJ/zRPRFo3qDeJzEysSUBfPsBcmmhLSGdFf4J1slshYCmPuKi87nRF9sksI1v8WJU4SxnmYMsQVpelGDM9RL6+RUVIDu1xtAxxkr82c9bUb2sZY4xpeuflYkaf5XK8ANgJDBejpiZFpmvLa+rV0hqs2IhcIq5KltXsvtzYdPyQT3vedYnbGo1o+K7y0U2x6KIA17CuGMNZgJWM99cNTlkh+kwY9b2lkXuZvtBqk3/KWl3uudJ0uB15yXUfmW/rMHQbuKu9hcOv/S+LyF0YtBI/XiyDz/wzQBxZGyUb1icGp3ug+JiVHyn1Gk7VkMYUwIzjG4i9Wvhp/Gdx37ivagAvypWRbNzNx3itoI6ScvJBA4fSr3p+covPJCjbgowDKCXHnMwwY4zNxzjq/HWR2g3NIDc+Yzmv+xSlYcGmQEVU0usU/fjc/m7hnDX9psGGW5+GUPtMxMXPv9HlxLbZffBzdizT8JAwdb8Voyh8gHRw2nGv40mxUDFci92OntBcmxgkXh70BEY6NRQLPbUWqv7tnoDnLuqpdbTEYvEX22pGPlchXzwgCpcue5O7Rw9mkqfY9J2qe1nWxuv8PFXUjIAzlSkxh7XMzQOqs54TVePqeO1ZgmWrANOBW/g5VXKv0MrsfBZzhOZ45ujAMiPvFqTreL/eW7qpYmiwkmm+i8WTsOly8ONga93lBo72I+LkfqNou/zFngwFQIoStaJ/Gp763kET5WTDNa9SyxKSpv3B15XecvCoT2o78I8J4cfLEGGz6AbmitQgDS1De8Pe7dU6FuhEza8cetQyfo4vClLKByH8TLKa9/aX0frpsjmKCbQxt4UoCpn2Xc4+osLtGotUsAQNnuBW7hI9lNLa+l7ZdF9GMf8w06TX700f42ww7rNxHZpx+qBsVWeoXxgCs3e3oXcG/2GxhYECVVC6Ln9r+I/bm7FuEFkH0NhzMimHdhtzbw0vHG0Ei56scwALr5gQH5pnycrA98CHvvndA=");
		request.addHeader("alightPersonSessionToken",
				"bOiAh9QDExxcLlAEl5s2AtpviYozDv+gjaPyaJb5MeEsvzbpdcklLNzKZEXWR9yZ0QdHvPJ4MG2kZTP5ZSq146m3BA9z8DLTwogMpEY6YViZcGB4K7ww793sqS2NSalp1jP252MnHCwOOvrHtVCndGPSCBDsESxiv9vDH8R0RMhHfA5XSky8AGhJVVMRF0hEQQl7a5cCKkBpsbOmGFebj5YWFdPttCa5GSjDL3AyoKSr/xrocv67ta5WibVMsR9I3t4AdpTjNFbm7Wha5rRz+s3nU+MrDJPU/83cXTpzq97msqPkiauYjLYLmizoGNWj9ol0J9IVyBGQxiHU8bHPxiBHY3/k+aV1k3etwMJYLqrhpuG7xkuhfUxoCUDZIBdBNV/q2khsRW9Ny224mlaW+BcsVrbHHY7Nm+E87L6yBOKSD1S2IKZtTSPRM/jl53wzt6mXRnj/ciblLZX1ND3Le6dJFI4fUBba1vVPTcIdj8VJy53I6wzuTbF+VZ1xLmqmhTmnNxRxyAFqWSUbjxvOEFefVWPGhI8z+MpervVuCtHbRpq/Lm5CuZDEmbiE+T9mk8+waVDme7xvGpPG1hf/P2YK+zxQkt17v2gwFlN6DXWChPh3Dst5madir2JwApRXiBirGgSwpvEy40EVH1PeGPxaKo+efbuWeRRIqlzYB7fZgeWc/1K2xvwLBLzW9k2jYBmqOdcV/2yitCVrmkWkFwANItX+O/L0gUsodVqJYxw/dMl6KokXizjmYUqeO4OP67XcuE4USHwAWuAyuxZ2p5kaaTFKpHPbuj20m7y3nz1ShQvycyvagraAEcvmlqLth/8xkJ/zRPRFo3qDeJzEysSUBfPsBcmmhLSGdFf4J1slshYCmPuKi87nRF9sksI1v8WJU4SxnmYMsQVpelGDM9RL6+RUVIDu1xtAxxkr82c9bUb2sZY4xpeuflYkaf5XK8ANgJDBejpiZFpmvLa+rV0hqs2IhcIq5KltXsvtzYdPyQT3vedYnbGo1o+K7y0U2x6KIA17CuGMNZgJWM99cNTlkh+kwY9b2lkXuZvtBqk3/KWl3uudJ0uB15yXUfmW/rMHQbuKu9hcOv/S+LyF0YtBI/XiyDz/wzQBxZGyUb1icGp3ug+JiVHyn1Gk7VkMYUwIzjG4i9Wvhp/Gdx37ivagAvypWRbNzNx3itoI6ScvJBA4fSr3p+covPJCjbgowDKCXHnMwwY4zNxzjq/HWR2g3NIDc+Yzmv+xSlYcGmQEVU0usU/fjc/m7hnDX9psGGW5+GUPtMxMXPv9HlxLbZffBzdizT8JAwdb8Voyh8gHRw2nGv40mxUDFci92OntBcmxgkXh70BEY6NRQLPbUWqv7tnoDnLuqpdbTEYvEX22pGPlchXzwgCpcue5O7Rw9mkqfY9J2qe1nWxuv8PFXUjIAzlSkxh7XMzQOqs54TVePqeO1ZgmWrANOBW/g5VXKv0MrsfBZzhOZ45ujAMiPvFqTreL/eW7qpYmiwkmm+i8WTsOly8ONga93lBo72I+LkfqNou/zFngwFQIoStaJ/Gp763kET5WTDNa9SyxKSpv3B15XecvCoT2o78I8J4cfLEGGz6AbmitQgDS1De8Pe7dU6FuhEza8cetQyfo4vClLKByH8TLKa9/aX0frpsjmKCbQxt4UoCpn2Xc4+osLtGotUsAQNnuBW7hI9lNLa+l7ZdF9GMf8w06TX700f42ww7rNxHZpx+qBsVWeoXxgCs3e3oXcG/2GxhYECVVC6Ln9r+I/bm7FuEFkH0NhzMimHdhtzbw0vHG0Ei56scwALr5gQH5pnycrA98CHvvndA=");
		
		RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
		
		try (MockedStatic<ApplicationContextProvider> applicationContextProviderMockedStatic = mockStatic(
				ApplicationContextProvider.class);) {
			applicationContextProviderMockedStatic
					.when(() -> ApplicationContextProvider.getBean(Mockito.any(String.class), Mockito.any()))
					.thenReturn(baseServices2);
			uDPBrokerHeaderUtilMockedStatic.when(() -> UDPBrokerHeaderUtil.createBrokerHeaderMapFromAHUser())
					.thenReturn(new HashMap<String, Object>());
			uDPUriResourceHandlerMockedStatic.when(() -> UDPUriResourceHandler.getResourceURI(Mockito.anyString()))
					.thenReturn("");

			Object resp = configurationListUtils.getUdpNextPersonSchemaObject();
		}
	}

	@Test
	public void testUPC() throws JsonProcessingException {

		// ObjectMapper mapper = new ObjectMapper();
		List<Map<Object, Object>> output = new ArrayList<Map<Object, Object>>();
		RestServiceInvocationResult serviceInvocationResult = new RestServiceInvocationResult();

		Map<Object, Object> widgetMap = new HashMap<Object, Object>();
		widgetMap.put("type", "expression");
		widgetMap.put("value", "false");

		Map<Object, Object> widgetMapResult = new HashMap<Object, Object>();
		widgetMapResult.put("IS_HEADER_FOOTER_V2", widgetMap);
		output.add(widgetMapResult);

		// create output Json
		// String outputJson = mapper.writeValueAsString(output);

		// create mock ServiceInvocation

		serviceInvocationResult.setResponseCMValues(output);
		serviceInvocationResult.setResponseStatus(RestServiceInvocationResult.ResponseStatusEnum.SUCCESS);
		GenericRequestBean bean = new GenericRequestBean();
		bean.setLineage("19920_1.0");

		UPCKey upcKey = new UPCKey();
		upcKey.setWidgetName("ytrstmtviewportletv2_WAR_ahytrportlet");

		bean.setUpcKey(upcKey);
		new MockAdapterService(serviceInvocationResult);
		Object resp = configurationListUtils.getUpcConfig(bean);
		assertNotNull(resp);
		// com.alight.upoint.udp.beans.UDPInvocationResult
		// invoResult=(com.alight.upoint.udp.beans.UDPInvocationResult)resp;
		// assertTrue(resp..equals("Udp Response returned successfully"));

	}

	@Test
	public void testPrintJedisPoolConfig() {

		configurationListUtils1.printJedisPoolConfig();
		JedisConnectionFactory connFactory = new JedisConnectionFactory();
		JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
		jedisPoolConfig.setMaxIdle(0);
		jedisPoolConfig.setMaxTotal(0);
		jedisPoolConfig.setMaxWaitMillis(0);
		jedisPoolConfig.setMinIdle(0);
		jedisPoolConfig.setMinEvictableIdleTimeMillis(0);

		connFactory.setPoolConfig(jedisPoolConfig);
		// JedisShardInfo shardInfo = new JedisShardInfo("localhost");
		// connFactory.setShardInfo(shardInfo);
		redisTemplateForObject.setConnectionFactory(connFactory);
		connFactory.setHostName("localhost");
		connFactory.setPassword("password");
		connFactory.setClientName("Test");
		connFactory.setPort(0);
		connFactory.setTimeout(0);

		Object responseMap = configurationListUtils.printJedisPoolConfig();
		assertTrue(responseMap != null);
	}

	@Test
	public void testVerifyCacheKey() throws BaseCacheException {
		new MockRedisObjectProvider();
		new MockSessionRedisObjectProvider();
		CacheKeys keys = new CacheKeys();
		keys.setModuleId("CORE_TXT_RESOLVER");
		keys.setName("aa-en_us");
		keys.setCacheOperation("get");

		CacheKeys keys2 = new CacheKeys();
		keys2.setModuleId("NA");
		keys2.setName("aa-en_us");
		keys2.setCacheOperation("get");

		CacheKeys keys3 = new CacheKeys();
		keys3.setModuleId("Invalid");
		keys3.setName("aa-en_us");
		keys3.setCacheOperation("get");

		List<CacheKeys> list = new ArrayList<CacheKeys>();
		list.add(keys);

		list.add(keys2);
		list.add(keys3);

		GenericRequestBean bean = new GenericRequestBean();
		bean.setLineage("19920_1.0");
		bean.setCacheKeys(list);

		DistributedCacheUtil cacheManager = mock(DistributedCacheUtil.class);
		List<String> eliList= new ArrayList<>();
		eliList.add("CORE_TXT_RESOLVER");
		
			
			
			DistributedCacheUtil distributedCacheUtil=new DistributedCacheUtil(redisCacheProvider, null);
			try (MockedStatic<DistributedCacheUtil> distributedCacheUtilMockedStatic = mockStatic(
					DistributedCacheUtil.class);
					) {
				distributedCacheUtilMockedStatic
				.when(() -> DistributedCacheUtil.getInstance())
				.thenReturn(distributedCacheUtil);
				
				distributedCacheUtilMockedStatic.when(() -> DistributedCacheUtil.getEligibleModules())
				.thenReturn(eliList);
			
			
			when(cacheManager.getDCKeyNamespace(Mockito.anyString(), Mockito.anyString())).thenReturn("test");

			// Map<String, String> response1= (Map<String, String>)
			// configurationListUtils.verifyCacheKey(new GenericRequestBean());
			Map<String, String> response = (Map<String, String>) configurationListUtils.verifyCacheKey(bean);
			// assertTrue(response1!=null);

			// assertTrue(response.get("CORE_TXT_RESOLVER:aa-en_us").equals("Exist - under
			// cache!!![C]"));
			// assertTrue(response.get(":aa-en_us").equals("Exist - under
			// cache!!![C].Numbers of keys for this Module is:1"));
			assertTrue(response.get("Wrong input ").equals("ModuleID not supported!!!"));

			CacheKeys skeys = new CacheKeys();
			skeys.setModuleId("CORE_TXT_RESOLVER");
			skeys.setName("aa-en_us");
			skeys.setCacheOperation("get");

			CacheKeys skeys2 = new CacheKeys();
			skeys2.setModuleId("NA");
			skeys2.setName("aa-es_us");
			skeys2.setCacheOperation("get");

			CacheKeys skeys3 = new CacheKeys();
			skeys3.setModuleId("Invalid");
			skeys3.setName("aa-en_us");
			skeys3.setCacheOperation("get");

			List<CacheKeys> list1 = new ArrayList<CacheKeys>();
			list1.add(skeys);

			list1.add(skeys2);
			list1.add(skeys3);

			GenericRequestBean bean1 = new GenericRequestBean();
			bean1.setLineage("19921_1.0");
			bean1.setCacheKeys(list1);
			Map<String, String> sessionresponse = (Map<String, String>) configurationListUtils.verifyCacheKey(bean1);
			assertTrue(sessionresponse != null);

			// Map<String, String> response2 = (Map<String, String>)
			// configurationListUtils.verifyCacheKey(bean);

			// configurationListUtils.deleteCacheKey("NA");
		}

	}

	@Test
	public void testJndiConnections() throws NamingException, SQLException {
		final DataSource mockDataSource;
		Connection mockConnection;

		System.setProperty("java.naming.factory.initial", "com.wrapper.MyContextFactory");

		// InitialContext mockInitialContext = (InitialContext)
		// NamingManager.getInitialContext(System.getProperties());
		mockDataSource = mock(DataSource.class);
		mockConnection = mock(Connection.class);

		// when(mockInitialContext.lookup(anyString())).thenReturn(mockDataSource);
		when(mockDataSource.getConnection()).thenReturn(mockConnection);

		String output1 = configurationListUtils.testJNDIConnections();
		// assertTrue(output1!=null );

		/*
		 * new MockUp<InitialContext>() {
		 * 
		 * @Mock public void $init() { }
		 * 
		 * @Mock public Object lookup(String name) { return ds; }
		 * 
		 * };
		 */

		// new MockInitialContext();
		String output = configurationListUtils.testJNDIConnections();
		// String expected = "<h1><font color=\"black\">JNDI RESOURCE LOOK UP
		// STATUS</font></h1><ul><font color=\"green\"><li>Connection to base DB server
		// <b>jdbc:oracle:thin:@localhost:1521:xe</b> is
		// successful.</li></font><br><br><font color=\"green\"><li>Connection to UCCE
		// base DB server <b>jdbc:oracle:thin:@localhost:1521:xe</b> is
		// successful.</li></font><br><br><font color=\"green\"><li>Connection to client
		// DB server <b>jdbc:oracle:thin:@localhost:1521:xe</b> is successful
		// [].</li></font><br><br><font color=\"green\"><li>Connection to Ucce client DB
		// server <b>jdbc:oracle:thin:@localhost:1521:xe</b> is successful
		// [].</li></font><br><br><font color=\"green\"><li>Connection to geolocation DB
		// server <b>jdbc:oracle:thin:@localhost:1521:xe</b> is
		// successful.</li></font><br><br></ul>";
		String expected = "<h1><font color=\"black\">JNDI RESOURCE LOOK UP STATUS</font></h1><ul><font color=\"green\"><li>Connection to geolocation DB server <b>jdbc:oracle:thin:@localhost:1521:xe</b> is successful.</li></font><br><br></ul>";
		// assertTrue(output.equals(expected));

	}
	
	@Test
	public void testJndiConnectionsException() throws NamingException, SQLException {
		
		
		try (MockedConstruction<InitialContext> initialContext = Mockito
				.mockConstruction(InitialContext.class, (mock, context) -> {
					when(mock.lookup(Mockito.any(String.class))).thenReturn(new Object());
				})) {
		try (MockedStatic<JNDIRepository> jNDIRepositoryMockedStatic = mockStatic(
				JNDIRepository.class)) {
			jNDIRepositoryMockedStatic
					.when(() -> JNDIRepository.getDBServerName(Mockito.anyString()))
					.thenReturn("jdbc:oracle:thin:@localhost:1521:xe");
			JNDIClientMapping mapping = new JNDIClientMapping();
			mapping.setJndiName("client");
			mapping.setDbUrl("jdbc:oracle:thin:@localhost:1521:xe");
			List<JNDIClientMapping> list = new ArrayList<JNDIClientMapping>();
			list.add(mapping);
			
			jNDIRepositoryMockedStatic
			.when(() -> JNDIRepository.getJNDIClientMappingList())
			.thenReturn(list);
			assertNotNull(configurationListUtils.testJNDIConnections());
		}	
			
		
		}
	}

	@Test
	public void testCacheKeySize() throws BaseCacheException {
		System.setProperty("dcLimitToShowCacheKeys", "50");
		CacheKeys keys = new CacheKeys();
		keys.setName("channel-widgetconfigurations:SystemParameters:*");

		List<CacheKeys> list = new ArrayList<CacheKeys>();
		list.add(keys);

		GenericRequestBean bean = new GenericRequestBean();
		bean.setLineage("19920_1.0");
		bean.setCacheKeys(list);
		// this lines handle case when get
		DistributedCacheUtil cacheManager = mock(DistributedCacheUtil.class);

		try (MockedStatic<DistributedCacheUtil> cacheManagerMock = mockStatic(DistributedCacheUtil.class)) {
			cacheManagerMock.when(DistributedCacheUtil::getInstance).thenReturn(cacheManager);
			when(cacheManager.getCachedObject(Mockito.anyString(), Mockito.anyString())).thenReturn(new Object());

			Map<String, Integer> response = (Map<String, Integer>) configurationListUtils.printCacheKeySize(bean);
			assertTrue(response != null);

			CacheKeys keys1 = new CacheKeys();
			keys1.setName("CWC:SystemParameters:*");

			List<CacheKeys> list1 = new ArrayList<CacheKeys>();
			list1.add(keys1);

			GenericRequestBean bean1 = new GenericRequestBean();
			bean1.setLineage("19920_1.0");
			bean1.setCacheKeys(list1);
			ReflectionTestUtils.setField(configurationListUtils, """
					sessionRedisEnable\
					""", true);
			Map<String, Integer> response1 = (Map<String, Integer>) configurationListUtils.printCacheKeySize(bean1);
			assertTrue(response1 != null);
		}

	}

	@Test
	public void testUPCWithException() throws JsonProcessingException {

		GenericRequestBean bean = new GenericRequestBean();
		bean.setLineage("19920_1.0");

		UPCKey upcKey = new UPCKey();
		upcKey.setWidgetName("ytrstmtviewportletv2_WAR_ahytrportlet");
		Map<Object, Object> requestParam= new HashMap<>();
		requestParam.put("test", "test");
		
		upcKey.setRequestParam(requestParam);
		Map<Object, ValueHolder> requestParamToValueHolderMap= new HashMap<>();
		requestParamToValueHolderMap.put(upcKey, new ValueHolder());
		upcKey.setRequestParamToValueHolderMap(requestParamToValueHolderMap);
		
		
		bean.setUpcKey(upcKey);
		new MockAdapterService(null);
		List<Map<Object, Object>> errorList = new ArrayList<Map<Object, Object>>();
		when(cmServicesUtil.buildServcieErrorResponseMap(Mockito.any(), Mockito.any())).thenReturn(errorList);
		Object resp = configurationListUtils.getUpcConfig(bean);
		assertNotNull(resp);
		Map<Object, Object> map =new HashMap<Object, Object>();
		map.put("test", "test");
		when(cmServicesUtil.getRequestParamFromValueHolder(Mockito.any(), Mockito.any())).thenReturn(map);
		
		configurationListUtils.getUpcConfig(bean);
		bean.setUpcKey(upcKey);
		configurationListUtils.getUpcConfig(bean);

	}

	@Test
	public void testClearHro() {
		//new MockHROCacheMap();
		HROCacheMap hroCacheMapMock = mock(HROCacheMap.class);
		hroCacheMapMock.put("HRO:"+"19920", null);
		when(hroCacheMapMock.containsKey(Mockito.any(Object.class))).thenReturn(true);
		try (MockedStatic<HROCacheMap> hROCacheMaprMockedStatic = mockStatic(
				HROCacheMap.class);) {

			hROCacheMaprMockedStatic.when(() -> HROCacheMap.getInstance())
					.thenReturn(hroCacheMapMock);
		Map<String, String> map = configurationListUtils.clearHro();

		assertNotNull(map);
		
		when(hroCacheMapMock.containsKey(Mockito.any(Object.class))).thenReturn(false);
		configurationListUtils.clearHro();
		}
	}

	@Test
	public void testClearHroAll() {

		new MockHROCacheMap();

		Map<String, String> map = configurationListUtils.clearHroAll();

		assertNotNull(map);

	}

	@Test
	public void testClearAFLinksCache() {
		// new MockShortCacheUtil();
		ShortCacheUtil cacheManager = mock(ShortCacheUtil.class);

		try (MockedStatic<ShortCacheUtil> cacheManagerMock = mockStatic(ShortCacheUtil.class)) {
			cacheManagerMock.when(ShortCacheUtil::getInstance).thenReturn(cacheManager);
			when(cacheManager.getAfLinksCache()).thenReturn(new HashMap<String, Map<String, List<String>>>());

			Map<String, String> map = configurationListUtils.clearAFLinksCache("00095_1.0");

			assertNotNull(map);
		}
	}

	@Test
	public void testClearAFLinksCacheAll() {

		ShortCacheUtil cacheManager = mock(ShortCacheUtil.class);

		try (MockedStatic<ShortCacheUtil> cacheManagerMock = mockStatic(ShortCacheUtil.class)) {
			cacheManagerMock.when(ShortCacheUtil::getInstance).thenReturn(cacheManager);
			when(cacheManager.getAfLinksCache()).thenReturn(new HashMap<String, Map<String, List<String>>>());
			Map<String, String> map = configurationListUtils.clearAFLinksCache("all");

			assertNotNull(map);
		}
	}

	@Test
	public void testisRedisCallFlag() {

		MockHttpServletRequest request1 = new MockHttpServletRequest();
		request1.addHeader("REDIS_CALL_FLAG", "true");
		Map<String, String> logconfigstoragemap = new HashMap<String, String>();
		ReflectionTestUtils.setField(configurationListUtils, """
				profile\
				""", "QA");
		boolean flag = configurationListUtils.isRedisCallFlag(request1, logconfigstoragemap);
		assertTrue(flag);

		// assertNotNull(map);

	}

	@Test
	public void testlogRedisCallCountInfoLog() {
		configurationListUtils.logRedisCallCountInfoLog();
		try (MockedStatic<LogInheritableThreadLocal> mockInheritableThreadLocal = Mockito
				.mockStatic(LogInheritableThreadLocal.class)) {
			mockInheritableThreadLocal.when(() -> LogInheritableThreadLocal.get()).thenReturn(getRedisCallCountStorage());
			
		configurationListUtils.logRedisCallCountInfoLog();
		
		mockInheritableThreadLocal.when(() -> LogInheritableThreadLocal.get()).thenReturn(null);
		configurationListUtils.logRedisCallCountInfoLog();
		
	}
		}

	@Test
	public void testlogRedisCallCountInfoLog1() {
		//new MockLogInheritableThreadLocalDup();
		configurationListUtils.logRedisCallCountInfoLog();
	}
	
	LogInheritableThreadLocal getRedisCallCountStorage(){
		RedisCallCountStorage redisCallCountStorage = new RedisCallCountStorage();
		List<Integer> redisCallCountList = new ArrayList<>();
		redisCallCountList.add(4);
		redisCallCountList.add(5);
		redisCallCountList.add(8);
		redisCallCountStorage.setRedisMgetCountList(redisCallCountList);
		redisCallCountStorage.setRedisSingleGetCount(12);
		
		LogInheritableThreadLocal instance = new LogInheritableThreadLocal();
		
		instance.put(UpointLogConstants.REDIS_CALL_COUNT_POOL, redisCallCountStorage);	
		
		return instance;		
		
	}

	@Test
	public void testClearAFLinksCacheNullCase() {
		new MockShortCacheUtil();

		configurationListUtils.clearAFLinksCache("");

	}

	@Test
	public void testClearAFLinksCacheEndsWith() {
		// new MockShortCacheUtil();
		ShortCacheUtil cacheManager = mock(ShortCacheUtil.class);

		try (MockedStatic<ShortCacheUtil> cacheManagerMock = mockStatic(ShortCacheUtil.class)) {
			cacheManagerMock.when(ShortCacheUtil::getInstance).thenReturn(cacheManager);
			when(cacheManager.getAfLinksCache()).thenReturn(new HashMap<String, Map<String, List<String>>>());
			Map<String, String> map = configurationListUtils.clearAFLinksCache("00095");

			assertNotNull(map);
		}
	}

	@Test
	public void testClearHroNullClient() {
		new MockHROCacheMap();
		messageAppContainer.setPrimaryClientId("");
		Map<String, String> map = configurationListUtils.clearHro();

		assertNotNull(map);

	}

	@Test
	public void testClearLinksCacheWithLineage() {
		// new MockShortCacheUtil();
		ShortCacheUtil cacheManager = mock(ShortCacheUtil.class);
		try (MockedStatic<ShortCacheUtil> cacheManagerMock = mockStatic(ShortCacheUtil.class)) {
			cacheManagerMock.when(ShortCacheUtil::getInstance).thenReturn(cacheManager);
			when(cacheManager.getUceLinksCache()).thenReturn(getUceLinksCache());

			when(cacheManager.getUceLinksCache()).thenReturn(new ConcurrentHashMap<String, Map<String, Link>>());

			Map<String, String> cachemap = configurationListUtils.clearLinksCache("00095_1.0", true);
			assertNotNull(cachemap);
			
			configurationListUtils.clearLinksCache("", true);
			
			configurationListUtils.clearLinksCache("all_1", false);
		}
	}

	@Test
	public void testClearLinksCacheALL() {
		// new MockShortCacheUtil();
		ShortCacheUtil cacheManager = mock(ShortCacheUtil.class);

		try (MockedStatic<ShortCacheUtil> cacheManagerMock = mockStatic(ShortCacheUtil.class)) {
			cacheManagerMock.when(ShortCacheUtil::getInstance).thenReturn(cacheManager);
			when(cacheManager.getUceLinksCache()).thenReturn(getUceLinksCache());

			when(cacheManager.getUceLinksCache()).thenReturn(new ConcurrentHashMap<String, Map<String, Link>>());

			Map<String, String> mapUCE = configurationListUtils.clearLinksCache("all", true);
			Map<String, String> mapUPT = configurationListUtils.clearLinksCache("all", false);
			assertNotNull(mapUCE);
			assertNotNull(mapUPT);
		}
	}

	@Test
	public void testClearExpressionsCacheWithLineage() {
		// new MockShortCacheUtil();
		ShortCacheUtil cacheManager = mock(ShortCacheUtil.class);
		try (MockedStatic<ShortCacheUtil> cacheManagerMock = mockStatic(ShortCacheUtil.class)) {
			cacheManagerMock.when(ShortCacheUtil::getInstance).thenReturn(cacheManager);
			when(cacheManager.getUceLinksCache()).thenReturn(getUceLinksCache());

			when(cacheManager.getUceLinksCache()).thenReturn(new ConcurrentHashMap<String, Map<String, Link>>());
			Map<String, String> cachemap = configurationListUtils.clearExpressionsCache("00095_1.0", true);
			assertNotNull(cachemap);
		}
	}

	@Test
	public void testClearExpressionsCacheAll() {
		// new MockShortCacheUtil();
		ShortCacheUtil cacheManager = mock(ShortCacheUtil.class);
		try (MockedStatic<ShortCacheUtil> cacheManagerMock = mockStatic(ShortCacheUtil.class)) {
			cacheManagerMock.when(ShortCacheUtil::getInstance).thenReturn(cacheManager);
			when(cacheManager.getUceLinksCache()).thenReturn(getUceLinksCache());

			when(cacheManager.getUceLinksCache()).thenReturn(new ConcurrentHashMap<String, Map<String, Link>>());
			Map<String, String> ucemap = configurationListUtils.clearExpressionsCache("all", true);
			assertNotNull(ucemap);
			Map<String, String> uptmap = configurationListUtils.clearExpressionsCache("all", false);
			assertNotNull(uptmap);
			configurationListUtils.clearExpressionsCache("", false);
			
			configurationListUtils.clearExpressionsCache("123", false);
		}
	}

	public void mockDistributedCacheUtil(DistributedCacheUtil distributedCacheUtil) {
		MockedStatic<DistributedCacheUtil> mockdistributedCacheUtil = Mockito.mockStatic(DistributedCacheUtil.class);
		mockdistributedCacheUtil.when(() -> DistributedCacheUtil.getInstance()).thenReturn(distributedCacheUtil);
	}

	public void mockAppConfigReader(AppConfigReader appConfigReader) {
		MockedStatic<AppConfigReader> appConfigReader1 = Mockito.mockStatic(AppConfigReader.class);
		appConfigReader1.when(() -> AppConfigReader.getInstance()).thenReturn(appConfigReader);
		Mockito.when(((AppConfigReader) appConfigReader1).isUpcJVMCacheClear()).thenReturn(true);
	}

	public void mockRedisImpl(ICache redisCompleteCacheMapProvider) throws BaseCacheException {
		RedisCacheMapProviderImpl redisCacheMapProviderImpl = Mockito.mock(RedisCacheMapProviderImpl.class);
		Mockito.when(redisCacheMapProviderImpl.getCompleteMap(Mockito.anyString())).thenAnswer(InvocationOnMock -> {
			HashMap<Object, Object> cMap = new HashMap<Object, Object>();
			Long redisTimestamp = 12345678L;
			cMap.put("19943_1.0", redisTimestamp);
			return cMap;
		});
	}

	@Test
	public void testClearUPCCache() throws Exception {
		// distributedGMCCacheUtil.saveObjectInCache("KEY2", "VALUE2",300);
		// MockitoAnnotations.initMocks(this);

		// mockDistributedCacheUtil(distributedCacheUtil);
		// mockRedisImpl(redisCompleteCacheMapProvider);
		// mockAppConfigReader(appConfigReader);
		// Mockito.mockStatic(UPCMemoryManagerClientCacheUtil.class);
		/*
		 * Mockito.when(UPCMemoryManagerClientCacheUtil.getUpcConfigJvmTimestamp()).
		 * thenAnswer(InvocationOnMock->{ Map<String, Long> upcConfigJvmTimestamp = new
		 * HashMap <String, Long>(); upcConfigJvmTimestamp.put("19943_1.0",(long)
		 * 123456); return upcConfigJvmTimestamp; });
		 */
		DistributedCacheUtil cacheManager = mock(DistributedCacheUtil.class);

		try (MockedStatic<DistributedCacheUtil> cacheManagerMock = mockStatic(DistributedCacheUtil.class)) {
			cacheManagerMock.when(DistributedCacheUtil::getInstance).thenReturn(cacheManager);
			when(cacheManager.getClientUpdatedTimeStampKey()).thenReturn("");
			Map<String, Long> upcConfigJvmTimestamp = new HashMap<String, Long>();
			upcConfigJvmTimestamp.put("19943_1.0", (long) 123456);
			uPCMemoryManagerClientCacheUtilMockedStatic
					.when(() -> UPCMemoryManagerClientCacheUtil.getUpcConfigJvmTimestamp())
					.thenReturn(upcConfigJvmTimestamp);

			HashMap<Object, Object> cMap = new HashMap<Object, Object>();
			Long redisTimestamp = 12345678L;
			cMap.put("19943_1.0", redisTimestamp);
			Mockito.when(redisCompleteCacheMapProviderMock.getCompleteMap(Mockito.anyString())).thenReturn(cMap);

			// Mockito.when(ReflectionTestUtils.invokeMethod(UPCMemoryManagerClientCacheUtil.class,"isUPCCacheEnabled",Mockito.anyString())).thenReturn(true);
			// Mockito.when(UPCMemoryManagerClientCacheUtil.isUPCAssetGroupCacheON(Mockito.anyString())).thenReturn(true);
			configurationListUtils.clearUpcConfigCache();
		}

	}

	@Test
	public void testClearUPCCacheFail() throws Exception {

		configurationListUtils.clearUpcConfigCache();

	}

	@Test
	public void testLogExpCallCountInfoLog_success() throws Exception {

		LogInheritableThreadLocal inheritableThreadLocal = MockLogInheritableThreadLocalExp.get();
		try (MockedStatic<LogInheritableThreadLocal> mockInheritableThreadLocal = Mockito
				.mockStatic(LogInheritableThreadLocal.class);
				MockedStatic<ExpressionDebugAssistUtil> expressionDebugAssistUtil = Mockito
						.mockStatic(ExpressionDebugAssistUtil.class)) {
			mockInheritableThreadLocal.when(() -> LogInheritableThreadLocal.get()).thenReturn(inheritableThreadLocal);
			when(LogInheritableThreadLocal.get(UpointLogConstants.COMMON_TYPE_LOG_EVENT, CommonTypeLogEvent.class))
					.thenReturn(mock(CommonTypeLogEvent.class));
			when(logbookPropertiesConfiguration.isPushToElk()).thenReturn(true);
			when(dynamoDbHelper.isDynamoDbActive()).thenReturn(true);
			when(dynamoDbHelper.saveOrUpdateItem(anyString(), anyString(), anyString())).thenReturn(true);
			expressionDebugAssistUtil.when(() -> ExpressionDebugAssistUtil.isExpressionsLoggingEnabled())
			.thenReturn(true);
			configurationListUtils.logExpCallCountInfoLog();
		}
	}

	@Test
	public void testLogExpCallCountInfoLog_return() throws Exception {

		try (MockedStatic<ExpressionDebugAssistUtil> mockInheritableThreadLocal = Mockito
				.mockStatic(ExpressionDebugAssistUtil.class)) {
			mockInheritableThreadLocal.when(() -> ExpressionDebugAssistUtil.isExpressionsLoggingEnabled())
					.thenReturn(false);
			configurationListUtils.logExpCallCountInfoLog();
			
			mockInheritableThreadLocal.when(() -> ExpressionDebugAssistUtil.isExpressionsLoggingEnabled())
			.thenReturn(true);
			configurationListUtils.logExpCallCountInfoLog();
		}
	}

	@Test
	public void testLogExpCallCountInfoLog_exception() throws Exception {

		try (MockedStatic<ExpressionDebugAssistUtil> mockInheritableThreadLocal = Mockito
				.mockStatic(ExpressionDebugAssistUtil.class)) {
			mockInheritableThreadLocal.when(() -> ExpressionDebugAssistUtil.isExpressionsLoggingEnabled())
					.thenThrow(RuntimeException.class);
			configurationListUtils.logExpCallCountInfoLog();
		}
	}

	@Test
	public void testLogExpCallCountInfoLog_nestedException() throws Exception {

		LogInheritableThreadLocal inheritableThreadLocal = MockLogInheritableThreadLocalExp.get();
		try (MockedStatic<LogInheritableThreadLocal> mockInheritableThreadLocal = Mockito
				.mockStatic(LogInheritableThreadLocal.class)) {
			mockInheritableThreadLocal.when(() -> LogInheritableThreadLocal.get()).thenReturn(inheritableThreadLocal);
			when(LogInheritableThreadLocal.get(UpointLogConstants.COMMON_TYPE_LOG_EVENT, CommonTypeLogEvent.class))
					.thenReturn(mock(CommonTypeLogEvent.class));
			when(logbookPropertiesConfiguration.isPushToElk()).thenReturn(true);
			when(dynamoDbHelper.isDynamoDbActive()).thenReturn(true);
			when(dynamoDbHelper.saveOrUpdateItem(anyString(), anyString(), anyString()))
					.thenThrow(RuntimeException.class);
			configurationListUtils.logExpCallCountInfoLog();
		}

	}

	public Map<String, Map<String, List<String>>> getAfLinksCache() {
		Map<String, Map<String, List<String>>> afLinksCache = new ConcurrentHashMap<String, Map<String, List<String>>>();
		List<String> linkNames = new ArrayList<String>();
		linkNames.add("Link1");
		linkNames.add("Link2");
		Map<String, List<String>> assetGroupMap = new HashMap<String, List<String>>();
		assetGroupMap.put("AssetGroup1", linkNames);
		afLinksCache.put("00095_1.0", assetGroupMap);
		return afLinksCache;
	}

	public Map<String, Map<String, Link>> getUptLinksCache() {
		Map<String, Map<String, Link>> uptLinksCache = new ConcurrentHashMap<String, Map<String, Link>>();
		Map<String, Link> linkmap = new HashMap<String, Link>();
		Link l1 = new Link();
		l1.setId("l1");
		Link l2 = new Link();
		l2.setId("l2");
		linkmap.put("l1", l1);
		linkmap.put("l2", l2);
		uptLinksCache.put("00095_1.0", linkmap);
		return uptLinksCache;
	}

	public Map<String, Map<String, Link>> getUceLinksCache() {
		Map<String, Map<String, Link>> uceLinksCache = new ConcurrentHashMap<String, Map<String, Link>>();
		Map<String, Link> linkmap = new HashMap<String, Link>();
		Link l1 = new Link();
		l1.setId("l1");
		Link l2 = new Link();
		l2.setId("l2");
		linkmap.put("l1", l1);
		linkmap.put("l2", l2);
		uceLinksCache.put("00095_1.0", linkmap);
		return uceLinksCache;
	}

	public Map<String, Map<String, Tag>> getUptLinkTagCache() {
		Map<String, Map<String, Tag>> uptLinkTagCache = new ConcurrentHashMap<String, Map<String, Tag>>();
		Map<String, Tag> tagmap = new HashMap<String, Tag>();
		Tag lt1 = new AnchorTag();
		Tag lt2 = new AnchorTag();
		tagmap.put("lt1", lt1);
		tagmap.put("lt2", lt2);
		uptLinkTagCache.put("00095_1.0", tagmap);
		return uptLinkTagCache;
	}

	public Map<String, Map<String, Tag>> getUceLinkTagCache() {
		Map<String, Map<String, Tag>> uceLinkTagCache = new ConcurrentHashMap<String, Map<String, Tag>>();
		Map<String, Tag> tagmap = new HashMap<String, Tag>();
		Tag lt1 = new AnchorTag();
		Tag lt2 = new AnchorTag();
		tagmap.put("lt1", lt1);
		tagmap.put("lt2", lt2);
		uceLinkTagCache.put("00095_1.0", tagmap);
		return uceLinkTagCache;
	}

	public Map<String, Map<String, Boolean>> getUptExprCache() {
		Map<String, Map<String, Boolean>> uptExprCache = new HashMap<String, Map<String, Boolean>>();
		Map<String, Boolean> exprMap = new HashMap<String, Boolean>();
		exprMap.put("ALWAYS_PASS", Boolean.TRUE);
		exprMap.put("ALWAYS_FAIL", Boolean.FALSE);
		uptExprCache.put("00095_1.0", exprMap);
		return uptExprCache;
	}

	public Map<String, Map<String, Boolean>> getUceExprCache() {
		Map<String, Map<String, Boolean>> uceExprCache = new HashMap<String, Map<String, Boolean>>();
		Map<String, Boolean> exprMap = new HashMap<String, Boolean>();
		exprMap.put("ALWAYS_PASS", Boolean.TRUE);
		exprMap.put("ALWAYS_FAIL", Boolean.FALSE);
		uceExprCache.put("00095_1.0", exprMap);
		return uceExprCache;
	}
	
	@Test
	public void testUPCWithFailResponse() throws JsonProcessingException {
		List<Map<Object, Object>> output = new ArrayList<Map<Object, Object>>();
		RestServiceInvocationResult serviceInvocationResult = new RestServiceInvocationResult();

		Map<Object, Object> widgetMap = new HashMap<Object, Object>();
		widgetMap.put("type", "expression");
		widgetMap.put("value", "false");

		Map<Object, Object> widgetMapResult = new HashMap<Object, Object>();
		widgetMapResult.put("IS_HEADER_FOOTER_V2", widgetMap);
		output.add(widgetMapResult);


		serviceInvocationResult.setResponseCMValues(output);
		serviceInvocationResult.setResponseStatus(RestServiceInvocationResult.ResponseStatusEnum.FAILURE);
		GenericRequestBean bean = new GenericRequestBean();
		bean.setLineage("19920_1.0");

		UPCKey upcKey = new UPCKey();
		upcKey.setWidgetName("ytrstmtviewportletv2_WAR_ahytrportlet");

		bean.setUpcKey(upcKey);
		//new MockAdapterService(serviceInvocationResult);
		
		try (MockedStatic<AdapterService> adapterServiceMockedStatic = mockStatic(
				AdapterService.class);) {

			adapterServiceMockedStatic.when(() -> AdapterService.getResponse(Mockito.any()))
					.thenReturn(serviceInvocationResult);
		Object resp = configurationListUtils.getUpcConfig(bean);
		//assertNotNull(resp);
		
		adapterServiceMockedStatic.when(() -> AdapterService.getResponse(Mockito.any()))
		.thenThrow(new NullPointerException());
		Object resp1 = configurationListUtils.getUpcConfig(bean);
		assertNotNull(resp1);
		
		}
	}

}