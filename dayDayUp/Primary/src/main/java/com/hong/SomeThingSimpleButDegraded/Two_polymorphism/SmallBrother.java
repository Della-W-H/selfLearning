package com.hong.SomeThingSimpleButDegraded.Two_polymorphism;

/**
 * @author wanghong
 * @date 2022/6/22
 * @apiNote
 */
public class SmallBrother implements Father {
    @Override
    public String speak() {
        return "small brother is speaking";
    }

    @Override
    public String work() {
        return "small brother is working";
    }
}
