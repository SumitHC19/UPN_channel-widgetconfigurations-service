package com.aonhewitt.upoint.util;

import com.aonhewitt.beans.ComponentRequestBean;
import com.aonhewitt.portal.util.RestServiceInvocationResult;

public class MockAdapterService {
	RestServiceInvocationResult serviceInvocationResult ;
	public MockAdapterService(RestServiceInvocationResult serviceInvocationResult) {
		this.serviceInvocationResult = serviceInvocationResult;
		// TODO Auto-generated constructor stub
	}

//	@Mock
	RestServiceInvocationResult getResponse(ComponentRequestBean aRequestBean)
			throws ClassNotFoundException, InstantiationException, IllegalAccessException {
		
		return serviceInvocationResult;
	}
}
