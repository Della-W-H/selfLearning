package org.itstack.demo.design.agent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.FactoryBean;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

public class MapperFactoryBean<T> implements FactoryBean<T> {

    private Logger logger = LoggerFactory.getLogger(MapperFactoryBean.class);

    private Class<T> mapperInterface;

    public MapperFactoryBean(Class<T> mapperInterface) {
        this.mapperInterface = mapperInterface;
    }

    @Override
    @SuppressWarnings("all")
    public T getObject() throws Exception {
        //todo remeber? lambda表达式 可以进入源码 看一下 这 指代的就是一个 handler.invoke()方法 只是这个InvocationHandler没有添加FunctionInterface注解罢了
        /*InvocationHandler handler = (proxy, method, args) -> {
            Select select = method.getAnnotation(Select.class);
            logger.info("SQL：{}", select.value().replace("#{uId}", args[0].toString()));
            return args[0] + ",小傅哥,bugstack.cn - 沉淀、分享、成长，让自己和他人都能有所收获！";
        };*/
        //return (T) Proxy.newProxyInstance(this.getClass().getClassLoader(), new Class[]{mapperInterface}, handler);

        return (T) Proxy.newProxyInstance(this.getClass().getClassLoader(), new Class[]{mapperInterface},/*这就是个方法引用*/(proxy, method, args) -> {
            Select select = method.getAnnotation(Select.class);
            logger.info("SQL：{}", select.value().replace("#{uId}", args[0].toString()));
            return args[0] + ",小傅哥,bugstack.cn - 沉淀、分享、成长，让自己和他人都能有所收获！";
        });
    }

    @Override
    public Class<?> getObjectType() {
        return mapperInterface;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

}
