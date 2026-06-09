package com.aonhewitt.upoint.core.controller.error;

import java.util.HashMap;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aonhewitt.upoint.util.CommonUtils;

/**
 * 
 * @author Yogesh M
 * 
 * - Server the json clients 
 * - formats error responses for /personMessages invalid/bad requests, In case Asg-SessionToken is null
 *
 */

@RestController
@RequestMapping("/error/json")
public class RestErrorController {

	/**
	 * Should be called to server Json response in case Asg-SessionToken is null
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/personMessages", produces = "application/json")
	public Map<String, String> returnPersonMessagesAPIError(HttpServletRequest request) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("status", CommonUtils.getRequestAttributeString(request, "status"));
		map.put("error", CommonUtils.getRequestAttributeString(request, "error"));
		map.put("message", CommonUtils.getRequestAttributeString(request, "message"));
		map.put("path", CommonUtils.getRequestAttributeString(request, "path"));
		return map;
	}
	
}
