package com.aonhewitt.upoint.conf;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class AjpConnectorPropertiesTest {

	AjpConnectorProperties app = new AjpConnectorProperties();
	Ajp ajp ;
	
	@BeforeEach
	public void setup() {
		
		ajp = app.getAjp();
		ajp.setEnabled(app.getAjp().isEnabled());
		ajp.setPort(app.getAjp().getPort());
		ajp.setAjpRedirectPort(app.getAjp().getAjpRedirectPort());
		ajp.setRemoteauthentication(app.getAjp().getRemoteauthentication());
		app.setAjp(ajp);
	}
	
	@Test
	public void testGetAjp()
	{
		
		assertNotNull(ajp);
	}
}
