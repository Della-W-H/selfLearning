package com.hong.designModule.FactoryTemplatePattern;

/**
 * @author wanghong
 * @date 2022/7/1
 * @apiNote
 */
@FunctionalInterface
public interface TriFunction<T,U,V,R> {
    R apply(T t, U u, V v);
}
