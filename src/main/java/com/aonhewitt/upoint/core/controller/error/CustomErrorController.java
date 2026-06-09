package com.aonhewitt.upoint.core.controller.error;

import java.util.HashMap;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.error.ErrorAttributeOptions.Include;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.handler.DispatcherServletWebRequest;

import com.aonhewitt.logging.events.ErrorLogEvent;
import com.aonhewitt.logging.helpers.ErrorLogEventHelper;
import com.aonhewitt.upoint.util.CommonUtils;
import org.springframework.util.ObjectUtils;
/**
 * CustomErrorController  - Implementation should handle all application errors 
 *  e.g.
 *  - Handle All runtime exceptions, 
 *  - Prevent exception propagation to container, 
 *  - Handled the white page shown to user Gracefully 
 *     (e.g. any call (GET request) to personMessages/ lands in blank page due to missing ASG-Session token), 
 *  - Handle all Http error codes
 *   
 * 
 * @author Yogesh Mittal
 *
 */

@Controller
public class CustomErrorController implements ErrorController {
	private static final Logger LOG = LoggerFactory.getLogger(ErrorController.class);
	private static final String ERROR_PATH = "/error";
	private static final String ERROR_TEMPLATE = "/error/customError";
	//private static final String JSON_ERROR_TEMPLATE = "/error/json/404";
	private final ErrorAttributes errorAttributes;

	public CustomErrorController(ErrorAttributes errorAttributes) {
		this.errorAttributes = errorAttributes;
	}

	//@Override
	public String getErrorPath() {
		return ERROR_PATH;
	}

	@RequestMapping(value = ERROR_PATH )
	@ResponseBody
	public ResponseEntity<Map<String, Object>> error(HttpServletRequest request)
	{
		Map<String,Object> displayErrorMap = new HashMap<String,Object>();
		int status = HttpStatus.BAD_REQUEST.value(); // default is bad request - -status 400 
		String customProcessingError = getRequestAttributeString(request, "CustomProcessingError");
		Map<String, Object> error = getErrorAttributes(request, true);
		
		if ("True".equals(customProcessingError)) {
			displayErrorMap.put("status", getRequestAttributeString(request, "status"));
			displayErrorMap.put("error", getRequestAttributeString(request, "error"));
			displayErrorMap.put("message", getRequestAttributeString(request, "message"));
			displayErrorMap.put("path", getRequestAttributeString(request, "path"));
		}
		else {
			status = (Integer)error.get("status");
			displayErrorMap.put("status", error.get("status"));
			displayErrorMap.put("error", error.get("error"));
			displayErrorMap.put("message", error.get("message"));
			displayErrorMap.put("path", error.get("path"));
		}
		displayErrorMap.put("timestamp", error.get("timestamp"));
	
		// log error
		String erroMsg = !ObjectUtils.isEmpty(displayErrorMap.get("message"))?displayErrorMap.get("message").toString():"No Message";
		String errorMsg = displayErrorMap.get("error").toString() + ". " + erroMsg;
		ErrorLogEventHelper.logErrorEvent(CustomErrorController.class.getName(), errorMsg, "error-json", null, ErrorLogEvent.ERROR_SEVERITY);
		return new ResponseEntity<Map<String, Object>>(displayErrorMap, HttpStatus.valueOf(status));
	}
	
	
	
	
	/*@RequestMapping(ERROR_PATH)
	public String error(Model model, HttpServletRequest request) {
		Map<String, Object> error = getErrorAttributes(request, true);
		String customProcessingError = getRequestAttributeString(request, "CustomProcessingError");
		if ("True".equals(customProcessingError)) {
			model.addAttribute("status", getRequestAttributeString(request, "status"));
			model.addAttribute("error", getRequestAttributeString(request, "error"));
			model.addAttribute("message", getRequestAttributeString(request, "message"));
			model.addAttribute("path", getRequestAttributeString(request, "path"));
		} else {
			model.addAttribute("status", error.get("status"));
			model.addAttribute("error", error.get("error"));
			model.addAttribute("message", error.get("message"));
			model.addAttribute("path", error.get("path"));
		}

		model.addAttribute("timestamp", error.get("timestamp"));

		// log error 
		String errorMsg = model.asMap().get("error").toString() + ". " + model.asMap().get("message").toString();
		ErrorLogEventHelper.logErrorEvent(CustomErrorController.class.getName(), errorMsg, "error-text/html", null, ErrorLogEvent.ERROR_SEVERITY);
			
		return ERROR_TEMPLATE;
	}*/

	private String getRequestAttributeString(HttpServletRequest request, String param) {
		return CommonUtils.getRequestAttributeString(request, param);
	}

	public static String getErrorTemplate() {
		return ERROR_TEMPLATE;
	}

	public ErrorAttributes getErrorAttributes() {
		return errorAttributes;
	}

	private Map<String, Object> getErrorAttributes(HttpServletRequest request, boolean includeStackTrace) {
		WebRequest requestAttributes =  new DispatcherServletWebRequest(request);
		return this.errorAttributes.getErrorAttributes(requestAttributes, ErrorAttributeOptions.of(Include.STACK_TRACE,Include.MESSAGE,Include.EXCEPTION,Include.BINDING_ERRORS));

	}

}
