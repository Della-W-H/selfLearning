package com.hong.SomeThingSimpleButDegraded.Nine_SomeMultipleExtendsUseByAbstractClass;

/**
 * @author wanghong
 * @date 2022/7/13
 * @apiNote 观察 这几个方法的执行流程 通过debug模式 看源码就会发现 许多流程就是这样执行的
 */
public class A extends B{
    public static void main(String[] args) {
        A a = new A();
        a.doSpeak();
        a.speak();
    }
}

abstract class B extends C {
    @Override
    public void speak() {
        System.out.println("we made it!!");
    }
}

abstract class C implements D{
    void doSpeak(){speak();}
}

interface D {
    void speak();
}