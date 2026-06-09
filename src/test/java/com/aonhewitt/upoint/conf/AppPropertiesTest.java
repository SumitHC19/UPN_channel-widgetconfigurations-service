package com.aonhewitt.upoint.conf;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;

public class AppPropertiesTest {
	//fix for find bug
	//AppProperties appProp;

	@Test
	public void testAllProperties() {
		AppProperties appProp=new AppProperties();
		appProp.setPrprConfigRoot("/config");
		appProp.setTbiaConfigDir("/config");
		appProp.setLifecyclePhase("test");
		appProp.setAxis2ModuleRepo("testrepo");
		appProp.setKeyStoreAlias("alias1");
		appProp.setKeyStorePassword("pwd");
		appProp.setKeyStorePath("keypath");
		appProp.setKeyStoreUser("keyuser");
		appProp.setPartparmCacheTTL("60");
		appProp.setHasbroCacheTTL("15");
		appProp.setPreAuthCMSBrandingCacheTTL("15");
		appProp.setHmUPNActBeanCacheTTL("15");
		
		appProp.setNavPerformenceLogic("nav");
		//String isNav = appProp.isNavPerformanceLogic();
		appProp.setHeaderFooterOptimized("aHeaderFooterOptimized");
		
		appProp.setTilesNewLogic("tilesNewLogic");
		//appProp.getTilesNewLogic();
		appProp.setPreferenceEnabled("preferenceEnabled");
		
		appProp.setTbaAdapterCircuitBreakerSwitch("tbaAdapterCircuitBreakerSwitch");
		//appProp.getTbaAdapterCircuitBreakerSwitch();
		appProp.setTbaAdapterCircuitBreakerTrip("tbaAdapterCircuitBreakerTrip");
		//appProp.getTbaAdapterCircuitBreakerTrip();
		//appProp.setEnableMultiget("true");
		Map<String, String> m=new HashMap<>(); 
		appProp.setHasbroCacheableSettings(m);
		
		appProp.setLifeEvntCacheTTL("10");
		
		// starts
		appProp.setUdpTokenFromAPS("udpTokenFromUPS");
		appProp.setDcRcsPreAuthTTL(15L);
		appProp.setPreAuthCachingEnabled(true);
		appProp.setPreAuthBrandingCacheTTL("preAuth");
		// end
		
		assertTrue("testrepo".equals(appProp.getAxis2ModuleRepo()));
		
		assertTrue("preferenceEnabled".equals(appProp.getPreferenceEnabled()));
		assertTrue("tilesNewLogic".equals(appProp.getTilesNewLogic()));
		assertTrue("aHeaderFooterOptimized".equals(appProp.isHeaderFooterOptimized()));
		
		assertTrue("tbaAdapterCircuitBreakerSwitch".equals(appProp.getTbaAdapterCircuitBreakerSwitch()));
		assertTrue("tbaAdapterCircuitBreakerTrip".equals(appProp.getTbaAdapterCircuitBreakerTrip()));
		
		assertTrue("nav".equals(appProp.isNavPerformanceLogic()));
		assertTrue(m.equals(appProp.getHasbroCacheableSettings()));
		//assertTrue("true".equals(appProp.getEnableMultiget()));
		assertTrue("alias1".equals(appProp.getKeyStoreAlias()));
		assertTrue("pwd".equals(appProp.getKeyStorePassword()));
		assertTrue("keypath".equals(appProp.getKeyStorePath()));
		assertTrue("test".equals(appProp.getLifecyclePhase()));
		assertTrue("keyuser".equals(appProp.getKeyStoreUser()));
		assertTrue("60".equals(appProp.getPartparmCacheTTL()));
		assertTrue("/config".equals(appProp.getPrprConfigRoot()));
		assertTrue("/config".equals(appProp.getTbiaConfigDir()));
		assertTrue("15".equals(appProp.getHasbroCacheTTL()));
		assertTrue("10".equals(appProp.getLifeEvntCacheTTL()));
		assertTrue("15".equals(appProp.getPreAuthCMSBrandingCacheTTL()));
		assertTrue("15".equals(appProp.getHmUPNActBeanCacheTTL()));
		//starts
		
		Long l2 = appProp.getDcRcsPreAuthTTL();
		Long l1 = 15L;
		Boolean b1 = true;
		Boolean b2 = appProp.isPreAuthCachingEnabled();
		assertTrue("udpTokenFromUPS".equals(appProp.getUdpTokenFromAPS()));
		assertThat(l1==l2);
		assertThat(b1==b2);
		assertTrue("preAuth".equals(appProp.getPreAuthBrandingCacheTTL()));
		
		appProp.setPartParmReadTimeout("test");
		appProp.setPartParmConnectionTimeout("1000");
		assertEquals(appProp.getPartParmReadTimeout(),"test");
		assertEquals(appProp.getPartParmConnectionTimeout(),"1000");
		
		//ends
		
	}
}
