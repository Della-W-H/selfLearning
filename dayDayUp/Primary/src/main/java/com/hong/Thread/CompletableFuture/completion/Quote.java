package com.hong.Thread.CompletableFuture.completion;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author wanghong
 * @date 2022/7/4
 * @apiNote
 */
@Data
@AllArgsConstructor
public class Quote {
    private final String shopName;
    private final double price;
    private final Discount.Code discountCode;

    public static Quote parse(String s){
        String[] split = s.split(":");
        String shopName = split[0].substring(2);
        double price = Double.parseDouble(split[1].substring(2));
        Discount.Code discountCode = Discount.Code.valueOf(split[2].substring(2));
        return new Quote(shopName,price,discountCode);
    }

}
