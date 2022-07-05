package org.itstack.demo.design.before;

import java.util.concurrent.TimeUnit;

/**
 * 只是用 redis单体模式
 */
public interface CacheServiceOnlyRedisUtils {

    String get(final String key);

    void set(String key, String value);

    void set(String key, String value, long timeout, TimeUnit timeUnit);

    void del(String key);

}
