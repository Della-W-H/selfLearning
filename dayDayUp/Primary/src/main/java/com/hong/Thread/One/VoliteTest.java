package com.hong.Thread.One;

/**
 * @author wanghong
 * @date 2022/6/9
 * @apiNote 代码可见性问题
 */
public class VoliteTest {
    public static void main(String[] args) {
        Aobing aobing = new Aobing();
        aobing.start();
        while (true){
            if(aobing.isFlag()){
                System.out.println("真的要学习！");
            }
        }
    }
}

class Aobing extends Thread{

    //不添加 volatile 你看看结果

    private volatile boolean flag = false;

    public boolean isFlag(){
        return flag;
    }

    @Override
    public void run() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e){
            e.printStackTrace();
        }
        flag = true;
        System.out.println("flag =" +flag);
    }
}
