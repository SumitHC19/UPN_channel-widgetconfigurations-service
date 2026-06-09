package com.aonhewitt.upoint.util;

import static org.mockito.Mockito.mock;

public class MockApplicationContextProvider {
	
    public  static <T> T getBean(String name,Class<T> aClass){
		if(name.equals("udpBaseServices")){
			return (T) new com.alight.upoint.udp.base.service.UDPBaseServices(null);
		}else{
			return (T) mock(aClass);
		}
        
    }
	
	
	

}

