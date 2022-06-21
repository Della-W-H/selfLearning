package com.hong.leetcode;

import org.apache.commons.lang3.ArrayUtils;

import java.util.Arrays;

/**
 * @author wanghong
 * @date 2022/6/20
 * @apiNote 已只两个有序数组，找到两个数组合并后的中位数
 */
public class medianNumOfTwoSortArray {
    public static void main(String[] args) {
        int[] nums1 = {5, 6, 7};
        int[] nums2 = {2, 9};
        System.out.println("the median is : " + getIt(nums1, nums2));
        System.out.println("the median is : " + getMedianByYouself(nums1, nums2));

    }

    private static double getIt(int[] nums1, int[] nums2) {
        long start = System.currentTimeMillis();
        int[] sort = ArrayUtils.addAll(nums1, nums2);
        double result = getMedian(sort);
        long end = System.currentTimeMillis();
        System.out.println("时间消耗：" + (end - start));
        return result;
    }

    private static double getMedian(int[] sort) {
        Arrays.sort(sort);
        if ((sort.length & 1) == 0) {
            //偶数组
            return (sort[(sort.length / 2) - 1] + sort[sort.length / 2]) / 2.0;
        } else {
            //奇数组
            return sort[sort.length / 2];
        }

    }

    //用算法 找出来 不要用 java工具类 用多了 人会 变成傻逼的
    //时间复杂度 O(m+n) 考虑 将这个时间复杂度 降低到 O(log(m+n))

    private static double getMedianByYouself(int[] num1, int[] num2) {
        long start = System.currentTimeMillis();
        int length = num1.length + num2.length;
        int left = -1, right = -1;
        int value1 = 0, value2 = 0;
        int n = num1.length;
        int m = num2.length;
        for (int i = 0; i <= length / 2; i++) {
            left = right;
            if (value1 < n && (value2 >= m || num1[value1] <= num2[value2])) {
                right = num1[value1++];
            } else {
                right = num2[value2++];
            }
        }
        //判断奇偶
        if ((length & 1) == 1) {
            long end = System.currentTimeMillis();
            System.out.println("时间消耗：" + (end - start));
            //奇数
            return right;
        } else {
            //偶数
            long end = System.currentTimeMillis();
            System.out.println("时间消耗：" + (end - start));
            return (left + right) / 2.0;
        }
    }
}