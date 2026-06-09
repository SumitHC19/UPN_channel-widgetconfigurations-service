package com.aonhewitt.upoint.filter;

import java.io.IOException;
import java.util.Map;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.ConfigurableEnvironment;

import com.alight.logging.constant.UpointLogConstants;
import com.alight.logging.helpers.LogEventDataHandler;
import com.aonhewitt.exceptions.AHBaseRunTimeException;
import com.aonhewitt.logging.events.DebugLogEvent;
import com.aonhewitt.logging.events.ErrorLogEvent;
import com.aonhewitt.logging.helpers.DebugLogEventHelper;
import com.aonhewitt.logging.helpers.ErrorLogEventHelper;
import com.aonhewitt.logging.helpers.LogEventUtil;
import com.aonhewitt.portal.core.util.UserUtil;
import com.aonhewitt.portal.util.AHUserMapperUtil;
import com.aonhewitt.portal.util.AppContainerProvider;
import com.aonhewitt.portal.util.MessageAppContainer;
import com.aonhewitt.portal.util.ThreadLocalUtil;
import com.aonhewitt.upoint.cache.util.RedisCacheConfigUtil;
import com.aonhewitt.upoint.cba.service.util.CbaDataUtil;
import com.aonhewitt.upoint.util.AELockUtil;
import com.aonhewitt.upoint.util.CommonUtils;
import com.aonhewitt.upoint.util.ConfigurationListUtils;
import com.aonhewitt.upoint.util.PreAuthUtil;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.util.ObjectUtils;
/**
 * 
 * @author Yogesh Mittal
 *
 */

@WebFilter(urlPatterns = { "/channel/configurationList/*" })
public class ConfigurationListFilter implements Filter {
	
	@Autowired
	private ConfigurationListUtils configurationListUtils;
	
	@Autowired
	private CbaDataUtil cbaDataUtil;
	
	@Autowired
	private ConfigurableEnvironment envt;
	

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		boolean isRedisFlag=false;
		String redisCallFlag=null;
		try{
			String sessionToken = null;
		
			long startTime = System.currentTimeMillis();
			
			HttpServletRequest httpRequest = ((HttpServletRequest) request);			
			String url = httpRequest.getRequestURI();
			/**
			 * Initialize message app container
			 */
			MessageAppContainer messageAppContainer = AppContainerProvider.getContainer();
			if (ObjectUtils.isEmpty(messageAppContainer)) {
				messageAppContainer = new MessageAppContainer();
				/**
				 * Set Message App container
				 */
				AppContainerProvider.setContainer(messageAppContainer);
			}
			/**
			 * Process Alight session token - 
			 * Yogesh - TODO - verify Ahuser with for actual payload
			 */
			String asgSessionToken = httpRequest.getHeader("ASG-SessionToken");
			String alightRequestHeader = httpRequest.getHeader("alightRequestHeader");
			
			Map<String, String> logconfigstoragemap = LogEventDataHandler.getLogconfigstoragemap();
			if(!ObjectUtils.isEmpty(logconfigstoragemap)){
				redisCallFlag = logconfigstoragemap.get(UpointLogConstants.REDIS_CALL_FLAG);
				isRedisFlag=configurationListUtils.isRedisCallFlag(httpRequest,logconfigstoragemap);
			}
			
			if(PreAuthUtil.isPreAuthUrl(url)){
				// pre auth
				PreAuthUtil.populateMessageContainer(alightRequestHeader);
	
			}
			else
			{
			if (!ObjectUtils.isEmpty(asgSessionToken)) {
				buildAHUserFromRequestToken(asgSessionToken, alightRequestHeader, messageAppContainer);
			} else {
				 sessionToken = getSessionToken(httpRequest,messageAppContainer);//httpRequest.getHeader("alightSessionToken");
				if(ObjectUtils.isEmpty(sessionToken)){
					ErrorLogEventHelper.logErrorEvent(AHUserFilter.class.getName(), "alightSessionToken is null:", 
							"buildAHUserFromRequestToken()",
							"", "", ErrorLogEvent.ERROR_SEVERITY);
					
					throw new AHBaseRunTimeException("alightSessionToken is empty and is not passed to service by gateway.");
				}
				buildAHUserFromRequestToken(sessionToken, alightRequestHeader, messageAppContainer);
			}
			}
			
			
			

			/**
			 * No need to pass client id from header as this is build during the creation of AHuser.
			 * 
			 * clientId sent with header will override what is being sent under alightSessionToken
			 * Process ClientId
			 */		
			//processClientId(response, httpRequest, messageAppContainer);

			
			/**
			 * set header for pre prod instance 
			 */
			String useSecondaryDataCache = httpRequest.getHeader("UseSecondaryDataCache");
			
			String upointSecondaryRedisInstanceEnabled=envt.getProperty("upoint.redis.enabled");
			
			String redisAlias = httpRequest.getHeader("SecondaryRedisAlias");
			
			boolean isSecondaryRedisReq= StringUtils.isNotEmpty(redisAlias) ||(StringUtils.isNotEmpty(useSecondaryDataCache) && useSecondaryDataCache.equalsIgnoreCase("true"));
			
			boolean isSecondaryRedisSetup= StringUtils.isEmpty(upointSecondaryRedisInstanceEnabled)
					|| Boolean.valueOf(upointSecondaryRedisInstanceEnabled) == false;
			
			if(isSecondaryRedisReq &&  isSecondaryRedisSetup)
			{
				
					throw new RuntimeException(
							"Secondary Redis  is not configured and caller is requesting data from secondary redis instance");
				
			}
			
			if (StringUtils.isNotEmpty(redisAlias) && !(CommonUtils.getAliasList().contains(redisAlias.toLowerCase()))) {
				throw new RuntimeException("Secondary Redis instance with alias :" + redisAlias
						+ " is either not configured or is enabled after server startup,you need to start server again.");
			}
			
			RedisCacheConfigUtil.setHeadersinRequest(redisAlias, useSecondaryDataCache);
			
			/*if(StringUtils.isNotEmpty(redisAlias)){
				
				if(StringUtils.isEmpty(upointSecondaryRedisInstanceEnabled) ||Boolean.valueOf(upointSecondaryRedisInstanceEnabled)==false ){
					
					throw new RuntimeException ("Multi Redis Instance is not configured and caller is requesting data from secondary redis instance");
				}
				messageAppContainer.setSecondaryRedisAlias(redisAlias.toLowerCase());
				messageAppContainer.setUseSecondaryDataCache(true);
			}
		
			
			else if(StringUtils.isNotEmpty(useSecondaryDataCache) && useSecondaryDataCache.equalsIgnoreCase("true")){
				
				if(StringUtils.isEmpty(upointSecondaryRedisInstanceEnabled) ||Boolean.valueOf(upointSecondaryRedisInstanceEnabled)==false ){
					
					throw new RuntimeException ("Secondary Redis Instance is not configured and caller is requesting data from secondary redis instance");
				}
				messageAppContainer.setUseSecondaryDataCache(true);
			}*/

			cbaDataUtil.buildCbaSystemData(messageAppContainer);
			chain.doFilter(request, response);			
			long endTime = System.currentTimeMillis();
			String message = "*****" + httpRequest.getRequestURI() + " ---  Start Time =" + startTime + " , Stop Time = " + endTime + " , Total Time = " + (endTime-startTime) + "mili seconds. MethodName:: doFilter()";
			//PCA-6284
			DebugLogEvent event = new DebugLogEvent();
	        event.setDefaults();
	        LogEventUtil.populateCommonFields(event);	        	        
	        event.setMessage(message);	        	        
	        DebugLogEventHelper.logDebugEvent(event);
	        if(messageAppContainer!=null && messageAppContainer.getAttribute(CommonUtils.NON_SECURE_REQUEST_BODY_ATTRIBUTES_MESSAGE)!=null) {
				CommonUtils.displayCustomErrorMessage(httpRequest, response, CommonUtils.getNonSecureErroMessage());
			}
		}finally{
			//UFD-20568 -- Expression Logging
			configurationListUtils.logExpCallCountInfoLog();
			//UFD-20568 Expression Logging Ends
            ThreadLocalUtil.cleanThreadlocalVariables();
			
			if(isRedisFlag){
				configurationListUtils.logRedisCallCountInfoLog();
				LogEventDataHandler.getLogconfigstoragemap().put(UpointLogConstants.REDIS_CALL_FLAG,redisCallFlag);
			}
		}
	}
/*
	private void processClientId(ServletResponse response, HttpServletRequest httpRequest,
			MessageAppContainer messageAppContainer) throws ServletException, IOException {
		String clientId = httpRequest.getHeader("clientId");
		if (clientId == null) {
			com.aonhewitt.upoint.util.CommonUtils.displayCustomErrorMessage(httpRequest, response, "Missing 'clientId' under header");
			return; // this is needed else we get response already committed error
		} else {
			String lineage = (clientId.isEmpty() || clientId.equals("1.0")) ? "1.0" : clientId + "_1.0";
			if (!lineage.equals("1.0") && !JNDIRepository.getsupportedLineages().contains(lineage)) {
				// check if client id is in supported lineage List -
				com.aonhewitt.upoint.util.CommonUtils.displayCustomErrorMessage(httpRequest, response, "clientId-" + clientId + "  is not configured.");
				return; // this is needed else we get response already committed error
			}
			messageAppContainer.setLineage(lineage);
			messageAppContainer.setPrimaryClientId(clientId);
		}
	}
*/
	private String getSessionToken(HttpServletRequest httpRequest,MessageAppContainer messageAppContainer) {
		String sessionToken = null;
		
		if (!AELockUtil.isAELockEnabled()){
			sessionToken = httpRequest.getHeader("alightPersonSessionToken");
			
			if(StringUtils.isNotBlank(sessionToken)){
				messageAppContainer.setAlightPersonSessionToken(true);
			}
		}
		
        if(StringUtils.isBlank(sessionToken) ){
			sessionToken=httpRequest.getHeader("alightColleagueSessionToken");
			if(StringUtils.isNotBlank(sessionToken)){
				messageAppContainer.setAlightColleagueSessionToken(true);
			}
		}
		if(StringUtils.isBlank(sessionToken) ){
			sessionToken=httpRequest.getHeader("alightSessionToken");
		}
		
		return sessionToken;
	}
	
	private void buildAHUserFromRequestToken(String alightSessionToken, String alightRequestHeader, MessageAppContainer messageAppContainer ) {
		if (!ObjectUtils.isEmpty(alightSessionToken)) {
			try {
				AHUserMapperUtil.processAlightSessionPayload(alightSessionToken, alightRequestHeader, messageAppContainer);
				// following method is applicable for channel-widgetconfigurations-service only. It has dependency on configManager
				// Please donot use it for any other service.
				messageAppContainer.getAhUser().setUUID(UserUtil.createUUID());
			} catch (Exception e) {
				ErrorLogEventHelper.logErrorEvent(ConfigurationListFilter.class.getName(), "Exception : "+ e.getMessage(), 
						"buildAHUserFromRequestToken()",
						e, "", ErrorLogEvent.ERROR_SEVERITY);
			}
		} else {
			// fix for find bug
			ErrorLogEventHelper.logErrorEvent(ConfigurationListFilter.class.getName(), "alightSessionToken is null:", 
					"buildAHUserFromRequestToken()",
					"", "", ErrorLogEvent.ERROR_SEVERITY);
		}
	}

	public static String getSessionId(final HttpServletRequest request, final String alightSecureSessionToken) {

		String sessionId = StringUtils.EMPTY;
		
		if(StringUtils.isNotBlank(request.getHeader(UpointLogConstants.SESSION_ID))) {
			sessionId = request.getHeader(UpointLogConstants.SESSION_ID);
			return sessionId;
		}
		
		try {
			if (!ObjectUtils.isEmpty(alightSecureSessionToken)) {

				ObjectMapper mapper = new ObjectMapper();
				JsonNode rootNode = mapper.readTree(alightSecureSessionToken);

				if (!ObjectUtils.isEmpty(rootNode)) {
					JsonNode sessionIdNode = rootNode.path(UpointLogConstants.SESSION_ID);
					if (!ObjectUtils.isEmpty(sessionIdNode) && !sessionIdNode.isMissingNode()) {
						sessionId = sessionIdNode.asText();
					}
				}
			}
			

		} 
		catch (RuntimeException e) {
			ErrorLogEventHelper.logErrorEvent(AHUserFilter.class.getName(), "Error fetching Session Id:", 
					"getSessionId()",
					"", "", ErrorLogEvent.ERROR_SEVERITY);
		}
		
		catch (Exception e) {
			ErrorLogEventHelper.logErrorEvent(AHUserFilter.class.getName(), "Error fetching Session Id:", 
					"getSessionId()",
					"", "", ErrorLogEvent.ERROR_SEVERITY);
		}
		return sessionId;
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub

	}
	
}
