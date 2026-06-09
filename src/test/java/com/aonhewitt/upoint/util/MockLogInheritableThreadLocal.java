/*
 * package com.aonhewitt.upoint.util;
 * 
 * import java.util.ArrayList; import java.util.List;
 * 
 * import com.alight.logging.constant.UpointLogConstants; import
 * com.alight.logging.dto.RedisCallCountStorage; import
 * com.alight.logging.helpers.LogInheritableThreadLocal;
 * 
 * public class MockLogInheritableThreadLocal {
 * 
 * public static LogInheritableThreadLocal get(){ RedisCallCountStorage
 * redisCallCountStorage = new RedisCallCountStorage(); List<Integer>
 * redisCallCountList = new ArrayList<>(); redisCallCountList.add(4);
 * redisCallCountList.add(5); redisCallCountList.add(8);
 * redisCallCountStorage.setRedisMgetCountList(redisCallCountList);
 * redisCallCountStorage.setRedisSingleGetCount(12);
 * 
 * LogInheritableThreadLocal instance = new LogInheritableThreadLocal();
 * 
 * instance.put(UpointLogConstants.REDIS_CALL_COUNT_POOL,
 * redisCallCountStorage);
 * 
 * return instance;
 * 
 * } }
 */