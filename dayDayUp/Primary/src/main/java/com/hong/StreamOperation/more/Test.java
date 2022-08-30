package com.hong.StreamOperation.more;

import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author wanghong
 * @date 2022/8/15
 * @apiNote
 */
public class Test {
    public static void main(String[] args) {

        ArrayList<Human> humans = new ArrayList<Human>() {{
            add(new Human() {{
                setName("della");
                setAge("23");
                setNation("china");
            }});
            add(new Human() {{
                setName("phoenix");
                setAge("24");
                setNation("The USA");
            }});
        }};

        List<Animal> collect = humans.stream().map(a -> {
            Animal animal = new Animal();
            BeanUtils.copyProperties(a, animal);
            return animal;
        }).collect(Collectors.toList());

        System.out.println(collect);

    }
}
