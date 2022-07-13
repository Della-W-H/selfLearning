package com.hong.SomeThingSimpleButDegraded.Eight_testClassIsAssignableFromMethod;

/**
 * @author wanghong
 * @date 2022/7/12
 * @apiNote
 */
public class A {
    public static void main(String[] args) {
        A a = new A();
        if (a.getClass().isAssignableFrom(B.class)){
            System.out.println("这个方法是判断当前类是否 为 给定类的超类");
        }
    }
}

class B extends A{}


