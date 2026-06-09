package com.aonhewitt.upoint.filter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockConstruction;
import static org.mockito.Mockito.mockStatic;

import java.io.IOException;
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
import org.mockito.MockedConstruction;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.alight.logging.constant.UpointLogConstants;
import com.alight.logging.helpers.LogEventDataHandler;
import com.aonhewitt.portal.util.AppContainerProvider;
import com.aonhewitt.portal.util.MessageAppContainer;
import com.aonhewitt.upoint.cba.service.util.CbaDataUtil;
import com.aonhewitt.upoint.conf.AppConfig;
import com.aonhewitt.upoint.util.AELockUtil;
import com.aonhewitt.upoint.util.CommonUtils;
import com.aonhewitt.upoint.util.ConfigurationListUtils;
import com.aonhewitt.upoint.util.MockAELockFalse;
import com.aonhewitt.upoint.util.MockAELockTrue;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.ServletException;

@ExtendWith(SpringExtension.class)
public class ConfigurationListFilterTest {
	 
	String alightSessionToken="{\"accessToken\":\"eyJhbGciOiJg\",\"sessionId\":\"472874c1\",\"idMapping\":\"[ { \\\"platformType\\\" : \\\"TBA_4X\\\", \\\"domain\\\" : \\\"DC,DB\\\", \\\"clientId\\\" : \\\"19920\\\", \\\"normalizedClientid\\\" : \\\"19920\\\", \\\"systemInstanceId\\\" : \\\"\\\", \\\"platformInternalId\\\" : \\\"184000044\\\", \\\"platformExternalId\\\" : \\\"140626002\\\", \\\"roleType\\\" : \\\"rkp\\\" } ]\",\"channelUserProfile\":\"null\",\"brokerUserId\":\"Portal\",\"clientId\":\"19920\",\"systemInstanceId\":\"null\",\"testCfg\":\"@@SB\",\"subjectId\":\"null\",\"subjectType\":\"null\",\"systemDate\":\"null\",\"locale\":\"en_US\",\"roleId\":\"null\",\"domainDocumentMapping\":\"null\",\"consumerReferenceId\":\"null\",\"expires\":\"1510649321900\"}";
	String alightPersonSessionToken="{\"accessToken\":\"eyJhbGciOiJg\",\"sessionId\":\"472874c1\",\"idMapping\":\"[ { \\\"platformType\\\" : \\\"TBA_4X\\\", \\\"domain\\\" : \\\"DC,DB\\\", \\\"clientId\\\" : \\\"19920\\\", \\\"normalizedClientid\\\" : \\\"19920\\\", \\\"systemInstanceId\\\" : \\\"\\\", \\\"platformInternalId\\\" : \\\"184000044\\\", \\\"platformExternalId\\\" : \\\"140626002\\\", \\\"roleType\\\" : \\\"rkp\\\" } ]\",\"channelUserProfile\":\"null\",\"brokerUserId\":\"Portal\",\"clientId\":\"19920\",\"systemInstanceId\":\"null\",\"testCfg\":\"@@SB\",\"subjectId\":\"null\",\"subjectType\":\"null\",\"systemDate\":\"null\",\"locale\":\"en_US\",\"roleId\":\"null\",\"domainDocumentMapping\":\"null\",\"consumerReferenceId\":\"null\",\"expires\":\"1510649321900\"}";
	
	String alightRequestHeader= "{\"locale\":\"en_US\",\"consumerReferenceId\":\"3434\",\"roleId\":\\\"19941_1.0-E:\\\"}";
	
	String alightSessionTokenEmpty = "";
	
    private static final String testUri = "/channel/configurationList/get" ; 
    String alightRequestHeaderPreAuth= "{\"locale\":\"en_US\",\"clientId\":\"19920\",\"consumerReferenceId\":\"3434\"}";
    private static final String preAuthUri = "/channel/configurationList/get/public";
   
    private static MockedStatic<CommonUtils> commonUtilsMockedStatic;

	@BeforeAll
	public static void init() {
		Mockito.clearAllCaches();
		commonUtilsMockedStatic = mockStatic(CommonUtils.class);
		

	}

	@AfterAll
	public static void close() {
		commonUtilsMockedStatic.close();
	}
    
    @BeforeEach
	public void setup() {
		Mockito.clearAllCaches();
		MockitoAnnotations.openMocks(this);
	}
    @InjectMocks
    private ConfigurationListFilter confFilter = new ConfigurationListFilter();
    
    @org.mockito.Mock
    private ConfigurableEnvironment envt;
   
    @org.mockito.Mock
    private AppConfig appConfig;
    
    @MockBean
    private AELockUtil aELockUtil;
    
    @org.mockito.Mock
	private ConfigurationListUtils configurationListUtils;
    
    @org.mockito.Mock
    private CbaDataUtil cbaDataUtil;
    
    @MockBean
    private ObjectMapper mapper;
    
    @Test
    public void testDoFilterPositiveScenarioWhenTokenIsInHeader() throws ServletException, IOException {
    		/*new MockUp<DebugUtils>() {
			
			@Mock
			public void logRequestPayLoad(HttpServletRequest httpRequest) {
				

			}

		};*/
    	Mockito.when(configurationListUtils.isRedisCallFlag(Mockito.any(),Mockito.any())).thenReturn(true);
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("alightSessionToken", alightSessionToken);
        request.addHeader("alightRequestHeader", alightRequestHeader);
        request.setRequestURI(testUri);
        MockHttpServletResponse response = new MockHttpServletResponse();
        MockFilterChain filterChain = new MockFilterChain();
    //    doNothing().when(configurationListUtils).logRequestPayLoad(request);
      //  when(debugUtils.logRequestPayLoad(request));
        /*new MockUp<AELockUtil>() {
			@mockit.Mock
			public  boolean isAELockEnabled(){
				return true;
			}
			
		};*/
        //new MockAELockTrue();
        try (MockedStatic<AELockUtil> aELockUtilMockedStatic = mockStatic(AELockUtil.class)) {
			aELockUtilMockedStatic.when(() -> AELockUtil.isAELockEnabled()).thenReturn(true);
		 doNothing().when(cbaDataUtil).buildCbaSystemData(Mockito.any(MessageAppContainer.class));
        confFilter.doFilter(request, response, filterChain);
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        }
    }
    
    @Test
    public void testDoFilterWithSecondaryDataCache() throws ServletException, IOException {
    		
    	
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("alightSessionToken", alightSessionToken);
        request.addHeader("UseSecondaryDataCache", "true");
        request.setRequestURI(testUri);
        MockHttpServletResponse response = new MockHttpServletResponse();
        MockFilterChain filterChain = new MockFilterChain();
    //    doNothing().when(configurationListUtils).logRequestPayLoad(request);
        Mockito.when(envt.getProperty(Mockito.any())).thenReturn("true");
       /* new MockUp<AELockUtil>() {
			@mockit.Mock
			public  boolean isAELockEnabled(){
				return true;
			}
		};*/
       // new MockAELockTrue();
        try (MockedStatic<AELockUtil> aELockUtilMockedStatic = mockStatic(AELockUtil.class)) {
			aELockUtilMockedStatic.when(() -> AELockUtil.isAELockEnabled()).thenReturn(true);
		 doNothing().when(cbaDataUtil).buildCbaSystemData(Mockito.any(MessageAppContainer.class));
        confFilter.doFilter(request, response, filterChain);
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        }
    }
    
    @Test
    public void testDoFilterWithlogconfigstoragemap() throws ServletException, IOException {
    		
    	
        MockHttpServletRequest request = new MockHttpServletRequest();
        AppContainerProvider appContainerProvider=mock(AppContainerProvider.class);
       MessageAppContainer messageAppContainer=new MessageAppContainer();
      
        Map<Object, Object> attributes = new HashMap<Object, Object>();
        attributes.put(CommonUtils.NON_SECURE_REQUEST_BODY_ATTRIBUTES_MESSAGE, "test");
       messageAppContainer.setAttributes(attributes);
        appContainerProvider.setContainer(messageAppContainer);
        
        request.addHeader("alightSessionToken", alightSessionToken);
        request.addHeader("UseSecondaryDataCache", "true");
        request.setRequestURI(testUri);
        MockHttpServletResponse response = new MockHttpServletResponse();
        MockFilterChain filterChain = new MockFilterChain();
        Mockito.when(envt.getProperty(Mockito.any())).thenReturn("true");
        Mockito.when(configurationListUtils.isRedisCallFlag(Mockito.any(),Mockito.any())).thenReturn(true);
        Map<String, String> logconfigstoragemap= new HashMap<>();
        logconfigstoragemap.put(UpointLogConstants.REDIS_CALL_FLAG, "true");
        
        try (MockedStatic<AELockUtil> aELockUtilMockedStatic = mockStatic(AELockUtil.class);
        		MockedStatic<LogEventDataHandler> logEventDataHandlerMockedStatic = mockStatic(LogEventDataHandler.class)) {
			aELockUtilMockedStatic.when(() -> AELockUtil.isAELockEnabled()).thenReturn(true);
			logEventDataHandlerMockedStatic.when(() -> LogEventDataHandler.getLogconfigstoragemap()).thenReturn(logconfigstoragemap);
		 doNothing().when(cbaDataUtil).buildCbaSystemData(Mockito.any(MessageAppContainer.class));
        confFilter.doFilter(request, response, filterChain);
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        }
    }
    
    @Test
    public void testDoFilterWithSecondaryDataCacheFalse() throws ServletException, IOException {
		assertThrows(Exception.class, () -> {


			MockHttpServletRequest request = new MockHttpServletRequest();
			request.addHeader("alightSessionToken", alightSessionTokenEmpty);
			request.addHeader("UseSecondaryDataCache", "true");
			request.setRequestURI(testUri);
			MockHttpServletResponse response = new MockHttpServletResponse();
			MockFilterChain filterChain = new MockFilterChain();
			//    doNothing().when(configurationListUtils).logRequestPayLoad(request);
			Mockito.when(envt.getProperty(Mockito.any())).thenReturn("false");
			/*new MockUp<AELockUtil>() {
				@mockit.Mock
				public  boolean isAELockEnabled(){
					return true;
				}
			};*/
			new MockAELockTrue();
			//Removed for PC
			//doNothing().when(cbaDataUtil).buildCbaSystemData(Mockito.any(MessageAppContainer.class));
			confFilter.doFilter(request, response, filterChain);
			assertEquals(HttpStatus.OK.value(), response.getStatus());

		});

	}
    @Test
    public void testDoFilterWithSecondaryDataCacheSecDisabled() throws ServletException, IOException {
		assertThrows(Exception.class, () -> {
			MockHttpServletRequest request = new MockHttpServletRequest();
			request.addHeader("UseSecondaryDataCache", "true");
			request.addHeader("alightSessionToken", alightSessionToken);
			request.addHeader("alightRequestHeader", alightRequestHeader);
			request.setRequestURI(testUri);
			MockHttpServletResponse response = new MockHttpServletResponse();
			MockFilterChain filterChain = new MockFilterChain();
			Mockito.when(envt.getProperty(Mockito.any())).thenReturn("false");
			/*new MockUp<AELockUtil>() {
				@mockit.Mock
				public  boolean isAELockEnabled(){
					return false;
				}
			};*/
			new MockAELockFalse();
			//Removed for PC
			//doNothing().when(cbaDataUtil).buildCbaSystemData(Mockito.any(MessageAppContainer.class));
			confFilter.doFilter(request, response, filterChain);
			assertEquals(HttpStatus.OK.value(), response.getStatus());

		});

	}
    //to do need to uncommnet and fix
    //@Test
    public void testDoFilterWithRedisAlias() throws ServletException, IOException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("SecondaryRedisAlias", "default");
        request.addHeader("alightSessionToken", alightSessionToken);
        request.addHeader("alightRequestHeader", alightRequestHeader);
        request.setRequestURI(testUri);
        MockHttpServletResponse response = new MockHttpServletResponse();
        MockFilterChain filterChain = new MockFilterChain();
		Mockito.when(envt.getProperty(Mockito.any())).thenReturn("true");
		/*new MockUp<AELockUtil>() {
			@mockit.Mock
			public  boolean isAELockEnabled(){
				return false;
			}
		};*/
		//new MockAELockFalse();
		//new MockCommonUtils();
		List<String> list = new ArrayList<String>();
		list.add("default");
		//commonUtilsMockedStatic.when(() -> CommonUtils.getAliasList()).thenReturn(list);

		try (MockedStatic<AELockUtil> aELockUtilMockedStatic = mockStatic(AELockUtil.class)) {
			aELockUtilMockedStatic.when(() -> AELockUtil.isAELockEnabled()).thenReturn(false);
		doNothing().when(cbaDataUtil).buildCbaSystemData(Mockito.any(MessageAppContainer.class));
        confFilter.doFilter(request, response, filterChain);
       // assertEquals(HttpStatus.OK.value(), response.getStatus());
		}
    }
    
    @Test
    public void testDoFilterWithRedisAliasSecDisabled() throws ServletException, IOException {
		assertThrows(Exception.class, () -> {
			MockHttpServletRequest request = new MockHttpServletRequest();
			request.addHeader("SecondaryRedisAlias", "Alias");
			request.addHeader("alightSessionToken", alightSessionToken);
			request.addHeader("alightRequestHeader", alightRequestHeader);
			request.setRequestURI(testUri);
			MockHttpServletResponse response = new MockHttpServletResponse();
			MockFilterChain filterChain = new MockFilterChain();
			Mockito.when(envt.getProperty(Mockito.any())).thenReturn("false");
			/*new MockUp<AELockUtil>() {
				@mockit.Mock
				public  boolean isAELockEnabled(){
					return false;
				}
			};*/
			new MockAELockFalse();
			//Removed for PC
			//doNothing().when(cbaDataUtil).buildCbaSystemData(Mockito.any(MessageAppContainer.class));
			confFilter.doFilter(request, response, filterChain);
			assertEquals(HttpStatus.OK.value(), response.getStatus());

		});

	}
    
    @Test
    public void testDoFilterError() throws ServletException, IOException {
    	
    	MockHttpServletRequest request = new MockHttpServletRequest();
    	request.addHeader("alightSessionToken", alightSessionToken);
        request.setRequestURI(testUri);
        MockHttpServletResponse response = new MockHttpServletResponse();
        MockFilterChain filterChain = new MockFilterChain();
     //   doNothing().when(configurationListUtils).logRequestPayLoad(request);
        Mockito.when(envt.getProperty(Mockito.any())).thenReturn("true");
        /*new MockUp<AELockUtil>() {
			@mockit.Mock
			public  boolean isAELockEnabled(){
				return true;
			}
		};*/
        
        //new MockAELockTrue();
        try (MockedStatic<AELockUtil> aELockUtilMockedStatic = mockStatic(AELockUtil.class)) {
			aELockUtilMockedStatic.when(() -> AELockUtil.isAELockEnabled()).thenReturn(true);
		 doNothing().when(cbaDataUtil).buildCbaSystemData(Mockito.any(MessageAppContainer.class));
        confFilter.doFilter(request, response, filterChain);
        assertEquals(HttpStatus.OK.value(), response.getStatus());
    }}
    
    @Test
    public void testDoFilterPreAuth() throws ServletException, IOException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("alightRequestHeader", alightRequestHeaderPreAuth);
        request.setRequestURI(preAuthUri);
       /* new MockUp<AELockUtil>() {
			@mockit.Mock
			public  boolean isAELockEnabled(){
				return true;
			}
		};*/
        new MockAELockTrue();
        MockHttpServletResponse response = new MockHttpServletResponse();
        MockFilterChain filterChain = new MockFilterChain();
        doNothing().when(cbaDataUtil).buildCbaSystemData(Mockito.any(MessageAppContainer.class));
        confFilter.doFilter(request, response, filterChain);
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        
    }
    @Test
    public void testDoFilterPersonSessionToken() throws ServletException, IOException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("alightSessionToken", alightSessionTokenEmpty);
        request.addHeader("alightPersonSessionToken", alightPersonSessionToken);
       
        MockHttpServletResponse response = new MockHttpServletResponse();
        MockFilterChain filterChain = new MockFilterChain();
		Mockito.when(envt.getProperty(Mockito.any())).thenReturn("true");
		/*new MockUp<AELockUtil>() {
			@mockit.Mock
			public  boolean isAELockEnabled(){
				return false;
			}
		};*/ 
		
		//new MockAELockFalse();
		 try (MockedStatic<AELockUtil> aELockUtilMockedStatic = mockStatic(AELockUtil.class)) {
				aELockUtilMockedStatic.when(() -> AELockUtil.isAELockEnabled()).thenReturn(false);
		doNothing().when(cbaDataUtil).buildCbaSystemData(Mockito.any(MessageAppContainer.class));
        confFilter.doFilter(request, response, filterChain);
		 }
        
    }
    
    @Test
    public void testDoFilterASSessionTokenToken() throws ServletException, IOException {
        MockHttpServletRequest request = new MockHttpServletRequest();
      //  request.addHeader("alightSessionToken", alightSessionTokenEmpty);
      //  request.addHeader("alightPersonSessionToken", alightPersonSessionToken);
        request.addHeader("ASG-SessionToken", alightPersonSessionToken);
       
        MockHttpServletResponse response = new MockHttpServletResponse();
        MockFilterChain filterChain = new MockFilterChain();
		Mockito.when(envt.getProperty(Mockito.any())).thenReturn("true");
		/*new MockUp<AELockUtil>() {
			@mockit.Mock
			public  boolean isAELockEnabled(){
				return false;
			}
		};*/ 
		new MockAELockFalse();
		doNothing().when(cbaDataUtil).buildCbaSystemData(Mockito.any(MessageAppContainer.class));
        confFilter.doFilter(request, response, filterChain);
      
        
    }
    
    @Test
    public void testSessionId() throws ServletException, IOException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("alightSessionToken", alightSessionTokenEmpty);
        request.addHeader("alightPersonSessionToken", alightPersonSessionToken);
       
        
        ConfigurationListFilter.getSessionId(request, alightSessionToken);
        request.addHeader("sessionId", "123");
        ConfigurationListFilter.getSessionId(request, alightSessionToken);
      
        
    }
    @Test
    public void testSessionIdFail() throws ServletException, IOException {
		assertThrows(Exception.class, () -> {
			MockHttpServletRequest request = null;
			ConfigurationListFilter.getSessionId(request, alightSessionToken);


		});


	}
    
    @Test
    public void testDoFilterWithoutToken() throws ServletException, IOException {
		assertThrows(Exception.class, () -> {
			MockHttpServletRequest request = new MockHttpServletRequest();
			request.addHeader("alightRequestHeader", alightRequestHeader);
			request.addHeader("UseSecondaryDataCache", true);
			request.addHeader("sessionId", "1223");
			//Removed for PC
			//Mockito.when(envt.getProperty(Mockito.any())).thenReturn("false");
			request.setRequestURI(testUri);
			MockHttpServletResponse response = new MockHttpServletResponse();
			MockFilterChain filterChain = new MockFilterChain();
			/*new MockUp<AELockUtil>() {
				@mockit.Mock
				public  boolean isAELockEnabled(){
					return false;
				}
			};*/
			new MockAELockFalse();

			confFilter.doFilter(request, response, filterChain);


			assertEquals(HttpStatus.OK.value(), response.getStatus());

		});

	}
    @Test
	public void testSessionIdFailRuntimeException() throws ServletException, IOException {
		MockHttpServletRequest request = new MockHttpServletRequest();
		try (MockedConstruction<ObjectMapper> objectMapperMockedConstruction = mockConstruction(ObjectMapper.class,
				(objectMapper, context) -> {
					doThrow(new NullPointerException()).when(objectMapper).readTree(Mockito.any(String.class));
				})) {
			request.addHeader("alightRequestHeader", alightRequestHeader);
			request.addHeader("UseSecondaryDataCache", true);

			ConfigurationListFilter.getSessionId(request, alightSessionToken);

		}
	}
    
    
    
    
    
}