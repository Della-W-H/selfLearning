package org.itstack.demo.design;

/**
 * 内部静态类 懒汉模式 线程安全 有jvm保证
 */
public class Singleton_04 {

    private static class SingletonHolder {
        private static Singleton_04 instance = new Singleton_04();
    }

    private Singleton_04() {
    }

    public static Singleton_04 getInstance() {
        return SingletonHolder.instance;
    }

}
