package com.aonhewitt.upoint.db;

import static org.junit.jupiter.api.Assertions.assertEquals;


import org.apache.tomcat.util.descriptor.web.ContextResource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class DBUtilTest {

	DBUtil dbUtil = new DBUtil();
	DBConnectionProperties dbProps = new DBConnectionProperties();
	DataSourceConnection d = new DataSourceConnection();
	

	@BeforeEach
	public void setup() {

		JndiDataSource geoDs = new JndiDataSource();
		d.setMaxActive("55");
		d.getProperties().put("cc","cc");
		d.getProperties().put("Test", "Test");
		geoDs.setUrl("geoUrl");
		geoDs.setJndi("/geolocaiton");
		geoDs.setDriverClassName("com.oracle.thin");
		geoDs.setUsername("geolocaiton");
		geoDs.setPassword("geolocaiton");
		geoDs.setDefaultSchema("geolocation");
		dbProps.setGeolocation(geoDs);
		dbProps.setDataSourceConnection(d);
		dbProps.getDataSourceConnection().getProperties().put("Test", "Test");

		dbUtil.setdBConnectionProperties(dbProps);
	}

	@Test
	public void testContext() throws Exception {

		ContextResource geoResource = dbUtil.getGeoLocationContextResource();
		assertEquals("geoUrl", geoResource.getProperty("url"));

	}

}
