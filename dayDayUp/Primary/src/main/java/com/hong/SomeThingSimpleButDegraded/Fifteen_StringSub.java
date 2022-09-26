package com.hong.SomeThingSimpleButDegraded;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author wanghong
 * @date 2022/8/26
 * @apiNote
 */
public class Fifteen_StringSub {
    public static void main(String[] args) {
        String s = "reservationProductCodeTemplateForNonBroadBand (10).xls";
        Pattern compile = Pattern.compile("[^A-Za-z.]");
        Matcher matcher = compile.matcher(s);
        String s2 = matcher.replaceAll("");
        System.out.println(s2);
//        String s1 = s.split("\\.")[0];
//        if (s1.length()>=45){
//            String substring = s1.substring(0, 45);
//            System.out.println(substring);
//        }
//        System.out.println(s1);
//        System.out.println(s1.replaceAll("\\(", "").replaceAll("\\)", ""));

        //new HSSFWorkbook(s)

        String del = "della";

        System.out.println(StringUtils.rightPad("",del.length(),"*"));

        System.out.println(del.substring(0,2));
    }
}
