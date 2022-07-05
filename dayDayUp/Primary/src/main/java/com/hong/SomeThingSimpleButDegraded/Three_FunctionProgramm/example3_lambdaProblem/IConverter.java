package com.hong.SomeThingSimpleButDegraded.Three_FunctionProgramm.example3_lambdaProblem;

@FunctionalInterface
public interface IConverter<F, T> {
    //将F转化为T 如同 Function.apply()方法一样的功能
    T convert(F from);

    default void speak(){
        System.out.println("im in");
    }
}