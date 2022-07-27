package com.hong.SomeThingSimpleButDegraded;

import java.util.ArrayList;

/**
 * @author wanghong
 * @date 2022/7/27
 * @apiNote
 */
public class eleven_referenceObjectListQuestion {
    public static void main(String[] args) {
        A a = () -> {
        };

        ArrayList<A> as = new ArrayList<>();
        as.add(a);
        as.add(a);
        as.add(a);
        as.remove(a);

        System.out.println(as.size());
    }
}

interface A{
    void speak();
}