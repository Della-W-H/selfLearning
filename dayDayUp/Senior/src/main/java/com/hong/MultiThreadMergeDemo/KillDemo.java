package com.hong.MultiThreadMergeDemo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * @author wanghong
 * @date 2022/5/30
 * @apiNote b站极海视频，java高级 将多个线程操作合并到一个线程中 解决多线程合并通知
 */
public class KillDemo {
    /**
     * 启动10个线程
     * 库存六个
     * 生成一个合并列队
     * 每个用户能拿到自己的请求响应
     */
    public static void main(String[] args) throws InterruptedException {

        ExecutorService executorService = Executors.newCachedThreadPool();
        KillDemo killDemo = new KillDemo();
        //启动扣减库存的异步线程
        killDemo.mergeJob();
        //异步线程启动可能需要时间，可考虑让主线程等待一段时间
        Thread.sleep(2000);

        List<Future<Result>> futureList = new ArrayList<>();
        //用CountDownLatch模拟并发情况
        CountDownLatch countDownLatch = new CountDownLatch(10);
        for (int i = 0; i < 10; i++) {
            //订单号
            final Long orderId = i + 100L;
            //用户id
            final Long userId = (long) i;
            Future<Result> future = executorService.submit(() -> {
                countDownLatch.countDown();
                //等待一秒钟等待其他线程的提交
                countDownLatch.await(1000, TimeUnit.MILLISECONDS);
                return killDemo.operate(new UserRequest(orderId, userId, 1));
            });
            futureList.add(future);
        }
        //每个用户等待300ms 拿到 结果
        futureList.forEach(future -> {
            try {
                Result result = future.get(300, TimeUnit.MILLISECONDS);
                System.out.println(Thread.currentThread().getName() + "客户端请求响应" + result);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    //定义库存
    private Integer stack = 6;

    //定义阻塞队列
    private BlockingQueue<RequestPromise> queue = new LinkedBlockingQueue<>(10);

    /**
     * 用户库存扣减
     *
     * @param request
     * @return
     */
    public Result operate(UserRequest request) throws InterruptedException {

        RequestPromise requestPromise = new RequestPromise(request, null);

        //todo 一个很隐蔽的问题 如果 一个扣减请求 进入队列后 直接 就被消费了 提前释放了一个notify
        //todo 而 此时synchronized锁对象中wait操作还没有进行 到wait操作 可能还会导致 先notify后wait情况
        //改成这样 即将 synchronized加锁提到入队 之前 这样入队 和 出队消费 就不会产生顺序问题
        synchronized (requestPromise) {
            //用户扣减请求进入阻塞队列
            boolean inequeueSuccess = queue.offer(requestPromise, 100, TimeUnit.MILLISECONDS);
            //如果进入阻塞队列失败 则
            if (!inequeueSuccess) {
                return new Result(false, "系统繁忙");
            }
            //todo 此处 视频解释为 此处的promise是存于 全局对象queue中 即指向堆对象 说明针对这个对象是有竞争的
            //todo 二是 wait需要再加锁的情况下才能用 不然会报 IllegalMonitorStateException
            //synchronized（requestPromise）{
            try {
                requestPromise.wait(200);
                if (requestPromise.getResult() == null) {
                    //解：等待时间结束 结果仍未null 即等待超时
                    return new Result(false, "等待超时");
                }
            } catch (InterruptedException e) {
                //问：此处 等待时间结束后 并不会 抛出异常 而是直接进入了 下面的 return
                //return new Result(false,"等待超时");
                e.printStackTrace();
            }
        }
        return requestPromise.getResult();
    }

    @SuppressWarnings("all")
    public void mergeJob() {
        new Thread(() -> {
            List<RequestPromise> list = new ArrayList<>();
            while (true) {
                //如果阻塞列队为空 则 cpu不会一直空转 而是会等待10ms 等待阻塞列队有扣减请求进入其中
                if (queue.isEmpty()) {
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
               /* int mergeCount = 0;
                while (queue.peek() != null){
                    //todo 此处取线程请求如是过慢 而 存线程请求的操作如是过快 就会很有可能导致内存溢出
                    //todo 解决方案 1.遵循一个固定的批次 2.根据阻塞队列长度取完
                    System.out.println("取出被合并的线程：" + (++mergeCount));

                    list.add(queue.poll());
                }*/

                int batchSize = queue.size();
                for (int i = 0; i < batchSize; i++) {
                    //poll 自动有取出移除操作 多看操作注释
                    list.add(queue.poll());
                    System.out.println(Thread.currentThread().getName() + ":合并扣减库存：" + list);
                }


                //多个用户扣除库存的请求数
                int sum = list.stream().mapToInt(e -> e.getUserRequest().getCount()).sum();
                //两种情况
                //其一 库存够 即库存数量大于用户的扣减数量
                if (sum <= stack) {
                    stack -= sum;
                    //notify user 通知用户
                    list.forEach(requestPromise -> {
                        requestPromise.setResult(new Result(true, "ok"));
                        synchronized (requestPromise) {
                            requestPromise.notify();
                        }
                    });
                    continue;
                }

                //其二 库存数不足
                for (RequestPromise requestPromise : list) {
                    Integer count = requestPromise.getUserRequest().getCount();
                    if (count <= stack) {
                        stack -= count;
                        requestPromise.setResult(new Result(true, "ok"));
                    } else {
                        requestPromise.setResult(new Result(false, "库存不足"));
                    }
                    synchronized (requestPromise) {
                        requestPromise.notify();
                    }
                }

                list.clear();
            }

        }, "mergeThread").start();
    }
}

@Data
@ToString
@AllArgsConstructor
class RequestPromise {
    private UserRequest userRequest;
    private Result result;
}

@Data
@AllArgsConstructor
@ToString
class UserRequest {
    private Long orderId;
    private Long userId;
    private Integer count;
}

@ToString
@Data
@AllArgsConstructor
class Result {
    private Boolean result;
    private String msg;
}