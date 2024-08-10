package com.auction.service;

import java.util.List;
import java.util.Map;

public interface ICacheAccessor {
	String getName();
	
	/*
	 * 返回所允许的Key最大字符数
	 */
	int maxKeyLength();
	
	/*
	 * 返回所允许的Value最大字节数
	 */
	int maxValueSize();
	
	/*
	 * 返回允许的最大过期时间（单位：秒）
	 * 0：表示允许永不过期
	 */
	int maxExpireTime();
	
	/*
	 * 返回允许的最小过期时间（单位：秒）
	 */
	int minExpireTime();
	
	/*
	 * 返回缺省的过期时间（单位：秒）
	 * 0：表示允许永不过期
	 */
	int defaultExpireTime();
	
	/*
	 * 是否支持hashSet,hashGet,hashGetMulti的操作
	 */
	boolean hashSupported();
	
	boolean exists(String key);
	
	/*
	 * 获取指定key的剩余时间（单位：秒）
	 * TTL: time to live
	 *   -1, 如果key没有设置到期超时。
     *   -2, 如果键不存在。
	 */
	int getTTL(String key);
	
	//获取设置缓存的时间
	long getTimeStamp(String key);
	
	boolean set(String key, String value);
	boolean set(String key, String value, int seconds);
	String getString(String key);
	
	boolean set(String key, Object value);
	boolean set(String key, Object value, int seconds);
	Object get(String key);
	Map<String, Object> getMulti(List<String> keyList);
	
	boolean hashAdd(String key, int seconds);
	boolean hashSet(String key, String entryKey, Object entryValue);
	Object hashGet(String key, String entryKey);
	Map<String, Object> hashGetMulti(String key, List<String> entryKeyList);

    boolean batchSet(List<String> keys, List<Object> objects, int seconds);

	boolean delete(String key);
	boolean delete(List<String> keyList);

    String YM_NAV_CACHE_PREFIX = "ym_nav_cache_";
    String IN_SERVICE_KEY = "in_service_key";
    String USER_OPENID_KEY = "user_openid_key1_%d";
    String USER_SCENE_KEY = "user_scene_key_%d";
    String FUND_CODE_AND_NAME_KEY = "fund_code_name_%s";
    String FUND_SHARE_TIMES = "fund_share_times";
    String DEFAULT_MIN_RATE = "default_min_rate";
    String DEFAULT_MIN_EXCEED_RATE = "default_min_exceed_rate";
    String FUND_SUB_INFO = "fund_sub_info_";
    //防止穿透;使用的默认值
    String STRIKE_VALUE = "null";
}
