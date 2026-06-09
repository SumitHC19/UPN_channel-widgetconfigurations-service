package com.aonhewitt.upoint.core.controller.unit.config;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mockStatic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.alight.upoint.udp.beans.UDPInvocationResult;
import com.aonhewitt.beans.GenericRequestBean;
import com.aonhewitt.portal.core.service.AHUserLocalServiceUtil;
import com.aonhewitt.portal.core.service.util.CmServicesUtil;
import com.aonhewitt.portal.core.service.util.CommonUtil;
import com.aonhewitt.portal.core.udp.pojo.person.PersonService;
import com.aonhewitt.portal.hasbro.udp.base.service.pojo.UDPResponse;
import com.aonhewitt.portal.service.delegators.ConfigManagerService;
import com.aonhewitt.portal.userprovisioning.beans.AHUser;
import com.aonhewitt.portal.util.AppContainerProvider;
import com.aonhewitt.portal.util.MessageAppContainer;
import com.aonhewitt.portal.util.RestServiceInvocationResult;
import com.aonhewitt.portal.util.RestServiceInvocationResult.ResponseStatusEnum;
import com.aonhewitt.upoint.conf.AppConfig;
import com.aonhewitt.upoint.core.controller.config.ConfigurationListController;
import com.aonhewitt.upoint.core.service.util.ExpressionsCacheUtility;
import com.aonhewitt.upoint.util.CommonUtils;
import com.aonhewitt.upoint.util.ConfigurationListUtils;

@ExtendWith(SpringExtension.class)
public class ConfigurationListControllerTestV2 {

	@Mock
	private ConfigManagerService configManagerService;

	@Mock
	private CmServicesUtil cmServicesUtil;

	@Mock
	private ConfigurationListUtils configurationListUtils;

	@Mock
	private AppConfig appConfig;

	@Mock
	private ExpressionsCacheUtility expressionsCacheUtility;

	@InjectMocks
	ConfigurationListController configurationListController;

	private String profile;

	private MessageAppContainer messageAppContainer = new MessageAppContainer();

	private List<String> profileSet = Arrays.asList(new String[] { "localdev", "int", "qa", "ste" });
	private List<String> profileSetUptoQc = Arrays.asList(new String[] { "localdev", "int", "qa", "ste", "qc" });

	private static MockedStatic<CommonUtils> commonUtilsMockedStatic;
	private static MockedStatic<AHUserLocalServiceUtil> aHUserLocalServiceUtilMockedStatic;

	@BeforeAll
	public static void init() {
		Mockito.clearAllCaches();
		commonUtilsMockedStatic = mockStatic(CommonUtils.class);
		aHUserLocalServiceUtilMockedStatic = mockStatic(AHUserLocalServiceUtil.class);
	}

	@AfterAll
	public static void close() {

		commonUtilsMockedStatic.close();
		aHUserLocalServiceUtilMockedStatic.close();
	}

	GenericRequestBean aRequestBean = null;

	@BeforeEach
	public void setup() throws Exception {
		AHUser u = new AHUser();
		u.setLocale("en_US");

		aHUserLocalServiceUtilMockedStatic.when(() -> AHUserLocalServiceUtil.getAHUser()).thenReturn(u);
		aRequestBean = new GenericRequestBean();
		aRequestBean.setLineage("19920_1.0");
		aRequestBean.setLocale("en_US");

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
		messageAppContainer.setPrimaryClientId("123");
		AppContainerProvider.setContainer(messageAppContainer);
	}

	@Test
	public void getConfigurationListTestWithxssAttackCheckEnabled() {
		ReflectionTestUtils.setField(configurationListController, "xssAttackCheckEnabled", true);

		Map<String, String> validationFailStatus = new HashMap<>();
		validationFailStatus.put("test", "pass");
		commonUtilsMockedStatic.when(() -> CommonUtils.validateRequestForXSSAttacks(Mockito.any()))
				.thenReturn(validationFailStatus);
		Map<String, String> reObject = (Map<String, String>) configurationListController
				.getConfigurationList(aRequestBean);
		assertEquals("pass", reObject.get("test"));

	}

	@Test
	public void getConfigurationListUnsupportedRequestTest() {

		try (MockedStatic<CommonUtil> commonUtilMockedStatic = mockStatic(CommonUtil.class);) {
			commonUtilMockedStatic.when(() -> CommonUtil.getDomainLineage()).thenReturn("CM");
			Map<String, String> reObject = (Map<String, String>) configurationListController
					.getConfigurationList(aRequestBean);

			assertEquals("Unsupported Request Operation!!!", reObject.get("Error!"));

		}

	}

	@Test
	public void getConfigurationListTestWithOperationTyepConfigSetException() {
		try (MockedStatic<CommonUtil> commonUtilMockedStatic = mockStatic(CommonUtil.class);) {
			commonUtilMockedStatic.when(() -> CommonUtil.getDomainLineage()).thenReturn("CM");
			aRequestBean.setOperation("configSet");
			aRequestBean.setUce("true");
			Object reObject = configurationListController.getConfigurationList(aRequestBean);

			assertNotNull(reObject);

		}

	}

	@Test
	public void getConfigurationListWithOperationTyepConfigSetTest() {
		try (MockedStatic<CommonUtil> commonUtilMockedStatic = mockStatic(CommonUtil.class);) {
			commonUtilMockedStatic.when(() -> CommonUtil.getDomainLineage()).thenReturn("CM");
			aRequestBean.setOperation("configSet");
			aRequestBean.setUce("true");
			RestServiceInvocationResult restServiceInvocationResult = new RestServiceInvocationResult();
			restServiceInvocationResult.setResponseStatus(ResponseStatusEnum.SUCCESS);
			Map<Object, Object> map = new HashMap<>();
			map.put("cm", "cm");
			List<Map<Object, Object>> cmList = new ArrayList<>();
			cmList.add(map);

			restServiceInvocationResult.setResponseCMValues(cmList);
			Mockito.when(configManagerService.getResponse(aRequestBean)).thenReturn(restServiceInvocationResult);

			List<Map<Object, Object>> reObject = (List<Map<Object, Object>>) configurationListController
					.getConfigurationList(aRequestBean);

			assertEquals("cm", reObject.get(0).get("cm"));

		}

	}

	@Test
	public void getConfigurationListWithOperationTyepUPCTest() {
		try (MockedStatic<CommonUtil> commonUtilMockedStatic = mockStatic(CommonUtil.class);) {
			commonUtilMockedStatic.when(() -> CommonUtil.getDomainLineage()).thenReturn("CM");
			aRequestBean.setOperation("UPC");
			aRequestBean.setUce("true");
			RestServiceInvocationResult restServiceInvocationResult = new RestServiceInvocationResult();
			restServiceInvocationResult.setResponseStatus(ResponseStatusEnum.SUCCESS);
			Map<Object, Object> map = new HashMap<>();
			map.put("cm", "cm");
			List<Map<Object, Object>> cmList = new ArrayList<>();
			cmList.add(map);

			restServiceInvocationResult.setResponseCMValues(cmList);
			Mockito.when(configurationListUtils.getUpcConfig(aRequestBean)).thenReturn(cmList);

			List<Map<Object, Object>> reObject = (List<Map<Object, Object>>) configurationListController
					.getConfigurationList(aRequestBean);

			assertEquals("cm", reObject.get(0).get("cm"));

		}

	}

	@Test
	public void getConfigurationListWithOperationTyepahUserTest() {
		try (MockedStatic<CommonUtil> commonUtilMockedStatic = mockStatic(CommonUtil.class);) {
			commonUtilMockedStatic.when(() -> CommonUtil.getDomainLineage()).thenReturn("CM");
			aRequestBean.setOperation("ahUser");
			aRequestBean.setUce("true");
			RestServiceInvocationResult restServiceInvocationResult = new RestServiceInvocationResult();
			restServiceInvocationResult.setResponseStatus(ResponseStatusEnum.SUCCESS);
			Map<String, Object> map = new HashMap<>();
			map.put("cm", "cm");

			Mockito.when(configurationListUtils.getAhUserObject()).thenReturn(map);
			Mockito.when(appConfig.getProfile()).thenReturn("int");

			// Mockito.when(appConfig.getProfile()).thenReturn("test");
			// configurationListController.getConfigurationList(aRequestBean);
			Map<String, Object> reObject = (Map<String, Object>) configurationListController
					.getConfigurationList(aRequestBean);
			assertEquals("cm", reObject.get("cm"));

			aRequestBean.setOperation("requestPayload");
			Map<String, Object> reObject2 = (Map<String, Object>) configurationListController
					.getConfigurationList(aRequestBean);
			assertEquals("cm", reObject.get("cm"));

			Mockito.when(configurationListUtils.testJNDIConnections()).thenReturn("tested");
			aRequestBean.setOperation("dbDetails");
			String reObjectTested = (String) configurationListController.getConfigurationList(aRequestBean);
			assertEquals("tested", reObjectTested);

			Mockito.when(configurationListUtils.verifyCacheKey(Mockito.any())).thenReturn(new Object());
			aRequestBean.setOperation("cacheSet");
			configurationListController.getConfigurationList(aRequestBean);

			UDPResponse res = new PersonService();
			res.setResponseCode("200");
			Mockito.when(configurationListUtils.getUdpData()).thenReturn(res);
			aRequestBean.setOperation("udp");
			configurationListController.getConfigurationList(aRequestBean);

			Map<String, Object> aggregateMapResult = new HashMap<String, Object>();
			aggregateMapResult.put("JedisConnectionFactory Client Name", "" + "Premier");
			aggregateMapResult.put("JedisConnectionFactory Time Out", "" + 0);
			aggregateMapResult.put("JedisConnectionFactory Host Name", "localhost");
			aggregateMapResult.put("JedisConnectionFactory Port No", "" + "6379");
			aggregateMapResult.put("jedisPoolConfig Max Idle", "" + 0);
			aggregateMapResult.put("jedisPoolConfig Max Idle", "" + 0);
			aggregateMapResult.put("jedisPoolConfig Max Total", "" + 0);
			aggregateMapResult.put("jedisPoolConfig Min Idle", "" + 0);
			aggregateMapResult.put("jedisPoolConfig Max Wait", "" + 0);
			aggregateMapResult.put("jedisPoolConfig MinEvictableIdleTime", "" + 0);
			Mockito.when(configurationListUtils.printCacheKeySize(Mockito.any(GenericRequestBean.class)))
					.thenReturn(aggregateMapResult);
			aRequestBean.setOperation("cacheAnalysis");
			configurationListController.getConfigurationList(aRequestBean);

			Mockito.when(configurationListUtils.printJedisPoolConfig()).thenReturn(aggregateMapResult);
			aRequestBean.setOperation("jedisPoolConfig");
			Map<String, Object> reObject3 = (Map<String, Object>)configurationListController.getConfigurationList(aRequestBean);
			assertEquals("Premier", reObject3.get("JedisConnectionFactory Client Name"));

		}

	}
	
	@Test
	public void getConfigurationListWithOperationTest() {
		try (MockedStatic<CommonUtil> commonUtilMockedStatic = mockStatic(CommonUtil.class);) {
			commonUtilMockedStatic.when(() -> CommonUtil.getDomainLineage()).thenReturn("CM");
			aRequestBean.setOperation("ahUser");
			aRequestBean.setUce("true");
			RestServiceInvocationResult restServiceInvocationResult = new RestServiceInvocationResult();
			restServiceInvocationResult.setResponseStatus(ResponseStatusEnum.SUCCESS);
			Map<String, Object> map = new HashMap<>();
			map.put("cm", "cm");

			Mockito.when(configurationListUtils.getAhUserObject()).thenReturn(map);
			Mockito.when(appConfig.getProfile()).thenReturn("int");



			Map<String, Object> aggregateMapResult = new HashMap<String, Object>();
			aggregateMapResult.put("JedisConnectionFactory Client Name", "" + "Premier");
			aggregateMapResult.put("JedisConnectionFactory Time Out", "" + 0);
			aggregateMapResult.put("JedisConnectionFactory Host Name", "localhost");
			aggregateMapResult.put("JedisConnectionFactory Port No", "" + "6379");
			aggregateMapResult.put("jedisPoolConfig Max Idle", "" + 0);
			aggregateMapResult.put("jedisPoolConfig Max Idle", "" + 0);
			aggregateMapResult.put("jedisPoolConfig Max Total", "" + 0);
			aggregateMapResult.put("jedisPoolConfig Min Idle", "" + 0);
			aggregateMapResult.put("jedisPoolConfig Max Wait", "" + 0);
			aggregateMapResult.put("jedisPoolConfig MinEvictableIdleTime", "" + 0);
			Mockito.when(configurationListUtils.printCacheKeySize(Mockito.any(GenericRequestBean.class)))
					.thenReturn(aggregateMapResult);
			aRequestBean.setOperation("cacheAnalysis");
			configurationListController.getConfigurationList(aRequestBean);

			Mockito.when(configurationListUtils.printJedisPoolConfig()).thenReturn(aggregateMapResult);
			aRequestBean.setOperation("jedisPoolConfig");
			Map<String, Object> reObject3 = (Map<String, Object>)configurationListController.getConfigurationList(aRequestBean);

			assertEquals("Premier", reObject3.get("JedisConnectionFactory Client Name"));

		}

	}
	
	@Test
	public void getConfigurationListWithOperationudpNextTest() {
		try (MockedStatic<CommonUtil> commonUtilMockedStatic = mockStatic(CommonUtil.class);) {
			commonUtilMockedStatic.when(() -> CommonUtil.getDomainLineage()).thenReturn("CM");
			aRequestBean.setOperation("ahUser");
			aRequestBean.setUce("true");
			RestServiceInvocationResult restServiceInvocationResult = new RestServiceInvocationResult();
			restServiceInvocationResult.setResponseStatus(ResponseStatusEnum.SUCCESS);
			Map<String, Object> map = new HashMap<>();
			map.put("cm", "cm");
			Mockito.when(configurationListUtils.getAhUserObject()).thenReturn(map);
			Mockito.when(appConfig.getProfile()).thenReturn("int");
			

			com.alight.upoint.udp.beans.UDPInvocationResult invoResult=new com.alight.upoint.udp.beans.UDPInvocationResult();
			invoResult.setStatusCode(2);
			invoResult.setHttpStatusMessage("Udp Response returned successfully");
			
	         
	         Mockito.when(configurationListUtils.getUdpNextPersonSchemaObject()).thenReturn(invoResult);
			aRequestBean.setOperation("udpNext");
			Object object=configurationListController.getConfigurationList(aRequestBean);

			assertEquals(2, ((UDPInvocationResult) object).getStatusCode());

		}
	}
	
	@Test
	public void getConfigurationListWithOperationClearHroTest() {
		try (MockedStatic<CommonUtil> commonUtilMockedStatic = mockStatic(CommonUtil.class);) {
			commonUtilMockedStatic.when(() -> CommonUtil.getDomainLineage()).thenReturn("CM");
			aRequestBean.setOperation("ahUser");
			aRequestBean.setUce("true");
			RestServiceInvocationResult restServiceInvocationResult = new RestServiceInvocationResult();
			restServiceInvocationResult.setResponseStatus(ResponseStatusEnum.SUCCESS);
			Map<String, Object> map = new HashMap<>();
			map.put("cm", "cm");
			Mockito.when(configurationListUtils.getAhUserObject()).thenReturn(map);
			Mockito.when(appConfig.getProfile()).thenReturn("int");
			

			aRequestBean.setOperation("clearHro");
			Mockito.when(configurationListUtils.clearHro()).thenReturn(new HashMap<String, String>());
			configurationListController.getConfigurationList(aRequestBean);
			
			aRequestBean.setOperation("clearHroAll");
			Mockito.when(configurationListUtils.clearHroAll()).thenReturn(new HashMap<String, String>());
			configurationListController.getConfigurationList(aRequestBean);

			//assertEquals(2, ((UDPInvocationResult) object).getStatusCode());

		}
	}
	
	@Test
	public void getConfigurationListWithOperationclearAFLinksCacheTest() {
		try (MockedStatic<CommonUtil> commonUtilMockedStatic = mockStatic(CommonUtil.class);) {
			commonUtilMockedStatic.when(() -> CommonUtil.getDomainLineage()).thenReturn("CM");
			aRequestBean.setOperation("ahUser");
			aRequestBean.setUce("true");
			RestServiceInvocationResult restServiceInvocationResult = new RestServiceInvocationResult();
			restServiceInvocationResult.setResponseStatus(ResponseStatusEnum.SUCCESS);
			Map<String, Object> map = new HashMap<>();
			map.put("cm", "cm");
			Mockito.when(configurationListUtils.getAhUserObject()).thenReturn(map);
			Mockito.when(appConfig.getProfile()).thenReturn("int");
			

			Map<String, String> map1=new HashMap<String, String>();
			map1.put("clearAFLinksCache", "testData");
			
			aRequestBean.setOperation("clearAFLinksCache");
			Mockito.when(configurationListUtils.clearAFLinksCache(Mockito.any())).thenReturn(map1);
			Map<String, String>  result =(Map<String, String> )configurationListController.getConfigurationList(aRequestBean);

			aRequestBean.setOperation("clearAFLinksCacheAll");
			configurationListController.getConfigurationList(aRequestBean);
			
			assertEquals("testData", result.get("clearAFLinksCache"));
			
			

		}
	}
	
	@Test
	public void getConfigurationListWithOperationclearLinksCacheTest() {
		try (MockedStatic<CommonUtil> commonUtilMockedStatic = mockStatic(CommonUtil.class);) {
			commonUtilMockedStatic.when(() -> CommonUtil.getDomainLineage()).thenReturn("CM");
			aRequestBean.setOperation("ahUser");
			aRequestBean.setUce("true");
			RestServiceInvocationResult restServiceInvocationResult = new RestServiceInvocationResult();
			restServiceInvocationResult.setResponseStatus(ResponseStatusEnum.SUCCESS);
			Map<String, Object> map = new HashMap<>();
			map.put("cm", "cm");
			Mockito.when(configurationListUtils.getAhUserObject()).thenReturn(map);
			Mockito.when(appConfig.getProfile()).thenReturn("int");
			

			Map<String, String> map1=new HashMap<String, String>();
			map1.put("clearLinksCache", "testData");
			
			aRequestBean.setOperation("clearLinksCache");
			Mockito.when(configurationListUtils.clearLinksCache(Mockito.any(String.class),Mockito.any(Boolean.class))).thenReturn(map1);
			Map<String, String>  result =(Map<String, String> )configurationListController.getConfigurationList(aRequestBean);

			aRequestBean.setOperation("clearLinksCacheAll");
			configurationListController.getConfigurationList(aRequestBean);
			
			assertEquals("testData", result.get("clearLinksCache"));
			
			

		}
	}
	
	@Test
	public void getConfigurationListWithOperationclearExpressionsCacheTest() {
		try (MockedStatic<CommonUtil> commonUtilMockedStatic = mockStatic(CommonUtil.class);) {
			commonUtilMockedStatic.when(() -> CommonUtil.getDomainLineage()).thenReturn("CM");
			aRequestBean.setOperation("ahUser");
			aRequestBean.setUce("true");
			RestServiceInvocationResult restServiceInvocationResult = new RestServiceInvocationResult();
			restServiceInvocationResult.setResponseStatus(ResponseStatusEnum.SUCCESS);
			Map<String, Object> map = new HashMap<>();
			map.put("cm", "cm");
			Mockito.when(configurationListUtils.getAhUserObject()).thenReturn(map);
			Mockito.when(appConfig.getProfile()).thenReturn("int");
			

			Map<String, String> map1=new HashMap<String, String>();
			map1.put("clearExpressionsCache", "testData");
			
			aRequestBean.setOperation("clearExpressionsCache");
			Mockito.when(configurationListUtils.clearExpressionsCache(Mockito.any(String.class),Mockito.any(Boolean.class))).thenReturn(map1);
			Map<String, String>  result =(Map<String, String> )configurationListController.getConfigurationList(aRequestBean);

			aRequestBean.setOperation("clearExpressionsCacheAll");
			configurationListController.getConfigurationList(aRequestBean);
			
			assertEquals("testData", result.get("clearExpressionsCache"));
			
			

		}
	}
	
	@Test
	public void getConfigurationListWithOperationgetBusinessObjectTest() {
		try (MockedStatic<CommonUtil> commonUtilMockedStatic = mockStatic(CommonUtil.class);) {
			commonUtilMockedStatic.when(() -> CommonUtil.getDomainLineage()).thenReturn("CM");
			aRequestBean.setOperation("ahUser");
			aRequestBean.setUce("true");
			RestServiceInvocationResult restServiceInvocationResult = new RestServiceInvocationResult();
			restServiceInvocationResult.setResponseStatus(ResponseStatusEnum.SUCCESS);
			Map<String, Object> map = new HashMap<>();
			map.put("cm", "cm");
			Mockito.when(configurationListUtils.getAhUserObject()).thenReturn(map);
			Mockito.when(appConfig.getProfile()).thenReturn("int");
			
			aRequestBean.setOperation("getBusinessObject");
			Mockito.when(configurationListUtils.getBusinessDomainObject()).thenReturn(new Object());
		    configurationListController.getConfigurationList(aRequestBean);

			Mockito.when(configurationListUtils.getUpdatedARHForSharedAccess()).thenReturn("BusinessService Object is null, So ARH cannot be updated.");
			aRequestBean.setOperation("getUpdatedARHForsharedAccess");
			Object  object= configurationListController.getConfigurationList(aRequestBean);
			assertNotNull(object);
			
			

		}
	}
	
	@Test
	public void getConfigurationListWithOperationAhUserProd() {
		Mockito.when(appConfig.getProfile()).thenReturn("prod");
		try (MockedStatic<CommonUtil> commonUtilMockedStatic = mockStatic(CommonUtil.class);) {
			commonUtilMockedStatic.when(() -> CommonUtil.getDomainLineage()).thenReturn("CM");
			RestServiceInvocationResult restServiceInvocationResult = new RestServiceInvocationResult();
			restServiceInvocationResult.setResponseStatus(ResponseStatusEnum.SUCCESS);
			Map<String, Object> map = new HashMap<>();
			map.put("cm", "cm");
			Mockito.when(configurationListUtils.getAhUserObject()).thenReturn(map);
			Mockito.when(appConfig.getProfile()).thenReturn("prod");
			aRequestBean.setOperation("ahUser");
			Mockito.when(configurationListUtils.getBusinessDomainObject()).thenReturn(new Object());
		    configurationListController.getConfigurationList(aRequestBean);
		}
	}
	
	@Test
	public void getPreAuthWidgetConfigurationListTestWithxssAttackCheckEnabled() {
		ReflectionTestUtils.setField(configurationListController, "xssAttackCheckEnabled", true);

		Map<String, String> validationFailStatus = new HashMap<>();
		validationFailStatus.put("test", "pass");
		commonUtilsMockedStatic.when(() -> CommonUtils.validateRequestForXSSAttacks(Mockito.any()))
				.thenReturn(validationFailStatus);
		Map<String, String> reObject = (Map<String, String>) configurationListController
				.getPreAuthWidgetConfigurationList(aRequestBean);
		assertEquals("pass", reObject.get("test"));

	}
	
	@Test
	public void getPreAuthWidgetConfigurationListUnsupportedRequestTest() {

		try (MockedStatic<CommonUtil> commonUtilMockedStatic = mockStatic(CommonUtil.class);) {
			commonUtilMockedStatic.when(() -> CommonUtil.getDomainLineage()).thenReturn("CM");
			Map<String, String> reObject = (Map<String, String>) configurationListController
					.getPreAuthWidgetConfigurationList(aRequestBean);

			assertEquals("Unsupported Request Operation!!!", reObject.get("Error!"));

		}

	}
	@Test
	public void getPreAuthWidgetConfigurationListWithOperationTyepConfigSetException() {
		try (MockedStatic<CommonUtil> commonUtilMockedStatic = mockStatic(CommonUtil.class);) {
			commonUtilMockedStatic.when(() -> CommonUtil.getDomainLineage()).thenReturn("CM");
			aRequestBean.setOperation("configSet");
			aRequestBean.setUce("true");
			Object reObject = configurationListController.getPreAuthWidgetConfigurationList(aRequestBean);

			assertNotNull(reObject);

		}

	}


	@Test
	public void getPreAuthWidgetConfigurationListConfigSetTest() {
		try (MockedStatic<CommonUtil> commonUtilMockedStatic = mockStatic(CommonUtil.class);) {
			commonUtilMockedStatic.when(() -> CommonUtil.getDomainLineage()).thenReturn("CM");
			aRequestBean.setOperation("UPC");
			aRequestBean.setUce("true");
			RestServiceInvocationResult restServiceInvocationResult = new RestServiceInvocationResult();
			restServiceInvocationResult.setResponseStatus(ResponseStatusEnum.SUCCESS);
			Map<Object, Object> map = new HashMap<>();
			map.put("cm", "cm");
			List<Map<Object, Object>> cmList = new ArrayList<>();
			cmList.add(map);
			Mockito.when(configurationListUtils.getUpcConfig(aRequestBean)).thenReturn(cmList);

			restServiceInvocationResult.setResponseCMValues(cmList);
			Mockito.when(configManagerService.getResponse(aRequestBean)).thenReturn(restServiceInvocationResult);

			List<Map<Object, Object>> reObject = (List<Map<Object, Object>>) configurationListController
					.getPreAuthWidgetConfigurationList(aRequestBean);

			assertEquals("cm", reObject.get(0).get("cm"));

		}

	}
}
