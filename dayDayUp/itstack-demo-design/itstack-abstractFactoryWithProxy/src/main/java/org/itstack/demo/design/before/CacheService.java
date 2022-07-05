package org.itstack.demo.design.before;

import java.util.concurrent.TimeUnit;

/**
 * 添加个 标记识别 不同redis处理源
 */
public interface CacheService {

    String get(final String key, int redisType);

    void set(String key, String value, int redisType);

    void set(String key, String value, long timeout, TimeUnit timeUnit, int redisType);

    void del(String key, int redisType);

}
