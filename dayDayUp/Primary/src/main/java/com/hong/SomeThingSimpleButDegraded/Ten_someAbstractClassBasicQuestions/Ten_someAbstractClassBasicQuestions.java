package com.hong.SomeThingSimpleButDegraded.Ten_someAbstractClassBasicQuestions;

/**
 * @author wanghong
 * @date 2022/7/14
 * @apiNote  抽象类可以有构造方法但是 不能通过 实例化new的方式直接用 可以通过 子类实现类继承的方式进行使用
 */
public class Ten_someAbstractClassBasicQuestions extends abstractA{
    public Ten_someAbstractClassBasicQuestions(String a) {
        super(a);
    }

    public static void main(String[] args) {

        //new abstractA("della");

        Ten_someAbstractClassBasicQuestions della = new Ten_someAbstractClassBasicQuestions("della");
        System.out.println(della);
    }
}

abstract class abstractA{
    private String name;
    protected abstractA(String a){this.name = a;}
}