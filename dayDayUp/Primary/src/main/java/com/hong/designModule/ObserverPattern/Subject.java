package com.hong.designModule.ObserverPattern;

/**
 * @author wanghong
 * @date 2022/7/1
 * @apiNote
 */
public interface Subject {
   void registerObserver(Observer o);
   void notifyObservers(String tweet);
}
