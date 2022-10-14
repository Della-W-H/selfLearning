package com.hong.Thread;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author wanghong
 * @date 2022/10/14
 * @apiNote
 */
@Slf4j
public class SomeThingWithJVMLock{

    volatile int a = 1;
    volatile int b = 1;

//    AtomicInteger a = new AtomicInteger(1);
//    AtomicInteger b = new AtomicInteger(1);
    public synchronized void add() {
        log.info("add start");
        for (int i = 0; i < 1000000; i++) {
//            a.incrementAndGet();
//            a.incrementAndGet();
            a++;
            b++;
        }
        log.info("add done");
    }
    public synchronized void compare() {
        log.info("compare start");
        for (int i = 0; i < 1000000; i++) {
            //a始终等于b吗？
            if (a < b) {
                log.info("a:{},b:{},{}", a, b, a > b);
                //最后的a>b应该始终是false吗？
            }
//            if (a.get() < b.get()) {
//                log.info("a:{},b:{},{}", a, b, a.get() > b.get());
//                //最后的a>b应该始终是false吗？
//            }
        }
        log.info("compare done");
    }
    //todo 将synchronized 去掉 就会发现 问题所在
    /*
    * 之所以出现这种错乱，是因为两个线程是交错执行 add 和 compare 方法中的业务逻辑，
    * 而且这些业务逻辑不是原子性的：a++ 和 b++ 操作中可以穿插在 compare 方法的比较代码中；
    * 更需要注意的是，a<b 这种比较操作在字节码层面是加载 a、加载 b 和比较三步，代码虽然是一行但也不是原子性的。
    *
    * 所以，正确的做法应该是，为 add 和 compare 都加上方法锁，确保 add 方法执行时，compare 无法读取 a 和 b
    *
    * 解释 为啥 第二个方法 不加同步关键字 就不行
    * 因为 这个两个关键字的 锁对象 其实指向的都是同一个东西 即被创建出来的类对象
    * 之所以不行 是因为 当第一个方法上锁的时候 这个片 类对象的同步区域会被上锁 但是并不会 影响 类对象未被上锁区域的访问
    * 这个意味着 a和b 的实时变动 compare方法随意都可以看见 而且 a++;b++;是两步操作 并不具有原子性
    * 但是如果不加关键字 另一种方式就是将a和b替换成 锁对象类型
    * 而且从执行细节上来说 二者也有细微的差别
    * 前者 是顺序执行
    * 后者 仍然是交叉执行
    * 解释： 因为前者锁对象 是同一个类即此处的lock 所以第二个线程进入的时候只有等 add()执行完毕 才能执行compare
    *       而后者 锁对象为a和b lock并没有加索 意味着add()和compare()方法是可以交叉执行的
    * */

    public static void main(String[] args){
        SomeThingWithJVMLock lock=new SomeThingWithJVMLock();
        new Thread(lock::add).start();
        new Thread(lock::compare).start();
    }
}

