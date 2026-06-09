package com.aonhewitt.upoint.util;

import com.aonhewitt.portal.core.udp.pojo.person.v2.PersonService;
import com.aonhewitt.portal.hasbro.udp.base.service.pojo.UDPResponse;

public class MockUDPBaseServices {
	//@Mock
	
	public UDPResponse getUdpPersonSchemaObject() {
		UDPResponse res = new PersonService();
		res.setResponseCode("200");
		return res;
	}
	
	//@Mock
	public com.alight.upoint.udp.beans.UDPInvocationResult invoke(com.alight.upoint.udp.beans.UDPServiceObject object) {
		com.alight.upoint.udp.beans.UDPInvocationResult invoResult=new com.alight.upoint.udp.beans.UDPInvocationResult();
		invoResult.setStatusCode(2);
		invoResult.setHttpStatusMessage("Udp Response returned successfully");
		return invoResult;
	}
}


	
