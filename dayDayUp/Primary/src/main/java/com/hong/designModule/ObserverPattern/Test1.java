package com.hong.designModule.ObserverPattern;

/**
 * @author wanghong
 * @date 2022/7/1
 * @apiNote  //todo 有些情况下 像是这种Observer观察者接口只有一种函数式的方法 即可用lambda 方式进行优化 不过 这个例子中 这算得上是优化嘛
 *   多数情况下 Observer中的方法都会很复杂 也许远远不止 一个函数式的默认方法 这时候 Lambda就无法用了 还是老老实实的使用 具体观察者类自身的方法
 */
public class Test1 {
    public static void main(String[] args) {
        Feed feed = new Feed();
        feed.registerObserver(new NYTimes());
        feed.registerObserver(new Guardian());
        feed.registerObserver(new LeMonde());

        feed.registerObserver((String tweet)->{
            if (tweet != null && tweet.contains("queen")){
                System.out.println("Yet another news in London... " + tweet);
            }
        });
        feed.notifyObservers("the queen said her favourite book is Java 8 in Action!");
    }
}
