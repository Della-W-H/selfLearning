package com.hong.designModule.ChainOfResponsibilityPattern;

/**
 * @author wanghong
 * @date 2022/7/1
 * @apiNote  责任链模式
 */

public class Test1{
    public static void main(String[] args) {
        ProcessingObject<String> p1 = new HeaderTextProcessing();
        ProcessingObject<String> p2 = new SpellCheckProcessing();
        p1.setSuccessor(p2);
        System.out.println(p1.handle("Aren`t labdas really sexy?!!"));
    }
}

class HeaderTextProcessing extends ProcessingObject<String> {
    @Override
    protected String handleWork(String input) {
        return "From Raoul, Mario and Alan: "+input;
    }
}

class SpellCheckProcessing extends ProcessingObject<String>{

    @Override
    protected String handleWork(String input) {
        return input.replaceAll("labda","lambda");
    }
}
