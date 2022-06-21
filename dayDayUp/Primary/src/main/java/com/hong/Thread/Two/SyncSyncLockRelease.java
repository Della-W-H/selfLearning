package com.hong.Thread.Two;

import lombok.SneakyThrows;
import org.openjdk.jol.info.ClassLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.LockSupport;

public class SyncSyncLockRelease {
    static Thread A;
    static Thread B;

    @SuppressWarnings("all")
    public static void main(String[] args) {
        final List<Object> list = new ArrayList<>();
       //-XX:BiasedLockingStartupDelay=0 默认无延迟开启偏向锁
        A = new Thread() {
            @SneakyThrows
            @Override
            public void run() {
                Object a = new Object();
                list.add(a);
                System.out.println("AAAA加锁前" + ClassLayout.parseInstance(a).toPrintable());
                synchronized (a) {
                    System.out.println("AAAA加锁中" + ClassLayout.parseInstance(a).toPrintable());
                }
                System.out.println("AAAA加锁后" + ClassLayout.parseInstance(a).toPrintable());
                //防止竞争 执行完后唤醒线程B/ 确保A线程 死亡 Terminated
                LockSupport.unpark(B);
            }
        };
        B = new Thread() {
            @Override
            public void run() {
                LockSupport.park();
                Object a = list.get(0);
                System.out.println("线程BBBB加锁前" + ClassLayout.parseInstance(a).toPrintable());
                synchronized (a) {
                    //轻量级锁状态 000
                    System.out.println("线程BBBB加锁中" + ClassLayout.parseInstance(a).toPrintable());
                }
                //轻量级锁释放后，会变成无锁状体 001
                System.out.println("线程BBBB加锁后" + ClassLayout.parseInstance(a).toPrintable());
                System.out.println("新产生的对象" + ClassLayout.parseInstance(new Object()).toPrintable());
            }
        };
        A.start();
        B.start();
    }
}