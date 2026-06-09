package com.aonhewitt.upoint.util;

import com.aonhewitt.beans.ComponentRequestBean;
import com.aonhewitt.portal.util.RestServiceInvocationResult;

public class TileMock {
	RestServiceInvocationResult serviceInvocationResult = null;

	public TileMock(RestServiceInvocationResult aServiceInvocationResult) {
		this.serviceInvocationResult = aServiceInvocationResult;
	}

	//@Mock
	public RestServiceInvocationResult process(ComponentRequestBean componentRequestBean) {
		return this.serviceInvocationResult;
	}
}
