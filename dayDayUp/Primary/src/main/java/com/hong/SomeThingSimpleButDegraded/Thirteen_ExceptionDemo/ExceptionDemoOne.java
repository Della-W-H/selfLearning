package com.hong.SomeThingSimpleButDegraded.Thirteen_ExceptionDemo;

/**
 * @author wanghong
 * @date 2022/8/4
 * @apiNote RuntimeException可以 直接throw且不用上抛处理 直接会导致程序停止运行
 *          而 非RuntimeException则必须配置上抛处理 即必须 程序自己捕捉处理或者 要抛到最上一层
 */
public class ExceptionDemoOne {
    public static void main(String[] args) {
        Demo1 demo1 = new Demo1();
        try {
            demo1.throwA();
        } catch (Exception e) {
            e.printStackTrace();
        }

        demo1.throwB();
    }
}

class Demo1{
    public void throwA() throws Exception{
        throw new MyException("erro");
    }

    public void throwB(){
        throw new MyException1("bug");
    }
}

class MyException extends Exception{
   public MyException(){
   }

   public MyException(String msg){}

   public MyException(String msg, Throwable throwable){
       super(msg,throwable);
   }
}

class MyException1 extends RuntimeException{
    public MyException1(String msg){
        super(msg);
    }
}