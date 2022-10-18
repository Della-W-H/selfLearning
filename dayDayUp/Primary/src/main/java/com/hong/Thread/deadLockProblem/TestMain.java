package com.hong.Thread.deadLockProblem;

import lombok.Data;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @author wanghong
 * @date 2022/10/17
 * @apiNote
 */
@Slf4j
public class TestMain{

    static Item item1;
    static Item item2;
    static Item item3;

    static {
        item3=new Item("clothes");
        item2=new Item("computer");
        item1=new Item("phone");
    }
    private static Map<String,Item> items = new HashMap<String,Item>(){
        {
            put("item1",item1);
            put("item2",item2);
            put("item3",item3);
        }
    };

    private List<Item> createCart(){

        return IntStream.rangeClosed(1,3).mapToObj(a->"item"+(ThreadLocalRandom.current().nextInt(items.size())+1)).
        map(name->items.get(name)).collect(Collectors.toList());
    }

    /**
     * 下单代码如下：先声明一个 List 来保存所有获得的锁，然后遍历购物车中的商品依次尝试获得商品的锁，
     * 最长等待 10 秒，获得全部锁之后再扣减库存；如果有无法获得锁的情况则解锁之前获得的所有锁，返回 false 下单失败。
     * @param order
     * @return
     */
    private boolean createOrder(List<Item> order) {
        //存放所有获得的锁
        List<ReentrantLock> locks = new ArrayList<>();
        for (Item item : order) {
            try {
                //获得锁10秒超时
                if (item.lock.tryLock(10, TimeUnit.SECONDS)) {
                    locks.add(item.lock);
                } else {
                    locks.forEach(ReentrantLock::unlock);
                    return false;
                }
            } catch (InterruptedException e) {
            }
        }
        //锁全部拿到之后执行扣减库存业务逻辑
        try {
            order.forEach(item -> item.remaining--);
        } finally {
            locks.forEach(ReentrantLock::unlock);
        }
        return true;
    }

    public long wrong() {
        long begin = System.currentTimeMillis();
        //并发进行100次下单操作，统计成功次数
        long success = IntStream.rangeClosed(1, 100).parallel()
                .mapToObj(i -> {
                    List<Item> cart = createCart();
                    return createOrder(cart);
                })
                .filter(result -> result)
                .count();
        log.info("success:{} totalRemaining:{} took:{}ms items:{}",
                success,
                items.entrySet().stream().map(item -> item.getValue().remaining).reduce(0, Integer::sum),
                System.currentTimeMillis() - begin, items);
        return success;
    }

    public long right() {
        long begin = System.currentTimeMillis();
        long success = IntStream.rangeClosed(1, 100).parallel()
                .mapToObj(i -> {
                    List<Item> cart = createCart().stream()
                            .sorted(Comparator.comparing(Item::getName))
                            .collect(Collectors.toList());
                    return createOrder(cart);
                })
                .filter(result -> result)
                .count();
        log.info("success:{} totalRemaining:{} took:{}ms items:{}",
                success,
                items.entrySet().stream().map(item -> item.getValue().remaining).reduce(0, Integer::sum),
                System.currentTimeMillis() - begin, items);
        return success;
    }

    /**
     * 回忆一下购物车添加商品的逻辑，随机添加了三种商品，假设一个购物车中的商品是 item1 和 item2，
     * 另一个购物车中的商品是 item2 和 item1，一个线程先获取到了 item1 的锁，同时另一个线程获取到了 item2 的锁，
     * 然后两个线程接下来要分别获取 item2 和 item1 的锁，这个时候锁已经被对方获取了，只能相互等待一直到 10 秒超时。
     *
     * 其实，避免死锁的方案很简单，**为购物车中的商品排一下序，
     * 让所有的线程一定是先获取 item1 的锁然后获取 item2 的锁，就不会有问题了
     * @param args
     */
    public static void main(String[] args){
        TestMain testMain=new TestMain();
        //System.out.println(testMain.createCart());

        //testMain.wrong();
        testMain.right();

    }
}

//@Data
@ToString
class Item{
    //商品名
    final String name;
    //库存产品
    int remaining = 1000;

    //@ToString.Exclude
    transient ReentrantLock lock = new ReentrantLock();

    public Item(String name){
        this.name = name;
    }

    public String getName(){
        return name;
    }

    //public item(){} 属性有final 修饰即不能提供无参构造方法了
}
