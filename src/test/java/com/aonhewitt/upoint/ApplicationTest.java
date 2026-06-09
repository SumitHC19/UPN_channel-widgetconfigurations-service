package com.aonhewitt.upoint;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
//@PrepareForTest(SpringApplication.class)
public class ApplicationTest {

	@AfterEach
	public void clear() {
		Mockito.clearAllCaches();
	}
	@BeforeEach
	public void setup() {
		Mockito.mockStatic(SpringApplication.class);
	}
	
	@Test
	public void testMain() throws Exception {
		String[] args = new String[] {};
		when(SpringApplication.run(Application.class, args)).thenReturn(mock(ConfigurableApplicationContext.class));
		Application.main(args);
		
	}
	
	@Test
	public void testTom() throws Exception {
		Application app = new Application();
			app.tomcatFactory();	
	}

}
