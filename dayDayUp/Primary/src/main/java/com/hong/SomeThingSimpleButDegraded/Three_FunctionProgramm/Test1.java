package com.hong.SomeThingSimpleButDegraded.Three_FunctionProgramm;


import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.functions.Function;

import java.util.ArrayList;
import java.util.Random;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * @author wanghong
 * @date 2022/6/24
 * @apiNote Lambda 表达式
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
        strs.add("yes");
        strs.add("yez");
        strs.add("yea");
        System.out.println(strs.stream().filter("yes"::equals).collect(Collectors.toList()));

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


        System.out.println(strs.stream().filter(selfPredicate).collect(Collectors.toList()).size());

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
