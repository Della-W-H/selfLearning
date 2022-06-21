package com.hong.Thread.ConcurrentHashMapTrouble;

import lombok.extern.slf4j.Slf4j;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

/**
 * @author wanghong
 * @date 2022/6/14
 * @apiNote 并发工具类的线程安全问题
 */
@Slf4j
public class Example1 {
    public static void main(String[] args) throws InterruptedException {
        System.out.println(new Example1().wrong());
    }
    //线程个数
    private static int THREAD_COUNT = 10;
    //总元素数量
    private static int ITEM_COUNT = 1000;

    //帮助方法，用来获得一个指定元素数量模拟数据的ConcurrentHashMap

    private ConcurrentHashMap<String, Long> getData(int count) {
        return LongStream.rangeClosed(1, count)
                .boxed()
                .collect(Collectors.toConcurrentMap(i -> UUID.randomUUID().toString(), Function.identity(),
                        (o1, o2) -> o1, ConcurrentHashMap::new));
    }


    public String wrong() throws InterruptedException {

        //todo 即使这个concurrentHashMap 是用volatile修饰 保证了 可见性 但是 这仍然无济于事 因为 putAll等系列操作 不具有原子性 volatile只能保证 数据的瞬时正确性 但是操作流程是随时会改变 数据的
        ConcurrentHashMap<String, Long> concurrentHashMap = getData(ITEM_COUNT - 100);
        //初始900个元素
        log.info("init size:{}", concurrentHashMap.size());
        ForkJoinPool forkJoinPool = new ForkJoinPool(THREAD_COUNT);
        //使用线程池并发处理逻辑
        forkJoinPool.execute(() -> IntStream.rangeClosed(1, 10).parallel().forEach(i -> {
            //查询还需要补充多少个元素
            synchronized (concurrentHashMap) {
                //加锁 可以解决 那加锁后 用线程池的意义在哪里呢？
                int gap = ITEM_COUNT - concurrentHashMap.size();
                log.info("gap size:{}", gap);
                //补充元素
                concurrentHashMap.putAll(getData(gap));
                //todo 相较于 普通的hashMap并发类仍然具有优势
            }
        }));
        //等待所有任务完成
        forkJoinPool.shutdown();
        forkJoinPool.awaitTermination(1, TimeUnit.HOURS);
        //最后元素个数会是1000吗？
        log.info("finish size:{}", concurrentHashMap.size());
        return "OK";
    }
}
