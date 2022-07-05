package org.itstack.demo.design.changed.factory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

/**
 * 抽象工厂
 */
public class JDKProxy {

    @SuppressWarnings("all")
    public static <T> T getProxy(Class<T> interfaceClass, ICacheAdapter cacheAdapter) throws Exception {
        InvocationHandler handler = new JDKInvocationHandler(cacheAdapter);
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        //进去看这个方法的源码 是拿到 父类接口
        Class<?>[] classes = interfaceClass.getInterfaces();

        //CacheService proxy_EGM = JDKProxy.getProxy(CacheServiceImpl.class, new EGMCacheAdapter());

        //可以将newProxyInstance的参数A,B,C
        //A 即加载代理类的类加载器
        //B 被代理的接口方法 数组 可以代理多个接口
        //C 实际处理类

        /**
         *  这个方法的处理逻辑即 通过类加载器 对接口数组对象们 生成一个 代理类 Proxy 这个代理类继承这些接口数组所有的方法
         *  但是 执行其中的任意一个方法 都会调用 实际处理类handler 来执行  执行方式是通过经典的invoke反射调用
         *  handler类中会被注入 指定的 具体适配器执行类 而不是使用 生成的代理类 这个代理类一般都不会使用
         */
        return (T) Proxy.newProxyInstance(classLoader, new Class[]{classes[0]}, handler);
    }

}
