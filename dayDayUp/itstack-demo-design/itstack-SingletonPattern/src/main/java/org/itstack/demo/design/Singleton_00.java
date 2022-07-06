package org.itstack.demo.design;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Singleton_00 {

    /**
     * 静态类 本身就是一种 全局类变量 即 单例模式
     */
    public static Map<String,String> cache = new ConcurrentHashMap<String, String>();

}
