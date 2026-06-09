package com.aonhewitt.upoint.core.controller.unit.widget;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.aonhewitt.portal.adapter.AdapterService;
import com.aonhewitt.portal.core.service.util.CmServicesUtil;
import com.aonhewitt.portal.core.service.util.CommonUtil;
import com.aonhewitt.portal.feature.tile.mget.action.TileMultiGetAdapter;
import com.aonhewitt.portal.service.delegators.ConfigManagerService;
import com.aonhewitt.portal.util.MessageAppContainer;
import com.aonhewitt.portal.util.RestServiceInvocationResult;
import com.aonhewitt.upoint.conf.AppProperties;
import com.aonhewitt.upoint.conf.YAMLConfig;
import com.aonhewitt.upoint.portal.configuration.util.ConfigurationParmUtil;
import com.aonhewitt.upoint.util.MockAELockFalse;
import com.aonhewitt.upoint.util.MockSystemProperties;
import com.aonhewitt.upoint.util.TileMock;

import org.mockito.InjectMocks;

//@RunWith(SpringRunner.class)
//@WebMvcTest(WidgetRestController.class)
//@TestPropertySource(properties = "spring.cloud.discovery.enabled=false")

public class TileAdapterTest {

	

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

	@InjectMocks
	ConfigurationParmUtil configurationParmUtil;

	@InjectMocks
	TileMultiGetAdapter multigetAdapter;

	com.alight.portal.feature.tile.mget.action.TileMultiGetAdapter multigetNewLogic;

	
	private List<Map<Object, Object>> output;
	RestServiceInvocationResult serviceInvocationResult;
	private MessageAppContainer messageAppContainer = new MessageAppContainer();

	//@Before
	public void setUp() throws Exception {
		messageAppContainer.setPreAuthRequest(false);
		output = new ArrayList<Map<Object, Object>>();
		serviceInvocationResult = new RestServiceInvocationResult();
		new MockAELockFalse();
		new MockSystemProperties();
	}

	//@After
	public void tearDown() throws Exception {
	
		output = null;
		serviceInvocationResult = null;

	}

	// old Design Test Case Positive test case
	//@Test
	public void tileOldDesignPositiveTest() throws Exception {
		Map<Object, Object> widgetMap = new HashMap<Object, Object>();
		widgetMap.put("GroupName", "Highlights for U");
		Map<Object, Object> widgetMapResult = new HashMap<Object, Object>();
		widgetMapResult.put("text", widgetMap);
		output.add(widgetMapResult);
		serviceInvocationResult.setResponseCMValues(output);
		serviceInvocationResult.setResponseStatus(RestServiceInvocationResult.ResponseStatusEnum.SUCCESS);
		new TileMock(serviceInvocationResult);
		RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/channel/widgetConfigurations/tiles")
				.header("alightSecureSessionToken",
						"bOiAh9QDExxcLlAEl5s2AtpviYozDv+gjaPyaJb5MeEsvzbpdcklLNzKZEXWR9yZ9MvQ2xJndbWDRNvF16Dc2455I7Zh94h2uUln6pPYAZIOsHiBG4wCWEUh+87EPowJkwexeTeBawbFePyNlQqCdZL6X2tF6GqUG6ZyI1mv+3A+8ALuB3Aw7HjkqzaIS0bvoLw0QnboRLdOlAJ44LAb7ep/ZffHHN9IBHiYMgilwukHN0EdPV+0dA9+0jZeG1AC1DuNtW4nev8TwxsLM8diu4EGIxKDixK+Vv6VV93G5gtAGzsI9+jMSt61Ok6N6fK1nfoSWsDIrno1CM8gydkdRm4/y4Ivp5ByW+1FaUwVHuhwYDAchVfb3LJuEwGC1y/wDFCqyXgIdnDX5Jkg1cka3lWIyK2ysy3CKUHahyeWxBqmoHHbwsnFkuZDqMS3QNRWRqcbcnyKhr8qIz0UWoKGx/4S7Z1izQANdRP0Bz4kE3yaQvH2CQ+Q+WuKv8+SUNCI8bo67Ka2x5q73lhkevWYjOL2OR64hCXQPdeM7gKP0/d0dDxTvX9koJ8KGXMfqfsJmOBDXWXKpXuuIOJEYp3WvcsTEPbuhHQ3R3DpKFUe3sj4BubZShpzCy7mB4LCFUmH9Z9GPdhquO68B1XmlxqlT5p+eaz/0l87DuUGX8LJdcuAJxreVAYazaLUHSbR+JSR6s+CCn72Ay7XXmIV8V2Rsj9a4msefajY/q/8WqONGTogYLf+m8JcXeq10vGxCywMVkS2BMp2IMZ4LkF6IRvTGf72N+A78E3OVZMgP46lv7hvjY9dnE9ysLyVYhf03Zfc4XxKyrKIYJQLvFz/kARCM4DYYnr5NM2qyoRdB+SLDgStISK3YSkygWAkuzg3nlargzplVeDpAxHWGfT1o31xnPMyW1cs0Pbu3aFawR8QkbRUx5KvQ4qWrtXU7d7vwT5Nuaven6y79WBt47s3reL6yN2kXMBnhoT2wKFi2keaHVhr8P9yhLdhJzbun+7F0sv5Gl2A6j7QDGeSQMSGrUXQbYsVNTH81ZmEcRfgLZ2Xnow=",
						"")
				.contentType(MediaType.APPLICATION_JSON);
		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		MockHttpServletResponse response = result.getResponse();
		assertEquals(HttpStatus.OK.value(), response.getStatus());
		//JSONAssert.assertEquals(outputJson, response.getContentAsString(), false);
	}

	//@Test
	public void tileOldDesignNegativeTest() throws Exception {
		Map<Object, Object> widgetMap = new HashMap<Object, Object>();
		widgetMap.put("tileResponse", "The participant is not eligibile for any tiles in the group");
		Map<Object, Object> widgetMapResult = new HashMap<Object, Object>();
		widgetMapResult.put("Error", widgetMap);
		output.add(widgetMapResult);
		serviceInvocationResult.setResponseCMValues(output);
		serviceInvocationResult.setResponseStatus(RestServiceInvocationResult.ResponseStatusEnum.FAILURE);
		final Map<Object, Object> errorMap = new HashMap<>();
		List<Map<Object, Object>> errorList = new ArrayList<>();
		errorMap.put("Error", "The participant is not eligibile for any tiles in the group");
		errorList.add(errorMap);
		serviceInvocationResult.setResponseStatus(RestServiceInvocationResult.ResponseStatusEnum.FAILURE);
		serviceInvocationResult.setResponseCMError(errorList);
		new TileMock(serviceInvocationResult);
		RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/channel/widgetConfigurations/tiles")
				.header("newDesign","N")
				.contentType(MediaType.APPLICATION_JSON);
		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		MockHttpServletResponse response = result.getResponse();
		assertEquals(HttpStatus.OK.value(), response.getStatus());
	}
	//@Test
	public void tileNewDesignTestCase() throws Exception {
		Map<Object, Object> widgetMap = new HashMap<Object, Object>();
		widgetMap.put("GroupName", "Highlights for U");
		Map<Object, Object> widgetMapResult = new HashMap<Object, Object>();
		widgetMapResult.put("text", widgetMap);
		output.add(widgetMapResult);
		serviceInvocationResult.setResponseCMValues(output);
		serviceInvocationResult.setResponseStatus(RestServiceInvocationResult.ResponseStatusEnum.SUCCESS);
		new TileMock(serviceInvocationResult);
		RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/channel/widgetConfigurations/tiles")
				.header("newDesign", "Y", "").contentType(MediaType.APPLICATION_JSON);
		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		MockHttpServletResponse response = result.getResponse();
		assertEquals(HttpStatus.OK.value(), response.getStatus());
		//JSONAssert.assertEquals(outputJson, response.getContentAsString(), false);
	}

}
