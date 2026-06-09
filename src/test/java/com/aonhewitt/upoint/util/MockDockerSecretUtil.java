package com.aonhewitt.upoint.util;

import java.util.HashMap;
import java.util.Map;

public class MockDockerSecretUtil {
	public Map<String, String> load() {
		Map<String,String> map = new HashMap<String,String>();
		
        map.put("upoint.base.password", "password");
        map.put("upoint.base.null.null.password", "password");
		
		map.put("upoint.client0.password", "password");
		map.put("upoint.client.null.null.password", "password");
		
		map.put("upoint.ucceclient0.password", "password");
		map.put("upoint.ucceclient.null.null.password", "password");
		
		map.put("upoint.uccebase.null.null.password", "password");
		map.put("upoint.uccebase.password", "password");
		map.put("app.rampart.keystore.password", "password");
		map.put("upoint.geolocation.username", "username");
		map.put("upoint.geolocation.password", "password");
		
		return map;
	}
}


	
