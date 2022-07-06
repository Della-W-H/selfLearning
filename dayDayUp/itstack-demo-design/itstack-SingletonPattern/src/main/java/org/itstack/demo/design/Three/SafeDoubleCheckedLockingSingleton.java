package org.itstack.demo.design.Three;


import java.io.Serializable;

/**
 * @author wanghong
 * @date 2022/6/10
 * @apiNote 安全的双重检查锁 实现的懒汉式单例模式
 */
public class SafeDoubleCheckedLockingSingleton implements Serializable {

    /**
     * 若是没有volatile修饰属性 可能 当第一次判断时 instance为null 线程拿到锁进入 第二次
     * 判断仍然为null 进行实例化
     * todo 实例化 有三个JVM指令  1.memory = allocate() 分配对象内存空间 -->2.ctorInstance(memory) 初始化对象--> 3.instance = memory 设置instance指向刚分配的内存地址
     * 不过 有需要注意点地方，即内存的指令优化重排序 会导致 2，3交换顺序 导致，线程2进入第一个判断时instance == null为false但是并没有给instance初始化 此时就会return instance导致一些未知的问题
     * todo volatile不加 单线程下确实没啥问题 多线程即可能出未知error
     */

    private volatile static SafeDoubleCheckedLockingSingleton instance;

    public static SafeDoubleCheckedLockingSingleton getInstance(){
        if (instance == null){
            synchronized (SafeDoubleCheckedLockingSingleton.class){
                if (instance == null){
                    instance = new SafeDoubleCheckedLockingSingleton();
                }
            }
        }
        return instance;
    }
}
