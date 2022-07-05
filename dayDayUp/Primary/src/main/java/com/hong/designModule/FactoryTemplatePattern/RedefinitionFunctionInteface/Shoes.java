package com.hong.designModule.FactoryTemplatePattern.RedefinitionFunctionInteface;

import com.hong.designModule.FactoryTemplatePattern.RedefinitionFunctionInteface.Clothes;
import lombok.*;

/**
 * @author wanghong
 * @date 2022/7/1
 * @apiNote
 */

@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString(callSuper = true)
public class Shoes extends Clothes {
    private String color;
    private Integer price;

    public Shoes(String name, String color, Integer price) {
        super(name);
        this.color = color;
        this.price = price;
    }

   /* @Override
    public String toString(){
        return "Shoes{name="+super.getName()+", color="+color+", price="+price+"}";
    }*/
}
