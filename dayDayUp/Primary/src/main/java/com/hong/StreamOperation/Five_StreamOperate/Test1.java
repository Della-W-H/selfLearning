package com.hong.StreamOperation.Five_StreamOperate;


import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.*;


/**
 * @author wanghong
 * @date 2022/6/29
 * @apiNote
 */
public class Test1 {
    public static void main(String[] args) {
        List<Dish> dishes = Arrays.asList(
                new Dish("pork",false,800,Dish.Type.MEAT),
                new Dish("beef",false,700, Dish.Type.MEAT),
                new Dish("chicken",false,400,Dish.Type.MEAT),
                new Dish("french fries",true,530, Dish.Type.OTHER),
                new Dish("rice",true,350, Dish.Type.OTHER),
                new Dish("season fruit",true,120, Dish.Type.OTHER),
                new Dish("pizza",true,550, Dish.Type.OTHER),
                new Dish("prawns",false,300, Dish.Type.FISH),
                new Dish("salmon",false,450, Dish.Type.FISH)
        );



        //dishes.stream().filter(Dish::isVegetarian).findAny().isPresent();

        //System.out.println(dishes.stream().collect(partitioningBy(Dish::isVegetarian)));

        //自定义Collector
        System.out.println(dishes.stream().collect(new ToListCollector<>()));

        //stream多级分类
        Map<Dish.CaloricLevel, List<Dish>> collect1 = dishes.stream().collect(
                groupingBy(Test1::getCaloricLevel)
        );
        System.out.println(collect1);

        System.out.println(dishes.stream().collect(groupingBy(Dish::getType, groupingBy(Test1::getCaloricLevel))));

        //包装类型转换 找到每种类型中热量最高的食物
        System.out.println(dishes.stream().collect(groupingBy(Dish::getType, collectingAndThen(maxBy(Comparator.comparingInt(Dish::getCalories)), Optional::get))));
        System.out.println(dishes.stream().collect(Collectors.toMap(Dish::getType, Function.identity(), BinaryOperator.maxBy(Comparator.comparingInt(Dish::getCalories)))));

        System.out.println(dishes.stream().collect(groupingBy(Dish::getType, mapping(Test1::getCaloricLevel, toSet()))));

        int a = 1_000_000;
    }

    private static Dish.CaloricLevel getCaloricLevel(@NotNull Dish dish) {
        if (dish.getCalories() <= 400) return Dish.CaloricLevel.DIET;
        else if (dish.getCalories() <= 700) return Dish.CaloricLevel.NORMAL;
        else return Dish.CaloricLevel.FAT;
    }


}
