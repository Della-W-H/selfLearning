package com.hong.SomeThingSimpleButDegraded.Three_FunctionProgramm.example3_lambdaProblem;

import java.util.function.Consumer;

public class LambdaTest {

    Object instanceObj = new Object();

    private void test() {
        // 用于直接引用
        Object localObj1 = new Object();
        // 用于传参
        Object localObj2 = new Object();
        Consumer consumer = (x) -> {
            System.out.println(x);
            System.out.println(localObj1);
            System.out.println(instanceObj);
        };
        consumer.accept(localObj2);
    }

    public static void main(String[] args) {
        new LambdaTest().test();
    }
}
