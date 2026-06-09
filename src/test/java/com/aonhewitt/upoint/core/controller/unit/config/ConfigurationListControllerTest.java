//package com.aonhewitt.upoint.core.controller.unit.config;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
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
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.MvcResult;
//import org.springframework.test.web.servlet.RequestBuilder;
//import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
//
//import com.aonhewitt.beans.GenericRequestBean;
//import com.aonhewitt.portal.core.service.util.CmServicesUtil;
//import com.aonhewitt.portal.core.udp.pojo.person.PersonService;
//import com.aonhewitt.portal.hasbro.udp.base.service.pojo.UDPResponse;
//import com.aonhewitt.portal.service.delegators.ConfigManagerService;
//import com.aonhewitt.portal.util.AppContainerProvider;
//import com.aonhewitt.portal.util.MessageAppContainer;
//import com.aonhewitt.portal.util.RestServiceInvocationResult;
//import com.aonhewitt.upoint.conf.AppConfig;
//import com.aonhewitt.upoint.conf.AppProperties;
//import com.aonhewitt.upoint.core.controller.config.ConfigurationListController;
//import com.aonhewitt.upoint.core.service.util.ExpressionsCacheUtility;
//import com.aonhewitt.upoint.util.ConfigurationListUtils;
//import com.fasterxml.jackson.databind.ObjectMapper;
//
///**
// * 
// * @author A0152308
// * 
// *
// */
//
//@WebMvcTest(value = ConfigurationListController.class)
////@TestPropertySource(properties = {"spring.cloud.discovery.enabled=false","spring.profiles.active=int"})
//@TestPropertySource(properties = { "spring.cloud.consul.discovery.enabled=false","spring.cloud.consul.config.enabled=false","spring.profiles.active=int"})
//
////@Import(ConfigurationTestConfig.class)
//public class ConfigurationListControllerTest {
//	
//
//	/*@TestConfiguration
//	public static class ConfigurationTestConfig{
//		
//		@Bean
//		public ConfigurationListFilter getFilter(){
//			return new  ConfigurationListFilter();
//		}
//		
//	}*/
//	
//	@Autowired
//    private MockMvc mockMvc;
//	
//	//@MockBean
//	//ConfigurationListFilter configFilter;
//	
//	@MockBean
//	TomcatServletWebServerFactory tomcatServletWebServerFactory;
//	
//	@MockBean
//	private ConfigManagerService configManagerService;
//	
//	@MockBean
//	private CmServicesUtil cmServicesUtil;
//	
//	@MockBean
//	private ConfigurationListUtils configurationListUtils;
//	
//	@MockBean
//	private AppConfig appConfig;
//	
//	@MockBean
//	private ExpressionsCacheUtility expressionsCacheUtility;
//	
//	@MockBean
//	private AppProperties appProperties;
//	
//	
//	private ObjectMapper mapper;
//	private List<Map<Object, Object>> output;
//	
//	
//	RestServiceInvocationResult serviceResponseResult;
//
//	MessageAppContainer messageAppContainer;
//	
//	@BeforeEach
//	public void setUp() throws Exception {
//		mapper = new ObjectMapper();
//		output = new ArrayList<Map<Object, Object>>();
//		serviceResponseResult = new RestServiceInvocationResult();
//		
//		//messageAppContainer.set
//		
//		/*new MockUp<AHUserLocalServiceUtil>() {
//			@Mock
//			AHUser getAHUser(){
//				return new AHUser();
//			}
//		};*/
//		Mockito.when(appConfig.getProfile()).thenReturn("localdev");
//		
//		/*new MockUp<CommonUtil>() {
//			@Mock
//			public String getDomainLineage() {
//				return lineage;
//			}
//		};*/
//		
//		/*new MockUp<CoreFeatureUtil>() {
//			@Mock
//			public String getLocale() {
//				return locale;
//			}
//		};*/
//		/*new MockUp<AppContainerProvider>() {
//			@Mock
//			MessageAppContainer getContainer() {
//				return messageAppContainer;
//			}
//		};*/
//		messageAppContainer = new MessageAppContainer();
//		messageAppContainer.setLineage("19920_1.0");
//		messageAppContainer.getBusinessLineages().put("CM", "19920_1.0");
//		// messageAppContainer.setAsgSessionToken(aSGSessionToken);
//		AppContainerProvider.setContainer(messageAppContainer);
//	}
//
//	@AfterEach
//	public void tearDown() throws Exception {
//		mapper = null;
//		output = null;
//		serviceResponseResult = null;
//
//	}
//	
//	@Test
//	public void testConfigurationControllerPreAuthConfigSet() throws Exception
//	{
//		// create Response
//	     Map<Object, Object> aggregateMap = new HashMap<Object, Object>();
//				aggregateMap.put("AA", "Army-America");
//
//		 Map<Object, Object> aggregateMapResult = new HashMap<Object, Object>();
//	 	 aggregateMapResult.put("text", aggregateMap);
//	     output.add(aggregateMapResult);
// 		 // create input and output Json
//		 String outputJson = mapper.writeValueAsString(output);
//		 // create mock serviceResponseResult
//		 serviceResponseResult.setResponseCMValues(output);
//		 serviceResponseResult.setResponseStatus(RestServiceInvocationResult.ResponseStatusEnum.SUCCESS);
//		 String alightRequestHeader= "{\"locale\":\"en_US\",\"consumerReferenceId\":\"3434\",\"roleId\":\\\"19941_1.0-E:\\\"}";
//		
//		Mockito.when(configManagerService.getResponse(Mockito.any(GenericRequestBean.class))).thenReturn(serviceResponseResult);
//		
//		String inputJson ="{ \"lineage\": \"19920_1.0\", \"locale\": \"en_US\", \"operation\": \"configset\", \"textKeys\": [ { \"name\": \"AA\", \"alias\": \"AA Alias Name\" } ] }";
//		RequestBuilder requestBuilder = MockMvcRequestBuilders.post
//				("""
//						/channel/configurationList/get/public?requestInfoLogParam=true\
//						&showCachedShortCacheInfo=true&showJVMShortCacheInfo=true&allClients=true\
//						""").header("alightSecureSessionToken",
//				"").header("alightRequestHeader", alightRequestHeader)
//				.contentType(MediaType.APPLICATION_JSON).content(inputJson);
//		
//		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
//		MockHttpServletResponse response = result.getResponse();
//		assertEquals(HttpStatus.OK.value(), response.getStatus());
//		JSONAssert.assertEquals(outputJson, response.getContentAsString(), false);
//		
//		//second UseCase
//		serviceResponseResult.setResponseStatus(RestServiceInvocationResult.ResponseStatusEnum.FAILURE);
//		Map<Object, Object> errorMap = new HashMap<Object, Object>();
//		List<Map<Object, Object>> errorList = new ArrayList<Map<Object, Object>>();
//		errorMap.put("error", "Invalid Request");
//		errorList.add(errorMap);
//		serviceResponseResult.setResponseCMError(errorList);
//		Mockito.when(configManagerService.getResponse(Mockito.any(GenericRequestBean.class))).thenReturn(serviceResponseResult);
//		inputJson ="{ \"lineage\": \"19920_1.0\", \"locale\": \"en_US\", \"operation\": \"configset\", \"textKeys\": [ { \"name\": \"AA\", \"alias\": \"AA Alias Name\" } ] }";
//		requestBuilder = MockMvcRequestBuilders.post("/channel/configurationList/get/public")
//				.header("alightSecureSessionToken",
//						"").header("alightRequestHeader", alightRequestHeader).contentType(MediaType.APPLICATION_JSON).content(inputJson);
//		result = mockMvc.perform(requestBuilder).andReturn();
//		response = result.getResponse();
//		assertEquals(HttpStatus.OK.value(), response.getStatus());
//	//	assertThat(response.getContentAsString()!=null);
//		
//		//Third UseCase
//		serviceResponseResult=null;
//		Mockito.when(configManagerService.getResponse(Mockito.any(GenericRequestBean.class))).thenReturn(serviceResponseResult);
//		inputJson ="{ \"lineage\": \"19920_1.0\", \"locale\": \"en_US\", \"operation\": \"configset\", \"textKeys\": [ { \"name\": \"AA\", \"alias\": \"AA Alias Name\" } ] }";
//		requestBuilder = MockMvcRequestBuilders.post("/channel/configurationList/get/public").header("alightSecureSessionToken",
//				"").
//				header("alightRequestHeader", alightRequestHeader).contentType(MediaType.APPLICATION_JSON).content(inputJson);
//		result = mockMvc.perform(requestBuilder).andReturn();
//		
//		response = result.getResponse();
//		assertEquals(HttpStatus.OK.value(), response.getStatus());
//	//	assertThat(response.getContentAsString()!=null);
//	}
//	@Test
//	public void testConfigurationControllerConfigSet() throws Exception
//	{
//		// create Response
//	     Map<Object, Object> aggregateMap = new HashMap<Object, Object>();
//				aggregateMap.put("AA", "Army-America");
//
//		 Map<Object, Object> aggregateMapResult = new HashMap<Object, Object>();
//	 	 aggregateMapResult.put("text", aggregateMap);
//	     output.add(aggregateMapResult);
// 		 // create input and output Json
//		 String outputJson = mapper.writeValueAsString(output);
//		 // create mock serviceResponseResult
//		 serviceResponseResult.setResponseCMValues(output);
//		 serviceResponseResult.setResponseStatus(RestServiceInvocationResult.ResponseStatusEnum.SUCCESS);
//		
//		Mockito.when(configManagerService.getResponse(Mockito.any(GenericRequestBean.class))).thenReturn(serviceResponseResult);
//		
//		String inputJson ="{ \"lineage\": \"19920_1.0\", \"locale\": \"en_US\", \"operation\": \"configset\",\"uce\": \"true\", \"textKeys\": [ { \"name\": \"AA\", \"alias\": \"AA Alias Name\" } ] }";
//		RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/channel/configurationList/get")
//				.header("alightSecureSessionToken",
//						"yPAGJ0XvzYy7sJUtoBHt9BulwOHvW7kxHvkl7vv2+ufeovToLFWvAXdUExffzmcgA1gYPn+dHaHNUTT6fR5SRx5/j9+/5nO2/X2dRf47Plhx7bi8SkzEZhoUpo8wEGzNeU4O1ft2naVxfKIi4V4xjNe64bpwar8y/G3/0Lx96MF3BNTyzWXGZ78mM5hLUx6f24kLK/aYsx+vI/1wn0y8kIO/V4zhUBrRj2Ixe8W3CF4E8vaDOJZySj5Qw2PGyhUz4STfCtzStP8k2uVsKyW1HorkU4DkDVVg6YdS6IIB/wkyurisvp3MN3IBRzj2uxctkmZ67zpJjYzNNyeWtXa/INR5nLcoaO+AgfRYbFWW05FjQSrL+OBLFe6XxJRA0nPJl3ExBvCxRLbHEEFzII5qQagjje+Ykn3zdR46PTcBgm6bxWmS3jgG0Ju2cWiQfZn2/wLBcMVMJ+jVVa7o6WEJtsXCagnHjdyzwdiLD5f/uB4CsmmqqM+cWR6n9w2c8MVGm7a0A6PczM/CLtlJrPrTbOg+nNwuML5uURp19t4Ryh4JxtLkDuooXH4lpOJL1E2HB75zg1oIX3sqQ6Nj7i1Fu8LpHHqwx5NIz034oaFrjc7Q1DWaLI/HOmIIjWhqOliNb9RzpTaVRQWTKdeT8Ci4vvdYYmauW/94DYPh+eqefFxAff3/ADfW7H4Ssq2DWLoD6DJmsw+ossjUwzBrfHh1aMbZ9HL0lRXMwI40jwBbwrmBBxZiwSXUxLIM+c/8ystJN0J56A5WR+ltHiR/AdfDOwZIzOwmGmWQKa0/PYr5LUa75T0LyRNKR2WyfocBtWDyiyaoDylDOQdWkxTqvLlrY1xWpOHGcIrRbj3sMrGrVnz9MwR9l3dYNMnN4Jo/XzN2ghO12jBnO0Prlm1dxgw37Yfaqj49X7QcFmfGJGO+P3yA/jMz97cVR0q5pfkuinRdUBiCnjycO8OOlZz7ni0rTQICOqIL8Qr2oClFl1e1VPAiIozvzQrXISe6sfanVzzusjGldD+C7rzJe2DSCrOXirdFMn5JQidU9fX8XZTiyvgYBg9GgvDc+zvogUfPhHhDOi4CTd0zPsx3BAnWHmNH7fmIjkrGXwtwXrAqZ9d/auhnsMAgpeKJ62B/KfII5m139+Vgvsyx6GU8bw1li0Cl5Wys2/Oa0tssUxoE/ZVMhb8gCN3omP9MKr8YhXoprWY4cKweklbKsrnZA3o2dWMnS9izJlEWg6ku7k/L7pEb4y9kPB21A6VGSDYn9aGpV/tLORk9WgCKgrRep50U1i/zRjGml3Kpj5C/P3biaERyd8+gh3EjKuTRYd0Gb1CLb81Qj6vne8QMJau4VBO73/xTpv9xqGbYYDskrZYeS80SwcwMUolcGIEZq/oUUAFQEIX6mtn7bUaQZEkEDghh7wvTEOcnwdWD4EZ/1TkRNSugXdjvZN7XLydavYhv71UFbRfmyNcvjFE6aI348P9TlmyC+BBBjGLQ9PRYXrS7QJo58GuGUiIDu6J6uJFKUKMBpvuBcZoRgMARYgc4EYRlrMl6WjieIyibK3/d1XTAkw1YcpSBfnJ9etspOcab17Lo/75oOX5Oc2N2IteCqHk4JI1sjZu4qv01OX9xhajh/tKJ4Rbn4zxHybwl+tgSnCTL3uJ9E5KrUn886et3BOXL4kEz8sjyCDJHlJJfB+PFjnLdZgD/Li8XIdJ5EXX0I2Zzz7gVfiO4MzeQXgzvu1/o5TAEDp7PrGqHCHJCn35Vr2jlQcCLnmN+6GWyuEf3iMe39UuWCBFqoEPanB3L5jz5Y+O0Wvm4LU951nDP2a5glnDyiOB5UCOSSl2/nFXoLNvyxCKDnNGIfDdCZKShW3spOfeVKZoHoqBbG9NqBBX8izWQB5/c7aa5sjnRXT7+jU9RiNOgsJYNPa03A5BHp/HaJo80iQx2TsTMI2Nd5BZa0gQ7RT8=")
//				.contentType(MediaType.APPLICATION_JSON).content(inputJson);
//		
//		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
//		MockHttpServletResponse response = result.getResponse();
//		assertEquals(HttpStatus.OK.value(), response.getStatus());
//		JSONAssert.assertEquals(outputJson, response.getContentAsString(), false);
//		
//		//second UseCase
//		serviceResponseResult.setResponseStatus(RestServiceInvocationResult.ResponseStatusEnum.FAILURE);
//		Map<Object, Object> errorMap = new HashMap<Object, Object>();
//		List<Map<Object, Object>> errorList = new ArrayList<Map<Object, Object>>();
//		errorMap.put("error", "Invalid Request");
//		errorList.add(errorMap);
//		serviceResponseResult.setResponseCMError(errorList);
//		Mockito.when(configManagerService.getResponse(Mockito.any(GenericRequestBean.class))).thenReturn(serviceResponseResult);
//		inputJson ="{ \"lineage\": \"19920_1.0\", \"locale\": \"en_US\", \"operation\": \"configset\", \"textKeys\": [ { \"name\": \"AA\", \"alias\": \"AA Alias Name\" } ] }";
//		requestBuilder = MockMvcRequestBuilders.post("/channel/configurationList/get")
//				.header("alightSecureSessionToken",
//						"yPAGJ0XvzYy7sJUtoBHt9BulwOHvW7kxHvkl7vv2+ufeovToLFWvAXdUExffzmcgA1gYPn+dHaHNUTT6fR5SRx5/j9+/5nO2/X2dRf47Plhx7bi8SkzEZhoUpo8wEGzNeU4O1ft2naVxfKIi4V4xjNe64bpwar8y/G3/0Lx96MF3BNTyzWXGZ78mM5hLUx6f24kLK/aYsx+vI/1wn0y8kIO/V4zhUBrRj2Ixe8W3CF4E8vaDOJZySj5Qw2PGyhUz4STfCtzStP8k2uVsKyW1HorkU4DkDVVg6YdS6IIB/wkyurisvp3MN3IBRzj2uxctkmZ67zpJjYzNNyeWtXa/INR5nLcoaO+AgfRYbFWW05FjQSrL+OBLFe6XxJRA0nPJl3ExBvCxRLbHEEFzII5qQagjje+Ykn3zdR46PTcBgm6bxWmS3jgG0Ju2cWiQfZn2/wLBcMVMJ+jVVa7o6WEJtsXCagnHjdyzwdiLD5f/uB4CsmmqqM+cWR6n9w2c8MVGm7a0A6PczM/CLtlJrPrTbOg+nNwuML5uURp19t4Ryh4JxtLkDuooXH4lpOJL1E2HB75zg1oIX3sqQ6Nj7i1Fu8LpHHqwx5NIz034oaFrjc7Q1DWaLI/HOmIIjWhqOliNb9RzpTaVRQWTKdeT8Ci4vvdYYmauW/94DYPh+eqefFxAff3/ADfW7H4Ssq2DWLoD6DJmsw+ossjUwzBrfHh1aMbZ9HL0lRXMwI40jwBbwrmBBxZiwSXUxLIM+c/8ystJN0J56A5WR+ltHiR/AdfDOwZIzOwmGmWQKa0/PYr5LUa75T0LyRNKR2WyfocBtWDyiyaoDylDOQdWkxTqvLlrY1xWpOHGcIrRbj3sMrGrVnz9MwR9l3dYNMnN4Jo/XzN2ghO12jBnO0Prlm1dxgw37Yfaqj49X7QcFmfGJGO+P3yA/jMz97cVR0q5pfkuinRdUBiCnjycO8OOlZz7ni0rTQICOqIL8Qr2oClFl1e1VPAiIozvzQrXISe6sfanVzzusjGldD+C7rzJe2DSCrOXirdFMn5JQidU9fX8XZTiyvgYBg9GgvDc+zvogUfPhHhDOi4CTd0zPsx3BAnWHmNH7fmIjkrGXwtwXrAqZ9d/auhnsMAgpeKJ62B/KfII5m139+Vgvsyx6GU8bw1li0Cl5Wys2/Oa0tssUxoE/ZVMhb8gCN3omP9MKr8YhXoprWY4cKweklbKsrnZA3o2dWMnS9izJlEWg6ku7k/L7pEb4y9kPB21A6VGSDYn9aGpV/tLORk9WgCKgrRep50U1i/zRjGml3Kpj5C/P3biaERyd8+gh3EjKuTRYd0Gb1CLb81Qj6vne8QMJau4VBO73/xTpv9xqGbYYDskrZYeS80SwcwMUolcGIEZq/oUUAFQEIX6mtn7bUaQZEkEDghh7wvTEOcnwdWD4EZ/1TkRNSugXdjvZN7XLydavYhv71UFbRfmyNcvjFE6aI348P9TlmyC+BBBjGLQ9PRYXrS7QJo58GuGUiIDu6J6uJFKUKMBpvuBcZoRgMARYgc4EYRlrMl6WjieIyibK3/d1XTAkw1YcpSBfnJ9etspOcab17Lo/75oOX5Oc2N2IteCqHk4JI1sjZu4qv01OX9xhajh/tKJ4Rbn4zxHybwl+tgSnCTL3uJ9E5KrUn886et3BOXL4kEz8sjyCDJHlJJfB+PFjnLdZgD/Li8XIdJ5EXX0I2Zzz7gVfiO4MzeQXgzvu1/o5TAEDp7PrGqHCHJCn35Vr2jlQcCLnmN+6GWyuEf3iMe39UuWCBFqoEPanB3L5jz5Y+O0Wvm4LU951nDP2a5glnDyiOB5UCOSSl2/nFXoLNvyxCKDnNGIfDdCZKShW3spOfeVKZoHoqBbG9NqBBX8izWQB5/c7aa5sjnRXT7+jU9RiNOgsJYNPa03A5BHp/HaJo80iQx2TsTMI2Nd5BZa0gQ7RT8=")
//				.contentType(MediaType.APPLICATION_JSON).content(inputJson);
//		result = mockMvc.perform(requestBuilder).andReturn();
//		response = result.getResponse();
//		assertEquals(HttpStatus.OK.value(), response.getStatus());
//		//assertThat(response.getContentAsString()!=null);
//		
//		//Third UseCase
//		serviceResponseResult=null;
//		Mockito.when(configManagerService.getResponse(Mockito.any(GenericRequestBean.class))).thenReturn(serviceResponseResult);
//		inputJson ="{ \"lineage\": \"19920_1.0\", \"locale\": \"en_US\", \"operation\": \"configset\", \"textKeys\": [ { \"name\": \"AA\", \"alias\": \"AA Alias Name\" } ] }";
//		requestBuilder = MockMvcRequestBuilders.post("/channel/configurationList/get")
//				.header("alightSecureSessionToken",
//						"yPAGJ0XvzYy7sJUtoBHt9BulwOHvW7kxHvkl7vv2+ufeovToLFWvAXdUExffzmcgA1gYPn+dHaHNUTT6fR5SRx5/j9+/5nO2/X2dRf47Plhx7bi8SkzEZhoUpo8wEGzNeU4O1ft2naVxfKIi4V4xjNe64bpwar8y/G3/0Lx96MF3BNTyzWXGZ78mM5hLUx6f24kLK/aYsx+vI/1wn0y8kIO/V4zhUBrRj2Ixe8W3CF4E8vaDOJZySj5Qw2PGyhUz4STfCtzStP8k2uVsKyW1HorkU4DkDVVg6YdS6IIB/wkyurisvp3MN3IBRzj2uxctkmZ67zpJjYzNNyeWtXa/INR5nLcoaO+AgfRYbFWW05FjQSrL+OBLFe6XxJRA0nPJl3ExBvCxRLbHEEFzII5qQagjje+Ykn3zdR46PTcBgm6bxWmS3jgG0Ju2cWiQfZn2/wLBcMVMJ+jVVa7o6WEJtsXCagnHjdyzwdiLD5f/uB4CsmmqqM+cWR6n9w2c8MVGm7a0A6PczM/CLtlJrPrTbOg+nNwuML5uURp19t4Ryh4JxtLkDuooXH4lpOJL1E2HB75zg1oIX3sqQ6Nj7i1Fu8LpHHqwx5NIz034oaFrjc7Q1DWaLI/HOmIIjWhqOliNb9RzpTaVRQWTKdeT8Ci4vvdYYmauW/94DYPh+eqefFxAff3/ADfW7H4Ssq2DWLoD6DJmsw+ossjUwzBrfHh1aMbZ9HL0lRXMwI40jwBbwrmBBxZiwSXUxLIM+c/8ystJN0J56A5WR+ltHiR/AdfDOwZIzOwmGmWQKa0/PYr5LUa75T0LyRNKR2WyfocBtWDyiyaoDylDOQdWkxTqvLlrY1xWpOHGcIrRbj3sMrGrVnz9MwR9l3dYNMnN4Jo/XzN2ghO12jBnO0Prlm1dxgw37Yfaqj49X7QcFmfGJGO+P3yA/jMz97cVR0q5pfkuinRdUBiCnjycO8OOlZz7ni0rTQICOqIL8Qr2oClFl1e1VPAiIozvzQrXISe6sfanVzzusjGldD+C7rzJe2DSCrOXirdFMn5JQidU9fX8XZTiyvgYBg9GgvDc+zvogUfPhHhDOi4CTd0zPsx3BAnWHmNH7fmIjkrGXwtwXrAqZ9d/auhnsMAgpeKJ62B/KfII5m139+Vgvsyx6GU8bw1li0Cl5Wys2/Oa0tssUxoE/ZVMhb8gCN3omP9MKr8YhXoprWY4cKweklbKsrnZA3o2dWMnS9izJlEWg6ku7k/L7pEb4y9kPB21A6VGSDYn9aGpV/tLORk9WgCKgrRep50U1i/zRjGml3Kpj5C/P3biaERyd8+gh3EjKuTRYd0Gb1CLb81Qj6vne8QMJau4VBO73/xTpv9xqGbYYDskrZYeS80SwcwMUolcGIEZq/oUUAFQEIX6mtn7bUaQZEkEDghh7wvTEOcnwdWD4EZ/1TkRNSugXdjvZN7XLydavYhv71UFbRfmyNcvjFE6aI348P9TlmyC+BBBjGLQ9PRYXrS7QJo58GuGUiIDu6J6uJFKUKMBpvuBcZoRgMARYgc4EYRlrMl6WjieIyibK3/d1XTAkw1YcpSBfnJ9etspOcab17Lo/75oOX5Oc2N2IteCqHk4JI1sjZu4qv01OX9xhajh/tKJ4Rbn4zxHybwl+tgSnCTL3uJ9E5KrUn886et3BOXL4kEz8sjyCDJHlJJfB+PFjnLdZgD/Li8XIdJ5EXX0I2Zzz7gVfiO4MzeQXgzvu1/o5TAEDp7PrGqHCHJCn35Vr2jlQcCLnmN+6GWyuEf3iMe39UuWCBFqoEPanB3L5jz5Y+O0Wvm4LU951nDP2a5glnDyiOB5UCOSSl2/nFXoLNvyxCKDnNGIfDdCZKShW3spOfeVKZoHoqBbG9NqBBX8izWQB5/c7aa5sjnRXT7+jU9RiNOgsJYNPa03A5BHp/HaJo80iQx2TsTMI2Nd5BZa0gQ7RT8=")
//				.contentType(MediaType.APPLICATION_JSON).content(inputJson);
//		result = mockMvc.perform(requestBuilder).andReturn();
//		response = result.getResponse();
//		assertEquals(HttpStatus.OK.value(), response.getStatus());
//		//assertThat(response.getContentAsString()!=null);
//	}
//	
//	@Test
//	public void testConfigurationControllerGetAHUser() throws Exception
//	{
//		 // create Response
//				
//		Map<String, Object> aggregateMapResult = new HashMap<String, Object>();
//		// fix for find bug
//		aggregateMapResult.put("AhUserObject", "AhUser is null");
//		Mockito.when(configurationListUtils.getAhUserObject()).thenReturn(aggregateMapResult);
//		
//		String inputJson ="{ \"lineage\": \"19920_1.0\", \"locale\": \"en_US\", \"operation\": \"ahuser\"} ] }";
//		RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/channel/configurationList/get")
//				.header("alightSecureSessionToken",
//						"yPAGJ0XvzYy7sJUtoBHt9BulwOHvW7kxHvkl7vv2+ufeovToLFWvAXdUExffzmcgA1gYPn+dHaHNUTT6fR5SRx5/j9+/5nO2/X2dRf47Plhx7bi8SkzEZhoUpo8wEGzNeU4O1ft2naVxfKIi4V4xjNe64bpwar8y/G3/0Lx96MF3BNTyzWXGZ78mM5hLUx6f24kLK/aYsx+vI/1wn0y8kIO/V4zhUBrRj2Ixe8W3CF4E8vaDOJZySj5Qw2PGyhUz4STfCtzStP8k2uVsKyW1HorkU4DkDVVg6YdS6IIB/wkyurisvp3MN3IBRzj2uxctkmZ67zpJjYzNNyeWtXa/INR5nLcoaO+AgfRYbFWW05FjQSrL+OBLFe6XxJRA0nPJl3ExBvCxRLbHEEFzII5qQagjje+Ykn3zdR46PTcBgm6bxWmS3jgG0Ju2cWiQfZn2/wLBcMVMJ+jVVa7o6WEJtsXCagnHjdyzwdiLD5f/uB4CsmmqqM+cWR6n9w2c8MVGm7a0A6PczM/CLtlJrPrTbOg+nNwuML5uURp19t4Ryh4JxtLkDuooXH4lpOJL1E2HB75zg1oIX3sqQ6Nj7i1Fu8LpHHqwx5NIz034oaFrjc7Q1DWaLI/HOmIIjWhqOliNb9RzpTaVRQWTKdeT8Ci4vvdYYmauW/94DYPh+eqefFxAff3/ADfW7H4Ssq2DWLoD6DJmsw+ossjUwzBrfHh1aMbZ9HL0lRXMwI40jwBbwrmBBxZiwSXUxLIM+c/8ystJN0J56A5WR+ltHiR/AdfDOwZIzOwmGmWQKa0/PYr5LUa75T0LyRNKR2WyfocBtWDyiyaoDylDOQdWkxTqvLlrY1xWpOHGcIrRbj3sMrGrVnz9MwR9l3dYNMnN4Jo/XzN2ghO12jBnO0Prlm1dxgw37Yfaqj49X7QcFmfGJGO+P3yA/jMz97cVR0q5pfkuinRdUBiCnjycO8OOlZz7ni0rTQICOqIL8Qr2oClFl1e1VPAiIozvzQrXISe6sfanVzzusjGldD+C7rzJe2DSCrOXirdFMn5JQidU9fX8XZTiyvgYBg9GgvDc+zvogUfPhHhDOi4CTd0zPsx3BAnWHmNH7fmIjkrGXwtwXrAqZ9d/auhnsMAgpeKJ62B/KfII5m139+Vgvsyx6GU8bw1li0Cl5Wys2/Oa0tssUxoE/ZVMhb8gCN3omP9MKr8YhXoprWY4cKweklbKsrnZA3o2dWMnS9izJlEWg6ku7k/L7pEb4y9kPB21A6VGSDYn9aGpV/tLORk9WgCKgrRep50U1i/zRjGml3Kpj5C/P3biaERyd8+gh3EjKuTRYd0Gb1CLb81Qj6vne8QMJau4VBO73/xTpv9xqGbYYDskrZYeS80SwcwMUolcGIEZq/oUUAFQEIX6mtn7bUaQZEkEDghh7wvTEOcnwdWD4EZ/1TkRNSugXdjvZN7XLydavYhv71UFbRfmyNcvjFE6aI348P9TlmyC+BBBjGLQ9PRYXrS7QJo58GuGUiIDu6J6uJFKUKMBpvuBcZoRgMARYgc4EYRlrMl6WjieIyibK3/d1XTAkw1YcpSBfnJ9etspOcab17Lo/75oOX5Oc2N2IteCqHk4JI1sjZu4qv01OX9xhajh/tKJ4Rbn4zxHybwl+tgSnCTL3uJ9E5KrUn886et3BOXL4kEz8sjyCDJHlJJfB+PFjnLdZgD/Li8XIdJ5EXX0I2Zzz7gVfiO4MzeQXgzvu1/o5TAEDp7PrGqHCHJCn35Vr2jlQcCLnmN+6GWyuEf3iMe39UuWCBFqoEPanB3L5jz5Y+O0Wvm4LU951nDP2a5glnDyiOB5UCOSSl2/nFXoLNvyxCKDnNGIfDdCZKShW3spOfeVKZoHoqBbG9NqBBX8izWQB5/c7aa5sjnRXT7+jU9RiNOgsJYNPa03A5BHp/HaJo80iQx2TsTMI2Nd5BZa0gQ7RT8=")
//				.contentType(MediaType.APPLICATION_JSON).content(inputJson);
//		
//		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
//		MockHttpServletResponse response = result.getResponse();
//		assertEquals(HttpStatus.OK.value(), response.getStatus());
//		
//		
//	
//	}
//	
//	@Test
//	public void testConfigurationControllerGetUpdatedARHForSharedAccess() throws Exception
//	{
//		// fix for find bug
//		Mockito.when(configurationListUtils.getUpdatedARHForSharedAccess()).thenReturn("BusinessService Object is null, So ARH cannot be updated.");
//		
//		String inputJson ="{ \"lineage\": \"19920_1.0\", \"locale\": \"en_US\", \"operation\": \"getUpdatedARHForSharedAccess\"} ] }";
//		RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/channel/configurationList/get")
//				.header("alightSecureSessionToken",
//						"yPAGJ0XvzYy7sJUtoBHt9BulwOHvW7kxHvkl7vv2+ufeovToLFWvAXdUExffzmcgA1gYPn+dHaHNUTT6fR5SRx5/j9+/5nO2/X2dRf47Plhx7bi8SkzEZhoUpo8wEGzNeU4O1ft2naVxfKIi4V4xjNe64bpwar8y/G3/0Lx96MF3BNTyzWXGZ78mM5hLUx6f24kLK/aYsx+vI/1wn0y8kIO/V4zhUBrRj2Ixe8W3CF4E8vaDOJZySj5Qw2PGyhUz4STfCtzStP8k2uVsKyW1HorkU4DkDVVg6YdS6IIB/wkyurisvp3MN3IBRzj2uxctkmZ67zpJjYzNNyeWtXa/INR5nLcoaO+AgfRYbFWW05FjQSrL+OBLFe6XxJRA0nPJl3ExBvCxRLbHEEFzII5qQagjje+Ykn3zdR46PTcBgm6bxWmS3jgG0Ju2cWiQfZn2/wLBcMVMJ+jVVa7o6WEJtsXCagnHjdyzwdiLD5f/uB4CsmmqqM+cWR6n9w2c8MVGm7a0A6PczM/CLtlJrPrTbOg+nNwuML5uURp19t4Ryh4JxtLkDuooXH4lpOJL1E2HB75zg1oIX3sqQ6Nj7i1Fu8LpHHqwx5NIz034oaFrjc7Q1DWaLI/HOmIIjWhqOliNb9RzpTaVRQWTKdeT8Ci4vvdYYmauW/94DYPh+eqefFxAff3/ADfW7H4Ssq2DWLoD6DJmsw+ossjUwzBrfHh1aMbZ9HL0lRXMwI40jwBbwrmBBxZiwSXUxLIM+c/8ystJN0J56A5WR+ltHiR/AdfDOwZIzOwmGmWQKa0/PYr5LUa75T0LyRNKR2WyfocBtWDyiyaoDylDOQdWkxTqvLlrY1xWpOHGcIrRbj3sMrGrVnz9MwR9l3dYNMnN4Jo/XzN2ghO12jBnO0Prlm1dxgw37Yfaqj49X7QcFmfGJGO+P3yA/jMz97cVR0q5pfkuinRdUBiCnjycO8OOlZz7ni0rTQICOqIL8Qr2oClFl1e1VPAiIozvzQrXISe6sfanVzzusjGldD+C7rzJe2DSCrOXirdFMn5JQidU9fX8XZTiyvgYBg9GgvDc+zvogUfPhHhDOi4CTd0zPsx3BAnWHmNH7fmIjkrGXwtwXrAqZ9d/auhnsMAgpeKJ62B/KfII5m139+Vgvsyx6GU8bw1li0Cl5Wys2/Oa0tssUxoE/ZVMhb8gCN3omP9MKr8YhXoprWY4cKweklbKsrnZA3o2dWMnS9izJlEWg6ku7k/L7pEb4y9kPB21A6VGSDYn9aGpV/tLORk9WgCKgrRep50U1i/zRjGml3Kpj5C/P3biaERyd8+gh3EjKuTRYd0Gb1CLb81Qj6vne8QMJau4VBO73/xTpv9xqGbYYDskrZYeS80SwcwMUolcGIEZq/oUUAFQEIX6mtn7bUaQZEkEDghh7wvTEOcnwdWD4EZ/1TkRNSugXdjvZN7XLydavYhv71UFbRfmyNcvjFE6aI348P9TlmyC+BBBjGLQ9PRYXrS7QJo58GuGUiIDu6J6uJFKUKMBpvuBcZoRgMARYgc4EYRlrMl6WjieIyibK3/d1XTAkw1YcpSBfnJ9etspOcab17Lo/75oOX5Oc2N2IteCqHk4JI1sjZu4qv01OX9xhajh/tKJ4Rbn4zxHybwl+tgSnCTL3uJ9E5KrUn886et3BOXL4kEz8sjyCDJHlJJfB+PFjnLdZgD/Li8XIdJ5EXX0I2Zzz7gVfiO4MzeQXgzvu1/o5TAEDp7PrGqHCHJCn35Vr2jlQcCLnmN+6GWyuEf3iMe39UuWCBFqoEPanB3L5jz5Y+O0Wvm4LU951nDP2a5glnDyiOB5UCOSSl2/nFXoLNvyxCKDnNGIfDdCZKShW3spOfeVKZoHoqBbG9NqBBX8izWQB5/c7aa5sjnRXT7+jU9RiNOgsJYNPa03A5BHp/HaJo80iQx2TsTMI2Nd5BZa0gQ7RT8=")
//				.contentType(MediaType.APPLICATION_JSON).content(inputJson);
//		
//		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
//		MockHttpServletResponse response = result.getResponse();
//		assertEquals(HttpStatus.OK.value(), response.getStatus());
//	}
//	
//	@Test
//	public void testConfigurationControllerRequestPayload() throws Exception
//	{
//		 // create Response
//		 Map<String, Object> aggregateMapResult = new HashMap<String, Object>();
//		 aggregateMapResult.put("AhUserObject", "AhUser is null");
//         Mockito.when(configurationListUtils.getAhUserObject()).thenReturn(aggregateMapResult);
//		
//		 String inputJson ="{ \"lineage\": \"19920_1.0\", \"locale\": \"en_US\", \"operation\": \"requestPayload\"} ] }";
//		 RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/channel/configurationList/get")
//				.header("alightSecureSessionToken",
//						"yPAGJ0XvzYy7sJUtoBHt9BulwOHvW7kxHvkl7vv2+ufeovToLFWvAXdUExffzmcgA1gYPn+dHaHNUTT6fR5SRx5/j9+/5nO2/X2dRf47Plhx7bi8SkzEZhoUpo8wEGzNeU4O1ft2naVxfKIi4V4xjNe64bpwar8y/G3/0Lx96MF3BNTyzWXGZ78mM5hLUx6f24kLK/aYsx+vI/1wn0y8kIO/V4zhUBrRj2Ixe8W3CF4E8vaDOJZySj5Qw2PGyhUz4STfCtzStP8k2uVsKyW1HorkU4DkDVVg6YdS6IIB/wkyurisvp3MN3IBRzj2uxctkmZ67zpJjYzNNyeWtXa/INR5nLcoaO+AgfRYbFWW05FjQSrL+OBLFe6XxJRA0nPJl3ExBvCxRLbHEEFzII5qQagjje+Ykn3zdR46PTcBgm6bxWmS3jgG0Ju2cWiQfZn2/wLBcMVMJ+jVVa7o6WEJtsXCagnHjdyzwdiLD5f/uB4CsmmqqM+cWR6n9w2c8MVGm7a0A6PczM/CLtlJrPrTbOg+nNwuML5uURp19t4Ryh4JxtLkDuooXH4lpOJL1E2HB75zg1oIX3sqQ6Nj7i1Fu8LpHHqwx5NIz034oaFrjc7Q1DWaLI/HOmIIjWhqOliNb9RzpTaVRQWTKdeT8Ci4vvdYYmauW/94DYPh+eqefFxAff3/ADfW7H4Ssq2DWLoD6DJmsw+ossjUwzBrfHh1aMbZ9HL0lRXMwI40jwBbwrmBBxZiwSXUxLIM+c/8ystJN0J56A5WR+ltHiR/AdfDOwZIzOwmGmWQKa0/PYr5LUa75T0LyRNKR2WyfocBtWDyiyaoDylDOQdWkxTqvLlrY1xWpOHGcIrRbj3sMrGrVnz9MwR9l3dYNMnN4Jo/XzN2ghO12jBnO0Prlm1dxgw37Yfaqj49X7QcFmfGJGO+P3yA/jMz97cVR0q5pfkuinRdUBiCnjycO8OOlZz7ni0rTQICOqIL8Qr2oClFl1e1VPAiIozvzQrXISe6sfanVzzusjGldD+C7rzJe2DSCrOXirdFMn5JQidU9fX8XZTiyvgYBg9GgvDc+zvogUfPhHhDOi4CTd0zPsx3BAnWHmNH7fmIjkrGXwtwXrAqZ9d/auhnsMAgpeKJ62B/KfII5m139+Vgvsyx6GU8bw1li0Cl5Wys2/Oa0tssUxoE/ZVMhb8gCN3omP9MKr8YhXoprWY4cKweklbKsrnZA3o2dWMnS9izJlEWg6ku7k/L7pEb4y9kPB21A6VGSDYn9aGpV/tLORk9WgCKgrRep50U1i/zRjGml3Kpj5C/P3biaERyd8+gh3EjKuTRYd0Gb1CLb81Qj6vne8QMJau4VBO73/xTpv9xqGbYYDskrZYeS80SwcwMUolcGIEZq/oUUAFQEIX6mtn7bUaQZEkEDghh7wvTEOcnwdWD4EZ/1TkRNSugXdjvZN7XLydavYhv71UFbRfmyNcvjFE6aI348P9TlmyC+BBBjGLQ9PRYXrS7QJo58GuGUiIDu6J6uJFKUKMBpvuBcZoRgMARYgc4EYRlrMl6WjieIyibK3/d1XTAkw1YcpSBfnJ9etspOcab17Lo/75oOX5Oc2N2IteCqHk4JI1sjZu4qv01OX9xhajh/tKJ4Rbn4zxHybwl+tgSnCTL3uJ9E5KrUn886et3BOXL4kEz8sjyCDJHlJJfB+PFjnLdZgD/Li8XIdJ5EXX0I2Zzz7gVfiO4MzeQXgzvu1/o5TAEDp7PrGqHCHJCn35Vr2jlQcCLnmN+6GWyuEf3iMe39UuWCBFqoEPanB3L5jz5Y+O0Wvm4LU951nDP2a5glnDyiOB5UCOSSl2/nFXoLNvyxCKDnNGIfDdCZKShW3spOfeVKZoHoqBbG9NqBBX8izWQB5/c7aa5sjnRXT7+jU9RiNOgsJYNPa03A5BHp/HaJo80iQx2TsTMI2Nd5BZa0gQ7RT8=")
//				.contentType(MediaType.APPLICATION_JSON).content(inputJson);
//		
//		 MvcResult result = mockMvc.perform(requestBuilder).andReturn();
//		 MockHttpServletResponse response = result.getResponse();
//		 assertEquals(HttpStatus.OK.value(), response.getStatus());
//		
//	
//	}
//	
//	@Test
//	public void testConfigurationControllerDBUtil() throws Exception
//	{
//		// create Response
//				
//		Mockito.when(configurationListUtils.testJNDIConnections()).thenReturn("JNDI Connection for all schema is successfull");
//		
//		String inputJson ="{ \"lineage\": \"19920_1.0\", \"locale\": \"en_US\", \"operation\": \"dbDetails\"} ] }";
//		RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/channel/configurationList/get")
//				.header("alightSecureSessionToken",
//						"yPAGJ0XvzYy7sJUtoBHt9BulwOHvW7kxHvkl7vv2+ufeovToLFWvAXdUExffzmcgA1gYPn+dHaHNUTT6fR5SRx5/j9+/5nO2/X2dRf47Plhx7bi8SkzEZhoUpo8wEGzNeU4O1ft2naVxfKIi4V4xjNe64bpwar8y/G3/0Lx96MF3BNTyzWXGZ78mM5hLUx6f24kLK/aYsx+vI/1wn0y8kIO/V4zhUBrRj2Ixe8W3CF4E8vaDOJZySj5Qw2PGyhUz4STfCtzStP8k2uVsKyW1HorkU4DkDVVg6YdS6IIB/wkyurisvp3MN3IBRzj2uxctkmZ67zpJjYzNNyeWtXa/INR5nLcoaO+AgfRYbFWW05FjQSrL+OBLFe6XxJRA0nPJl3ExBvCxRLbHEEFzII5qQagjje+Ykn3zdR46PTcBgm6bxWmS3jgG0Ju2cWiQfZn2/wLBcMVMJ+jVVa7o6WEJtsXCagnHjdyzwdiLD5f/uB4CsmmqqM+cWR6n9w2c8MVGm7a0A6PczM/CLtlJrPrTbOg+nNwuML5uURp19t4Ryh4JxtLkDuooXH4lpOJL1E2HB75zg1oIX3sqQ6Nj7i1Fu8LpHHqwx5NIz034oaFrjc7Q1DWaLI/HOmIIjWhqOliNb9RzpTaVRQWTKdeT8Ci4vvdYYmauW/94DYPh+eqefFxAff3/ADfW7H4Ssq2DWLoD6DJmsw+ossjUwzBrfHh1aMbZ9HL0lRXMwI40jwBbwrmBBxZiwSXUxLIM+c/8ystJN0J56A5WR+ltHiR/AdfDOwZIzOwmGmWQKa0/PYr5LUa75T0LyRNKR2WyfocBtWDyiyaoDylDOQdWkxTqvLlrY1xWpOHGcIrRbj3sMrGrVnz9MwR9l3dYNMnN4Jo/XzN2ghO12jBnO0Prlm1dxgw37Yfaqj49X7QcFmfGJGO+P3yA/jMz97cVR0q5pfkuinRdUBiCnjycO8OOlZz7ni0rTQICOqIL8Qr2oClFl1e1VPAiIozvzQrXISe6sfanVzzusjGldD+C7rzJe2DSCrOXirdFMn5JQidU9fX8XZTiyvgYBg9GgvDc+zvogUfPhHhDOi4CTd0zPsx3BAnWHmNH7fmIjkrGXwtwXrAqZ9d/auhnsMAgpeKJ62B/KfII5m139+Vgvsyx6GU8bw1li0Cl5Wys2/Oa0tssUxoE/ZVMhb8gCN3omP9MKr8YhXoprWY4cKweklbKsrnZA3o2dWMnS9izJlEWg6ku7k/L7pEb4y9kPB21A6VGSDYn9aGpV/tLORk9WgCKgrRep50U1i/zRjGml3Kpj5C/P3biaERyd8+gh3EjKuTRYd0Gb1CLb81Qj6vne8QMJau4VBO73/xTpv9xqGbYYDskrZYeS80SwcwMUolcGIEZq/oUUAFQEIX6mtn7bUaQZEkEDghh7wvTEOcnwdWD4EZ/1TkRNSugXdjvZN7XLydavYhv71UFbRfmyNcvjFE6aI348P9TlmyC+BBBjGLQ9PRYXrS7QJo58GuGUiIDu6J6uJFKUKMBpvuBcZoRgMARYgc4EYRlrMl6WjieIyibK3/d1XTAkw1YcpSBfnJ9etspOcab17Lo/75oOX5Oc2N2IteCqHk4JI1sjZu4qv01OX9xhajh/tKJ4Rbn4zxHybwl+tgSnCTL3uJ9E5KrUn886et3BOXL4kEz8sjyCDJHlJJfB+PFjnLdZgD/Li8XIdJ5EXX0I2Zzz7gVfiO4MzeQXgzvu1/o5TAEDp7PrGqHCHJCn35Vr2jlQcCLnmN+6GWyuEf3iMe39UuWCBFqoEPanB3L5jz5Y+O0Wvm4LU951nDP2a5glnDyiOB5UCOSSl2/nFXoLNvyxCKDnNGIfDdCZKShW3spOfeVKZoHoqBbG9NqBBX8izWQB5/c7aa5sjnRXT7+jU9RiNOgsJYNPa03A5BHp/HaJo80iQx2TsTMI2Nd5BZa0gQ7RT8=")
//				.contentType(MediaType.APPLICATION_JSON).content(inputJson);
//		
//		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
//		MockHttpServletResponse response = result.getResponse();
//		assertEquals(HttpStatus.OK.value(), response.getStatus());
//		
//	
//	}
//	
//	@Test
//	public void testConfigurationControllerUnsupportedRequest() throws Exception
//	{
//		// create Response
//				
//       /* Map<String, Object> aggregateMapResult = new HashMap<String, Object>();
//	    aggregateMapResult.put("Error!", "Unsupported Request Operation!!!");*/
//		String inputJson ="{ \"lineage\": \"19920_1.0\", \"locale\": \"en_US\", \"operation\": \"Unsupported\"} ] }";
//		RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/channel/configurationList/get")
//				.header("alightSecureSessionToken",
//						"yPAGJ0XvzYy7sJUtoBHt9BulwOHvW7kxHvkl7vv2+ufeovToLFWvAXdUExffzmcgA1gYPn+dHaHNUTT6fR5SRx5/j9+/5nO2/X2dRf47Plhx7bi8SkzEZhoUpo8wEGzNeU4O1ft2naVxfKIi4V4xjNe64bpwar8y/G3/0Lx96MF3BNTyzWXGZ78mM5hLUx6f24kLK/aYsx+vI/1wn0y8kIO/V4zhUBrRj2Ixe8W3CF4E8vaDOJZySj5Qw2PGyhUz4STfCtzStP8k2uVsKyW1HorkU4DkDVVg6YdS6IIB/wkyurisvp3MN3IBRzj2uxctkmZ67zpJjYzNNyeWtXa/INR5nLcoaO+AgfRYbFWW05FjQSrL+OBLFe6XxJRA0nPJl3ExBvCxRLbHEEFzII5qQagjje+Ykn3zdR46PTcBgm6bxWmS3jgG0Ju2cWiQfZn2/wLBcMVMJ+jVVa7o6WEJtsXCagnHjdyzwdiLD5f/uB4CsmmqqM+cWR6n9w2c8MVGm7a0A6PczM/CLtlJrPrTbOg+nNwuML5uURp19t4Ryh4JxtLkDuooXH4lpOJL1E2HB75zg1oIX3sqQ6Nj7i1Fu8LpHHqwx5NIz034oaFrjc7Q1DWaLI/HOmIIjWhqOliNb9RzpTaVRQWTKdeT8Ci4vvdYYmauW/94DYPh+eqefFxAff3/ADfW7H4Ssq2DWLoD6DJmsw+ossjUwzBrfHh1aMbZ9HL0lRXMwI40jwBbwrmBBxZiwSXUxLIM+c/8ystJN0J56A5WR+ltHiR/AdfDOwZIzOwmGmWQKa0/PYr5LUa75T0LyRNKR2WyfocBtWDyiyaoDylDOQdWkxTqvLlrY1xWpOHGcIrRbj3sMrGrVnz9MwR9l3dYNMnN4Jo/XzN2ghO12jBnO0Prlm1dxgw37Yfaqj49X7QcFmfGJGO+P3yA/jMz97cVR0q5pfkuinRdUBiCnjycO8OOlZz7ni0rTQICOqIL8Qr2oClFl1e1VPAiIozvzQrXISe6sfanVzzusjGldD+C7rzJe2DSCrOXirdFMn5JQidU9fX8XZTiyvgYBg9GgvDc+zvogUfPhHhDOi4CTd0zPsx3BAnWHmNH7fmIjkrGXwtwXrAqZ9d/auhnsMAgpeKJ62B/KfII5m139+Vgvsyx6GU8bw1li0Cl5Wys2/Oa0tssUxoE/ZVMhb8gCN3omP9MKr8YhXoprWY4cKweklbKsrnZA3o2dWMnS9izJlEWg6ku7k/L7pEb4y9kPB21A6VGSDYn9aGpV/tLORk9WgCKgrRep50U1i/zRjGml3Kpj5C/P3biaERyd8+gh3EjKuTRYd0Gb1CLb81Qj6vne8QMJau4VBO73/xTpv9xqGbYYDskrZYeS80SwcwMUolcGIEZq/oUUAFQEIX6mtn7bUaQZEkEDghh7wvTEOcnwdWD4EZ/1TkRNSugXdjvZN7XLydavYhv71UFbRfmyNcvjFE6aI348P9TlmyC+BBBjGLQ9PRYXrS7QJo58GuGUiIDu6J6uJFKUKMBpvuBcZoRgMARYgc4EYRlrMl6WjieIyibK3/d1XTAkw1YcpSBfnJ9etspOcab17Lo/75oOX5Oc2N2IteCqHk4JI1sjZu4qv01OX9xhajh/tKJ4Rbn4zxHybwl+tgSnCTL3uJ9E5KrUn886et3BOXL4kEz8sjyCDJHlJJfB+PFjnLdZgD/Li8XIdJ5EXX0I2Zzz7gVfiO4MzeQXgzvu1/o5TAEDp7PrGqHCHJCn35Vr2jlQcCLnmN+6GWyuEf3iMe39UuWCBFqoEPanB3L5jz5Y+O0Wvm4LU951nDP2a5glnDyiOB5UCOSSl2/nFXoLNvyxCKDnNGIfDdCZKShW3spOfeVKZoHoqBbG9NqBBX8izWQB5/c7aa5sjnRXT7+jU9RiNOgsJYNPa03A5BHp/HaJo80iQx2TsTMI2Nd5BZa0gQ7RT8=")
//				.contentType(MediaType.APPLICATION_JSON).content(inputJson);
//		
//		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
//		MockHttpServletResponse response = result.getResponse();
//		assertEquals(HttpStatus.OK.value(), response.getStatus());
//	
//	}
//	@Test
//	public void testConfigurationControllerCacheAnalysis() throws Exception
//	{
//		 Map<String, Integer> aggregateMapResult = new HashMap<String, Integer>();
//		 aggregateMapResult.put("channel-widgetconfigurations:SystemParameters:19941:Workday_WDCDS_HR_aa0308962b684b378b4d1b05f0370783_CW",250);
//         Mockito.when(configurationListUtils.printCacheKeySize(Mockito.any(GenericRequestBean.class))).thenReturn(aggregateMapResult);
//		
//		 String inputJson ="{\"operation\": \"cacheAnalysis\",\"cacheKeys\": [ { \"name\": \"channel-widgetconfigurations:SystemParameters*\" } ]} ] }";
//		 RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/channel/configurationList/get")
//				.header("alightSecureSessionToken",
//						"yPAGJ0XvzYy7sJUtoBHt9BulwOHvW7kxHvkl7vv2+ufeovToLFWvAXdUExffzmcgA1gYPn+dHaHNUTT6fR5SRx5/j9+/5nO2/X2dRf47Plhx7bi8SkzEZhoUpo8wEGzNeU4O1ft2naVxfKIi4V4xjNe64bpwar8y/G3/0Lx96MF3BNTyzWXGZ78mM5hLUx6f24kLK/aYsx+vI/1wn0y8kIO/V4zhUBrRj2Ixe8W3CF4E8vaDOJZySj5Qw2PGyhUz4STfCtzStP8k2uVsKyW1HorkU4DkDVVg6YdS6IIB/wkyurisvp3MN3IBRzj2uxctkmZ67zpJjYzNNyeWtXa/INR5nLcoaO+AgfRYbFWW05FjQSrL+OBLFe6XxJRA0nPJl3ExBvCxRLbHEEFzII5qQagjje+Ykn3zdR46PTcBgm6bxWmS3jgG0Ju2cWiQfZn2/wLBcMVMJ+jVVa7o6WEJtsXCagnHjdyzwdiLD5f/uB4CsmmqqM+cWR6n9w2c8MVGm7a0A6PczM/CLtlJrPrTbOg+nNwuML5uURp19t4Ryh4JxtLkDuooXH4lpOJL1E2HB75zg1oIX3sqQ6Nj7i1Fu8LpHHqwx5NIz034oaFrjc7Q1DWaLI/HOmIIjWhqOliNb9RzpTaVRQWTKdeT8Ci4vvdYYmauW/94DYPh+eqefFxAff3/ADfW7H4Ssq2DWLoD6DJmsw+ossjUwzBrfHh1aMbZ9HL0lRXMwI40jwBbwrmBBxZiwSXUxLIM+c/8ystJN0J56A5WR+ltHiR/AdfDOwZIzOwmGmWQKa0/PYr5LUa75T0LyRNKR2WyfocBtWDyiyaoDylDOQdWkxTqvLlrY1xWpOHGcIrRbj3sMrGrVnz9MwR9l3dYNMnN4Jo/XzN2ghO12jBnO0Prlm1dxgw37Yfaqj49X7QcFmfGJGO+P3yA/jMz97cVR0q5pfkuinRdUBiCnjycO8OOlZz7ni0rTQICOqIL8Qr2oClFl1e1VPAiIozvzQrXISe6sfanVzzusjGldD+C7rzJe2DSCrOXirdFMn5JQidU9fX8XZTiyvgYBg9GgvDc+zvogUfPhHhDOi4CTd0zPsx3BAnWHmNH7fmIjkrGXwtwXrAqZ9d/auhnsMAgpeKJ62B/KfII5m139+Vgvsyx6GU8bw1li0Cl5Wys2/Oa0tssUxoE/ZVMhb8gCN3omP9MKr8YhXoprWY4cKweklbKsrnZA3o2dWMnS9izJlEWg6ku7k/L7pEb4y9kPB21A6VGSDYn9aGpV/tLORk9WgCKgrRep50U1i/zRjGml3Kpj5C/P3biaERyd8+gh3EjKuTRYd0Gb1CLb81Qj6vne8QMJau4VBO73/xTpv9xqGbYYDskrZYeS80SwcwMUolcGIEZq/oUUAFQEIX6mtn7bUaQZEkEDghh7wvTEOcnwdWD4EZ/1TkRNSugXdjvZN7XLydavYhv71UFbRfmyNcvjFE6aI348P9TlmyC+BBBjGLQ9PRYXrS7QJo58GuGUiIDu6J6uJFKUKMBpvuBcZoRgMARYgc4EYRlrMl6WjieIyibK3/d1XTAkw1YcpSBfnJ9etspOcab17Lo/75oOX5Oc2N2IteCqHk4JI1sjZu4qv01OX9xhajh/tKJ4Rbn4zxHybwl+tgSnCTL3uJ9E5KrUn886et3BOXL4kEz8sjyCDJHlJJfB+PFjnLdZgD/Li8XIdJ5EXX0I2Zzz7gVfiO4MzeQXgzvu1/o5TAEDp7PrGqHCHJCn35Vr2jlQcCLnmN+6GWyuEf3iMe39UuWCBFqoEPanB3L5jz5Y+O0Wvm4LU951nDP2a5glnDyiOB5UCOSSl2/nFXoLNvyxCKDnNGIfDdCZKShW3spOfeVKZoHoqBbG9NqBBX8izWQB5/c7aa5sjnRXT7+jU9RiNOgsJYNPa03A5BHp/HaJo80iQx2TsTMI2Nd5BZa0gQ7RT8=")
//				.contentType(MediaType.APPLICATION_JSON).content(inputJson);
//		
//		 MvcResult result = mockMvc.perform(requestBuilder).andReturn();
//		 MockHttpServletResponse response = result.getResponse();
//		 assertEquals(HttpStatus.OK.value(), response.getStatus());
//	}
//	@Test
//	public void testConfigurationControllerCachSet() throws Exception
//	{
//		 Map<String, Object> aggregateMapResult = new HashMap<String, Object>();
//		 aggregateMapResult.put("HRO:19941","Exists under cache !");
//         Mockito.when(configurationListUtils.printCacheKeySize(Mockito.any(GenericRequestBean.class))).thenReturn(aggregateMapResult);
//		
//		 String inputJson ="{\"operation\": \"cacheSet\",\"cacheKeys\": [ { \"name\": \"HRO:19941\" } ]} ] }";
//		 RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/channel/configurationList/get")
//				.header("alightSecureSessionToken",
//						"yPAGJ0XvzYy7sJUtoBHt9BulwOHvW7kxHvkl7vv2+ufeovToLFWvAXdUExffzmcgA1gYPn+dHaHNUTT6fR5SRx5/j9+/5nO2/X2dRf47Plhx7bi8SkzEZhoUpo8wEGzNeU4O1ft2naVxfKIi4V4xjNe64bpwar8y/G3/0Lx96MF3BNTyzWXGZ78mM5hLUx6f24kLK/aYsx+vI/1wn0y8kIO/V4zhUBrRj2Ixe8W3CF4E8vaDOJZySj5Qw2PGyhUz4STfCtzStP8k2uVsKyW1HorkU4DkDVVg6YdS6IIB/wkyurisvp3MN3IBRzj2uxctkmZ67zpJjYzNNyeWtXa/INR5nLcoaO+AgfRYbFWW05FjQSrL+OBLFe6XxJRA0nPJl3ExBvCxRLbHEEFzII5qQagjje+Ykn3zdR46PTcBgm6bxWmS3jgG0Ju2cWiQfZn2/wLBcMVMJ+jVVa7o6WEJtsXCagnHjdyzwdiLD5f/uB4CsmmqqM+cWR6n9w2c8MVGm7a0A6PczM/CLtlJrPrTbOg+nNwuML5uURp19t4Ryh4JxtLkDuooXH4lpOJL1E2HB75zg1oIX3sqQ6Nj7i1Fu8LpHHqwx5NIz034oaFrjc7Q1DWaLI/HOmIIjWhqOliNb9RzpTaVRQWTKdeT8Ci4vvdYYmauW/94DYPh+eqefFxAff3/ADfW7H4Ssq2DWLoD6DJmsw+ossjUwzBrfHh1aMbZ9HL0lRXMwI40jwBbwrmBBxZiwSXUxLIM+c/8ystJN0J56A5WR+ltHiR/AdfDOwZIzOwmGmWQKa0/PYr5LUa75T0LyRNKR2WyfocBtWDyiyaoDylDOQdWkxTqvLlrY1xWpOHGcIrRbj3sMrGrVnz9MwR9l3dYNMnN4Jo/XzN2ghO12jBnO0Prlm1dxgw37Yfaqj49X7QcFmfGJGO+P3yA/jMz97cVR0q5pfkuinRdUBiCnjycO8OOlZz7ni0rTQICOqIL8Qr2oClFl1e1VPAiIozvzQrXISe6sfanVzzusjGldD+C7rzJe2DSCrOXirdFMn5JQidU9fX8XZTiyvgYBg9GgvDc+zvogUfPhHhDOi4CTd0zPsx3BAnWHmNH7fmIjkrGXwtwXrAqZ9d/auhnsMAgpeKJ62B/KfII5m139+Vgvsyx6GU8bw1li0Cl5Wys2/Oa0tssUxoE/ZVMhb8gCN3omP9MKr8YhXoprWY4cKweklbKsrnZA3o2dWMnS9izJlEWg6ku7k/L7pEb4y9kPB21A6VGSDYn9aGpV/tLORk9WgCKgrRep50U1i/zRjGml3Kpj5C/P3biaERyd8+gh3EjKuTRYd0Gb1CLb81Qj6vne8QMJau4VBO73/xTpv9xqGbYYDskrZYeS80SwcwMUolcGIEZq/oUUAFQEIX6mtn7bUaQZEkEDghh7wvTEOcnwdWD4EZ/1TkRNSugXdjvZN7XLydavYhv71UFbRfmyNcvjFE6aI348P9TlmyC+BBBjGLQ9PRYXrS7QJo58GuGUiIDu6J6uJFKUKMBpvuBcZoRgMARYgc4EYRlrMl6WjieIyibK3/d1XTAkw1YcpSBfnJ9etspOcab17Lo/75oOX5Oc2N2IteCqHk4JI1sjZu4qv01OX9xhajh/tKJ4Rbn4zxHybwl+tgSnCTL3uJ9E5KrUn886et3BOXL4kEz8sjyCDJHlJJfB+PFjnLdZgD/Li8XIdJ5EXX0I2Zzz7gVfiO4MzeQXgzvu1/o5TAEDp7PrGqHCHJCn35Vr2jlQcCLnmN+6GWyuEf3iMe39UuWCBFqoEPanB3L5jz5Y+O0Wvm4LU951nDP2a5glnDyiOB5UCOSSl2/nFXoLNvyxCKDnNGIfDdCZKShW3spOfeVKZoHoqBbG9NqBBX8izWQB5/c7aa5sjnRXT7+jU9RiNOgsJYNPa03A5BHp/HaJo80iQx2TsTMI2Nd5BZa0gQ7RT8=")
//				.contentType(MediaType.APPLICATION_JSON).content(inputJson);
//		
//		 MvcResult result = mockMvc.perform(requestBuilder).andReturn();
//		 MockHttpServletResponse response = result.getResponse();
//		 assertEquals(HttpStatus.OK.value(), response.getStatus());
//	}
//	
//	@Test
//	public void testConfigurationControllerPrintJedisPoolConfig() throws Exception
//	{
//		 Map<String, Object> aggregateMapResult = new HashMap<String, Object>();
//		 aggregateMapResult.put("JedisConnectionFactory Client Name",""+"Premier");
//		 aggregateMapResult.put("JedisConnectionFactory Time Out",""+0);
//		 aggregateMapResult.put("JedisConnectionFactory Host Name", "localhost");
//		 aggregateMapResult.put("JedisConnectionFactory Port No", ""+"6379");
//		 aggregateMapResult.put("jedisPoolConfig Max Idle", ""+0);
//		 aggregateMapResult.put("jedisPoolConfig Max Idle", ""+0);
//		 aggregateMapResult.put("jedisPoolConfig Max Total",""+ 0);
//		 aggregateMapResult.put("jedisPoolConfig Min Idle", ""+0);
//		 aggregateMapResult.put("jedisPoolConfig Max Wait", ""+0);
//		 aggregateMapResult.put("jedisPoolConfig MinEvictableIdleTime", ""+0);
//         Mockito.when(configurationListUtils.printJedisPoolConfig()).thenReturn(aggregateMapResult);
//		
//		 String inputJson ="{\"operation\": \"jedisPoolConfig\"}";
//		 RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/channel/configurationList/get")
//				.header("alightSecureSessionToken",
//						"yPAGJ0XvzYy7sJUtoBHt9BulwOHvW7kxHvkl7vv2+ufeovToLFWvAXdUExffzmcgA1gYPn+dHaHNUTT6fR5SRx5/j9+/5nO2/X2dRf47Plhx7bi8SkzEZhoUpo8wEGzNeU4O1ft2naVxfKIi4V4xjNe64bpwar8y/G3/0Lx96MF3BNTyzWXGZ78mM5hLUx6f24kLK/aYsx+vI/1wn0y8kIO/V4zhUBrRj2Ixe8W3CF4E8vaDOJZySj5Qw2PGyhUz4STfCtzStP8k2uVsKyW1HorkU4DkDVVg6YdS6IIB/wkyurisvp3MN3IBRzj2uxctkmZ67zpJjYzNNyeWtXa/INR5nLcoaO+AgfRYbFWW05FjQSrL+OBLFe6XxJRA0nPJl3ExBvCxRLbHEEFzII5qQagjje+Ykn3zdR46PTcBgm6bxWmS3jgG0Ju2cWiQfZn2/wLBcMVMJ+jVVa7o6WEJtsXCagnHjdyzwdiLD5f/uB4CsmmqqM+cWR6n9w2c8MVGm7a0A6PczM/CLtlJrPrTbOg+nNwuML5uURp19t4Ryh4JxtLkDuooXH4lpOJL1E2HB75zg1oIX3sqQ6Nj7i1Fu8LpHHqwx5NIz034oaFrjc7Q1DWaLI/HOmIIjWhqOliNb9RzpTaVRQWTKdeT8Ci4vvdYYmauW/94DYPh+eqefFxAff3/ADfW7H4Ssq2DWLoD6DJmsw+ossjUwzBrfHh1aMbZ9HL0lRXMwI40jwBbwrmBBxZiwSXUxLIM+c/8ystJN0J56A5WR+ltHiR/AdfDOwZIzOwmGmWQKa0/PYr5LUa75T0LyRNKR2WyfocBtWDyiyaoDylDOQdWkxTqvLlrY1xWpOHGcIrRbj3sMrGrVnz9MwR9l3dYNMnN4Jo/XzN2ghO12jBnO0Prlm1dxgw37Yfaqj49X7QcFmfGJGO+P3yA/jMz97cVR0q5pfkuinRdUBiCnjycO8OOlZz7ni0rTQICOqIL8Qr2oClFl1e1VPAiIozvzQrXISe6sfanVzzusjGldD+C7rzJe2DSCrOXirdFMn5JQidU9fX8XZTiyvgYBg9GgvDc+zvogUfPhHhDOi4CTd0zPsx3BAnWHmNH7fmIjkrGXwtwXrAqZ9d/auhnsMAgpeKJ62B/KfII5m139+Vgvsyx6GU8bw1li0Cl5Wys2/Oa0tssUxoE/ZVMhb8gCN3omP9MKr8YhXoprWY4cKweklbKsrnZA3o2dWMnS9izJlEWg6ku7k/L7pEb4y9kPB21A6VGSDYn9aGpV/tLORk9WgCKgrRep50U1i/zRjGml3Kpj5C/P3biaERyd8+gh3EjKuTRYd0Gb1CLb81Qj6vne8QMJau4VBO73/xTpv9xqGbYYDskrZYeS80SwcwMUolcGIEZq/oUUAFQEIX6mtn7bUaQZEkEDghh7wvTEOcnwdWD4EZ/1TkRNSugXdjvZN7XLydavYhv71UFbRfmyNcvjFE6aI348P9TlmyC+BBBjGLQ9PRYXrS7QJo58GuGUiIDu6J6uJFKUKMBpvuBcZoRgMARYgc4EYRlrMl6WjieIyibK3/d1XTAkw1YcpSBfnJ9etspOcab17Lo/75oOX5Oc2N2IteCqHk4JI1sjZu4qv01OX9xhajh/tKJ4Rbn4zxHybwl+tgSnCTL3uJ9E5KrUn886et3BOXL4kEz8sjyCDJHlJJfB+PFjnLdZgD/Li8XIdJ5EXX0I2Zzz7gVfiO4MzeQXgzvu1/o5TAEDp7PrGqHCHJCn35Vr2jlQcCLnmN+6GWyuEf3iMe39UuWCBFqoEPanB3L5jz5Y+O0Wvm4LU951nDP2a5glnDyiOB5UCOSSl2/nFXoLNvyxCKDnNGIfDdCZKShW3spOfeVKZoHoqBbG9NqBBX8izWQB5/c7aa5sjnRXT7+jU9RiNOgsJYNPa03A5BHp/HaJo80iQx2TsTMI2Nd5BZa0gQ7RT8=")
//				.contentType(MediaType.APPLICATION_JSON).content(inputJson);
//		
//		 MvcResult result = mockMvc.perform(requestBuilder).andReturn();
//		 MockHttpServletResponse response = result.getResponse();
//		 assertEquals(HttpStatus.OK.value(), response.getStatus());
//	}
//	
//	
//	
//	@Test
//	public void testConfigurationControllerUDP() throws Exception
//	{
//		UDPResponse res = new PersonService();
//		res.setResponseCode("200");
//         
//         Mockito.when(configurationListUtils.getUdpData()).thenReturn(res);
//		
//		 String inputJson ="{\"operation\": \"udp\",\"uce\": \"true\" }";
//		 RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/channel/configurationList/get")
//				.header("alightSecureSessionToken",
//						"yPAGJ0XvzYy7sJUtoBHt9BulwOHvW7kxHvkl7vv2+ufeovToLFWvAXdUExffzmcgA1gYPn+dHaHNUTT6fR5SRx5/j9+/5nO2/X2dRf47Plhx7bi8SkzEZhoUpo8wEGzNeU4O1ft2naVxfKIi4V4xjNe64bpwar8y/G3/0Lx96MF3BNTyzWXGZ78mM5hLUx6f24kLK/aYsx+vI/1wn0y8kIO/V4zhUBrRj2Ixe8W3CF4E8vaDOJZySj5Qw2PGyhUz4STfCtzStP8k2uVsKyW1HorkU4DkDVVg6YdS6IIB/wkyurisvp3MN3IBRzj2uxctkmZ67zpJjYzNNyeWtXa/INR5nLcoaO+AgfRYbFWW05FjQSrL+OBLFe6XxJRA0nPJl3ExBvCxRLbHEEFzII5qQagjje+Ykn3zdR46PTcBgm6bxWmS3jgG0Ju2cWiQfZn2/wLBcMVMJ+jVVa7o6WEJtsXCagnHjdyzwdiLD5f/uB4CsmmqqM+cWR6n9w2c8MVGm7a0A6PczM/CLtlJrPrTbOg+nNwuML5uURp19t4Ryh4JxtLkDuooXH4lpOJL1E2HB75zg1oIX3sqQ6Nj7i1Fu8LpHHqwx5NIz034oaFrjc7Q1DWaLI/HOmIIjWhqOliNb9RzpTaVRQWTKdeT8Ci4vvdYYmauW/94DYPh+eqefFxAff3/ADfW7H4Ssq2DWLoD6DJmsw+ossjUwzBrfHh1aMbZ9HL0lRXMwI40jwBbwrmBBxZiwSXUxLIM+c/8ystJN0J56A5WR+ltHiR/AdfDOwZIzOwmGmWQKa0/PYr5LUa75T0LyRNKR2WyfocBtWDyiyaoDylDOQdWkxTqvLlrY1xWpOHGcIrRbj3sMrGrVnz9MwR9l3dYNMnN4Jo/XzN2ghO12jBnO0Prlm1dxgw37Yfaqj49X7QcFmfGJGO+P3yA/jMz97cVR0q5pfkuinRdUBiCnjycO8OOlZz7ni0rTQICOqIL8Qr2oClFl1e1VPAiIozvzQrXISe6sfanVzzusjGldD+C7rzJe2DSCrOXirdFMn5JQidU9fX8XZTiyvgYBg9GgvDc+zvogUfPhHhDOi4CTd0zPsx3BAnWHmNH7fmIjkrGXwtwXrAqZ9d/auhnsMAgpeKJ62B/KfII5m139+Vgvsyx6GU8bw1li0Cl5Wys2/Oa0tssUxoE/ZVMhb8gCN3omP9MKr8YhXoprWY4cKweklbKsrnZA3o2dWMnS9izJlEWg6ku7k/L7pEb4y9kPB21A6VGSDYn9aGpV/tLORk9WgCKgrRep50U1i/zRjGml3Kpj5C/P3biaERyd8+gh3EjKuTRYd0Gb1CLb81Qj6vne8QMJau4VBO73/xTpv9xqGbYYDskrZYeS80SwcwMUolcGIEZq/oUUAFQEIX6mtn7bUaQZEkEDghh7wvTEOcnwdWD4EZ/1TkRNSugXdjvZN7XLydavYhv71UFbRfmyNcvjFE6aI348P9TlmyC+BBBjGLQ9PRYXrS7QJo58GuGUiIDu6J6uJFKUKMBpvuBcZoRgMARYgc4EYRlrMl6WjieIyibK3/d1XTAkw1YcpSBfnJ9etspOcab17Lo/75oOX5Oc2N2IteCqHk4JI1sjZu4qv01OX9xhajh/tKJ4Rbn4zxHybwl+tgSnCTL3uJ9E5KrUn886et3BOXL4kEz8sjyCDJHlJJfB+PFjnLdZgD/Li8XIdJ5EXX0I2Zzz7gVfiO4MzeQXgzvu1/o5TAEDp7PrGqHCHJCn35Vr2jlQcCLnmN+6GWyuEf3iMe39UuWCBFqoEPanB3L5jz5Y+O0Wvm4LU951nDP2a5glnDyiOB5UCOSSl2/nFXoLNvyxCKDnNGIfDdCZKShW3spOfeVKZoHoqBbG9NqBBX8izWQB5/c7aa5sjnRXT7+jU9RiNOgsJYNPa03A5BHp/HaJo80iQx2TsTMI2Nd5BZa0gQ7RT8=")
//				.contentType(MediaType.APPLICATION_JSON).content(inputJson);
//		
//		 MvcResult result = mockMvc.perform(requestBuilder).andReturn();
//		 MockHttpServletResponse response = result.getResponse();
//		 assertEquals(HttpStatus.OK.value(), response.getStatus());
//	}
//	
//	@Test
//	public void testConfigurationControllerUDPNext() throws Exception
//	{
//		com.alight.upoint.udp.beans.UDPInvocationResult invoResult=new com.alight.upoint.udp.beans.UDPInvocationResult();
//		invoResult.setStatusCode(2);
//		invoResult.setHttpStatusMessage("Udp Response returned successfully");
//		
//         
//         Mockito.when(configurationListUtils.getUdpNextPersonSchemaObject()).thenReturn(invoResult);
//		 String inputJson ="{\"operation\": \"udpNext\" }";
//		 RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/channel/configurationList/get")
//				.header("alightSecureSessionToken",
//						"yPAGJ0XvzYy7sJUtoBHt9BulwOHvW7kxHvkl7vv2+ufeovToLFWvAXdUExffzmcgA1gYPn+dHaHNUTT6fR5SRx5/j9+/5nO2/X2dRf47Plhx7bi8SkzEZhoUpo8wEGzNeU4O1ft2naVxfKIi4V4xjNe64bpwar8y/G3/0Lx96MF3BNTyzWXGZ78mM5hLUx6f24kLK/aYsx+vI/1wn0y8kIO/V4zhUBrRj2Ixe8W3CF4E8vaDOJZySj5Qw2PGyhUz4STfCtzStP8k2uVsKyW1HorkU4DkDVVg6YdS6IIB/wkyurisvp3MN3IBRzj2uxctkmZ67zpJjYzNNyeWtXa/INR5nLcoaO+AgfRYbFWW05FjQSrL+OBLFe6XxJRA0nPJl3ExBvCxRLbHEEFzII5qQagjje+Ykn3zdR46PTcBgm6bxWmS3jgG0Ju2cWiQfZn2/wLBcMVMJ+jVVa7o6WEJtsXCagnHjdyzwdiLD5f/uB4CsmmqqM+cWR6n9w2c8MVGm7a0A6PczM/CLtlJrPrTbOg+nNwuML5uURp19t4Ryh4JxtLkDuooXH4lpOJL1E2HB75zg1oIX3sqQ6Nj7i1Fu8LpHHqwx5NIz034oaFrjc7Q1DWaLI/HOmIIjWhqOliNb9RzpTaVRQWTKdeT8Ci4vvdYYmauW/94DYPh+eqefFxAff3/ADfW7H4Ssq2DWLoD6DJmsw+ossjUwzBrfHh1aMbZ9HL0lRXMwI40jwBbwrmBBxZiwSXUxLIM+c/8ystJN0J56A5WR+ltHiR/AdfDOwZIzOwmGmWQKa0/PYr5LUa75T0LyRNKR2WyfocBtWDyiyaoDylDOQdWkxTqvLlrY1xWpOHGcIrRbj3sMrGrVnz9MwR9l3dYNMnN4Jo/XzN2ghO12jBnO0Prlm1dxgw37Yfaqj49X7QcFmfGJGO+P3yA/jMz97cVR0q5pfkuinRdUBiCnjycO8OOlZz7ni0rTQICOqIL8Qr2oClFl1e1VPAiIozvzQrXISe6sfanVzzusjGldD+C7rzJe2DSCrOXirdFMn5JQidU9fX8XZTiyvgYBg9GgvDc+zvogUfPhHhDOi4CTd0zPsx3BAnWHmNH7fmIjkrGXwtwXrAqZ9d/auhnsMAgpeKJ62B/KfII5m139+Vgvsyx6GU8bw1li0Cl5Wys2/Oa0tssUxoE/ZVMhb8gCN3omP9MKr8YhXoprWY4cKweklbKsrnZA3o2dWMnS9izJlEWg6ku7k/L7pEb4y9kPB21A6VGSDYn9aGpV/tLORk9WgCKgrRep50U1i/zRjGml3Kpj5C/P3biaERyd8+gh3EjKuTRYd0Gb1CLb81Qj6vne8QMJau4VBO73/xTpv9xqGbYYDskrZYeS80SwcwMUolcGIEZq/oUUAFQEIX6mtn7bUaQZEkEDghh7wvTEOcnwdWD4EZ/1TkRNSugXdjvZN7XLydavYhv71UFbRfmyNcvjFE6aI348P9TlmyC+BBBjGLQ9PRYXrS7QJo58GuGUiIDu6J6uJFKUKMBpvuBcZoRgMARYgc4EYRlrMl6WjieIyibK3/d1XTAkw1YcpSBfnJ9etspOcab17Lo/75oOX5Oc2N2IteCqHk4JI1sjZu4qv01OX9xhajh/tKJ4Rbn4zxHybwl+tgSnCTL3uJ9E5KrUn886et3BOXL4kEz8sjyCDJHlJJfB+PFjnLdZgD/Li8XIdJ5EXX0I2Zzz7gVfiO4MzeQXgzvu1/o5TAEDp7PrGqHCHJCn35Vr2jlQcCLnmN+6GWyuEf3iMe39UuWCBFqoEPanB3L5jz5Y+O0Wvm4LU951nDP2a5glnDyiOB5UCOSSl2/nFXoLNvyxCKDnNGIfDdCZKShW3spOfeVKZoHoqBbG9NqBBX8izWQB5/c7aa5sjnRXT7+jU9RiNOgsJYNPa03A5BHp/HaJo80iQx2TsTMI2Nd5BZa0gQ7RT8=")
//				.contentType(MediaType.APPLICATION_JSON).content(inputJson);
//		
//		 MvcResult result = mockMvc.perform(requestBuilder).andReturn();
//		 MockHttpServletResponse response = result.getResponse();
//		 assertEquals(HttpStatus.OK.value(), response.getStatus());
//	}
//	
//	@Test
//	public void testConfigurationControllerUPC() throws Exception
//	{
//		
//		List<Map<Object,Object>> list = new ArrayList<Map<Object,Object>>();
//		Map<Object,Object> map1 = new HashMap<Object,Object>();
//		map1.put("assetType","string");
//		map1.put("assetValue","BLANK_ASSET_VALUE");
//		map1.put("assetKey","BLANK_ASSET_VALUE");
//		Map<Object,Object> map2 = new HashMap<Object,Object>();
//		map2.put("headerStatus",map1);
//		Map<Object,Object> map3 = new HashMap<Object,Object>();
//		map3.put("header3_4d556b09_6245_4fc7_ba76_c4f37b9d7bea",map2);
//		
//		list.add(map3);
//         
//         Mockito.when(configurationListUtils.getUpcConfig(Mockito.any(GenericRequestBean.class))).thenReturn(list);
//		 String inputJson ="{\"operation\": \"upc\" }";
//		 RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/channel/configurationList/get")
//				.header("alightSecureSessionToken",
//						"yPAGJ0XvzYy7sJUtoBHt9BulwOHvW7kxHvkl7vv2+ufeovToLFWvAXdUExffzmcgA1gYPn+dHaHNUTT6fR5SRx5/j9+/5nO2/X2dRf47Plhx7bi8SkzEZhoUpo8wEGzNeU4O1ft2naVxfKIi4V4xjNe64bpwar8y/G3/0Lx96MF3BNTyzWXGZ78mM5hLUx6f24kLK/aYsx+vI/1wn0y8kIO/V4zhUBrRj2Ixe8W3CF4E8vaDOJZySj5Qw2PGyhUz4STfCtzStP8k2uVsKyW1HorkU4DkDVVg6YdS6IIB/wkyurisvp3MN3IBRzj2uxctkmZ67zpJjYzNNyeWtXa/INR5nLcoaO+AgfRYbFWW05FjQSrL+OBLFe6XxJRA0nPJl3ExBvCxRLbHEEFzII5qQagjje+Ykn3zdR46PTcBgm6bxWmS3jgG0Ju2cWiQfZn2/wLBcMVMJ+jVVa7o6WEJtsXCagnHjdyzwdiLD5f/uB4CsmmqqM+cWR6n9w2c8MVGm7a0A6PczM/CLtlJrPrTbOg+nNwuML5uURp19t4Ryh4JxtLkDuooXH4lpOJL1E2HB75zg1oIX3sqQ6Nj7i1Fu8LpHHqwx5NIz034oaFrjc7Q1DWaLI/HOmIIjWhqOliNb9RzpTaVRQWTKdeT8Ci4vvdYYmauW/94DYPh+eqefFxAff3/ADfW7H4Ssq2DWLoD6DJmsw+ossjUwzBrfHh1aMbZ9HL0lRXMwI40jwBbwrmBBxZiwSXUxLIM+c/8ystJN0J56A5WR+ltHiR/AdfDOwZIzOwmGmWQKa0/PYr5LUa75T0LyRNKR2WyfocBtWDyiyaoDylDOQdWkxTqvLlrY1xWpOHGcIrRbj3sMrGrVnz9MwR9l3dYNMnN4Jo/XzN2ghO12jBnO0Prlm1dxgw37Yfaqj49X7QcFmfGJGO+P3yA/jMz97cVR0q5pfkuinRdUBiCnjycO8OOlZz7ni0rTQICOqIL8Qr2oClFl1e1VPAiIozvzQrXISe6sfanVzzusjGldD+C7rzJe2DSCrOXirdFMn5JQidU9fX8XZTiyvgYBg9GgvDc+zvogUfPhHhDOi4CTd0zPsx3BAnWHmNH7fmIjkrGXwtwXrAqZ9d/auhnsMAgpeKJ62B/KfII5m139+Vgvsyx6GU8bw1li0Cl5Wys2/Oa0tssUxoE/ZVMhb8gCN3omP9MKr8YhXoprWY4cKweklbKsrnZA3o2dWMnS9izJlEWg6ku7k/L7pEb4y9kPB21A6VGSDYn9aGpV/tLORk9WgCKgrRep50U1i/zRjGml3Kpj5C/P3biaERyd8+gh3EjKuTRYd0Gb1CLb81Qj6vne8QMJau4VBO73/xTpv9xqGbYYDskrZYeS80SwcwMUolcGIEZq/oUUAFQEIX6mtn7bUaQZEkEDghh7wvTEOcnwdWD4EZ/1TkRNSugXdjvZN7XLydavYhv71UFbRfmyNcvjFE6aI348P9TlmyC+BBBjGLQ9PRYXrS7QJo58GuGUiIDu6J6uJFKUKMBpvuBcZoRgMARYgc4EYRlrMl6WjieIyibK3/d1XTAkw1YcpSBfnJ9etspOcab17Lo/75oOX5Oc2N2IteCqHk4JI1sjZu4qv01OX9xhajh/tKJ4Rbn4zxHybwl+tgSnCTL3uJ9E5KrUn886et3BOXL4kEz8sjyCDJHlJJfB+PFjnLdZgD/Li8XIdJ5EXX0I2Zzz7gVfiO4MzeQXgzvu1/o5TAEDp7PrGqHCHJCn35Vr2jlQcCLnmN+6GWyuEf3iMe39UuWCBFqoEPanB3L5jz5Y+O0Wvm4LU951nDP2a5glnDyiOB5UCOSSl2/nFXoLNvyxCKDnNGIfDdCZKShW3spOfeVKZoHoqBbG9NqBBX8izWQB5/c7aa5sjnRXT7+jU9RiNOgsJYNPa03A5BHp/HaJo80iQx2TsTMI2Nd5BZa0gQ7RT8=")
//				.contentType(MediaType.APPLICATION_JSON).content(inputJson);
//		
//		 MvcResult result = mockMvc.perform(requestBuilder).andReturn();
//		 MockHttpServletResponse response = result.getResponse();
//		 assertEquals(HttpStatus.OK.value(), response.getStatus());
//	}
//	
//	@Test
//	public void testConfigurationControllerInvalidProfile() throws Exception
//	{
//		
//		
//         
//		Mockito.when(appConfig.getProfile()).thenReturn("pu");
//		 String inputJson ="{\"operation\": \"udpNext\" }";
//		 RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/channel/configurationList/get")
//				.header("alightSecureSessionToken",
//						"yPAGJ0XvzYy7sJUtoBHt9BulwOHvW7kxHvkl7vv2+ufeovToLFWvAXdUExffzmcgA1gYPn+dHaHNUTT6fR5SRx5/j9+/5nO2/X2dRf47Plhx7bi8SkzEZhoUpo8wEGzNeU4O1ft2naVxfKIi4V4xjNe64bpwar8y/G3/0Lx96MF3BNTyzWXGZ78mM5hLUx6f24kLK/aYsx+vI/1wn0y8kIO/V4zhUBrRj2Ixe8W3CF4E8vaDOJZySj5Qw2PGyhUz4STfCtzStP8k2uVsKyW1HorkU4DkDVVg6YdS6IIB/wkyurisvp3MN3IBRzj2uxctkmZ67zpJjYzNNyeWtXa/INR5nLcoaO+AgfRYbFWW05FjQSrL+OBLFe6XxJRA0nPJl3ExBvCxRLbHEEFzII5qQagjje+Ykn3zdR46PTcBgm6bxWmS3jgG0Ju2cWiQfZn2/wLBcMVMJ+jVVa7o6WEJtsXCagnHjdyzwdiLD5f/uB4CsmmqqM+cWR6n9w2c8MVGm7a0A6PczM/CLtlJrPrTbOg+nNwuML5uURp19t4Ryh4JxtLkDuooXH4lpOJL1E2HB75zg1oIX3sqQ6Nj7i1Fu8LpHHqwx5NIz034oaFrjc7Q1DWaLI/HOmIIjWhqOliNb9RzpTaVRQWTKdeT8Ci4vvdYYmauW/94DYPh+eqefFxAff3/ADfW7H4Ssq2DWLoD6DJmsw+ossjUwzBrfHh1aMbZ9HL0lRXMwI40jwBbwrmBBxZiwSXUxLIM+c/8ystJN0J56A5WR+ltHiR/AdfDOwZIzOwmGmWQKa0/PYr5LUa75T0LyRNKR2WyfocBtWDyiyaoDylDOQdWkxTqvLlrY1xWpOHGcIrRbj3sMrGrVnz9MwR9l3dYNMnN4Jo/XzN2ghO12jBnO0Prlm1dxgw37Yfaqj49X7QcFmfGJGO+P3yA/jMz97cVR0q5pfkuinRdUBiCnjycO8OOlZz7ni0rTQICOqIL8Qr2oClFl1e1VPAiIozvzQrXISe6sfanVzzusjGldD+C7rzJe2DSCrOXirdFMn5JQidU9fX8XZTiyvgYBg9GgvDc+zvogUfPhHhDOi4CTd0zPsx3BAnWHmNH7fmIjkrGXwtwXrAqZ9d/auhnsMAgpeKJ62B/KfII5m139+Vgvsyx6GU8bw1li0Cl5Wys2/Oa0tssUxoE/ZVMhb8gCN3omP9MKr8YhXoprWY4cKweklbKsrnZA3o2dWMnS9izJlEWg6ku7k/L7pEb4y9kPB21A6VGSDYn9aGpV/tLORk9WgCKgrRep50U1i/zRjGml3Kpj5C/P3biaERyd8+gh3EjKuTRYd0Gb1CLb81Qj6vne8QMJau4VBO73/xTpv9xqGbYYDskrZYeS80SwcwMUolcGIEZq/oUUAFQEIX6mtn7bUaQZEkEDghh7wvTEOcnwdWD4EZ/1TkRNSugXdjvZN7XLydavYhv71UFbRfmyNcvjFE6aI348P9TlmyC+BBBjGLQ9PRYXrS7QJo58GuGUiIDu6J6uJFKUKMBpvuBcZoRgMARYgc4EYRlrMl6WjieIyibK3/d1XTAkw1YcpSBfnJ9etspOcab17Lo/75oOX5Oc2N2IteCqHk4JI1sjZu4qv01OX9xhajh/tKJ4Rbn4zxHybwl+tgSnCTL3uJ9E5KrUn886et3BOXL4kEz8sjyCDJHlJJfB+PFjnLdZgD/Li8XIdJ5EXX0I2Zzz7gVfiO4MzeQXgzvu1/o5TAEDp7PrGqHCHJCn35Vr2jlQcCLnmN+6GWyuEf3iMe39UuWCBFqoEPanB3L5jz5Y+O0Wvm4LU951nDP2a5glnDyiOB5UCOSSl2/nFXoLNvyxCKDnNGIfDdCZKShW3spOfeVKZoHoqBbG9NqBBX8izWQB5/c7aa5sjnRXT7+jU9RiNOgsJYNPa03A5BHp/HaJo80iQx2TsTMI2Nd5BZa0gQ7RT8=")
//				.contentType(MediaType.APPLICATION_JSON).content(inputJson);
//		
//		 MvcResult result = mockMvc.perform(requestBuilder).andReturn();
//		 MockHttpServletResponse response = result.getResponse();
//		 assertEquals(HttpStatus.OK.value(), response.getStatus());
//	}
//	
//	@Test
//	public void testConfigurationControllerClearHro() throws Exception
//	{
//		
//		
//		 String inputJson ="{\"operation\": \"clearHro\" }";
//		 RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/channel/configurationList/get")
//				.header("alightSecureSessionToken",
//						"yPAGJ0XvzYy7sJUtoBHt9BulwOHvW7kxHvkl7vv2+ufeovToLFWvAXdUExffzmcgA1gYPn+dHaHNUTT6fR5SRx5/j9+/5nO2/X2dRf47Plhx7bi8SkzEZhoUpo8wEGzNeU4O1ft2naVxfKIi4V4xjNe64bpwar8y/G3/0Lx96MF3BNTyzWXGZ78mM5hLUx6f24kLK/aYsx+vI/1wn0y8kIO/V4zhUBrRj2Ixe8W3CF4E8vaDOJZySj5Qw2PGyhUz4STfCtzStP8k2uVsKyW1HorkU4DkDVVg6YdS6IIB/wkyurisvp3MN3IBRzj2uxctkmZ67zpJjYzNNyeWtXa/INR5nLcoaO+AgfRYbFWW05FjQSrL+OBLFe6XxJRA0nPJl3ExBvCxRLbHEEFzII5qQagjje+Ykn3zdR46PTcBgm6bxWmS3jgG0Ju2cWiQfZn2/wLBcMVMJ+jVVa7o6WEJtsXCagnHjdyzwdiLD5f/uB4CsmmqqM+cWR6n9w2c8MVGm7a0A6PczM/CLtlJrPrTbOg+nNwuML5uURp19t4Ryh4JxtLkDuooXH4lpOJL1E2HB75zg1oIX3sqQ6Nj7i1Fu8LpHHqwx5NIz034oaFrjc7Q1DWaLI/HOmIIjWhqOliNb9RzpTaVRQWTKdeT8Ci4vvdYYmauW/94DYPh+eqefFxAff3/ADfW7H4Ssq2DWLoD6DJmsw+ossjUwzBrfHh1aMbZ9HL0lRXMwI40jwBbwrmBBxZiwSXUxLIM+c/8ystJN0J56A5WR+ltHiR/AdfDOwZIzOwmGmWQKa0/PYr5LUa75T0LyRNKR2WyfocBtWDyiyaoDylDOQdWkxTqvLlrY1xWpOHGcIrRbj3sMrGrVnz9MwR9l3dYNMnN4Jo/XzN2ghO12jBnO0Prlm1dxgw37Yfaqj49X7QcFmfGJGO+P3yA/jMz97cVR0q5pfkuinRdUBiCnjycO8OOlZz7ni0rTQICOqIL8Qr2oClFl1e1VPAiIozvzQrXISe6sfanVzzusjGldD+C7rzJe2DSCrOXirdFMn5JQidU9fX8XZTiyvgYBg9GgvDc+zvogUfPhHhDOi4CTd0zPsx3BAnWHmNH7fmIjkrGXwtwXrAqZ9d/auhnsMAgpeKJ62B/KfII5m139+Vgvsyx6GU8bw1li0Cl5Wys2/Oa0tssUxoE/ZVMhb8gCN3omP9MKr8YhXoprWY4cKweklbKsrnZA3o2dWMnS9izJlEWg6ku7k/L7pEb4y9kPB21A6VGSDYn9aGpV/tLORk9WgCKgrRep50U1i/zRjGml3Kpj5C/P3biaERyd8+gh3EjKuTRYd0Gb1CLb81Qj6vne8QMJau4VBO73/xTpv9xqGbYYDskrZYeS80SwcwMUolcGIEZq/oUUAFQEIX6mtn7bUaQZEkEDghh7wvTEOcnwdWD4EZ/1TkRNSugXdjvZN7XLydavYhv71UFbRfmyNcvjFE6aI348P9TlmyC+BBBjGLQ9PRYXrS7QJo58GuGUiIDu6J6uJFKUKMBpvuBcZoRgMARYgc4EYRlrMl6WjieIyibK3/d1XTAkw1YcpSBfnJ9etspOcab17Lo/75oOX5Oc2N2IteCqHk4JI1sjZu4qv01OX9xhajh/tKJ4Rbn4zxHybwl+tgSnCTL3uJ9E5KrUn886et3BOXL4kEz8sjyCDJHlJJfB+PFjnLdZgD/Li8XIdJ5EXX0I2Zzz7gVfiO4MzeQXgzvu1/o5TAEDp7PrGqHCHJCn35Vr2jlQcCLnmN+6GWyuEf3iMe39UuWCBFqoEPanB3L5jz5Y+O0Wvm4LU951nDP2a5glnDyiOB5UCOSSl2/nFXoLNvyxCKDnNGIfDdCZKShW3spOfeVKZoHoqBbG9NqBBX8izWQB5/c7aa5sjnRXT7+jU9RiNOgsJYNPa03A5BHp/HaJo80iQx2TsTMI2Nd5BZa0gQ7RT8=")
//				.contentType(MediaType.APPLICATION_JSON).content(inputJson);
//		
//		 MvcResult result = mockMvc.perform(requestBuilder).andReturn();
//		 MockHttpServletResponse response = result.getResponse();
//		 assertEquals(HttpStatus.OK.value(), response.getStatus());
//	}
//	
//	@Test
//	public void testConfigurationControllerClearHroAll() throws Exception
//	{
//		
//		
//		 String inputJson ="{\"operation\": \"clearHroAll\" }";
//		 RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/channel/configurationList/get")
//				.header("alightSecureSessionToken",
//						"yPAGJ0XvzYy7sJUtoBHt9BulwOHvW7kxHvkl7vv2+ufeovToLFWvAXdUExffzmcgA1gYPn+dHaHNUTT6fR5SRx5/j9+/5nO2/X2dRf47Plhx7bi8SkzEZhoUpo8wEGzNeU4O1ft2naVxfKIi4V4xjNe64bpwar8y/G3/0Lx96MF3BNTyzWXGZ78mM5hLUx6f24kLK/aYsx+vI/1wn0y8kIO/V4zhUBrRj2Ixe8W3CF4E8vaDOJZySj5Qw2PGyhUz4STfCtzStP8k2uVsKyW1HorkU4DkDVVg6YdS6IIB/wkyurisvp3MN3IBRzj2uxctkmZ67zpJjYzNNyeWtXa/INR5nLcoaO+AgfRYbFWW05FjQSrL+OBLFe6XxJRA0nPJl3ExBvCxRLbHEEFzII5qQagjje+Ykn3zdR46PTcBgm6bxWmS3jgG0Ju2cWiQfZn2/wLBcMVMJ+jVVa7o6WEJtsXCagnHjdyzwdiLD5f/uB4CsmmqqM+cWR6n9w2c8MVGm7a0A6PczM/CLtlJrPrTbOg+nNwuML5uURp19t4Ryh4JxtLkDuooXH4lpOJL1E2HB75zg1oIX3sqQ6Nj7i1Fu8LpHHqwx5NIz034oaFrjc7Q1DWaLI/HOmIIjWhqOliNb9RzpTaVRQWTKdeT8Ci4vvdYYmauW/94DYPh+eqefFxAff3/ADfW7H4Ssq2DWLoD6DJmsw+ossjUwzBrfHh1aMbZ9HL0lRXMwI40jwBbwrmBBxZiwSXUxLIM+c/8ystJN0J56A5WR+ltHiR/AdfDOwZIzOwmGmWQKa0/PYr5LUa75T0LyRNKR2WyfocBtWDyiyaoDylDOQdWkxTqvLlrY1xWpOHGcIrRbj3sMrGrVnz9MwR9l3dYNMnN4Jo/XzN2ghO12jBnO0Prlm1dxgw37Yfaqj49X7QcFmfGJGO+P3yA/jMz97cVR0q5pfkuinRdUBiCnjycO8OOlZz7ni0rTQICOqIL8Qr2oClFl1e1VPAiIozvzQrXISe6sfanVzzusjGldD+C7rzJe2DSCrOXirdFMn5JQidU9fX8XZTiyvgYBg9GgvDc+zvogUfPhHhDOi4CTd0zPsx3BAnWHmNH7fmIjkrGXwtwXrAqZ9d/auhnsMAgpeKJ62B/KfII5m139+Vgvsyx6GU8bw1li0Cl5Wys2/Oa0tssUxoE/ZVMhb8gCN3omP9MKr8YhXoprWY4cKweklbKsrnZA3o2dWMnS9izJlEWg6ku7k/L7pEb4y9kPB21A6VGSDYn9aGpV/tLORk9WgCKgrRep50U1i/zRjGml3Kpj5C/P3biaERyd8+gh3EjKuTRYd0Gb1CLb81Qj6vne8QMJau4VBO73/xTpv9xqGbYYDskrZYeS80SwcwMUolcGIEZq/oUUAFQEIX6mtn7bUaQZEkEDghh7wvTEOcnwdWD4EZ/1TkRNSugXdjvZN7XLydavYhv71UFbRfmyNcvjFE6aI348P9TlmyC+BBBjGLQ9PRYXrS7QJo58GuGUiIDu6J6uJFKUKMBpvuBcZoRgMARYgc4EYRlrMl6WjieIyibK3/d1XTAkw1YcpSBfnJ9etspOcab17Lo/75oOX5Oc2N2IteCqHk4JI1sjZu4qv01OX9xhajh/tKJ4Rbn4zxHybwl+tgSnCTL3uJ9E5KrUn886et3BOXL4kEz8sjyCDJHlJJfB+PFjnLdZgD/Li8XIdJ5EXX0I2Zzz7gVfiO4MzeQXgzvu1/o5TAEDp7PrGqHCHJCn35Vr2jlQcCLnmN+6GWyuEf3iMe39UuWCBFqoEPanB3L5jz5Y+O0Wvm4LU951nDP2a5glnDyiOB5UCOSSl2/nFXoLNvyxCKDnNGIfDdCZKShW3spOfeVKZoHoqBbG9NqBBX8izWQB5/c7aa5sjnRXT7+jU9RiNOgsJYNPa03A5BHp/HaJo80iQx2TsTMI2Nd5BZa0gQ7RT8=")
//				.contentType(MediaType.APPLICATION_JSON).content(inputJson);
//		
//		 MvcResult result = mockMvc.perform(requestBuilder).andReturn();
//		 MockHttpServletResponse response = result.getResponse();
//		 assertEquals(HttpStatus.OK.value(), response.getStatus());
//	}
//	
//	@Test
//	public void testConfigurationControllerclearAFLinksCache() throws Exception
//	{
//		
//		
//		 String inputJson ="{\"operation\": \"clearAFLinksCache\" }";
//		 RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/channel/configurationList/get")
//				.header("alightSecureSessionToken",
//						"yPAGJ0XvzYy7sJUtoBHt9BulwOHvW7kxHvkl7vv2+ufeovToLFWvAXdUExffzmcgA1gYPn+dHaHNUTT6fR5SRx5/j9+/5nO2/X2dRf47Plhx7bi8SkzEZhoUpo8wEGzNeU4O1ft2naVxfKIi4V4xjNe64bpwar8y/G3/0Lx96MF3BNTyzWXGZ78mM5hLUx6f24kLK/aYsx+vI/1wn0y8kIO/V4zhUBrRj2Ixe8W3CF4E8vaDOJZySj5Qw2PGyhUz4STfCtzStP8k2uVsKyW1HorkU4DkDVVg6YdS6IIB/wkyurisvp3MN3IBRzj2uxctkmZ67zpJjYzNNyeWtXa/INR5nLcoaO+AgfRYbFWW05FjQSrL+OBLFe6XxJRA0nPJl3ExBvCxRLbHEEFzII5qQagjje+Ykn3zdR46PTcBgm6bxWmS3jgG0Ju2cWiQfZn2/wLBcMVMJ+jVVa7o6WEJtsXCagnHjdyzwdiLD5f/uB4CsmmqqM+cWR6n9w2c8MVGm7a0A6PczM/CLtlJrPrTbOg+nNwuML5uURp19t4Ryh4JxtLkDuooXH4lpOJL1E2HB75zg1oIX3sqQ6Nj7i1Fu8LpHHqwx5NIz034oaFrjc7Q1DWaLI/HOmIIjWhqOliNb9RzpTaVRQWTKdeT8Ci4vvdYYmauW/94DYPh+eqefFxAff3/ADfW7H4Ssq2DWLoD6DJmsw+ossjUwzBrfHh1aMbZ9HL0lRXMwI40jwBbwrmBBxZiwSXUxLIM+c/8ystJN0J56A5WR+ltHiR/AdfDOwZIzOwmGmWQKa0/PYr5LUa75T0LyRNKR2WyfocBtWDyiyaoDylDOQdWkxTqvLlrY1xWpOHGcIrRbj3sMrGrVnz9MwR9l3dYNMnN4Jo/XzN2ghO12jBnO0Prlm1dxgw37Yfaqj49X7QcFmfGJGO+P3yA/jMz97cVR0q5pfkuinRdUBiCnjycO8OOlZz7ni0rTQICOqIL8Qr2oClFl1e1VPAiIozvzQrXISe6sfanVzzusjGldD+C7rzJe2DSCrOXirdFMn5JQidU9fX8XZTiyvgYBg9GgvDc+zvogUfPhHhDOi4CTd0zPsx3BAnWHmNH7fmIjkrGXwtwXrAqZ9d/auhnsMAgpeKJ62B/KfII5m139+Vgvsyx6GU8bw1li0Cl5Wys2/Oa0tssUxoE/ZVMhb8gCN3omP9MKr8YhXoprWY4cKweklbKsrnZA3o2dWMnS9izJlEWg6ku7k/L7pEb4y9kPB21A6VGSDYn9aGpV/tLORk9WgCKgrRep50U1i/zRjGml3Kpj5C/P3biaERyd8+gh3EjKuTRYd0Gb1CLb81Qj6vne8QMJau4VBO73/xTpv9xqGbYYDskrZYeS80SwcwMUolcGIEZq/oUUAFQEIX6mtn7bUaQZEkEDghh7wvTEOcnwdWD4EZ/1TkRNSugXdjvZN7XLydavYhv71UFbRfmyNcvjFE6aI348P9TlmyC+BBBjGLQ9PRYXrS7QJo58GuGUiIDu6J6uJFKUKMBpvuBcZoRgMARYgc4EYRlrMl6WjieIyibK3/d1XTAkw1YcpSBfnJ9etspOcab17Lo/75oOX5Oc2N2IteCqHk4JI1sjZu4qv01OX9xhajh/tKJ4Rbn4zxHybwl+tgSnCTL3uJ9E5KrUn886et3BOXL4kEz8sjyCDJHlJJfB+PFjnLdZgD/Li8XIdJ5EXX0I2Zzz7gVfiO4MzeQXgzvu1/o5TAEDp7PrGqHCHJCn35Vr2jlQcCLnmN+6GWyuEf3iMe39UuWCBFqoEPanB3L5jz5Y+O0Wvm4LU951nDP2a5glnDyiOB5UCOSSl2/nFXoLNvyxCKDnNGIfDdCZKShW3spOfeVKZoHoqBbG9NqBBX8izWQB5/c7aa5sjnRXT7+jU9RiNOgsJYNPa03A5BHp/HaJo80iQx2TsTMI2Nd5BZa0gQ7RT8=")
//				.contentType(MediaType.APPLICATION_JSON).content(inputJson);
//		
//		 MvcResult result = mockMvc.perform(requestBuilder).andReturn();
//		 MockHttpServletResponse response = result.getResponse();
//		 assertEquals(HttpStatus.OK.value(), response.getStatus());
//	}
//	
//	@Test
//	public void testConfigurationControllerclearAFLinksCacheAll() throws Exception
//	{
//		
//		
//		 String inputJson ="{\"operation\": \"clearAFLinksCacheAll\" }";
//		 RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/channel/configurationList/get")
//				.header("alightSecureSessionToken",
//						"yPAGJ0XvzYy7sJUtoBHt9BulwOHvW7kxHvkl7vv2+ufeovToLFWvAXdUExffzmcgA1gYPn+dHaHNUTT6fR5SRx5/j9+/5nO2/X2dRf47Plhx7bi8SkzEZhoUpo8wEGzNeU4O1ft2naVxfKIi4V4xjNe64bpwar8y/G3/0Lx96MF3BNTyzWXGZ78mM5hLUx6f24kLK/aYsx+vI/1wn0y8kIO/V4zhUBrRj2Ixe8W3CF4E8vaDOJZySj5Qw2PGyhUz4STfCtzStP8k2uVsKyW1HorkU4DkDVVg6YdS6IIB/wkyurisvp3MN3IBRzj2uxctkmZ67zpJjYzNNyeWtXa/INR5nLcoaO+AgfRYbFWW05FjQSrL+OBLFe6XxJRA0nPJl3ExBvCxRLbHEEFzII5qQagjje+Ykn3zdR46PTcBgm6bxWmS3jgG0Ju2cWiQfZn2/wLBcMVMJ+jVVa7o6WEJtsXCagnHjdyzwdiLD5f/uB4CsmmqqM+cWR6n9w2c8MVGm7a0A6PczM/CLtlJrPrTbOg+nNwuML5uURp19t4Ryh4JxtLkDuooXH4lpOJL1E2HB75zg1oIX3sqQ6Nj7i1Fu8LpHHqwx5NIz034oaFrjc7Q1DWaLI/HOmIIjWhqOliNb9RzpTaVRQWTKdeT8Ci4vvdYYmauW/94DYPh+eqefFxAff3/ADfW7H4Ssq2DWLoD6DJmsw+ossjUwzBrfHh1aMbZ9HL0lRXMwI40jwBbwrmBBxZiwSXUxLIM+c/8ystJN0J56A5WR+ltHiR/AdfDOwZIzOwmGmWQKa0/PYr5LUa75T0LyRNKR2WyfocBtWDyiyaoDylDOQdWkxTqvLlrY1xWpOHGcIrRbj3sMrGrVnz9MwR9l3dYNMnN4Jo/XzN2ghO12jBnO0Prlm1dxgw37Yfaqj49X7QcFmfGJGO+P3yA/jMz97cVR0q5pfkuinRdUBiCnjycO8OOlZz7ni0rTQICOqIL8Qr2oClFl1e1VPAiIozvzQrXISe6sfanVzzusjGldD+C7rzJe2DSCrOXirdFMn5JQidU9fX8XZTiyvgYBg9GgvDc+zvogUfPhHhDOi4CTd0zPsx3BAnWHmNH7fmIjkrGXwtwXrAqZ9d/auhnsMAgpeKJ62B/KfII5m139+Vgvsyx6GU8bw1li0Cl5Wys2/Oa0tssUxoE/ZVMhb8gCN3omP9MKr8YhXoprWY4cKweklbKsrnZA3o2dWMnS9izJlEWg6ku7k/L7pEb4y9kPB21A6VGSDYn9aGpV/tLORk9WgCKgrRep50U1i/zRjGml3Kpj5C/P3biaERyd8+gh3EjKuTRYd0Gb1CLb81Qj6vne8QMJau4VBO73/xTpv9xqGbYYDskrZYeS80SwcwMUolcGIEZq/oUUAFQEIX6mtn7bUaQZEkEDghh7wvTEOcnwdWD4EZ/1TkRNSugXdjvZN7XLydavYhv71UFbRfmyNcvjFE6aI348P9TlmyC+BBBjGLQ9PRYXrS7QJo58GuGUiIDu6J6uJFKUKMBpvuBcZoRgMARYgc4EYRlrMl6WjieIyibK3/d1XTAkw1YcpSBfnJ9etspOcab17Lo/75oOX5Oc2N2IteCqHk4JI1sjZu4qv01OX9xhajh/tKJ4Rbn4zxHybwl+tgSnCTL3uJ9E5KrUn886et3BOXL4kEz8sjyCDJHlJJfB+PFjnLdZgD/Li8XIdJ5EXX0I2Zzz7gVfiO4MzeQXgzvu1/o5TAEDp7PrGqHCHJCn35Vr2jlQcCLnmN+6GWyuEf3iMe39UuWCBFqoEPanB3L5jz5Y+O0Wvm4LU951nDP2a5glnDyiOB5UCOSSl2/nFXoLNvyxCKDnNGIfDdCZKShW3spOfeVKZoHoqBbG9NqBBX8izWQB5/c7aa5sjnRXT7+jU9RiNOgsJYNPa03A5BHp/HaJo80iQx2TsTMI2Nd5BZa0gQ7RT8=")
//				.contentType(MediaType.APPLICATION_JSON).content(inputJson);
//		
//		 MvcResult result = mockMvc.perform(requestBuilder).andReturn();
//		 MockHttpServletResponse response = result.getResponse();
//		 assertEquals(HttpStatus.OK.value(), response.getStatus());
//	}
//	
//	@Test
//	public void testConfigurationControllerclearLinksCache() throws Exception
//	{
//		
//		
//		 String inputJson ="{\"operation\": \"clearLinksCache\" }";
//		 RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/channel/configurationList/get")
//				.header("alightSecureSessionToken",
//						"yPAGJ0XvzYy7sJUtoBHt9BulwOHvW7kxHvkl7vv2+ufeovToLFWvAXdUExffzmcgA1gYPn+dHaHNUTT6fR5SRx5/j9+/5nO2/X2dRf47Plhx7bi8SkzEZhoUpo8wEGzNeU4O1ft2naVxfKIi4V4xjNe64bpwar8y/G3/0Lx96MF3BNTyzWXGZ78mM5hLUx6f24kLK/aYsx+vI/1wn0y8kIO/V4zhUBrRj2Ixe8W3CF4E8vaDOJZySj5Qw2PGyhUz4STfCtzStP8k2uVsKyW1HorkU4DkDVVg6YdS6IIB/wkyurisvp3MN3IBRzj2uxctkmZ67zpJjYzNNyeWtXa/INR5nLcoaO+AgfRYbFWW05FjQSrL+OBLFe6XxJRA0nPJl3ExBvCxRLbHEEFzII5qQagjje+Ykn3zdR46PTcBgm6bxWmS3jgG0Ju2cWiQfZn2/wLBcMVMJ+jVVa7o6WEJtsXCagnHjdyzwdiLD5f/uB4CsmmqqM+cWR6n9w2c8MVGm7a0A6PczM/CLtlJrPrTbOg+nNwuML5uURp19t4Ryh4JxtLkDuooXH4lpOJL1E2HB75zg1oIX3sqQ6Nj7i1Fu8LpHHqwx5NIz034oaFrjc7Q1DWaLI/HOmIIjWhqOliNb9RzpTaVRQWTKdeT8Ci4vvdYYmauW/94DYPh+eqefFxAff3/ADfW7H4Ssq2DWLoD6DJmsw+ossjUwzBrfHh1aMbZ9HL0lRXMwI40jwBbwrmBBxZiwSXUxLIM+c/8ystJN0J56A5WR+ltHiR/AdfDOwZIzOwmGmWQKa0/PYr5LUa75T0LyRNKR2WyfocBtWDyiyaoDylDOQdWkxTqvLlrY1xWpOHGcIrRbj3sMrGrVnz9MwR9l3dYNMnN4Jo/XzN2ghO12jBnO0Prlm1dxgw37Yfaqj49X7QcFmfGJGO+P3yA/jMz97cVR0q5pfkuinRdUBiCnjycO8OOlZz7ni0rTQICOqIL8Qr2oClFl1e1VPAiIozvzQrXISe6sfanVzzusjGldD+C7rzJe2DSCrOXirdFMn5JQidU9fX8XZTiyvgYBg9GgvDc+zvogUfPhHhDOi4CTd0zPsx3BAnWHmNH7fmIjkrGXwtwXrAqZ9d/auhnsMAgpeKJ62B/KfII5m139+Vgvsyx6GU8bw1li0Cl5Wys2/Oa0tssUxoE/ZVMhb8gCN3omP9MKr8YhXoprWY4cKweklbKsrnZA3o2dWMnS9izJlEWg6ku7k/L7pEb4y9kPB21A6VGSDYn9aGpV/tLORk9WgCKgrRep50U1i/zRjGml3Kpj5C/P3biaERyd8+gh3EjKuTRYd0Gb1CLb81Qj6vne8QMJau4VBO73/xTpv9xqGbYYDskrZYeS80SwcwMUolcGIEZq/oUUAFQEIX6mtn7bUaQZEkEDghh7wvTEOcnwdWD4EZ/1TkRNSugXdjvZN7XLydavYhv71UFbRfmyNcvjFE6aI348P9TlmyC+BBBjGLQ9PRYXrS7QJo58GuGUiIDu6J6uJFKUKMBpvuBcZoRgMARYgc4EYRlrMl6WjieIyibK3/d1XTAkw1YcpSBfnJ9etspOcab17Lo/75oOX5Oc2N2IteCqHk4JI1sjZu4qv01OX9xhajh/tKJ4Rbn4zxHybwl+tgSnCTL3uJ9E5KrUn886et3BOXL4kEz8sjyCDJHlJJfB+PFjnLdZgD/Li8XIdJ5EXX0I2Zzz7gVfiO4MzeQXgzvu1/o5TAEDp7PrGqHCHJCn35Vr2jlQcCLnmN+6GWyuEf3iMe39UuWCBFqoEPanB3L5jz5Y+O0Wvm4LU951nDP2a5glnDyiOB5UCOSSl2/nFXoLNvyxCKDnNGIfDdCZKShW3spOfeVKZoHoqBbG9NqBBX8izWQB5/c7aa5sjnRXT7+jU9RiNOgsJYNPa03A5BHp/HaJo80iQx2TsTMI2Nd5BZa0gQ7RT8=")
//				.contentType(MediaType.APPLICATION_JSON).content(inputJson);
//		
//		 MvcResult result = mockMvc.perform(requestBuilder).andReturn();
//		 MockHttpServletResponse response = result.getResponse();
//		 assertEquals(HttpStatus.OK.value(), response.getStatus());
//	}
//	
//	@Test
//	public void testConfigurationControllerclearLinksCacheAll() throws Exception
//	{
//		
//		
//		 String inputJson ="{\"operation\": \"clearLinksCacheAll\" }";
//		 RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/channel/configurationList/get")
//				.header("alightSecureSessionToken",
//						"yPAGJ0XvzYy7sJUtoBHt9BulwOHvW7kxHvkl7vv2+ufeovToLFWvAXdUExffzmcgA1gYPn+dHaHNUTT6fR5SRx5/j9+/5nO2/X2dRf47Plhx7bi8SkzEZhoUpo8wEGzNeU4O1ft2naVxfKIi4V4xjNe64bpwar8y/G3/0Lx96MF3BNTyzWXGZ78mM5hLUx6f24kLK/aYsx+vI/1wn0y8kIO/V4zhUBrRj2Ixe8W3CF4E8vaDOJZySj5Qw2PGyhUz4STfCtzStP8k2uVsKyW1HorkU4DkDVVg6YdS6IIB/wkyurisvp3MN3IBRzj2uxctkmZ67zpJjYzNNyeWtXa/INR5nLcoaO+AgfRYbFWW05FjQSrL+OBLFe6XxJRA0nPJl3ExBvCxRLbHEEFzII5qQagjje+Ykn3zdR46PTcBgm6bxWmS3jgG0Ju2cWiQfZn2/wLBcMVMJ+jVVa7o6WEJtsXCagnHjdyzwdiLD5f/uB4CsmmqqM+cWR6n9w2c8MVGm7a0A6PczM/CLtlJrPrTbOg+nNwuML5uURp19t4Ryh4JxtLkDuooXH4lpOJL1E2HB75zg1oIX3sqQ6Nj7i1Fu8LpHHqwx5NIz034oaFrjc7Q1DWaLI/HOmIIjWhqOliNb9RzpTaVRQWTKdeT8Ci4vvdYYmauW/94DYPh+eqefFxAff3/ADfW7H4Ssq2DWLoD6DJmsw+ossjUwzBrfHh1aMbZ9HL0lRXMwI40jwBbwrmBBxZiwSXUxLIM+c/8ystJN0J56A5WR+ltHiR/AdfDOwZIzOwmGmWQKa0/PYr5LUa75T0LyRNKR2WyfocBtWDyiyaoDylDOQdWkxTqvLlrY1xWpOHGcIrRbj3sMrGrVnz9MwR9l3dYNMnN4Jo/XzN2ghO12jBnO0Prlm1dxgw37Yfaqj49X7QcFmfGJGO+P3yA/jMz97cVR0q5pfkuinRdUBiCnjycO8OOlZz7ni0rTQICOqIL8Qr2oClFl1e1VPAiIozvzQrXISe6sfanVzzusjGldD+C7rzJe2DSCrOXirdFMn5JQidU9fX8XZTiyvgYBg9GgvDc+zvogUfPhHhDOi4CTd0zPsx3BAnWHmNH7fmIjkrGXwtwXrAqZ9d/auhnsMAgpeKJ62B/KfII5m139+Vgvsyx6GU8bw1li0Cl5Wys2/Oa0tssUxoE/ZVMhb8gCN3omP9MKr8YhXoprWY4cKweklbKsrnZA3o2dWMnS9izJlEWg6ku7k/L7pEb4y9kPB21A6VGSDYn9aGpV/tLORk9WgCKgrRep50U1i/zRjGml3Kpj5C/P3biaERyd8+gh3EjKuTRYd0Gb1CLb81Qj6vne8QMJau4VBO73/xTpv9xqGbYYDskrZYeS80SwcwMUolcGIEZq/oUUAFQEIX6mtn7bUaQZEkEDghh7wvTEOcnwdWD4EZ/1TkRNSugXdjvZN7XLydavYhv71UFbRfmyNcvjFE6aI348P9TlmyC+BBBjGLQ9PRYXrS7QJo58GuGUiIDu6J6uJFKUKMBpvuBcZoRgMARYgc4EYRlrMl6WjieIyibK3/d1XTAkw1YcpSBfnJ9etspOcab17Lo/75oOX5Oc2N2IteCqHk4JI1sjZu4qv01OX9xhajh/tKJ4Rbn4zxHybwl+tgSnCTL3uJ9E5KrUn886et3BOXL4kEz8sjyCDJHlJJfB+PFjnLdZgD/Li8XIdJ5EXX0I2Zzz7gVfiO4MzeQXgzvu1/o5TAEDp7PrGqHCHJCn35Vr2jlQcCLnmN+6GWyuEf3iMe39UuWCBFqoEPanB3L5jz5Y+O0Wvm4LU951nDP2a5glnDyiOB5UCOSSl2/nFXoLNvyxCKDnNGIfDdCZKShW3spOfeVKZoHoqBbG9NqBBX8izWQB5/c7aa5sjnRXT7+jU9RiNOgsJYNPa03A5BHp/HaJo80iQx2TsTMI2Nd5BZa0gQ7RT8=")
//				.contentType(MediaType.APPLICATION_JSON).content(inputJson);
//		
//		 MvcResult result = mockMvc.perform(requestBuilder).andReturn();
//		 MockHttpServletResponse response = result.getResponse();
//		 assertEquals(HttpStatus.OK.value(), response.getStatus());
//	}
//	
//	@Test
//	public void testConfigurationControllerclearExpressionsCache() throws Exception
//	{
//		
//		
//		 String inputJson ="{\"operation\": \"clearExpressionsCache\" }";
//		 RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/channel/configurationList/get")
//				.header("alightSecureSessionToken",
//						"yPAGJ0XvzYy7sJUtoBHt9BulwOHvW7kxHvkl7vv2+ufeovToLFWvAXdUExffzmcgA1gYPn+dHaHNUTT6fR5SRx5/j9+/5nO2/X2dRf47Plhx7bi8SkzEZhoUpo8wEGzNeU4O1ft2naVxfKIi4V4xjNe64bpwar8y/G3/0Lx96MF3BNTyzWXGZ78mM5hLUx6f24kLK/aYsx+vI/1wn0y8kIO/V4zhUBrRj2Ixe8W3CF4E8vaDOJZySj5Qw2PGyhUz4STfCtzStP8k2uVsKyW1HorkU4DkDVVg6YdS6IIB/wkyurisvp3MN3IBRzj2uxctkmZ67zpJjYzNNyeWtXa/INR5nLcoaO+AgfRYbFWW05FjQSrL+OBLFe6XxJRA0nPJl3ExBvCxRLbHEEFzII5qQagjje+Ykn3zdR46PTcBgm6bxWmS3jgG0Ju2cWiQfZn2/wLBcMVMJ+jVVa7o6WEJtsXCagnHjdyzwdiLD5f/uB4CsmmqqM+cWR6n9w2c8MVGm7a0A6PczM/CLtlJrPrTbOg+nNwuML5uURp19t4Ryh4JxtLkDuooXH4lpOJL1E2HB75zg1oIX3sqQ6Nj7i1Fu8LpHHqwx5NIz034oaFrjc7Q1DWaLI/HOmIIjWhqOliNb9RzpTaVRQWTKdeT8Ci4vvdYYmauW/94DYPh+eqefFxAff3/ADfW7H4Ssq2DWLoD6DJmsw+ossjUwzBrfHh1aMbZ9HL0lRXMwI40jwBbwrmBBxZiwSXUxLIM+c/8ystJN0J56A5WR+ltHiR/AdfDOwZIzOwmGmWQKa0/PYr5LUa75T0LyRNKR2WyfocBtWDyiyaoDylDOQdWkxTqvLlrY1xWpOHGcIrRbj3sMrGrVnz9MwR9l3dYNMnN4Jo/XzN2ghO12jBnO0Prlm1dxgw37Yfaqj49X7QcFmfGJGO+P3yA/jMz97cVR0q5pfkuinRdUBiCnjycO8OOlZz7ni0rTQICOqIL8Qr2oClFl1e1VPAiIozvzQrXISe6sfanVzzusjGldD+C7rzJe2DSCrOXirdFMn5JQidU9fX8XZTiyvgYBg9GgvDc+zvogUfPhHhDOi4CTd0zPsx3BAnWHmNH7fmIjkrGXwtwXrAqZ9d/auhnsMAgpeKJ62B/KfII5m139+Vgvsyx6GU8bw1li0Cl5Wys2/Oa0tssUxoE/ZVMhb8gCN3omP9MKr8YhXoprWY4cKweklbKsrnZA3o2dWMnS9izJlEWg6ku7k/L7pEb4y9kPB21A6VGSDYn9aGpV/tLORk9WgCKgrRep50U1i/zRjGml3Kpj5C/P3biaERyd8+gh3EjKuTRYd0Gb1CLb81Qj6vne8QMJau4VBO73/xTpv9xqGbYYDskrZYeS80SwcwMUolcGIEZq/oUUAFQEIX6mtn7bUaQZEkEDghh7wvTEOcnwdWD4EZ/1TkRNSugXdjvZN7XLydavYhv71UFbRfmyNcvjFE6aI348P9TlmyC+BBBjGLQ9PRYXrS7QJo58GuGUiIDu6J6uJFKUKMBpvuBcZoRgMARYgc4EYRlrMl6WjieIyibK3/d1XTAkw1YcpSBfnJ9etspOcab17Lo/75oOX5Oc2N2IteCqHk4JI1sjZu4qv01OX9xhajh/tKJ4Rbn4zxHybwl+tgSnCTL3uJ9E5KrUn886et3BOXL4kEz8sjyCDJHlJJfB+PFjnLdZgD/Li8XIdJ5EXX0I2Zzz7gVfiO4MzeQXgzvu1/o5TAEDp7PrGqHCHJCn35Vr2jlQcCLnmN+6GWyuEf3iMe39UuWCBFqoEPanB3L5jz5Y+O0Wvm4LU951nDP2a5glnDyiOB5UCOSSl2/nFXoLNvyxCKDnNGIfDdCZKShW3spOfeVKZoHoqBbG9NqBBX8izWQB5/c7aa5sjnRXT7+jU9RiNOgsJYNPa03A5BHp/HaJo80iQx2TsTMI2Nd5BZa0gQ7RT8=")
//				.contentType(MediaType.APPLICATION_JSON).content(inputJson);
//		
//		 MvcResult result = mockMvc.perform(requestBuilder).andReturn();
//		 MockHttpServletResponse response = result.getResponse();
//		 assertEquals(HttpStatus.OK.value(), response.getStatus());
//	}
//	
//	@Test
//	public void testConfigurationControllerclearExpressionsCacheAll() throws Exception
//	{
//		
//		
//		 String inputJson ="{\"operation\": \"clearExpressionsCacheAll\" }";
//		 RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/channel/configurationList/get")
//				.header("alightSecureSessionToken",
//						"yPAGJ0XvzYy7sJUtoBHt9BulwOHvW7kxHvkl7vv2+ufeovToLFWvAXdUExffzmcgA1gYPn+dHaHNUTT6fR5SRx5/j9+/5nO2/X2dRf47Plhx7bi8SkzEZhoUpo8wEGzNeU4O1ft2naVxfKIi4V4xjNe64bpwar8y/G3/0Lx96MF3BNTyzWXGZ78mM5hLUx6f24kLK/aYsx+vI/1wn0y8kIO/V4zhUBrRj2Ixe8W3CF4E8vaDOJZySj5Qw2PGyhUz4STfCtzStP8k2uVsKyW1HorkU4DkDVVg6YdS6IIB/wkyurisvp3MN3IBRzj2uxctkmZ67zpJjYzNNyeWtXa/INR5nLcoaO+AgfRYbFWW05FjQSrL+OBLFe6XxJRA0nPJl3ExBvCxRLbHEEFzII5qQagjje+Ykn3zdR46PTcBgm6bxWmS3jgG0Ju2cWiQfZn2/wLBcMVMJ+jVVa7o6WEJtsXCagnHjdyzwdiLD5f/uB4CsmmqqM+cWR6n9w2c8MVGm7a0A6PczM/CLtlJrPrTbOg+nNwuML5uURp19t4Ryh4JxtLkDuooXH4lpOJL1E2HB75zg1oIX3sqQ6Nj7i1Fu8LpHHqwx5NIz034oaFrjc7Q1DWaLI/HOmIIjWhqOliNb9RzpTaVRQWTKdeT8Ci4vvdYYmauW/94DYPh+eqefFxAff3/ADfW7H4Ssq2DWLoD6DJmsw+ossjUwzBrfHh1aMbZ9HL0lRXMwI40jwBbwrmBBxZiwSXUxLIM+c/8ystJN0J56A5WR+ltHiR/AdfDOwZIzOwmGmWQKa0/PYr5LUa75T0LyRNKR2WyfocBtWDyiyaoDylDOQdWkxTqvLlrY1xWpOHGcIrRbj3sMrGrVnz9MwR9l3dYNMnN4Jo/XzN2ghO12jBnO0Prlm1dxgw37Yfaqj49X7QcFmfGJGO+P3yA/jMz97cVR0q5pfkuinRdUBiCnjycO8OOlZz7ni0rTQICOqIL8Qr2oClFl1e1VPAiIozvzQrXISe6sfanVzzusjGldD+C7rzJe2DSCrOXirdFMn5JQidU9fX8XZTiyvgYBg9GgvDc+zvogUfPhHhDOi4CTd0zPsx3BAnWHmNH7fmIjkrGXwtwXrAqZ9d/auhnsMAgpeKJ62B/KfII5m139+Vgvsyx6GU8bw1li0Cl5Wys2/Oa0tssUxoE/ZVMhb8gCN3omP9MKr8YhXoprWY4cKweklbKsrnZA3o2dWMnS9izJlEWg6ku7k/L7pEb4y9kPB21A6VGSDYn9aGpV/tLORk9WgCKgrRep50U1i/zRjGml3Kpj5C/P3biaERyd8+gh3EjKuTRYd0Gb1CLb81Qj6vne8QMJau4VBO73/xTpv9xqGbYYDskrZYeS80SwcwMUolcGIEZq/oUUAFQEIX6mtn7bUaQZEkEDghh7wvTEOcnwdWD4EZ/1TkRNSugXdjvZN7XLydavYhv71UFbRfmyNcvjFE6aI348P9TlmyC+BBBjGLQ9PRYXrS7QJo58GuGUiIDu6J6uJFKUKMBpvuBcZoRgMARYgc4EYRlrMl6WjieIyibK3/d1XTAkw1YcpSBfnJ9etspOcab17Lo/75oOX5Oc2N2IteCqHk4JI1sjZu4qv01OX9xhajh/tKJ4Rbn4zxHybwl+tgSnCTL3uJ9E5KrUn886et3BOXL4kEz8sjyCDJHlJJfB+PFjnLdZgD/Li8XIdJ5EXX0I2Zzz7gVfiO4MzeQXgzvu1/o5TAEDp7PrGqHCHJCn35Vr2jlQcCLnmN+6GWyuEf3iMe39UuWCBFqoEPanB3L5jz5Y+O0Wvm4LU951nDP2a5glnDyiOB5UCOSSl2/nFXoLNvyxCKDnNGIfDdCZKShW3spOfeVKZoHoqBbG9NqBBX8izWQB5/c7aa5sjnRXT7+jU9RiNOgsJYNPa03A5BHp/HaJo80iQx2TsTMI2Nd5BZa0gQ7RT8=")
//				.contentType(MediaType.APPLICATION_JSON).content(inputJson);
//		
//		 MvcResult result = mockMvc.perform(requestBuilder).andReturn();
//		 MockHttpServletResponse response = result.getResponse();
//		 assertEquals(HttpStatus.OK.value(), response.getStatus());
//	}
//	
//	@Test
//	public void testConfigurationControllergetBusinessObject() throws Exception
//	{
//		
//		
//		 String inputJson ="{\"operation\": \"getBusinessObject\" }";
//		 RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/channel/configurationList/get")
//				.header("alightSecureSessionToken",
//						"yPAGJ0XvzYy7sJUtoBHt9BulwOHvW7kxHvkl7vv2+ufeovToLFWvAXdUExffzmcgA1gYPn+dHaHNUTT6fR5SRx5/j9+/5nO2/X2dRf47Plhx7bi8SkzEZhoUpo8wEGzNeU4O1ft2naVxfKIi4V4xjNe64bpwar8y/G3/0Lx96MF3BNTyzWXGZ78mM5hLUx6f24kLK/aYsx+vI/1wn0y8kIO/V4zhUBrRj2Ixe8W3CF4E8vaDOJZySj5Qw2PGyhUz4STfCtzStP8k2uVsKyW1HorkU4DkDVVg6YdS6IIB/wkyurisvp3MN3IBRzj2uxctkmZ67zpJjYzNNyeWtXa/INR5nLcoaO+AgfRYbFWW05FjQSrL+OBLFe6XxJRA0nPJl3ExBvCxRLbHEEFzII5qQagjje+Ykn3zdR46PTcBgm6bxWmS3jgG0Ju2cWiQfZn2/wLBcMVMJ+jVVa7o6WEJtsXCagnHjdyzwdiLD5f/uB4CsmmqqM+cWR6n9w2c8MVGm7a0A6PczM/CLtlJrPrTbOg+nNwuML5uURp19t4Ryh4JxtLkDuooXH4lpOJL1E2HB75zg1oIX3sqQ6Nj7i1Fu8LpHHqwx5NIz034oaFrjc7Q1DWaLI/HOmIIjWhqOliNb9RzpTaVRQWTKdeT8Ci4vvdYYmauW/94DYPh+eqefFxAff3/ADfW7H4Ssq2DWLoD6DJmsw+ossjUwzBrfHh1aMbZ9HL0lRXMwI40jwBbwrmBBxZiwSXUxLIM+c/8ystJN0J56A5WR+ltHiR/AdfDOwZIzOwmGmWQKa0/PYr5LUa75T0LyRNKR2WyfocBtWDyiyaoDylDOQdWkxTqvLlrY1xWpOHGcIrRbj3sMrGrVnz9MwR9l3dYNMnN4Jo/XzN2ghO12jBnO0Prlm1dxgw37Yfaqj49X7QcFmfGJGO+P3yA/jMz97cVR0q5pfkuinRdUBiCnjycO8OOlZz7ni0rTQICOqIL8Qr2oClFl1e1VPAiIozvzQrXISe6sfanVzzusjGldD+C7rzJe2DSCrOXirdFMn5JQidU9fX8XZTiyvgYBg9GgvDc+zvogUfPhHhDOi4CTd0zPsx3BAnWHmNH7fmIjkrGXwtwXrAqZ9d/auhnsMAgpeKJ62B/KfII5m139+Vgvsyx6GU8bw1li0Cl5Wys2/Oa0tssUxoE/ZVMhb8gCN3omP9MKr8YhXoprWY4cKweklbKsrnZA3o2dWMnS9izJlEWg6ku7k/L7pEb4y9kPB21A6VGSDYn9aGpV/tLORk9WgCKgrRep50U1i/zRjGml3Kpj5C/P3biaERyd8+gh3EjKuTRYd0Gb1CLb81Qj6vne8QMJau4VBO73/xTpv9xqGbYYDskrZYeS80SwcwMUolcGIEZq/oUUAFQEIX6mtn7bUaQZEkEDghh7wvTEOcnwdWD4EZ/1TkRNSugXdjvZN7XLydavYhv71UFbRfmyNcvjFE6aI348P9TlmyC+BBBjGLQ9PRYXrS7QJo58GuGUiIDu6J6uJFKUKMBpvuBcZoRgMARYgc4EYRlrMl6WjieIyibK3/d1XTAkw1YcpSBfnJ9etspOcab17Lo/75oOX5Oc2N2IteCqHk4JI1sjZu4qv01OX9xhajh/tKJ4Rbn4zxHybwl+tgSnCTL3uJ9E5KrUn886et3BOXL4kEz8sjyCDJHlJJfB+PFjnLdZgD/Li8XIdJ5EXX0I2Zzz7gVfiO4MzeQXgzvu1/o5TAEDp7PrGqHCHJCn35Vr2jlQcCLnmN+6GWyuEf3iMe39UuWCBFqoEPanB3L5jz5Y+O0Wvm4LU951nDP2a5glnDyiOB5UCOSSl2/nFXoLNvyxCKDnNGIfDdCZKShW3spOfeVKZoHoqBbG9NqBBX8izWQB5/c7aa5sjnRXT7+jU9RiNOgsJYNPa03A5BHp/HaJo80iQx2TsTMI2Nd5BZa0gQ7RT8=")
//				.contentType(MediaType.APPLICATION_JSON).content(inputJson);
//		
//		 MvcResult result = mockMvc.perform(requestBuilder).andReturn();
//		 MockHttpServletResponse response = result.getResponse();
//		 assertEquals(HttpStatus.OK.value(), response.getStatus());
//	}
//	
//	
//	
//	
//	
//	
//}
