package com.aonhewitt.upoint.util;

public class MockUDPNextBaseServices {
	
	
	//@Mock
	public com.alight.upoint.udp.beans.UDPInvocationResult invoke(com.alight.upoint.udp.beans.UDPServiceObject object) {
		com.alight.upoint.udp.beans.UDPInvocationResult invoResult=new com.alight.upoint.udp.beans.UDPInvocationResult();
		invoResult.setStatusCode(2);
		invoResult.setHttpStatusMessage("Udp Response returned successfully");
		return invoResult;
	}
}


	
