package com.hong.SomeThingSimpleButDegraded.One_TemplatemethodCore;

/**
 * @author wanghong
 * @date 2022/6/10
 * @apiNote
 */
public abstract class Supper {

    abstract void doRun();

    public void doSee(){
        System.out.println("you are my son!");
        doRun();
    }

}
