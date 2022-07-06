package org.itstack.demo.design.change;

import com.alibaba.fastjson.JSON;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

public class MQAdapter {

    public static RebateInfo filter(String strJson, Map<String, String> link) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        return filter(JSON.parseObject(strJson, Map.class), link);
    }

    public static RebateInfo filter(Map obj, Map<String, String> link) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        RebateInfo rebateInfo = new RebateInfo();
        for (String key : link.keySet()) {
            Object val = obj.get(link.get(key));
            String result = "set" + key.substring(0, 1).toUpperCase() + key.substring(1);
            System.out.println("-----------------"+result);
            RebateInfo.class.getMethod(result, String.class).invoke(rebateInfo, val.toString());
        }
        return rebateInfo;
    }

}
