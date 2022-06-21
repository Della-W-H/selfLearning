package com.hong.Thread.Two;

import lombok.SneakyThrows;
import org.openjdk.jol.info.ClassLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.LockSupport;

public class SyncSyncLockRelease1 {
    @SuppressWarnings("all")
    public static void main(String[] args) throws InterruptedException {
        //-XX:BiasedLockingStartupDelay=0 默认无延迟开启偏向锁
        Object obj = new Object();
        System.out.println("====A 加锁前==="+ClassLayout.parseInstance(obj).toPrintable());
        Thread A = new Thread() {
            @SneakyThrows
            @Override
            public void run() {
                synchronized (obj) {
                    System.out.println("===A 加锁中==="+ClassLayout.parseInstance(obj).toPrintable());
                    //sleep 此时 即A线程未释放锁的时候 B线程争抢锁 锁对象会直接变成重量级锁
                    //Thread.sleep(2000);
                }
                //sleep 在A线程释放锁 未死亡或者死亡 时B线程争抢锁 会见锁变为轻量级 000
                Thread.sleep(2000);
            }
        };
        A.start();
        Thread.sleep(500);
        System.out.println("====B加锁前==="+ClassLayout.parseInstance(obj).toPrintable());
        Thread B = new Thread() {
            @SneakyThrows
            @Override
            public void run() {
                synchronized (obj) {
                    System.out.println("====B加锁中==="+ClassLayout.parseInstance(obj).toPrintable());
                    Thread.sleep(1000);
                }
            }
        };
        B.start();
    }
}