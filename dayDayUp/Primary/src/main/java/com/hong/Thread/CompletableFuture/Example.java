package com.hong.Thread.CompletableFuture;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

/**
 * @author wanghong
 * @date 2022/7/4
 * @apiNote
 */
public class Example {

    private static ExecutorService pool = null;
    public static void main(String[] args) {

        String product = "iphone";
        List<Shop> shops = Arrays.asList(new Shop("BestPrice"),
                new Shop("LetsSaveBig"),
                new Shop("MyFavoriteShop"),
                new Shop("BuyItAll"));

        //版本1
        List<CompletableFuture<String>> collect = shops.stream().map(shop -> CompletableFuture.supplyAsync(
                () -> String.format("%s price is %.2f", shop.getProduct(), shop.getPriceAsync1(product)
                ))).collect(Collectors.toList());

        //版本3
        pool = Executors.newFixedThreadPool(Math.min(shops.size(), 100), r -> {
            Thread thread = new Thread(r);
            //主线程main结束 守护线程亦会结束 守护线程和其他线程没有执行效果的差距，只是它多了标记
            thread.setDaemon(true);
            return thread;
        });

        System.out.println(collect);
    }

    //版本2
    public static List<String> findPrices2(String product,List<Shop> shops){
        List<CompletableFuture<String>> collect = shops.stream().map(shop -> CompletableFuture.supplyAsync(() -> shop.getProduct() + " price is " + shop.getPriceAsync1(product))).collect(Collectors.toList());
        return collect.stream().map(CompletableFuture::join).collect(Collectors.toList());
    }

    //版本3 指定线程创建方式
    public static List<String> findPrices3(String product,List<Shop> shops){
        List<CompletableFuture<String>> collect = shops.stream().map(shop -> CompletableFuture.supplyAsync(() -> shop.getProduct() + " price is " + shop.getPriceAsync1(product),pool)).collect(Collectors.toList());
        return collect.stream().map(CompletableFuture::join).collect(Collectors.toList());
    }
}
