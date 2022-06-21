package com.hong.Thread.Three;

import java.io.Serializable;

/**
 * @author wanghong
 * @date 2022/6/10
 * @apiNote 静态内部类实现的懒汉式单例 目前最常用的实现方式
 */
public class InnerClassSingleton implements Serializable {


    private static class Holder{
        //static 修饰的 JVM会保证这个类的多线程下的单例和安全性
        //todo 因为 JVM在对一个对象初始化时会自动获得一个锁，这是JVM自动实现的 反正大家都这么说 ？
        static InnerClassSingleton INSTANCE = new InnerClassSingleton();
    }

    public static InnerClassSingleton getInstance(){
        return Holder.INSTANCE;
    }


}
