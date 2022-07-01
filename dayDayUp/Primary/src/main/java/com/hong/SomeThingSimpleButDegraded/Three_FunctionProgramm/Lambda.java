package com.hong.SomeThingSimpleButDegraded.Three_FunctionProgramm;

/**
 * @author wanghong
 * @date 2022/6/24
 * @apiNote
 */
@FunctionalInterface
public interface Lambda {

    /**
     * why Function style programing must be one default method ? because CPU can not tell which one to choose
     */
    void justOneMethodWhy();

    default void see(){
        this.justOneMethodWhy();
    }

    //这个是属于方法的重载嘛？

    default void justOneMethodWhy(String s){
        System.out.println(s);
    }
    /*default void justOneMethodWhy(){

    }*/
}
