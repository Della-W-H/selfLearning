package org.itstack.demo.design;

/**
 * 懒汉模式线程不安全
 */
public class Singleton_01 {

    private static Singleton_01 instance;

    private Singleton_01() {
    }

    public static Singleton_01 getInstance(){
        if (null != instance) return instance;
        return new Singleton_01();
    }

}
