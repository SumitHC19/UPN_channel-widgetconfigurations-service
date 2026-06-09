package com.aonhewitt.upoint.util;

import com.aonhewitt.exceptions.AHBaseException;

public class MockURIResorceLocator {
	//@Mock
	public String getResourceURI(String a) throws AHBaseException{
		
		return "httpps://udp/uri";
	}
}