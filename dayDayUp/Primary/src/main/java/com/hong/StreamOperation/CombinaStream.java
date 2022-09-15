package com.hong.StreamOperation;



import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author wanghong
 * @date 2022/9/13
 * @apiNote
 */
public class CombinaStream{
    public static void main(String[] args){
        Stream stream=Stream.of(1,2,3);
        Stream another=Stream.of(4,5,6);
        Stream third=Stream.of(7,8,9);
        Stream more=Stream.of(0);
        Stream concat=Stream.of(stream,another,third,more).flatMap(integerStream->integerStream);
        List collect=(List)concat.collect(Collectors.toList());
        collect.forEach(System.out::print);
        //List expected=Lists.list(1,2,3,4,5,6,7,8,9,0);
        //Assertions.assertIterableEquals(expected,collect);
    }
}
