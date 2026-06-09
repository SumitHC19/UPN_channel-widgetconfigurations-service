package com.aonhewitt.upoint.util;

import java.util.HashMap;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.web.context.request.RequestAttributes;

public class MockCommonUtilsFalse {
	
		String getRequestAttributeString(HttpServletRequest request, String param) {
			return "False";
		}
		
		 Map<String, Object> getErrorAttributes(RequestAttributes requestAttributes,
			boolean includeStackTrace) {
			Map<String, Object> map = new HashMap<String,Object>();
		    map.put("Error", "Custom");
		    map.put("status", "400");
			return map;
		}
		
}


	
