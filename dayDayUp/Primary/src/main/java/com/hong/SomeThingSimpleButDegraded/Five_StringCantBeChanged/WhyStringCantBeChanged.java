package com.hong.SomeThingSimpleButDegraded.Five_StringCantBeChanged;

import lombok.Data;
import lombok.ToString;

import java.lang.reflect.Field;

/**
 * @author wanghong
 * @date 2022/7/4
 * @apiNote 一种可理解的解释就是 编译器的自动优化 当以string变量传值并赋值时 编译器赋值时会自动新生一个string对象 将传递的引用值改变为指向这个新的string对象
 *    这种解释存疑
 */
public class WhyStringCantBeChanged {
    public static void main(String[] args) throws NoSuchFieldException, IllegalAccessException {
        A a = new A(1);
        System.out.println(a);
        change(a);
        System.out.println(a);

        String str = "della";
        System.out.println(str);
        change2(str);
        System.out.println(str);
    }

    private static void change2(String str) throws NoSuchFieldException, IllegalAccessException {
        //str = "phoenix";
        Field value = String.class.getDeclaredField("value");
        value.setAccessible(true);
        char[] chars = (char[]) value.get(str);
        chars[2] = '2';
    }

    private static void change(A a) throws NoSuchFieldException, IllegalAccessException {
        //此处编译就无法通过
        //a.a = 2;
        //反射倒是可以改变属性值
        Field field = A.class.getDeclaredField("value");
        field.setAccessible(true);
        field.set(a,10);
        //todo 换成Integer对象试试  怎么都不行 哪怕是只留一个对象也不能这样get改变只能set...why？ 因为@Data注解
      /*  int i = (int) field.get(a);
        i = 10;
        Field field1 = A.class.getDeclaredField("value2");
        field1.setAccessible(true);
        Integer integer = (Integer) field1.get(a);
        integer = 11;*/
    }
}

@Data
@ToString
class A {
    private final int value;
    //private final Integer value2;
}
