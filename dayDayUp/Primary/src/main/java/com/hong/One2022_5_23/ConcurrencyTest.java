package com.hong.One2022_5_23;


/**
 * @author wanghong
 * @date 2022/5/23
 * @apiNote 下列代码并发执行一定比串行执行快吗？
 */
public class ConcurrencyTest {
    private static final long count = 100001;

    private static void main(String[] args) throws InterruptedException {

        concurrency();
        serial();
    }

    @SuppressWarnings("all")
    private static void concurrency() throws InterruptedException {
        long start = System.currentTimeMillis();
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                int a = 0;
                for (long i = 0; i < count; i++) {
                    a += 5;
                }
            }
        });
        thread.start();
        int b = 0;
        for (long i = 0; i < count; i++) {
            b--;
        }
        thread.join();
        long time = System.currentTimeMillis() - start;
        System.out.println("concurrency:" + time + "ms,b=" + b);
    }

    private static void serial() {

        long start = System.currentTimeMillis();
        int a = 0;
        for (long i = 0; i < count; i++) {
            a += 5;
        }
        int b = 0;
        for (long i = 0; i < count; i++) {
            b--;
        }
        long time = System.currentTimeMillis() - start;
        System.out.println("serial:" + time + "ms,b=" + b);
    }
}
