package org.itstack.demo.design;

/**
 * 枚举类的单例模式 不能被继承
 *
 * 这种写法在功能上与共有域⽅法相近，但是它更简洁，⽆偿地提供了串⾏化机制，绝对防⽌对此实例
 * 化，即使是在⾯对复杂的串⾏化或者反射攻击的时候。虽然这中⽅法还没有⼴泛采⽤，但是单元素的枚
 * 举类型已经成为实现Singleton的最佳⽅法
 *
 */
public enum Singleton_07 {

    //
    INSTANCE;

    public void test(){
        System.out.println("hi~");
    }

}

