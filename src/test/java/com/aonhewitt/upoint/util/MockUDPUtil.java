package com.aonhewitt.upoint.util;

import java.util.HashMap;
import java.util.Map;

import com.aonhewitt.portal.hasbro.udp.base.service.pojo.UDPBrokerHeader;
import com.aonhewitt.portal.userprovisioning.beans.AHUser;

public class MockUDPUtil {
//	@Mock
	public Map<String,String> getUdpCommonDate(AHUser ahUser){
		
		Map<String,String> udpBrokerHeaderConfigMap = new HashMap<String,String>();
		udpBrokerHeaderConfigMap.put("subjectId","11111");
		udpBrokerHeaderConfigMap.put("subjectType","XYZ");
		udpBrokerHeaderConfigMap.put("systemInstanceId","");
		udpBrokerHeaderConfigMap.put("clientId","XXXXX");
		return udpBrokerHeaderConfigMap;
	}


//@Mock
public UDPBrokerHeader createBrokerHeader(){
	
	UDPBrokerHeader brokerHeader = new UDPBrokerHeader();
	brokerHeader.setBrokerUserId("Portal");
	brokerHeader.setClientId("19920");
	brokerHeader.setSubjectId("333333333");
	brokerHeader.setSubjectType("PRSN");
	brokerHeader.setSystemInstanceId("");
	brokerHeader.setUdpTestCfg("D$EB");
	
	return brokerHeader;
}
}


	
