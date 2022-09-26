package com.hong.SomeThingSimpleButDegraded;

import java.math.BigDecimal;

/**
 * @author wanghong
 * @date 2022/9/19
 * @apiNote
 */
public class Twenty_LongCalculateProblem{
    public static void main(String[] args){
        Long max = 1231623L;
        Long min = 519829L;

        long result = max -min; //711794
        System.out.println(result);

        double ceil=Math.ceil((max-min)/10); //71179.0
        System.out.println(ceil);

        BigDecimal maxB=BigDecimal.valueOf(max);
        BigDecimal minB=BigDecimal.valueOf(min);
        BigDecimal subtract=maxB.subtract(minB);
        BigDecimal resultB=subtract.divide(new BigDecimal(10),0);
        System.out.println(resultB);

        Integer integer=Integer.valueOf(null);

    }
}
