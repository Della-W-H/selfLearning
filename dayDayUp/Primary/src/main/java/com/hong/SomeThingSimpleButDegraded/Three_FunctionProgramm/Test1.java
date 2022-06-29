package com.hong.SomeThingSimpleButDegraded.Three_FunctionProgramm;


import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.functions.Function;

import java.util.ArrayList;
import java.util.Random;
import java.util.function.Predicate;

import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.toList;

/**
 * @author wanghong
 * @date 2022/6/24
 * @apiNote Lambda 表达式
 *
 * todo something need to be written down
 *  关于lambda 一个接口 只能有一个 抽象方法 从而导致了 匿名内部类使用时 此唯一的方法 才是需要被重写的 从而又导致了 lambda的产生
 *  此时看起来 就像是 对于 匿名内部类的取消直接使用 了 方法形式的传值 那为啥不进一步 直接使用方法的引用呢？JDK1.8也正是如此做的
 *  任何方法都可以被写成引用形式 当是引用形式的值 或者var 其所能被接收的结果对象 也只可以是 各种形式上的 接口唯一抽象方法 所以整体上看起来
 *  就像是 对于接口的一种 构造方法 使用 而这个接口有一个唯一的“属性” 即 唯一的抽象方法 ----> 这个真实方法的引用形式就如同是对于接口唯一默认方法
 *  的“一次赋值” 一般意义上属性赋值 讲究着 数据类型的对应 对于方法引用 则不同 若是接口抽象方法 其抽象结论是（T t）-->R r 即给出一参数
 *  生成另一个参数 如同 Function接口的apply方法 那么其被“赋值”的方法引用本身亦可被抽象成同样的形式
 *  todo 注意lambda对于局部变量的处理和理解
 */
public class Test1 {
    public static void main(String[] args) {
        //匿名内部类
        new LambdaImpl(){
            @Override
            public void justOneMethodWhy() {
                System.out.println("let`s see what will happen?");
                //super.justOneMethodWhy();
            }
        }.see();

        //lambda表达式
        ((Lambda) () -> System.out.println("yaahh")).see();

        String o = "yes";
        ((Lambda2) p -> {
            if (p.test(o)) {
                System.out.println("im done");
            }
        }).getRun(new SelfPredicate());

        ArrayList<String> strs = new ArrayList<>();
        strs.add("yes1");
        strs.add("yez333");
        strs.add("yea22");
        System.out.println(strs.stream().filter("yes"::equals).collect(toList()));
        System.out.println(strs.stream().sorted(comparing(String::length)).collect(toList()));

        //(Lambda)()-> "flying to the moon"

        LambdaImpl lambda = new LambdaImpl();
        lambda.justOneMethodWhy("success");

        strs.add("");
        Predicate<String> selfPredicate = (String s) -> !s.isEmpty();
        Consumer<String> stringConsumer = String::isEmpty;
        Function<String, String> stringObjectFunction = (String s) -> {if (s.length() > 4) {
            return s;
         } else {
            return "";
         }
        };

        Lambdas<String> stringConsumer1 = (String s) -> {
            Random random = new Random();
            int i = random.nextInt(10) + 1;
            System.out.println(s+i);
        };


        System.out.println(strs.stream().filter(selfPredicate).collect(toList()).size());

        doRun(Test1::get,"della");
        doRun((Lambdas<String>) (String s) -> {
            Random random = new Random();
            int i = random.nextInt(10) + 1;
            System.out.println(s+i);
        },"della");

        Lambda speak = Test1::speak;
        Runnable aNew = Test1::new;
    }

    @SuppressWarnings("unchecked")
    private static <S> void doRun(Lambdas l, S s){
        l.get(s);
    }
    private static String speak(){
        return "s";
    }

    private static void get(Object s) {
        Random random = new Random();
        int i = random.nextInt(10) + 1;
        System.out.println(s +""+ i);
    }
}
class LambdaImpl implements Lambda {
    @Override
    public void justOneMethodWhy() {
        System.out.println("function programing is future!");
    }
}

class SelfPredicate implements Predicate{

    @Override
    public boolean test(Object o) {
        return "yes".equals(o);
    }
}
