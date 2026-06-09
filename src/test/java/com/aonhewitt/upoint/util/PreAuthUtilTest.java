package com.aonhewitt.upoint.util;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.aonhewitt.portal.adapter.util.DistributedRcsCacheUtil;
import com.aonhewitt.portal.util.AppContainerProvider;
import com.aonhewitt.portal.util.MessageAppContainer;
import com.aonhewitt.upoint.cache.config.provider.ICache;
import com.aonhewitt.upoint.cache.exception.BaseCacheException;

import jakarta.servlet.http.HttpServletRequest;

public class PreAuthUtilTest {

	  @Mock
	  private HttpServletRequest request;
	
	  
	private MessageAppContainer messageAppContainer = new MessageAppContainer();
	
	@InjectMocks
	PreAuthUtil preAuthUtil;

	@Mock
	ICache redisCacheProvider;
	
	@BeforeEach
	public void setup(){
		
		// messageAppContainer.setAsgSessionToken(aSGSessionToken);
		messageAppContainer.setPreAuthRequest(true);
		AppContainerProvider.setContainer(messageAppContainer);
	}
	
	@Test
	public void testIsPreAuthUrl(){
		
		boolean isPreAuth = PreAuthUtil.isPreAuthUrl("/public");
		assertTrue(isPreAuth);
	}
	
	
	@Test
	public void testPopulateMessageContainer(){
		
		String alightRequestHeader= "{\"locale\":\"en_US\",\"clientId\":\"19920\",\"consumerReferenceId\":\"3434\",\"channelRequestData\":\"3434\"}";
		PreAuthUtil.populateMessageContainer(alightRequestHeader);
		assertTrue(messageAppContainer.isPreAuthRequest());
	}
	
	@Test
	public void testPopulateMessageContainerWithoutAlightRequestHeader(){
		assertThrows(Exception.class, () -> {

			String alightRequestHeader = null;
			PreAuthUtil.populateMessageContainer(alightRequestHeader);
			assertTrue(messageAppContainer.isPreAuthRequest());
		});
	}
	
	@Test
	public void testPopulateMessageContainerWithoutLocale(){
		assertThrows(Exception.class, () -> {
			String alightRequestHeader = "{\"clientId\":\"19920\",\"consumerReferenceId\":\"3434\",\"channelRequestData\":\"3434\"}";

			PreAuthUtil.populateMessageContainer(alightRequestHeader);
			assertTrue(messageAppContainer.isPreAuthRequest());
		});
	}
	
	@Test
	public void testPopulateMessageContainerWithBlankAlightRequestHeader(){
		assertThrows(Exception.class, () -> {
			String alightRequestHeader = "{}";

			PreAuthUtil.populateMessageContainer(alightRequestHeader);
			assertTrue(messageAppContainer.isPreAuthRequest());
		});
	}
	

	@Test
	public void getPreAuthDataFromCache() throws BaseCacheException {
		
		preAuthUtil.getPreAuthDataFromCache("cacheName", "header");
		
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.addHeader("AF_ENABLED", "true");
		request.addHeader("afSwitchOn", "true");
		request.addHeader("AF_ENABLED", "true");
		request.addParameter("requestInfoLogParam", "requestInfoLogParam");
		request.addParameter("showCachedShortCacheInfo", "showCachedShortCacheInfo");
		request.addParameter("showJVMShortCacheInfo", "showJVMShortCacheInfo");
		request.addParameter("allClients", "allClients");
		String alightSessionToken = "{\"accessToken\":\"eyJhbGciOiJg\",\"sessionId\":\"472874c1\",\"idMapping\":\"[ { \\\"platformType\\\" : \\\"TBA_4X\\\", \\\"domain\\\" : \\\"DC,DB\\\", \\\"clientId\\\" : \\\"19920\\\", \\\"normalizedClientid\\\" : \\\"19920\\\", \\\"systemInstanceId\\\" : \\\"\\\", \\\"platformInternalId\\\" : \\\"184000044\\\", \\\"platformExternalId\\\" : \\\"140626002\\\", \\\"roleType\\\" : \\\"rkp\\\" } ]\",\"channelUserProfile\":\"null\",\"brokerUserId\":\"Portal\",\"clientId\":\"19920\",\"systemInstanceId\":\"null\",\"testCfg\":\"@@SB\",\"subjectId\":\"null\",\"subjectType\":\"null\",\"systemDate\":\"null\",\"locale\":\"en_US\",\"roleId\":\"null\",\"domainDocumentMapping\":\"null\",\"consumerReferenceId\":\"null\",\"expires\":\"1510649321900\"}";

		request.addHeader("alightPersonSessionToken", alightSessionToken);

		String arh = "{\"locale\":\"en_US\",\"channelRequestData\":\"URL::login::groupId::6727607::orgName::hmorg::device::Desktop::gblsId::9aeb05d1-c150-41b7-96e0-31185803cfeb_2023-06-29-06.06.20.080000::uxPageRequestId::null::pageName::login::deviceType::null::sessionCreatedTimestamp::null::widgetName::null::\",\"clientId\":\"19968\"}";
		request.addHeader("alightRequestHeader", arh);
		ServletRequestAttributes attrs = new ServletRequestAttributes(request);
		RequestContextHolder.setRequestAttributes(attrs);
		try (MockedStatic<DistributedRcsCacheUtil> distributedRcsCacheUtilMockedStatic = mockStatic(DistributedRcsCacheUtil.class);) {
			DistributedRcsCacheUtil distributedRcsCacheUtil = mock(DistributedRcsCacheUtil.class);
			Mockito.when(distributedRcsCacheUtil.getObjectFromCache(Mockito.anyString())).thenReturn(new Object());
			distributedRcsCacheUtilMockedStatic.when(() -> DistributedRcsCacheUtil.getInstance()).thenReturn(distributedRcsCacheUtil);
		assertNull(preAuthUtil.getPreAuthDataFromCache("cacheName", "header"));
		
	}
	}
	
	@Test
	public void setPreAuthDataInCacheTest() throws BaseCacheException {
		try (MockedStatic<DistributedRcsCacheUtil> distributedRcsCacheUtilMockedStatic = mockStatic(DistributedRcsCacheUtil.class);) {
			DistributedRcsCacheUtil distributedRcsCacheUtil=mock(DistributedRcsCacheUtil.class);
			distributedRcsCacheUtilMockedStatic.when(() -> DistributedRcsCacheUtil.getInstance()).thenReturn(distributedRcsCacheUtil);
			doNothing().when(distributedRcsCacheUtil).saveObjectInCache(Mockito.anyString(), Mockito.any(Object.class), Mockito.anyLong());
			preAuthUtil.setPreAuthDataInCache("header", 1l, messageAppContainer);
			preAuthUtil.setPreAuthDataInCache("afinit", 1l, messageAppContainer);
	}
	}
}