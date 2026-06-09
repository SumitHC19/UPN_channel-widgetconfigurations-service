/*package com.aonhewitt.upoint.core.controller.unit.widget;


import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.aonhewitt.beans.ComponentRequestBean;
import com.aonhewitt.portal.adapter.AdapterService;
import com.aonhewitt.portal.core.service.util.CmServicesUtil;
import com.aonhewitt.portal.core.service.util.CommonUtil;
import com.aonhewitt.portal.feature.tile.mget.action.TileMultiGetAdapter;
import com.aonhewitt.portal.feature.util.CoreFeatureUtil;
import com.aonhewitt.portal.service.delegators.ConfigManagerService;
import com.aonhewitt.portal.userprovisioning.beans.ChannelRequestData;
import com.aonhewitt.portal.userprovisioning.beans.ChannelUserProfile;
import com.aonhewitt.portal.util.AppContainerProvider;
import com.aonhewitt.portal.util.MessageAppContainer;
import com.aonhewitt.portal.util.RestServiceInvocationResult;
import com.aonhewitt.upoint.conf.AppProperties;
import com.aonhewitt.upoint.conf.YAMLConfig;
import com.aonhewitt.upoint.core.controller.widget.WidgetRestController;
import com.aonhewitt.upoint.core.service.util.ExpressionsCacheUtility;
import com.aonhewitt.upoint.portal.configuration.util.ConfigurationParmUtil;
import com.aonhewitt.upoint.util.MockAELockFalse;
import com.aonhewitt.upoint.util.TileMock;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

//@WebMvcTest(WidgetRestController.class)
@SpringBootTest
@TestPropertySource(properties = "spring.cloud.discovery.enabled=false")
public class TilePreAuthTest {

	@MockBean
	TomcatServletWebServerFactory tomcatServletWebServerFactory;

	@Autowired
	MockMvc mockMvc;

	@MockBean
	CmServicesUtil cmServicesUtil;

	@MockBean
	AppProperties appProperties;

	@InjectMocks
	AdapterService adapterService;

	@MockBean
	ConfigManagerService configManagerService;

	@MockBean
	YAMLConfig yamlConfig;

	@InjectMocks
	CommonUtil commonUtil;
	
	@MockBean
	ExpressionsCacheUtility expressionsCacheUtility;

	@InjectMocks
	CoreFeatureUtil featureUtil;

	@InjectMocks
	ConfigurationParmUtil configurationParmUtil;

	@Mock
	ComponentRequestBean componentRequestBean;

	private List<Map<Object, Object>> output;
	RestServiceInvocationResult serviceInvocationResult;
	private MessageAppContainer messageAppContainer = new MessageAppContainer();
	private ObjectMapper mapper;

	@BeforeEach
	public void setUp() throws Exception {
		messageAppContainer.setPreAuthRequest(true);
		mapper = new ObjectMapper();
		output = new ArrayList<Map<Object, Object>>();
		serviceInvocationResult = new RestServiceInvocationResult();

		ChannelRequestData crd = new ChannelRequestData();
		Map<String, String> ud = new HashMap<String, String>();
		ud.put("IS_HIDE_PRIMARY_ACCOUNT_IN_MAS_DROP_DOWN", "false");
		ud.put("originator", "");
		ud.put("com.aonhewitt.upoint.psp.integration.masrolecd", "P_LR");
		ud.put("MAS_CURRENT_REQUESTED_ACTIVE_ACCT_TYPE", "TBA");
		crd.setUserData(ud);

		messageAppContainer.setChannelRequestData(crd);
		ChannelUserProfile cup = new ChannelUserProfile();
		// ahUser.setChannelUserProfile(cup);
		// messageAppContainer = new MessageAppContainer();
		messageAppContainer.setLineage("19920_1.0");
		messageAppContainer.getBusinessLineages().put("CM", "19920_1.0");
		// messageAppContainer.setAsgSessionToken(aSGSessionToken);
		AppContainerProvider.setContainer(messageAppContainer);
		messageAppContainer.getAhUser().setLocale("en_US");
		messageAppContainer.getAhUser().setChannelUserProfile(cup);

		output = new ArrayList<Map<Object, Object>>();
		serviceInvocationResult = new RestServiceInvocationResult();

		new MockAELockFalse();

	}

	@AfterEach
	public void tearDown() throws Exception {

		output = null;
		serviceInvocationResult = null;

	}

	@Test
	public void preAuthTileTestCase() throws Exception {

		Map<Object, Object> widgetMap = new HashMap<Object, Object>();
		widgetMap.put("GroupName", "Highlights for U");
		Map<Object, Object> widgetMapResult = new HashMap<Object, Object>();
		widgetMapResult.put("text", widgetMap);
		output.add(widgetMapResult);
		serviceInvocationResult.setResponseCMValues(output);
		serviceInvocationResult.setResponseStatus(RestServiceInvocationResult.ResponseStatusEnum.SUCCESS);
		new TileMock(serviceInvocationResult);
		
		TileMultiGetAdapter tileMultiGetAdapter = Mockito.mock(TileMultiGetAdapter.class);
		Mockito.when(tileMultiGetAdapter.process(componentRequestBean)).thenReturn(serviceInvocationResult);
		try {
			RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/channel/widgetConfigurations/tiles/public")
					.header("afSwitchOn", "true", "").contentType(MediaType.APPLICATION_JSON);
			MvcResult result = mockMvc.perform(requestBuilder).andReturn();
			MockHttpServletResponse response = result.getResponse();
			assertEquals(HttpStatus.OK.value(), response.getStatus());
		} catch (Exception er) {

		}
		// JSONAssert.assertEquals(outputJson, response.getContentAsString(),
		// false);
	}

	@Test
	public void preAuthTileErrorCase() throws Exception {

		Map<Object, Object> widgetMap = new HashMap<Object, Object>();
		widgetMap.put("GroupName", "Highlights for U");
		Map<Object, Object> widgetMapResult = new HashMap<Object, Object>();
		widgetMapResult.put("text", widgetMap);
		output.add(widgetMapResult);
		serviceInvocationResult.setResponseCMValues(output);
		serviceInvocationResult.setResponseStatus(RestServiceInvocationResult.ResponseStatusEnum.FAILURE);
		new TileMock(serviceInvocationResult);
		TileMultiGetAdapter tileMultiGetAdapter = Mockito.mock(TileMultiGetAdapter.class);
		Mockito.when(tileMultiGetAdapter.process(componentRequestBean)).thenReturn(serviceInvocationResult);
		try {
			RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/channel/widgetConfigurations/tiles/public")
					.header("afSwitchOn", "true", "").contentType(MediaType.APPLICATION_JSON);
			MvcResult result = mockMvc.perform(requestBuilder).andReturn();
			MockHttpServletResponse response = result.getResponse();
			assertEquals(HttpStatus.OK.value(), response.getStatus());
		} catch (Exception er) {

		}
		// JSONAssert.assertEquals(outputJson, response.getContentAsString(),
		// false);
	}
	
	@Test
	public void preAuthNewTileErrorCase() throws Exception {
		messageAppContainer.setPreAuthRequest(false);
		Map<Object, Object> widgetMap = new HashMap<Object, Object>();
		widgetMap.put("GroupName", "Highlights for U");
		Map<Object, Object> widgetMapResult = new HashMap<Object, Object>();
		widgetMapResult.put("text", widgetMap);
		output.add(widgetMapResult);
		serviceInvocationResult.setResponseCMValues(output);
		serviceInvocationResult.setResponseStatus(RestServiceInvocationResult.ResponseStatusEnum.FAILURE);
		new TileMock(serviceInvocationResult);
		com.alight.portal.feature.tile.mget.action.TileMultiGetAdapter tileMultiGetAdapter = Mockito.mock(com.alight.portal.feature.tile.mget.action.TileMultiGetAdapter.class);
		Mockito.when(tileMultiGetAdapter.process(componentRequestBean)).thenReturn(serviceInvocationResult);
		try {
			RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/channel/widgetConfigurations/tiles")
					.header("newDesign", "Y", "").contentType(MediaType.APPLICATION_JSON);
			MvcResult result = mockMvc.perform(requestBuilder).andReturn();
			MockHttpServletResponse response = result.getResponse();
			assertEquals(HttpStatus.OK.value(), response.getStatus());
		} catch (Exception er) {

		}
		// JSONAssert.assertEquals(outputJson, response.getContentAsString(),
		// false);
	}
	
	

}*/