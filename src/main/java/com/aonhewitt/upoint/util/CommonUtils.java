package com.aonhewitt.upoint.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.context.ApplicationContext;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.alight.portal.core.udp.v2.dto.person.PersonsV2;
import com.alight.portal.udp.v2.feign.PersonsV2ServiceFeignClient;
import com.aonhewitt.beans.GenericRequestBean;
import com.aonhewitt.logging.events.ErrorLogEvent;
import com.aonhewitt.logging.helpers.ErrorLogEventHelper;
import com.aonhewitt.portal.util.AppContainerProvider;
import com.aonhewitt.portal.util.MessageAppContainer;
import com.aonhewitt.upoint.feign.helper.FeignApplicationContext;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;

/**
 * 
 * @author Yogesh M
 *
 */
@Service
public class CommonUtils {

	private static List<String> aliasList = new ArrayList<String>();
	
	private static ObjectMapper mapper = new ObjectMapper();

	private static List<String> NON_SCEURE_TOKENS = Arrays.asList(new String[] { "<script>","</script>" });
	
	public static final String NON_SECURE_REQUEST_BODY_ATTRIBUTES_MESSAGE="NON_SECURE_REQUEST_BODY_ATTRIBUTES_MESSAGE";
	
	public static final String FEATURE_UCE="feature_uce";
	public static final String PENDO_EXPRESSION = "WLF_PENDO_TRACKING_ENABLED";
	

	public static List<String> getAliasList() {
		return aliasList;
	}

	public static void setAliasList(List<String> aliasList) {
		CommonUtils.aliasList = aliasList;
	}

	public static List<String> getNonSecureToeknList() {
		return new ArrayList<String>(NON_SCEURE_TOKENS);
	}

	/**
	 * Return non null RequestAttribute String
	 * 
	 * @param request
	 * @param param
	 * @return
	 */
	public static String getRequestAttributeString(HttpServletRequest request, String param) {
		String reqparam = (String) request.getAttribute(param);
		if (ObjectUtils.isEmpty(reqparam)) {
			reqparam = "";
		}
		return reqparam;
	}

	/**
	 * method to prepare error values and forward control to /error controller
	 * 
	 * @param req
	 * @param res
	 * @param errorMsg
	 * @throws ServletException
	 * @throws IOException
	 */
	public static void displayCustomErrorMessage(HttpServletRequest request, ServletResponse response, String errorMsg)
			throws ServletException, IOException {
		request.setAttribute("CustomProcessingError", "True");
		request.setAttribute("status", "Request Processing Failed");
		request.setAttribute("error", "Bad Request for '" + request.getRequestURI() + "' resource");
		request.setAttribute("message", errorMsg);
		request.setAttribute("path", request.getRequestURI());
		RequestDispatcher rd = request.getRequestDispatcher("/error");
		rd.forward(request, response);
	}
	
	/**
	 * 
	 * @param requestBean
	 * @return
	 */
	public static Map<String,String> validateRequestForXSSAttacks(GenericRequestBean requestBean) {
		boolean isValidRequest = true;
		Map<String,String> validationStatus = null;		
		ObjectWriter withDefaultPrettyPrinter = mapper.writer().withDefaultPrettyPrinter();
		String jsonRequst = null;
		try {
			jsonRequst=withDefaultPrettyPrinter.writeValueAsString(requestBean);
		} catch (JsonProcessingException e) {
			ErrorLogEventHelper.logErrorEvent(CommonUtils.class.getName(),
					"No functional impact!! Unable to parse the request body. XSS attack check will not be performed.",
					"com.aonhewitt.upoint.util.CommonUtils.validateRequestForXSSAttacks(GenericRequestBean)", e,
					"", ErrorLogEvent.ERROR_SEVERITY);
		}
		if(jsonRequst!=null) {
			for (String token : NON_SCEURE_TOKENS) {
				if(jsonRequst.toLowerCase().contains(token.toLowerCase())) {
					isValidRequest=false;
					break;
				}
			}			
		}
		if(isValidRequest==false) {
			validationStatus = new HashMap<String,String>();
			validationStatus.put("Error!", CommonUtils.getNonSecureErroMessage());
			MessageAppContainer messageAppContainer = AppContainerProvider.getContainer();
			if (messageAppContainer != null) {
				messageAppContainer.setAttribute(CommonUtils.NON_SECURE_REQUEST_BODY_ATTRIBUTES_MESSAGE, CommonUtils.NON_SECURE_REQUEST_BODY_ATTRIBUTES_MESSAGE);
			}
		}
		return validationStatus;
	}
	
	/**
	 * 
	 * @return
	 */
	public static String getNonSecureErroMessage() {
		String message = "One of the request body attributes contains value, vulnerable to XSS attack. "
				+ "Please validate your request to make sure that request should not contain any of the java scripting elemlents.";
		return message;
	}
	
	public String getGlobalPersonIdentifier(List<Map<Object, Object>> expressionsMapList) {

		boolean pendoEnabled = false;
		String globalpersonidentifer = null;
		if(!ObjectUtils.isEmpty(expressionsMapList.get(0).get(FEATURE_UCE))){
			Map<Object, Object> expressionMap = (Map<Object, Object>) expressionsMapList.get(0).get(FEATURE_UCE);
			if (null != expressionMap && expressionMap.containsKey(PENDO_EXPRESSION) && expressionMap.get(PENDO_EXPRESSION)!=null) {
				pendoEnabled = (Boolean) expressionMap.get(PENDO_EXPRESSION);
			}
		}
		if(pendoEnabled) {
			ResponseEntity<PersonsV2> personV2Response = null ;
			PersonsV2 personV2 = null;

			try {
				ApplicationContext context = FeignApplicationContext.getAppContext();
				PersonsV2ServiceFeignClient personV2FeignClient = context.getBean(PersonsV2ServiceFeignClient.class);

				personV2Response = personV2FeignClient.getPersonsV2Response();
				if(!(ObjectUtils.isEmpty(personV2Response))) {
					personV2 = personV2Response.getBody();
					if(!(ObjectUtils.isEmpty(personV2))) {
						globalpersonidentifer = personV2.getGlobalPersonIdentifier();
					}
				}
			}
			catch(Exception e){
				ErrorLogEventHelper.logErrorEvent(CommonUtils.class.getName(),
						"Exception while calling persons-v2 call", "generateUserActivityData", e, ErrorLogEvent.ERROR_SEVERITY);
			} 

		}
		return globalpersonidentifer;
	}
}