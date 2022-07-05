package com.hong.SomeThingSimpleButDegraded.Three_FunctionProgramm.example3_lambdaProblem;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

public class Lambda4 {

    // 静态变量 读写权限都能有
    static int outerStaticNum;
    // 成员变量
    int outerNum;

    void testScopes() {
        System.out.println("成员变量："+outerNum);
        IConverter<Integer, String> stringConverter1 = (from) -> {
            // 对成员变量赋值
            outerNum = 23;
            System.out.println("lambda中对成员变量赋值："+outerNum);
            return String.valueOf(from);
        };
        stringConverter1.convert(1);
        System.out.println("方法中对成员变量进行赋值操作后成员变量的值："+outerNum);
        System.out.println(outerStaticNum);
        IConverter<Integer, String> stringConverter2 = (from) -> {
            // 对静态变量赋值
            outerStaticNum = 72;
            return String.valueOf(from);
        };
        stringConverter2.convert(2);
        System.out.println(outerStaticNum);
    }

    void test2(){
        int num = 1; //局部变量 只有都权限
        Object r = new Object();
        Temp della = new Temp("della");
        Integer i = 2;
        IConverter<Integer, String> stringConverter =
                (from) -> {
                    //num = 18; 对于基本数据类型 此处放开就报错
                    // r = new Object();
                    //della = new Temp("phoenix");
                    della.setName("phoenix");
                    //i += 1;
                    System.out.println(r+""+num+della+i);
                    return String.valueOf(from + num);
                };
        //num = 3; 此处都不能放开
        //r= new Object();
        //i = 2;
        //todo final 的解释
        //局部变量有一个特点, 存在于局部变量表中, 属于线程私有, 不共享. 随着作用域的结束, 可能会被内存回收。
        //这意味 对于 基本类型变量 lambda只是对于 值的赋值 再怎么改变也不会 改变原值 那为啥 一开就用final修饰 杜绝可以改变原值的错觉呢
        //其二 对于 引用型变量 一种情况是上面说到的情况 再者 先是在 创建时 在 堆中创建了元对象 再有的引用变量 同样的 lambda流程中 也是对于 引用数据的复制 而不是对于
        //此引用 本身所代表的对象本身 复制 意味着在某种情况下 即 lambda表达式中起了另外的一个线程拿到了新的引用对象 指向 主线程或者前一个线程
        //创建的对象 但是主线程执行完毕后 他的栈帧中创建的对象 应该被 销毁或者冻结等待销毁 但是因为有了一个新的引用他不会被GC 如果这个对象还保存着
        //上个死亡线程的信息 那就 是一定程度上的 线程数据泄露了



        stringConverter.convert(1);
        //todo 书上的说明 加上查阅的资料：lambda表达式中值 只是一个引用 而不是对象本身 对引用本身重新赋值 并不会改变 lambda表达式之外
        // 局部变量本身 即操作对象也不会是 对象本身而只是它的一个引用 但是可以通过这个引用指向的对象 方法本身改变 实际的对象
        //像是 包装类 Integer i 在lambda中 亦是不可能 i +=1 如此改变 因为这也是用到了=，是为对象的一种赋值 并没有通过对象的方法改变自身
    }
}
@Data
@AllArgsConstructor
@ToString
class Temp{
    private String  name;
}