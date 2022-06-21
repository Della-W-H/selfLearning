package com.hong.leetcode;

/**
 * @author wanghong
 * @date 2022/6/16
 * @apiNote 符号函数
 */
public class SignFunction {
    public static void main(String[] args) {
        System.out.println(sign1(-5));
    }
    private static int sign1(int x){
        return x > 0 ? 1 : x < 0 ? -1 : 0;
    }
}
