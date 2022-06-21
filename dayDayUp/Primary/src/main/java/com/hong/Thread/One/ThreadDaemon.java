package com.hong.Thread.One;

/**
 * @author wanghong
 * @date 2022/6/7
 * @apiNote 守护线程
 */
public class ThreadDaemon {
    public static void main(String[] args) {
        Thread thread = new Thread(new DaemonThread(), "Daemon Thread!");
        //设置为守护线程
        thread.setDaemon(true);
        //todo 一个注意点 start必须在setDaemon之后执行 因为看源码 setDaemon之前会有一个isAlive本地方法的程序状态检查，即非活跃状态才能设置为Daemon线程
        thread.start();
        //thread 执行完后 main线程退出 看Finish！会不会被输出
    }
    static class DaemonThread implements Runnable{

        @Override
        public void run() {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e){
                e.printStackTrace();
            } finally { //todo finally不能保证守护线程的最终执行
                System.out.println("Finish!");
            }
        }
    }
}
