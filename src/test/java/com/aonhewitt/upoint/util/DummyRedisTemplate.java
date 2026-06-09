package com.aonhewitt.upoint.util;
import java.util.HashSet;
import java.util.Set;

import org.springframework.data.redis.core.RedisTemplate;

public class DummyRedisTemplate extends RedisTemplate{
 
            @Override
            public Set keys(Object pattern) {
                        // TODO Auto-generated method stub
                        String keyname=(String) pattern;
                        if(keyname.equals("CWC:SystemParameters:*")){
                        	return null;
                        }else{
	                        HashSet set = new HashSet();
	                        set.add("Key 1");
	                        return set;
                        }
            }
            
} 

