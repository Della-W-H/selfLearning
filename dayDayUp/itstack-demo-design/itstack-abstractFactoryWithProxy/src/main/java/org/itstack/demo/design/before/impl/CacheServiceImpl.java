package org.itstack.demo.design.before.impl;

import org.itstack.demo.design.before.CacheServiceOnlyRedisUtils;
import org.itstack.demo.design.before.RedisUtils;

import java.util.concurrent.TimeUnit;

public class CacheServiceImpl implements CacheServiceOnlyRedisUtils {

    private RedisUtils redisUtils = new RedisUtils();

    @Override
    public String get(String key) {
        return redisUtils.get(key);
    }

    @Override
    public void set(String key, String value) {
        redisUtils.set(key, value);
    }

    @Override
    public void set(String key, String value, long timeout, TimeUnit timeUnit) {
        redisUtils.set(key, value, timeout, timeUnit);
    }

    @Override
    public void del(String key) {
        redisUtils.del(key);
    }

}
