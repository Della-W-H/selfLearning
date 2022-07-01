package com.hong.designModule.ChainOfResponsibilityPattern;

/**
 * @author wanghong
 * @date 2022/7/1
 * @apiNote
 */
public abstract class ProcessingObject<T> {
    protected ProcessingObject<T> successor;
    public void setSuccessor(ProcessingObject<T> successor){
        this.successor = successor;
    }

    public T handle(T input){
       T t = handleWork(input);
       if (successor != null){
           return successor.handle(t);
       }
       return t;
    }

    abstract protected T handleWork(T input);
}
