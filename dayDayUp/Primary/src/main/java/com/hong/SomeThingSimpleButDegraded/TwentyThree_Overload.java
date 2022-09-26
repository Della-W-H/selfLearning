package com.hong.SomeThingSimpleButDegraded;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * @author wanghong
 * @date 2022/9/21
 * @apiNote
 */
public class TwentyThree_Overload{

    public static void main(String[] args){
        //speak("della",new Date());
        //speak("della",2);

        Random random=new Random();

        for(int i=0;i<100;i++){
            long start=System.currentTimeMillis();
            List<Map<String,Object>> add=new ArrayList<>();
            for(int i1=0;i1<10000;i1++){
                Date date=new Date();
                Calendar instance=Calendar.getInstance();
                instance.setTime(date);
                HashMap<String,Object> map=new HashMap<>();

                instance.add(Calendar.MONTH,random.nextInt(9)-8);
                map.put("expired_time",instance.getTime());
                instance.add(Calendar.MONTH,-1);
                map.put("create_time",instance.getTime());
                map.put("update_time",new Date());
                instance.add(Calendar.MONTH,-1);
                map.put("used_time",instance.getTime());
                instance.add(Calendar.MONTH,-1);
                map.put("assign_time",instance.getTime());
                map.put("recovery_time",new Date());
                add.add(map);
                map.entrySet().iterator().forEachRemaining(a->System.out.println(a.getKey()+" : "+a.getValue()));
            }
            System.out.println("第"+(i+1)+"次耗时："+(System.currentTimeMillis()-start));
        }
    }

    public static void speak(String a, Date date){
        System.out.println(a+"说现在："+date+"时间");
    }

    public static void speak(String a, Integer b){
        System.out.println(a+"说现在:"+b+"分了");
    }
}

