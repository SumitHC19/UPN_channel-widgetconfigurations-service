package com.aonhewitt.upoint.util;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;

public class MockObjectMapper {
	public JsonNode readTree(String content)
	        throws IOException, JsonProcessingException{
		throw new NullPointerException();
	}
}
