package com.hong.Thread;

import java.util.stream.IntStream;

/**
 * @author wanghong
 * @date 2022/10/14
 * @apiNote
 * 在非静态的 wrong 方法上加锁，只能确保多个线程无法执行同一个实例的 wrong 方法，
 * 却不能保证不会执行不同实例的 wrong 方法。而静态的 counter 在多个实例中共享，所以必然会出现线程安全问题。
 *
 * 你可能要问了，把 wrong 方法定义为静态不就可以了，这个时候锁是类级别的。
 * 可以是可以，但我们不可能为了解决线程安全问题改变代码结构，把实例方法改为静态方法。
 */
public class SomeThingWithJVMStaticVariableSyncTrouble{

    public static void main(String[] args){
        Data.reset();

        //IntStream.rangeClosed(1,1000000).parallel().forEach(i->new Data().wrong());
        IntStream.rangeClosed(1,1000000).parallel().forEach(i->new Data().right());
        System.out.println(Data.getCount());
    }

}

class Data{
    private static int count = 0;
    private static Object locker = new Object();
    public static int reset(){
        count = 0;
        return count;
    }
    public synchronized void wrong(){
        count++;
    }
    public void right() {
        synchronized (locker) {
            count++;
        }
    }
    public static int getCount(){
        return count;
    }
}
