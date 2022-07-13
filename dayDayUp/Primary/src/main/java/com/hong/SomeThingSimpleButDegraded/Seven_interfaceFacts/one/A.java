package com.hong.SomeThingSimpleButDegraded.Seven_interfaceFacts.one;

/**
 * @author wanghong
 * @date 2022/7/12
 * @apiNote 继承 对于接口之间而言 不需要 他们之间 的 方法 被实现
 */
public interface A {
    void speak();
}

interface B extends A{

}

class C{}

abstract class D extends C{}