package com.aonhewitt.upoint.util;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mockStatic;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.MockedStatic;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.aonhewitt.beans.GenericRequestBean;
import com.aonhewitt.portal.util.AppContainerProvider;
import com.aonhewitt.portal.util.MessageAppContainer;
import com.fasterxml.jackson.core.JsonProcessingException;

import jakarta.servlet.AsyncContext;
import jakarta.servlet.DispatcherType;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletConnection;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.HttpUpgradeHandler;
import jakarta.servlet.http.Part;

@ExtendWith(SpringExtension.class)
public class CommonUtilsTest {

	private HttpServletRequest request;
//	private HttpServletResponse response;
	
	
	@InjectMocks
	CommonUtils commonUtils;
	@BeforeEach
	public void setup(){
	
	
		request = new HttpServletRequest() {
			
			
			@Override
			public AsyncContext startAsync(ServletRequest servletRequest, ServletResponse servletResponse)
					throws IllegalStateException {
				return null;
			}
			
			@Override
			public AsyncContext startAsync() throws IllegalStateException {
				return null;
			}
			
			@Override
			public void setCharacterEncoding(String env) throws UnsupportedEncodingException {
				
			}
			
			@Override
			public void setAttribute(String name, Object o) {
				
			}
			
			@Override
			public void removeAttribute(String name) {
				
			}
			
			@Override
			public boolean isSecure() {
				return false;
			}
			
			@Override
			public boolean isAsyncSupported() {
				return false;
			}
			
			@Override
			public boolean isAsyncStarted() {
				return false;
			}
			
			@Override
			public ServletContext getServletContext() {
				return null;
			}
			
			@Override
			public int getServerPort() {
				return 0;
			}
			
			@Override
			public String getServerName() {
				return null;
			}
			
			@Override
			public String getScheme() {
				return null;
			}
			
			@Override
			public RequestDispatcher getRequestDispatcher(String path) {
				return new RequestDispatcher() {
					
					@Override
					public void include(ServletRequest request, ServletResponse response) throws ServletException, IOException {
						// TODO Auto-generated method stub
						
					}
					
					@Override
					public void forward(ServletRequest request, ServletResponse response) throws ServletException, IOException {
						// TODO Auto-generated method stub
						
					}
				};
			}
			
			@Override
			public int getRemotePort() {
				return 0;
			}
			
			@Override
			public String getRemoteHost() {
				return null;
			}
			
			@Override
			public String getRemoteAddr() {
				return null;
			}
			
			public String getRealPath(String path) {
				return null;
			}
			
			@Override
			public BufferedReader getReader() throws IOException {
				return null;
			}
			
			@Override
			public String getProtocol() {
				return null;
			}
			
			@Override
			public String[] getParameterValues(String name) {
				String[] result =null;
				return result;
			}
			
			@Override
			public Enumeration<String> getParameterNames() {
				return null;
			}
			
			@Override
			public Map<String, String[]> getParameterMap() {
				return Collections.emptyMap();
			}
			
			@Override
			public String getParameter(String name) {
				return null;
			}
			
			@Override
			public Enumeration<Locale> getLocales() {
				return null;
			}
			
			@Override
			public Locale getLocale() {
				return null;
			}
			
			@Override
			public int getLocalPort() {
				return 0;
			}
			
			@Override
			public String getLocalName() {
				return null;
			}
			
			@Override
			public String getLocalAddr() {
				return null;
			}
			
			@Override
			public ServletInputStream getInputStream() throws IOException {
				return null;
			}
			
			@Override
			public DispatcherType getDispatcherType() {
				return null;
			}
			
			@Override
			public String getContentType() {
				return null;
			}
			
			@Override
			public long getContentLengthLong() {
				return 0;
			}
			
			@Override
			public int getContentLength() {
				return 0;
			}
			
			@Override
			public String getCharacterEncoding() {
				return null;
			}
			
			@Override
			public Enumeration<String> getAttributeNames() {
				return null;
			}
			
			@Override
			public Object getAttribute(String name) {
			
				if("clientId".equals(name)){
					
					return "19920";
				}else if("CustomProcessingError".equals(name)){
					return "true";
				}
				else {
					return null;
				}
			}
			
			@Override
			public AsyncContext getAsyncContext() {
				
				return null;
			}
			
			@Override
			public <T extends HttpUpgradeHandler> T upgrade(Class<T> handlerClass) throws IOException, ServletException {
				
				return null;
			}
			
			@Override
			public void logout() throws ServletException {
				
				
			}
			
			@Override
			public void login(String username, String password) throws ServletException {
				
				
			}
			
			@Override
			public boolean isUserInRole(String role) {
				
				return false;
			}
			
			@Override
			public boolean isRequestedSessionIdValid() {
				
				return false;
			}
			
			public boolean isRequestedSessionIdFromUrl() {
				
				return false;
			}
			
			@Override
			public boolean isRequestedSessionIdFromURL() {
				
				return false;
			}
			
			@Override
			public boolean isRequestedSessionIdFromCookie() {
				
				return false;
			}
			
			@Override
			public Principal getUserPrincipal() {
				
				return null;
			}
			
			@Override
			public HttpSession getSession(boolean create) {
				
				return null;
			}
			
			@Override
			public HttpSession getSession() {
				
				return null;
			}
			
			@Override
			public String getServletPath() {
				
				return null;
			}
			
			@Override
			public String getRequestedSessionId() {
				
				return null;
			}
			
			@Override
			public StringBuffer getRequestURL() {
				
				return null;
			}
			
			@Override
			public String getRequestURI() {
				
				return null;
			}
			
			@Override
			public String getRemoteUser() {
				
				return null;
			}
			
			@Override
			public String getQueryString() {
				
				return null;
			}
			
			@Override
			public String getPathTranslated() {
				
				return null;
			}
			
			@Override
			public String getPathInfo() {
				
				return null;
			}
			
			@Override
			public Collection<Part> getParts() throws IOException, ServletException {
				return Collections.emptyList();
			}
			
			@Override
			public Part getPart(String name) throws IOException, ServletException {
				
				return null;
			}
			
			@Override
			public String getMethod() {
				
				return null;
			}
			
			@Override
			public int getIntHeader(String name) {
				
				return 0;
			}
			
			@Override
			public Enumeration<String> getHeaders(String name) {
				
				return null;
			}
			
			@Override
			public Enumeration<String> getHeaderNames() {
				
				return null;
			}
			
			@Override
			public String getHeader(String name) {
				
				return null;
			}
			
			@Override
			public long getDateHeader(String name) {
				
				return 0;
			}
			
			@Override
			public Cookie[] getCookies() {
				Cookie[] result = null;
				return result;
			}
			
			@Override
			public String getContextPath() {
				
				return null;
			}
			
			@Override
			public String getAuthType() {
				
				return null;
			}
			
			@Override
			public String changeSessionId() {
				
				return null;
			}
			
			@Override
			public boolean authenticate(HttpServletResponse response) throws IOException, ServletException {
				
				return false;
			}

			@Override
			public String getRequestId() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public String getProtocolRequestId() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public ServletConnection getServletConnection() {
				// TODO Auto-generated method stub
				return null;
			}
		};

	}
	// fix for find bug
	@Test
	public void testGetRequestAttributeString(){
		
		String clientId = CommonUtils.getRequestAttributeString(request, "clientId");
		assertTrue("19920".equals(clientId));
		
		String role = CommonUtils.getRequestAttributeString(request, "role");
		assertTrue("".equals(role));
		
		
	}
	
	@Test
	public void testDisplayCustomErrorMessage() throws ServletException, IOException{	
		CommonUtils.displayCustomErrorMessage(request, null, "");
		assertTrue("true".equals(request.getAttribute("CustomProcessingError")));		
	}

	@Test
	public void testGetAliasList() {
		CommonUtils.getAliasList();
	}
	
	@Test
	public void testvalidateRequestForXSSAttacks() throws JsonProcessingException {
		GenericRequestBean requestBean= new GenericRequestBean();
		requestBean.setLineage("<script>");
		
		try (MockedStatic<AppContainerProvider> appContainerProviderContextMockedStatic = mockStatic(
				AppContainerProvider.class)) {
			appContainerProviderContextMockedStatic
					.when(() -> AppContainerProvider.getContainer())
					.thenReturn(new MessageAppContainer());
			assertNotNull(commonUtils.validateRequestForXSSAttacks(requestBean));
			
	}
		
	}
	
	@Test
	public void testgetNonSecureErroMessage() {
	
		assertNotNull(commonUtils.getNonSecureErroMessage());
	}
	
	@Test
	public void testgetGlobalPersonIdentifier() {
		List<Map<Object, Object>> expressionsMapList= new ArrayList<>();
		Map<Object, Object> exprMapData= new LinkedHashMap<>();
		exprMapData.put("WLF_PENDO_TRACKING_ENABLED", true);
		
		Map<Object, Object> exprMap= new LinkedHashMap<>();
		exprMap.put("feature_uce", exprMapData);
		exprMap.put("WLF_PENDO_TRACKING_ENABLED", true);
		expressionsMapList.add(exprMap);
		commonUtils.getGlobalPersonIdentifier(expressionsMapList);
		/*ApplicationContext applicationContext= mock(ApplicationContext.class);
		try (MockedStatic<FeignApplicationContext> feignApplicationContextMockedStatic = mockStatic(
				FeignApplicationContext.class)) {
			feignApplicationContextMockedStatic
					.when(() -> FeignApplicationContext.getAppContext())
					.thenReturn(applicationContext);
			commonUtils.getGlobalPersonIdentifier(expressionsMapList);
			
		}*/
	}
	
	@Test
	public void getNonSecureToeknListTest() {
		assertNotNull(commonUtils.getNonSecureToeknList());		
	}
}