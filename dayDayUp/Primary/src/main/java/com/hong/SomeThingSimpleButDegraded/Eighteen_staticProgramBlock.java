package com.hong.SomeThingSimpleButDegraded;

/**
 * @author wanghong
 * @date 2022/9/9
 * @apiNote 类初始化的时候 静态代码块也会被初始化
 */
public class Eighteen_staticProgramBlock{

    public static void main(String[] args){
        User user=new User();
        user.setName("della");

    }
}


class User{
    static String tag;

    static {
        tag = "爷来了";
        System.out.println(tag);
    }

    private String name;
    private String age;

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name=name;
    }

    public String getAge(){
        return age;
    }

    public void setAge(String age){
        this.age=age;
    }
}
