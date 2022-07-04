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
               future.complete(price);
            } catch (Exception e){
                //一般而言 线程之间的 报错信息是隔离的 但是可以用一个对象 进行 线程间的 数据传递 主要而言 是 Future对象是个隔离的对象
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
