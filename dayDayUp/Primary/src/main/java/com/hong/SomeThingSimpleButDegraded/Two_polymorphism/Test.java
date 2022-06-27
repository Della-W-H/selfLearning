package com.hong.SomeThingSimpleButDegraded.Two_polymorphism;

/**
 * @author wanghong
 * @date 2022/6/22
 * @apiNote 多态
 */
public class Test {
    public static void main(String[] args) {

        Father father = new SmallBrother();
        System.out.println(father.speak());

        Mother mother = new BigBrother();
        System.out.println(mother.speak());
    }
}
