package com.aonhewitt.upoint.db;

import static org.junit.jupiter.api.Assertions.assertTrue;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import com.aonhewitt.upoint.util.MockDockerSecretUtil;
import com.aonhewitt.upoint.util.MockDockerSecretUtilFail;

public class DBConnectionPropertiesTest {

	@BeforeEach
	public void setup() {

//		MockedStatic<MockDockerSecretUtilFail> mockDockerSecretUtilFail = Mockito.mockStatic(MockDockerSecretUtilFail.class);
//		mockDockerSecretUtilFail.when(() -> MockDockerSecretUtilFail.load()).thenReturn(new MockDockerSecretUtil());


	}

	@Test
	public void testInit() {

		DBConnectionProperties dbProps = new DBConnectionProperties();

		JndiDataSource geolocation = new JndiDataSource();

		dbProps.setGeolocation(geolocation);

		dbProps.initDb();

		//assertTrue(dbProps.getGeolocation().getPassword().equals("password"));
		//assertTrue(dbProps.getGeolocation().getUsername().equals("username"));

	}
}