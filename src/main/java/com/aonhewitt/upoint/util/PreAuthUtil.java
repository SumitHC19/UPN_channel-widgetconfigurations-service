package com.aonhewitt.upoint.util;

import java.util.List;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.aonhewitt.logging.helpers.ErrorLogEventHelper;
import com.aonhewitt.portal.adapter.util.DistributedRcsCacheUtil;
import com.aonhewitt.portal.core.service.util.CommonUtil;
import com.aonhewitt.portal.feature.util.CoreFeatureUtil;
import com.aonhewitt.portal.util.AHUserMapperUtil;
import com.aonhewitt.portal.util.AppContainerProvider;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.util.ObjectUtils;

public class PreAuthUtil {

	private static final String PUBLIC="public";
	private static final String UNDERSCORE="_";
	private static final String COLON=":";
	private static final String NAME_SPACE_PREAUTH="CWC:PREAUTH";
	public static boolean isPreAuthUrl(String url){
		
		if(!ObjectUtils.isEmpty(url) && url.endsWith("/public")){
			
				return true;
		}
		
		return false;
	}
	
	public static void populateMessageContainer(String alightRequestHeader) {

		try {
			// need below three values

			String locale = null;
			String clientId = null;
			String lineage = null;

			//String device = "Desktop"; // TODOD check

			
			if(ObjectUtils.isEmpty(alightRequestHeader)){
				throw new IllegalArgumentException("alightRequestHeader is missing");
			}
			ObjectMapper mapper = new ObjectMapper();
			JsonNode rootNode = mapper.readTree(alightRequestHeader);

			if (rootNode.isMissingNode()) {

				throw new IllegalArgumentException("alightRequestHeader is missing");
			}

			JsonNode localeNode = rootNode.path("locale");
			JsonNode clientIdNode = rootNode.path("clientId");

			JsonNode channelRequestData = rootNode.path("channelRequestData");

			if (localeNode.isMissingNode() || clientIdNode.isMissingNode()) {
				throw new IllegalArgumentException("locale or clientId  is missing in alightRequestHeader");
			} else {

				locale = localeNode.asText();
				clientId = clientIdNode.asText();
				lineage = clientId + "_1.0";
			}

			// set AHUse as null
			AppContainerProvider.getContainer().resetAhUser();

			// set preauth locale in message app container
			AppContainerProvider.getContainer().setPreAuthLocale(locale);

			// set lineage and primary client id
			AppContainerProvider.getContainer().setPrimaryClientId(clientId);
			AppContainerProvider.getContainer().setLineage(lineage);

			// set channel request data
			if (!channelRequestData.isMissingNode()) {
				AHUserMapperUtil.populateChannelUserRequestData(channelRequestData.asText(),
						AppContainerProvider.getContainer());
			}
			// set Pre auth request

			AppContainerProvider.getContainer().setPreAuthRequest(true);

		} catch (Exception e) {
			throw new IllegalArgumentException(e.getMessage());
		}
	}

	@SuppressWarnings("unchecked")
	public static List<Map<Object, Object>> getPreAuthDataFromCache(String cacheName, String nameSet) {
		List<Map<Object, Object>> response = null;
		HttpServletRequest httpRequest = null;
		RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
		ServletRequestAttributes servletRequestAttributes = null;
		if (!ObjectUtils.isEmpty(requestAttributes)) {
			servletRequestAttributes = (ServletRequestAttributes) requestAttributes;
			httpRequest = servletRequestAttributes.getRequest();
		}
		Boolean afEnabled = true;//Boolean.valueOf(null != httpRequest ? httpRequest.getHeader("AF_ENABLED") : null);
		if (!ObjectUtils.isEmpty(nameSet) && (nameSet.equals("header") || nameSet.equals("footer")  || nameSet.equals("afinit") || nameSet.equals("primarynav") || nameSet.equals("navigation")) && afEnabled) {
			try {
				// String preAuthCacheKey = nameSet;
				response = (List<Map<Object, Object>>) DistributedRcsCacheUtil.getInstance()
						.getObjectFromCache(cacheName);
			} catch (Exception e) {
				ErrorLogEventHelper.logErrorEvent(PreAuthUtil.class.getName(),
						"Failed to get PreAuth Response from cache", "getPreAuthDataFromCache", null, null);
			}
		}
		return response;
	}

	public static void setPreAuthDataInCache(String nameSet, Long ttl, Object objToSave) {
		if (AppContainerProvider.getContainer().isPreAuthRequest()
				&& (nameSet.equalsIgnoreCase("header") || nameSet.equalsIgnoreCase("footer")  
						|| nameSet.equals("afinit") || nameSet.equals("primarynav") || nameSet.equals("navigation"))) {
			String lineage = CommonUtil.getDomainLineage();
			String locale = CoreFeatureUtil.getLocale();
			String cacheName = NAME_SPACE_PREAUTH + COLON + lineage + UNDERSCORE + locale + COLON + nameSet
					+ UNDERSCORE + PUBLIC;
			DistributedRcsCacheUtil.getInstance().saveObjectInCache(cacheName, objToSave, ttl * 60);	
		}
	}

	/*private static String getPreAuthCacheKey(String nameSet) {
		String lineage = CommonUtil.getDomainLineage();
		String locale = CoreFeatureUtil.getLocale();
		String cachePreAuthKey = NAME_SPACE_PREAUTH + COLON + lineage + UNDERSCORE + locale + COLON + nameSet
				+ UNDERSCORE + PUBLIC;
		try {
			cachePreAuthKey = EncryptDecryptUtil.encrypt(cachePreAuthKey);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			ErrorLogEventHelper.logErrorEvent(PreAuthUtil.class.getName(),
					"Failed to get PreAuth Response from cache", "getPreAuthCacheKey", null, null);
		}
		return cachePreAuthKey;
	}*/
}