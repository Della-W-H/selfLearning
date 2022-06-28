package com.hong.SomeThingSimpleButDegraded.Three_FunctionProgramm;

/**
 * @author wanghong
 * @date 2022/6/27
 * @apiNote
 */
@FunctionalInterface
public interface Lambdas<T> {
   void get(T s);

    /**
     * 这个 equals方法继承自 父类 Object 并没有违反 函数接口 的定义即 只有一个抽象方法
     * @param o
     * @return
     */
   @Override
   boolean equals(Object o);
}
