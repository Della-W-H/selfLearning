package org.itstack.demo.design.changed.factory;

import org.itstack.demo.design.changed.utils.ClassLoaderUtils;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * 工厂代理实现类
 */
public class JDKInvocationHandler implements InvocationHandler {

    private ICacheAdapter cacheAdapter;

    //注入适配器
    public JDKInvocationHandler(ICacheAdapter cacheAdapter) {
        this.cacheAdapter = cacheAdapter;
    }

    /**
     *
     * @param proxy 代理对象 Proxy 一般情况下 这个代理类并不会被使用
     * @param method 方法名字
     * @param args 入参值
     * @return 方法执行结果
     * @throws Throwable 此处以为着其有复杂的执行逻辑
     */
    @SuppressWarnings("all")
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        //此处是通过 被代理对象的 要执行的方法的名字 拿到适配器相对应的方法的名字
        //todo 意味着适配器 肯定有不同方法的名字
        return ICacheAdapter.class.getMethod(method.getName(), /*通过入参拿到各种参数的数据类型*/ClassLoaderUtils.getClazzByArgs(args)).invoke(cacheAdapter, args);
    }

}
