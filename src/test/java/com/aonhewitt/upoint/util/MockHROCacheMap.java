package com.aonhewitt.upoint.util;

import static org.mockito.Mockito.mock;

import com.alight.cloud.data.util.HROCacheMap;
import com.alight.cloud.data.util.HROCacheValueHolder;

public class MockHROCacheMap {
	private  static HROCacheMap hroCacheMapMock = mock(HROCacheMap.class);
	private  HROCacheValueHolder holderobj = mock(HROCacheValueHolder.class);
	
	public static HROCacheMap getInstance() {
		
		return hroCacheMapMock; 
	}
	
	public boolean containsKey(Object obj) {
		
		return true; 
	}
	
	public HROCacheValueHolder remove(Object obj) {
		
		return holderobj; 
	}
}