package com.hong.SomeThingSimpleButDegraded.NineTeen_FastJson;

import com.alibaba.fastjson.JSON;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;

/**
 * @author wanghong
 * @date 2022/9/14
 * @apiNote 根据注解将属性名映射成不同的名称给前端
 */
public class demoOne{
    public static void main(String[] args){
    /*    Fruit fruit=new Fruit();
        fruit.setName("phoenix");
        System.out.println(fruit);
        String result=JSON.toJSONString(fruit);
        System.out.println(result);
        Fruit object=JSON.parseObject(result,Fruit.class);
        System.out.println(object);*/


        Lemon lemon=new Lemon();
        lemon.setName("芒果");
        lemon.setPrice("100");
        System.out.println(lemon);
        String result=JSON.toJSONString(lemon);
        System.out.println(result);
        Fruit object=JSON.parseObject(result,Lemon.class);
        System.out.println(object);
    }
}


class Lemon extends Fruit{
    @JSONField(name = "della")
    private String name;

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name=name;
    }

    @Override
    public String toString(){
        return "Lemon{"+
                "name='"+name+'\''+
                '}';
    }
}


class Fruit{
    @JSONField(serialize=false)
    private String price;

    public String getPrice(){
        return price;
    }

    public void setPrice(String price){
        this.price=price;
    }

    @Override
    public String toString(){
        return "Fruit{"+
                "price='"+price+'\''+
                '}';
    }
}
