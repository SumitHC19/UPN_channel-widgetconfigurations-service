package com.aonhewitt.upoint.conf;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;

public class YAMLConfigTest {

	
	private YAMLConfig config = new YAMLConfig();
	
	@Test
	public void testClass() {
		
		Map<Object,Object> gaMap = new HashMap<Object, Object>();
		
		String ivaScript = "iva";
		
		gaMap.put("k2","v2");
		
		config.setGaConfigurations(gaMap);
		config.setIvaJavaScriptSrc(ivaScript);
		
		
		assertTrue(config.getGaConfigurations()!=null);
		assertTrue(config.getIvaJavaScriptSrc()!=null);
		
		assertTrue(config.getYamlConfig()!=null);
		
	}
	
}
