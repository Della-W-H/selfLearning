package com.hong.designModule.FactoryTemplatePattern;

/**
 * @author wanghong
 * @date 2022/7/1
 * @apiNote
 */
public class Test2 {
    public static void main(String[] args) {
        System.out.println(ClothesFactory.createClothes("鞋子", "blue", 10));
    }
}
