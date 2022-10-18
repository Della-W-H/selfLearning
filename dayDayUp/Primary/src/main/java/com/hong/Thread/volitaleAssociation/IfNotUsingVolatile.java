package com.hong.Thread.volitaleAssociation;

import java.util.concurrent.TimeUnit;

/**
 * @author wanghong
 * @date 2022/10/18
 * @apiNote
 * 这个b 不加volatile修饰 主程序 执行都执行不完
 * 其实，这是可见性的问题。
 *
 * 虽然另一个线程把 b 设置为了 false，但是这个字段在 CPU 缓存中，另一个线程（主线程）还是读不到最新的值。
 * 使用 volatile 关键字，可以让数据刷新到主内存中去。准确来说，让数据刷新到主内存中去是两件事情：
 *
 * 将当前处理器缓存行的数据，写回到系统内存；
 *
 * 这个写回内存的操作会导致其他 CPU 里缓存了该内存地址的数据变为无效。
 *
 * 当然，使用 AtomicBoolean 等关键字来修改变量 b 也行。但相比 volatile 来说，
 * AtomicBoolean 等关键字除了确保可见性，还提供了 CAS 方法，具有更多的功能，在本例的场景中用不到
 */
public class IfNotUsingVolatile{
    private static boolean b = true;

    @SuppressWarnings("all")
    public static void main(String[] args) throws InterruptedException {
        new Thread(()->{
            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) { }
            b =false;
        }).start();
        while (b) {
            TimeUnit.MILLISECONDS.sleep(0);
        }
        System.out.println("done");
    }
}
