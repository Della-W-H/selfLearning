package com.hong.SomeThingSimpleButDegraded.Three_FunctionProgramm;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * @author wanghong
 * @date 2022/6/28
 * @apiNote
 */
public class Test2 {

    private static Map<String, Function<String,Fruit>> map = new HashMap<>();
    static {
        map.put("apple", Fruit::new);
        map.put("orange", Fruit::new);
    }
    public static void main(String[] args) {
        Fruit apple = createSpecialFruit("apple", "100");
        System.out.println(apple);
        Fruit orange = createSpecialFruit("orange", "10");
        System.out.println(orange);
    }

    private static Fruit createSpecialFruit(String name, String price) {
        Fruit fruit = map.get(name).apply(price);
        fruit.setName(name);
        return fruit;
    }
}

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
class Fruit{
    private String name;
    private String price;
    Fruit(String price) {
        this.price = price;
    }
}
