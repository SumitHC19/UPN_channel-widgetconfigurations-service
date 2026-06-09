package com.aonhewitt.upoint.core.controller.unit.error;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mockStatic;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.context.request.WebRequest;

import com.aonhewitt.upoint.core.controller.error.CustomErrorController;
import com.aonhewitt.upoint.util.CommonUtils;

import jakarta.servlet.http.HttpServletRequest;


@ExtendWith(SpringExtension.class)
public class CustomErrorControllerTest {
	
	@Mock
    private ApplicationContext applicationContext;

	
	@Mock
	private ErrorAttributes errorAttributes;
	
	@Mock
	private HttpStatus httpStatus;
	
	@Mock
	private HttpServletRequest request;
	
	@Mock
	private WebRequest requestAttributes;

	
	/**
	 * @Yogesh M - Below mock dependency required by Spring-boot
	 */
	@MockBean
	TomcatServletWebServerFactory tomcatServletWebServerFactory;
	
	@InjectMocks
	private CustomErrorController customErrorController;
	
	
    @Test
	public void testCustomErrorController() throws Exception {
		try (MockedStatic<CommonUtils> commonUtilsMockedStatic = mockStatic(CommonUtils.class);) {

			Map<String, Object> map = new HashMap<String, Object>();
			map.put("error", "Custom");
			map.put("status", 400);
			map.put("message", "testmessage");

			commonUtilsMockedStatic
					.when(() -> CommonUtils.getRequestAttributeString(Mockito.any(), Mockito.any(String.class)))
					.thenReturn("True");

			Mockito.when(errorAttributes.getErrorAttributes(Mockito.any(), Mockito.any())).thenReturn(map);

			ResponseEntity<Map<String, Object>> response = customErrorController.error(request);
			assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCode().value());
			
			
			commonUtilsMockedStatic
			.when(() -> CommonUtils.getRequestAttributeString(Mockito.any(), Mockito.any(String.class)))
			.thenReturn("");
			
			ResponseEntity<Map<String, Object>> response2 = customErrorController.error(request);
			assertEquals(HttpStatus.BAD_REQUEST.value(), response2.getStatusCode().value());

		}
	}
    
    
    @Test
   	public void getErrorTemplateTest() throws Exception {
    	assertNotNull(customErrorController.getErrorTemplate());
    	
    	assertNotNull(customErrorController.getErrorAttributes());
    	
    	assertNotNull(customErrorController.getErrorPath());
    	
    	
    	
    }
}
