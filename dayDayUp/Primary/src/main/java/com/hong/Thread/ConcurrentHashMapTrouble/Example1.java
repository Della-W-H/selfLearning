package com.hong.Thread.ConcurrentHashMapTrouble;

import cn.hutool.core.date.StopWatch;
import cn.hutool.core.lang.Assert;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.LongAdder;
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

    //线程个数
    private static int THREAD_COUNT = 10;
    //总元素数量
    private static int ITEM_COUNT = 1000;

    //帮助方法，用来获得一个指定元素数量模拟数据的ConcurrentHashMap

    private static ConcurrentHashMap<String, Long> getData(int count) {
        return LongStream.rangeClosed(1, count)
                //todo boxed()即 将基本数据类型 转为 包装类 因为有些方法参数是Object对象 或者 泛型 即即无法接收 基本数据类型
                .boxed()
                .collect(Collectors.toConcurrentMap(i-> UUID.randomUUID().toString(), Function.identity(),
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
        System.out.println(concurrentHashMap);
        return "OK";
    }

    //其二

    //循环次数
    private static int LOOP_COUNT = 10000000;
    //线程数量
    private static int THREAD_COUNT1 = 10;
    //元素数量
    private static int ITEM_COUNT1 = 10;
    private Map<String, Long> normaluse() throws InterruptedException {
        ConcurrentHashMap<String, Long> freqs = new ConcurrentHashMap<>(ITEM_COUNT1);
        ForkJoinPool forkJoinPool = new ForkJoinPool(THREAD_COUNT1);
        forkJoinPool.execute(() -> IntStream.rangeClosed(1, LOOP_COUNT).parallel().forEach(i -> {
                    //获得一个随机的Key
                    String key = "item" + ThreadLocalRandom.current().nextInt(ITEM_COUNT1);
                    synchronized (freqs) {
                        if (freqs.containsKey(key)) {
                            //Key存在则+1
                            freqs.put(key, freqs.get(key) + 1);
                        } else {
                            //Key不存在则初始化为1
                            freqs.put(key, 1L);
                        }
                    }
                }
        ));
        forkJoinPool.shutdown();
        forkJoinPool.awaitTermination(1, TimeUnit.HOURS);
        return freqs;
    }

    //其二优化
    /**
     *      使用 ConcurrentHashMap 的原子性方法 computeIfAbsent 来做复合逻辑操作，
     *      判断 Key 是否存在 Value，如果不存在则把 Lambda 表达式运行后的结果放入 Map 作为 Value，
     *      也就是新创建一个 LongAdder 对象，最后返回 Value。
     *      由于 computeIfAbsent 方法返回的 Value 是 LongAdder，是一个线程安全的累加器，
     *      因此可以直接调用其 increment 方法进行累加
     */

    private Map<String, Long> gooduse() throws InterruptedException {
        ConcurrentHashMap<String,LongAdder> freqs = new ConcurrentHashMap<>(ITEM_COUNT1);
        ForkJoinPool forkJoinPool = new ForkJoinPool(THREAD_COUNT1);
        forkJoinPool.execute(() -> IntStream.rangeClosed(1, LOOP_COUNT).parallel().forEach(i -> {
                    String key = "item" + ThreadLocalRandom.current().nextInt(ITEM_COUNT1);
                    //利用computeIfAbsent()方法来实例化LongAdder，然后利用LongAdder来进行线程安全计数
                    freqs.computeIfAbsent(key, k -> new LongAdder()).increment();
                }
        ));
        forkJoinPool.shutdown();
        forkJoinPool.awaitTermination(1, TimeUnit.HOURS);
        //因为我们的Value是LongAdder而不是Long，所以需要做一次转换才能返回
        return freqs.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        e -> e.getValue().longValue())
                );
    }


    /**
     * 这段测试代码并无特殊之处，使用 StopWatch 来测试两段代码的性能，
     * 最后跟了一个断言判断 Map 中元素的个数以及所有 Value 的和，是否符合预期来校验代码的正确性。
     * @return
     * @throws InterruptedException
     */
    public String good() throws InterruptedException {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start("normaluse");
        Map<String, Long> normaluse = normaluse();
        stopWatch.stop();
        //校验元素数量
        Assert.isTrue(normaluse.size() == ITEM_COUNT1, "normaluse size error");
        //校验累计总数
        Assert.isTrue(normaluse.entrySet().stream()
                        .mapToLong(Map.Entry::getValue).reduce(0, Long::sum) == LOOP_COUNT
                , "normaluse count error");
        stopWatch.start("gooduse");
        Map<String, Long> gooduse = gooduse();
        stopWatch.stop();
        Assert.isTrue(gooduse.size() == ITEM_COUNT1, "gooduse size error");
        Assert.isTrue(gooduse.entrySet().stream()
                        .mapToLong(item -> item.getValue())
                        .reduce(0, Long::sum) == LOOP_COUNT
                , "gooduse count error");
        log.info(stopWatch.prettyPrint());
        return "OK";
    }

    public static void main(String[] args) throws InterruptedException {
        //System.out.println(new Example1().wrong());
        new Example1().good();
    }
}
