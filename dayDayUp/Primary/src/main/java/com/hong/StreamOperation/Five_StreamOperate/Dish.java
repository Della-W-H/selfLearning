package com.hong.StreamOperation.Five_StreamOperate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

/**
 * @author wanghong
 * @date 2022/6/29
 * @apiNote
 */
@Data
public class Dish {
    private final String name;
    private final Type type;
    private final int calories;
    private final boolean vegetarian;

    public Dish(String name, boolean vegetarian, int calories, Type type){
        this.name = name;
        this.vegetarian = vegetarian;
        this.calories = calories;
        this.type = type;
    }

    public String getName(){
        return name;
    }

    public boolean isVegetarian(){
        return vegetarian;
    }

    public int getCalories(){
        return calories;
    }

    public Type getType(){
        return type;
    }

    @Override
    public String toString(){
        return name;
    }

    @Getter
    @AllArgsConstructor
    public enum Type{
        //肉类
        MEAT(1,"肉类"),
        //鱼类
        FISH(2,"鱼类"),
        //其他
        OTHER(3,"其他");

        private int code;
        private String nick;

        //todo 注意枚举类有一个默认方法values()  JDK文档中你都发现不了

        public static String getNick(int code){
            for (Type value : values()) {
                if (value.code == code)
                {
                    return value.getNick();
                }
            }
            return "找不到啊";
        }
    }

    @Getter
    public enum CaloricLevel{
        //低热量
        DIET,
        //一般
        NORMAL,
        //高热量
        FAT
    }
}

