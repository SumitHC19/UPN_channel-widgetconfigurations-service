package com.aonhewitt.upoint.core.controller.config;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
/**
 * 
 * @author Yogesh Mittal
 * 
 * Controller used to fetch the upoint configuratrion using POST Http verb
 */
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.aonhewitt.beans.GenericRequestBean;
import com.aonhewitt.logging.events.ErrorLogEvent;
import com.aonhewitt.logging.helpers.ErrorLogEventHelper;
import com.aonhewitt.portal.configuration.UCCERequestor;
import com.aonhewitt.portal.core.service.AHUserLocalServiceUtil;
import com.aonhewitt.portal.core.service.ExpressionsLocalServiceUtil;
import com.aonhewitt.portal.core.service.util.CmServicesUtil;
import com.aonhewitt.portal.core.service.util.CommonUtil;
import com.aonhewitt.portal.feature.util.CoreFeatureUtil;
import com.aonhewitt.portal.service.delegators.ConfigManagerService;
import com.aonhewitt.portal.util.AppContainerProvider;
import com.aonhewitt.portal.util.MessageAppContainer;
import com.aonhewitt.portal.util.RestServiceInvocationResult;
import com.aonhewitt.upoint.conf.AppConfig;
import com.aonhewitt.upoint.core.service.util.ExpressionsCacheUtility;
import com.aonhewitt.upoint.util.CommonUtils;
import com.aonhewitt.upoint.util.ConfigurationListUtils;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/channel/configurationList/")
//@Profile({"localdev", "int", "qa","ste"})
//@ConditionalOnProperty(name = "app.enableDebugController", havingValue = "true", matchIfMissing = false)
public class ConfigurationListController {

	@Autowired
	private ConfigManagerService configManagerService;

	@Autowired
	private CmServicesUtil cmServicesUtil;

	@Autowired
	private ConfigurationListUtils configurationListUtils;

	@Autowired
	private AppConfig appConfig;

	@Autowired
	private ExpressionsCacheUtility expressionsCacheUtility;

	private String profile;

	private List<String> profileSet = Arrays.asList(new String[] { "localdev", "int", "qa", "ste" });
	private List<String> profileSetUptoQc = Arrays.asList(new String[] { "localdev", "int", "qa", "ste", "qc" });
	
	@Value("${app.xssAttackCheckEnabled:true}")
	private boolean xssAttackCheckEnabled; 
	
	@PostMapping(value = "get", produces = "application/json")
	public Object getConfigurationList(@RequestBody GenericRequestBean aRequestBean) {

		if (!ObjectUtils.isEmpty(aRequestBean)) {
			if (xssAttackCheckEnabled == true) {
				Map<String, String> validationFailStatus = CommonUtils.validateRequestForXSSAttacks(aRequestBean);
				if (validationFailStatus != null) {
					return validationFailStatus;
				}
			}
			if (!StringUtils.isEmpty(aRequestBean.getLocale())) {

				AHUserLocalServiceUtil.getAHUser().setLocale(aRequestBean.getLocale());
			} else {
				String locale = CoreFeatureUtil.getLocale();
				aRequestBean.setLocale(locale);
			}

			if (StringUtils.isEmpty(aRequestBean.getLineage())) {
				String lineage = CommonUtil.getDomainLineage();
				aRequestBean.setLineage(lineage);
			}

			String operationType = aRequestBean.getOperation();
			// UFD-20568 -- Changes for expression Caching
			HttpServletRequest httpRequest = null;
			RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
			ServletRequestAttributes servletRequestAttributes = null;
			if (!ObjectUtils.isEmpty(requestAttributes)) {
				servletRequestAttributes = (ServletRequestAttributes) requestAttributes;
				httpRequest = servletRequestAttributes.getRequest();

			}
			expressionsCacheUtility.evalAndPopulateInitConfigExpressionValuesIntoThreadLocal(httpRequest);

			configManagerService.multiGetInitForClientParms(aRequestBean.getLineage(), aRequestBean.getLocale());
			Object response = processConfigurationOperations(aRequestBean, operationType);

			expressionsCacheUtility.storeResolvedExpressionNamesIntoRedis(httpRequest);
			// UFD-20568 --Changes for Expression Caching Ends

			return response;
		}

		return buildResponseMessage("Error!", "Invalid Request!!!");
	}

	@PostMapping(value = "get/public", produces = "application/json")
	public Object getPreAuthWidgetConfigurationList(@RequestBody GenericRequestBean aRequestBean) {
		if (!ObjectUtils.isEmpty(aRequestBean)) {			
			if (xssAttackCheckEnabled == true) {
				Map<String, String> validationFailStatus = CommonUtils.validateRequestForXSSAttacks(aRequestBean);
				if (validationFailStatus != null) {
					return validationFailStatus;
				}
			}
			HttpServletRequest httpRequest = null;
			RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
			ServletRequestAttributes servletRequestAttributes = null;
			if (!ObjectUtils.isEmpty(requestAttributes) && "prod".equalsIgnoreCase(appConfig.getProfile()) == false) {
				servletRequestAttributes = (ServletRequestAttributes) requestAttributes;
				httpRequest = servletRequestAttributes.getRequest();
				String requestInfoLogParam = httpRequest.getParameter("requestInfoLogParam");
				String showCachedShortCacheInfo = httpRequest.getParameter("showCachedShortCacheInfo");
				String showJVMShortCacheInfo = httpRequest.getParameter("showJVMShortCacheInfo");
				String allClients = httpRequest.getParameter("allClients");
				if (StringUtils.isNotBlank(requestInfoLogParam)
						&& !ObjectUtils.isEmpty(AppContainerProvider.getContainer())) {
					AppContainerProvider.getContainer().setAttribute("requestInfoLogParam", requestInfoLogParam);
				}
				if (StringUtils.isNotBlank(showCachedShortCacheInfo)
						&& !ObjectUtils.isEmpty(AppContainerProvider.getContainer())) {
					AppContainerProvider.getContainer().setAttribute("showCachedShortCacheInfo",
							showCachedShortCacheInfo);
				}
				if (StringUtils.isNotBlank(showJVMShortCacheInfo)
						&& !ObjectUtils.isEmpty(AppContainerProvider.getContainer())) {
					AppContainerProvider.getContainer().setAttribute("showJVMShortCacheInfo", showJVMShortCacheInfo);
				}
				if (StringUtils.isNotBlank(allClients) && !ObjectUtils.isEmpty(AppContainerProvider.getContainer())) {
					AppContainerProvider.getContainer().setAttribute("allClients", allClients);
				}
			}
			if (!StringUtils.isEmpty(aRequestBean.getLocale())) {
				AppContainerProvider.getContainer().setPreAuthLocale(aRequestBean.getLocale());
			}

			if (!StringUtils.isEmpty(aRequestBean.getLineage())) {
				AppContainerProvider.getContainer().setLineage(aRequestBean.getLineage());
			}

			aRequestBean.setLineage(AppContainerProvider.getContainer().getLineage());
			aRequestBean.setLocale(AppContainerProvider.getContainer().getPreAuthLocale());

			// set Pre auth request

			String operationType = aRequestBean.getOperation();
			configManagerService.multiGetInitForClientParms(aRequestBean.getLineage(), aRequestBean.getLocale());
			Object response = processConfigurationOperations(aRequestBean, operationType);
			return response;
		}

		return buildResponseMessage("Error!", "Invalid Request!!!");
	}

	/**
	 * return config set
	 * 
	 * @param aRequestBean
	 * @return
	 */
	private List<Map<Object, Object>> performConfigSetOperation(GenericRequestBean aRequestBean) {
		RestServiceInvocationResult serviceResponseResult = null;
		try {
			if ("true".equalsIgnoreCase(aRequestBean.getUce())) {
				UCCERequestor.markRequestUCCE();
			}
			serviceResponseResult = configManagerService.getResponse(aRequestBean);
			if (RestServiceInvocationResult.ResponseStatusEnum.SUCCESS == serviceResponseResult.getResponseStatus()) {
				return serviceResponseResult.getResponseCMValues();
			} else {
				return serviceResponseResult.getResponseCMError();
			}

		} catch (Exception e) {
			ErrorLogEventHelper.logErrorEvent(ConfigurationListController.class.getName(),
					"Exception while resolving generic service", "getService method failed", e,
					ErrorLogEvent.ERROR_SEVERITY);
			return cmServicesUtil.buildServcieErrorResponseMap("Error", e.getMessage());
		} finally {
			if (UCCERequestor.isUCCE() == true) {
				UCCERequestor.reset();
			}
		}
	}

	/**
	 * @param aRequestBean
	 * @param operationType
	 * @return
	 */
	private Object processConfigurationOperations(GenericRequestBean aRequestBean, String operationType) {

		if ("configSet".equalsIgnoreCase(operationType)) {
			return performConfigSetOperation(aRequestBean);
		} else if ("upc".equalsIgnoreCase(operationType)) {

			return configurationListUtils.getUpcConfig(aRequestBean);

		}

		else if (isProfileValid(operationType)) {
			MessageAppContainer msgAppContainer = AppContainerProvider.getContainer();
			String clientId = msgAppContainer.getPrimaryClientId();
			boolean isUCE = "true".equalsIgnoreCase(String.valueOf(aRequestBean.getUce()));
			if ("ahUser".equalsIgnoreCase(operationType)) {
				return configurationListUtils.getAhUserObject();
			} else if ("requestPayload".equalsIgnoreCase(operationType)) {
				return configurationListUtils.getRequestPayload();
			} else if ("dbDetails".equalsIgnoreCase(operationType)) {
				return configurationListUtils.testJNDIConnections();
			} else if ("cacheSet".equalsIgnoreCase(operationType)) {
				return configurationListUtils.verifyCacheKey(aRequestBean);
			} else if ("udp".equalsIgnoreCase(operationType)) {
				return configurationListUtils.getUdpData();
			} else if ("cacheAnalysis".equalsIgnoreCase(operationType)) {
				return configurationListUtils.printCacheKeySize(aRequestBean);
			} else if ("jedisPoolConfig".equalsIgnoreCase(operationType)) {
				return configurationListUtils.printJedisPoolConfig();
			} else if ("udpNext".equalsIgnoreCase(operationType)) {
				return configurationListUtils.getUdpNextPersonSchemaObject();
			} else if ("clearHro".equalsIgnoreCase(operationType)) {
				return configurationListUtils.clearHro();
			} else if ("clearHroAll".equalsIgnoreCase(operationType)) {
				return configurationListUtils.clearHroAll();
			} else if ("clearAFLinksCache".equalsIgnoreCase(operationType)) {
				return configurationListUtils.clearAFLinksCache(clientId);
			} else if ("clearAFLinksCacheAll".equalsIgnoreCase(operationType)) {
				return configurationListUtils.clearAFLinksCache("all");
			} else if ("clearLinksCache".equalsIgnoreCase(operationType)) {
				return configurationListUtils.clearLinksCache(clientId, isUCE);
			} else if ("clearLinksCacheAll".equalsIgnoreCase(operationType)) {
				return configurationListUtils.clearLinksCache("all", isUCE);
			} else if ("clearExpressionsCache".equalsIgnoreCase(operationType)) {
				return configurationListUtils.clearExpressionsCache(clientId, isUCE);
			} else if ("clearExpressionsCacheAll".equalsIgnoreCase(operationType)) {
				return configurationListUtils.clearExpressionsCache("all", isUCE);
			} else if ("getBusinessObject".equalsIgnoreCase(operationType)) {
				return configurationListUtils.getBusinessDomainObject();

			} else if ("getUpdatedARHForsharedAccess".equalsIgnoreCase(operationType)) {
				return configurationListUtils.getUpdatedARHForSharedAccess();

			}
		}

		return buildResponseMessage("Error!", "Unsupported Request Operation!!!");

	}

	/**
	 * @param key
	 * @param value
	 * @return
	 */
	private Map<String, String> buildResponseMessage(final String key, final String value) {
		Map<String, String> message = new HashMap<String, String>();
		message.put(key, value);
		return message;
	}

	private Boolean isProfileValid(String operationType) {
		profile = appConfig.getProfile();
		if ("ahUser".equalsIgnoreCase(operationType) || "getBusinessObject".equalsIgnoreCase(operationType)) {
			if (profileSetUptoQc.contains(profile))
				return true;
			else if("prod".equalsIgnoreCase(profile) && "ahUser".equalsIgnoreCase(operationType)) {
				String lineage = AppContainerProvider.getContainer().getLineage();
				boolean isAhUserEligibleForProd = ExpressionsLocalServiceUtil.evaluateExpression(lineage, "IS_DEBUG_PAGE_ELIG",false);
				return isAhUserEligibleForProd;
			} else
				return false;
		} else {
			if (profileSet.contains(profile))
				return true;
			else
				return false;
		}

	}

}