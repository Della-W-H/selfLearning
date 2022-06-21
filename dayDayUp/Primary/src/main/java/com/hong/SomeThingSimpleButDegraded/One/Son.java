package com.hong.SomeThingSimpleButDegraded.One;

/**
 * @author wanghong
 * @date 2022/6/10
 * @apiNote   验证多态的形式下 子类 继承父类方法 执行了 父类的方法 父类方法中调用了一个子类继承的方法时 又会走子类的方法
 *   todo 这就是模板方法的精髓
 */
public class Son {

    private static class InnerSon extends Supper{
        @Override
        void doRun() {
            System.out.println("we made it!!!");
        }
    }

    private final InnerSon is = new InnerSon();

    public void doSeeReal(){
        is.doSee();
    }

    public static void main(String[] args) {
        Son son = new Son();
        son.doSeeReal();
    }
}
