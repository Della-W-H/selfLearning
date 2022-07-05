package com.hong.Thread.CompletableFuture;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

/**
 * @author wanghong
 * @date 2022/7/4
 * @apiNote Future对象 一个阻塞的异步通信对象
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Shop {
    private String product;
    @SuppressWarnings("all")
    public Future<Double> getPriceAsync(String product){
        CompletableFuture<Double> future = new CompletableFuture<>();
        new Thread(()->{
            try {
               double price = calculatePrice(product);
               //将 calculate 所在线程执行结果封装到 一个具有阻塞效果的 completableFuture对象中  因为阻塞也不用添加 Thread.join()方法了
               future.complete(price);
            } catch (Exception e){
                //todo 一般而言 线程之间的 报错信息是隔离的 但是可以用一个对象 进行 线程间的 数据传递 主要而言 是 Future
                //同时 亦可将 此线程执行的报错信息封装到 future对象中 从而可以传递给 主线程 此处即为 调用此方法的 所在线程
                //一般 而言线程之间是无法通信的 确实 没有任何方法直接让正在执行的两个线程间相互通信 但可以通过一个介质或者中介实现
                //此处就是future 展开来而言 对于分布式 架构 redis，数据库等这种 中间件或者隔离于程序的第三方 介质都是理论上可以
                //实现 多线程之间的通信的 事实上 也确实如此
                future.completeExceptionally(e);
            }
        }).start();
        return future;
    }

    private double calculatePrice(String product) {
        return 0;
    }

    public Future<Double> getPriceAsync1(String product){
        //这个 方法 是自待的工具工厂 方法 完全可以的代替 上面的 方法 不过 自带的 默认方法 其中用到是ForkJoinPool线程池 同样的他有一个重载的方法 可以通过第二个参数指定线程方式
        return CompletableFuture.supplyAsync(()->calculatePrice(product));
    }
}
