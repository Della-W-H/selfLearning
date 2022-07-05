package com.hong.designModule.FactoryTemplatePattern.RedefinitionFunctionInteface;
import java.util.HashMap;
import java.util.Map;

public class ClothesFactory {

    final static Map<String, TriFunction<String,String,Integer,Clothes>> map = new HashMap<>();
    static {
        map.put("鞋子", Shoes::new);
    }

    public static Clothes createClothes(String name,String color, Integer price){
        TriFunction<String, String, Integer, Clothes> function = map.get(name);
        if (function != null)return function.apply(name,color,price);
        throw new IllegalArgumentException("No such product "+name);
    }
}
