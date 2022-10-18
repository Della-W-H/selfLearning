package com.hong.Thread.ThreadPoolAssociation;

import jodd.util.concurrent.ThreadFactoryBuilder;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

/**
 * @author wanghong
 * @date 2022/10/18
 * @apiNote
 *  至此，我们可以总结出线程池默认的工作行为：
 *
 * 不会初始化 corePoolSize 个线程，有任务来了才创建工作线程；
 *
 * 当核心线程满了之后不会立即扩容线程池，而是把任务堆积到工作队列中；
 *
 * 当工作队列满了后扩容线程池，一直到线程个数达到 maximumPoolSize 为止；
 *
 * 如果队列已满且达到了最大线程后还有任务进来，按照拒绝策略处理；
 *
 * 当线程数大于核心线程数时，线程等待 keepAliveTime 后还是没有任务需要处理的话，收缩线程到核心线程数。
 */
@Slf4j
public class ThreadPoolFailSubmit{

    public static int right() throws InterruptedException {

        //使用一个计数器跟踪完成的任务数
        AtomicInteger atomicInteger = new AtomicInteger();

        // 创建一个具有2个核心线程、5个最大线程，
        // 使用容量为10的ArrayBlockingQueue阻塞队列作为工作队列的线程池，使用默认的AbortPolicy拒绝策略
        ThreadPoolExecutor threadPool = new ThreadPoolExecutor(
                2, 5,
                5, TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(10),
                new ThreadFactoryBuilder().setNameFormat("demo-threadpool-%d").get(),
                new ThreadPoolExecutor.AbortPolicy());

        OOMProblem.printStats(threadPool);

        //每隔1秒提交一次，一共提交20次任务
        IntStream.rangeClosed(1, 20).forEach(i -> {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            int id = atomicInteger.incrementAndGet();
            try {
                threadPool.submit(() -> {
                    log.info("{} started", id);
                    //每个任务耗时10秒
                    try {
                        TimeUnit.SECONDS.sleep(10);
                    } catch (InterruptedException e) {
                    }
                    log.info("{} finished", id);
                });
            } catch (Exception ex) {
                //提交出现异常的话，打印出错信息并为计数器减一
                log.error("error submitting task {}", id, ex);
                atomicInteger.decrementAndGet();
            }
        });
        TimeUnit.SECONDS.sleep(60);
        return atomicInteger.intValue();
    }

    public static void main(String[] args) throws InterruptedException{
        right();
    }
}
