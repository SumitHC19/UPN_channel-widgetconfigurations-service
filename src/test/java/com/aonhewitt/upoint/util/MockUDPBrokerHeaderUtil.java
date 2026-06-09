package com.aonhewitt.upoint.util;

import java.util.HashMap;
import java.util.Map;

public class MockUDPBrokerHeaderUtil {

             // @Mock
              public Map<String,Object> createBrokerHeaderMapFromAHUser(){
                             
                             Map<String,Object> udpBrokerHeaderConfigMap = new HashMap<String,Object>();
                             udpBrokerHeaderConfigMap.put("brokerUserId","xxxxx");
                             udpBrokerHeaderConfigMap.put("nationalIdCountry", "");
                             udpBrokerHeaderConfigMap.put("platformType", "");
                             udpBrokerHeaderConfigMap.put("udpTestCfg","xx@z");
                             udpBrokerHeaderConfigMap.put("subjectId","11111");
                             udpBrokerHeaderConfigMap.put("subjectType","XYZ");
                             udpBrokerHeaderConfigMap.put("systemInstanceId","");
                             udpBrokerHeaderConfigMap.put("clientId","aaaaa");
                             return udpBrokerHeaderConfigMap;
              }
}
