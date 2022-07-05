package org.itstack.demo.design.changed.impl;

import org.itstack.demo.design.before.RedisUtils;

import java.util.concurrent.TimeUnit;

public class CacheServiceImpl implements CacheService {

    //模拟redis
    //此处类 并不能 处理复合的集群业务 但是可以拿到他的 接口父类 通过反射代理模式进行再实现
    //同时不会 破坏原有代码处理

    private RedisUtils redisUtils = new RedisUtils();

    public String get(String key) {
        return redisUtils.get(key);
    }

    public void set(String key, String value) {
        redisUtils.set(key, value);
    }

    public void set(String key, String value, long timeout, TimeUnit timeUnit) {
        redisUtils.set(key, value, timeout, timeUnit);
    }

    public void del(String key) {
        redisUtils.del(key);
    }

}
