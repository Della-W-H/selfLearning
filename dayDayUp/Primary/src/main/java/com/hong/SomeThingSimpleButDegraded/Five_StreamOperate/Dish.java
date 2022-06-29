package com.hong.SomeThingSimpleButDegraded.Five_StreamOperate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author wanghong
 * @date 2022/6/29
 * @apiNote
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Dish {
    private String name;
    private String type;

    public static boolean isVegetarian(Dish dish){
        return !"肉菜".equals(dish.getType());
    }
}
