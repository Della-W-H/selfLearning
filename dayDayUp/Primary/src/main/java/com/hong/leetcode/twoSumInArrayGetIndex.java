package com.hong.leetcode;

import java.util.Arrays;
import java.util.HashMap;


/**
 * @author wanghong
 * @date 2022/6/14
 * @apiNote 给定 数组 和 目标元素 求 元素之和 于数组中的下标
 */
public class twoSumInArrayGetIndex {
    public static void main(String[] args) {
        int[] givens = {2,11,7,15};
        int target = 26;
        System.out.print("---------->indices："+ Arrays.toString(getIndices(givens, target)));
    }
    private static int[] getIndices(int[] givens, int target) {
        HashMap<Integer, Integer> imap = new HashMap<>();
        for (int i = 0; i <= givens.length - 1; i++) {
            imap.put(givens[i],i);
            System.out.print("values:"+givens[i]+" index:"+i+"~~");
        }
        for (int given : givens) {
            if (given >= target){
                return new int[]{-1};
            }
            Integer result = imap.get(target - given);
            if (null != result){
                return new int[] {imap.get(given),result};
            }
        }
        return new int[] {-1};
    }
}
