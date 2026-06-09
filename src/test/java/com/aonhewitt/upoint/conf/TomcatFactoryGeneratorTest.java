package com.aonhewitt.upoint.conf;


import static org.junit.jupiter.api.Assertions.assertEquals;


import org.apache.catalina.Context;
import org.apache.catalina.core.StandardContext;
import org.apache.catalina.startup.Tomcat;
import org.apache.tomcat.util.descriptor.web.ContextResource;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.web.embedded.tomcat.TomcatWebServer;

import com.aonhewitt.upoint.db.DBUtil;
import com.aonhewitt.upoint.util.MockTomcatFactoryGenerator;

@ExtendWith(MockitoExtension.class)
public class TomcatFactoryGeneratorTest {
	@InjectMocks
	@Spy
	TomcatFactoryGenerator gen = new TomcatFactoryGenerator();
	
	@org.mockito.Mock
	DBUtil dbUtil ;
	
	@org.mockito.Mock
	AjpConnectorProperties ajpConnectorProperties ;
	
	

	

	//@Test
	public void test() {
		new MockTomcatFactoryGenerator();
		Tomcat tom = new Tomcat();
		TomcatWebServer cont = gen.getTomcatWebServer(tom);
		
		assertEquals(true, cont == null);

	}
	
	@Test
	public void testPostConstruct() {
		ContextResource resourceBase = new ContextResource();
		resourceBase.setType("DataSource");
		resourceBase.setName("CL101");
		resourceBase.setProperty("driverClassName", "OracleDriver");
		resourceBase.setProperty("url", "l8sita09:9711");
		resourceBase.setProperty("username", "system");
		resourceBase.setProperty("password", "password");
		/*List<ContextResource> list = new ArrayList<ContextResource>();
		list.add(resourceBase);*/
		//Ajp ajp = new Ajp();
		//ajp.setEnabled(true);
		
		Mockito.when(dbUtil.getGeoLocationContextResource()).thenReturn(resourceBase);
	  //Mockito.when(ajpConnectorProperties.getAjp()).thenReturn(ajp);
		/*Mockito.when(dbUtil.getClientContextResource(Mockito.anyBoolean())).thenReturn(list);
		Mockito.when(dbUtil.getUcceBaseContextResource()).thenReturn(resourceBase);*/
		
			
		Context context = new StandardContext();
		gen.postProcessContext(context);
	
	}
}