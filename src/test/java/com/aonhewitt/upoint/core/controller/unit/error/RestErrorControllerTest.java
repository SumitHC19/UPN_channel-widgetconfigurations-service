package com.aonhewitt.upoint.core.controller.unit.error;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.request.WebRequest;

import com.aonhewitt.upoint.core.controller.error.RestErrorController;
import com.aonhewitt.upoint.util.CommonUtils;

import jakarta.servlet.http.HttpServletRequest;

@ExtendWith(SpringExtension.class)
@TestPropertySource(properties = "spring.cloud.discovery.enabled=false")

/*@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
@WebMvcTest(value = CustomErrorController.class, secure = false)
@TestPropertySource(properties = "spring.cloud.discovery.enabled=false")*/
//@RunWith(SpringRunner.class)
public class RestErrorControllerTest {
	
	@Mock
    private ApplicationContext applicationContext;

	//@Autowired
    private MockMvc mockMvc;
	
	@InjectMocks
	private CommonUtils commonUtils;
	
	@Mock
	private ErrorAttributes errorAttributes;
	
	@Mock
	private HttpStatus httpStatus;
	
	@Mock
	private HttpServletRequest request;
	
	@Mock
	private WebRequest requestAttributes;//Modified for PC

	
	/**
	 * @Yogesh M - Below mock dependency required by Spring-boot
	 */
	@MockBean
	TomcatServletWebServerFactory tomcatServletWebServerFactory;
	
	@InjectMocks
	RestErrorController restErrorController;
	
	//@InjectMocks
	//private RestErrorController restErrorController;
	
	@BeforeEach
    public void init(){
		//MockitoAnnotations.initMocks(this);
	//	mockMvc = MockMvcBuilders.standaloneSetup(restErrorController).build();
		mockMvc = MockMvcBuilders.standaloneSetup(restErrorController).build();
		
		Map<String, Object> map = new HashMap<String,Object>();
    	map.put("error", "Custom");
    	map.put("status", 400);
    	map.put("message", "testmessage");
		
		when(errorAttributes.getErrorAttributes(requestAttributes, ErrorAttributeOptions.defaults())).thenReturn(map);
    	
    }
	
    @Test
    public void testRestErrorController() throws Exception {
    	
    	//new MockCommonUtils();
    	
    	MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/error/json/personMessages")
				.contentType(MediaType.APPLICATION_JSON_VALUE);
    	
    	MvcResult result = mockMvc.perform(requestBuilder).andReturn();
    	   	
    	MockHttpServletResponse response = result.getResponse();
    	
    	assertEquals(HttpStatus.OK.value(), response.getStatus());

    }
    
    
}
