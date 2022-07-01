package com.hong.designModule.ObserverPattern;

/**
 * @author wanghong
 * @date 2022/7/1
 * @apiNote
 */
public class LeMonde implements Observer {
    @Override
    public void notify(String tweet) {
        if (tweet != null && tweet.contains("wine")){
            System.out.println("Today cheese, wine and news! " + tweet);

        }
    }
}
