package org.itstack.demo.design;

/**
 * 饿汉模式线程安全
 */
public class Singleton_03 {

    private static Singleton_03 instance = new Singleton_03();

    private Singleton_03() {
    }

    public static Singleton_03 getInstance() {
        return instance;
    }

}
