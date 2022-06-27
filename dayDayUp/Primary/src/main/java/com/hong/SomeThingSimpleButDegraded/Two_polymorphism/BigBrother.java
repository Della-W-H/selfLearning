package com.hong.SomeThingSimpleButDegraded.Two_polymorphism;

/**
 * @author wanghong
 * @date 2022/6/22
 * @apiNote
 */
public class BigBrother extends Mother implements Father {
    @Override
    public String speak() {
        return "i`m big brother";
    }

    @Override
    public String work() {
        return "big brother is working!";
    }
}
