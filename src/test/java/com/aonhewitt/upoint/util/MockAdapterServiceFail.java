package com.aonhewitt.upoint.util;

import com.aonhewitt.beans.ComponentRequestBean;
import com.aonhewitt.portal.util.RestServiceInvocationResult;

public class MockAdapterServiceFail {
	
	RestServiceInvocationResult getResponse(ComponentRequestBean aRequestBean)
			throws Exception {
		
		throw new Exception();
	}
}
