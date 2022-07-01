package com.hong.designModule.ObserverPattern;

/**
 * @author wanghong
 * @date 2022/7/1
 * @apiNote
 */
public class NYTimes implements Observer {
    @Override
    public void notify(String tweet) {
        if (tweet != null && tweet.contains("money")){
            System.out.println("Breaking news in NY! " + tweet);
        }
    }
}
