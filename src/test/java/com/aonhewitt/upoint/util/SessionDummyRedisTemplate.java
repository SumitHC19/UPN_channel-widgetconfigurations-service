package com.aonhewitt.upoint.util;

import java.util.HashSet;
import java.util.Set;

import org.springframework.data.redis.core.RedisTemplate;

public class SessionDummyRedisTemplate extends RedisTemplate{
	 
    @Override
    public Set keys(Object pattern) {
                // TODO Auto-generated method stub
                
                HashSet set = new HashSet();
                set.add("Key 2");
                return set;
    }

}
