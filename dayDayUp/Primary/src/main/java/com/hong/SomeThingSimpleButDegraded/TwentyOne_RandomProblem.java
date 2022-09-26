package com.hong.SomeThingSimpleButDegraded;

import java.util.Random;

/**
 * @author wanghong
 * @date 2022/9/20
 * @apiNote
 */
public class TwentyOne_RandomProblem{
    public static void main(String[] args){
        Random random=new Random();
        for(int i=0;i<1000;i++){

            System.out.println("a------>第"+(i+1)+"次随机数："+random.nextInt(10));
            System.out.println("b------>第"+(i+1)+"次随机数："+(random.nextInt(10)+100));
        }
    }
}
