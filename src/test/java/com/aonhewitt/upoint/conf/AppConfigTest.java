package com.aonhewitt.upoint.conf;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import java.io.File;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.env.ConfigurableEnvironment;

import com.aonhewitt.portal.adapter.BaseAdapter;
import com.aonhewitt.portal.adapter.util.DistributedRcsCacheUtil;
import com.aonhewitt.portal.assetmanagement.handler.AssetManagementHandler;
import com.aonhewitt.portal.configuration.service.util.MultiGetUtil;
import com.aonhewitt.portal.core.service.LinkResolverLocalServiceUtil;
import com.aonhewitt.portal.core.service.util.CmServicesUtil;
import com.aonhewitt.portal.hasbro.base.service.DistributedHasbroCacheUtil;
import com.aonhewitt.portal.hasbro.ist.service.AlightDistributedHasbroCacheUtil;
import com.aonhewitt.portal.hasbro.udp.base.service.UDPBaseServices;
import com.aonhewitt.portal.service.delegators.ConfigManagerService;
import com.aonhewitt.portal.service.delegators.LinkService;
import com.aonhewitt.portal.service.delegators.UDPManagerService;
import com.aonhewitt.portal.service.linkres.LinkResolver;
import com.aonhewitt.portal.service.linkres.LinksConfigRepository;
import com.aonhewitt.upoint.questionnaire.expression.handler.LifeEventsExpressionHelper; //Modified for PC
import com.aonhewitt.upoint.util.MockDockerSecretUtil;
import com.aonhewitt.upoint.util.MockDockerSecretUtilFail;
import com.aonhewitt.util.DistributedSystemParmCacheUtil;

//@ExtendWith(MockitoExtension.class)
public class AppConfigTest {
	
	@InjectMocks
	@Spy
	private AppConfig appConfig = new AppConfig();

	@org.mockito.Mock
	private ConfigurableEnvironment envt;

	@org.mockito.Mock
	private AppProperties appProperties;

	@org.mockito.Mock
	private File configLoc;

	@BeforeEach
	public void setup() {
		Mockito.clearAllCaches();
		MockitoAnnotations.openMocks(this);
		appProperties.setPrprConfigRoot("/config");
		appProperties.setTbiaConfigDir("/config");
		appProperties.setLifecyclePhase("test");
		appProperties.setAxis2ModuleRepo("testrepo");
		appProperties.setKeyStoreAlias("alias1");
		appProperties.setKeyStorePassword("pwd");
		appProperties.setKeyStorePath("keypath");
		appProperties.setKeyStoreUser("keyuser");
		appProperties.setPrprConfigRoot("prprConfigRoot");

		/*new MockUp<ServiceTransportSystemProperties>() {
			@Mock
			void setHasbroCacheableSettings(Map<String, String> hasbroCacheableSettings) {
				// do nothing
			}

		};*/

		Mockito.when(appProperties.getKeyStorePassword()).thenReturn("pwd");
		Mockito.when(appProperties.getKeyStorePath()).thenReturn("keyPath");
		Mockito.when(appProperties.getKeyStoreUser()).thenReturn("keyUser");
		Mockito.when(appProperties.getKeyStoreAlias()).thenReturn("alias");

		/*new MockUp<DockerSecretsUtil>() {

			@Mock
			public Map<String, String> load() {
				Map<String, String> map = new HashMap<String, String>();

				map.put("app.rampart.keystore.password", "password");
				return map;

			}
		};*/
		
		new MockDockerSecretUtil();		

	}

	@Test
	public void testInit() throws Exception {
		appConfig.setProfile("localdev");
		AssetManagementHandler assetMgmt = appConfig.assetManagementHandler();
		MultiGetUtil multi = appConfig.multiGetUtil();
		CmServicesUtil util = appConfig.cmServicesUtil();
		ConfigManagerService confService = appConfig.configManagerService();
		LinksConfigRepository configRepository = appConfig.configRepository();
		DistributedHasbroCacheUtil distributedHasbroCacheUtil = appConfig.distributedHasbroCacheUtil();
		DistributedSystemParmCacheUtil distributedSystemParmCacheUtil = appConfig.distributedSystemParmCacheUtil();

		 BaseAdapter genericWidgetConfigurationAdapter = appConfig.genericWidgetConfigurationAdapter();
		//LinkResolver linkresolver = appConfig.linkresolver();
		LinkResolverLocalServiceUtil linkResolverLocalServiceUtil = appConfig.linkResolverLocalServiceUtil();
		LinkService linkService = appConfig.linkService();
		UDPBaseServices uDPBaseServices = appConfig.uDPBaseServices();
		UDPManagerService uDPManagerService = appConfig.uDPManagerService();
		LifeEventsExpressionHelper lifeEventsExpressionHelper = appConfig.lifeEventsExpressionHelper();
		DistributedRcsCacheUtil distributedRcsCacheUtil = appConfig.distributedRcsCacheUtil();	
		AlightDistributedHasbroCacheUtil alightdist = appConfig.alightDistributedHasbroCacheUtil();
		LinkResolver link = appConfig.linkresolver();
		appConfig.init();

		assertNotNull(link);
		assertTrue("alias".equals(appProperties.getKeyStoreAlias()));
		assertTrue("pwd".equals(appProperties.getKeyStorePassword()));
		assertNotNull(assetMgmt);
		assertNotNull(multi);
		assertNotNull(util);
		assertNotNull(confService);
		assertNotNull(configRepository);
		assertNotNull(distributedHasbroCacheUtil);
		assertNotNull(distributedSystemParmCacheUtil);
		assertNotNull(genericWidgetConfigurationAdapter);
		//assertNotNull(linkresolver);
		assertNotNull(genericWidgetConfigurationAdapter);
		assertNotNull(linkResolverLocalServiceUtil);
		assertNotNull(linkService);
		assertNotNull(uDPBaseServices);
		assertNotNull(uDPManagerService);
	    assertNotNull(lifeEventsExpressionHelper); 
		assertNotNull(distributedRcsCacheUtil);
		
	}

	@Test
	public void testInitException() throws Exception {
		assertThrows(Exception.class, () -> {
			Mockito.when(envt.getSystemProperties()).thenReturn(null);
			appConfig.init();

		});

	}
	@Test
	public void testInitExceptionSecrets() throws Exception {
		assertThrows(Exception.class, () -> {
			appConfig.setProfile("qa");
			Map<String, String> map = new HashMap<String, String>();

			map.put("upoint.base.password", "password");
			map.put("upoint.base.null.null.password", "password");

			map.put("upoint.client0.password", "password");
			map.put("upoint.client.null.null.password", "password");

			map.put("upoint.ucceclient0.password", "password");
			map.put("upoint.ucceclient.null.null.password", "password");

			map.put("upoint.uccebase.null.null.password", "password");
			map.put("upoint.uccebase.password", "password");
			map.put("app.rampart.keystore.password1", "password");
			//new MockDockerSecretUtilFail();
			MockedStatic<MockDockerSecretUtilFail> mockDockerSecretUtilFail = Mockito.mockStatic(MockDockerSecretUtilFail.class);
			mockDockerSecretUtilFail.when(() -> MockDockerSecretUtilFail.load()).thenReturn(map);
			/*new MockUp<DockerSecretsUtil>() {
						@Mock
				public Map<String, String> load() {
					Map<String, String> map = new HashMap<String, String>();
							map.put("app.rampart.keystore.password1", "password");
					return map;
						}
			};*/
		
			
			appConfig.init();

		});

	}
	
	@Test
	public void testMain() throws Exception {
		Method method = AppConfig.class.getDeclaredMethod("getConfigKeyStorePath");
		method.setAccessible(true);
		AppConfig appConf = new AppConfig();
		 
		method.invoke(appConf);
		
	}
}