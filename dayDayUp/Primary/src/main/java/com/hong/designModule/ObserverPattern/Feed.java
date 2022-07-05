package com.hong.designModule.ObserverPattern;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wanghong
 * @date 2022/7/1
 * @apiNote 今典的观察者模式 观察者容器
 */
public class Feed implements Subject {
    private final List<Observer> observers = new ArrayList<>();

    @Override
    public void registerObserver(Observer o) {
        this.observers.add(o);
    }

    @Override
    public void notifyObservers(String tweet) {
         observers.forEach(o->o.notify(tweet));
    }
}
