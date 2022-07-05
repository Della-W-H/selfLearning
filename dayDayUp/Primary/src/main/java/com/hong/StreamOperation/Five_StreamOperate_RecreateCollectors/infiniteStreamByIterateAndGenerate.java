package com.hong.StreamOperation.Five_StreamOperate_RecreateCollectors;

import java.util.function.IntSupplier;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * @author wanghong
 * @date 2022/6/29
 * @apiNote
 */
public class infiniteStreamByIterateAndGenerate {
    public static void main(String[] args) {
        //此处即为无状态的数据源
        Stream.iterate(0,n->n+2).limit(10).forEach(System.out::println);

        //内部类
        //todo 此处用内部类代替 lambda表达式是因为 lambda表示无法 像此处使用 previous和current等类数据 即有状态的形式生产无限流的数据源
        IntSupplier intSupplier = new IntSupplier() {
            private int previous = 0;
            private int current = 1;

            @Override
            public int getAsInt() {
                int oldPrevious = this.previous;
                int nextValue = this.previous + this.current;
                this.previous = this.current;
                this.current = nextValue;
                return oldPrevious;
            }
        };
        IntStream.generate(intSupplier).limit(10).forEach(System.out::print);
    }
}
