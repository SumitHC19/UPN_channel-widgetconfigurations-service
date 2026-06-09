package com.aonhewitt.upoint.core.controller.unit.widget;

import static org.mockito.Mockito.mock;

import com.aonhewitt.upoint.cache.util.AppConfigReader;

public class MockAppConfigReader  {
	
	private  static AppConfigReader appConfigReaderMock = mock(AppConfigReader.class);
		//@Mock
		public static AppConfigReader getInstance(){
			return appConfigReaderMock;
		}
}
