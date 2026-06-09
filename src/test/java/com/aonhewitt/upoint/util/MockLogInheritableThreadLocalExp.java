package com.aonhewitt.upoint.util;

import java.util.HashMap;
import java.util.Map;

import com.alight.logging.constant.UpointLogConstants;
import com.alight.logging.events.CommonTypeLogEvent;
import com.alight.logging.helpers.LogInheritableThreadLocal;
import com.aonhewitt.upoint.core.service.util.ExpressionDebugAssistInfo;
import com.aonhewitt.upoint.core.service.util.ExpressionDebugAssistUtil;

public class MockLogInheritableThreadLocalExp {
	
	public static LogInheritableThreadLocal get(){
		
		ExpressionDebugAssistInfo expLinkCallCountStorage = new ExpressionDebugAssistInfo();
		Map<String, Integer> expCountMap = new HashMap();
		expCountMap.put("ALL_LINKS_CONVERTED_IN_ANGULAR||19968_1.0||true::false", 1);
		expCountMap.put("IS_CLOUDCMS_SUPPORTED||19968_1.0||true::false", 1);
		expCountMap.put("IS_CLOUDCMS_FALLBACK_ENABLED||19968_1.0||true::false", 1);
		expCountMap.put("IS_CBA_CLIENT||19968_1.0||false::false", 1);
		expCountMap.put("AF_ENABLED||19968_1.0||false::false", 1);
		//expLinkCallCountStorage.setExpCountMap(expCountMap);
		
		CommonTypeLogEvent commonTypeLogEvent = new CommonTypeLogEvent();
		commonTypeLogEvent.setSpanID("testSpanId");
		LogInheritableThreadLocal instance = new LogInheritableThreadLocal();
		
		instance.put(ExpressionDebugAssistUtil.DEBUG_ASST_EXP_INFO, expLinkCallCountStorage);
		instance.put(UpointLogConstants.COMMON_TYPE_LOG_EVENT, commonTypeLogEvent);
		
		return instance;		
		
	}

}
