package com.hong.StreamOperation.Five_StreamOperate;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

import static java.util.stream.Collector.Characteristics.CONCURRENT;
import static java.util.stream.Collector.Characteristics.IDENTITY_FINISH;

/**
 * @author wanghong
 * @date 2022/6/29
 * @apiNote
 */
public class ToListCollector<T> implements Collector<T, List<T>,List<T>> {
    /**
     * 创作集合操作的起始点
     * @return
     */
    @Override
    public Supplier<List<T>> supplier() {
        return ArrayList::new;
    }

    /**
     * 累积遍历过的项目，原位修改累加器
     * @return
     */
    @Override
    public BiConsumer<List<T>, T> accumulator() {
        return List::add;
    }

    /**
     * 修改第一个累加器，将其与第二个累加器的内容合并 返回修改后的第一个累加器
     * @return
     */
    @Override
    public BinaryOperator<List<T>> combiner() {
        return (list1,list2)->{
            list1.addAll(list2);
            return list1;
        };
    }

    /**
     * 恒等函数
     * @return
     */
    @Override
    public Function<List<T>, List<T>> finisher() {
        return Function.identity();
    }

    /**
     * 位收集器添加IDENTITY_FINISH和CONCURRENT标识
     * @return
     */
    @Override
    public Set<Characteristics> characteristics() {
        return Collections.unmodifiableSet(EnumSet.of(
                IDENTITY_FINISH,CONCURRENT
        ));
    }
}
