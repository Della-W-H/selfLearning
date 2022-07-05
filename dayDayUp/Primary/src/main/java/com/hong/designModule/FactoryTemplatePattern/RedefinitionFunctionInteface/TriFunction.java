package com.hong.designModule.FactoryTemplatePattern.RedefinitionFunctionInteface;

/**
 * @author wanghong
 * @date 2022/7/1
 * @apiNote 自定义功能函数接口
 */
@FunctionalInterface
public interface TriFunction<T,U,V,R> {
    R apply(T t, U u, V v);
}
