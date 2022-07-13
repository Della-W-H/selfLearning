package com.hong.SomeThingSimpleButDegraded.Nine_SomeMultipleExtendsUseByAbstractClass;

import java.util.HashMap;
import java.util.Map;

/**
 * @author wanghong
 * @date 2022/7/13
 * @apiNote 观察 这几个方法的执行流程 通过debug模式 看源码就会发现 许多流程就是这样执行的
 *    这就是多余 继承的 灵活运用 即 子类会直接 在 实例化期间 以栈的形式 加载 所有的父类链条中的 构造方法和 所有的属性值
 */
public class A extends B{
    public static void main(String[] args) {
        A a = new A();
        a.doSpeak();
        a.speak();
        System.out.println(a.map);


    }
}

abstract class B extends C {
    @Override
    public void speak() {
        System.out.println("we made it!!");
    }
}

abstract class C implements D{
    protected Map<String,String> map = new HashMap<String,String>(){
        {
            put("della","hong");
        }
    };

    public C(){
        map.put("phoenix","wang");
    }

    void doSpeak(){speak();}
}

interface D {
    void speak();
}

abstract class E extends B{
    @Override
    public void speak() {
        System.out.println("can you find me?");
    }
}