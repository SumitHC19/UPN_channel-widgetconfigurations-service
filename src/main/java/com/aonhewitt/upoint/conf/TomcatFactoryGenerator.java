package com.aonhewitt.upoint.conf;

import org.apache.catalina.Context;
import org.apache.catalina.startup.Tomcat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.embedded.tomcat.TomcatWebServer;
import com.aonhewitt.upoint.db.DBUtil;
/**
 * @author - Yogesh Mittal
 */
import org.springframework.util.ObjectUtils;
public class TomcatFactoryGenerator extends TomcatServletWebServerFactory {

	@Autowired
	private DBUtil dbUtil;

	@Autowired
	private com.aonhewitt.upoint.conf.AjpConnectorProperties ajpConnectorProperties;

	private static final Logger LOG = LoggerFactory.getLogger(TomcatFactoryGenerator.class);

	public TomcatFactoryGenerator() {
		super();
	}

	@Override
    protected TomcatWebServer getTomcatWebServer(Tomcat tomcat) {
	tomcat.enableNaming();
	return super.getTomcatWebServer(tomcat);
	}

	@Override
	protected void postProcessContext(Context context) {
		//configureAJP(ajpConnectorProperties);
		if(!ObjectUtils.isEmpty(dbUtil.getGeoLocationContextResource()))
		{
			context.getNamingResources().addResource(dbUtil.getGeoLocationContextResource());
		}
		
	}

	/*protected void configureAJP(AjpConnectorProperties ajpConnectorProperties) {
		if (ajpConnectorProperties.getAjp().isEnabled()) {
			super.addAdditionalTomcatConnectors(createAjpConnector(ajpConnectorProperties));
			super.addContextValves(createRemoteIpValves());

		}
	}
*/
	/*private Connector createAjpConnector(AjpConnectorProperties ajpConnectorProperties) {
		// TODO - Make below constants configurables
		// TODO - Add Apache SSL configurations
		// TODO - Add connection setting
		if (LOG.isInfoEnabled()) {
			LOG.info("creating createAjpConnector");
		}
		Connector ajpConnector = new Connector("AJP/1.3");
		ajpConnector.setProtocol("AJP/1.3");
		ajpConnector.setScheme("apj");
		ajpConnector.setSecure(false);
		ajpConnector.setAllowTrace(false);
		ajpConnector.setPort(ajpConnectorProperties.getAjp().getPort());
		ajpConnector.setRedirectPort(ajpConnectorProperties.getAjp().getAjpRedirectPort());
		return ajpConnector;
	}
*/
	/*
	private RemoteIpValve createRemoteIpValves() {
		if (LOG.isInfoEnabled()) {
			LOG.info("createRemoteIpValves");
		}
		RemoteIpValve remoteIpValve = new RemoteIpValve();

		remoteIpValve.setRemoteIpHeader("x-forwarded-for");
		remoteIpValve.setProtocolHeader("x-forwarded-protocol");
		return remoteIpValve;
	}
*/
}
