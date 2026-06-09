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
import org.mockito.Mock;
import org.mockito.MockedConstruction;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.alight.logging.constant.UpointLogConstants;
import com.alight.logging.helpers.LogEventDataHandler;
import com.aonhewitt.portal.userprovisioning.beans.AHUser;
import com.aonhewitt.portal.userprovisioning.beans.ChannelRequestData;
import com.aonhewitt.portal.userprovisioning.beans.ChannelUserProfile;
import com.aonhewitt.portal.util.AHUserMapperUtil;
import com.aonhewitt.portal.util.AppContainerProvider;
import com.aonhewitt.portal.util.MessageAppContainer;
import com.aonhewitt.upoint.cba.service.util.CbaDataUtil;
import com.aonhewitt.upoint.util.AELockUtil;
import com.aonhewitt.upoint.util.CommonUtils;
import com.aonhewitt.upoint.util.ConfigurationListUtils;
import com.aonhewitt.upoint.util.MockAELockFalse;
import com.aonhewitt.upoint.util.MockAELockTrue;
import com.aonhewitt.upoint.util.MockObjectMapper;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.ServletException;


@ExtendWith(SpringExtension.class)
public class AHUserFilterTest {

	String alightSessionToken="{\"accessToken\":\"eyJhbGciOiJg\",\"sessionId\":\"472874c1\",\"idMapping\":\"[ { \\\"platformType\\\" : \\\"TBA_4X\\\", \\\"domain\\\" : \\\"DC,DB\\\", \\\"clientId\\\" : \\\"19920\\\", \\\"normalizedClientid\\\" : \\\"19920\\\", \\\"systemInstanceId\\\" : \\\"\\\", \\\"platformInternalId\\\" : \\\"184000044\\\", \\\"platformExternalId\\\" : \\\"140626002\\\", \\\"roleType\\\" : \\\"rkp\\\" } ]\",\"channelUserProfile\":\"null\",\"brokerUserId\":\"Portal\",\"clientId\":\"19920\",\"systemInstanceId\":\"null\",\"testCfg\":\"@@SB\",\"subjectId\":\"null\",\"subjectType\":\"null\",\"systemDate\":\"null\",\"locale\":\"en_US\",\"roleId\":\"null\",\"domainDocumentMapping\":\"null\",\"consumerReferenceId\":\"null\",\"expires\":\"1510649321900\"}";
	String alightPersonSessionToken="{\"accessToken\":\"eyJhbGciOiJg\",\"sessionId\":\"472874c1\",\"idMapping\":\"[ { \\\"platformType\\\" : \\\"TBA_4X\\\", \\\"domain\\\" : \\\"DC,DB\\\", \\\"clientId\\\" : \\\"19920\\\", \\\"normalizedClientid\\\" : \\\"19920\\\", \\\"systemInstanceId\\\" : \\\"\\\", \\\"platformInternalId\\\" : \\\"184000044\\\", \\\"platformExternalId\\\" : \\\"140626002\\\", \\\"roleType\\\" : \\\"rkp\\\" } ]\",\"channelUserProfile\":\"null\",\"brokerUserId\":\"Portal\",\"clientId\":\"19920\",\"systemInstanceId\":\"null\",\"testCfg\":\"@@SB\",\"subjectId\":\"null\",\"subjectType\":\"null\",\"systemDate\":\"null\",\"locale\":\"en_US\",\"roleId\":\"null\",\"domainDocumentMapping\":\"null\",\"consumerReferenceId\":\"null\",\"expires\":\"1510649321900\"}";
	String alightSessionTokenEmpty = "";

	
	String alightRequestHeader= "{\"locale\":\"en_US\",\"consumerReferenceId\":\"3434\",\"roleId\":\\\"19941_1.0-E:\\\"}";
    private static final String testUri = "/personMessages/messages";
 
    
    String alightRequestHeaderPreAuth= "{\"locale\":\"en_US\",\"clientId\":\"19920\",\"consumerReferenceId\":\"3434\"}";
    private static final String preAuthUri = "/public";

    @org.mockito.Mock
	private ConfigurationListUtils configurationListUtils;

    @InjectMocks
    private AHUserFilter ahUserFilter = new AHUserFilter();
    
    @org.mockito.Mock
    private ConfigurableEnvironment envt;
    
    @MockBean
    private AELockUtil aeLockUtil;
    private MessageAppContainer messageAppContainer = new MessageAppContainer();

    
    @org.mockito.Mock
    private CbaDataUtil cbaDataUtil;
    
    @MockBean
    private ObjectMapper mapper;
    
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
    
    // fix for find bug
    //AHUserMapperUtil AHUserMapperUtil = new AHUserMapperUtil();
	
	@BeforeEach
	public void setup() throws Exception {
		AHUser u = new AHUser();
		u.setLocale("en_US");

		
		ChannelRequestData crd = new ChannelRequestData();
		Map<String, String> ud = new HashMap<String, String>();
		ud.put("IS_HIDE_PRIMARY_ACCOUNT_IN_MAS_DROP_DOWN", "false");
		ud.put("originator", "");
		ud.put("com.aonhewitt.upoint.psp.integration.masrolecd", "P_LR");
		ud.put("MAS_CURRENT_REQUESTED_ACTIVE_ACCT_TYPE", "TBA");
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
	}

    @Test
    public void testDoFilterInternalPositiveScenarioWhenTokenIsInHeader() throws ServletException, IOException {
    	Mockito.when(configurationListUtils.isRedisCallFlag(Mockito.any(),Mockito.any())).thenReturn(true);
    	MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("alightSessionToken", alightSessionToken);
        request.addHeader("alightRequestHeader", alightRequestHeader);
        request.addHeader("UseSecondaryDataCache", true);
        Mockito.when(envt.getProperty(Mockito.any())).thenReturn("true");
        request.setRequestURI(testUri);
        MockHttpServletResponse response = new MockHttpServletResponse();
        MockFilterChain filterChain = new MockFilterChain();
       /* new MockUp<AELockUtil>() {
			@mockit.Mock
			public  boolean isAELockEnabled(){
				return false;
			}
		};*/
       // new MockAELockFalse();
        try (MockedStatic<AELockUtil> aELockUtilMockedStatic = mockStatic(AELockUtil.class)) {
			aELockUtilMockedStatic.when(() -> AELockUtil.isAELockEnabled()).thenReturn(false);
	    doNothing().when(cbaDataUtil).buildCbaSystemData(Mockito.any(MessageAppContainer.class));
        ahUserFilter.doFilter(request, response, filterChain);
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        }
    }
    
    @Test
    public void testDoFilterWithSecondaryDataCache() throws ServletException, IOException {
		assertThrows(Exception.class, () -> {
			MockHttpServletRequest request = new MockHttpServletRequest();
			request.addHeader("UseSecondaryDataCache", "true");
			request.setRequestURI(testUri);
			MockHttpServletResponse response = new MockHttpServletResponse();
			MockFilterChain filterChain = new MockFilterChain();
			//Removed for PC
			//Mockito.when(envt.getProperty(Mockito.any())).thenReturn("true");
			/*new MockUp<AELockUtil>() {
				@mockit.Mock
				public  boolean isAELockEnabled(){
					return false;
				}
			};*/
			new MockAELockFalse();
			//Removed for PC
		//doNothing().when(cbaDataUtil).buildCbaSystemData(Mockito.any(MessageAppContainer.class));
		ahUserFilter.doFilter(request, response, filterChain);
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
		ahUserFilter.doFilter(request, response, filterChain);
			assertEquals(HttpStatus.OK.value(), response.getStatus());

		});

	}
    @Test
    public void testDoFilterWithRedisAlias() throws ServletException, IOException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("SecondaryRedisAlias", "default");
        request.addHeader("alightSessionToken", alightSessionToken);
        request.addHeader("alightRequestHeader", alightRequestHeader);
        request.addHeader("alightColleagueSessionToken", alightRequestHeader);
        request.setRequestURI(testUri);
        MockHttpServletResponse response = new MockHttpServletResponse();
        MockFilterChain filterChain = new MockFilterChain();
		Mockito.when(envt.getProperty(Mockito.any())).thenReturn("true");
		//Mockito.when(methodCall)
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
		commonUtilsMockedStatic.when(() -> CommonUtils.getAliasList()).thenReturn(list);

		
		
		try (MockedStatic<AELockUtil> aELockUtilMockedStatic = mockStatic(AELockUtil.class);
				MockedStatic<AHUserMapperUtil> aHUserMapperUtilMockedStatic = mockStatic(AHUserMapperUtil.class)) {
			
			aHUserMapperUtilMockedStatic.when(() -> AHUserMapperUtil.processAlightSessionPayload(Mockito.any(String.class),Mockito.any(String.class),Mockito.any(MessageAppContainer.class)))
			.thenReturn(messageAppContainer);
			aELockUtilMockedStatic.when(() -> AELockUtil.isAELockEnabled()).thenReturn(false);
		doNothing().when(cbaDataUtil).buildCbaSystemData(Mockito.any(MessageAppContainer.class));
		MockedStatic<AppContainerProvider> mockedApp = Mockito.mockStatic(AppContainerProvider.class);
		mockedApp.when(()-> AppContainerProvider.getContainer()).thenReturn(messageAppContainer);
        ahUserFilter.doFilter(request, response, filterChain);
        assertEquals(HttpStatus.OK.value(), response.getStatus());
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
			ahUserFilter.doFilter(request, response, filterChain);
			assertEquals(HttpStatus.OK.value(), response.getStatus());

		});

	}
    @Test
    public void testDoFilterError() throws ServletException, IOException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("alightSessionToken", alightPersonSessionToken);
       
       
        MockHttpServletResponse response = new MockHttpServletResponse();
        MockFilterChain filterChain = new MockFilterChain();
		Mockito.when(envt.getProperty(Mockito.any())).thenReturn("true");
		/*new MockUp<AELockUtil>() {
			@mockit.Mock
			public  boolean isAELockEnabled(){
				return true;
			}
		}; */
		//new MockAELockTrue();
		List<String> list = new ArrayList<String>();
		list.add("default");
		commonUtilsMockedStatic.when(() -> CommonUtils.getAliasList()).thenReturn(list);

		try (MockedStatic<AELockUtil> aELockUtilMockedStatic = mockStatic(AELockUtil.class)) {
			aELockUtilMockedStatic.when(() -> AELockUtil.isAELockEnabled()).thenReturn(true);
		doNothing().when(cbaDataUtil).buildCbaSystemData(Mockito.any(MessageAppContainer.class));
        ahUserFilter.doFilter(request, response, filterChain);
       assertEquals(HttpStatus.OK.value(), response.getStatus());
		}
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
		}; */
		//new MockAELockFalse();

		try (MockedStatic<AELockUtil> aELockUtilMockedStatic = mockStatic(AELockUtil.class)) {
			aELockUtilMockedStatic.when(() -> AELockUtil.isAELockEnabled()).thenReturn(false);
		doNothing().when(cbaDataUtil).buildCbaSystemData(Mockito.any(MessageAppContainer.class));
        ahUserFilter.doFilter(request, response, filterChain);
      
		}
    }
    @Test
    public void testDoFilterWithoutToken() throws ServletException, IOException {
		assertThrows(Exception.class, () -> {
			MockHttpServletRequest request = new MockHttpServletRequest();
			request.addHeader("alightRequestHeader", alightRequestHeader);
			request.addHeader("UseSecondaryDataCache", true);
			request.addHeader("sessionId", "1223");

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
			AHUserFilter.getSessionId(request, alightSessionToken);
			ahUserFilter.doFilter(request, response, filterChain);


			assertEquals(HttpStatus.OK.value(), response.getStatus());

		});

	}
    
    @Test
    public void testSessionIdFail() throws ServletException, IOException {
    	MockHttpServletRequest request = new MockHttpServletRequest();
        
        request.addHeader("alightRequestHeader", alightRequestHeader);
        request.addHeader("UseSecondaryDataCache", true);
        try (MockedConstruction<ObjectMapper> objectMapperMockedConstruction = mockConstruction(ObjectMapper.class,
				(objectMapper, context) -> {
					doThrow(new NullPointerException()).when(objectMapper).readTree(Mockito.any(String.class));
				})) {
			request.addHeader("alightRequestHeader", alightRequestHeader);
			request.addHeader("UseSecondaryDataCache", true);

			AHUserFilter.getSessionId(request, alightSessionToken);

		}
    	
       
      
        
    }
    
    @Test
    public void testSessionIdPass() throws ServletException, IOException {
    	
    	MockHttpServletRequest request = new MockHttpServletRequest();
        
        request.addHeader("alightRequestHeader", alightRequestHeader);
        request.addHeader("UseSecondaryDataCache", true);
       
    	
       AHUserFilter.getSessionId(request, alightSessionToken);
       
      
        
    }
    @Test
    public void testDoFilterPreAuth() throws ServletException, IOException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("alightRequestHeader", alightRequestHeaderPreAuth);
        request.setRequestURI(preAuthUri);
        MockHttpServletResponse response = new MockHttpServletResponse();
        MockFilterChain filterChain = new MockFilterChain();
       /* new MockUp<AELockUtil>() {
			@mockit.Mock
			public  boolean isAELockEnabled(){
				return true;
			}
		};*/
        new MockAELockTrue();
		 doNothing().when(cbaDataUtil).buildCbaSystemData(Mockito.any(MessageAppContainer.class));
        ahUserFilter.doFilter(request, response, filterChain);
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        
    }
    @Test
    public void testDoFilterisRedisFlag() throws ServletException, IOException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("alightRequestHeader", alightRequestHeaderPreAuth);
        request.setRequestURI(preAuthUri);
        MockHttpServletResponse response = new MockHttpServletResponse();
        MockFilterChain filterChain = new MockFilterChain();
        Map<String, String> logconfigstoragemap= new HashMap<>();
        logconfigstoragemap.put(UpointLogConstants.REDIS_CALL_FLAG, "true");
        Mockito.when(configurationListUtils.isRedisCallFlag(Mockito.any(),Mockito.any())).thenReturn(true);
        try (MockedStatic<AELockUtil> aELockUtilMockedStatic = mockStatic(AELockUtil.class);
        		MockedStatic<LogEventDataHandler> logEventDataHandlerMockedStatic = mockStatic(LogEventDataHandler.class)) {
			aELockUtilMockedStatic.when(() -> AELockUtil.isAELockEnabled()).thenReturn(true);
			logEventDataHandlerMockedStatic.when(() -> LogEventDataHandler.getLogconfigstoragemap()).thenReturn(logconfigstoragemap);
		 doNothing().when(cbaDataUtil).buildCbaSystemData(Mockito.any(MessageAppContainer.class));
        ahUserFilter.doFilter(request, response, filterChain);
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        }  
    }
    
    @Test
    public void testDoFilterEmptycontailner() throws ServletException, IOException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("alightRequestHeader", alightRequestHeaderPreAuth);
        request.setRequestURI(preAuthUri);
        MockHttpServletResponse response = new MockHttpServletResponse();
        MockFilterChain filterChain = new MockFilterChain();
        AppContainerProvider appContainerProvider=mock(AppContainerProvider.class);
         appContainerProvider.setContainer(null);
         
		 doNothing().when(cbaDataUtil).buildCbaSystemData(Mockito.any(MessageAppContainer.class));
        ahUserFilter.doFilter(request, response, filterChain);
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        }  
    
}