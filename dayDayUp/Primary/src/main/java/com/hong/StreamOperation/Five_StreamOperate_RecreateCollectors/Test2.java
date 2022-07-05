package com.hong.StreamOperation.Five_StreamOperate_RecreateCollectors;

import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

/**
 * @author wanghong
 * @date 2022/6/30
 * @apiNote
 */
public class Test2 <A>{
    public static void main(String[] args) {
        long fastest = Long.MAX_VALUE;
        Map<Boolean, List<Integer>> result = null;
        for (int i = 0; i < 10; i++) {
            long start = System.nanoTime();
            result = partitionPrimeWithCustomCollector(100);
            long duration = (System.nanoTime() - start) / 100;
            if (duration < fastest) fastest = duration;
        }
        System.out.println("Fastest execution done in " + fastest + " msecs");
        System.out.println("result :" + result);
    }

    private static Map<Boolean,List<Integer>> partitionPrimeWithCustomCollector(int n){
        return IntStream.rangeClosed(2,n).boxed().collect(new PrimeNumberCollector());
    }
}
