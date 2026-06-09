package com.aonhewitt.upoint.core.controller.unit.widget;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
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
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.alight.asg.model.token.v1_0.PersonSessionToken;
import com.aonhewitt.logging.helpers.ErrorLogEventHelper;
import com.aonhewitt.portal.adapter.AdapterService;
import com.aonhewitt.portal.configuration.modules.assetmanagement.model.AssetDTO;
import com.aonhewitt.portal.core.service.AHUserLocalServiceUtil;
import com.aonhewitt.portal.core.service.util.CmServicesUtil;
import com.aonhewitt.portal.feature.util.CoreFeatureUtil;
import com.aonhewitt.portal.service.delegators.ConfigManagerService;
import com.aonhewitt.portal.userprovisioning.beans.AHUser;
import com.aonhewitt.portal.userprovisioning.beans.ChannelRequestData;
import com.aonhewitt.portal.userprovisioning.beans.ChannelUserProfile;
import com.aonhewitt.portal.util.AppContainerProvider;
import com.aonhewitt.portal.util.MessageAppContainer;
import com.aonhewitt.portal.util.RestServiceInvocationResult;
import com.aonhewitt.portal.util.RestServiceInvocationResult.ResponseStatusEnum;
import com.aonhewitt.upoint.blackout.util.BlackoutUtil;
import com.aonhewitt.upoint.cache.util.AppConfigReader;
import com.aonhewitt.upoint.conf.AppProperties;
import com.aonhewitt.upoint.conf.YAMLConfig;
import com.aonhewitt.upoint.core.controller.widget.WidgetRestController;
import com.aonhewitt.upoint.core.service.util.ExpressionsCacheUtility;
import com.aonhewitt.upoint.portal.configuration.util.ConfigurationParmUtil;
import com.aonhewitt.upoint.util.AELockUtil;
import com.aonhewitt.upoint.util.CommonUtils;
import com.aonhewitt.upoint.util.PreAuthUtil;
import com.fasterxml.jackson.databind.ObjectMapper;

@ExtendWith(SpringExtension.class)
public class WidgetRestControllerTestV2 {

	@Mock
	private ConfigManagerService configManagerService;

	@Mock
	CmServicesUtil cmServicesUtil;

	@Mock
	private AppProperties appProperties;

	@Mock
	private ExpressionsCacheUtility expressionsCacheUtility;

	@Mock
	private BlackoutUtil blackoutUtil;

	@Mock
	private YAMLConfig yamlConfig;

	@Mock
	CommonUtils commonUtils;

	@InjectMocks
	WidgetRestController widgetRestController;

	private MessageAppContainer messageAppContainer = new MessageAppContainer();

	private ObjectMapper mapper;
	private List<Map<Object, Object>> output;
	RestServiceInvocationResult serviceInvocationResult;

	private static MockedStatic<AHUserLocalServiceUtil> aHUserLocalServiceUtilMockedStatic;
	private static MockedStatic<CommonUtils> commonUtilsMockedStatic;
	private static MockedStatic<CoreFeatureUtil> coreFeatureUtilMockedStatic;
	private static MockedStatic<ConfigurationParmUtil> configurationParmUtilMockedStatic;

	//private static MockedStatic<ConfigManagerService> configManagerServiceMockedStatic;
	private static MockedStatic<PreAuthUtil> preAuthUtilMockedStatic;
	private static MockedStatic<AELockUtil> aELockUtilMockedStatic;
	
	

	@BeforeAll
	public static void init() {
		Mockito.clearAllCaches();
		aHUserLocalServiceUtilMockedStatic = mockStatic(AHUserLocalServiceUtil.class);
		commonUtilsMockedStatic = mockStatic(CommonUtils.class);
		coreFeatureUtilMockedStatic = mockStatic(CoreFeatureUtil.class);
		mockStatic(ErrorLogEventHelper.class);
		configurationParmUtilMockedStatic = mockStatic(ConfigurationParmUtil.class);

		//configManagerServiceMockedStatic = mockStatic(ConfigManagerService.class);
		preAuthUtilMockedStatic = mockStatic(PreAuthUtil.class);
		aELockUtilMockedStatic = mockStatic(AELockUtil.class);

	}

	@AfterAll
	public static void close() {
		aHUserLocalServiceUtilMockedStatic.close();
		coreFeatureUtilMockedStatic.close();
		commonUtilsMockedStatic.close();
		//configManagerServiceMockedStatic.close();
		preAuthUtilMockedStatic.close();
		aELockUtilMockedStatic.close();
	}

	@BeforeEach
	public void setUp() throws Exception {

		mapper = new ObjectMapper();
		output = new ArrayList<Map<Object, Object>>();
		serviceInvocationResult = new RestServiceInvocationResult();
		serviceInvocationResult.setResponseStatus(ResponseStatusEnum.SUCCESS);

		ChannelRequestData crd = new ChannelRequestData();
		Map<String, String> ud = new HashMap<String, String>();
		ud.put("IS_HIDE_PRIMARY_ACCOUNT_IN_MAS_DROP_DOWN", "false");
		ud.put("originator", "");
		ud.put("com.aonhewitt.upoint.psp.integration.masrolecd", "");
		ud.put("MAS_CURRENT_REQUESTED_ACTIVE_ACCT_TYPE", "TBA");
		ud.put("gblsId", "test");
		ud.put("IS_HIDE_PRIMARY_ACCOUNT_IN_MAS_DROP_DOWN", "true");
		ud.put("com.aonhewitt.upoint.psp.integration.masrolecd", "");
		ud.put("MAS_CURRENT_REQUESTED_ACTIVE_ACCT_TYPE", "");
		ud.put("com.aonhewitt.portal.auth.constants.Constants_MAS_PAGE_ATTR", "test");
		ud.put("csid", "csid");
		crd.setUserData(ud);

		messageAppContainer.setChannelRequestData(crd);
		ChannelUserProfile cup = new ChannelUserProfile();
		AHUser ahUser = new AHUser();
		ahUser.setChannelUserProfile(cup);
		// messageAppContainer = new MessageAppContainer();
		messageAppContainer.setLineage("19920_1.0");
		messageAppContainer.getBusinessLineages().put("CM", "19920_1.0");
		// messageAppContainer.setAsgSessionToken(aSGSessionToken);
		AppContainerProvider.setContainer(messageAppContainer);
		messageAppContainer.getAhUser().setLocale("en_US");
		messageAppContainer.getAhUser().setChannelUserProfile(cup);
		aHUserLocalServiceUtilMockedStatic.when(() -> AHUserLocalServiceUtil.getAHUser()).thenReturn(ahUser);
		// commonUtilsMockedStatic.when(() ->
		// CommonUtils.getDomainLineage()).thenReturn("00095_1.0");
		Mockito.when(commonUtils.getGlobalPersonIdentifier(Mockito.any())).thenReturn("test");
		coreFeatureUtilMockedStatic.when(() -> CoreFeatureUtil.getLocale()).thenReturn("en_US");
		aELockUtilMockedStatic.when(() -> AELockUtil.isAELockEnabled()).thenReturn(false);

		MockHttpServletRequest request = new MockHttpServletRequest();
		request.addHeader("AF_ENABLED", "true");
		request.addHeader("afSwitchOn", "true");
		request.addHeader("AF_ENABLED", "true");
		String alightSessionToken="{\"accessToken\":\"eyJhbGciOiJg\",\"sessionId\":\"472874c1\",\"idMapping\":\"[ { \\\"platformType\\\" : \\\"TBA_4X\\\", \\\"domain\\\" : \\\"DC,DB\\\", \\\"clientId\\\" : \\\"19920\\\", \\\"normalizedClientid\\\" : \\\"19920\\\", \\\"systemInstanceId\\\" : \\\"\\\", \\\"platformInternalId\\\" : \\\"184000044\\\", \\\"platformExternalId\\\" : \\\"140626002\\\", \\\"roleType\\\" : \\\"rkp\\\" } ]\",\"channelUserProfile\":\"null\",\"brokerUserId\":\"Portal\",\"clientId\":\"19920\",\"systemInstanceId\":\"null\",\"testCfg\":\"@@SB\",\"subjectId\":\"null\",\"subjectType\":\"null\",\"systemDate\":\"null\",\"locale\":\"en_US\",\"roleId\":\"null\",\"domainDocumentMapping\":\"null\",\"consumerReferenceId\":\"null\",\"expires\":\"1510649321900\"}";

		request.addHeader("alightPersonSessionToken", alightSessionToken);
		
		String arh ="{\"locale\":\"en_US\",\"channelRequestData\":\"tgtSite::true::CCMSBranchName::test::URL::login::groupId::6727607::orgName::hmorg::device::Desktop::gblsId::9aeb05d1-c150-41b7-96e0-31185803cfeb_2023-06-29-06.06.20.080000::uxPageRequestId::null::pageName::login::deviceType::null::sessionCreatedTimestamp::null::widgetName::null::\",\"clientId\":\"19968\"}";
		request.addHeader("alightRequestHeader", arh);
		request.addParameter("srvc", "srvc");
		request.addParameter("usrAction", "usrAction");
		request.addParameter("brokerUserId", "usrAction");
		request.addParameter("languageId", "languageId");
		request.addHeader("newDesign", "Y");
		
		ServletRequestAttributes attrs = new ServletRequestAttributes(request);
		RequestContextHolder.setRequestAttributes(attrs);
		
	 Map<Object, Object> configMap = new HashMap<Object, Object>(); 
	 Map<Object, Object> configMap1 = new HashMap<Object, Object>();
	 configMap1.put("he", "d");
	 configMap.put("gaConfigurations", configMap1);
	 configMap.put("ivaJavaScriptSrc", "hello");
	 when(yamlConfig.getYamlConfig().get("gaConfigurations")).thenReturn(configMap);
	 when(yamlConfig.getYamlConfig().get("ivaJavaScriptSrc")).thenReturn(configMap);
		
		

	}

	@Test
	public void testGetWidgetConfiguration() throws Exception {
		messageAppContainer.setPreAuthRequest(true);

		// when name nameSet is null
		widgetRestController.getWidgetConfiguration(null);

		// when nameSet is header
		widgetRestController.getWidgetConfiguration("header");

		List<Map<Object, Object>> resultMaps3 = widgetRestController.getWidgetConfiguration("tiles");
		assertNotNull(resultMaps3);

	}

	@Test
	public void testGetWidgetConfigurationWhenPreAuthNotEnabled() throws Exception {
		messageAppContainer.setPreAuthRequest(false);

		List<Map<Object, Object>> resultMaps3 = widgetRestController.getWidgetConfiguration("header");
		assertNotNull(resultMaps3);

		widgetRestController.getWidgetConfiguration("navigation");

		widgetRestController.getWidgetConfiguration("footer");

		widgetRestController.getWidgetConfiguration("footer");

		widgetRestController.getWidgetConfiguration("header-secondary-window");

		widgetRestController.getWidgetConfiguration("primarynav");

		widgetRestController.getWidgetConfiguration("search-component");

		widgetRestController.getWidgetConfiguration("tiles");

		widgetRestController.getWidgetConfiguration("initwidget");

		widgetRestController.getWidgetConfiguration("upc");

		widgetRestController.getWidgetConfiguration("branding");

		widgetRestController.getWidgetConfiguration("validateblackout");

		widgetRestController.getWidgetConfiguration("chat");

		List<Map<Object, Object>> responseList = new ArrayList<>();
		Map<Object, Object> responseMap = new HashMap<>();
		Map<Object, Object> uceMap = new HashMap<>();
		Map<Object, Object> homemap = new HashMap<>();
		homemap.put("WLF_HOME_REORDER_ASSET_GRP", "test");
		AssetDTO assetDTO= new AssetDTO(null, homemap);
		homemap.put("WLF_HOME_REORDER_TEMPLATE_DEFAULT_ASSET", assetDTO);
		uceMap.put("WLF_HOME_REORDER_ASSET_GRP", homemap);
		responseMap.put("asset_uce", uceMap);
		responseMap.put("expr", uceMap);
		responseMap.put("asset", uceMap);

		responseList.add(responseMap);

		RestServiceInvocationResult restServiceInvocationResult = new RestServiceInvocationResult();
		restServiceInvocationResult.setResponseCMValues(responseList);
		try (MockedStatic<AdapterService> adapterServiceMockedStatic = mockStatic(AdapterService.class);) {
			adapterServiceMockedStatic.when(() -> AdapterService.getResponse(Mockito.any()))
					.thenReturn(restServiceInvocationResult);
			widgetRestController.getWidgetConfiguration("initConfig");

			widgetRestController.getWidgetConfiguration("afinit");

			widgetRestController.getWidgetConfiguration("blackout");

		}

	}
	

	@Test
	public void getPreAuthWidgetConfigurationTest() throws Exception {
		//messageAppContainer.setPreAuthRequest(false);
		
		widgetRestController.getPreAuthWidgetConfiguration("blackout");
		//AppProperties appProperties= new AppProperties();
		//appProperties.setPreAuthCachingEnabled(true);
		Mockito.when(appProperties.isPreAuthCachingEnabled()).thenReturn(true);
		List<Map<Object, Object>> list= new ArrayList();
		Map<Object, Object> map= new HashMap<>();
		map.put("test", "test");
		//list.add(map);
		
		preAuthUtilMockedStatic.when(() -> PreAuthUtil.getPreAuthDataFromCache(Mockito.anyString(),Mockito.anyString())).thenReturn(list);
		widgetRestController.getPreAuthWidgetConfiguration("blackout");
		
		preAuthUtilMockedStatic.when(() -> PreAuthUtil.getPreAuthDataFromCache(Mockito.anyString(),Mockito.anyString())).thenReturn(list);
		widgetRestController.getPreAuthWidgetConfiguration("");
		
		Mockito.when(cmServicesUtil.buildServcieErrorResponseMap(Mockito.anyString(),Mockito.anyString())).thenThrow(new NullPointerException());
		assertNotNull(widgetRestController.getPreAuthWidgetConfiguration(""));
		
		RestServiceInvocationResult serviceResponseResult = new RestServiceInvocationResult();
		serviceResponseResult.setResponseStatus(RestServiceInvocationResult.ResponseStatusEnum.SUCCESS);
		Mockito.when(configManagerService.initWidgetConfig(Mockito.anyString())).thenReturn(serviceResponseResult);
		AppConfigReader appConfigReader =mock(AppConfigReader.class);
		try (MockedStatic<AppConfigReader> appConfigReaderMockedStatic = mockStatic(AppConfigReader.class);
				MockedStatic<AdapterService> adapterServiceMockedStatic = mockStatic(AdapterService.class);) {
			adapterServiceMockedStatic.when(() -> AdapterService.getResponse(Mockito.any()))
			.thenReturn(serviceResponseResult);
			appConfigReaderMockedStatic.when(() -> AppConfigReader.getInstance()).thenReturn(appConfigReader);
			Mockito.when(appConfigReader.isTriggerInitConfigAfterInitWidget()).thenReturn(true);
		assertNull(widgetRestController.getPreAuthWidgetConfiguration("initwidget"));
		
		
		}
		}
	@Test
	public void getPreAuthWidgetConfigurationWithPersonSessionTokenTest() throws Exception {
		
		Mockito.when(appProperties.isPreAuthCachingEnabled()).thenReturn(true);
		List<Map<Object, Object>> list= new ArrayList();
		Map<Object, Object> map= new HashMap<>();
		map.put("test", "test");
		//list.add(map);
		
		preAuthUtilMockedStatic.when(() -> PreAuthUtil.getPreAuthDataFromCache(Mockito.anyString(),Mockito.anyString())).thenReturn(list);
		
		preAuthUtilMockedStatic.when(() -> PreAuthUtil.getPreAuthDataFromCache(Mockito.anyString(),Mockito.anyString())).thenReturn(list);
		
		Mockito.when(cmServicesUtil.buildServcieErrorResponseMap(Mockito.anyString(),Mockito.anyString())).thenThrow(new NullPointerException());
		
		RestServiceInvocationResult serviceResponseResult = new RestServiceInvocationResult();
		serviceResponseResult.setResponseStatus(RestServiceInvocationResult.ResponseStatusEnum.SUCCESS);
		Mockito.when(configManagerService.initWidgetConfig(Mockito.anyString())).thenReturn(serviceResponseResult);
		AppConfigReader appConfigReader =mock(AppConfigReader.class);
		 PersonSessionToken personSessionToken= new PersonSessionToken();
		 personSessionToken.setBrokerUserId("NextIT");
		
		try (MockedStatic<AppConfigReader> appConfigReaderMockedStatic = mockStatic(AppConfigReader.class);
				MockedStatic<PersonSessionToken> personSessionTokenMockedStatic = mockStatic(PersonSessionToken.class);) {
			
			personSessionTokenMockedStatic.when(() -> PersonSessionToken.parse(Mockito.anyString())).thenReturn(personSessionToken);
			appConfigReaderMockedStatic.when(() -> AppConfigReader.getInstance()).thenReturn(appConfigReader);
			Mockito.when(appConfigReader.isTriggerInitConfigAfterInitWidget()).thenReturn(true);
			widgetRestController.getPreAuthWidgetConfiguration("chat");
			RestServiceInvocationResult restServiceInvocationResult = new RestServiceInvocationResult();
			restServiceInvocationResult.setResponseStatus(RestServiceInvocationResult.ResponseStatusEnum.SUCCESS);
			Mockito.when(blackoutUtil.validateBlackout(Mockito.anyString())).thenReturn(restServiceInvocationResult);
			
			widgetRestController.getPreAuthWidgetConfiguration("validateblackout");
			
			restServiceInvocationResult.setResponseStatus(RestServiceInvocationResult.ResponseStatusEnum.FAILURE);
			widgetRestController.getPreAuthWidgetConfiguration("validateblackout");
			
		}
		}
	
	
	@Test
	public void testGetWidgetConfigurationWhenPreAuthNotEnabledAndinitConfig() throws Exception {
		messageAppContainer.setPreAuthRequest(false);
		Mockito.when(commonUtils.getGlobalPersonIdentifier(Mockito.any())).thenThrow(new NullPointerException());
		List<Map<Object, Object>> responseList = new ArrayList<>();
		Map<Object, Object> responseMap = new HashMap<>();
		Map<Object, Object> uceMap = new HashMap<>();
		Map<Object, Object> homemap = new HashMap<>();
		homemap.put("WLF_HOME_REORDER_ASSET_GRP", "test");
		
		AssetDTO assetDTO= new AssetDTO(null, homemap);
		homemap.put("WLF_HOME_REORDER_TEMPLATE_1_ASSET", assetDTO);
		uceMap.put("WLF_HOME_REORDER_ASSET_GRP", homemap);
		responseMap.put("asset_uce", uceMap);
		responseMap.put("expr", uceMap);
		responseMap.put("asset", uceMap);

		responseList.add(responseMap);

		RestServiceInvocationResult restServiceInvocationResult = new RestServiceInvocationResult();
		restServiceInvocationResult.setResponseCMValues(responseList);
		restServiceInvocationResult.setResponseStatus(RestServiceInvocationResult.ResponseStatusEnum.SUCCESS);
		try (MockedStatic<AdapterService> adapterServiceMockedStatic = mockStatic(AdapterService.class);) {
			adapterServiceMockedStatic.when(() -> AdapterService.getResponse(Mockito.any()))
					.thenReturn(restServiceInvocationResult);
			widgetRestController.getWidgetConfiguration("initConfig");

		}

	}
}