package com.hong.SomeThingSimpleButDegraded;

/**
 * @author wanghong
 * @date 2022/9/20
 * @apiNote
 */
public class TwentyTwo_ThreadLocalProblem{

    private final static ThreadLocal<String> threadLocal = new ThreadLocal<>();

    public static void main(String[] args){

        set();
        get();
    }

    private static void get(){

        System.out.println("被塞进去的值是："+threadLocal.get());
        String s = threadLocal.get().substring(0,2);
        System.out.println(s);
        System.out.println(threadLocal.get());
        threadLocal.remove();
    }

    private static void set(){
        threadLocal.set("della");
        System.out.println("值della被塞进去了");
    }
}
