package com.hong.designModule.FactoryTemplatePattern;

import java.util.HashMap;
import java.util.Map;

/**
 * @author wanghong
 * @date 2022/7/1
 * @apiNote
 */
public class ClothesFactory {
    final static Map<String,TriFunction<String,String,Integer,Clothes>> map = new HashMap<>();
    static {
        map.put("鞋子", Shoes::new);
    }

    public static Clothes createClothes(String name,String color,Integer price){
        TriFunction<String, String, Integer, Clothes> triFunction = map.get(name);
        if (triFunction != null)return triFunction.apply(name,color,price);
        throw new IllegalArgumentException("No such product "+name);
    }
}
