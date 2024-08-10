package com.auction.common;

import com.auction.service.ICacheAccessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.*;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Copyright: 2016 7min.com.cn Inc. All rights reserved.
 */

@Component("redisCache")
public class RedisCacheAccessorImpl implements ICacheAccessor{
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    private Integer defaultExpireTime = 3600 /*秒*/;

    @Override
    public String getName() {
        return "redis-cache-server";
    }

    @Override
    public int maxKeyLength() {
        return 0;
    }

    @Override
    public int maxValueSize() {
        return 0;
    }

    @Override
    public int maxExpireTime() {
        return 0;
    }

    @Override
    public int minExpireTime() {
        return 0;
    }

    @Override
    public int defaultExpireTime() {
        return 0;
    }

    @Override
    public boolean hashSupported() {
        return true;
    }

    @Override
    public boolean exists(final String key) {
        return redisTemplate.hasKey(key);
    }

    @Override
    public int getTTL(String key) {
        Long ttl = redisTemplate.getExpire(key);
        if (null == ttl) {
            return -2;
        }

        return ttl.intValue();
    }

    @Override
    public boolean set(final String key, final String value) {
        BoundValueOperations<String, Object> valueOper = redisTemplate.boundValueOps(key);
        valueOper.set(value, defaultExpireTime, TimeUnit.SECONDS);
        return true;
    }

    @Override
    public boolean set(final String key, final String value, final int seconds) {
        BoundValueOperations<String, Object> valueOper = redisTemplate.boundValueOps(key);
        valueOper.set(value, seconds, TimeUnit.SECONDS);
        return true;
    }

    @Override
    public String getString(String key) {
        BoundValueOperations<String, Object> valueOper = redisTemplate.boundValueOps(key);
        return (String) valueOper.get();
    }

    @Override
    public boolean set(String key, Object value) {
        BoundValueOperations<String, Object> valueOper = redisTemplate.boundValueOps(key);
        valueOper.set(value, defaultExpireTime, TimeUnit.SECONDS);
        return true;
    }

    @Override
    public boolean set(String key, Object value, int seconds) {
        BoundValueOperations<String, Object> valueOper = redisTemplate.boundValueOps(key);
        valueOper.set(value, seconds, TimeUnit.SECONDS);
        return true;
    }

    @Override
    public Object get(String key) {
        BoundValueOperations<String, Object> valueOper = redisTemplate.boundValueOps(key);
        return valueOper.get();
    }

    @Override
    public Map<String, Object> getMulti(List<String> keyList) {
        ValueOperations<String, Object> valueOper = redisTemplate.opsForValue();
        Map<String, Object> result = new HashMap<>();

        keyList.stream().filter(e1 -> null != valueOper.get(e1)).forEach(e1 -> result.put(e1, valueOper.get(e1)));
        return result;
    }

    @Override
    public boolean hashAdd(String key, int seconds) {
        BoundHashOperations<String, String, Object> hashOper = redisTemplate.boundHashOps(key);
        hashOper.expire(seconds, TimeUnit.SECONDS);

        return true;
    }

    @Override
    public boolean hashSet(String key, String entryKey, Object entryValue) {
        HashOperations<String, String, Object> hashOperations = redisTemplate.opsForHash();
        hashOperations.put(key, entryKey, entryValue);
        return true;
    }

    @Override
    public Object hashGet(String key, String entryKey) {
        HashOperations<String, ?, ?> hashOperations = redisTemplate.opsForHash();
        return hashOperations.get(key, entryKey);
    }

    @Override
    public Map<String, Object> hashGetMulti(String key, List<String> entryKeyList) {
        HashOperations<String, String, Object> hashOperations = redisTemplate.opsForHash();
        Map<String, Object> entries = hashOperations.entries(key);
        Map<String, Object> result = new HashMap<>();
        entryKeyList.stream().filter(hkey -> entries.containsKey(hkey)).forEach(hkey -> {
            result.put(hkey, entries.get(hkey));
        });

        return result;
    }

    @Override
    public boolean batchSet(List<String> keys, List<Object> objects, int seconds) {
        redisTemplate.executePipelined((RedisCallback<Object>) redisConnection -> {
            for (int i = 0; i < keys.size(); i++) {
                String key = keys.get(i);
                Object object = objects.get(i);

                RedisSerializer valueSerializer = redisTemplate.getValueSerializer();
                redisConnection.setEx(redisTemplate.getStringSerializer().serialize(key), seconds, valueSerializer.serialize(object));
            }
            return null;
        });

        return true;
    }

    @Override
    public boolean delete(String key) {
        if (redisTemplate.hasKey(key)) {
            redisTemplate.delete(key);
            return true;
        }

        return false;
    }

    @Override
    public boolean delete(List<String> keyList) {
        redisTemplate.delete(keyList);
        return true;
    }

    @Override
    public long getTimeStamp(String key) {
        throw new RuntimeException("not implemented");
    }
}
