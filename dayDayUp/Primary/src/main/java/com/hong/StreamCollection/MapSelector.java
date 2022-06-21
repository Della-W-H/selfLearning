package com.hong.StreamCollection;

import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author wanghong
 * @date 2022/5/31
 * @apiNote
 */
public class MapSelector {
    public static void main(String[] args) {

        List<Human> humans = new ArrayList<>();
        humans.add(Human.builder().age(1).name("Aella").build());
        humans.add(Human.builder().age(2).name("Bella").build());
        humans.add(Human.builder().age(3).name("Cella").build());
        humans.add(Human.builder().age(4).name("Della").build());
        humans.add(Human.builder().age(5).name("Eella").build());

        //拿出名字是name=>Della age=>4
        humans.stream().map(c ->
        {if ("Della".equals(c.getName()) && c.getAge().equals(4)) {
            return c;
        } else {
            return "";   //就很蠢
        }
        }).filter(c->!"".equals(c)).forEach(System.out::println);
    }
}

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
class Human{
    private String name;
    private Integer age;
}