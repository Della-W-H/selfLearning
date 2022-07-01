package com.hong.designModule.ObserverPattern;

/**
 * @author wanghong
 * @date 2022/7/1
 * @apiNote
 */
public class Guardian implements Observer {
    @Override
    public void notify(String tweet) {
        if (tweet != null && tweet.contains("queen")){
            System.out.println("Yet another news in London... " + tweet);
        }
    }
}
