//package com.aonhewitt.upoint.core.controller.unit.widget;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.when;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.Mockito;
//import org.skyscreamer.jsonassert.JSONAssert;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.MediaType;
//import org.springframework.mock.web.MockHttpServletResponse;
//import org.springframework.test.context.TestPropertySource;
//import org.springframework.test.context.junit.jupiter.SpringExtension;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.MvcResult;
//import org.springframework.test.web.servlet.RequestBuilder;
//import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
//import org.springframework.test.web.servlet.setup.MockMvcBuilders;
//import org.springframework.web.context.WebApplicationContext;
//
//import com.aonhewitt.beans.ComponentRequestBean;
//import com.aonhewitt.portal.adapter.AdapterService;
//import com.aonhewitt.portal.configuration.modules.assetmanagement.model.AssetDTO;
//import com.aonhewitt.portal.core.service.util.CmServicesUtil;
//import com.aonhewitt.portal.core.service.util.CommonUtil;
//import com.aonhewitt.portal.service.delegators.ConfigManagerService;
//import com.aonhewitt.portal.userprovisioning.beans.ChannelRequestData;
//import com.aonhewitt.portal.userprovisioning.beans.ChannelUserProfile;
//import com.aonhewitt.portal.util.AppContainerProvider;
//import com.aonhewitt.portal.util.MessageAppContainer;
//import com.aonhewitt.portal.util.RestServiceInvocationResult;
//import com.aonhewitt.upoint.conf.AppProperties;
//import com.aonhewitt.upoint.conf.YAMLConfig;
//import com.aonhewitt.upoint.core.controller.widget.WidgetRestController;
//import com.aonhewitt.upoint.core.service.util.ExpressionsCacheUtility;
//import com.aonhewitt.upoint.portal.configuration.util.ConfigurationParmUtil;
//import com.aonhewitt.upoint.util.MockAELockFalse;
//import com.aonhewitt.upoint.util.MockAdapterService;
//import com.aonhewitt.upoint.util.MockAdapterServiceFail;
//import com.aonhewitt.upoint.util.MockComponentRequestBean;
//import com.fasterxml.jackson.databind.ObjectMapper;
//
////Modified for PC
////import edu.umd.cs.findbugs.annotations.ExpectWarning;
//import org.mockito.InjectMocks;
//
////@WebMvcTest(WidgetRestController.class)
////@TestPropertySource(properties = { "spring.cloud.consul.discovery.enabled=false","spring.cloud.consul.config.enabled=false"})
//@ExtendWith(SpringExtension.class)
//@TestPropertySource(properties = "spring.cloud.discovery.enabled=false")
////@Import(WidgetTestConfig.class)
//public class WidgetRestControllerTest {
//	
//	
//	/*@TestConfiguration
//	public static class WidgetTestConfig {
//		@Bean
//		public AHUserFilter getFilter() {
//			return new AHUserFilter();
//		}
//	}*/
//	@InjectMocks
//	WidgetRestController widgetRestController;
//	
//	//@Autowired
//	//private WebApplicationContext context;
//
//	@MockBean
//	TomcatServletWebServerFactory tomcatServletWebServerFactory;
//
//	//@Autowired
//	MockMvc mockMvc;
//
//	@MockBean
//	CmServicesUtil cmServicesUtil;
//	
//	@MockBean
//	AppProperties appProperties;
//
//	//@MockBean
//	//AHUserFilter ahUSerFilter;
//	
//	@InjectMocks
//	AdapterService adapterService;
//	
//	@InjectMocks
//	ComponentRequestBean componentRequestBean;
//	
//	@MockBean
//	ConfigManagerService configManagerService;
//
//	@MockBean
//	YAMLConfig yamlConfig;
//	
//	@MockBean
//	ExpressionsCacheUtility expressionsCacheUtility;
//	
//	@InjectMocks
//	CommonUtil commonUtil;
//	
//	@InjectMocks
//	ConfigurationParmUtil configurationParmUtil;
//
//	private ObjectMapper mapper;
//	private List<Map<Object, Object>> output;
//	RestServiceInvocationResult serviceInvocationResult;
//
//	private MessageAppContainer messageAppContainer = new MessageAppContainer();
//	
//	
//	
//	@BeforeEach
//	public void setUp() throws Exception {
//
//		/*   mockMvc = MockMvcBuilders
//		            .webAppContextSetup(context)
//		            .addFilters(ahUserFilter)
//		            .build();
//		*/
//		mockMvc = MockMvcBuilders.standaloneSetup(widgetRestController).build();
//		messageAppContainer.setPreAuthRequest(false);
//		mapper = new ObjectMapper();
//		output = new ArrayList<Map<Object, Object>>();
//		serviceInvocationResult = new RestServiceInvocationResult();
//		
//		ChannelRequestData crd = new ChannelRequestData();
//		Map<String, String> ud = new HashMap<String, String>();
//		ud.put("IS_HIDE_PRIMARY_ACCOUNT_IN_MAS_DROP_DOWN", "false");
//		ud.put("originator", "");
//		ud.put("com.aonhewitt.upoint.psp.integration.masrolecd", "P_LR");
//		ud.put("MAS_CURRENT_REQUESTED_ACTIVE_ACCT_TYPE", "TBA");
//		crd.setUserData(ud);
//		
//		messageAppContainer.setChannelRequestData(crd);
//		ChannelUserProfile cup = new ChannelUserProfile();
//	//	ahUser.setChannelUserProfile(cup);
//		//messageAppContainer = new MessageAppContainer();
//		messageAppContainer.setLineage("19920_1.0");
//		messageAppContainer.getBusinessLineages().put("CM", "19920_1.0");
//		// messageAppContainer.setAsgSessionToken(aSGSessionToken);
//		AppContainerProvider.setContainer(messageAppContainer);
//		messageAppContainer.getAhUser().setLocale("en_US");
//		messageAppContainer.getAhUser().setChannelUserProfile(cup);
//		/*new MockUp<AHUserLocalServiceUtil>() {
//			@Mock
//			public AHUser getAHUser() {
//				return ahUser;
//			}
//		};
//		
//		new MockUp<CommonUtil>() {
//			@Mock
//			public String getDomainLineage() {
//				return lineage;
//			}
//		};
//		
//		new MockUp<CoreFeatureUtil>() {
//			@Mock
//			public String getLocale() {
//				return locale;
//			}
//		};*/
//		
//		/*new MockUp<ConfigurationParmUtil>() {
//			@Mock
//			public void saveClntPrmInRedisForLineage(String aLineage){
//				
//			}
//		};*/
//		
//		/*new MockUp<ConfigManagerService>() {
//			@Mock
//			public void multiGetInitForClientParms(String aLineage, String aLocale){
//				
//			}
//		};*/
//		
//		/*new MockUp<AdapterService>() {
//			@Mock
//			RestServiceInvocationResult getResponse(ComponentRequestBean aRequestBean)
//					throws ClassNotFoundException, InstantiationException, IllegalAccessException {
//				return serviceInvocationResult;
//			}
//		};*/
//		
//		//new MockAdapterService();
//		
//		
//		/*new MockUp<AELockUtil>() {
//			@Mock
//			public boolean isAELockEnabled(){
//				return false;
//			}
//		};*/
//		new MockAELockFalse();
//	}
//
//	@AfterEach
//	public void tearDown() throws Exception {
//		mapper = null;
//		output = null;
//		serviceInvocationResult = null;
//
//	}
//
//	@Test
//	public void testWidgetService() throws Exception {
//
//		// create Widget Response
//
//		Map<Object, Object> widgetMap = new HashMap<Object, Object>();
//		widgetMap.put("AA", "Army-America");
//
//		Map<Object, Object> widgetMapResult = new HashMap<Object, Object>();
//		widgetMapResult.put("text", widgetMap);
//
//		output.add(widgetMapResult);
//
//		// create input and output Json
//		String outputJson = mapper.writeValueAsString(output);
//
//		// create mock ServiceInvocation
//
//		serviceInvocationResult.setResponseCMValues(output);
//		serviceInvocationResult.setResponseStatus(RestServiceInvocationResult.ResponseStatusEnum.SUCCESS);
//
//		/*new MockUp<AdapterService>() {
//			@Mock
//			RestServiceInvocationResult getResponse(ComponentRequestBean aRequestBean)
//					throws ClassNotFoundException, InstantiationException, IllegalAccessException {
//				return serviceInvocationResult;
//			}
//		};*/
//		//new MockAdapterService(serviceInvocationResult);
//
//		RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/channel/widgetConfigurations/generic")
//				.header("alightSecureSessionToken",
//						"bOiAh9QDExxcLlAEl5s2AtpviYozDv+gjaPyaJb5MeEsvzbpdcklLNzKZEXWR9yZ9MvQ2xJndbWDRNvF16Dc2455I7Zh94h2uUln6pPYAZIOsHiBG4wCWEUh+87EPowJkwexeTeBawbFePyNlQqCdZL6X2tF6GqUG6ZyI1mv+3A+8ALuB3Aw7HjkqzaIS0bvoLw0QnboRLdOlAJ44LAb7ep/ZffHHN9IBHiYMgilwukHN0EdPV+0dA9+0jZeG1AC1DuNtW4nev8TwxsLM8diu4EGIxKDixK+Vv6VV93G5gtAGzsI9+jMSt61Ok6N6fK1nfoSWsDIrno1CM8gydkdRm4/y4Ivp5ByW+1FaUwVHuhwYDAchVfb3LJuEwGC1y/wDFCqyXgIdnDX5Jkg1cka3lWIyK2ysy3CKUHahyeWxBqmoHHbwsnFkuZDqMS3QNRWRqcbcnyKhr8qIz0UWoKGx/4S7Z1izQANdRP0Bz4kE3yaQvH2CQ+Q+WuKv8+SUNCI8bo67Ka2x5q73lhkevWYjOL2OR64hCXQPdeM7gKP0/d0dDxTvX9koJ8KGXMfqfsJmOBDXWXKpXuuIOJEYp3WvcsTEPbuhHQ3R3DpKFUe3sj4BubZShpzCy7mB4LCFUmH9Z9GPdhquO68B1XmlxqlT5p+eaz/0l87DuUGX8LJdcuAJxreVAYazaLUHSbR+JSR6s+CCn72Ay7XXmIV8V2Rsj9a4msefajY/q/8WqONGTogYLf+m8JcXeq10vGxCywMVkS2BMp2IMZ4LkF6IRvTGf72N+A78E3OVZMgP46lv7hvjY9dnE9ysLyVYhf03Zfc4XxKyrKIYJQLvFz/kARCM4DYYnr5NM2qyoRdB+SLDgStISK3YSkygWAkuzg3nlargzplVeDpAxHWGfT1o31xnPMyW1cs0Pbu3aFawR8QkbRUx5KvQ4qWrtXU7d7vwT5Nuaven6y79WBt47s3reL6yN2kXMBnhoT2wKFi2keaHVhr8P9yhLdhJzbun+7F0sv5Gl2A6j7QDGeSQMSGrUXQbYsVNTH81ZmEcRfgLZ2Xnow=")
//				.contentType(MediaType.APPLICATION_JSON);
//
//		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
//
//		MockHttpServletResponse response = result.getResponse();
//		assertEquals(HttpStatus.OK.value(), response.getStatus());
//
//		JSONAssert.assertEquals(outputJson, response.getContentAsString(), false);
//	}
//	@Test
//	public void testWidgetServiceNoComponent() throws Exception {
//
//		
//		serviceInvocationResult.setResponseStatus(RestServiceInvocationResult.ResponseStatusEnum.SUCCESS);
//		
//	//	Mockito.when(componentRequestBean.getComponent()).thenReturn("");
//
//		
//		new MockComponentRequestBean();
//		/*new MockUp<ComponentRequestBean>() {
//			@Mock
//			public String getComponent() {
//				return "";
//			}
//		};*/
//
//		RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/channel/widgetConfigurations/generic")
//				.header("alightSecureSessionToken",
//						"bOiAh9QDExxcLlAEl5s2AtpviYozDv+gjaPyaJb5MeEsvzbpdcklLNzKZEXWR9yZ9MvQ2xJndbWDRNvF16Dc2455I7Zh94h2uUln6pPYAZIOsHiBG4wCWEUh+87EPowJkwexeTeBawbFePyNlQqCdZL6X2tF6GqUG6ZyI1mv+3A+8ALuB3Aw7HjkqzaIS0bvoLw0QnboRLdOlAJ44LAb7ep/ZffHHN9IBHiYMgilwukHN0EdPV+0dA9+0jZeG1AC1DuNtW4nev8TwxsLM8diu4EGIxKDixK+Vv6VV93G5gtAGzsI9+jMSt61Ok6N6fK1nfoSWsDIrno1CM8gydkdRm4/y4Ivp5ByW+1FaUwVHuhwYDAchVfb3LJuEwGC1y/wDFCqyXgIdnDX5Jkg1cka3lWIyK2ysy3CKUHahyeWxBqmoHHbwsnFkuZDqMS3QNRWRqcbcnyKhr8qIz0UWoKGx/4S7Z1izQANdRP0Bz4kE3yaQvH2CQ+Q+WuKv8+SUNCI8bo67Ka2x5q73lhkevWYjOL2OR64hCXQPdeM7gKP0/d0dDxTvX9koJ8KGXMfqfsJmOBDXWXKpXuuIOJEYp3WvcsTEPbuhHQ3R3DpKFUe3sj4BubZShpzCy7mB4LCFUmH9Z9GPdhquO68B1XmlxqlT5p+eaz/0l87DuUGX8LJdcuAJxreVAYazaLUHSbR+JSR6s+CCn72Ay7XXmIV8V2Rsj9a4msefajY/q/8WqONGTogYLf+m8JcXeq10vGxCywMVkS2BMp2IMZ4LkF6IRvTGf72N+A78E3OVZMgP46lv7hvjY9dnE9ysLyVYhf03Zfc4XxKyrKIYJQLvFz/kARCM4DYYnr5NM2qyoRdB+SLDgStISK3YSkygWAkuzg3nlargzplVeDpAxHWGfT1o31xnPMyW1cs0Pbu3aFawR8QkbRUx5KvQ4qWrtXU7d7vwT5Nuaven6y79WBt47s3reL6yN2kXMBnhoT2wKFi2keaHVhr8P9yhLdhJzbun+7F0sv5Gl2A6j7QDGeSQMSGrUXQbYsVNTH81ZmEcRfgLZ2Xnow=")
//				.contentType(MediaType.APPLICATION_JSON);
//
//		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
//
//		MockHttpServletResponse response = result.getResponse();
//		assertEquals(HttpStatus.OK.value(), response.getStatus());
//
//		//JSONAssert.assertEquals(outputJson, response.getContentAsString(), false);
//	}
//
//	@Test
//	public void testWidgetServiceHeader() throws Exception {
//		// create Response
//		/*new MockUp<AELockUtil>() {
//			@Mock
//			public boolean isAELockEnabled(){
//				return false;
//			}
//		};*/ 
//		new MockAELockFalse();
//		Map<Object, Object> widgetMap = new HashMap<Object, Object>();
//		widgetMap.put("type", "expression");
//		widgetMap.put("value", "false");
//
//		Map<Object, Object> widgetMapResult = new HashMap<Object, Object>();
//		widgetMapResult.put("IS_HEADER_FOOTER_V2", widgetMap);
//		output.add(widgetMapResult);
//
//		// create input and output Json
//		String outputJson = mapper.writeValueAsString(output);
//		// create mock ServiceInvocation
//
//		serviceInvocationResult.setResponseCMValues(output);
//		serviceInvocationResult.setResponseStatus(RestServiceInvocationResult.ResponseStatusEnum.SUCCESS);
//		
//		new MockAdapterService(serviceInvocationResult);
//
//		RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/channel/widgetConfigurations/header")
//				.header("alightSecureSessionToken",
//						"bOiAh9QDExxcLlAEl5s2AtpviYozDv+gjaPyaJb5MeEsvzbpdcklLNzKZEXWR9yZ9MvQ2xJndbWDRNvF16Dc2455I7Zh94h2uUln6pPYAZIOsHiBG4wCWEUh+87EPowJkwexeTeBawbFePyNlQqCdZL6X2tF6GqUG6ZyI1mv+3A+8ALuB3Aw7HjkqzaIS0bvoLw0QnboRLdOlAJ44LAb7ep/ZffHHN9IBHiYMgilwukHN0EdPV+0dA9+0jZeG1AC1DuNtW4nev8TwxsLM8diu4EGIxKDixK+Vv6VV93G5gtAGzsI9+jMSt61Ok6N6fK1nfoSWsDIrno1CM8gydkdRm4/y4Ivp5ByW+1FaUwVHuhwYDAchVfb3LJuEwGC1y/wDFCqyXgIdnDX5Jkg1cka3lWIyK2ysy3CKUHahyeWxBqmoHHbwsnFkuZDqMS3QNRWRqcbcnyKhr8qIz0UWoKGx/4S7Z1izQANdRP0Bz4kE3yaQvH2CQ+Q+WuKv8+SUNCI8bo67Ka2x5q73lhkevWYjOL2OR64hCXQPdeM7gKP0/d0dDxTvX9koJ8KGXMfqfsJmOBDXWXKpXuuIOJEYp3WvcsTEPbuhHQ3R3DpKFUe3sj4BubZShpzCy7mB4LCFUmH9Z9GPdhquO68B1XmlxqlT5p+eaz/0l87DuUGX8LJdcuAJxreVAYazaLUHSbR+JSR6s+CCn72Ay7XXmIV8V2Rsj9a4msefajY/q/8WqONGTogYLf+m8JcXeq10vGxCywMVkS2BMp2IMZ4LkF6IRvTGf72N+A78E3OVZMgP46lv7hvjY9dnE9ysLyVYhf03Zfc4XxKyrKIYJQLvFz/kARCM4DYYnr5NM2qyoRdB+SLDgStISK3YSkygWAkuzg3nlargzplVeDpAxHWGfT1o31xnPMyW1cs0Pbu3aFawR8QkbRUx5KvQ4qWrtXU7d7vwT5Nuaven6y79WBt47s3reL6yN2kXMBnhoT2wKFi2keaHVhr8P9yhLdhJzbun+7F0sv5Gl2A6j7QDGeSQMSGrUXQbYsVNTH81ZmEcRfgLZ2Xnow=")
//				.contentType(MediaType.APPLICATION_JSON);
//
//		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
//
//		MockHttpServletResponse response = result.getResponse();
//		assertEquals(HttpStatus.OK.value(), response.getStatus());
//
//		JSONAssert.assertEquals(outputJson, response.getContentAsString(), false);
//	}
//	
//	@Test
//	public void testWidgetServiceHeaderFailure() throws Exception {
//		// create Response
//		/*new MockUp<AELockUtil>() {
//			@Mock
//			public boolean isAELockEnabled(){
//				return false;
//			}
//		};*/
//		
//		new MockAELockFalse();
//		final Map<Object, Object> errorMap = new HashMap<Object, Object>();
//		List<Map<Object, Object>> errorList = new ArrayList<Map<Object, Object>>();
//		errorMap.put("Error",
//		        "Bad Request!, componentParams not available or does not have required parameter defined :");
//		errorList.add(errorMap);
//		serviceInvocationResult.setResponseStatus(
//		        RestServiceInvocationResult.ResponseStatusEnum.FAILURE);
//		serviceInvocationResult.setResponseCMError(errorList);
//
//		new MockAdapterService(serviceInvocationResult);
//		RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/channel/widgetConfigurations/header")
//				.header("alightSecureSessionToken",
//						"bOiAh9QDExxcLlAEl5s2AtpviYozDv+gjaPyaJb5MeEsvzbpdcklLNzKZEXWR9yZ9MvQ2xJndbWDRNvF16Dc2455I7Zh94h2uUln6pPYAZIOsHiBG4wCWEUh+87EPowJkwexeTeBawbFePyNlQqCdZL6X2tF6GqUG6ZyI1mv+3A+8ALuB3Aw7HjkqzaIS0bvoLw0QnboRLdOlAJ44LAb7ep/ZffHHN9IBHiYMgilwukHN0EdPV+0dA9+0jZeG1AC1DuNtW4nev8TwxsLM8diu4EGIxKDixK+Vv6VV93G5gtAGzsI9+jMSt61Ok6N6fK1nfoSWsDIrno1CM8gydkdRm4/y4Ivp5ByW+1FaUwVHuhwYDAchVfb3LJuEwGC1y/wDFCqyXgIdnDX5Jkg1cka3lWIyK2ysy3CKUHahyeWxBqmoHHbwsnFkuZDqMS3QNRWRqcbcnyKhr8qIz0UWoKGx/4S7Z1izQANdRP0Bz4kE3yaQvH2CQ+Q+WuKv8+SUNCI8bo67Ka2x5q73lhkevWYjOL2OR64hCXQPdeM7gKP0/d0dDxTvX9koJ8KGXMfqfsJmOBDXWXKpXuuIOJEYp3WvcsTEPbuhHQ3R3DpKFUe3sj4BubZShpzCy7mB4LCFUmH9Z9GPdhquO68B1XmlxqlT5p+eaz/0l87DuUGX8LJdcuAJxreVAYazaLUHSbR+JSR6s+CCn72Ay7XXmIV8V2Rsj9a4msefajY/q/8WqONGTogYLf+m8JcXeq10vGxCywMVkS2BMp2IMZ4LkF6IRvTGf72N+A78E3OVZMgP46lv7hvjY9dnE9ysLyVYhf03Zfc4XxKyrKIYJQLvFz/kARCM4DYYnr5NM2qyoRdB+SLDgStISK3YSkygWAkuzg3nlargzplVeDpAxHWGfT1o31xnPMyW1cs0Pbu3aFawR8QkbRUx5KvQ4qWrtXU7d7vwT5Nuaven6y79WBt47s3reL6yN2kXMBnhoT2wKFi2keaHVhr8P9yhLdhJzbun+7F0sv5Gl2A6j7QDGeSQMSGrUXQbYsVNTH81ZmEcRfgLZ2Xnow=")
//				.contentType(MediaType.APPLICATION_JSON);
//
//		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
//
//		MockHttpServletResponse response = result.getResponse();
//		assertEquals(HttpStatus.OK.value(), response.getStatus());
//
//		assertEquals(RestServiceInvocationResult.ResponseStatusEnum.FAILURE.toString(), serviceInvocationResult.getResponseStatus().toString());
//	}
//	
//
//	@Test
//	public void testWidgetServiceFooter() throws Exception {
//		// create Response
//		/*new MockUp<AELockUtil>() {
//			@Mock
//			public boolean isAELockEnabled(){
//				return false;
//			}
//		};*/
//		messageAppContainer.setPreAuthRequest(true);
//		
//		new MockAELockFalse();
//		Map<Object, Object> widgetMap = new HashMap<Object, Object>();
//		widgetMap.put("type", "expression");
//		widgetMap.put("value", "false");
//		Map<Object, Object> widgetMapResult = new HashMap<Object, Object>();
//		widgetMapResult.put("IS_HEADER_FOOTER_V2", widgetMap);
//		output.add(widgetMapResult);
//
//		// create input and output Json
////		String outputJson = mapper.writeValueAsString(output);
//
//		// create mock ServiceInvocation
//		when(appProperties.isPreAuthCachingEnabled()).thenReturn(true);
//
//		serviceInvocationResult.setResponseCMValues(output);
//		serviceInvocationResult.setResponseStatus(RestServiceInvocationResult.ResponseStatusEnum.SUCCESS);
//		new MockAdapterService(serviceInvocationResult);
//		RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/channel/widgetConfigurations/footer/public")
//				.header("AF_ENABLED", true)
//				.contentType(MediaType.APPLICATION_JSON);
//
//		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
//
//		MockHttpServletResponse response = result.getResponse();
//		assertEquals(HttpStatus.OK.value(), response.getStatus());
//
//		//JSONAssert.assertEquals(outputJson, response.getContentAsString(), false);
//	}
//
//	@Test
//	public void testWidgetServiceHeaderSecWindow() throws Exception {
//
//		Map<Object, Object> widgetMap = new HashMap<Object, Object>();
//		widgetMap.put("type", "expression");
//		widgetMap.put("value", "false");
//
//		Map<Object, Object> widgetMapResult = new HashMap<Object, Object>();
//		widgetMapResult.put("IS_HEADER_FOOTER_V2", widgetMap);
//
//		output.add(widgetMapResult);
//		// create input and output Json
//		String outputJson = mapper.writeValueAsString(output);
//		// create mock ServiceInvocation
//
//		serviceInvocationResult.setResponseCMValues(output);
//		serviceInvocationResult.setResponseStatus(RestServiceInvocationResult.ResponseStatusEnum.SUCCESS);
//		new MockAdapterService(serviceInvocationResult);
//		RequestBuilder requestBuilder = MockMvcRequestBuilders
//				.get("/channel/widgetConfigurations/header-secondary-window")
//				.header("alightSecureSessionToken",
//						"bOiAh9QDExxcLlAEl5s2AtpviYozDv+gjaPyaJb5MeEsvzbpdcklLNzKZEXWR9yZ9MvQ2xJndbWDRNvF16Dc2455I7Zh94h2uUln6pPYAZIOsHiBG4wCWEUh+87EPowJkwexeTeBawbFePyNlQqCdZL6X2tF6GqUG6ZyI1mv+3A+8ALuB3Aw7HjkqzaIS0bvoLw0QnboRLdOlAJ44LAb7ep/ZffHHN9IBHiYMgilwukHN0EdPV+0dA9+0jZeG1AC1DuNtW4nev8TwxsLM8diu4EGIxKDixK+Vv6VV93G5gtAGzsI9+jMSt61Ok6N6fK1nfoSWsDIrno1CM8gydkdRm4/y4Ivp5ByW+1FaUwVHuhwYDAchVfb3LJuEwGC1y/wDFCqyXgIdnDX5Jkg1cka3lWIyK2ysy3CKUHahyeWxBqmoHHbwsnFkuZDqMS3QNRWRqcbcnyKhr8qIz0UWoKGx/4S7Z1izQANdRP0Bz4kE3yaQvH2CQ+Q+WuKv8+SUNCI8bo67Ka2x5q73lhkevWYjOL2OR64hCXQPdeM7gKP0/d0dDxTvX9koJ8KGXMfqfsJmOBDXWXKpXuuIOJEYp3WvcsTEPbuhHQ3R3DpKFUe3sj4BubZShpzCy7mB4LCFUmH9Z9GPdhquO68B1XmlxqlT5p+eaz/0l87DuUGX8LJdcuAJxreVAYazaLUHSbR+JSR6s+CCn72Ay7XXmIV8V2Rsj9a4msefajY/q/8WqONGTogYLf+m8JcXeq10vGxCywMVkS2BMp2IMZ4LkF6IRvTGf72N+A78E3OVZMgP46lv7hvjY9dnE9ysLyVYhf03Zfc4XxKyrKIYJQLvFz/kARCM4DYYnr5NM2qyoRdB+SLDgStISK3YSkygWAkuzg3nlargzplVeDpAxHWGfT1o31xnPMyW1cs0Pbu3aFawR8QkbRUx5KvQ4qWrtXU7d7vwT5Nuaven6y79WBt47s3reL6yN2kXMBnhoT2wKFi2keaHVhr8P9yhLdhJzbun+7F0sv5Gl2A6j7QDGeSQMSGrUXQbYsVNTH81ZmEcRfgLZ2Xnow=")
//				.header("locale", "en_US")
//				.contentType(MediaType.APPLICATION_JSON);
//
//		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
//
//		MockHttpServletResponse response = result.getResponse();
//		assertEquals(HttpStatus.OK.value(), response.getStatus());
//		
//		JSONAssert.assertEquals(outputJson, response.getContentAsString(), false);
//	}
//	
//	@Test
//	public void testWidgetServiceHeaderSecWindowFailure() throws Exception {
//		
//
//		final Map<Object, Object> errorMap = new HashMap<Object, Object>();
//		List<Map<Object, Object>> errorList = new ArrayList<Map<Object, Object>>();
//
//		errorMap.put("Error", "Bad Request!, process method failed :");
//		errorList.add(errorMap);
//		serviceInvocationResult.setResponseStatus(
//		        RestServiceInvocationResult.ResponseStatusEnum.FAILURE);
//		serviceInvocationResult.setResponseCMError(errorList);
//
//		RequestBuilder requestBuilder = MockMvcRequestBuilders
//				.get("/channel/widgetConfigurations/header-secondary-window")
//				.header("alightSecureSessionToken",
//						"bOiAh9QDExxcLlAEl5s2AtpviYozDv+gjaPyaJb5MeEsvzbpdcklLNzKZEXWR9yZ9MvQ2xJndbWDRNvF16Dc2455I7Zh94h2uUln6pPYAZIOsHiBG4wCWEUh+87EPowJkwexeTeBawbFePyNlQqCdZL6X2tF6GqUG6ZyI1mv+3A+8ALuB3Aw7HjkqzaIS0bvoLw0QnboRLdOlAJ44LAb7ep/ZffHHN9IBHiYMgilwukHN0EdPV+0dA9+0jZeG1AC1DuNtW4nev8TwxsLM8diu4EGIxKDixK+Vv6VV93G5gtAGzsI9+jMSt61Ok6N6fK1nfoSWsDIrno1CM8gydkdRm4/y4Ivp5ByW+1FaUwVHuhwYDAchVfb3LJuEwGC1y/wDFCqyXgIdnDX5Jkg1cka3lWIyK2ysy3CKUHahyeWxBqmoHHbwsnFkuZDqMS3QNRWRqcbcnyKhr8qIz0UWoKGx/4S7Z1izQANdRP0Bz4kE3yaQvH2CQ+Q+WuKv8+SUNCI8bo67Ka2x5q73lhkevWYjOL2OR64hCXQPdeM7gKP0/d0dDxTvX9koJ8KGXMfqfsJmOBDXWXKpXuuIOJEYp3WvcsTEPbuhHQ3R3DpKFUe3sj4BubZShpzCy7mB4LCFUmH9Z9GPdhquO68B1XmlxqlT5p+eaz/0l87DuUGX8LJdcuAJxreVAYazaLUHSbR+JSR6s+CCn72Ay7XXmIV8V2Rsj9a4msefajY/q/8WqONGTogYLf+m8JcXeq10vGxCywMVkS2BMp2IMZ4LkF6IRvTGf72N+A78E3OVZMgP46lv7hvjY9dnE9ysLyVYhf03Zfc4XxKyrKIYJQLvFz/kARCM4DYYnr5NM2qyoRdB+SLDgStISK3YSkygWAkuzg3nlargzplVeDpAxHWGfT1o31xnPMyW1cs0Pbu3aFawR8QkbRUx5KvQ4qWrtXU7d7vwT5Nuaven6y79WBt47s3reL6yN2kXMBnhoT2wKFi2keaHVhr8P9yhLdhJzbun+7F0sv5Gl2A6j7QDGeSQMSGrUXQbYsVNTH81ZmEcRfgLZ2Xnow=")
//				.header("locale", "en_US")
//				.contentType(MediaType.APPLICATION_JSON);
//
//		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
//
//		MockHttpServletResponse response = result.getResponse();
//		assertEquals(HttpStatus.OK.value(), response.getStatus());
//		
//		assertEquals(RestServiceInvocationResult.ResponseStatusEnum.FAILURE.toString(), serviceInvocationResult.getResponseStatus().toString());
//	}
//	@Test
//	public void testWidgetServiceSearch() throws Exception {
//
//		Map<Object, Object> widgetMap = new HashMap<Object, Object>();
//		widgetMap.put("type", "expression");
//		widgetMap.put("value", "false");
//
//		Map<Object, Object> widgetMapResult = new HashMap<Object, Object>();
//		widgetMapResult.put("IS_HEADER_FOOTER_V2", widgetMap);
//		output.add(widgetMapResult);
//
//		// create output Json
//		String outputJson = mapper.writeValueAsString(output);
//
//		// create mock ServiceInvocation
//
//		serviceInvocationResult.setResponseCMValues(output);
//		serviceInvocationResult.setResponseStatus(RestServiceInvocationResult.ResponseStatusEnum.SUCCESS);
//
//		/*new MockUp<AdapterService>() {
//			@Mock
//			RestServiceInvocationResult getResponse(ComponentRequestBean aRequestBean)
//					throws ClassNotFoundException, InstantiationException, IllegalAccessException {
//				return serviceInvocationResult;
//			}
//		};*/
//		
//		new MockAdapterService(serviceInvocationResult);
//
//		RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/channel/widgetConfigurations/search-component")
//				.header("alightSecureSessionToken",
//						"bOiAh9QDExxcLlAEl5s2AtpviYozDv+gjaPyaJb5MeEsvzbpdcklLNzKZEXWR9yZ9MvQ2xJndbWDRNvF16Dc2455I7Zh94h2uUln6pPYAZIOsHiBG4wCWEUh+87EPowJkwexeTeBawbFePyNlQqCdZL6X2tF6GqUG6ZyI1mv+3A+8ALuB3Aw7HjkqzaIS0bvoLw0QnboRLdOlAJ44LAb7ep/ZffHHN9IBHiYMgilwukHN0EdPV+0dA9+0jZeG1AC1DuNtW4nev8TwxsLM8diu4EGIxKDixK+Vv6VV93G5gtAGzsI9+jMSt61Ok6N6fK1nfoSWsDIrno1CM8gydkdRm4/y4Ivp5ByW+1FaUwVHuhwYDAchVfb3LJuEwGC1y/wDFCqyXgIdnDX5Jkg1cka3lWIyK2ysy3CKUHahyeWxBqmoHHbwsnFkuZDqMS3QNRWRqcbcnyKhr8qIz0UWoKGx/4S7Z1izQANdRP0Bz4kE3yaQvH2CQ+Q+WuKv8+SUNCI8bo67Ka2x5q73lhkevWYjOL2OR64hCXQPdeM7gKP0/d0dDxTvX9koJ8KGXMfqfsJmOBDXWXKpXuuIOJEYp3WvcsTEPbuhHQ3R3DpKFUe3sj4BubZShpzCy7mB4LCFUmH9Z9GPdhquO68B1XmlxqlT5p+eaz/0l87DuUGX8LJdcuAJxreVAYazaLUHSbR+JSR6s+CCn72Ay7XXmIV8V2Rsj9a4msefajY/q/8WqONGTogYLf+m8JcXeq10vGxCywMVkS2BMp2IMZ4LkF6IRvTGf72N+A78E3OVZMgP46lv7hvjY9dnE9ysLyVYhf03Zfc4XxKyrKIYJQLvFz/kARCM4DYYnr5NM2qyoRdB+SLDgStISK3YSkygWAkuzg3nlargzplVeDpAxHWGfT1o31xnPMyW1cs0Pbu3aFawR8QkbRUx5KvQ4qWrtXU7d7vwT5Nuaven6y79WBt47s3reL6yN2kXMBnhoT2wKFi2keaHVhr8P9yhLdhJzbun+7F0sv5Gl2A6j7QDGeSQMSGrUXQbYsVNTH81ZmEcRfgLZ2Xnow=")
//				.contentType(MediaType.APPLICATION_JSON);
//
//		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
//
//		MockHttpServletResponse response = result.getResponse();
//		assertEquals(HttpStatus.OK.value(), response.getStatus());
//
//		JSONAssert.assertEquals(outputJson, response.getContentAsString(), false);
//	}
//
//	@Test
//	public void testWidgetServiceWithException() throws Exception {
//
//		// create Response
//		Map<Object, Object> widgetMapResult = new HashMap<Object, Object>();
//
//	//	widgetMapResult = new HashMap<Object, Object>();
//		widgetMapResult.put("Error", "Invalid Request!");
//
//		output = new ArrayList<Map<Object, Object>>();
//		output.add(widgetMapResult);
//
//		/*new MockUp<CmServicesUtil>() {
//			@Mock
//			public List<Map<Object, Object>> buildServcieErrorResponseMap(String key, String obj) {
//				return output;
//			}
//		};
//*/
//		/*new MockUp<AdapterService>() {
//			@Mock
//			RestServiceInvocationResult getResponse(ComponentRequestBean aRequestBean) throws Exception {
//				throw new Exception();
//			}
//		};*/
//		new MockAdapterServiceFail();
//
//		RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/channel/widgetConfigurations/generic")
//				.header("alightSecureSessionToken",
//						"bOiAh9QDExxcLlAEl5s2AtpviYozDv+gjaPyaJb5MeEsvzbpdcklLNzKZEXWR9yZ9MvQ2xJndbWDRNvF16Dc2455I7Zh94h2uUln6pPYAZIOsHiBG4wCWEUh+87EPowJkwexeTeBawbFePyNlQqCdZL6X2tF6GqUG6ZyI1mv+3A+8ALuB3Aw7HjkqzaIS0bvoLw0QnboRLdOlAJ44LAb7ep/ZffHHN9IBHiYMgilwukHN0EdPV+0dA9+0jZeG1AC1DuNtW4nev8TwxsLM8diu4EGIxKDixK+Vv6VV93G5gtAGzsI9+jMSt61Ok6N6fK1nfoSWsDIrno1CM8gydkdRm4/y4Ivp5ByW+1FaUwVHuhwYDAchVfb3LJuEwGC1y/wDFCqyXgIdnDX5Jkg1cka3lWIyK2ysy3CKUHahyeWxBqmoHHbwsnFkuZDqMS3QNRWRqcbcnyKhr8qIz0UWoKGx/4S7Z1izQANdRP0Bz4kE3yaQvH2CQ+Q+WuKv8+SUNCI8bo67Ka2x5q73lhkevWYjOL2OR64hCXQPdeM7gKP0/d0dDxTvX9koJ8KGXMfqfsJmOBDXWXKpXuuIOJEYp3WvcsTEPbuhHQ3R3DpKFUe3sj4BubZShpzCy7mB4LCFUmH9Z9GPdhquO68B1XmlxqlT5p+eaz/0l87DuUGX8LJdcuAJxreVAYazaLUHSbR+JSR6s+CCn72Ay7XXmIV8V2Rsj9a4msefajY/q/8WqONGTogYLf+m8JcXeq10vGxCywMVkS2BMp2IMZ4LkF6IRvTGf72N+A78E3OVZMgP46lv7hvjY9dnE9ysLyVYhf03Zfc4XxKyrKIYJQLvFz/kARCM4DYYnr5NM2qyoRdB+SLDgStISK3YSkygWAkuzg3nlargzplVeDpAxHWGfT1o31xnPMyW1cs0Pbu3aFawR8QkbRUx5KvQ4qWrtXU7d7vwT5Nuaven6y79WBt47s3reL6yN2kXMBnhoT2wKFi2keaHVhr8P9yhLdhJzbun+7F0sv5Gl2A6j7QDGeSQMSGrUXQbYsVNTH81ZmEcRfgLZ2Xnow=")
//				.contentType(MediaType.APPLICATION_JSON);
//
//		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
//
//		MockHttpServletResponse response = result.getResponse();
//		assertEquals(HttpStatus.OK.value(), response.getStatus());
//	}
//	
//	@Test
//	public void testInitWidget() throws Exception {
//
//		Map<Object, Object> widgetMap = new HashMap<Object, Object>();
//		widgetMap.put("Success", "WidgetConfig Cleared Successfully");
//
//		List<Map<Object, Object>> list = new ArrayList<Map<Object, Object>>();
//		list.add(widgetMap);
//
//		// create output Json
//		String outputJson = mapper.writeValueAsString(list);
//
//		// create mock ServiceInvocation
//
//		serviceInvocationResult.setResponseCMValues(list);
//		serviceInvocationResult.setResponseStatus(RestServiceInvocationResult.ResponseStatusEnum.SUCCESS);
//
//		Mockito.when(configManagerService.initWidgetConfig(Mockito.any())).thenReturn(serviceInvocationResult);//Modified for PC
//
//		RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/channel/widgetConfigurations/initwidget")
//				.header("alightSecureSessionToken",
//						"bOiAh9QDExxcLlAEl5s2AtpviYozDv+gjaPyaJb5MeEsvzbpdcklLNzKZEXWR9yZ9MvQ2xJndbWDRNvF16Dc2455I7Zh94h2uUln6pPYAZIOsHiBG4wCWEUh+87EPowJkwexeTeBawbFePyNlQqCdZL6X2tF6GqUG6ZyI1mv+3A+8ALuB3Aw7HjkqzaIS0bvoLw0QnboRLdOlAJ44LAb7ep/ZffHHN9IBHiYMgilwukHN0EdPV+0dA9+0jZeG1AC1DuNtW4nev8TwxsLM8diu4EGIxKDixK+Vv6VV93G5gtAGzsI9+jMSt61Ok6N6fK1nfoSWsDIrno1CM8gydkdRm4/y4Ivp5ByW+1FaUwVHuhwYDAchVfb3LJuEwGC1y/wDFCqyXgIdnDX5Jkg1cka3lWIyK2ysy3CKUHahyeWxBqmoHHbwsnFkuZDqMS3QNRWRqcbcnyKhr8qIz0UWoKGx/4S7Z1izQANdRP0Bz4kE3yaQvH2CQ+Q+WuKv8+SUNCI8bo67Ka2x5q73lhkevWYjOL2OR64hCXQPdeM7gKP0/d0dDxTvX9koJ8KGXMfqfsJmOBDXWXKpXuuIOJEYp3WvcsTEPbuhHQ3R3DpKFUe3sj4BubZShpzCy7mB4LCFUmH9Z9GPdhquO68B1XmlxqlT5p+eaz/0l87DuUGX8LJdcuAJxreVAYazaLUHSbR+JSR6s+CCn72Ay7XXmIV8V2Rsj9a4msefajY/q/8WqONGTogYLf+m8JcXeq10vGxCywMVkS2BMp2IMZ4LkF6IRvTGf72N+A78E3OVZMgP46lv7hvjY9dnE9ysLyVYhf03Zfc4XxKyrKIYJQLvFz/kARCM4DYYnr5NM2qyoRdB+SLDgStISK3YSkygWAkuzg3nlargzplVeDpAxHWGfT1o31xnPMyW1cs0Pbu3aFawR8QkbRUx5KvQ4qWrtXU7d7vwT5Nuaven6y79WBt47s3reL6yN2kXMBnhoT2wKFi2keaHVhr8P9yhLdhJzbun+7F0sv5Gl2A6j7QDGeSQMSGrUXQbYsVNTH81ZmEcRfgLZ2Xnow=")
//				.contentType(MediaType.APPLICATION_JSON);
//
//		MockAppConfigReader aapp = new MockAppConfigReader();
//		when(aapp.getInstance().isTriggerInitConfigAfterInitWidget()).thenReturn(true);
//		
//		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
//		
//
//		MockHttpServletResponse response = result.getResponse();
//		assertEquals(HttpStatus.OK.value(), response.getStatus());
//
//		JSONAssert.assertEquals(outputJson, response.getContentAsString(), false);
//		//Negative case tesing
//		Map<Object, Object> widgetMapError = new HashMap<Object, Object>();
//
//		List<Map<Object, Object>> listError = new ArrayList<Map<Object, Object>>();
//		widgetMapError.put("Error", "WidgetConfig Not Cleared Successfully");
//		listError.add(widgetMapError);
//		outputJson = mapper.writeValueAsString(listError);
//		serviceInvocationResult.setResponseCMError(listError);
//		serviceInvocationResult.setResponseStatus(RestServiceInvocationResult.ResponseStatusEnum.FAILURE);
//		Mockito.when(configManagerService.initWidgetConfig(Mockito.anyString())).thenReturn(serviceInvocationResult);
//		RequestBuilder requestBuilder1 = MockMvcRequestBuilders.get("/channel/widgetConfigurations/initwidget")
//				.header("alightSecureSessionToken",
//						"bOiAh9QDExxcLlAEl5s2AtpviYozDv+gjaPyaJb5MeEsvzbpdcklLNzKZEXWR9yZ9MvQ2xJndbWDRNvF16Dc2455I7Zh94h2uUln6pPYAZIOsHiBG4wCWEUh+87EPowJkwexeTeBawbFePyNlQqCdZL6X2tF6GqUG6ZyI1mv+3A+8ALuB3Aw7HjkqzaIS0bvoLw0QnboRLdOlAJ44LAb7ep/ZffHHN9IBHiYMgilwukHN0EdPV+0dA9+0jZeG1AC1DuNtW4nev8TwxsLM8diu4EGIxKDixK+Vv6VV93G5gtAGzsI9+jMSt61Ok6N6fK1nfoSWsDIrno1CM8gydkdRm4/y4Ivp5ByW+1FaUwVHuhwYDAchVfb3LJuEwGC1y/wDFCqyXgIdnDX5Jkg1cka3lWIyK2ysy3CKUHahyeWxBqmoHHbwsnFkuZDqMS3QNRWRqcbcnyKhr8qIz0UWoKGx/4S7Z1izQANdRP0Bz4kE3yaQvH2CQ+Q+WuKv8+SUNCI8bo67Ka2x5q73lhkevWYjOL2OR64hCXQPdeM7gKP0/d0dDxTvX9koJ8KGXMfqfsJmOBDXWXKpXuuIOJEYp3WvcsTEPbuhHQ3R3DpKFUe3sj4BubZShpzCy7mB4LCFUmH9Z9GPdhquO68B1XmlxqlT5p+eaz/0l87DuUGX8LJdcuAJxreVAYazaLUHSbR+JSR6s+CCn72Ay7XXmIV8V2Rsj9a4msefajY/q/8WqONGTogYLf+m8JcXeq10vGxCywMVkS2BMp2IMZ4LkF6IRvTGf72N+A78E3OVZMgP46lv7hvjY9dnE9ysLyVYhf03Zfc4XxKyrKIYJQLvFz/kARCM4DYYnr5NM2qyoRdB+SLDgStISK3YSkygWAkuzg3nlargzplVeDpAxHWGfT1o31xnPMyW1cs0Pbu3aFawR8QkbRUx5KvQ4qWrtXU7d7vwT5Nuaven6y79WBt47s3reL6yN2kXMBnhoT2wKFi2keaHVhr8P9yhLdhJzbun+7F0sv5Gl2A6j7QDGeSQMSGrUXQbYsVNTH81ZmEcRfgLZ2Xnow=")
//				.contentType(MediaType.APPLICATION_JSON);
//
//		MvcResult result1 = mockMvc.perform(requestBuilder1).andReturn();
//
//		MockHttpServletResponse response1 = result1.getResponse();
//		assertEquals(HttpStatus.OK.value(), response1.getStatus());
//		JSONAssert.assertEquals(outputJson, response1.getContentAsString(), false);
//	}
//	
//	@Test
//	public void testWidgetApiGateway() throws Exception {
//
//		Map<Object, Object> widgetMap = new HashMap<Object, Object>();
//		widgetMap.put("API_GATEWAY_URL", "https://api-dv.alight.com/api/");
//
//		List<Map<Object, Object>> list = new ArrayList<Map<Object, Object>>();
//		list.add(widgetMap);
//
//		// create output Json
//		String outputJson = mapper.writeValueAsString(list);
//
//		// create mock ServiceInvocation
//
//		serviceInvocationResult.setResponseCMValues(list);
//		serviceInvocationResult.setResponseStatus(RestServiceInvocationResult.ResponseStatusEnum.SUCCESS);
//		new MockAdapterService(serviceInvocationResult);
//		Mockito.when(configManagerService.initWidgetConfig(Mockito.anyString())).thenReturn(serviceInvocationResult);
//
//		RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/channel/widgetConfigurations/api-gatewayurl")
//				.header("alightSecureSessionToken",
//						"bOiAh9QDExxcLlAEl5s2AtpviYozDv+gjaPyaJb5MeEsvzbpdcklLNzKZEXWR9yZ9MvQ2xJndbWDRNvF16Dc2455I7Zh94h2uUln6pPYAZIOsHiBG4wCWEUh+87EPowJkwexeTeBawbFePyNlQqCdZL6X2tF6GqUG6ZyI1mv+3A+8ALuB3Aw7HjkqzaIS0bvoLw0QnboRLdOlAJ44LAb7ep/ZffHHN9IBHiYMgilwukHN0EdPV+0dA9+0jZeG1AC1DuNtW4nev8TwxsLM8diu4EGIxKDixK+Vv6VV93G5gtAGzsI9+jMSt61Ok6N6fK1nfoSWsDIrno1CM8gydkdRm4/y4Ivp5ByW+1FaUwVHuhwYDAchVfb3LJuEwGC1y/wDFCqyXgIdnDX5Jkg1cka3lWIyK2ysy3CKUHahyeWxBqmoHHbwsnFkuZDqMS3QNRWRqcbcnyKhr8qIz0UWoKGx/4S7Z1izQANdRP0Bz4kE3yaQvH2CQ+Q+WuKv8+SUNCI8bo67Ka2x5q73lhkevWYjOL2OR64hCXQPdeM7gKP0/d0dDxTvX9koJ8KGXMfqfsJmOBDXWXKpXuuIOJEYp3WvcsTEPbuhHQ3R3DpKFUe3sj4BubZShpzCy7mB4LCFUmH9Z9GPdhquO68B1XmlxqlT5p+eaz/0l87DuUGX8LJdcuAJxreVAYazaLUHSbR+JSR6s+CCn72Ay7XXmIV8V2Rsj9a4msefajY/q/8WqONGTogYLf+m8JcXeq10vGxCywMVkS2BMp2IMZ4LkF6IRvTGf72N+A78E3OVZMgP46lv7hvjY9dnE9ysLyVYhf03Zfc4XxKyrKIYJQLvFz/kARCM4DYYnr5NM2qyoRdB+SLDgStISK3YSkygWAkuzg3nlargzplVeDpAxHWGfT1o31xnPMyW1cs0Pbu3aFawR8QkbRUx5KvQ4qWrtXU7d7vwT5Nuaven6y79WBt47s3reL6yN2kXMBnhoT2wKFi2keaHVhr8P9yhLdhJzbun+7F0sv5Gl2A6j7QDGeSQMSGrUXQbYsVNTH81ZmEcRfgLZ2Xnow=")
//				.contentType(MediaType.APPLICATION_JSON);
//
//		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
//
//		MockHttpServletResponse response = result.getResponse();
//		assertEquals(HttpStatus.OK.value(), response.getStatus());
//
//		JSONAssert.assertEquals(outputJson, response.getContentAsString(), false);
//		//Negative case tesing
//		Map<Object, Object> widgetMapError = new HashMap<Object, Object>();
//
//		List<Map<Object, Object>> listError = new ArrayList<Map<Object, Object>>();
//		widgetMapError.put("Error", "WidgetConfig Not Cleared Successfully");
//		listError.add(widgetMapError);
//		outputJson = mapper.writeValueAsString(listError);
//		serviceInvocationResult.setResponseCMError(listError);
//		serviceInvocationResult.setResponseStatus(RestServiceInvocationResult.ResponseStatusEnum.FAILURE);
//		Mockito.when(configManagerService.initWidgetConfig(Mockito.anyString())).thenReturn(serviceInvocationResult);
//		RequestBuilder requestBuilder1 = MockMvcRequestBuilders.get("/channel/widgetConfigurations/api-gatewayurl")
//				.header("alightSecureSessionToken",
//						"bOiAh9QDExxcLlAEl5s2AtpviYozDv+gjaPyaJb5MeEsvzbpdcklLNzKZEXWR9yZ9MvQ2xJndbWDRNvF16Dc2455I7Zh94h2uUln6pPYAZIOsHiBG4wCWEUh+87EPowJkwexeTeBawbFePyNlQqCdZL6X2tF6GqUG6ZyI1mv+3A+8ALuB3Aw7HjkqzaIS0bvoLw0QnboRLdOlAJ44LAb7ep/ZffHHN9IBHiYMgilwukHN0EdPV+0dA9+0jZeG1AC1DuNtW4nev8TwxsLM8diu4EGIxKDixK+Vv6VV93G5gtAGzsI9+jMSt61Ok6N6fK1nfoSWsDIrno1CM8gydkdRm4/y4Ivp5ByW+1FaUwVHuhwYDAchVfb3LJuEwGC1y/wDFCqyXgIdnDX5Jkg1cka3lWIyK2ysy3CKUHahyeWxBqmoHHbwsnFkuZDqMS3QNRWRqcbcnyKhr8qIz0UWoKGx/4S7Z1izQANdRP0Bz4kE3yaQvH2CQ+Q+WuKv8+SUNCI8bo67Ka2x5q73lhkevWYjOL2OR64hCXQPdeM7gKP0/d0dDxTvX9koJ8KGXMfqfsJmOBDXWXKpXuuIOJEYp3WvcsTEPbuhHQ3R3DpKFUe3sj4BubZShpzCy7mB4LCFUmH9Z9GPdhquO68B1XmlxqlT5p+eaz/0l87DuUGX8LJdcuAJxreVAYazaLUHSbR+JSR6s+CCn72Ay7XXmIV8V2Rsj9a4msefajY/q/8WqONGTogYLf+m8JcXeq10vGxCywMVkS2BMp2IMZ4LkF6IRvTGf72N+A78E3OVZMgP46lv7hvjY9dnE9ysLyVYhf03Zfc4XxKyrKIYJQLvFz/kARCM4DYYnr5NM2qyoRdB+SLDgStISK3YSkygWAkuzg3nlargzplVeDpAxHWGfT1o31xnPMyW1cs0Pbu3aFawR8QkbRUx5KvQ4qWrtXU7d7vwT5Nuaven6y79WBt47s3reL6yN2kXMBnhoT2wKFi2keaHVhr8P9yhLdhJzbun+7F0sv5Gl2A6j7QDGeSQMSGrUXQbYsVNTH81ZmEcRfgLZ2Xnow=")
//				.contentType(MediaType.APPLICATION_JSON);
//
//		MvcResult result1 = mockMvc.perform(requestBuilder1).andReturn();
//
//		MockHttpServletResponse response1 = result1.getResponse();
//		assertEquals(HttpStatus.OK.value(), response1.getStatus());
//		JSONAssert.assertEquals(outputJson, response1.getContentAsString(), false);
//		
//		
//		
//	}
//	
//	@Test
//	public void testPreAuthWidgetService() throws Exception {
//
//		// create Widget Response
//
//		Map<Object, Object> widgetMap = new HashMap<Object, Object>();
//		widgetMap.put("AA", "Army-America");
//
//		Map<Object, Object> widgetMapResult = new HashMap<Object, Object>();
//		widgetMapResult.put("text", widgetMap);
//
//		output.add(widgetMapResult);
//
//		// create input and output Json
//		String outputJson = mapper.writeValueAsString(output);
//
//		// create mock ServiceInvocation
//
//		serviceInvocationResult.setResponseCMValues(output);
//		serviceInvocationResult.setResponseStatus(RestServiceInvocationResult.ResponseStatusEnum.SUCCESS);
//		messageAppContainer.setPreAuthRequest(true);
//
//		/*new MockUp<AdapterService>() {
//			@Mock
//			RestServiceInvocationResult getResponse(ComponentRequestBean aRequestBean)
//					throws ClassNotFoundException, InstantiationException, IllegalAccessException {
//				return serviceInvocationResult;
//			}
//		};*/
//		new MockAdapterService(serviceInvocationResult);
//
//		RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/channel/widgetConfigurations/generic")
//				.header("alightSecureSessionToken",
//						"")
//				.contentType(MediaType.APPLICATION_JSON);
//
//		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
//
//		MockHttpServletResponse response = result.getResponse();
//		assertEquals(HttpStatus.OK.value(), response.getStatus());
//
//		JSONAssert.assertEquals(outputJson, response.getContentAsString(), false);
//	}
//	
//	@Test
//	public void testWidgetServicePrimaryNav() throws Exception {
//		/*new MockUp<AELockUtil>() {
//			@Mock
//			public boolean isAELockEnabled(){
//				return false;
//			}
//		};*/ 
//		
//		new MockAELockFalse();
//		// create Response
//		String homeLink = "<a href=\"javascript:void(0);\" role=\"menuitem\" class=\"ah-show-anchor\">Home</a>";
//		
//		Map<Object, Object> navigationMap = new HashMap<Object, Object>();
//		//Map<Object, Object> assetGrpMap = new HashMap<Object, Object>();//Modified for PC
//		//List<Map<Object, Object>> assetList = new ArrayList<Map<Object, Object>>();
//		//Map<Object, Object> textMap = new LinkedHashMap<Object, Object>();
//		//textMap.put("id", "Home");
//		//textMap.put("type", "link");
//		//textMap.put("value", homeLink);
//		
//		//assetList.add(textMap);
//		// Stores the asset group for the navigation.
//		//assetGrpMap.put("id", "Nav_Home");//Modified for PC
//	    //assetGrpMap.put("type", "assetGrp");//Modified for PC
//		//assetGrpMap.put("assets", assetList);//Modified for PC
//		// Finally create one navigation and assign values to it.
//		navigationMap.put("navId", "Nav_Home");
//		//navigationMap.put("navigationItems", null); //Modified for PC
//		
//		List<Map<Object, Object>> navList = new ArrayList<Map<Object, Object>> (); 
//		navList.add(0, navigationMap);
//		
//		Map<Object, Object> navigationItems = new HashMap<Object, Object>();
//		navigationItems.put("navigationItems", navList);
//		output.add(navigationItems);
//
//		// create input and output Json
//		String outputJson = mapper.writeValueAsString(output);
//		// create mock ServiceInvocation
//
//		serviceInvocationResult.setResponseCMValues(output);
//		serviceInvocationResult.setResponseStatus(RestServiceInvocationResult.ResponseStatusEnum.SUCCESS);
//		new MockAdapterService(serviceInvocationResult);
//		RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/channel/widgetConfigurations/primarynav")
//				.header("alightSecureSessionToken",
//						"bOiAh9QDExxcLlAEl5s2AtpviYozDv+gjaPyaJb5MeEsvzbpdcklLNzKZEXWR9yZ9MvQ2xJndbWDRNvF16Dc2455I7Zh94h2uUln6pPYAZIOsHiBG4wCWEUh+87EPowJkwexeTeBawbFePyNlQqCdZL6X2tF6GqUG6ZyI1mv+3A+8ALuB3Aw7HjkqzaIS0bvoLw0QnboRLdOlAJ44LAb7ep/ZffHHN9IBHiYMgilwukHN0EdPV+0dA9+0jZeG1AC1DuNtW4nev8TwxsLM8diu4EGIxKDixK+Vv6VV93G5gtAGzsI9+jMSt61Ok6N6fK1nfoSWsDIrno1CM8gydkdRm4/y4Ivp5ByW+1FaUwVHuhwYDAchVfb3LJuEwGC1y/wDFCqyXgIdnDX5Jkg1cka3lWIyK2ysy3CKUHahyeWxBqmoHHbwsnFkuZDqMS3QNRWRqcbcnyKhr8qIz0UWoKGx/4S7Z1izQANdRP0Bz4kE3yaQvH2CQ+Q+WuKv8+SUNCI8bo67Ka2x5q73lhkevWYjOL2OR64hCXQPdeM7gKP0/d0dDxTvX9koJ8KGXMfqfsJmOBDXWXKpXuuIOJEYp3WvcsTEPbuhHQ3R3DpKFUe3sj4BubZShpzCy7mB4LCFUmH9Z9GPdhquO68B1XmlxqlT5p+eaz/0l87DuUGX8LJdcuAJxreVAYazaLUHSbR+JSR6s+CCn72Ay7XXmIV8V2Rsj9a4msefajY/q/8WqONGTogYLf+m8JcXeq10vGxCywMVkS2BMp2IMZ4LkF6IRvTGf72N+A78E3OVZMgP46lv7hvjY9dnE9ysLyVYhf03Zfc4XxKyrKIYJQLvFz/kARCM4DYYnr5NM2qyoRdB+SLDgStISK3YSkygWAkuzg3nlargzplVeDpAxHWGfT1o31xnPMyW1cs0Pbu3aFawR8QkbRUx5KvQ4qWrtXU7d7vwT5Nuaven6y79WBt47s3reL6yN2kXMBnhoT2wKFi2keaHVhr8P9yhLdhJzbun+7F0sv5Gl2A6j7QDGeSQMSGrUXQbYsVNTH81ZmEcRfgLZ2Xnow=")
//				.contentType(MediaType.APPLICATION_JSON);
//
//		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
//
//		MockHttpServletResponse response = result.getResponse();
//		assertEquals(HttpStatus.OK.value(), response.getStatus());
//
//		JSONAssert.assertEquals(outputJson, response.getContentAsString(), false);
//	}
//	
//	@Test
//	public void testRouterConfiguration() {
//		//Map<Object, Object> widgetMap = new HashMap<Object, Object>();
//		//widgetMap.put("AA", "Army-America");
//		YAMLConfig yamlConfig = new YAMLConfig();
//		assertNotNull(yamlConfig.getYamlConfig());
//		assertNotNull(yamlConfig);
//	}
//	
//	@Test
//	public void testInitConfigwithAF_Disabled() throws Exception {
//		/*new MockUp<AdapterService>() {
//			@Mock
//			RestServiceInvocationResult getResponse(ComponentRequestBean aRequestBean)
//					throws ClassNotFoundException, InstantiationException, IllegalAccessException {
//				return serviceInvocationResult;
//			}
//		};*/
//		
//		new MockAdapterService(serviceInvocationResult);
//		Map<Object,Object> expressions = new HashMap<Object,Object>();
//        Map<Object,Object> expValueMap = new HashMap<Object,Object>();
//        expValueMap.put("HM_MEDICARE_ELIG", false);
//        expValueMap.put("AF_ENABLED", false);
//        expValueMap.put("IS_PENS_ELIG", false);
//        expValueMap.put("IS_DC_ELIG", false);
//        expValueMap.put("UBB_SECURITY_GROUP_BASE", false);
//        expValueMap.put("IS_HM_TBA_HGB_ELIG", true);
//        expValueMap.put("IS_HCSA_TILE_ELIG", false);
//        expValueMap.put("UBB_IS_ACTIVE_EMP_EXPR", false);
//        expressions.put("expr", expValueMap);
//       
//       
//		output.add(expressions);
//
//		// create input and output Json
//		String outputJson = mapper.writeValueAsString(output);
//		serviceInvocationResult.setResponseCMValues(output);
//		serviceInvocationResult.setResponseStatus(RestServiceInvocationResult.ResponseStatusEnum.SUCCESS);
//
//		RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/channel/widgetConfigurations/initConfig")
//				.header("alightSecureSessionToken",
//						"bOiAh9QDExxcLlAEl5s2AtpviYozDv+gjaPyaJb5MeEsvzbpdcklLNzKZEXWR9yZ9MvQ2xJndbWDRNvF16Dc2455I7Zh94h2uUln6pPYAZIOsHiBG4wCWEUh+87EPowJkwexeTeBawbFePyNlQqCdZL6X2tF6GqUG6ZyI1mv+3A+8ALuB3Aw7HjkqzaIS0bvoLw0QnboRLdOlAJ44LAb7ep/ZffHHN9IBHiYMgilwukHN0EdPV+0dA9+0jZeG1AC1DuNtW4nev8TwxsLM8diu4EGIxKDixK+Vv6VV93G5gtAGzsI9+jMSt61Ok6N6fK1nfoSWsDIrno1CM8gydkdRm4/y4Ivp5ByW+1FaUwVHuhwYDAchVfb3LJuEwGC1y/wDFCqyXgIdnDX5Jkg1cka3lWIyK2ysy3CKUHahyeWxBqmoHHbwsnFkuZDqMS3QNRWRqcbcnyKhr8qIz0UWoKGx/4S7Z1izQANdRP0Bz4kE3yaQvH2CQ+Q+WuKv8+SUNCI8bo67Ka2x5q73lhkevWYjOL2OR64hCXQPdeM7gKP0/d0dDxTvX9koJ8KGXMfqfsJmOBDXWXKpXuuIOJEYp3WvcsTEPbuhHQ3R3DpKFUe3sj4BubZShpzCy7mB4LCFUmH9Z9GPdhquO68B1XmlxqlT5p+eaz/0l87DuUGX8LJdcuAJxreVAYazaLUHSbR+JSR6s+CCn72Ay7XXmIV8V2Rsj9a4msefajY/q/8WqONGTogYLf+m8JcXeq10vGxCywMVkS2BMp2IMZ4LkF6IRvTGf72N+A78E3OVZMgP46lv7hvjY9dnE9ysLyVYhf03Zfc4XxKyrKIYJQLvFz/kARCM4DYYnr5NM2qyoRdB+SLDgStISK3YSkygWAkuzg3nlargzplVeDpAxHWGfT1o31xnPMyW1cs0Pbu3aFawR8QkbRUx5KvQ4qWrtXU7d7vwT5Nuaven6y79WBt47s3reL6yN2kXMBnhoT2wKFi2keaHVhr8P9yhLdhJzbun+7F0sv5Gl2A6j7QDGeSQMSGrUXQbYsVNTH81ZmEcRfgLZ2Xnow=")
//				.contentType(MediaType.APPLICATION_JSON);
//
//		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
//
//		MockHttpServletResponse response = result.getResponse();
//		assertEquals(HttpStatus.OK.value(), response.getStatus());
//
//		JSONAssert.assertEquals(outputJson, response.getContentAsString(), false);
//	}
//	
//	@Test
//	public void testInitConfigwithAF_Enabled() throws Exception {
//		/*new MockUp<AdapterService>() {
//			@Mock
//			RestServiceInvocationResult getResponse(ComponentRequestBean aRequestBean)
//					throws ClassNotFoundException, InstantiationException, IllegalAccessException {
//				return serviceInvocationResult;
//			}
//		};*/
//		
//		Map<Object,Object> expressions = new HashMap<Object,Object>();
//        Map<Object,Object> expValueMap = new HashMap<Object,Object>();
//        expValueMap.put("HM_MEDICARE_ELIG", false);
//        expValueMap.put("AF_ENABLED", true);
//        expValueMap.put("IS_PENS_ELIG", false);
//        expValueMap.put("IS_DC_ELIG", false);
//        expValueMap.put("UBB_SECURITY_GROUP_BASE", false);
//        expValueMap.put("IS_HM_TBA_HGB_ELIG", true);
//        expValueMap.put("IS_HCSA_TILE_ELIG", false);
//        expValueMap.put("UBB_IS_ACTIVE_EMP_EXPR", false);
//        expressions.put("expr", expValueMap);
//       
//       
//		output.add(expressions);
//
//		// create input and output Json
//		String outputJson = mapper.writeValueAsString(output);
//		serviceInvocationResult.setResponseCMValues(output);
//		serviceInvocationResult.setResponseStatus(RestServiceInvocationResult.ResponseStatusEnum.SUCCESS);
//		new MockAdapterService(serviceInvocationResult);
//
//		RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/channel/widgetConfigurations/initConfig")
//				.header("alightSecureSessionToken",
//						"bOiAh9QDExxcLlAEl5s2AtpviYozDv+gjaPyaJb5MeEsvzbpdcklLNzKZEXWR9yZ9MvQ2xJndbWDRNvF16Dc2455I7Zh94h2uUln6pPYAZIOsHiBG4wCWEUh+87EPowJkwexeTeBawbFePyNlQqCdZL6X2tF6GqUG6ZyI1mv+3A+8ALuB3Aw7HjkqzaIS0bvoLw0QnboRLdOlAJ44LAb7ep/ZffHHN9IBHiYMgilwukHN0EdPV+0dA9+0jZeG1AC1DuNtW4nev8TwxsLM8diu4EGIxKDixK+Vv6VV93G5gtAGzsI9+jMSt61Ok6N6fK1nfoSWsDIrno1CM8gydkdRm4/y4Ivp5ByW+1FaUwVHuhwYDAchVfb3LJuEwGC1y/wDFCqyXgIdnDX5Jkg1cka3lWIyK2ysy3CKUHahyeWxBqmoHHbwsnFkuZDqMS3QNRWRqcbcnyKhr8qIz0UWoKGx/4S7Z1izQANdRP0Bz4kE3yaQvH2CQ+Q+WuKv8+SUNCI8bo67Ka2x5q73lhkevWYjOL2OR64hCXQPdeM7gKP0/d0dDxTvX9koJ8KGXMfqfsJmOBDXWXKpXuuIOJEYp3WvcsTEPbuhHQ3R3DpKFUe3sj4BubZShpzCy7mB4LCFUmH9Z9GPdhquO68B1XmlxqlT5p+eaz/0l87DuUGX8LJdcuAJxreVAYazaLUHSbR+JSR6s+CCn72Ay7XXmIV8V2Rsj9a4msefajY/q/8WqONGTogYLf+m8JcXeq10vGxCywMVkS2BMp2IMZ4LkF6IRvTGf72N+A78E3OVZMgP46lv7hvjY9dnE9ysLyVYhf03Zfc4XxKyrKIYJQLvFz/kARCM4DYYnr5NM2qyoRdB+SLDgStISK3YSkygWAkuzg3nlargzplVeDpAxHWGfT1o31xnPMyW1cs0Pbu3aFawR8QkbRUx5KvQ4qWrtXU7d7vwT5Nuaven6y79WBt47s3reL6yN2kXMBnhoT2wKFi2keaHVhr8P9yhLdhJzbun+7F0sv5Gl2A6j7QDGeSQMSGrUXQbYsVNTH81ZmEcRfgLZ2Xnow=")
//				.contentType(MediaType.APPLICATION_JSON);
//
//		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
//
//		MockHttpServletResponse response = result.getResponse();
//		assertEquals(HttpStatus.OK.value(), response.getStatus());
//
//		JSONAssert.assertEquals(outputJson, response.getContentAsString(), false);
//	}
//	@Test
//	public void testUPC() throws Exception {
//
//		Map<Object, Object> widgetMap = new HashMap<Object, Object>();
//		widgetMap.put("type", "expression");
//		widgetMap.put("value", "false");
//		
//		
//		Map<Object, Object> widgetMapResult = new HashMap<Object, Object>();
//		widgetMapResult.put("IS_HEADER_FOOTER_V2", widgetMap);
//		output.add(widgetMapResult);
//
//		// create output Json
//		String outputJson = mapper.writeValueAsString(output);
//
//		// create mock ServiceInvocation
//		
//
//		serviceInvocationResult.setResponseCMValues(output);
//		serviceInvocationResult.setResponseStatus(RestServiceInvocationResult.ResponseStatusEnum.SUCCESS);
///*
//		new MockUp<AdapterService>() {
//			@Mock
//			RestServiceInvocationResult getResponse(ComponentRequestBean aRequestBean)
//					throws ClassNotFoundException, InstantiationException, IllegalAccessException {
//				return serviceInvocationResult;
//			}
//		};*/
//		
//		new MockAdapterService(serviceInvocationResult);
//
//		RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/channel/widgetConfigurations/upc")
//				.header("alightSecureSessionToken",
//						"bOiAh9QDExxcLlAEl5s2AtpviYozDv+gjaPyaJb5MeEsvzbpdcklLNzKZEXWR9yZ9MvQ2xJndbWDRNvF16Dc2455I7Zh94h2uUln6pPYAZIOsHiBG4wCWEUh+87EPowJkwexeTeBawbFePyNlQqCdZL6X2tF6GqUG6ZyI1mv+3A+8ALuB3Aw7HjkqzaIS0bvoLw0QnboRLdOlAJ44LAb7ep/ZffHHN9IBHiYMgilwukHN0EdPV+0dA9+0jZeG1AC1DuNtW4nev8TwxsLM8diu4EGIxKDixK+Vv6VV93G5gtAGzsI9+jMSt61Ok6N6fK1nfoSWsDIrno1CM8gydkdRm4/y4Ivp5ByW+1FaUwVHuhwYDAchVfb3LJuEwGC1y/wDFCqyXgIdnDX5Jkg1cka3lWIyK2ysy3CKUHahyeWxBqmoHHbwsnFkuZDqMS3QNRWRqcbcnyKhr8qIz0UWoKGx/4S7Z1izQANdRP0Bz4kE3yaQvH2CQ+Q+WuKv8+SUNCI8bo67Ka2x5q73lhkevWYjOL2OR64hCXQPdeM7gKP0/d0dDxTvX9koJ8KGXMfqfsJmOBDXWXKpXuuIOJEYp3WvcsTEPbuhHQ3R3DpKFUe3sj4BubZShpzCy7mB4LCFUmH9Z9GPdhquO68B1XmlxqlT5p+eaz/0l87DuUGX8LJdcuAJxreVAYazaLUHSbR+JSR6s+CCn72Ay7XXmIV8V2Rsj9a4msefajY/q/8WqONGTogYLf+m8JcXeq10vGxCywMVkS2BMp2IMZ4LkF6IRvTGf72N+A78E3OVZMgP46lv7hvjY9dnE9ysLyVYhf03Zfc4XxKyrKIYJQLvFz/kARCM4DYYnr5NM2qyoRdB+SLDgStISK3YSkygWAkuzg3nlargzplVeDpAxHWGfT1o31xnPMyW1cs0Pbu3aFawR8QkbRUx5KvQ4qWrtXU7d7vwT5Nuaven6y79WBt47s3reL6yN2kXMBnhoT2wKFi2keaHVhr8P9yhLdhJzbun+7F0sv5Gl2A6j7QDGeSQMSGrUXQbYsVNTH81ZmEcRfgLZ2Xnow=")
//				.contentType(MediaType.APPLICATION_JSON);
//
//		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
//
//		MockHttpServletResponse response = result.getResponse();
//		assertEquals(HttpStatus.OK.value(), response.getStatus());
//
//		JSONAssert.assertEquals(outputJson, response.getContentAsString(), false);
//	}
//	
//	@Test
//	public void testWidgetServiceBranding() throws Exception {
//		// create Response
//		/*new MockUp<AELockUtil>() {
//			@Mock
//			public boolean isAELockEnabled(){
//				return false;
//			}
//		};*/ 
//		new MockAELockFalse();
//		Map<Object, Object> widgetMap = new HashMap<Object, Object>();
//		widgetMap.put("AF_ENABLED", "false");
//		widgetMap.put("lineage", "19920_1.0");
//		widgetMap.put("locale", "en_US");
//
//		
//		
//		Map<Object, Object> widgetMapResult = new HashMap<Object, Object>();
//		widgetMapResult.put("IS_HEADER_FOOTER_V2", widgetMap);
//		output.add(widgetMapResult);
//
//		// create input and output Json
//		String outputJson = mapper.writeValueAsString(output);
//		// create mock ServiceInvocation
//
//		serviceInvocationResult.setResponseCMValues(output);
//		serviceInvocationResult.setResponseStatus(RestServiceInvocationResult.ResponseStatusEnum.SUCCESS);
//		
//		new MockAdapterService(serviceInvocationResult);
//
//		RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/channel/widgetConfigurations/branding")
//				.header("alightSecureSessionToken",
//						"bOiAh9QDExxcLlAEl5s2AtpviYozDv+gjaPyaJb5MeEsvzbpdcklLNzKZEXWR9yZ9MvQ2xJndbWDRNvF16Dc2455I7Zh94h2uUln6pPYAZIOsHiBG4wCWEUh+87EPowJkwexeTeBawbFePyNlQqCdZL6X2tF6GqUG6ZyI1mv+3A+8ALuB3Aw7HjkqzaIS0bvoLw0QnboRLdOlAJ44LAb7ep/ZffHHN9IBHiYMgilwukHN0EdPV+0dA9+0jZeG1AC1DuNtW4nev8TwxsLM8diu4EGIxKDixK+Vv6VV93G5gtAGzsI9+jMSt61Ok6N6fK1nfoSWsDIrno1CM8gydkdRm4/y4Ivp5ByW+1FaUwVHuhwYDAchVfb3LJuEwGC1y/wDFCqyXgIdnDX5Jkg1cka3lWIyK2ysy3CKUHahyeWxBqmoHHbwsnFkuZDqMS3QNRWRqcbcnyKhr8qIz0UWoKGx/4S7Z1izQANdRP0Bz4kE3yaQvH2CQ+Q+WuKv8+SUNCI8bo67Ka2x5q73lhkevWYjOL2OR64hCXQPdeM7gKP0/d0dDxTvX9koJ8KGXMfqfsJmOBDXWXKpXuuIOJEYp3WvcsTEPbuhHQ3R3DpKFUe3sj4BubZShpzCy7mB4LCFUmH9Z9GPdhquO68B1XmlxqlT5p+eaz/0l87DuUGX8LJdcuAJxreVAYazaLUHSbR+JSR6s+CCn72Ay7XXmIV8V2Rsj9a4msefajY/q/8WqONGTogYLf+m8JcXeq10vGxCywMVkS2BMp2IMZ4LkF6IRvTGf72N+A78E3OVZMgP46lv7hvjY9dnE9ysLyVYhf03Zfc4XxKyrKIYJQLvFz/kARCM4DYYnr5NM2qyoRdB+SLDgStISK3YSkygWAkuzg3nlargzplVeDpAxHWGfT1o31xnPMyW1cs0Pbu3aFawR8QkbRUx5KvQ4qWrtXU7d7vwT5Nuaven6y79WBt47s3reL6yN2kXMBnhoT2wKFi2keaHVhr8P9yhLdhJzbun+7F0sv5Gl2A6j7QDGeSQMSGrUXQbYsVNTH81ZmEcRfgLZ2Xnow=")
//				.contentType(MediaType.APPLICATION_JSON);
//
//		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
//
//		MockHttpServletResponse response = result.getResponse();
//		assertEquals(HttpStatus.OK.value(), response.getStatus());
//
//		JSONAssert.assertEquals(outputJson, response.getContentAsString(), false);
//	}
//	
//	@Test
//	public void testWidgetServiceBrandingFailure() throws Exception {
//		// create Response
//		/*new MockUp<AELockUtil>() {
//			@Mock
//			public boolean isAELockEnabled(){
//				return false;
//			}
//		};*/
//		
//		new MockAELockFalse();
//		final Map<Object, Object> errorMap = new HashMap<Object, Object>();
//		List<Map<Object, Object>> errorList = new ArrayList<Map<Object, Object>>();
//
//		errorMap.put("Error",
//		        "Bad Request!, componentParams not available or does not have required parameter defined :");
//		errorList.add(errorMap);
//		serviceInvocationResult.setResponseStatus(
//		        RestServiceInvocationResult.ResponseStatusEnum.FAILURE);
//		serviceInvocationResult.setResponseCMError(errorList);
//
//		new MockAdapterService(serviceInvocationResult);
//		RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/channel/widgetConfigurations/branding")
//				.header("alightSecureSessionToken",
//						"bOiAh9QDExxcLlAEl5s2AtpviYozDv+gjaPyaJb5MeEsvzbpdcklLNzKZEXWR9yZ9MvQ2xJndbWDRNvF16Dc2455I7Zh94h2uUln6pPYAZIOsHiBG4wCWEUh+87EPowJkwexeTeBawbFePyNlQqCdZL6X2tF6GqUG6ZyI1mv+3A+8ALuB3Aw7HjkqzaIS0bvoLw0QnboRLdOlAJ44LAb7ep/ZffHHN9IBHiYMgilwukHN0EdPV+0dA9+0jZeG1AC1DuNtW4nev8TwxsLM8diu4EGIxKDixK+Vv6VV93G5gtAGzsI9+jMSt61Ok6N6fK1nfoSWsDIrno1CM8gydkdRm4/y4Ivp5ByW+1FaUwVHuhwYDAchVfb3LJuEwGC1y/wDFCqyXgIdnDX5Jkg1cka3lWIyK2ysy3CKUHahyeWxBqmoHHbwsnFkuZDqMS3QNRWRqcbcnyKhr8qIz0UWoKGx/4S7Z1izQANdRP0Bz4kE3yaQvH2CQ+Q+WuKv8+SUNCI8bo67Ka2x5q73lhkevWYjOL2OR64hCXQPdeM7gKP0/d0dDxTvX9koJ8KGXMfqfsJmOBDXWXKpXuuIOJEYp3WvcsTEPbuhHQ3R3DpKFUe3sj4BubZShpzCy7mB4LCFUmH9Z9GPdhquO68B1XmlxqlT5p+eaz/0l87DuUGX8LJdcuAJxreVAYazaLUHSbR+JSR6s+CCn72Ay7XXmIV8V2Rsj9a4msefajY/q/8WqONGTogYLf+m8JcXeq10vGxCywMVkS2BMp2IMZ4LkF6IRvTGf72N+A78E3OVZMgP46lv7hvjY9dnE9ysLyVYhf03Zfc4XxKyrKIYJQLvFz/kARCM4DYYnr5NM2qyoRdB+SLDgStISK3YSkygWAkuzg3nlargzplVeDpAxHWGfT1o31xnPMyW1cs0Pbu3aFawR8QkbRUx5KvQ4qWrtXU7d7vwT5Nuaven6y79WBt47s3reL6yN2kXMBnhoT2wKFi2keaHVhr8P9yhLdhJzbun+7F0sv5Gl2A6j7QDGeSQMSGrUXQbYsVNTH81ZmEcRfgLZ2Xnow=")
//				.contentType(MediaType.APPLICATION_JSON);
//
//		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
//
//		MockHttpServletResponse response = result.getResponse();
//		assertEquals(HttpStatus.OK.value(), response.getStatus());
//
//		assertEquals(RestServiceInvocationResult.ResponseStatusEnum.FAILURE.toString(), serviceInvocationResult.getResponseStatus().toString());
//	}
//	@Test
//	public void testGAConfiguration() {
//		Map<Object, Object> widgetMap = new HashMap<Object, Object>();
//		widgetMap.put("domainname", "auto");
//		YAMLConfig yamlConfig = new YAMLConfig();
//		yamlConfig.setGaConfigurations(widgetMap);
//		assertNotNull(yamlConfig.getYamlConfig().get("gaConfigurations"));
//		Map<Object, Object> gaConfig = (Map<Object, Object>) yamlConfig.getGaConfigurations();
//		assertEquals("auto", gaConfig.get("domainname"));
//	}
//	
//	@Test
//	public void testIVAJsSrcURL() {
//		YAMLConfig yamlConfig = new YAMLConfig();
//		yamlConfig.setIvaJavaScriptSrc("https://alight-uat.nextit.com/Alme/nextit-script-manager.js");
//		assertNotNull(yamlConfig.getYamlConfig());
//		assertNotNull(yamlConfig);
//		assertNotNull(yamlConfig.getIvaJavaScriptSrc());
//		assertEquals("https://alight-uat.nextit.com/Alme/nextit-script-manager.js",yamlConfig.getIvaJavaScriptSrc());
//	}
//	
//	@Test
//	public void testWidgetServiceAFInitAFDisabled() throws Exception {
//		
//		new MockAdapterService(serviceInvocationResult);
//		Map<Object,Object> expressions = new HashMap<Object,Object>();
//        Map<Object,Object> expValueMap = new HashMap<Object,Object>();
//        expValueMap.put("AF_IVA_ENABLED", true);
//        expValueMap.put("AF_ENABLED", false);
//        expValueMap.put("AF_Pages_List_Enabled", false);
//        expressions.put("expr", expValueMap);
//       
//        
//        
//		output.add(expressions);
//		
//
//		// create input and output Json
//		String outputJson = mapper.writeValueAsString(output);
//		serviceInvocationResult.setResponseCMValues(output);
//		serviceInvocationResult.setResponseStatus(RestServiceInvocationResult.ResponseStatusEnum.SUCCESS);
//
//		RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/channel/widgetConfigurations/afinit")
//				.header("alightSecureSessionToken",
//						"bOiAh9QDExxcLlAEl5s2AtpviYozDv+gjaPyaJb5MeEsvzbpdcklLNzKZEXWR9yZ9MvQ2xJndbWDRNvF16Dc2455I7Zh94h2uUln6pPYAZIOsHiBG4wCWEUh+87EPowJkwexeTeBawbFePyNlQqCdZL6X2tF6GqUG6ZyI1mv+3A+8ALuB3Aw7HjkqzaIS0bvoLw0QnboRLdOlAJ44LAb7ep/ZffHHN9IBHiYMgilwukHN0EdPV+0dA9+0jZeG1AC1DuNtW4nev8TwxsLM8diu4EGIxKDixK+Vv6VV93G5gtAGzsI9+jMSt61Ok6N6fK1nfoSWsDIrno1CM8gydkdRm4/y4Ivp5ByW+1FaUwVHuhwYDAchVfb3LJuEwGC1y/wDFCqyXgIdnDX5Jkg1cka3lWIyK2ysy3CKUHahyeWxBqmoHHbwsnFkuZDqMS3QNRWRqcbcnyKhr8qIz0UWoKGx/4S7Z1izQANdRP0Bz4kE3yaQvH2CQ+Q+WuKv8+SUNCI8bo67Ka2x5q73lhkevWYjOL2OR64hCXQPdeM7gKP0/d0dDxTvX9koJ8KGXMfqfsJmOBDXWXKpXuuIOJEYp3WvcsTEPbuhHQ3R3DpKFUe3sj4BubZShpzCy7mB4LCFUmH9Z9GPdhquO68B1XmlxqlT5p+eaz/0l87DuUGX8LJdcuAJxreVAYazaLUHSbR+JSR6s+CCn72Ay7XXmIV8V2Rsj9a4msefajY/q/8WqONGTogYLf+m8JcXeq10vGxCywMVkS2BMp2IMZ4LkF6IRvTGf72N+A78E3OVZMgP46lv7hvjY9dnE9ysLyVYhf03Zfc4XxKyrKIYJQLvFz/kARCM4DYYnr5NM2qyoRdB+SLDgStISK3YSkygWAkuzg3nlargzplVeDpAxHWGfT1o31xnPMyW1cs0Pbu3aFawR8QkbRUx5KvQ4qWrtXU7d7vwT5Nuaven6y79WBt47s3reL6yN2kXMBnhoT2wKFi2keaHVhr8P9yhLdhJzbun+7F0sv5Gl2A6j7QDGeSQMSGrUXQbYsVNTH81ZmEcRfgLZ2Xnow=")
//				.contentType(MediaType.APPLICATION_JSON);
//
//		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
//
//		MockHttpServletResponse response = result.getResponse();
//		assertEquals(HttpStatus.OK.value(), response.getStatus());
//
//		JSONAssert.assertEquals(outputJson, response.getContentAsString(), false);
//	}
//	
//	
//	@Test
//	public void testWidgetServiceAfConfigAFEnabled() throws Exception {
//		
//		new MockAdapterService(serviceInvocationResult);
//		Map<Object,Object> expressions = new HashMap<Object,Object>();
//        Map<Object,Object> expValueMap = new HashMap<Object,Object>();
//        expValueMap.put("AF_IVA_ENABLED", true);
//        expValueMap.put("AF_ENABLED", true);
//        expValueMap.put("AF_Pages_List_Enabled", false);
//        expressions.put("expr", expValueMap);
//       
//
//		output.add(expressions);
//		
//		
//		 Map<Object, Object> configMap = new HashMap<Object, Object>(); 
//		 Map<Object, Object> configMap1 = new HashMap<Object, Object>();
//		 configMap1.put("he", "d");
//		 configMap.put("gaConfigurations", configMap1);
//		 configMap.put("ivaJavaScriptSrc", "hello");
//		 when(yamlConfig.getYamlConfig().get("gaConfigurations")).thenReturn(configMap);
//		 when(yamlConfig.getYamlConfig().get("ivaJavaScriptSrc")).thenReturn(configMap);
//
//		// create input and output Json
//		String outputJson = mapper.writeValueAsString(output);
//		serviceInvocationResult.setResponseCMValues(output);
//		serviceInvocationResult.setResponseStatus(RestServiceInvocationResult.ResponseStatusEnum.SUCCESS);
//
//		RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/channel/widgetConfigurations/afinit")
//				.header("alightSecureSessionToken",
//						"bOiAh9QDExxcLlAEl5s2AtpviYozDv+gjaPyaJb5MeEsvzbpdcklLNzKZEXWR9yZ9MvQ2xJndbWDRNvF16Dc2455I7Zh94h2uUln6pPYAZIOsHiBG4wCWEUh+87EPowJkwexeTeBawbFePyNlQqCdZL6X2tF6GqUG6ZyI1mv+3A+8ALuB3Aw7HjkqzaIS0bvoLw0QnboRLdOlAJ44LAb7ep/ZffHHN9IBHiYMgilwukHN0EdPV+0dA9+0jZeG1AC1DuNtW4nev8TwxsLM8diu4EGIxKDixK+Vv6VV93G5gtAGzsI9+jMSt61Ok6N6fK1nfoSWsDIrno1CM8gydkdRm4/y4Ivp5ByW+1FaUwVHuhwYDAchVfb3LJuEwGC1y/wDFCqyXgIdnDX5Jkg1cka3lWIyK2ysy3CKUHahyeWxBqmoHHbwsnFkuZDqMS3QNRWRqcbcnyKhr8qIz0UWoKGx/4S7Z1izQANdRP0Bz4kE3yaQvH2CQ+Q+WuKv8+SUNCI8bo67Ka2x5q73lhkevWYjOL2OR64hCXQPdeM7gKP0/d0dDxTvX9koJ8KGXMfqfsJmOBDXWXKpXuuIOJEYp3WvcsTEPbuhHQ3R3DpKFUe3sj4BubZShpzCy7mB4LCFUmH9Z9GPdhquO68B1XmlxqlT5p+eaz/0l87DuUGX8LJdcuAJxreVAYazaLUHSbR+JSR6s+CCn72Ay7XXmIV8V2Rsj9a4msefajY/q/8WqONGTogYLf+m8JcXeq10vGxCywMVkS2BMp2IMZ4LkF6IRvTGf72N+A78E3OVZMgP46lv7hvjY9dnE9ysLyVYhf03Zfc4XxKyrKIYJQLvFz/kARCM4DYYnr5NM2qyoRdB+SLDgStISK3YSkygWAkuzg3nlargzplVeDpAxHWGfT1o31xnPMyW1cs0Pbu3aFawR8QkbRUx5KvQ4qWrtXU7d7vwT5Nuaven6y79WBt47s3reL6yN2kXMBnhoT2wKFi2keaHVhr8P9yhLdhJzbun+7F0sv5Gl2A6j7QDGeSQMSGrUXQbYsVNTH81ZmEcRfgLZ2Xnow=")
//				.contentType(MediaType.APPLICATION_JSON);
//
//		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
//
//		MockHttpServletResponse response = result.getResponse();
//		assertEquals(HttpStatus.OK.value(), response.getStatus());
//
//		JSONAssert.assertNotEquals(outputJson, response.getContentAsString(), false);
//	}
//	
//	@Test
//	public void testWidgetServiceTiles() throws Exception {
//		
//		new MockAdapterService(serviceInvocationResult);
//		Map<Object,Object> expressions = new HashMap<Object,Object>();
//        Map<Object,Object> expValueMap = new HashMap<Object,Object>();
//        expValueMap.put("AF_IVA_ENABLED", true);
//        expValueMap.put("AF_ENABLED", true);
//       expValueMap.put("AF_Pages_List_Enabled", false);
//        expressions.put("expr", expValueMap);
//       
//		output.add(expressions);
//
//		// create input and output Json
//	
//		serviceInvocationResult.setResponseCMValues(output);
//		serviceInvocationResult.setResponseStatus(RestServiceInvocationResult.ResponseStatusEnum.SUCCESS);
//
//		RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/channel/widgetConfigurations/tiles")
//				.header("alightSecureSessionToken",
//						"bOiAh9QDExxcLlAEl5s2AtpviYozDv+gjaPyaJb5MeEsvzbpdcklLNzKZEXWR9yZ9MvQ2xJndbWDRNvF16Dc2455I7Zh94h2uUln6pPYAZIOsHiBG4wCWEUh+87EPowJkwexeTeBawbFePyNlQqCdZL6X2tF6GqUG6ZyI1mv+3A+8ALuB3Aw7HjkqzaIS0bvoLw0QnboRLdOlAJ44LAb7ep/ZffHHN9IBHiYMgilwukHN0EdPV+0dA9+0jZeG1AC1DuNtW4nev8TwxsLM8diu4EGIxKDixK+Vv6VV93G5gtAGzsI9+jMSt61Ok6N6fK1nfoSWsDIrno1CM8gydkdRm4/y4Ivp5ByW+1FaUwVHuhwYDAchVfb3LJuEwGC1y/wDFCqyXgIdnDX5Jkg1cka3lWIyK2ysy3CKUHahyeWxBqmoHHbwsnFkuZDqMS3QNRWRqcbcnyKhr8qIz0UWoKGx/4S7Z1izQANdRP0Bz4kE3yaQvH2CQ+Q+WuKv8+SUNCI8bo67Ka2x5q73lhkevWYjOL2OR64hCXQPdeM7gKP0/d0dDxTvX9koJ8KGXMfqfsJmOBDXWXKpXuuIOJEYp3WvcsTEPbuhHQ3R3DpKFUe3sj4BubZShpzCy7mB4LCFUmH9Z9GPdhquO68B1XmlxqlT5p+eaz/0l87DuUGX8LJdcuAJxreVAYazaLUHSbR+JSR6s+CCn72Ay7XXmIV8V2Rsj9a4msefajY/q/8WqONGTogYLf+m8JcXeq10vGxCywMVkS2BMp2IMZ4LkF6IRvTGf72N+A78E3OVZMgP46lv7hvjY9dnE9ysLyVYhf03Zfc4XxKyrKIYJQLvFz/kARCM4DYYnr5NM2qyoRdB+SLDgStISK3YSkygWAkuzg3nlargzplVeDpAxHWGfT1o31xnPMyW1cs0Pbu3aFawR8QkbRUx5KvQ4qWrtXU7d7vwT5Nuaven6y79WBt47s3reL6yN2kXMBnhoT2wKFi2keaHVhr8P9yhLdhJzbun+7F0sv5Gl2A6j7QDGeSQMSGrUXQbYsVNTH81ZmEcRfgLZ2Xnow=")
//				.contentType(MediaType.APPLICATION_JSON);
//
//		mockMvc.perform(requestBuilder).andReturn();
//
//	}
//	
//	@Test
//	public void testWidgetServiceNavigation() throws Exception {
//		// create Response
//		/*new MockUp<AELockUtil>() {
//			@Mock
//			public boolean isAELockEnabled(){
//				return false;
//			}
//		};*/ 
//		new MockAELockFalse();
//		Map<Object, Object> widgetMap = new HashMap<Object, Object>();
//		widgetMap.put("type", "expression");
//		widgetMap.put("value", "false");
//
//		Map<Object, Object> widgetMapResult = new HashMap<Object, Object>();
//		widgetMapResult.put("IS_HEADER_FOOTER_V2", widgetMap);
//		output.add(widgetMapResult);
//
//		// create input and output Json
//		String outputJson = mapper.writeValueAsString(output);
//		// create mock ServiceInvocation
//
//		serviceInvocationResult.setResponseCMValues(output);
//		serviceInvocationResult.setResponseStatus(RestServiceInvocationResult.ResponseStatusEnum.SUCCESS);
//		
//		new MockAdapterService(serviceInvocationResult);
//
//		RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/channel/widgetConfigurations/navigation")
//				.header("alightSecureSessionToken",
//						"bOiAh9QDExxcLlAEl5s2AtpviYozDv+gjaPyaJb5MeEsvzbpdcklLNzKZEXWR9yZ9MvQ2xJndbWDRNvF16Dc2455I7Zh94h2uUln6pPYAZIOsHiBG4wCWEUh+87EPowJkwexeTeBawbFePyNlQqCdZL6X2tF6GqUG6ZyI1mv+3A+8ALuB3Aw7HjkqzaIS0bvoLw0QnboRLdOlAJ44LAb7ep/ZffHHN9IBHiYMgilwukHN0EdPV+0dA9+0jZeG1AC1DuNtW4nev8TwxsLM8diu4EGIxKDixK+Vv6VV93G5gtAGzsI9+jMSt61Ok6N6fK1nfoSWsDIrno1CM8gydkdRm4/y4Ivp5ByW+1FaUwVHuhwYDAchVfb3LJuEwGC1y/wDFCqyXgIdnDX5Jkg1cka3lWIyK2ysy3CKUHahyeWxBqmoHHbwsnFkuZDqMS3QNRWRqcbcnyKhr8qIz0UWoKGx/4S7Z1izQANdRP0Bz4kE3yaQvH2CQ+Q+WuKv8+SUNCI8bo67Ka2x5q73lhkevWYjOL2OR64hCXQPdeM7gKP0/d0dDxTvX9koJ8KGXMfqfsJmOBDXWXKpXuuIOJEYp3WvcsTEPbuhHQ3R3DpKFUe3sj4BubZShpzCy7mB4LCFUmH9Z9GPdhquO68B1XmlxqlT5p+eaz/0l87DuUGX8LJdcuAJxreVAYazaLUHSbR+JSR6s+CCn72Ay7XXmIV8V2Rsj9a4msefajY/q/8WqONGTogYLf+m8JcXeq10vGxCywMVkS2BMp2IMZ4LkF6IRvTGf72N+A78E3OVZMgP46lv7hvjY9dnE9ysLyVYhf03Zfc4XxKyrKIYJQLvFz/kARCM4DYYnr5NM2qyoRdB+SLDgStISK3YSkygWAkuzg3nlargzplVeDpAxHWGfT1o31xnPMyW1cs0Pbu3aFawR8QkbRUx5KvQ4qWrtXU7d7vwT5Nuaven6y79WBt47s3reL6yN2kXMBnhoT2wKFi2keaHVhr8P9yhLdhJzbun+7F0sv5Gl2A6j7QDGeSQMSGrUXQbYsVNTH81ZmEcRfgLZ2Xnow=")
//				.contentType(MediaType.APPLICATION_JSON);
//
//		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
//
//		MockHttpServletResponse response = result.getResponse();
//		assertEquals(HttpStatus.OK.value(), response.getStatus());
//
//		JSONAssert.assertEquals(outputJson, response.getContentAsString(), false);
//	}
//	
//	@Test
//	public void testWidgetServiceNAvigationFailure() throws Exception {
//		// create Response
//		/*new MockUp<AELockUtil>() {
//			@Mock
//			public boolean isAELockEnabled(){
//				return false;
//			}
//		};*/
//		
//		new MockAELockFalse();
//		final Map<Object, Object> errorMap = new HashMap<Object, Object>();
//		List<Map<Object, Object>> errorList = new ArrayList<Map<Object, Object>>();
//		errorMap.put("Error",
//		        "Bad Request!, componentParams not available or does not have required parameter defined :");
//		errorList.add(errorMap);
//		serviceInvocationResult.setResponseStatus(
//		        RestServiceInvocationResult.ResponseStatusEnum.FAILURE);
//		serviceInvocationResult.setResponseCMError(errorList);
//
//		new MockAdapterService(serviceInvocationResult);
//		RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/channel/widgetConfigurations/navigation")
//				.header("alightSecureSessionToken",
//						"bOiAh9QDExxcLlAEl5s2AtpviYozDv+gjaPyaJb5MeEsvzbpdcklLNzKZEXWR9yZ9MvQ2xJndbWDRNvF16Dc2455I7Zh94h2uUln6pPYAZIOsHiBG4wCWEUh+87EPowJkwexeTeBawbFePyNlQqCdZL6X2tF6GqUG6ZyI1mv+3A+8ALuB3Aw7HjkqzaIS0bvoLw0QnboRLdOlAJ44LAb7ep/ZffHHN9IBHiYMgilwukHN0EdPV+0dA9+0jZeG1AC1DuNtW4nev8TwxsLM8diu4EGIxKDixK+Vv6VV93G5gtAGzsI9+jMSt61Ok6N6fK1nfoSWsDIrno1CM8gydkdRm4/y4Ivp5ByW+1FaUwVHuhwYDAchVfb3LJuEwGC1y/wDFCqyXgIdnDX5Jkg1cka3lWIyK2ysy3CKUHahyeWxBqmoHHbwsnFkuZDqMS3QNRWRqcbcnyKhr8qIz0UWoKGx/4S7Z1izQANdRP0Bz4kE3yaQvH2CQ+Q+WuKv8+SUNCI8bo67Ka2x5q73lhkevWYjOL2OR64hCXQPdeM7gKP0/d0dDxTvX9koJ8KGXMfqfsJmOBDXWXKpXuuIOJEYp3WvcsTEPbuhHQ3R3DpKFUe3sj4BubZShpzCy7mB4LCFUmH9Z9GPdhquO68B1XmlxqlT5p+eaz/0l87DuUGX8LJdcuAJxreVAYazaLUHSbR+JSR6s+CCn72Ay7XXmIV8V2Rsj9a4msefajY/q/8WqONGTogYLf+m8JcXeq10vGxCywMVkS2BMp2IMZ4LkF6IRvTGf72N+A78E3OVZMgP46lv7hvjY9dnE9ysLyVYhf03Zfc4XxKyrKIYJQLvFz/kARCM4DYYnr5NM2qyoRdB+SLDgStISK3YSkygWAkuzg3nlargzplVeDpAxHWGfT1o31xnPMyW1cs0Pbu3aFawR8QkbRUx5KvQ4qWrtXU7d7vwT5Nuaven6y79WBt47s3reL6yN2kXMBnhoT2wKFi2keaHVhr8P9yhLdhJzbun+7F0sv5Gl2A6j7QDGeSQMSGrUXQbYsVNTH81ZmEcRfgLZ2Xnow=")
//				.contentType(MediaType.APPLICATION_JSON);
//
//		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
//
//		MockHttpServletResponse response = result.getResponse();
//		assertEquals(HttpStatus.OK.value(), response.getStatus());
//
//		assertEquals(RestServiceInvocationResult.ResponseStatusEnum.FAILURE.toString(), serviceInvocationResult.getResponseStatus().toString());
//	}
//
//	@Test
//	public void testWidgetServicePreAuthBranding_WO_CCMSBranchNameInARH() throws Exception {
//		// create Response
//		/*new MockUp<AELockUtil>() {
//			@Mock
//			public boolean isAELockEnabled(){
//				return false;
//			}
//		};*/ 
//		new MockAELockFalse();
//		Map<Object, Object> widgetMap = new HashMap<Object, Object>();
//		widgetMap.put("AF_ENABLED", "false");
//		widgetMap.put("lineage", "19920_1.0");
//		widgetMap.put("locale", "en_US");
//
//		
//		
//		Map<Object, Object> widgetMapResult = new HashMap<Object, Object>();
//		widgetMapResult.put("IS_HEADER_FOOTER_V2", widgetMap);
//		output.add(widgetMapResult);
//
//		// create input and output Json
//		String outputJson = mapper.writeValueAsString(output);
//		// create mock ServiceInvocation
//
//		serviceInvocationResult.setResponseCMValues(output);
//		serviceInvocationResult.setResponseStatus(RestServiceInvocationResult.ResponseStatusEnum.SUCCESS);
//		
//		new MockAdapterService(serviceInvocationResult);
//
//		RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/channel/widgetConfigurations/branding/public")
//				.header("alightRequestHeader",
//						"{\"locale\":\"en_US\",\"channelRequestData\":\"URL::login::groupId::6727607::orgName::hmorg::device::Desktop::gblsId::9aeb05d1-c150-41b7-96e0-31185803cfeb_2023-06-29-06.06.20.080000::uxPageRequestId::null::pageName::login::deviceType::null::sessionCreatedTimestamp::null::widgetName::null::\",\"clientId\":\"19968\"}")
//				.contentType(MediaType.APPLICATION_JSON);
//
//		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
//
//		MockHttpServletResponse response = result.getResponse();
//		assertEquals(HttpStatus.OK.value(), response.getStatus());
//
//		JSONAssert.assertEquals(outputJson, response.getContentAsString(), false);
//	}
//	
//	@Test
//	public void testWidgetServicePreAuthBranding_WITH_CCMSBranchNameInARH() throws Exception {
//		// create Response
//		/*new MockUp<AELockUtil>() {
//			@Mock
//			public boolean isAELockEnabled(){
//				return false;
//			}
//		};*/ 
//		new MockAELockFalse();
//		Map<Object, Object> widgetMap = new HashMap<Object, Object>();
//		widgetMap.put("AF_ENABLED", "false");
//		widgetMap.put("lineage", "19920_1.0");
//		widgetMap.put("locale", "en_US");
//	
//		Map<Object, Object> widgetMapResult = new HashMap<Object, Object>();
//		widgetMapResult.put("IS_HEADER_FOOTER_V2", widgetMap);
//		output.add(widgetMapResult);
//
//		// create input and output Json
//		String outputJson = mapper.writeValueAsString(output);
//		// create mock ServiceInvocation
//
//		serviceInvocationResult.setResponseCMValues(output);
//		serviceInvocationResult.setResponseStatus(RestServiceInvocationResult.ResponseStatusEnum.SUCCESS);
//		
//		new MockAdapterService(serviceInvocationResult);
//
//		RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/channel/widgetConfigurations/branding/public")
//				.header("alightRequestHeader",
//						"{\"locale\":\"en_US\",\"channelRequestData\":\"CCMSBranchName::LALITHABRANCH_DONOTDELETE::URL::login::groupId::6727607::orgName::hmorg::device::Desktop::gblsId::9aeb05d1-c150-41b7-96e0-31185803cfeb_2023-06-29-06.06.20.080000::uxPageRequestId::null::pageName::login::deviceType::null::sessionCreatedTimestamp::null::widgetName::null::\",\"clientId\":\"19968\"}")
//				.contentType(MediaType.APPLICATION_JSON);
//
//		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
//
//		MockHttpServletResponse response = result.getResponse();
//		assertEquals(HttpStatus.OK.value(), response.getStatus());
//
//		JSONAssert.assertEquals(outputJson, response.getContentAsString(), false);
//	}
//	
//	@Test
//	public void getAssetValue_configDataMap_eq_null(){
//		String assetValue = WidgetRestController.getUCEAssetValue(null, null, null);
//		assertNull(assetValue, "when configDataMap is null then it returns null");
//	}
//	
//	@Test
//	public void getAssetValue_configDataMap_neq_null_and_assetkey_not_exists(){
//		Map<Object, Object> configDataMap = new HashMap<>();
//		String assetValue = WidgetRestController.getUCEAssetValue(configDataMap, null, null);
//		assertNull(assetValue, "when configDataMap is not null but assetkey not exists then it returns null");
//	}
//	
//	@Test
//	public void getAssetValue_configDataMap_neq_null_and_assetkey_exists_but_value_eq_null(){
//		Map<Object, Object> configDataMap = new HashMap<>();
//		configDataMap.put(WidgetRestController.ASSET_UCE, null);
//		String assetValue = WidgetRestController.getUCEAssetValue(configDataMap, null, null);
//		assertNull(assetValue, "when configDataMap is not null but assetkey exists but value is null then it returns null");
//	}
//	
//	@Test
//	public void getAssetValue_configDataMap_neq_null_and_assetkey_exists_but_assetMap_neq_null(){
//		Map<Object, Object> configDataMap = new HashMap<>();
//		Map<Object, Object> assetGrps = new HashMap<>();
//		configDataMap.put(WidgetRestController.ASSET_UCE, assetGrps);
//		String assetValue = WidgetRestController.getUCEAssetValue(configDataMap, null, null);
//		assertNull(assetValue, "when configDataMap is not null but assetkey exists but assetmap is not null then it returns null");
//	}
//	
//	@Test
//	public void getAssetValue_configDataMap_neq_null_and_assetkey_exists_but_assetMap_neq_null_and_contains_group(){
//		Map<Object, Object> configDataMap = new HashMap<>();
//		Map<Object, Object> assetGrps = new HashMap<>();
//		AssetDTO assetDTO = new AssetDTO("WLF_HOME_REORDER_TEMPLATE_DEFAULT_ASSET", "banner,recommendation,journeys,todos,quicklinks,pillars,foryou");
//		assetGrps.put("WLF_HOME_REORDER_TEMPLATE_DEFAULT_ASSET", assetDTO);
//		configDataMap.put("WLF_HOME_REORDER_TEMPLATE_1_ASSET", assetGrps);
//		String assetValue = WidgetRestController.getUCEAssetValue(configDataMap, "WLF_HOME_REORDER_TEMPLATE_1_ASSET", "WLF_HOME_REORDER_TEMPLATE_DEFAULT_ASSET");
//		assertNotNull(assetValue, "when configDataMap is not null but assetkey exists but assetmap is not null and contains group then it returns null");
//	}
//	
//	@Test
//	public void getAssetValue_configDataMap_neq_null_and_assetkey_exists_but_assetMap_neq_null_and_contains_group_test(){
//		Map<Object, Object> configDataMap = new HashMap<>();
//		Map<Object, Object> assetGrps = new HashMap<>();
//		AssetDTO assetDTO = new AssetDTO("WLF_HOME_REORDER_TEMPLATE_DEFAULT_ASSET", "banner,recommendation,journeys,todos,quicklinks,pillars,foryou");
//		assetDTO.setAssetValue(null);
//		assetGrps.put("WLF_HOME_REORDER_TEMPLATE_DEFAULT_ASSET", assetDTO);
//		configDataMap.put("WLF_HOME_REORDER_TEMPLATE_1_ASSET", assetGrps);
//		String assetValue = WidgetRestController.getUCEAssetValue(configDataMap, "WLF_HOME_REORDER_TEMPLATE_1_ASSET", "WLF_HOME_REORDER_TEMPLATE_DEFAULT_ASSET");
//		assertNull(assetValue, "when configDataMap is not null but assetkey exists but assetmap is not null and contains group then it returns null");
//	}
//	
//	
//	
//	@Test
//	public void getAssetValue_configDataMap_neq_null_and_assetkey_exists_but_assetMap_neq_null_and_contains_group_and_asset_map_neq_null(){
//		Map<Object, Object> configDataMap = new HashMap<>();
//		Map<Object, Object> assetGrps = new HashMap<>();
//		Map<Object, Object> assets = new HashMap<>();
//		Map<Object, Object> assetsnew = new HashMap<>();
//		
//		assetsnew.put("test", "banner");
//		assets.put("WLF_HOME_REORDER_TEMPLATE_DEFAULT_ASSET", assetsnew);
//		assetGrps.put("test", assets);
//		configDataMap.put(WidgetRestController.ASSET_UCE, assetGrps);
//		String assetValue = WidgetRestController.getUCEAssetValue(configDataMap, "test", "WLF_HOME_REORDER_TEMPLATE_DEFAULT_ASSET");
//		assertNull(assetValue, "when configDataMap is not null but assetkey exists but assetmap is not null and contains group and asset map is not null then it returns null");
//	}
//	
//	@Test
//	public void getOrEmpty_Test(){
//		Map<String, String> userData = new HashMap<>();
//		userData.put("test", "testData");
//		assertNotNull(WidgetRestController.getOrEmpty(userData, "test"), "when configDataMap is not null but assetkey exists but assetmap is not null and contains group then it returns null");
//    }
//	
//	
//	
//	
//}