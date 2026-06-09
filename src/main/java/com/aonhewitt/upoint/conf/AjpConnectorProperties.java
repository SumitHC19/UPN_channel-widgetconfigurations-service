package com.aonhewitt.upoint.conf;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author Yogesh M
 */
@Configuration
@ConfigurationProperties(prefix = "tomcat")
public class AjpConnectorProperties {

	private Ajp ajp = new Ajp();

	public Ajp getAjp() {
		return ajp;
	}

	public void setAjp(Ajp ajp) {
		this.ajp = ajp;
	}

}

class Ajp {

	private boolean enabled;
	private int port;
	private int ajpRedirectPort;
	private String remoteauthentication;

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public int getAjpRedirectPort() {
		return ajpRedirectPort;
	}

	public void setAjpRedirectPort(int ajpRedirectPort) {
		this.ajpRedirectPort = ajpRedirectPort;
	}

	public String getRemoteauthentication() {
		return remoteauthentication;
	}

	public void setRemoteauthentication(String remoteauthentication) {
		this.remoteauthentication = remoteauthentication;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

}