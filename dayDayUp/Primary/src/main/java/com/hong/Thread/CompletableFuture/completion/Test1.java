package com.hong.Thread.CompletableFuture.completion;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author wanghong
 * @date 2022/7/4
 * @apiNote
 */
public class Test1 {

    public static void main(String[] args) throws InterruptedException {
        List<Goods> goods = Arrays.asList(new Goods("BestPrice"),
                new Goods("LetsSaveBig"),
                new Goods("MyFavoriteShop"),
                new Goods("BuyItAll"));

        ExecutorService pool = Executors.newFixedThreadPool(Math.min(goods.size(), 100), r -> {
            Thread thread = new Thread(r);
            //主线程main结束 守护线程亦会结束 守护线程和其他线程没有执行效果的差距，只是它多了标记
            thread.setDaemon(true);
            return thread;
        });
        System.out.println(findPricesStream1("iphone",goods, pool));
        System.out.println(findPricesStream2("iphone",goods, pool));


        long start = System.currentTimeMillis();
        CompletableFuture[] futures = findPricesStream3("iphone", goods, pool).map(f -> f.thenAccept(System.out::println))
                .toArray(CompletableFuture[]::new);
        //这边可以改成anOf即只要有一个结果被计算出来就输出
        CompletableFuture.allOf(futures).join();
        System.out.println("耗时：" + (System.currentTimeMillis() - start) + " 毫秒");
    }

    //版一
    public static List<String> findPricesStream1(String iphone, List<Goods> goods, ExecutorService pool) {
        long start = System.currentTimeMillis();

        //todo 整体而言 stream的单个流程是同步执行的嘛？ 应该是的
        List<CompletableFuture<String>> collect = goods.stream()
                //以异步方式取得每个goods中指定商品的原始价格
                .map(good -> CompletableFuture.supplyAsync(() -> good.getPrice(iphone), pool))
                //Quote对象存在时，对其返回的值进行转化
                .map(future -> future.thenApply(Quote::parse))
                //使用另一个异步任务构造期望的Future申请折扣
                .map(future -> future.thenCompose(quote -> CompletableFuture.supplyAsync(
                        () -> Discount.applyDiscount(quote), pool
                ))).collect(Collectors.toList());
        //todo 同样的 CompletableFuture的join和get方法是阻塞的
        //等待 流中的所有Future执行完毕，并提取各自的返回值
        List<String> list = collect.stream().map(CompletableFuture::join).collect(Collectors.toList());

        System.out.println("耗时：" + (System.currentTimeMillis() - start) + " 毫秒");
        return list;
    }
    //版二
    public static List<String> findPricesStream2(String iphone, List<Goods> goods, ExecutorService pool) {
        long start = System.currentTimeMillis();
        List<String> collect = goods.stream()
                .map(good -> good.getPrice(iphone))
                .map(Quote::parse)
                .map(Discount::applyDiscount)
                .collect(Collectors.toList());
        System.out.println("耗时：" + (System.currentTimeMillis() - start) + " 毫秒");
        return collect;
    }

    //版三
    public static Stream<CompletableFuture<String>> findPricesStream3(String iphone, List<Goods> goods, ExecutorService pool) {
        return goods.stream()
                .map(goods1 -> CompletableFuture.supplyAsync(
                        ()->goods1.getPrice(iphone),pool
                ))
                .map(future -> future.thenApply(Quote::parse))
                .map(future -> future.thenCompose(quote -> CompletableFuture.supplyAsync(
                        ()->Discount.applyDiscount(quote),pool
                )));
    }
}
