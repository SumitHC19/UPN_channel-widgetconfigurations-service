package com.aonhewitt.upoint.util;

import java.util.ArrayList;
import java.util.Collection;

import com.aonhewitt.upoint.cache.exception.BaseCacheException;

public class MockRedisObjectProvider {
	
	Object find(String key, String arg1) throws BaseCacheException {

		if (key.equals("19920_1.0:CORE_TXT_RESOLVER:aa-en_us")) {
			return "CachedObject";
		}

		if (key.equals("chnl-widget:hasbro")) {
			return "CachedObject";
		}

		return null;

	}

	
	void delete(String arg0, String arg1) throws BaseCacheException {

	}
	
	Collection<String> findKeys(String aCacheKeyPattern) throws BaseCacheException {
		Collection<String> keys = new ArrayList<String>();
		keys.add("chnl-widget:hasbro");
		return keys;
	}
}
