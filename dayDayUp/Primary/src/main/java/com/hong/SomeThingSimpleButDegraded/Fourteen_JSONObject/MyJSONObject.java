package com.hong.SomeThingSimpleButDegraded.Fourteen_JSONObject;

import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.Collections;

/**
 * @author wanghong
 * @date 2022/8/17
 * @apiNote JSONObject的扩容问题
 */
public class MyJSONObject {
    public static void main(String[] args) {
        JSONObject object = new JSONObject();
        for (int i = 0; i < 100; i++) {
            object.put(i+"-",i);
        }
        System.out.println(object);


        String a = "1";

        System.out.println(Long.parseLong(a));

        ArrayList<String> list = new ArrayList<>();
        list.add("a");

        ArrayList<String> dest = new ArrayList<>(list);
        list.remove("a");
        System.out.println(dest);

    }


}
