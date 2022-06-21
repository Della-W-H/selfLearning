package com.hong.Thread.One;

import lombok.SneakyThrows;

/**
 * @author wanghong
 * @date 2022/6/7
 * @apiNote jstack 查看各种状态的线程信息
 */
public class ReadStackLog {
    public static void main(String[] args) {
        new Thread(new TimeWaiting(),"TimeWaitingThread").start();
        new Thread(new Waiting(),"WaitingThread").start();
        //使用两个Blocked线程，一个获取锁成功，另一个被阻塞
        new Thread(new Blocked(),"BlockedThread-A").start();
        new Thread(new Blocked(),"BlockedThread-B").start();

        //todo 运行程序 jps+jstack 查看当前类的线程信息
    }
}

//该线程不断进行睡眠
class TimeWaiting implements Runnable{
    //加在方法上时 可以欺骗编译器 让RuntimeException不报错
    @SneakyThrows
    @Override
    public void run() {
       while (true){
           Thread.sleep(100000);
       }
    }
}
//该线程在Waiting.class实例等待
class Waiting implements Runnable{

    @Override
    public void run() {
        while (true){
            //todo 这个锁的 锁对象是其本身 意味着第一个进入这个对象的线程会将这个对象本身当成锁
            synchronized (Waiting.class){
                try {
                    //此时调用的是Object对象的 Wait()方法
                    Waiting.class.wait();
                } catch (InterruptedException e){
                    e.printStackTrace();
                }
            }
        }
    }
}
//该线程在Blocked.class实例上架=加锁后，不会释放该锁
class Blocked implements Runnable{

    @SneakyThrows
    @Override
    public void run() {
        synchronized (Blocked.class){
            while (true){
                Thread.sleep(100000);
            }
        }
    }
}
