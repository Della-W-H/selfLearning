package com.hong.SomeThingSimpleButDegraded;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author wanghong
 * @date 2022/9/22
 * @apiNote
 */
public class TwentyFour_StringBuilderHash{
    public static void main(String[] args){

        Map<String,Object> map=new HashMap<>();
        map.put("1","della");
        map.put("2",2);
        map.put("3",new Date(99999));
        map.put("4","d");
        map.put("5","w");


        for(int i=0;i<10;i++){
            if(i%3 == 0){
                map.put("3",new Date(10000));
                map.put("2",4);
            } else {
                map.put("2",2);
                map.put("3",new Date(99999));
            }
            StringBuilder builder=new StringBuilder();
            map.entrySet().stream().filter(a->
                    !"3".equals(a.getKey()) && !"2".equals(a.getKey())
            ).forEach(a->builder.append(a.getValue()));
            map.values().forEach(System.out::print);
            System.out.println();
            System.out.println(builder.toString());
            System.out.println(builder.toString().hashCode());
            System.out.println("_____");
        }
    }
}
