package com.aonhewitt.upoint.util;

import java.util.ArrayList;
import java.util.Collection;

import com.aonhewitt.upoint.cache.exception.BaseCacheException;

public class MockSessionRedisObjectProvider {
		
		Object find(String key, String arg1) throws BaseCacheException {

			if (key.equals("19921_1.0:CORE_TXT_RESOLVER:aa-es_us")) {
				return "SessionCachedObject";
			}

			if (key.equals("chnl-widget:hasbro")) {
				return "SessionCachedObject";
			}

			return null;

		}

		
		void delete(String arg0, String arg1) throws BaseCacheException {

		}
		
		Collection<String> findKeys(String aCacheKeyPattern) throws BaseCacheException {
			Collection<String> keys = new ArrayList<String>();
			keys.add("chnl-widget:hasbro1");
			return keys;
		}
}
