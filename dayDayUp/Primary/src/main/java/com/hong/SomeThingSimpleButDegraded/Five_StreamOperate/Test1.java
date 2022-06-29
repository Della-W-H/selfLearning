package com.hong.SomeThingSimpleButDegraded.Five_StreamOperate;


import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.partitioningBy;

/**
 * @author wanghong
 * @date 2022/6/29
 * @apiNote
 */
public class Test1 {
    public static void main(String[] args) {
        List<Dish> dishes = new ArrayList<>();
        dishes.add(new Dish("鱼香肉丝", "肉菜"));
        dishes.add(new Dish("土豆汤", "素菜"));
        dishes.add(new Dish("梅菜扣肉", "肉菜"));
        dishes.add(new Dish("番茄鸡蛋", "素菜"));
        dishes.add(new Dish("宫保鸡丁", "肉菜"));


        System.out.println(dishes.stream().collect(partitioningBy(Dish::isVegetarian)));
        System.out.println(dishes.stream().collect(new ToListCollector<>()));
    }


}
