package com.aonhewitt.upoint.conf;

import java.io.File;
import java.util.Map;

import javax.xml.parsers.FactoryConfigurationError;

import jakarta.annotation.PostConstruct;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.ConfigurableEnvironment;
import com.alight.cloud.data.store.util.DockerSecretsUtil;
import com.aonhewitt.exceptions.AHBaseException;
import com.aonhewitt.logging.events.ErrorLogEvent;
import com.aonhewitt.logging.helpers.ErrorLogEventHelper;
import com.aonhewitt.portal.adapter.BaseAdapter;
import com.aonhewitt.portal.adapter.GenericWidgetConfigurationAdapter;
import com.aonhewitt.portal.adapter.util.DistributedRcsCacheUtil;
import com.aonhewitt.portal.assetmanagement.handler.AssetManagementHandler;
import com.aonhewitt.portal.configuration.service.util.MultiGetUtil;
import com.aonhewitt.portal.core.service.LinkResolverLocalServiceUtil;
import com.aonhewitt.portal.core.service.util.CmServicesUtil;
import com.aonhewitt.portal.hasbro.base.service.DistributedHasbroCacheUtil;
import com.aonhewitt.portal.hasbro.base.service.utils.ServiceTransportSystemProperties;
import com.aonhewitt.portal.hasbro.ist.service.AlightDistributedHasbroCacheUtil;
import com.aonhewitt.portal.hasbro.udp.base.service.UDPBaseServices;
import com.aonhewitt.portal.service.delegators.ConfigManagerService;
import com.aonhewitt.portal.service.delegators.LinkService;
import com.aonhewitt.portal.service.delegators.UDPManagerService;
import com.aonhewitt.portal.service.linkres.LinkResolver;
import com.aonhewitt.portal.service.linkres.LinksConfigRepository;
import com.aonhewitt.portal.service.linkres.LinksConfigRepositoryImpl;
import com.aonhewitt.upoint.cache.config.provider.ICache;
import com.aonhewitt.upoint.questionnaire.expression.handler.LifeEventsExpressionHelper;
import com.aonhewitt.util.DistributedSystemParmCacheUtil;
import org.springframework.util.ObjectUtils;
/**
 * 
 * @author Yogesh M
 * 
 * Configure System level properties
 *
 */

@Configuration
@EnableConfigurationProperties(AppProperties.class)
public class AppConfig {

	@Autowired
	private ConfigurableEnvironment env;
	
	public void setEnv(ConfigurableEnvironment env) {
		this.env = env;
	}

	@Autowired
	private AppProperties appProperties;
	
	public void setAppProperties(AppProperties appProperties) {
		this.appProperties = appProperties;
	}
	
	@Autowired
	@Qualifier("redisCacheObjectProvider")
	private ICache redisCacheProvider;
	
	@Autowired
	@Qualifier("sessionRedisCacheObjectProvider")
	private ICache sessionRedisCacheProvider;
	
	private String profile;
	
	/**
	 * @return the profile
	 */
	public String getProfile() {
		return profile;
	}

	
	/**
	 * @param profile the profile to set
	 */
	@Value("${spring.profiles.active}")
	public void setProfile(String profile) {
		this.profile = profile;
	}
	
	@Bean
	public LinkService linkService(){
		return new LinkService();
	}
	
	
	
	@Bean	
	public ConfigManagerService configManagerService(){
		ConfigManagerService configManagerService = new ConfigManagerService();
		return configManagerService;
	}
	@Bean	
	public CmServicesUtil cmServicesUtil(){
		return new CmServicesUtil();
	}

	
	@Bean	
	public LifeEventsExpressionHelper lifeEventsExpressionHelper(){
		return new LifeEventsExpressionHelper();
	}
	
	
	@Bean
	public AssetManagementHandler assetManagementHandler() {
		return new AssetManagementHandler();
	}

	@Bean
	public LinkResolverLocalServiceUtil linkResolverLocalServiceUtil() {
		return new LinkResolverLocalServiceUtil();
	}

	@Bean
	public LinksConfigRepository configRepository() {
		return new LinksConfigRepositoryImpl();
	}

	@Bean
	public LinkResolver linkresolver() {
		return new LinkResolver();
	}
	
	@Bean	
	public UDPManagerService uDPManagerService(){
		return new UDPManagerService();
	}
	
	@Bean
	public UDPBaseServices uDPBaseServices(){
		return new UDPBaseServices();
	}
    // change for find bug fix
	@Bean(name="distributedHasbroCacheUtil")
	public DistributedHasbroCacheUtil distributedHasbroCacheUtil(){
		return new DistributedHasbroCacheUtil(sessionRedisCacheProvider);
	}
	
	@Bean(name="distributedSystemParmCacheUtil")
	public DistributedSystemParmCacheUtil distributedSystemParmCacheUtil(){
		return new DistributedSystemParmCacheUtil(sessionRedisCacheProvider);
	}

	@Bean
	public BaseAdapter genericWidgetConfigurationAdapter(){
		return new 	GenericWidgetConfigurationAdapter();
	}
	
	@Bean(name="distributedRcsCacheUtil")
	public DistributedRcsCacheUtil distributedRcsCacheUtil(){
		//binding configProviderImpl
		return new DistributedRcsCacheUtil(redisCacheProvider);
	}
	
	@Bean	
	public MultiGetUtil multiGetUtil(){
		return new MultiGetUtil();
	}
	
	//YTRGW-4058 Phil Hoden
	@Bean(name="alightDistributedHasbroCacheUtil")
	public AlightDistributedHasbroCacheUtil alightDistributedHasbroCacheUtil(){
		return new AlightDistributedHasbroCacheUtil(sessionRedisCacheProvider);
	}
	
	@PostConstruct
	public void init() throws Exception {
		try {
			Map<String, Object> systemProperties = env.getSystemProperties();
			systemProperties.put("prprConfigRoot", appProperties.getPrprConfigRoot());
			systemProperties.put("TBIAConfigDir", appProperties.getTbiaConfigDir());
			systemProperties.put("lifecycle_phase", appProperties.getLifecyclePhase());
			systemProperties.put("axis2.module.repo", appProperties.getAxis2ModuleRepo());
			systemProperties.put("preferenceEnabled", appProperties.getPreferenceEnabled());
			systemProperties.put("navPerformanceLogic", appProperties.isNavPerformanceLogic());
			systemProperties.put("tilesNewLogic", appProperties.getTilesNewLogic());
			systemProperties.put("headerFooterOptimized", appProperties.isHeaderFooterOptimized());
			systemProperties.put("partParm.connectionTimeout", appProperties.getPartParmConnectionTimeout());
			systemProperties.put("partParm.ReadTimeout", appProperties.getPartParmReadTimeout());

			/**
			 * Rampart policy properties
			 */
			Map<String, String> secrets = DockerSecretsUtil.load();
			String keyStorePassword = appProperties.getKeyStorePassword().trim();
			
			if(secrets.containsKey("app.rampart.keystore.password")){
				keyStorePassword = secrets.get("app.rampart.keystore.password");
			}
			else
			{
				if(!"localdev".equalsIgnoreCase(getProfile()))
				{
					throw new AHBaseException(
							"app.rampart.keystore.password is not configured in secret");
				}
			}
			
			String keyStorePath = getConfigKeyStorePath();
			
			if(ObjectUtils.isEmpty(keyStorePath))
			{
				if(!"localdev".equalsIgnoreCase(getProfile()))
				{
					throw new AHBaseException("keyStorePath is not configured in config");
				}
				else
				{
					keyStorePath=appProperties.getKeyStorePath().trim();
				}
				
			}
			systemProperties.put("keyStoreUser", appProperties.getKeyStoreUser().trim());
			systemProperties.put("keyStoreAlias", appProperties.getKeyStoreAlias().trim());
			systemProperties.put("keyStorePassword", keyStorePassword);
			systemProperties.put("keyStorePath", keyStorePath);

			/**
             * Hasbro Cacheable Settings 
             */

            ServiceTransportSystemProperties.setHasbroCacheableSettings(appProperties.getHasbroCacheableSettings());
            systemProperties.put("hasbroCacheTimeToLiveInterval",appProperties.getHasbroCacheTTL());


			
			/**
			 * This property only required to test the DB connectivity, SHOULD be always set to false.
			 */
			//systemProperties.put("enableDistributedCache", "true");
			//systemProperties.put("enableMultiget","true");
			systemProperties.put("dcTimeToLiveInterval",appProperties.getPartparmCacheTTL());
			systemProperties.put("dcLifeEventTimeToLiveInterval",appProperties.getLifeEvntCacheTTL());
			systemProperties.put("dcLimitToShowCacheKeys","100");
			// TODO below only for testing purpose only for DV profile and will be removed 
			/*systemProperties.put("enableDistributedCacheLinkOptimization", env.getProperty("enableDistributedCacheLinkOptimization"));
			 systemProperties.put("enableDistributedCacheParmOptimization", env.getProperty("enableDistributedCacheParmOptimization"));*/
			
			//systemProperties.put("enableDistributedCacheLinkOptimization", "true");
			//systemProperties.put("enableDistributedCacheParmOptimization", "true");
			
			// Circuit breaker cache block start
			String tbaAdapterCircuitBreakerSwitch = StringUtils
					.isEmpty(appProperties.getTbaAdapterCircuitBreakerSwitch()) ? "True"
							: appProperties.getTbaAdapterCircuitBreakerSwitch();
			String tbaAdapterCircuitBreakerTrip = StringUtils.isEmpty(appProperties.getTbaAdapterCircuitBreakerTrip())
					? "1"
					: appProperties.getTbaAdapterCircuitBreakerTrip();
			systemProperties.put("tbaAdapterCircuitBreakerSwitch", tbaAdapterCircuitBreakerSwitch);
			systemProperties.put("tbaAdapterCircuitBreakerTrip", tbaAdapterCircuitBreakerTrip);
			// Circuit breaker cache block end

			/**
			 * ---- INITIALIZE LOGGING ---
			 * This properties order is important as we needs to 
			 * a) first set after logs path location -> used as placeholder inside Loj4j.xml
			 * b) and then load Configure Loj4j.xml
			 */
			//systemProperties.put("app.lo4jPath", appProperties.getLogsFilePath());
			//DOMConfigurator.configure(appProperties.getLog4jConfig() + "log4j.xml");
			
			//Authenticate Application From APS token
			systemProperties.put("udpTokenFromAPS", appProperties.getUdpTokenFromAPS());
			
			systemProperties.put("preAuthBrandingCacheTimeToLiveInterval", appProperties.getPreAuthBrandingCacheTTL());
			systemProperties.put("preAuthCMSBrandingCacheTimeToLiveInterval", appProperties.getPreAuthCMSBrandingCacheTTL());
			systemProperties.put("hmUPNActBeanCacheTTL", appProperties.getHmUPNActBeanCacheTTL());
			
			systemProperties.put("multiTenantBrandingEnabled", appProperties.getMultiTenantBrandingEnabled());
			systemProperties.put("preAuthMultiTenantCMSBrandingCacheTTL", appProperties.getPreAuthMultiTenantCMSBrandingCacheTTL());

		} catch (FactoryConfigurationError e) {
			 ErrorLogEventHelper.logErrorEvent(AppConfig.class.getName(),
			 "Exception occured in init", "init method failed", e,
			 ErrorLogEvent.ERROR_SEVERITY);
		}

	}

	/**
	 * Below code is commented by Rajesh Rathod.
	 * CORS configuration are managed by Zuul server so its not needed to configure again at microservice level.
	 * If we continue doing so then consumer gets multiple cross exception.
	 */
	/*@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurerAdapter() {

			*//**
			 * TODO: control the access control using properties file
			 * e.g. allowed-origins=?, allowed-methods="GET, PUT", allowed-headers="?", exposed-headers="?" allow-credentials="false", max-age="?"
			 * 
			 * Default spring boot will restrict rest call from cross domain and
			 * hence enable it for all origins.
			 *//*
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("/**").allowedOrigins("*");
			}
		};
	}
	
	@Bean
    public FilterRegistrationBean corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOrigin("*");
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        source.registerCorsConfiguration("/**", config);
        FilterRegistrationBean bean = new FilterRegistrationBean(new CorsFilter(source));
        bean.setOrder(0);
        return bean;
    }*/

	// @author - Rajesh Rathod
	// HttpSession strategy needs to defined for redis.
	/*@Bean
    public HttpSessionStrategy httpSessionStrategy() {
        return new HeaderHttpSessionStrategy();
    }*/
	
	/**
	 * 
	 * @return
	 */
	private String getConfigKeyStorePath(){
		String path=null;
		String loc = getURL();
		File configLoc = new File(loc);
		if(configLoc.exists()){
			path=configLoc.toPath().toString();
		}
		return path;
	}
	
	private String getURL()
	{
		return "/run/config/PortalKeyStore";
		
	}
}