package com.hong.Thread.One;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;

/**
 * @author wanghong
 * @date 2022/6/7
 * @apiNote main方法执行的线程信息
 */
public class ThreadPrint {
    public static void main(String[] args) throws InterruptedException {
        //获取Java线程管理MXbean
        ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();
        // 不需要获取同步的monitor和synchronizer信息，仅获取线程和线程堆栈信息
        ThreadInfo[] threadInfos = threadMXBean.dumpAllThreads(false, false);
        //遍历线程信息，仅打印线程ID和线程名称信息
        for (ThreadInfo threadInfo : threadInfos) {
            System.out.println("["+threadInfo.getThreadId()+"]"+threadInfo.getThreadName());
        }
        Thread.sleep(1000000000);
        /*
        * [6]Monitor Ctrl-Break 跟JVM关系不大，这是一个IDEA通过反射的方式，开启一个随着我们运行的jvm进程开启与关闭的一个监听线程
        * [5]Attach Listener
        *      （附加监听器），jdk工具类提供的jvm进程之间通信的工具 如cmd java --version；jvm --jstack，jmap，dump进程间的通信
        *       开启这个线程的两种方式
        *           1.通过jvm参数开启，-XX：StartAttachListener
        *           2.延迟开启，cmd --java-version ---> JVM适时开启Attach Listener线程
        * [4]Signal Dispatcher
        *          信号分发器，我们通过cmd发送jstack，传到了jvm进程中，这时候信号分发器就要发挥作用了
        * [3]Finalizer
        *          JVM垃圾回收相关的内容 此处在只做简单的介绍
        *          1.只有当开始一轮垃圾收集的时候，才会开始调用finalize方法
        *          2.daemon prio =10 高优先级的守护线程 通过运行ThreadPrint 程序可以先在terminal中jps后拿到pid 进行jstack该线程
        *          3.jvm在垃圾收集时，会将失去引用的对象封装到我们的Finalizer对象（Reference），放入我们的F-queue列队中。由Finalizer线程执行inalize方法
        * [2]Reference Handler
        *         引用处理的线程。强，软，弱，虚。 -GC有不同表现 -JVM深入分析
        * [1]main 主线程
        * */

        Thread thread = new Thread();
    }
}
