package com.hong.StreamOperation.Five_StreamOperate;

import java.util.*;
import java.util.function.*;
import java.util.stream.Collector;
import java.util.stream.IntStream;

/**
 * @author wanghong
 * @date 2022/6/30
 * @apiNote todo (｡･∀･)ﾉﾞ嗨 boy 你一定要看出来 collector本质上还是一种对于 @FunctionalInterface的综合运用 你一定要get it 他的所有中间操作还是通过各种 已知的函数接口 lambda形式 动态完成的啊
 */
public class PrimeNumberCollector implements
        Collector<Integer/*流中处理的数据类型 T*/,
        Map<Boolean, List<Integer>>/*累加器数据类型 A*/,
        Map<Boolean,List<Integer>>/*collect收集器结果类型 R*/> {

    /**
     * 此方法会返回一个在调用时创建的累加器函数 A 此累加器可以看成一个中间变量 本质上是一个容器 负责存储累加数据
     * @return
     */
    @Override
    public Supplier<Map<Boolean, List<Integer>>> supplier() {
        return ()->new HashMap<Boolean, List<Integer>>(){
            //todo awesome! 这种lambda初始化hashMap 你得记下来啊
            {
                put(true, new ArrayList<>());
                put(false, new ArrayList<>());
            }
        };
    }

    /**
     * 定义流程中数据的处理逻辑 累加器A即将容器取出来 本质上流程中是一种原地此处理自行累加 及处理数据类型T
     * @return
     */
    @Override
    public BiConsumer<Map<Boolean, List<Integer>>, Integer> accumulator() {
        return (Map<Boolean,List<Integer>> acc,Integer candidate)->{
            acc.get(isPrime(acc.get(true),candidate))//根据isPrime的结果，获取质数或者非质数的集合
                    .add(candidate);//将被检测数添加到相应的列表中
        };
    }

    /**
     * 让收集器并行工作(如果可能) 即在并行收集时将两部分的收集合并起来，此处即合并相应的两个map，处理手段为
     * 将第二个map中的质数和非质数列表中数据合并到第一个map集合中
     * 实际上在本次的用例中 这个收集器并没有被使用 因为该算法本身就是顺序的 即意味着 永远不会调用combiner 方法
     * 也可考虑 抛出一UnsupportedOperationException异常
     * 处理对象为R 收集器
     * @return
     */
    @Override
    public BinaryOperator<Map<Boolean, List<Integer>>> combiner() {
        return (Map<Boolean,List<Integer>> map1,Map<Boolean,List<Integer>> map2)->{
            map1.get(true).addAll(map2.get(true));
            map1.get(false).addAll(map2.get(false));
            return map1;
        };
        //throw new UnsupportedOperationException("操作不支持");
    }

    /**
     * 此方法本意是将 累加操作中的数据结果转变为其他类型即 A-->R 是容器存储完数据后对象类型的一种转变 但是此处用例的A与R类型是一致的 所以此处用恒等函数处理
     * 同时此处也是符合Function功能函数的定义的本质上是一种方法的引用即 后台核心处理逻辑同样是 Function.apply()方法的抽象即T-->R的转变
     * @return
     */
    @Override
    public Function<Map<Boolean, List<Integer>>/*A*/, Map<Boolean, List<Integer>>>/*R*/ finisher() {
        return Function.identity();
    }

    /**
     * 此为定义了 收集器的行为----尤其是关于流是否可以进行并行规约即combiner
     * UNORDERED----规约结果不受流中项目的遍历和积累顺序的影响
     * CONCURRENT----accumulator函数可以从多个线程同时调用，且改收集器可以进行并行规约流，如果收集器没有标为UNORDERED，那么仅在用于无序数据源才可以进行并行规约
     * IDENTITY_FINISH----这表明完成器返回的函数结果是一个恒等函数，可以跳过，此时累加器对象将会直接用作规约过程的最终结果，说明累加器A不加检查地转换为结果R是安全的！！！！
     * @return
     */
    @Override
    public Set<Characteristics> characteristics() {
        return Collections.unmodifiableSet(EnumSet.of(Characteristics.IDENTITY_FINISH));
    }

    /**
     * 是否为质数
     * @param candidate
     * @return
     */
    public static boolean isPrime(int candidate){
        int candidateRoot = (int)Math.sqrt((double)candidate);
        //使用取平方根 对比找质数已经是最快的方法了 或许
        return IntStream.rangeClosed(2,candidateRoot).noneMatch(i->candidate%i == 0);
    }

    /**
     *  这个集合 收集的就是 小于参选数 的所有质数
     * @param primes
     * @param candidate
     * @return
     */
    public static boolean isPrime(List<Integer> primes, int candidate){
        int candidateRoot = (int) Math.sqrt((double) candidate);
        //复习一下 这就涉及到方法引用的传递 同样的还是截取 candidate数的平方根数 进行 质数的判断
        return takeWhile(primes,o -> o<=candidateRoot).stream().noneMatch(p->candidate % p == 0);
    }

    private static <A> List<A> takeWhile(List<A> list, Predicate<A> p){
        int i = 0;
        for (A a : list) {
            if(!p.test(a)){
                return list.subList(0,i);
            }
            i++;
        }
        return list;
    }
}
