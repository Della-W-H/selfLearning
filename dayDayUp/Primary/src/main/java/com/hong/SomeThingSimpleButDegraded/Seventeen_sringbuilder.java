package com.hong.SomeThingSimpleButDegraded;

import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;

/**
 * @author wanghong
 * @date 2022/9/8
 * @apiNote
 */
public class Seventeen_sringbuilder{
    public static void main(String[] args){
        String s = "della";
        String s1 = "逃离";

        System.out.println(new StringBuilder(s).replace(0,s.length(),"*").toString());

        System.out.println(new StringBuilder(s1).replace(0,s1.length(),"*").toString());

        StringBuilder builder=new StringBuilder();

        for(int i=0;i<s.length();i++){
            builder.append("*");
        }
        System.out.println(builder.toString());




        char[] chars=s.toCharArray();
        for(int i=0;i<chars.length;i++){
            chars[i]='*';
        }

        System.out.println(Arrays.toString(chars));

        String s2=StringUtils.rightPad("",s.length(),"*");
        System.out.println(s2);
    }
}
