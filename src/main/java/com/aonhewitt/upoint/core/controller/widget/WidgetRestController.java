package com.aonhewitt.upoint.core.controller.widget;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.text.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.alight.asg.model.header.v1_0.RequestHeader;
import com.alight.asg.model.token.v1_0.PersonSessionToken;
import com.alight.logging.helpers.InfoTypeLogEventHelper;
import com.alight.logging.helpers.LogEventDataHandler;
import com.aonhewitt.beans.ComponentRequestBean;
import com.aonhewitt.logging.events.ErrorLogEvent;
import com.aonhewitt.logging.helpers.DebugLogEventHelper;
import com.aonhewitt.logging.helpers.ErrorLogEventHelper;
//import com.aonhewitt.logging.helpers.RestJSONLogEventHelper;
import com.aonhewitt.portal.adapter.AdapterService;
import com.aonhewitt.portal.adapter.BaseAdapter;
import com.aonhewitt.portal.adapter.util.KeyConstants;
import com.aonhewitt.portal.configuration.UCCERequestor;
import com.aonhewitt.portal.configuration.modules.assetmanagement.model.AssetDTO;
import com.aonhewitt.portal.core.service.AHUserLocalServiceUtil;
import com.aonhewitt.portal.core.service.ExpressionsLocalServiceUtil;
import com.aonhewitt.portal.core.service.util.CmServicesUtil;
import com.aonhewitt.portal.core.service.util.CommonUtil;
import com.aonhewitt.portal.expression.handler.IVAHandler;
import com.aonhewitt.portal.feature.util.CoreFeatureUtil;
import com.aonhewitt.portal.service.delegators.ConfigManagerService;
import com.aonhewitt.portal.userprovisioning.beans.AHUser;
import com.aonhewitt.portal.util.AppContainerProvider;
import com.aonhewitt.portal.util.MessageAppContainer;
import com.aonhewitt.portal.util.RestServiceInvocationResult;
import com.aonhewitt.upoint.blackout.util.BlackoutUtil;
import com.aonhewitt.upoint.cache.util.AppConfigReader;
import com.aonhewitt.upoint.conf.AppProperties;
import com.aonhewitt.upoint.conf.YAMLConfig;
import com.aonhewitt.upoint.core.service.util.ExpressionsCacheUtility;
import com.aonhewitt.upoint.util.AELockUtil;
import com.aonhewitt.upoint.util.CommonUtils;
import com.aonhewitt.upoint.util.PageInfoUtil;
import com.aonhewitt.upoint.util.PreAuthUtil;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

import jakarta.servlet.http.HttpServletRequest;
/**
 * 
 * @Created 20/09/2017 -  A0709276
 * 
 */

@RestController
@RequestMapping("/channel/")
public class WidgetRestController { 
	
	
	PageInfoUtil pageInfoUtil = new PageInfoUtil();

	@Autowired
	private ConfigManagerService configManagerService;
	
	@Autowired
	CmServicesUtil cmServicesUtil;
	
	@Autowired
	private AppProperties appProperties;
	
	@Autowired
	private ExpressionsCacheUtility expressionsCacheUtility;

	@Autowired
	private BlackoutUtil blackoutUtil;
	
	@Autowired
	private YAMLConfig yamlConfig;
	
	@Autowired
	CommonUtils commonUtils;
	
	private static final String PUBLIC="public";
	private static final String UNDERSCORE="_";
	private static final String COLON=":";
	private static final String NAME_SPACE_PREAUTH="CWC:PREAUTH";
	public static final String ASSET = "asset";
	public static final String ASSET_UCE="asset_uce";
	public static final String ASSETVALUE = "assetValue";
	
	@GetMapping(value = "widgetConfigurations/{nameSet}")
	public List<Map<Object, Object>> getWidgetConfiguration(@PathVariable String nameSet) {
		// UFD-20568 -- Changes for expression Caching
		String nameSets = StringEscapeUtils.escapeHtml4(nameSet);
		HttpServletRequest httpRequest = null;
		RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
		ServletRequestAttributes servletRequestAttributes = null;
		if (!ObjectUtils.isEmpty(requestAttributes)) {
			servletRequestAttributes = (ServletRequestAttributes) requestAttributes;
			httpRequest = servletRequestAttributes.getRequest();
		}
		expressionsCacheUtility.evalAndPopulateInitConfigExpressionValuesIntoThreadLocal(httpRequest);
		List<Map<Object, Object>> response = processWidgetRequest(nameSets);
		expressionsCacheUtility.storeResolvedExpressionNamesIntoRedis(httpRequest);
		// UFD-20568 --Changes for Expression Caching Ends
		return response;

	}

	
	@GetMapping(value = "widgetConfigurations/{nameSet}/public")
	public List<Map<Object, Object>> getPreAuthWidgetConfiguration(@PathVariable String nameSet) {
		List<Map<Object, Object>> response = null;
		String lineage = CommonUtil.getDomainLineage();
		String locale = CoreFeatureUtil.getLocale();
		String cacheName = StringEscapeUtils.escapeHtml4(NAME_SPACE_PREAUTH + COLON + lineage + UNDERSCORE + locale + COLON + nameSet
				+ UNDERSCORE + PUBLIC);
		String nameSets = StringEscapeUtils.escapeHtml4(nameSet);
		if (appProperties.isPreAuthCachingEnabled() && !"validateblackout".equals(nameSet)) {
			response = PreAuthUtil.getPreAuthDataFromCache(cacheName, nameSets);
		}
		if (ObjectUtils.isEmpty(response)) {
			response = processWidgetRequest(nameSets);
		}
		return response;
	}

	private List<Map<Object, Object>> processWidgetRequest(String nameSet) {

		// fix for find bug
		List<Map<Object, Object>> result = null;
		try {
			if (!ObjectUtils.isEmpty(nameSet)) {

				result = getService(nameSet);
				return result;
			}

			else {

				return cmServicesUtil.buildServcieErrorResponseMap("Error", "Invalid Request");
			}
		} catch (Exception e) {
			ErrorLogEventHelper.logErrorEvent(WidgetRestController.class.getName(),
					"Exception while fetching widget configurations", "getWidgetConfiguration method failed", e,
					ErrorLogEvent.ERROR_SEVERITY);
			return cmServicesUtil.buildServcieErrorResponseMap("Error", e.getMessage());
		}

	}
	
	/**
	 * @param nameSet
	 * @return
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	private List<Map<Object, Object>> getService(String nameSet) throws JsonParseException, JsonMappingException, IOException {
		

		RestServiceInvocationResult serviceResponseResult = null ;
		ComponentRequestBean aRequestBean = new ComponentRequestBean();
		HttpServletRequest httpRequest = null;
		RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
		ServletRequestAttributes servletRequestAttributes = null;
		if (!ObjectUtils.isEmpty(requestAttributes)) {
			servletRequestAttributes = (ServletRequestAttributes) requestAttributes;
			httpRequest = servletRequestAttributes.getRequest();
				}
		String lineage=null;
		String locale=null;
		boolean isPreAuthComponent = false;
		boolean isAFEnabled = true;
		try {
			//Flag to identify if Header/Footer/Tiles call is pre-auth
			if(!AELockUtil.isAELockEnabled() && AppContainerProvider.getContainer().isPreAuthRequest()) {
				try{
					String afEnabled=null;
					if ("header".equals(nameSet) || "footer".equals(nameSet) || "branding".equals(nameSet) ||  "primarynav".equals(nameSet)  ||  "afinit".equals(nameSet) || "navigation".equals(nameSet)) {
						afEnabled = (String)httpRequest.getHeader("AF_ENABLED");
					} else if ("tiles".equals(nameSet)) {
						afEnabled = (String)httpRequest.getHeader("afSwitchOn");
					}
					if(!ObjectUtils.isEmpty(afEnabled) && afEnabled.equalsIgnoreCase("true")){
						isAFEnabled = true;
					}
				}
				catch(Exception e){
					isAFEnabled = false;
				}
				if(isAFEnabled || "upc".equals(nameSet)){
					isPreAuthComponent = true;	
				}
			}
			
			// Skipping the generic call in case Header/Footer/tiles pre-auth case
			if(AppContainerProvider.getContainer().isPreAuthRequest() && !isPreAuthComponent  && !"validateblackout".equals(nameSet)){
				
				locale= AppContainerProvider.getContainer().getPreAuthLocale();
				
				lineage = AppContainerProvider.getContainer().getLineage();
				aRequestBean = prepateRequestBeanForGenericConfigSet(lineage, locale, nameSet);
			}
			else {

				if (nameSet.equals("header")) {
									
					AHUser ahUser = AHUserLocalServiceUtil.getAHUser();
					Map<String, String> userData = null;
					
					if (!ObjectUtils.isEmpty(ahUser) && !ObjectUtils.isEmpty(AppContainerProvider.getContainer().getChannelRequestData())) {
						userData = AppContainerProvider.getContainer().getChannelRequestData().getUserData();
					}
					
					boolean hidePrimaryAccountParam = false;
					String originatorParam = "";
					String prsnRsnCdParam = "";
					String masSessionRoleCDParam = "";
					String masCurrentRequestedAcTypeParam = "";
					boolean isAfEnabled = false;

					if (!ObjectUtils.isEmpty(userData) && userData.containsKey("IS_HIDE_PRIMARY_ACCOUNT_IN_MAS_DROP_DOWN")) {
						String tempValue = userData.get("IS_HIDE_PRIMARY_ACCOUNT_IN_MAS_DROP_DOWN");
						if (!ObjectUtils.isEmpty(tempValue) && !tempValue.equals("") && tempValue.equals("true")) {
							hidePrimaryAccountParam = true;
						}
					}

					if (!ObjectUtils.isEmpty(userData) && userData.containsKey("originator")) {
						originatorParam = userData.get("originator");
						if (ObjectUtils.isEmpty(originatorParam)) {
							originatorParam = "";
						}
					}

					if (!ObjectUtils.isEmpty(ahUser) && !ObjectUtils.isEmpty(AppContainerProvider.getContainer().getChannelRequestData())) {
						prsnRsnCdParam = CoreFeatureUtil.getLRParam("prsnRsnCd");
						if (ObjectUtils.isEmpty(prsnRsnCdParam)) {
							prsnRsnCdParam = "";
						}
					}

					if (!ObjectUtils.isEmpty(userData) && userData.containsKey("com.aonhewitt.upoint.psp.integration.masrolecd")) {
						masSessionRoleCDParam = userData.get("com.aonhewitt.upoint.psp.integration.masrolecd");
						if (ObjectUtils.isEmpty(masSessionRoleCDParam)) {
							masSessionRoleCDParam = "";
						}
					}

					if (!ObjectUtils.isEmpty(userData) && userData.containsKey("MAS_CURRENT_REQUESTED_ACTIVE_ACCT_TYPE")) {
						masCurrentRequestedAcTypeParam = userData.get("MAS_CURRENT_REQUESTED_ACTIVE_ACCT_TYPE");
						if (ObjectUtils.isEmpty(masCurrentRequestedAcTypeParam)) {
							masCurrentRequestedAcTypeParam = "";
						}
					}

					locale = CoreFeatureUtil.getLocale();
					lineage = CommonUtil.getDomainLineage();

					aRequestBean.setLineage(lineage);
					aRequestBean.setLocale(locale);
					aRequestBean.setComponent("header.component");
					Map<String, Object> compParams = new HashMap<String, Object>();

					compParams.put("hidePrimaryAccountParam", hidePrimaryAccountParam);
					compParams.put("originatorParam", originatorParam);
					compParams.put("prsnRsnCdParam", prsnRsnCdParam);
					compParams.put("masSessionRoleCDParam", masSessionRoleCDParam);
					compParams.put("masCurrentRequestedAcTypeParam", masCurrentRequestedAcTypeParam);
					String isLanguageChangedParam = httpRequest.getParameter("languageId");
					if(!ObjectUtils.isEmpty(isLanguageChangedParam))
					{
						compParams.put("languageId", true);
					}
					// DCH-742: Angular First Expression
					if(!AELockUtil.isAELockEnabled()){
						try{
							String afEnabled=(String)httpRequest.getHeader("AF_ENABLED");
							if(!ObjectUtils.isEmpty(afEnabled) && afEnabled.equalsIgnoreCase("true")){
								isAfEnabled = true;
							}
						}
						catch(Exception e){
							isAfEnabled = false;
						}
					}
					compParams.put("AF_ENABLED", isAfEnabled);
					aRequestBean.setComponentParams(compParams);
			    } else if (nameSet.equals("navigation")) {
					AHUser ahUser = AHUserLocalServiceUtil.getAHUser();
					Map<String, String> userData = null;
					
					if (!ObjectUtils.isEmpty(ahUser) && !ObjectUtils.isEmpty(AppContainerProvider.getContainer().getChannelRequestData())) {
						userData = AppContainerProvider.getContainer().getChannelRequestData().getUserData();
					}
					
					boolean hidePrimaryAccountParam = false;
					String originatorParam = "";
					String prsnRsnCdParam = "";
					String masSessionRoleCDParam = "";
					String masCurrentRequestedAcTypeParam = "";
					boolean isAfEnabled = false;

					if (!ObjectUtils.isEmpty(userData) && userData.containsKey("IS_HIDE_PRIMARY_ACCOUNT_IN_MAS_DROP_DOWN")) {
						String tempValue = userData.get("IS_HIDE_PRIMARY_ACCOUNT_IN_MAS_DROP_DOWN");
						if (!ObjectUtils.isEmpty(tempValue) && !tempValue.equals("") && tempValue.equals("true")) {
							hidePrimaryAccountParam = true;
						}
					}

					if (!ObjectUtils.isEmpty(userData) && userData.containsKey("originator")) {
						originatorParam = userData.get("originator");
						if (ObjectUtils.isEmpty(originatorParam)) {
							originatorParam = "";
						}
					}

					if (!ObjectUtils.isEmpty(ahUser) && !ObjectUtils.isEmpty(AppContainerProvider.getContainer().getChannelRequestData())) {
						prsnRsnCdParam = CoreFeatureUtil.getLRParam("prsnRsnCd");
						if (ObjectUtils.isEmpty(prsnRsnCdParam)) {
							prsnRsnCdParam = "";
						}
					}

					if (!ObjectUtils.isEmpty(userData) && userData.containsKey("com.aonhewitt.upoint.psp.integration.masrolecd")) {
						masSessionRoleCDParam = userData.get("com.aonhewitt.upoint.psp.integration.masrolecd");
						if (ObjectUtils.isEmpty(masSessionRoleCDParam)) {
							masSessionRoleCDParam = "";
						}
					}

					if (!ObjectUtils.isEmpty(userData) && userData.containsKey("MAS_CURRENT_REQUESTED_ACTIVE_ACCT_TYPE")) {
						masCurrentRequestedAcTypeParam = userData.get("MAS_CURRENT_REQUESTED_ACTIVE_ACCT_TYPE");
						if (ObjectUtils.isEmpty(masCurrentRequestedAcTypeParam)) {
							masCurrentRequestedAcTypeParam = "";
						}
					}

					locale = CoreFeatureUtil.getLocale();
					lineage = CommonUtil.getDomainLineage();

					aRequestBean.setLineage(lineage);
					aRequestBean.setLocale(locale);
					aRequestBean.setComponent("navigation.component");
					Map<String, Object> compParams = new HashMap<String, Object>();

					compParams.put("hidePrimaryAccountParam", hidePrimaryAccountParam);
					compParams.put("originatorParam", originatorParam);
					compParams.put("prsnRsnCdParam", prsnRsnCdParam);
					compParams.put("masSessionRoleCDParam", masSessionRoleCDParam);
					compParams.put("masCurrentRequestedAcTypeParam", masCurrentRequestedAcTypeParam);
					String isLanguageChangedParam = httpRequest.getParameter("languageId");
					if(!ObjectUtils.isEmpty(isLanguageChangedParam))
					{
						compParams.put("languageId", true);
					}
					// DCH-742: Angular First Expression
					if(!AELockUtil.isAELockEnabled()){
						try{
							String afEnabled=(String)httpRequest.getHeader("AF_ENABLED");
							if(!ObjectUtils.isEmpty(afEnabled) && afEnabled.equalsIgnoreCase("true")){
								isAfEnabled = true;
							}
						}
						catch(Exception e){
							isAfEnabled = false;
						}
					}
					compParams.put("AF_ENABLED", isAfEnabled);
					aRequestBean.setComponentParams(compParams);
				} else if (nameSet.equals("footer")) {
					String url = "/web/{$friendlyurl}/home";
					String uuid = "";
					String scopeGrpId = "";
					String userCountryCode = "";
					String friendlyURL = "";
					String pageName = "";
					String isLogOnPage = "false";
					String requestURI = "";
					String masPageAttribute = "";
					String colorSchemeId = "default";
					boolean isAfEnabled = true;
					AHUser ahUser = AHUserLocalServiceUtil.getAHUser();
					if (!ObjectUtils.isEmpty(ahUser)) {
						if (!ObjectUtils.isEmpty(ahUser.getChannelUserProfile())) {
							scopeGrpId = String.valueOf(ahUser.getChannelUserProfile().getgroupId());
							userCountryCode = ahUser.getCountryCd();
							url = "/web/" + ahUser.getChannelUserProfile().getOrgName() + "/"
									+ CoreFeatureUtil.getPageName();
						}

						if (!ObjectUtils.isEmpty(AppContainerProvider.getContainer().getChannelRequestData())) {
							Map<String, String> map = AppContainerProvider.getContainer().getChannelRequestData().getUserData();
							if (!ObjectUtils.isEmpty(map)) {
								if (!ObjectUtils.isEmpty(map.get("com.aonhewitt.portal.auth.constants.Constants_MAS_PAGE_ATTR"))) {
									masPageAttribute = map
											.get("com.aonhewitt.portal.auth.constants.Constants_MAS_PAGE_ATTR");
								}
								if (!ObjectUtils.isEmpty(map.get("csid")) && !map.get("csid").trim().isEmpty()) {
									colorSchemeId = map.get("csid");
								}
							}
						}
					}

					locale = CoreFeatureUtil.getLocale();
					lineage = CommonUtil.getDomainLineage();
					aRequestBean.setLineage(lineage);
					aRequestBean.setLocale(locale);
					aRequestBean.setComponent("footer.component");
					Map<String, Object> compParams = new HashMap<String, Object>();

					compParams.put("url", url);
					compParams.put("friendlyURL", friendlyURL);
					compParams.put("pageName", pageName);
					compParams.put("isLogOnPage", isLogOnPage);
					compParams.put("requestURI", requestURI);
					compParams.put("scopeGrpId", scopeGrpId);
					compParams.put("uuid", uuid);
					compParams.put("masPageAttribute", masPageAttribute);
					compParams.put("colorSchemeId", colorSchemeId);
					compParams.put("userCountryCode", userCountryCode);
					String isLanguageChangedParam = httpRequest.getParameter("languageId");
					if(!ObjectUtils.isEmpty(isLanguageChangedParam))
					{
						compParams.put("languageId", true);
					}
					if(!AELockUtil.isAELockEnabled()){
						try{
							String afEnabled=(String)httpRequest.getHeader("AF_ENABLED");
							if(!ObjectUtils.isEmpty(afEnabled) && afEnabled.equalsIgnoreCase("true")){
								isAfEnabled = true;
							}
						}
						catch(Exception e){
							isAfEnabled = false;
						}
					}
					compParams.put("AF_ENABLED", isAfEnabled);

					aRequestBean.setComponentParams(compParams);
					// aRequestBean = mapper.readValue(new
					// File("/home/me/work/apps-config/widget-configurations-service/footer.json"),
					// ComponentRequestBean.class);
				} else if (nameSet.equals("header-secondary-window")) {
					locale = CoreFeatureUtil.getLocale();
					lineage = CommonUtil.getDomainLineage();

					aRequestBean.setLineage(lineage);
					aRequestBean.setLocale(locale);
					aRequestBean.setComponent("header-secondary-window.component");
					Map<String, Object> compParams = new HashMap<String, Object>();

					aRequestBean.setComponentParams(compParams);
				} else if (nameSet.equals("primarynav") && !AELockUtil.isAELockEnabled()) { 
					// PCA-7536
					locale = CoreFeatureUtil.getLocale();
					lineage = CommonUtil.getDomainLineage();

					aRequestBean.setLineage(lineage);
					aRequestBean.setLocale(locale);
					aRequestBean.setComponent("primary-nav.component");
					Map<String, Object> compParams = new HashMap<String, Object>();

					aRequestBean.setComponentParams(compParams);
				} else if (nameSet.equals("search-component")) {
					aRequestBean.setLineage("19920_1.0");
					aRequestBean.setLocale("en_US");
					aRequestBean.setComponent("header-secondary-window.component");
					Map<String, Object> compParams = new HashMap<String, Object>();

					compParams.put("groupId", "12345");
					compParams.put("hidePrimaryAccountParam", false);
					compParams.put("originatorParam", null);
					compParams.put("prsnRsnCdParam", null);
					compParams.put("masSessionRoleCDParam", null);
					compParams.put("ecsColorSchemeParam", "default");
					compParams.put("masCurrentRequestedAcTypeParam", "TBA");
					compParams.put("pspAuthorizationCheckedParam", false);
					aRequestBean.setComponentParams(compParams);
					// aRequestBean = mapper.readValue(new
					// File("/home/me/work/apps-config/widget-configurations-service/search.json"),
					// ComponentRequestBean.class);
				}
				// Tile module related activities
				else if (nameSet.equals("tiles")) {
					lineage = getSanitizedTilesHeader(httpRequest,"lineage");
					locale = getSanitizedTilesHeader(httpRequest,"locale");
					String tileGroupName = getSanitizedTilesHeader(httpRequest,"tileGroupName");
					String showPreference = getSanitizedTilesHeader(httpRequest,"showPreference");
					String showMore = getSanitizedTilesHeader(httpRequest,"showMore");
					String fixedContainer = getSanitizedTilesHeader(httpRequest,"fixedContainer");
					String rearrangedTiles = getSanitizedTilesHeader(httpRequest,"rearrangedTiles");
					String measurePerformance = getSanitizedTilesHeader(httpRequest,"measurePerformance");
					String trackResult = getSanitizedTilesHeader(httpRequest,"trackResult");
					String curTilePlid = getSanitizedTilesHeader(httpRequest,"plid");
					String curPorInst = getSanitizedTilesHeader(httpRequest,"instId");
					String delePlid=  getSanitizedTilesHeader(httpRequest,"delPlid");
					String newDesign=  getSanitizedTilesHeader(httpRequest,"newDesign");
					aRequestBean.setLineage(lineage);
					aRequestBean.setLocale(locale);
					String afSwitchOn= getSanitizedTilesHeader(httpRequest,"afSwitchOn");
					String isUCETile = getSanitizedTilesHeader(httpRequest,"isUCETile");
					String uceRows = getSanitizedTilesHeader(httpRequest,"uceRows");
					String uceColumns = getSanitizedTilesHeader(httpRequest,"uceColumns");
					
					aRequestBean.setComponent("tile.component");
					Map<String, Object> compParams = new HashMap<String, Object>();
					compParams.put("tileGroupName", tileGroupName);
					compParams.put("showPreference", showPreference);
					compParams.put("showMore", showMore);
					compParams.put("fixedContainer", fixedContainer);
					compParams.put("instanceName",curPorInst);
					compParams.put("rearrangedTiles", rearrangedTiles);
					compParams.put("measurePerformance", measurePerformance);
					compParams.put("trackResult", trackResult);
					compParams.put("currentTilePlid", curTilePlid);
					compParams.put("curPorInst", curPorInst);
					compParams.put("delePlid", delePlid);
					compParams.put("afSwitchOn", afSwitchOn);
					compParams.put("isUCETile", isUCETile);
					compParams.put("uceRows", uceRows);
					compParams.put("uceColumns", uceColumns);
					
					String tileName = getSanitizedTilesHeader(httpRequest,"tileName");
					String tileSource =  getSanitizedTilesHeader(httpRequest,"tileSource");
					compParams.put("tileName", tileName);
					compParams.put("tileSource", tileSource);

					aRequestBean.setComponentParams(compParams);

					BaseAdapter adapter = null;
					boolean newLogic = false;
					
					if(!ObjectUtils.isEmpty(newDesign) && newDesign.equalsIgnoreCase("Y")) {
						newLogic = true;
					} else  {
						if((!ObjectUtils.isEmpty(newDesign) && newDesign.equalsIgnoreCase("N"))) {
							// newLogic will be false by default
						} else {
							String tilesNewLogic = null;
							tilesNewLogic = System.getProperty("tilesNewLogic");
							if ((!ObjectUtils.isEmpty(tilesNewLogic) && tilesNewLogic.equalsIgnoreCase("true")) ) {
								newLogic = true;
							}
						} 
					}
					
					if(newLogic) {
						adapter = new com.alight.portal.feature.tile.mget.action.TileMultiGetAdapter();
					} else {
						adapter =  new com.aonhewitt.portal.feature.tile.mget.action.TileMultiGetAdapter();
					}

					serviceResponseResult = adapter.process(aRequestBean);
					if (RestServiceInvocationResult.ResponseStatusEnum.SUCCESS == serviceResponseResult
							.getResponseStatus()) {
						return serviceResponseResult.getResponseCMValues();
						
					} else {
						return serviceResponseResult.getResponseCMError();
					}
				} else if (nameSet.equals("initwidget")) {
					lineage = CommonUtil.getDomainLineage();
					String userAction = httpRequest.getParameter("usrAction");
					String serviceName = StringEscapeUtils.escapeHtml4(httpRequest.getParameter("srvc"));
					serviceResponseResult=configManagerService.initWidgetConfig(serviceName);
					if (RestServiceInvocationResult.ResponseStatusEnum.SUCCESS == serviceResponseResult.getResponseStatus()) {
						// will not make call to init config when parallesim is ON and on LOG OFF and the value of triggerInitConfigAfterInitWidget should be true
						boolean triggerInitConfigAfterInitWidget=AppConfigReader.getInstance().isTriggerInitConfigAfterInitWidget();
						if (triggerInitConfigAfterInitWidget && !isConccurrentConfigResolutionEnabled(lineage)&& !StringUtils.equals(userAction,"logoff")) {
							triggerInitConfig();
						}
						return serviceResponseResult.getResponseCMValues();
					} 
					else {
						return serviceResponseResult.getResponseCMError();
					}
				} else if(nameSet.equals("upc")){
					String widgetName = StringEscapeUtils.escapeHtml4(httpRequest.getParameter("widgetName"));
					String widgetInstanceId = StringEscapeUtils.escapeHtml4(httpRequest.getParameter("widgetInstanceId"));
					
					aRequestBean.setComponent("upc.component");
					Map<String, Object> compParams = new HashMap<String,Object>();
					compParams.put("widgetName", widgetName);
					compParams.put("widgetInstanceId", widgetInstanceId);
					aRequestBean.setComponentParams(compParams);
						
					serviceResponseResult = AdapterService.getResponse(aRequestBean);
							
					if (RestServiceInvocationResult.ResponseStatusEnum.SUCCESS == serviceResponseResult.getResponseStatus()) {
						return serviceResponseResult.getResponseCMValues(); 
					} else {
						return serviceResponseResult.getResponseCMError();
					}		
				}else if(nameSet.equals("branding")){
					aRequestBean.setComponent("branding.component");
					Map<String, Object> compParams = new HashMap<String,Object>();
					locale = CoreFeatureUtil.getLocale();
					lineage = CommonUtil.getDomainLineage();
					compParams.put("lineage", lineage);
					compParams.put("locale", locale);
					aRequestBean.setLineage(lineage);
					aRequestBean.setLocale(locale);
					
					//	Start -- UFD-254
					boolean isPostAuth = false;
					String url = httpRequest.getRequestURI();
					String personToken = httpRequest.getHeader("alightPersonSessionToken");
					if(StringUtils.isNotBlank(personToken) && StringUtils.isNotBlank(url) && !url.endsWith("/public")){
						isPostAuth = true;
					}
					compParams.put("isPostAuth", isPostAuth);
					//	End -- UFD-254
					
					//	Start -- UFD2-26342
					boolean isCCMSBranchNameFound = false;
					String ccmsBranchName = getCCMSBranchNameFromARH();
					if(StringUtils.isNotBlank(ccmsBranchName)){
						isCCMSBranchNameFound = true;
					}
					compParams.put("isCCMSBranchNameFound", isCCMSBranchNameFound);
					//	End -- UFD2-26342
					// Start -- EL-7149 -- For multi tenant client
					String tgtSite = getTgtSiteFromARH();
					compParams.put("tgtSite", tgtSite);
					// End -- EL-7149 -- For multi tenant client
					aRequestBean.setComponentParams(compParams);
					serviceResponseResult = AdapterService.getResponse(aRequestBean);
					if (RestServiceInvocationResult.ResponseStatusEnum.SUCCESS == serviceResponseResult.getResponseStatus()) {
						return serviceResponseResult.getResponseCMValues(); 
					} else {
						return serviceResponseResult.getResponseCMError();
					}
				} else if (nameSet.equals("validateblackout")) {				
					
					
					String brokerUserId =  httpRequest.getParameter("brokerUserId");
					
					serviceResponseResult = blackoutUtil.validateBlackout(brokerUserId);
					if (RestServiceInvocationResult.ResponseStatusEnum.SUCCESS == serviceResponseResult.getResponseStatus()) {
						return serviceResponseResult.getResponseCMValues(); 
					} else {
						return serviceResponseResult.getResponseCMError();
					}
				} else {
					if (StringUtils.isNotBlank(nameSet) && "chat".equalsIgnoreCase(nameSet)) {
						Map<String, String> headerMap = createHeaderMap();
						String brokerUserId = getBrokerUserId(headerMap.get("alightPersonSessionToken"));
						if (StringUtils.isNotBlank(brokerUserId) && "NextIT".equalsIgnoreCase(brokerUserId)
								&& StringUtils.isNotBlank(headerMap.get("alightRequestHeader"))) {
							InfoTypeLogEventHelper.logInfoEvent(WidgetRestController.class.getName(),
									"channel-widgetconfigurations-service~~alightRequestHeader~~~"
											+ headerMap.get("alightRequestHeader"));
						}
					}
					
					locale = CoreFeatureUtil.getLocale();
					lineage = CommonUtil.getDomainLineage();
					aRequestBean = prepateRequestBeanForGenericConfigSet(lineage, locale, nameSet);
				}

			}			if(!aRequestBean.getComponent().equals(KeyConstants.BLANK)){
				//make multi init for portal parms
				configManagerService.multiGetInitForClientParms(lineage,locale);

        		serviceResponseResult = AdapterService.getResponse(aRequestBean);
					
				if (nameSet.equals("initConfig")) {

					Map<Object, Object> AElockMap = new HashMap<Object, Object>();
					AElockMap.put("AE_LOCK", AELockUtil.isAELockEnabled());

					if (!ObjectUtils.isEmpty(AElockMap) && !AElockMap.isEmpty()) {

						List<Map<Object, Object>> expressionsMapList = (ArrayList<Map<Object, Object>>) serviceResponseResult
								.getResponseCMValues();
						if (!expressionsMapList.isEmpty()) {
							Boolean reorderValuePresent = false;
							expressionsMapList.get(0).put("properties", AElockMap);
							
							if(!ObjectUtils.isEmpty(expressionsMapList.get(0).get(ASSET_UCE))){
								Map<Object,Object> mapItem = expressionsMapList.get(0);
								Map <Object,Object> newAssetmap = new HashMap<Object,Object>();
								Map<Object, Object> map = (Map<Object, Object>) expressionsMapList.get(0).get(ASSET_UCE);
								if(!ObjectUtils.isEmpty(map) && !map.isEmpty()){
									Map<Object, Object> map1 = (Map<Object, Object>) map.get("WLF_HOME_REORDER_ASSET_GRP");
									if(!ObjectUtils.isEmpty(map.get("WLF_HOME_REORDER_ASSET_GRP"))){
										newAssetmap.put("WLF_HOME_REORDER_ASSET_GRP", map1);
									}
								}
								for(int i=1;i<=4;i++){
									String value=getUCEAssetValue(newAssetmap, "WLF_HOME_REORDER_ASSET_GRP", "WLF_HOME_REORDER_TEMPLATE_"+i+"_ASSET");
									if(!ObjectUtils.isEmpty(value) && !value.isEmpty()){
										expressionsMapList.get(0).put("homePageReorderItem", value);
										reorderValuePresent = true;
										break;
									}
									if(reorderValuePresent==false){
										expressionsMapList.get(0).put("homePageReorderItem", getUCEAssetValue(newAssetmap, "WLF_HOME_REORDER_ASSET_GRP", "WLF_HOME_REORDER_TEMPLATE_DEFAULT_ASSET"));
										break;
									}
								}
							}
						}
						
						
						// Start -- DCH-3484/DCH-3471 implementation to generate uuid, clientId, sessionId, globalSessionId						
						generateUserActivityData(httpRequest, expressionsMapList);
						// End -- DCH-3484/DCH-3471 implementation to generate uuid, clientId, sessionId, globalSessionId
						
						
					}

				}
				
				if (nameSet.equals("afinit")) { 
					List<Map<Object,Object>> expressionsMapList = (ArrayList<Map<Object,Object>>)serviceResponseResult.getResponseCMValues();
					Map<Object,Object> expressions = new HashMap<Object,Object>();
					Map<Object,Object> assets = new HashMap<Object,Object>();
					for(Map<Object,Object> expMap : expressionsMapList){
						if(!ObjectUtils.isEmpty(expMap.get("expr"))){
							expressions=(Map<Object, Object>) expMap.get("expr");
							assets = (Map<Object, Object>) expMap.get("asset");
							break;
						}
					}
						Map<Object, Object> ivaJsSrc = null;
						Map<Object, Object> gaconfig  = (Map<Object, Object>) yamlConfig.getYamlConfig().get("gaConfigurations");
						Map<Object, Object> routeconfig=null;
						if (MapUtils.isNotEmpty(assets)) {
							routeconfig = pageInfoUtil.getPageInfoFromAsset(assets);
						}
						String ivaJsSrcUrl =  (String) yamlConfig.getYamlConfig().get("ivaJavaScriptSrc");
						if(!ObjectUtils.isEmpty(ivaJsSrcUrl)){
							ivaJsSrc = new HashMap<Object,Object>();
							ivaJsSrc.put("ivaJsSrcUrl", ivaJsSrcUrl);
						}
						if (!ObjectUtils.isEmpty(routeconfig) && !routeconfig.isEmpty()) {
							serviceResponseResult.addResponseCMValue(routeconfig);
						}
						
						if (!ObjectUtils.isEmpty(gaconfig) && !gaconfig.isEmpty()) {
							serviceResponseResult.addResponseCMValue(gaconfig);
						}
						
						if (!ObjectUtils.isEmpty(ivaJsSrc) && !ivaJsSrc.isEmpty()) {
							serviceResponseResult.addResponseCMValue(ivaJsSrc);
						}
				}
				else if(nameSet.equals("blackout")) {
					Map<Object, Object> gaconfig  = (Map<Object, Object>) yamlConfig.getYamlConfig().get("gaConfigurations");
					if (!ObjectUtils.isEmpty(gaconfig) && !gaconfig.isEmpty()) {
						serviceResponseResult.addResponseCMValue(gaconfig);
					}
				}
        		//ConfigurationParmUtil.saveClntPrmInRedisForLineage(lineage);
        	}
    		else{
    			serviceResponseResult = new RestServiceInvocationResult();
    			Map<Object, Object> errorMap = new HashMap<Object, Object>();
    			List<Map<Object, Object>> errorList = new ArrayList<Map<Object, Object>>();
    			
    			errorMap.put("Error", "Bad Request!, componentName not available :"+aRequestBean.getComponent());
    			errorList.add(errorMap);
    			serviceResponseResult.setResponseStatus(RestServiceInvocationResult.ResponseStatusEnum.FAILURE);
    			serviceResponseResult.setResponseCMError(errorList);
    			ErrorLogEventHelper.logErrorEvent(ConfigManagerService.class.getName(), "Bad Request! getResponse method failed, componentName not available :"+aRequestBean.getComponent(), 
    					"getComponentResponse method failed",
    					"", "", ErrorLogEvent.ERROR_SEVERITY);
    		}
			if (RestServiceInvocationResult.ResponseStatusEnum.SUCCESS == serviceResponseResult.getResponseStatus()) {

				 /*if (RestJSONLogEventHelper.isEnabled(this.getClass().getName()))
			        {
					 RestJSONLogEventHelper.stop(this.getClass().getName(), aRequestBean.toString(),
			            		(serviceResponseResult.getResponseCMValues()).toString(),"/channel/widgetConfigurations/"+nameSet);
			        }*/
				List<Map<Object, Object>> responseCMValues = serviceResponseResult.getResponseCMValues();
				 
				
				if (appProperties.isPreAuthCachingEnabled() && isPreAuthComponent) {
					PreAuthUtil.setPreAuthDataInCache(nameSet, appProperties.getDcRcsPreAuthTTL(), responseCMValues);
				}
				return responseCMValues;

			} else {
				return serviceResponseResult.getResponseCMError();
			}

		} 
		catch (RuntimeException e) {
			ErrorLogEventHelper.logErrorEvent(WidgetRestController.class.getName(), "Exception while resolving generic service", "getService method failed", e,
					ErrorLogEvent.ERROR_SEVERITY);
			return cmServicesUtil.buildServcieErrorResponseMap("Error", e.getMessage());
		}
		catch (Exception e) {
			ErrorLogEventHelper.logErrorEvent(WidgetRestController.class.getName(), "Exception while resolving generic service", "getService method failed", e,
					ErrorLogEvent.ERROR_SEVERITY);
			return cmServicesUtil.buildServcieErrorResponseMap("Error", e.getMessage());
		}

	}

	private void generateUserActivityData(HttpServletRequest httpRequest,
			List<Map<Object, Object>> expressionsMapList) {
		AHUser ahUser = AHUserLocalServiceUtil.getAHUser();
		String uuid = ahUser.getUUID();
		
		String clientId = AppContainerProvider.getContainer().getLineage();

		Map<String, String> userData = AppContainerProvider.getContainer().getChannelRequestData().getUserData();
		String globalSessionId = getOrEmpty(userData, "gblsId");
		
		String alightPersonSessionToken = httpRequest.getHeader("alightPersonSessionToken");
		String sessionId = LogEventDataHandler.getSessionId(httpRequest, alightPersonSessionToken);
		
		Map<Object, Object> userActivityTrackingIdMap = new HashMap<Object, Object>();
		userActivityTrackingIdMap.put("uuid", (StringUtils.isNotBlank(uuid) ? uuid : StringUtils.EMPTY));
		userActivityTrackingIdMap.put("clientId", clientId);
		userActivityTrackingIdMap.put("globalSessionId", globalSessionId);
		userActivityTrackingIdMap.put("sessionId", sessionId);
		
		String globalpersonidentifer = null;

		try {
			globalpersonidentifer = commonUtils.getGlobalPersonIdentifier(expressionsMapList);
			if(!(ObjectUtils.isEmpty(globalpersonidentifer))) {
				userActivityTrackingIdMap.put("globalPersonIdentifier", globalpersonidentifer);
			}
		}
		catch(Exception e){
			ErrorLogEventHelper.logErrorEvent(WidgetRestController.class.getName(),
					"Exception while fetching globalPersonIdentifier", "getGlobalPersonIdentifier", e, ErrorLogEvent.ERROR_SEVERITY);
		}   

		expressionsMapList.get(0).put("userActivityTrackingIds", userActivityTrackingIdMap);
	}


	private ComponentRequestBean prepateRequestBeanForGenericConfigSet(String lineage, String locale, String nameSet){

		ComponentRequestBean aRequestBean = new ComponentRequestBean();
		aRequestBean.setLineage(lineage);
		aRequestBean.setLocale(locale);
		aRequestBean.setComponent("generic-widget.component");
		aRequestBean.setConfigSetName(nameSet.toUpperCase());
		Map<String, Object> compParams = new HashMap<String, Object>();
		aRequestBean.setComponentParams(compParams);
	
		return aRequestBean;
	}
	
	public static String getOrEmpty(Map<String, String> userData, String key) {
		if (!ObjectUtils.isEmpty(userData) && userData.containsKey(key)) {
			String paramValue = userData.get(key);
			if (StringUtils.isNotBlank(paramValue)) {
				return paramValue;
			}
		}
		return StringUtils.EMPTY;
	}
	private void triggerInitConfig() {
		try {
			String locale = CoreFeatureUtil.getLocale();
			String lineage = CommonUtil.getDomainLineage();
			String nameSet = "initConfig";
			ComponentRequestBean aRequestBean = prepateRequestBeanForGenericConfigSet(lineage, locale, nameSet);
			AdapterService.getResponse(aRequestBean);
		} catch (Exception e) {
			ErrorLogEventHelper.logErrorEvent(WidgetRestController.class.getName(),
					"Exception while calling initConfig from initwidget API", "triggerInitConfig", e,
					ErrorLogEvent.ERROR_SEVERITY);

		}
	}
	private  boolean isConccurrentConfigResolutionEnabled(String lineage) {
		boolean isConccurrentConfigResolutionEnabled = false;
		boolean ucceInitiator = false;
		try {
			
			MessageAppContainer msgAppContainer = AppContainerProvider.getContainer();
			if (!ObjectUtils.isEmpty(msgAppContainer) && !ObjectUtils.isEmpty(msgAppContainer.getAttribute("ENABLE_PARALLEL_CNF_RESOLUTION_EXPR_OUTPUT"))) {
				isConccurrentConfigResolutionEnabled = (Boolean) msgAppContainer.getAttribute("ENABLE_PARALLEL_CNF_RESOLUTION_EXPR_OUTPUT");

			} else {
				if (!UCCERequestor.isUCCE()) {
					ucceInitiator = true;
					UCCERequestor.markRequestUCCE();
				}
				isConccurrentConfigResolutionEnabled = ExpressionsLocalServiceUtil.evaluateExpression(lineage, "ENABLE_PARALLEL_CNF_RESOLUTION",
						false);
				if (!ObjectUtils.isEmpty(msgAppContainer)) {
					msgAppContainer.setAttribute("ENABLE_PARALLEL_CNF_RESOLUTION_EXPR_OUTPUT", Boolean.valueOf(isConccurrentConfigResolutionEnabled));//new Boolean(isConccurrentConfigResolutionEnabled)
				}
			}
		} catch (Exception e) {
			DebugLogEventHelper.logDebugEvent(this.getClass().getName(),
					"Exception occured while evaluating the switch ENABLE_PARALLEL_CNF_RESOLUTION..", "isConsccurrentConfigResolutionEnabled",
					"");
			isConccurrentConfigResolutionEnabled = false;
		}
		finally{
			if(ucceInitiator ){
				UCCERequestor.reset();
			}
		}
		return isConccurrentConfigResolutionEnabled;
	}
	
	private String getSanitizedTilesHeader(HttpServletRequest request,String headerName){
		String sanitizedHeader =
		        org.apache.commons.lang.StringEscapeUtils.escapeHtml(
		        org.apache.commons.lang.StringEscapeUtils.escapeJavaScript(  
		          request.getHeader(headerName)
		        ));
		return sanitizedHeader;
	}
	
	private String getCCMSBranchNameFromARH() {
		String ccmsBranchName = StringUtils.EMPTY;
		try {
			HttpServletRequest httpServletRequest = ((ServletRequestAttributes) RequestContextHolder
					.getRequestAttributes()).getRequest();
			String alightRequestHeader = httpServletRequest.getHeader("alightRequestHeader");
			RequestHeader requestHeader = RequestHeader.parse(alightRequestHeader);
			if(!ObjectUtils.isEmpty(requestHeader)){
				String channelReqData = requestHeader.getChannelRequestData();
				if(StringUtils.isNotBlank(channelReqData)){
					String[] channelRequestDataArray = channelReqData.split("::");
					if(!ObjectUtils.isEmpty(channelRequestDataArray) && channelRequestDataArray.length > 0){
						for (int i = 0; channelRequestDataArray.length > 1 && i < channelRequestDataArray.length - 1; i += 2) {
							if ("CCMSBranchName".equals(channelRequestDataArray[i])) {
								ccmsBranchName = channelRequestDataArray[i + 1];
								break;
							}
						}
					}
				}
			}
		} catch (Exception e) {
			ErrorLogEventHelper.logErrorEvent(this.getClass().getName(),
					"Exception occurred while parsing/getting CCMSBranchName from alightRequestHeader.channelRequestData.",
					"getCCMSBranchNameFromARH()", e, "ERROR");
		}
		return ccmsBranchName;
	}
	
	private String getTgtSiteFromARH() {
		String tgtSite = null;
		try {
			HttpServletRequest httpServletRequest = ((ServletRequestAttributes) RequestContextHolder
					.getRequestAttributes()).getRequest();
			String alightRequestHeader = httpServletRequest.getHeader("alightRequestHeader");
			RequestHeader requestHeader = RequestHeader.parse(alightRequestHeader);
			if(!ObjectUtils.isEmpty(requestHeader)){
				String channelReqData = requestHeader.getChannelRequestData();
				if(StringUtils.isNotBlank(channelReqData)){
					String[] channelRequestDataArray = channelReqData.split("::");
					if(!ObjectUtils.isEmpty(channelRequestDataArray) && channelRequestDataArray.length > 0){
						for (int i = 0; channelRequestDataArray.length > 1 && i < channelRequestDataArray.length - 1; i += 2) {
							if ("tgtSite".equals(channelRequestDataArray[i])) {
								tgtSite = channelRequestDataArray[i + 1];
								break;
							}
						}
					}
				}
			}
		} catch (Exception e) {
			ErrorLogEventHelper.logErrorEvent(this.getClass().getName(),
					"Exception occurred while parsing/getting tgtSite from alightRequestHeader.channelRequestData.",
					"getTgtSiteFromARH()", e, "ERROR");
		}
		return tgtSite;
	}
	
	public static String getUCEAssetValue(Map<Object, Object> goldDataMap, String assetGroup, String assetKey) {
		String assetValue = null;
			if (!ObjectUtils.isEmpty(goldDataMap) && goldDataMap.containsKey(assetGroup)) {
				Map<Object, Object> assets = (Map<Object, Object>) goldDataMap.get(assetGroup);
				if (!ObjectUtils.isEmpty(assets) && assets.containsKey(assetKey)) {
					//Map<Object, Object> assetValueMap = (Map<Object, Object>) assets.get(assetKey);
					com.aonhewitt.portal.configuration.modules.assetmanagement.model.AssetDTO assetDto =  (AssetDTO) assets.get(assetKey);
					assetValue = !ObjectUtils.isEmpty(assetDto.getAssetValue())?assetDto.getAssetValue().toString():null;
					
				}
			}
		
		return assetValue;
	}
	
	private Map<String, String> createHeaderMap() {
		Map<String, String> headerMap = new HashMap<String, String>();
		RequestAttributes context = RequestContextHolder.currentRequestAttributes();
		if (context != null) {
			ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) context;
			HttpServletRequest request = servletRequestAttributes.getRequest();
			headerMap.put("alightPersonSessionToken", request.getHeader("alightPersonSessionToken"));
			headerMap.put("alightRequestHeader", request.getHeader("alightRequestHeader"));
		}
		return headerMap;
	}

	private String getBrokerUserId(String alightPersonSessionToken) {
		String brokerUserId = "";
		try {
			if(StringUtils.isNotBlank(alightPersonSessionToken)) {
				PersonSessionToken personSessionToken = PersonSessionToken.parse(alightPersonSessionToken);
				if (personSessionToken != null && personSessionToken.getBrokerUserId() != null) {
					brokerUserId = personSessionToken.getBrokerUserId();
				}
			}
		} catch (Exception e) {
			ErrorLogEventHelper.logErrorEvent(IVAHandler.class.getName(),
					"Error: while parsing alightPersonSessionToken to PersonSessionToken object.",
					"fetchUUID()", "", "", ErrorLogEvent.ERROR_SEVERITY);
		}
		return brokerUserId;
	}
	
}