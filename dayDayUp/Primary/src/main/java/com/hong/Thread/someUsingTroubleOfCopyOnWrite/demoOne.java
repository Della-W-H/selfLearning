package com.hong.Thread.someUsingTroubleOfCopyOnWrite;

import cn.hutool.core.date.StopWatch;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @author wanghong
 * @date 2022/10/14
 * @apiNote
 * 一般来说，针对通用场景的通用解决方案，在所有场景下性能都还可以，属于“万金油”；
 * 而针对特殊场景的特殊实现，会有比通用解决方案更高的性能，但一定要在它针对的场景下使用，否则可能会产生性能问题甚至是 Bug。
 *
 * 之前在排查一个生产性能问题时，我们发现一段简单的非数据库操作的业务逻辑，
 * 消耗了超出预期的时间，在修改数据时操作本地缓存比回写数据库慢许多。
 * 查看代码发现，开发同学使用了 CopyOnWriteArrayList 来缓存大量的数据，而数据变化又比较频繁。
 *
 * CopyOnWrite 是一个时髦的技术，不管是 Linux 还是 Redis 都会用到。
 * 在 Java 中，CopyOnWriteArrayList 虽然是一个线程安全的 ArrayList，
 * 但因为其实现方式是，每次修改数据时都会复制一份数据出来，所以有明显的适用场景，即读多写少或者说希望无锁读的场景。
 *
 * 如果我们要使用 CopyOnWriteArrayList，那一定是因为场景需要而不是因为足够酷炫。
 * 如果读写比例均衡或者有大量写操作的话，使用 CopyOnWriteArrayList 的性能会非常糟糕。
 */
@Slf4j
public class demoOne{

    //测试并发写的性能
    public Map testWrite() {
        List<Integer> copyOnWriteArrayList = new CopyOnWriteArrayList<>();
        List<Integer> synchronizedList = Collections.synchronizedList(new ArrayList<>());
        StopWatch stopWatch = new StopWatch();
        int loopCount = 100000;
        stopWatch.start("Write:copyOnWriteArrayList");
        //循环100000次并发往CopyOnWriteArrayList写入随机元素
        IntStream.rangeClosed(1, loopCount).parallel().forEach(a-> copyOnWriteArrayList.add(ThreadLocalRandom.current().nextInt(loopCount)));
        stopWatch.stop();
        stopWatch.start("Write:synchronizedList");
        //循环100000次并发往加锁的ArrayList写入随机元素
        IntStream.rangeClosed(1, loopCount).parallel().forEach(a-> synchronizedList.add(ThreadLocalRandom.current().nextInt(loopCount)));
        stopWatch.stop();
        log.info(stopWatch.prettyPrint());
        Map result = new HashMap();
        result.put("copyOnWriteArrayList", copyOnWriteArrayList.size());
        result.put("synchronizedList", synchronizedList.size());
        return result;
    }
    //帮助方法用来填充List
    private void addAll(List<Integer> list) {
        list.addAll(IntStream.rangeClosed(1, 1000000).boxed().collect(Collectors.toList()));
    }
    //测试并发读的性能
    public Map testRead() {
        //创建两个测试对象
        List<Integer> copyOnWriteArrayList = new CopyOnWriteArrayList<>();
        List<Integer> synchronizedList = Collections.synchronizedList(new ArrayList<>());
        //填充数据
        addAll(copyOnWriteArrayList);
        addAll(synchronizedList);
        StopWatch stopWatch = new StopWatch();
        int loopCount = 1000000;
        int count = copyOnWriteArrayList.size();
        stopWatch.start("Read:copyOnWriteArrayList");
        //循环1000000次并发从CopyOnWriteArrayList随机查询元素
        IntStream.rangeClosed(1, loopCount).parallel().forEach(a -> copyOnWriteArrayList.get(ThreadLocalRandom.current().nextInt(count)));
        stopWatch.stop();
        stopWatch.start("Read:synchronizedList");
        //循环1000000次并发从加锁的ArrayList随机查询元素
        IntStream.range(0, loopCount).parallel().forEach(a -> synchronizedList.get(ThreadLocalRandom.current().nextInt(count)));
        stopWatch.stop();
        log.info(stopWatch.prettyPrint());
        Map result = new HashMap();
        result.put("copyOnWriteArrayList", copyOnWriteArrayList.size());
        result.put("synchronizedList", synchronizedList.size());
        return result;
    }

    public static void main(String[] args){

        demoOne demoOne=new demoOne();
        demoOne.testRead();
        demoOne.testWrite();
    }
}
