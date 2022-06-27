package com.hong.SomeThingSimpleButDegraded.Three_FunctionProgramm;

import java.util.function.Predicate;

/**
 * @author wanghong
 * @date 2022/6/27
 * @apiNote
 */
public interface Lambda2 {

    void run(Predicate p);

    default void getRun(Predicate p){
        this.run(p);
    }
}
