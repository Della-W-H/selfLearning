package com.hong.Thread.CompletableFuture.completion;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.scheduling.annotation.Async;

import java.util.Random;

/**
 * @author wanghong
 * @date 2022/7/4
 * @apiNote
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Goods {
    //商品
    private String product;
    //价格
    private int price;
    //店铺名
    private String name;
    private static final Random RANDOM = new Random();

    public Goods(String name){
        this.name = name;
    }
    @Async
    public String getPrice(String product){
        double price = calculatePrice(product);
        Discount.Code code = Discount.Code.values()[RANDOM.nextInt(Discount.Code.values().length)];
        return String.format("店名%s:原价%.2f:打折%s", name, price, code);
    }

    private double calculatePrice(String product){
        delay();
        return RANDOM.nextDouble()*product.charAt(0)+product.charAt(1);
    }

    public static void delay() {
        try {
            Thread.sleep(500+ RANDOM.nextInt(2000));
        } catch (InterruptedException e){
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        String str = "myGoods";
        Goods goods = new Goods("BuyItAll");
        System.out.println(goods.getPrice(str));
    }
}
