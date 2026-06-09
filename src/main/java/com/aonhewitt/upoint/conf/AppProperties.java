package com.aonhewitt.upoint.conf;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author - Yogesh M Defines System level Properties
 */
@ConfigurationProperties(prefix = "app")
public class AppProperties {

	private String prprConfigRoot;
	private String tbiaConfigDir;
	private String axis2ModuleRepo;
	private String lifecyclePhase;
	private String keyStoreAlias=StringUtils.EMPTY;
	private String keyStoreUser=StringUtils.EMPTY;
	private String keyStorePath=StringUtils.EMPTY;
	private String keyStorePassword=StringUtils.EMPTY;
	private Map<String, String> hasbroCacheableSettings = new HashMap<String, String>();
	private String partparmCacheTTL="15";
	private String lifeEvntCacheTTL="12";
	private String hasbroCacheTTL="15";
	private Long dcRcsPreAuthTTL= 15l;
	private Boolean preAuthCachingEnabled;
	private String preferenceEnabled;
	private String navPerformanceLogic;
	private String tilesNewLogic;
	private String headerFooterOptimized;
	
	private String udpTokenFromAPS;
	
	private String preAuthBrandingCacheTTL="15";
	
	private String preAuthCMSBrandingCacheTTL="15";
	
	private String hmUPNActBeanCacheTTL = "15";
	
	private String multiTenantBrandingEnabled = "false";
	
	private String preAuthMultiTenantCMSBrandingCacheTTL = "15";
	
	public String getUdpTokenFromAPS() {
		return udpTokenFromAPS;
	}

	@Value("${partParm.connectionTimeout:10}")
	private String partParmConnectionTimeout;
	
	@Value("${partParm.readTimeout:20}")
	private String partParmReadTimeout;
	
	public String getPartParmConnectionTimeout() {
		return partParmConnectionTimeout;
	}

	public void setPartParmConnectionTimeout(String partParmConnectionTimeout) {
		this.partParmConnectionTimeout = partParmConnectionTimeout;
	}

	public String getPartParmReadTimeout() {
		return partParmReadTimeout;
	}

	public void setPartParmReadTimeout(String partParmReadTimeout) {
		this.partParmReadTimeout = partParmReadTimeout;
	}
	@Value("${udpTokenFromAPS:true}")
	public void setUdpTokenFromAPS(String udpTokenFromAPS) {
		this.udpTokenFromAPS = udpTokenFromAPS;
	}
	/*private String enableCacheCompression="false";
	
	public String getEnableCacheCompression() {
		return enableCacheCompression;
	}

	public void setEnableCacheCompression(String enableCacheCompression) {
		this.enableCacheCompression = enableCacheCompression;
	}*/
	public String getTilesNewLogic() {
		return tilesNewLogic;
	}
	@Value("${tilesNewLogic:false}")
	public void setTilesNewLogic(String tilesNewLogic) {
		this.tilesNewLogic = tilesNewLogic;
	}
	// Circuit breaker cache block start
	private String tbaAdapterCircuitBreakerSwitch;
	private String tbaAdapterCircuitBreakerTrip;
	public String getTbaAdapterCircuitBreakerSwitch() {
		return tbaAdapterCircuitBreakerSwitch;
	}
	public void setTbaAdapterCircuitBreakerSwitch(String tbaAdapterCircuitBreakerSwitch) {
		this.tbaAdapterCircuitBreakerSwitch = tbaAdapterCircuitBreakerSwitch;
	}
	public String getTbaAdapterCircuitBreakerTrip() {
		return tbaAdapterCircuitBreakerTrip;
	}
	public void setTbaAdapterCircuitBreakerTrip(String tbaAdapterCircuitBreakerTrip) {
		this.tbaAdapterCircuitBreakerTrip = tbaAdapterCircuitBreakerTrip;
	}	
	// Circuit breaker cache block start
	
	public String getPreferenceEnabled() {
		return preferenceEnabled;
	}
	@Value("${preferenceEnabled:false}")
	public void setPreferenceEnabled(String preferenceEnabled) {
		this.preferenceEnabled = preferenceEnabled;
	}

	public String getPartparmCacheTTL() {
		return partparmCacheTTL;
	}

	public void setPartparmCacheTTL(String partparmCacheTTL) {
		this.partparmCacheTTL = partparmCacheTTL;
	}
	
	public String getHasbroCacheTTL() {
		return hasbroCacheTTL;
	}

	public void setHasbroCacheTTL(String hasbroCacheTTL) {
		this.hasbroCacheTTL = hasbroCacheTTL;
	}

	
	public Map<String, String> getHasbroCacheableSettings() {
		return hasbroCacheableSettings;
	}

	public void setHasbroCacheableSettings(Map<String, String> hasbroCacheableSettings) {
		this.hasbroCacheableSettings = hasbroCacheableSettings;
	}

	

	public String getPrprConfigRoot() {
		return prprConfigRoot;
	}

	public void setPrprConfigRoot(String prprConfigRoot) {
		this.prprConfigRoot = prprConfigRoot;
	}

	public String getTbiaConfigDir() {
		return tbiaConfigDir;
	}

	public void setTbiaConfigDir(String tbiaConfigDir) {
		this.tbiaConfigDir = tbiaConfigDir;
	}

	public String getAxis2ModuleRepo() {
		return axis2ModuleRepo;
	}

	public void setAxis2ModuleRepo(String axis2ModuleRepo) {
		this.axis2ModuleRepo = axis2ModuleRepo;
	}

	public String getLifecyclePhase() {
		return lifecyclePhase;
	}

	public void setLifecyclePhase(String lifecyclePhase) {
		this.lifecyclePhase = lifecyclePhase;
	}

	public String getKeyStoreAlias() {
		return keyStoreAlias;
	}

	public void setKeyStoreAlias(String keyStoreAlias) {
		this.keyStoreAlias = keyStoreAlias;
	}

	public String getKeyStorePath() {
		return keyStorePath;
	}

	public void setKeyStorePath(String keyStorePath) {
		this.keyStorePath = keyStorePath;
	}

	public String getKeyStorePassword() {
		return keyStorePassword;
	}

	public void setKeyStorePassword(String keyStorePassword) {
		this.keyStorePassword = keyStorePassword;
	}

	public String getKeyStoreUser() {
		return keyStoreUser;
	}

	public void setKeyStoreUser(String keyStoreUser) {
		this.keyStoreUser = keyStoreUser;
	}
	public String isNavPerformanceLogic() {
		return navPerformanceLogic;
	}
	@Value("${navPerformanceLogic:true}")
	public void setNavPerformenceLogic(String navPerformanceLogic) {
		this.navPerformanceLogic = navPerformanceLogic;
	}
	public String isHeaderFooterOptimized() {
		return headerFooterOptimized;
	}
	@Value("${headerFooterOptimized:true}")
	public void setHeaderFooterOptimized(String aHeaderFooterOptimized) {
		this.headerFooterOptimized = aHeaderFooterOptimized;
	}
	public String getLifeEvntCacheTTL() {
		return lifeEvntCacheTTL;
	}
	public void setLifeEvntCacheTTL(String lifeEvntCacheTTL) {
		this.lifeEvntCacheTTL = lifeEvntCacheTTL;
	}

	public Long getDcRcsPreAuthTTL() {
		return dcRcsPreAuthTTL;
	}

	public void setDcRcsPreAuthTTL(Long dcRcsPreAuthTTL) {
		this.dcRcsPreAuthTTL = dcRcsPreAuthTTL;
	}

	public Boolean isPreAuthCachingEnabled() {
		return preAuthCachingEnabled;
	}
	
	@Value("${preAuthCachingEnabled:false}")
	public void setPreAuthCachingEnabled(Boolean preAuthCachingEnabled) {
		this.preAuthCachingEnabled = preAuthCachingEnabled;
	}
	
	public String getPreAuthBrandingCacheTTL() {
		return preAuthBrandingCacheTTL;
	}
	
	@Value("${preAuthBrandingCacheTTL:15}")
	public void setPreAuthBrandingCacheTTL(String preAuthBrandingCacheTTL) {
		this.preAuthBrandingCacheTTL = preAuthBrandingCacheTTL;
	}
	
	public String getPreAuthCMSBrandingCacheTTL() {
		return preAuthCMSBrandingCacheTTL;
	}
	
	@Value("${preAuthCMSBrandingCacheTTL:15}")
	public void setPreAuthCMSBrandingCacheTTL(String preAuthCMSBrandingCacheTTL) {
		this.preAuthCMSBrandingCacheTTL = preAuthCMSBrandingCacheTTL;
	}
	
	public String getHmUPNActBeanCacheTTL()
	{
		return hmUPNActBeanCacheTTL;
	}

	@Value("${hmUPNActBeanCacheTTL:15}")
	public void setHmUPNActBeanCacheTTL(String hmUPNActBeanCacheTTL)
	{
		this.hmUPNActBeanCacheTTL = hmUPNActBeanCacheTTL;
	}
	
	public String getMultiTenantBrandingEnabled() {
		return multiTenantBrandingEnabled;
	}
	
	@Value("${multiTenantBrandingEnabled:false}")
	public void setMultiTenantBrandingEnabled(String multiTenantBrandingEnabled) {
		this.multiTenantBrandingEnabled = multiTenantBrandingEnabled;
	}

	public String getPreAuthMultiTenantCMSBrandingCacheTTL() {
		return preAuthMultiTenantCMSBrandingCacheTTL;
	}

	@Value("${preAuthMultiTenantCMSBrandingCacheTTL:15}")
	public void setPreAuthMultiTenantCMSBrandingCacheTTL(String preAuthMultiTenantCMSBrandingCacheTTL) {
		this.preAuthMultiTenantCMSBrandingCacheTTL = preAuthMultiTenantCMSBrandingCacheTTL;
	}
}
