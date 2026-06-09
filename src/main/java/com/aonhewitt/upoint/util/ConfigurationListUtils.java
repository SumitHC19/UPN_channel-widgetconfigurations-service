package com.aonhewitt.upoint.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.naming.InitialContext;
import jakarta.servlet.http.HttpServletRequest;
import javax.sql.DataSource;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.alight.cloud.data.util.HROCacheMap;
import com.alight.dynamodb.DynamoDbHelper;
import com.alight.filter.UpointLoggingFilter;
import com.alight.logging.constant.UpointLogConstants;
import com.alight.logging.dto.RedisCallCountStorage;
import com.alight.logging.events.CommonTypeLogEvent;
import com.alight.logging.helpers.InfoTypeLogEventHelper;
import com.alight.logging.helpers.LogInheritableThreadLocal;
import com.alight.logging.logbook.LogbookPropertiesConfiguration;
import com.aonhewitt.beans.CacheKeys;
import com.aonhewitt.beans.ComponentRequestBean;
import com.aonhewitt.beans.GenericRequestBean;
import com.aonhewitt.beans.UPCKey;
import com.aonhewitt.logging.events.ErrorLogEvent;
import com.aonhewitt.logging.helpers.ErrorLogEventHelper;
import com.aonhewitt.portal.adapter.AdapterService;
import com.aonhewitt.portal.core.service.util.CmServicesUtil;
import com.aonhewitt.portal.core.util.UserUtil;
import com.aonhewitt.portal.hasbro.rest.utils.UDPUriResourceHandler;
import com.aonhewitt.portal.hasbro.udp.base.service.UDPBaseServices;
import com.aonhewitt.portal.hasbro.udp.base.service.pojo.UDPResponse;
import com.aonhewitt.portal.service.upc.util.UPCMemoryManagerClientCacheUtil;
import com.aonhewitt.portal.service.upc.util.UPCReqCacheUtil;
import com.aonhewitt.portal.udp.util.UDPBrokerHeaderUtil;
import com.aonhewitt.portal.userprovisioning.beans.AHUser;
import com.aonhewitt.portal.util.AppContainerProvider;
import com.aonhewitt.portal.util.MessageAppContainer;
import com.aonhewitt.portal.util.RestServiceInvocationResult;
import com.aonhewitt.portal.util.SessionTokenParserUtil;
import com.aonhewitt.upoint.cache.config.RedisCacheConstants;
import com.aonhewitt.upoint.cache.config.provider.ICache;
import com.aonhewitt.upoint.cache.config.provider.impl.RedisCacheMapProviderImpl;
import com.aonhewitt.upoint.cache.exception.BaseCacheException;
import com.aonhewitt.upoint.cache.util.DistributedCacheUtil;
import com.aonhewitt.upoint.cache.util.ShortCacheUtil;
import com.aonhewitt.upoint.core.service.util.ExpressionDebugAssistInfo;
import com.aonhewitt.upoint.core.service.util.ExpressionDebugAssistUtil;
import com.aonhewitt.upoint.db.JNDIRepository;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.util.ObjectUtils;
//import redis.clients.jedis.JedisShardInfo;


/**
 * 
 * @author Yogesh M
 * 
 *         This Controller exposed Utility methods for Debugging. This feature
 *         available only under lower lifecycle
 *
 */
@Component
public class ConfigurationListUtils {
	
	@Autowired
	CmServicesUtil cmServicesUtil;
	
	private ICache redisCacheProvider;
	
	@Autowired(required=false)
	@Qualifier(RedisCacheConstants.SESSION_REDIS_TEMPLATE_OBJECT)
	RedisTemplate<String, Object> sessionRedisTemplateForObject;
	
	@Autowired
	@Qualifier("sessionRedisCacheObjectProvider")
	ICache sessionRedisCacheProvider;
	
	@Autowired
	@Qualifier("redisCacheMapProvider")
	RedisCacheMapProviderImpl redisCompleteCacheMapProvider;
	
	@Autowired
	DynamoDbHelper dynamoDbHelper;
	
	@Autowired
	LogbookPropertiesConfiguration logbookPropertiesConfiguration;
	
	@Value("${spring.session-redis.enabled:false}")
	private boolean sessionRedisEnable;

	@Value("${spring.profiles.active}")
	String profile;
	
	private RedisTemplate<String, Object> redisTemplateForObject;
	
	private static final String ERROR_MSG1 = "Bad Request - Invalid or Empty JSON input request & Missing ASGToken header";
	
	private static final String EXP_CALL_COUNT_EVENT_TYPE = "INFO_EXP_CALL_COUNT_POOL";
	private static final String EXP_TREE_MAP_EVENT_TYPE = "INFO_TREEMAP";
	private static final String EXP_VS_PPT_EVENT_TYPE = "INFO_EXP_VS_PPT";

	
	public ConfigurationListUtils(@Qualifier("redisCacheObjectProvider") ICache aRedisCacheProvider,@Qualifier("redisTemplateforObject") RedisTemplate aRedisTemplateforObject) {
		this.redisCacheProvider = aRedisCacheProvider;
		this.redisTemplateForObject=aRedisTemplateforObject;
	}

	public  UDPResponse getUdpData(){
	
		UDPResponse response = UDPBaseServices.getUdpPersonSchemaObject();
		return response;
	}
	public Map<String, Object> getAhUserObject() {
		Map<String, Object> map = new HashMap<String, Object>();
		MessageAppContainer msgAppContainer = AppContainerProvider.getContainer();
		AHUser ahUser = msgAppContainer.getAhUser();
		if (!ObjectUtils.isEmpty(ahUser)) {
			if (!ObjectUtils.isEmpty(ahUser.toString())) {
				String ahUserString = ahUser.toString().replaceAll("\n", "");
				map.put("AhUserObject", ahUserString);
				map.put("UUID", ahUser.getUUID());
				if (!ObjectUtils.isEmpty(msgAppContainer.getIdMapping())) {
					map.put("IdMapping", msgAppContainer.getIdMapping().replaceAll("\n", ""));
				}
			}
		} else {
			// fix for find bug issue
			map.put("AhUserObject", "AhUser is null");
		}
		return map;
	}

	public Map<String, Object> getRequestPayload() {
		Map<String, Object> map = new HashMap<String, Object>();

		MessageAppContainer msgAppContainer = AppContainerProvider.getContainer();
		if (!ObjectUtils.isEmpty(msgAppContainer.getIdMapping())) {
			map.putAll(msgAppContainer.getRequestHeaders());
			map.put("IdMapping", msgAppContainer.getIdMapping());
		}
		AHUser ahUser = msgAppContainer.getAhUser();
		if (!ObjectUtils.isEmpty(ahUser)) {
			if (!ObjectUtils.isEmpty(ahUser.toString())) {
				String ahUserString = ahUser.toString().replaceAll("\n", "");
				map.put("AhUserObject", ahUserString);
			}
		} else {
			// fix for find bug issue
			map.put("AhUserObject", "AhUser is null");
		}
		return map;
	}

	/*
	public Map getGMCMessages(HttpServletRequest httpServletRequest) {
		ASGSessionToken asgSessionTokenObject = (ASGSessionToken) httpServletRequest
				.getAttribute("ASG-SessionTokenObject");

		if (asgSessionTokenObject != null) {
			try {
				RestServiceInvocationResult serviceResponseResult = null;
				serviceResponseResult = messageEngineUtil.getAllMessages(
						MessageEngineUtil.getLineageFromAsgToken(asgSessionTokenObject), asgSessionTokenObject, false);
				if (RestServiceInvocationResult.ResponseStatusEnum.SUCCESS == serviceResponseResult
						.getResponseStatus()) {
					GMCMsgView msgView = GMCMsgViewFactory.getGMCMsgView(asgSessionTokenObject);
					return msgView.processResponse(serviceResponseResult);
				} else {
					return serviceResponseResult.getErrorMessage();
				}

			} catch (Exception e) {
				return messageEngineUtil.buildServcieErrorResponseMap("Error", e.getMessage());
			}
		} else {
			return messageEngineUtil.buildServcieErrorResponseMap("Error", ERROR_MSG1);
		}
	}*/

	/*@RequestMapping(value = "personMessages/view/count", produces = "application/json")
	public Map getGMCMessageCount(HttpServletRequest httpServletRequest) {
		ASGSessionToken asgSessionTokenObject = (ASGSessionToken) httpServletRequest
				.getAttribute("ASG-SessionTokenObject");
		if (asgSessionTokenObject != null) {
			try {
				RestServiceInvocationResult serviceResponseResult = null;
				serviceResponseResult = messageEngineUtil.getAllMessageCount(
						MessageEngineUtil.getLineageFromAsgToken(asgSessionTokenObject), asgSessionTokenObject);

				if (RestServiceInvocationResult.ResponseStatusEnum.SUCCESS == serviceResponseResult
						.getResponseStatus()) {
					// PCA-4608
					GMCMsgView msgView = GMCMsgViewFactory.getGMCMsgView(asgSessionTokenObject);
					serviceResponseResult = msgView.getMsgCount(serviceResponseResult);
					return serviceResponseResult.getTotalMsgCountsMap();
				} else {
					return serviceResponseResult.getErrorMessage();
				}

			} catch (Exception e) {
				return messageEngineUtil.buildServcieErrorResponseMap("Error", e.getMessage());
			}
		} else {
			return messageEngineUtil.buildServcieErrorResponseMap("Error", ERROR_MSG1);
		}
	}*/

	/**
	 * Yogesh - This method only for debugging & will be removed
	 * 
	 * @param httpRequest
	 */
	

	/*public void logRequestPayLoad(HttpServletRequest httpRequest) {
		final Enumeration<String> attributeNames = httpRequest.getAttributeNames();

		MessageAppContainer msgAppContainer = AppContainerProvider.getContainer();
		LOGGER.info("---------------------------------- Print Headers ---------------------------\n");
		final Enumeration<String> headerNames = httpRequest.getHeaderNames();
		while (headerNames.hasMoreElements()) {
			String header = headerNames.nextElement();
			String headerVal = httpRequest.getHeader(header);
			
			// fix for PMD
			if (msgAppContainer != null && headerVal != null) {
				//if (headerVal != null) {
					headerVal = httpRequest.getHeader(header);
					msgAppContainer.getRequestHeaders().put(header, headerVal);
				//}
			}
			Log.info(header + "= " + headerVal);
		}

		LOGGER.info("---------------------------------- Print parameters ---------------------------\n");
		final Enumeration<String> parameterNames = httpRequest.getParameterNames();
		while (parameterNames.hasMoreElements()) {
			String parameterName = parameterNames.nextElement();
			Log.info(parameterName + "= " + httpRequest.getParameter(parameterName));
		}

		LOGGER.info("---------------------------------- Print Attributes ---------------------------\n");
		while (attributeNames.hasMoreElements()) {
			String attributeName = attributeNames.nextElement();
			Log.info(attributeName + "= " + httpRequest.getAttribute(attributeName));
		}
		LOGGER.info("--------------------------------------------------------------------------------");
	}*/

	/**
	 * Test all the JNDI configured for any DB connection
	 * 
	 * @returns
	 */
	public String testJNDIConnections() {
		StringBuilder output = new StringBuilder(
				"<h1><font color=\"black\">JNDI RESOURCE LOOK UP STATUS</font></h1><ul>");
		try {
			InitialContext cntxt = new InitialContext();
			//testBaseSchema(output, cntxt);
			//testUCCEBaseSchema(output, cntxt);
			//testClientSchema(output, cntxt);
			//testUCCEClientSchema(output, cntxt);
			testGeoLocationSchema(output, cntxt);
		} catch (Exception e) {
			output.append("<h1>Unable to create initial context for JNDI resource look up with message:")
					.append(e.getMessage()).append("</h1>").append("\n");
		}
		output.append("</ul>");
		return output.toString();
	}

	/**
	 * Process testing of base Schema
	 * 
	 * @param output
	 * @param cntxt
	 */
	/*private void testBaseSchema(StringBuilder output, InitialContext cntxt) {
		Connection con = null;
		PreparedStatement stmt = null;
		String url = JNDIRepository.getDBServerName(System.getProperty("baseDBUrl").trim());
		String defaultSchemaValue = getDefaultSchemaValue(System.getProperty("baseDefaultSchema"));
		String query = "Select count(*) from " + defaultSchemaValue + "BASE_LINK";
		try {
			String jndiName = "java:comp/env/jdbc/base";
			DataSource dataSource = (DataSource) cntxt.lookup(jndiName);
			con = dataSource.getConnection();
			stmt = con.prepareStatement(query);
			stmt.execute();

			output.append("<font color=\"green\"><li>Connection to base DB server <b>" + url
					+ "</b> is successful.</li></font>");
			if (defaultSchemaValue != null && !"".equals(defaultSchemaValue)) {
				output.append("&nbsp;&nbsp;&nbsp;Connection was tested for Schema=" + defaultSchemaValue);
			}
			output.append("<br>");
			output.append("<br>");
		} catch (Exception e) {
			output.append(
					"<font color=\"red\"><li>Connection to base DB server <b>" + url + "</b> has failed with reason:")
					.append(e.getMessage()).append("</li></font>");
			if (defaultSchemaValue != null && !"".equals(defaultSchemaValue)) {
				output.append("&nbsp;&nbsp;&nbsp;Connection was tested for Schema=" + defaultSchemaValue);
			}
			output.append("<br>");
			output.append("<br>");
		} finally {
			closeConnection(con, stmt);
		}
	}*/

	/**
	 * Process testing of UCCE base Schema
	 * 
	 * @param output
	 * @param cntxt
	 */
	/*private void testUCCEBaseSchema(StringBuilder output, InitialContext cntxt) {
		if (System.getProperty("uccebaseDBUrl") != null) {
			Connection con = null;
			PreparedStatement stmt = null;
			String url = UcceJNDIRepository.getDBServerName(System.getProperty("uccebaseDBUrl").trim());

			String defaultSchemaValue = getDefaultSchemaValue(System.getProperty("uccebaseDefaultSchema"));
			String query = "Select count(*) from " + defaultSchemaValue + "BASE_LINK";
			try {
				String jndiName = "java:comp/env/jdbc/ucceBase";
				DataSource dataSource = (DataSource) cntxt.lookup(jndiName);
				con = dataSource.getConnection();
				stmt = con.prepareStatement(query);
				stmt.execute();
				output.append("<font color=\"green\"><li>Connection to UCCE base DB server <b>" + url
						+ "</b> is successful.</li></font>");
				if (defaultSchemaValue != null && !"".equals(defaultSchemaValue)) {
					output.append("&nbsp;&nbsp;&nbsp;Connection was tested for Schema=" + defaultSchemaValue);
				}
				output.append("<br>");
				output.append("<br>");
			} catch (Exception e) {
				output.append("<font color=\"red\"><li>Connection to UCCE base DB server <b>" + url
						+ "</b> has failed with reason:").append(e.getMessage()).append("</li></font>");
				if (defaultSchemaValue != null && !"".equals(defaultSchemaValue)) {
					output.append("&nbsp;&nbsp;&nbsp;Connection was tested for Schema=" + defaultSchemaValue);
				}
				output.append("<br>");
				output.append("<br>");
			} finally {
				closeConnection(con, stmt);
			}
		}
	}*/

	/**
	 * Process testing of ClientSchema
	 * 
	 * @param output
	 * @param cntxt
	 */
	/*private void testClientSchema(StringBuilder output, InitialContext cntxt) {
		Connection con = null;
		PreparedStatement stmt = null;
		String jndiName = "";
		String url = "";
		String defaultSchemaValue = "";
		List<JNDIClientMapping> jNDIClientMappingList = JNDIRepository.getJNDIClientMappingList();
		if (jNDIClientMappingList != null) {
			for (JNDIClientMapping jNDIClientMapping : jNDIClientMappingList) {
				try {
					jndiName = "java:comp/env/" + jNDIClientMapping.getJndiName();
					url = JNDIRepository.getDBServerName(jNDIClientMapping.getDbUrl().trim());
					defaultSchemaValue = getDefaultSchemaValue(jNDIClientMapping.getDefaultSchema());
					String query = "Select count(*) from " + defaultSchemaValue + "LINK";
					DataSource dataSource = (DataSource) cntxt.lookup(jndiName);
					con = dataSource.getConnection();
					stmt = con.prepareStatement(query);
					stmt.execute();
					output.append("<font color=\"green\"><li>Connection to client DB server <b>" + url
							+ "</b> is successful " + jNDIClientMapping.getFilteredClientList() + ".</li></font>");
					if (defaultSchemaValue != null && !"".equals(defaultSchemaValue)) {
						output.append("&nbsp;&nbsp;&nbsp;Connection was tested for Schema=" + defaultSchemaValue);
					}
					output.append("<br>");
					output.append("<br>");
				} catch (Exception e) {
					output.append("<font color=\"red\"><li>Connection to client DB server <b>" + url
							+ "</b> failed with reason:").append(e.getMessage()).append("</li></font>");
					if (defaultSchemaValue != null && !"".equals(defaultSchemaValue)) {
						output.append("&nbsp;&nbsp;&nbsp;Connection was tested for Schema=" + defaultSchemaValue);
					}
					output.append("<br>");
					output.append("<br>");
				} finally {
					closeConnection(con, stmt);
				}
			}
		}
	}/*

	/**
	 * Process testing of UCCE ClientSchema
	 * 
	 * @param output
	 * @param cntxt
	 */
	/*private void testUCCEClientSchema(StringBuilder output, InitialContext cntxt) {
		Connection con = null;
		PreparedStatement stmt = null;
		String jndiName = "";
		String url = "";
		String defaultSchemaValue = "";
		List<JNDIClientMapping> jNDIClientMappingList = UcceJNDIRepository.getJNDIClientMappingList();
		if (jNDIClientMappingList != null) {
			for (JNDIClientMapping jNDIClientMapping : jNDIClientMappingList) {
				try {
					jndiName = "java:comp/env/" + jNDIClientMapping.getJndiName();
					url = UcceJNDIRepository.getDBServerName(jNDIClientMapping.getDbUrl().trim());
					defaultSchemaValue = getDefaultSchemaValue(jNDIClientMapping.getDefaultSchema());
					String query = "Select count(*) from " + defaultSchemaValue + "LINK";
					DataSource dataSource = (DataSource) cntxt.lookup(jndiName);
					con = dataSource.getConnection();
					stmt = con.prepareStatement(query);
					stmt.execute();
					output.append("<font color=\"green\"><li>Connection to Ucce client DB server <b>" + url
							+ "</b> is successful " + jNDIClientMapping.getFilteredClientList() + ".</li></font>");
					if (defaultSchemaValue != null && !"".equals(defaultSchemaValue)) {
						output.append("&nbsp;&nbsp;&nbsp;Connection was tested for Schema=" + defaultSchemaValue);
					}
					output.append("<br>");
					output.append("<br>");
				} catch (Exception e) {
					output.append("<font color=\"red\"><li>Connection to Ucce client DB server <b>" + url
							+ "</b> failed with reason:").append(e.getMessage()).append("</li></font>");
					if (defaultSchemaValue != null && !"".equals(defaultSchemaValue)) {
						output.append("&nbsp;&nbsp;&nbsp;Connection was tested for Schema=" + defaultSchemaValue);
					}
					output.append("<br>");
					output.append("<br>");
				} finally {
					closeConnection(con, stmt);
				}
			}
		}
	}*/

	/**
	 * Process testing of geolocation Schema
	 * 
	 * @param output
	 * @param cntxt
	 */
	private void testGeoLocationSchema(StringBuilder output, InitialContext cntxt) {
		Connection con = null;
		PreparedStatement stmt = null;
		String url = "";
		try {
			if (!ObjectUtils.isEmpty(System.getProperty("geolocationDBUrl"))) {
				url = JNDIRepository.getDBServerName(System.getProperty("geolocationDBUrl").trim());
				String defaultSchemaValue = getDefaultSchemaValue(System.getProperty("geolocationDefaultSchema"));
				String query = "Select count(*) from " + defaultSchemaValue + "GEO_IP_BLOCKS";
				String jndiName = "java:comp/env/jdbc/GeoLocation";
				DataSource dataSource = (DataSource) cntxt.lookup(jndiName);
				con = dataSource.getConnection();
				stmt = con.prepareStatement(query);
				stmt.execute();

				output.append("<font color=\"green\"><li>Connection to geolocation DB server <b>" + url
						+ "</b> is successful.</li></font>");
				output.append("<br>");
				output.append("<br>");
			}
		} catch (Exception e) {
			output.append("<font color=\"red\"><li>Connection to geolocation DB server <b>" + url
					+ "</b> has failed with reason:").append(e.getMessage()).append("</li></font>");

			output.append("<br>");
			output.append("<br>");
		} finally {
			closeConnection(con, stmt);
		}
	}

	/**
	 * 
	 */
	public Object verifyCacheKey(GenericRequestBean aRequestBean) {
		List<CacheKeys> keys = aRequestBean.getCacheKeys();
		Map<String, String> response = new HashMap<String, String>();
		try {
			for (Iterator iterator = keys.iterator(); iterator.hasNext();) {
				CacheKeys cacheKeys = (CacheKeys) iterator.next();
				String moduleId = cacheKeys.getModuleId();
				String name = cacheKeys.getName();
				String caheOperation = cacheKeys.getCacheOperation();
				if (DistributedCacheUtil.getEligibleModules().contains(moduleId)) {
					String key = DistributedCacheUtil.getInstance().getDCKeyNamespace(aRequestBean.getLineage(),
							moduleId);
					String cachekey = key + name;
					performCacheOperation(response, moduleId, name, caheOperation, cachekey);
				} else if ("NA".equalsIgnoreCase(moduleId)) {
					performCacheOperation(response, "", name, caheOperation, name);
				} else {
					response.put("Wrong input ", "ModuleID not supported!!!");
				}
			}

		} catch (Exception e) {
			ErrorLogEventHelper.logErrorEvent(ConfigurationListUtils.class.getName(),
			"Exception occured in verifying cache", "verifyCacheKey method failed", e,
		     ErrorLogEvent.ERROR_SEVERITY);		
		}
		return response;
	}

	private void performCacheOperation(Map<String, String> response, String moduleId, String name, String caheOperation,
			String cachekey) {
		try {
			String templateConnected="[C]";
			Object resp = redisCacheProvider.find(cachekey, null);
			if(ObjectUtils.isEmpty(resp)){
				resp = sessionRedisCacheProvider.find(cachekey, null);
				templateConnected="[S]";
			}
			if (!ObjectUtils.isEmpty(resp)) {
				String output=moduleId.isEmpty()?name:moduleId + ":" + name;
				response.put(output, "Exist  - under cache!!!"+templateConnected);
				//response.put(moduleId + ":" + name, "Exist  - under cache!!!");
			} else {
				Collection<String> collection=redisCacheProvider.findKeys(cachekey);
				if(ObjectUtils.isEmpty(collection) || collection.size()==0){
					collection=sessionRedisCacheProvider.findKeys(cachekey);
					templateConnected="[S]";
				}
				if(!ObjectUtils.isEmpty(collection) && collection.size()>0){
					response.put(moduleId + ":" + name, "Exist  - under cache!!!"+templateConnected+".Numbers of keys for this Module is:"+collection.size());
				}
				else{
					response.put(moduleId + ":" + name, "Does NOT Exist - under cache!!!");
				}
			}
		} catch (Exception e) {
			response.put(moduleId + ":" + name, "Does NOT Exist - under cache!!!");
		}
	}

	public void deleteCacheKey(String aCacheKey) throws BaseCacheException {
		//try {
			redisCacheProvider.delete(aCacheKey, null);
		/*} catch (BaseCacheException bce) {
			ErrorLogEventHelper.logErrorEvent(ConfigurationListUtils.class.getName(),
					"Exception occured in deleting cache", "verifyCacheKey method failed", bce,
				     ErrorLogEvent.ERROR_SEVERITY);		
		}*/
	}

	/**
	 * Make sure to release DB resources
	 * 
	 * @param con
	 * @param stmt
	 */
	private void closeConnection(Connection con, PreparedStatement stmt) {
		try {
			if (!ObjectUtils.isEmpty(stmt)) {
				stmt.close();
			}
			if (!ObjectUtils.isEmpty(con)) {
				con.close();
			}
		} catch (Exception e) {
			ErrorLogEventHelper.logErrorEvent(ConfigurationListUtils.class.getName(),
					"Exception occured in closing connection", "closeConnection method failed", e,
				     ErrorLogEvent.ERROR_SEVERITY);
		}
	}

	/**
	 * Below method is required to test the DB connecting without defaultName
	 * set e.g. local dev machines
	 * 
	 * @param param
	 * @return
	 */
	private String getDefaultSchemaValue(String param) {
		String defaultSchemaValue = "";
		if (!ObjectUtils.isEmpty(param) && !"".equals(param)) {
			defaultSchemaValue = param + ".";
		}
		return defaultSchemaValue;
	}
	public Object printCacheKeySize(GenericRequestBean aRequestBean) {
		List<CacheKeys> keys = aRequestBean.getCacheKeys();
		Map<String,Integer> responseMap=new HashMap<String,Integer>();
		CacheKeys key= (!ObjectUtils.isEmpty(keys)&&keys.size()>0)?keys.get(0):null;
		String templateConnected="[C]";
		try{
			if(!ObjectUtils.isEmpty(key)){
				String cachename=key.getName();
				Collection<String> collOfKeys=this.redisTemplateForObject.keys(cachename);
				
				Collection<String> sessionCollOfKeys=null;
				if(sessionRedisEnable){
					sessionCollOfKeys=this.sessionRedisTemplateForObject.keys(cachename);
				}
				
				String cacheKeysLimit=System.getProperty("dcLimitToShowCacheKeys");
				long length=Long.parseLong(cacheKeysLimit);
				
				if(!ObjectUtils.isEmpty(collOfKeys) && collOfKeys.size()>0){
					putDataInResponseMap(responseMap, templateConnected, collOfKeys, length);
				}
				if(!ObjectUtils.isEmpty(sessionCollOfKeys) && sessionCollOfKeys.size()>0){
					templateConnected="[S]";
					putDataInResponseMap(responseMap, templateConnected, sessionCollOfKeys, length);
				}
			}
		  
		}
		// fix for find bug
		catch (RuntimeException e) {
			throw e;
		} catch (Exception e) {

		}
		
		return responseMap;
	}

	private void putDataInResponseMap(Map<String, Integer> responseMap, String templateConnected,
			Collection<String> collOfKeys, long length) throws IOException, BaseCacheException {
		int i=0;
		for(String akey:collOfKeys){
			i++;
			if(i<=length){
				ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
			    ObjectOutputStream out = new ObjectOutputStream(byteOut);
			    Object obj = DistributedCacheUtil.getInstance().getCachedObject(akey,null);
			    out.writeObject(obj);
			    responseMap.put(akey+"!!!"+templateConnected, byteOut.size());
			   						  
			}
		}
	}
	public Object printJedisPoolConfig() {
		Map<String,String> responseMap=new HashMap<String,String>();
		try {
			
			JedisConnectionFactory jedisConnectionFactory =(JedisConnectionFactory) redisTemplateForObject.getConnectionFactory();
		
			if(!ObjectUtils.isEmpty(jedisConnectionFactory))
			{
			GenericObjectPoolConfig jedisPoolConfig=jedisConnectionFactory.getPoolConfig();
			
			  if(!ObjectUtils.isEmpty(jedisPoolConfig))
				{		
			//JedisShardInfo sharedInfo=jedisConnectionFactory.getShardInfo();	
			responseMap.put("JedisConnectionFactory Client Name",""+jedisConnectionFactory.getClientName());
			responseMap.put("JedisConnectionFactory Time Out",""+jedisConnectionFactory.getTimeout());
			responseMap.put("JedisConnectionFactory Host Name", jedisConnectionFactory.getHostName());
			responseMap.put("JedisConnectionFactory Port No", ""+jedisConnectionFactory.getPort());
			responseMap.put("jedisPoolConfig Max Idle", ""+jedisPoolConfig.getMaxIdle());
			responseMap.put("jedisPoolConfig Max Idle", ""+jedisPoolConfig.getMaxIdle());
			responseMap.put("jedisPoolConfig Max Total",""+ jedisPoolConfig.getMaxTotal());
			responseMap.put("jedisPoolConfig Min Idle", ""+jedisPoolConfig.getMinIdle());
			responseMap.put("jedisPoolConfig Max Wait", ""+jedisPoolConfig.getMaxWaitMillis());
			responseMap.put("jedisPoolConfig MinEvictableIdleTime", ""+jedisPoolConfig.getMinEvictableIdleTimeMillis());
			
			//  if(sharedInfo != null)
	           //  {
			//responseMap.put("JedisShardInfo Connection Timeout", ""+sharedInfo.getConnectionTimeout());
			//responseMap.put("JedisShardInfo DB", ""+sharedInfo.getDb());
			//responseMap.put("JedisShardInfo Name", ""+sharedInfo.getName());
			//responseMap.put("JedisShardInfo Password", ""+sharedInfo.getPassword());
			//responseMap.put("JedisShardInfo SoTimeout", ""+sharedInfo.getSoTimeout());
			//responseMap.put("JedisShardInfo Host : Port : GetWeight", ""+sharedInfo.toString());
			//} 
				 }
			 }
			}
		    catch (Exception e) {
			// TODO Auto-generated catch block
			ErrorLogEventHelper.logErrorEvent(ConfigurationListUtils.class.getName(),
						"Exception occured in printJedisPoolConfig", "printJedisPoolConfig method failed", e,
					     ErrorLogEvent.ERROR_SEVERITY);
		}
		return responseMap;
	}
	
	public Object getUdpNextPersonSchemaObject() {
		com.alight.upoint.udp.beans.UDPResponse udpResponse = null;
		try {
			Map<String, Object> brokerHeaderMap=UDPBrokerHeaderUtil.createBrokerHeaderMapFromAHUser();

			//AHUser ahUser = AHUserLocalServiceUtil.getAHUser();
			udpResponse = new com.alight.upoint.udp.pojo.person.v2.PersonService();
			String locale = UserUtil.getLocale();

			String udpURI = UDPUriResourceHandler.getResourceURI("getSecurityInfo");
			com.alight.upoint.udp.beans.UDPRequest udpRequest = new com.alight.upoint.udp.beans.UDPRequest();
			com.alight.upoint.udp.beans.UDPBrokerHeader udpBrokerHeader = new com.alight.upoint.udp.beans.UDPBrokerHeader(brokerHeaderMap);
			
			udpRequest.setUdpBrokerHeaderObject(udpBrokerHeader);
			udpRequest.addQueryParam("locale", locale);
			udpRequest.addQueryParam("view", "participantSelfServiceView");

			HttpServletRequest httpRequest = null;
			RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
			ServletRequestAttributes servletRequestAttributes = null;
			if (!ObjectUtils.isEmpty(requestAttributes)) {
				servletRequestAttributes = (ServletRequestAttributes) requestAttributes;
				httpRequest = servletRequestAttributes.getRequest();
			String alightScrSessionToken = httpRequest.getHeader("alightSessionToken");
			String alightPrsnSessTkn = httpRequest.getHeader("alightPersonSessionToken");
			String alightRequestHeader = httpRequest.getHeader("alightRequestHeader");
			String useSecondaryDataCache = httpRequest.getHeader("UseSecondaryDataCache");

			com.alight.upoint.udp.beans.UDPRequestHeader udpRequestHeader = null;
			com.alight.upoint.udp.beans.UDPServiceObject mUDPServiceObject = null;
			if (StringUtils.isBlank(alightPrsnSessTkn)) {
				udpRequestHeader = new com.alight.upoint.udp.beans.UDPRequestHeader(alightRequestHeader,
						useSecondaryDataCache, null, alightScrSessionToken);
				mUDPServiceObject = new com.alight.upoint.udp.beans.UDPServiceObject(udpURI, locale, udpRequest,
						udpResponse, udpRequestHeader);
			} else {
				udpRequestHeader = new com.alight.upoint.udp.beans.UDPRequestHeader(alightRequestHeader,
						useSecondaryDataCache, alightPrsnSessTkn, null);
				mUDPServiceObject = new com.alight.upoint.udp.beans.UDPServiceObject(udpURI, locale, udpRequest,
						udpResponse, udpRequestHeader);
			}
			mUDPServiceObject.setServiceName("UDPResource:" + udpURI);
			com.alight.upoint.udp.beans.UDPInvocationResult result = getUDPBaseServiceObj().invoke(mUDPServiceObject);
			if (result.getStatusCode() != com.alight.upoint.udp.beans.UDPInvocationResult.SUCCESS) {
				return result;
			}
			}
		} catch (Exception ex) {
			ErrorLogEventHelper.logErrorEvent(ConfigurationListUtils.class.getName(),
					"Error occured while building the UDp cache", "buildUdpCache", ex, "",
					ErrorLogEvent.ERROR_SEVERITY);
		}
	
		return udpResponse;
	}

	public List<Map<Object, Object>> getUpcConfig(GenericRequestBean aRequestBean)  {
		try {
		RestServiceInvocationResult serviceResponseResult  = null;
		UPCKey upcKey = aRequestBean.getUpcKey();
		
		String widgetName= upcKey.getWidgetName();
		String widgetInstanceId=upcKey.getWidgetInstanceId();
	    ComponentRequestBean compBean = new ComponentRequestBean();
	    compBean.setComponent("upc.component");
		Map<String, Object> compParams = new HashMap<String,Object>();
		compParams.put("widgetName", widgetName);
		compParams.put("widgetInstanceId", widgetInstanceId);
		compBean.setComponentParams(compParams);
		Map<Object, Object> requestParam = cmServicesUtil.getRequestParamFromValueHolder(upcKey.getRequestParam(), upcKey.getRequestParamToValueHolderMap());
		if(!ObjectUtils.isEmpty(AppContainerProvider.getContainer()) && !ObjectUtils.isEmpty(requestParam) && !requestParam.isEmpty())
		{
			AppContainerProvider.getContainer().setAttribute("requestParam", requestParam);
		}	
			
		  serviceResponseResult = AdapterService.getResponse(compBean);
				
		if (RestServiceInvocationResult.ResponseStatusEnum.SUCCESS == serviceResponseResult.getResponseStatus()) {
			return serviceResponseResult.getResponseCMValues(); 
		} else {
			return serviceResponseResult.getResponseCMError();
		}	
		}
		catch (Exception e) {
			ErrorLogEventHelper.logErrorEvent(ConfigurationListUtils.class.getName(), "Exception while resolving UPC configuration", "getUpcConfig method failed", e,
					ErrorLogEvent.ERROR_SEVERITY);
			return cmServicesUtil.buildServcieErrorResponseMap("Error", e.getMessage());
		}
		finally
		{
			if(!ObjectUtils.isEmpty(AppContainerProvider.getContainer()))
				AppContainerProvider.getContainer().getAttributes().put("requestParam", null);
				

				
		}
	}
		
	public Map<String,String> clearHro() {
		String msg = "";
		Map<String, String> response = new HashMap<String, String>();
		MessageAppContainer msgAppContainer = AppContainerProvider.getContainer();
		String clientId = msgAppContainer.getPrimaryClientId();
		if (StringUtils.isNotBlank(clientId)) {
			HROCacheMap hroCacheMap = HROCacheMap.getInstance();
			if (hroCacheMap.containsKey("HRO:"+ clientId)) {
				hroCacheMap.remove("HRO:"+ clientId);
				msg = "The hrojvmcache is cleared for clientId:" + clientId;
			} else {
				msg = "There is no entry for clientId:" + clientId + " in hrojvmcache";
			}

		} else {
			msg = "The primaryClientId is not found in MessageAppContainer. It is required to clear hrojvmcache";
		}
		
		response.put("result", msg);
		return response;
		
	}
	public Map<String,String> clearHroAll() {
		String msg = "";
		Map<String, String> response = new HashMap<String, String>();
		HROCacheMap hroCacheMap = HROCacheMap.getInstance();
		hroCacheMap.clear();
		msg = "The hrojvmcache is cleared for all clientIds";


		response.put("result", msg);
		return response;

	}
	
	public boolean isRedisCallFlag(HttpServletRequest httpRequest, Map<String, String> logconfigstoragemap){
		boolean isRedisFlag=false;
		if(!ObjectUtils.isEmpty(profile) && !profile.equalsIgnoreCase("QC") && !profile.equalsIgnoreCase("prod")){
			String flag=httpRequest.getHeader("REDIS_CALL_FLAG");
			if(!ObjectUtils.isEmpty(flag) && flag.equalsIgnoreCase("true")){
				logconfigstoragemap.put(UpointLogConstants.REDIS_CALL_FLAG,UpointLogConstants.ON);
				isRedisFlag=true;
			}
		}
		return isRedisFlag;
	}
	
	/**
	 * Method to log the Redis Call Count information.
	 */
	public void logRedisCallCountInfoLog(){
		try{			
			RedisCallCountStorage redisCallCountPoolExist = (RedisCallCountStorage) LogInheritableThreadLocal.get()
					.get(UpointLogConstants.REDIS_CALL_COUNT_POOL);
			if (!ObjectUtils.isEmpty(redisCallCountPoolExist)) {
				StringBuffer redisCallSb = new StringBuffer();
				redisCallSb.append("Number of Singleget calls : ")
						.append(redisCallCountPoolExist.getRedisSingleGetCount());
		
				if (!ObjectUtils.isEmpty(redisCallCountPoolExist.getRedisMgetCountList())
						&& !redisCallCountPoolExist.getRedisMgetCountList().isEmpty()) {
					redisCallSb.append(",").append("Number of Multiget calls : ")
							.append(redisCallCountPoolExist.getRedisMgetCountList().size()).append(",")
							.append("Number of keys in each MGET =  ");
					for (int i = 0; i < redisCallCountPoolExist.getRedisMgetCountList().size(); i++) {
						redisCallSb.append("MGET" + (i + 1)).append("=")
								.append(redisCallCountPoolExist.getRedisMgetCountList().get(i)).append(",");
					}
		
				}
				if (StringUtils.isNotBlank(redisCallSb.toString())) {
					InfoTypeLogEventHelper.logInfoEvent(UpointLoggingFilter.class.getName(),
							redisCallSb.toString());
				}
		
			} 
		}catch(Exception e){
			ErrorLogEventHelper.logErrorEvent(ConfigurationListUtils.class.getName(),
					"Exception occured while calling logRedisCallCountInfoLog method from widget service",
					"logRedisCallCountInfoLog", e, ErrorLogEvent.ERROR_SEVERITY);
		}
 
	}
	/**
	 * 
	 * @param cacheName
	 * @return
	 */
	public Map<String, String> clearAFLinksCache(String clientId) {
		Map<String, String> response = new HashMap<String, String>();
		String message = "Invalid client id value!! either empty or null.";
		if (StringUtils.isBlank(clientId)) {
			response.put("result", "Invalid client id value!! either empty or null.");
			return response;
		}

		if (StringUtils.isNotBlank(clientId) && clientId.endsWith("_1.0") == false && "all".equalsIgnoreCase(clientId)==false) {
			clientId = clientId + "_1.0";
		}
		message = "AFLinks cache cleared successfully for clients [" + clientId + "]";
		if ("all".equals(clientId)) {
			ShortCacheUtil.getInstance().getAfLinksCache().clear();
		} else {
			ShortCacheUtil.getInstance().getAfLinksCache().remove(clientId);
		}
		response.put("result", message);
		return response;
	}
	
	/**
	 * 
	 * @param clientId
	 * @param isUCE
	 * @return
	 */
	public Map<String, String> clearLinksCache(String clientId, boolean isUCE) {
		Map<String, String> response = new HashMap<String, String>();
		String message = "Invalid client id value!! either empty or null.";
		if (StringUtils.isBlank(clientId)) {
			response.put("result", "Invalid client id value!! either empty or null.");
			return response;
		}

		if (StringUtils.isNotBlank(clientId) && clientId.endsWith("_1.0") == false
				&& "all".equalsIgnoreCase(clientId) == false) {
			clientId = clientId + "_1.0";
		}
		String moduleName=isUCE==false?"UPOINT":"UCE";
		message = moduleName+" Links cache cleared successfully for clients [" + clientId + "]";
		if ("all".equals(clientId)) {
			if (isUCE == true) {
				ShortCacheUtil.getInstance().getUceLinksCache().clear();
				ShortCacheUtil.getInstance().getUceLinkTagCache().clear();
			} else {
				ShortCacheUtil.getInstance().getUptLinksCache().clear();
				ShortCacheUtil.getInstance().getUptLinkTagCache().clear();
			}
		} else {
			if (isUCE == true) {
				ShortCacheUtil.getInstance().getUceLinksCache().remove(clientId);
				ShortCacheUtil.getInstance().getUceLinkTagCache().remove(clientId);
			} else {
				ShortCacheUtil.getInstance().getUptLinksCache().remove(clientId);
				ShortCacheUtil.getInstance().getUceLinksCache().remove(clientId);
			}
		}
		response.put("result", message);
		return response;
	}
	
	/**
	 * 
	 * @param clientId
	 * @param isUCE
	 * @return
	 */
	public Map<String, String> clearExpressionsCache(String clientId, boolean isUCE) {
		Map<String, String> response = new HashMap<String, String>();
		String message = "Invalid client id value!! either empty or null.";
		if (StringUtils.isBlank(clientId)) {
			response.put("result", "Invalid client id value!! either empty or null.");
			return response;
		}

		if (StringUtils.isNotBlank(clientId) && clientId.endsWith("_1.0") == false
				&& "all".equalsIgnoreCase(clientId) == false) {
			clientId = clientId + "_1.0";
		}
		String moduleName=isUCE==false?"UPOINT":"UCE";
		message = moduleName+" Expressions cache cleared successfully for clients [" + clientId + "]";
		if ("all".equals(clientId)) {
			if (isUCE == true) {
				ShortCacheUtil.getInstance().getUceExprCache().clear();
			} else {
				ShortCacheUtil.getInstance().getUptExprCache().clear();
			}
		} else {
			if (isUCE == true) {
				ShortCacheUtil.getInstance().getUceExprCache().remove(clientId);
			} else {
				ShortCacheUtil.getInstance().getUptExprCache().remove(clientId);
			}
		}
		response.put("result", message);
		return response;
	}
    
	/**
	 * Method to log the Expressions Call Count information.
	 */
	public void logExpCallCountInfoLog() {
		try {
			if (!ExpressionDebugAssistUtil.isExpressionsLoggingEnabled()) {
				return;
			}
			ExpressionDebugAssistInfo expressionDebugAssistInfo = (ExpressionDebugAssistInfo) LogInheritableThreadLocal
					.get().get(ExpressionDebugAssistUtil.DEBUG_ASST_EXP_INFO);
			if (!ObjectUtils.isEmpty(expressionDebugAssistInfo)) {
				StringBuffer debugAssistExpressionSB = new StringBuffer();
				debugAssistExpressionSB.append(expressionDebugAssistInfo.getExpTreeMap().values());
				if (StringUtils.isNotBlank(debugAssistExpressionSB.toString())) {
					Gson gson = new GsonBuilder()
							  .excludeFieldsWithoutExposeAnnotation().disableHtmlEscaping()
							  .create();
					if(logbookPropertiesConfiguration.isPushToElk()) {
						InfoTypeLogEventHelper.logInfoEvent(UpointLoggingFilter.class.getName(),
								"Expression Tree Size ::" + expressionDebugAssistInfo.getExpTreeMap().size()
										+ "  Exp Details ::  " + gson.toJson(expressionDebugAssistInfo.getExpTreeMap().values()));
					}
					// push the expression tree map to dynamo db.
					if (dynamoDbHelper.isDynamoDbActive()) {
				        String expressionTree = gson.toJson(expressionDebugAssistInfo.getExpTreeMap().values());
						StringBuilder expTreeFullPayload = new StringBuilder(expressionTree);
						pushLogTodynamoDb(expTreeFullPayload.toString(), EXP_TREE_MAP_EVENT_TYPE);
					}
				}
		}
 
		} catch (Exception e) {
			ErrorLogEventHelper.logErrorEvent(ConfigurationListUtils.class.getName(),
					"Exception occured while calling logExpCallCountInfoLog method from widget service",
					"logExpCallCountInfoLog", e, ErrorLogEvent.ERROR_SEVERITY);
		}
 
	}

	private void pushLogTodynamoDb(String fullPayLoad, String evenType) {

		try {
			CommonTypeLogEvent commonTypeLogEvent = (CommonTypeLogEvent) LogInheritableThreadLocal.get()
					.get(UpointLogConstants.COMMON_TYPE_LOG_EVENT);

			if (!ObjectUtils.isEmpty(commonTypeLogEvent)) {
				dynamoDbHelper.saveOrUpdateItem(commonTypeLogEvent.getSpanID(), evenType, fullPayLoad);
			}
		} catch (Exception e) {
			ErrorLogEventHelper.logErrorEvent(ConfigurationListUtils.class.getName(),
					"Exception occured while callig dynamoDbHelper pushLogTodynamoDb method from widget service",
					"pushLogTodynamoDb()", e, ErrorLogEvent.ERROR_SEVERITY);
		}
	}

	private com.alight.upoint.udp.base.service.UDPBaseServices getUDPBaseServiceObj(){
		return ApplicationContextProvider.getBean("udpBaseServices", com.alight.upoint.udp.base.service.UDPBaseServices.class);
	}
	
	public Object getBusinessDomainObject() {

		HttpServletRequest httpRequest = null;
		RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
		ServletRequestAttributes servletRequestAttributes = null;
		if (!ObjectUtils.isEmpty(requestAttributes)) {
			servletRequestAttributes = (ServletRequestAttributes) requestAttributes;
			httpRequest = servletRequestAttributes.getRequest();
		}
		String alightRequestHeader = httpRequest.getHeader("alightRequestHeader");
		String alightPersonSessionToken = httpRequest.getHeader("alightPersonSessionToken");
		String domain = httpRequest.getHeader("domain");
		return SessionTokenParserUtil.testBusinessDomainObject(alightPersonSessionToken, alightRequestHeader, domain);
	}
	
	public String getUpdatedARHForSharedAccess() {
		String updatedARH = null;
		String domain = null;
		try {
			HttpServletRequest httpRequest = null;
			RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
			ServletRequestAttributes servletRequestAttributes = null;
			if (!ObjectUtils.isEmpty(requestAttributes)) {
				servletRequestAttributes = (ServletRequestAttributes) requestAttributes;
				httpRequest = servletRequestAttributes.getRequest();
			}
			String alightRequestHeader = httpRequest.getHeader("alightRequestHeader");
			String alightPersonSessionToken = httpRequest.getHeader("alightPersonSessionToken");
			domain = httpRequest.getHeader("domain");
			updatedARH = SessionTokenParserUtil.getupdatedAlightRequestHeaderForSharedAccess(alightPersonSessionToken, alightRequestHeader, domain);
			
		} catch (Exception e) {
			ErrorLogEventHelper.logErrorEvent(SessionTokenParserUtil.class.getName(),
					"Error occured while getting clientId,PersonInternalId,systemInstnaceId from alightPersonSessionToken",
					"getUpdatedARHForSharedAccess. Domain :" + domain, e, ErrorLogEvent.ERROR_SEVERITY);
		}
		return updatedARH;
	}
	
	public void clearUpcConfigCache() {
		try {
			
			    Map<String, Long> jvmUpcConfigCacheTimeStamp = UPCMemoryManagerClientCacheUtil.getUpcConfigJvmTimestamp();
				String uPointKey = DistributedCacheUtil.getInstance().getClientUpdatedTimeStampKey()+"_"+"ASSET_MANAGEMENT";
				Map<Object,Object> cMap = redisCompleteCacheMapProvider.getCompleteMap(uPointKey);
						
				
			if (!ObjectUtils.isEmpty(cMap) && !cMap.isEmpty() && !jvmUpcConfigCacheTimeStamp.isEmpty()) {
				for (Entry<String, Long> entry : jvmUpcConfigCacheTimeStamp.entrySet()) {
					String jvmLng = entry.getKey();
					Long jvmTimeStamp = entry.getValue();
					Long lngRdsTmstmp = (Long) cMap.getOrDefault(jvmLng, 0L);

					if (lngRdsTmstmp != 0 && jvmTimeStamp < lngRdsTmstmp) {
					
						
							UPCMemoryManagerClientCacheUtil.clearUPCCache(jvmLng);							
							UPCReqCacheUtil.clearUPCClientIdAssetGpDefinitionCache(jvmLng);
												
					}
				}
			}
			
			
		} catch (Exception e) {
			ErrorLogEventHelper.logErrorEvent(ConfigurationListUtils.class.getName(),
					"Exception occured while clearing Upc config cache configuration.",
					"clearUpcConfigCache() method failed", e, ErrorLogEvent.ERROR_SEVERITY);
		}
		
	}

}