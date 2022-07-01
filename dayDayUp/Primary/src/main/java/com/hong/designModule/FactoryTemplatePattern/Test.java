package com.hong.designModule.FactoryTemplatePattern;

/**
 * @author wanghong
 * @date 2022/7/1
 * @apiNote
 */
public class Test {
    public static void main(String[] args) {
        System.out.println(ProductFactory.createProduct("loan"));
        System.out.println(ProductFactory.createProduct("apple"));
    }
}
