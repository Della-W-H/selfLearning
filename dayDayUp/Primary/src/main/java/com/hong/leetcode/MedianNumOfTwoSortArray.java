package com.hong.leetcode;

import org.apache.commons.lang3.ArrayUtils;

import java.util.Arrays;

/**
 * @author wanghong
 * @date 2022/6/20
 * @apiNote 已只两个有序数组，找到两个数组合并后的中位数
 * //todo 本题的 最优解 应该首先联想到的就是 二分查找哦 没想到 还是嫩了点撒
 */
public class MedianNumOfTwoSortArray {
    public static void main(String[] args) {
        int[] nums1 = {5, 6, 7, 10, 11};
        int[] nums2 = {2, 9, 13};
        System.out.println("the median is : " + getIt(nums1, nums2));
        System.out.println("the median is : " + getMedianByYouself(nums1, nums2));
        //System.out.println("the median is : " + getMedianByAnswer(nums1, nums2));
        System.out.println("the median is : " + findMedianSortedArrays(nums1, nums2));
        System.out.println("the median is : " + findMedianSortedArrays2(nums1, nums2));
        System.out.println("ok we pull!-");

        test();
    }

    private static void test() {
        int[] test = {1, 3, 5, 7, 9, 11, 13};
        System.out.println(Arrays.toString(Arrays.copyOfRange(test, 3, test.length)));
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
            //此处 就是 可以优化的 点 这个 本质上还是 全盘遍历 从头遍历到 中位点 可以考虑 尝试用 二分查找的思想进行优化 即 一次可以排除掉 一部分数据 而不是 一个 数据 一个数据的校验
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

    private static double getMedianByAnswer(int[] num1, int[] num2) {
        System.out.println(Arrays.toString(num1) + Arrays.toString(num2));

        int length = num1.length + num2.length;
        //可以确定 要查找的 中位数 即 在 length/2 的位置上 当然 考虑 向下取整
        int medianIndex = length / 2;
        //数学意义上的 medianIndex/2 同样向下取整 因为 数组的 个数 同样是两个
        int countIndex = medianIndex / 2;

        return 0;
    }

    //这个时间复杂度是O(log((m+n)/2)) 但是还可以优化  递归

    public static double findMedianSortedArrays(int[] nums1, int[] nums2) {
        int n = nums1.length;
        int m = nums2.length;
        int left = (n + m + 1) / 2;
        int right = (n + m + 2) / 2;
        //将偶数和奇数的情况合并，如果是奇数，会求两次同样的 k 。
        //改一下 不要奇数情况下的重复判断 降低 计算机的重复运算次数 试了下 这完全是增加了 代码量 实际工作中应该也不会批次中处理大量数据 而是会 部分数据分批处理
        return (getKth(nums1, 0, n - 1, nums2, 0, m - 1, left) + getKth(nums1, 0, n - 1, nums2, 0, m - 1, right)) * 0.5;
    }

    private static int getKth(int[] nums1, int start1, int end1, int[] nums2, int
            start2, int end2, int k) {
        int len1 = end1 - start1 + 1;
        int len2 = end2 - start2 + 1;
        //让 len1 的⻓度⼩于 len2，这样就能保证如果有数组空了，⼀定是 len1
        if (len1 > len2) {return getKth(nums2, start2, end2, nums1, start1, end1, k);}
        if (len1 == 0) {return nums2[start2 + k - 1];}
        if (k == 1) {return Math.min(nums1[start1], nums2[start2]);}
        //计算出要比较的 下标位置
        int i = start1 + Math.min(len1, k / 2) - 1;
        int j = start2 + Math.min(len2, k / 2) - 1;
        if (nums1[i] > nums2[j]) {
            //如果 nums1 数组相应的位置 比nums2要大 即排除 所有 大于 nums2相应下标之前的所有数据 反之亦然小于亦算于此
            return getKth(nums1, start1, end1, nums2, j + 1, end2, k - (j -
                    start2 + 1));
        } else {
            return getKth(nums1, i + 1, end1, nums2, start2, end2, k - (i -
                    start1 + 1));
        }
    }

    //答案上的最佳优化
    //直言 暂时没看懂 即使有一定的说明的情况下 哎 感觉 算法好鸡儿难啊

    private static double findMedianSortedArrays2(int[] A, int[] B) {
        int m = A.length;
        int n = B.length;

        if (m > n) {
            // 保证 m <= n
            return findMedianSortedArrays2(B, A);
        }
        int iMin = 0, iMax = m;
        while (iMin <= iMax) {
            int i = (iMin + iMax) / 2;
            int j = (m + n + 1) / 2 - i;
            // i 需要增⼤
            if (j != 0 && i != m && B[j - 1] > A[i]) {
                iMin = i + 1;
                // i 需要减⼩
            } else if (i != 0 && j != n && A[i - 1] > B[j]) {
                iMax = i - 1;
            } else { // 达到要求，并且将边界条件列出来单独考虑
                int maxLeft = 0;
                if (i == 0) {
                    maxLeft = B[j - 1];
                } else if (j == 0) {
                    maxLeft = A[i - 1];
                } else {
                    maxLeft = Math.max(A[i - 1], B[j - 1]);
                }
                if ((m + n) % 2 == 1) {
                    return maxLeft;
                } // 奇数的话不需要考虑右半部分
                int minRight = 0;
                if (i == m) {
                    minRight = B[j];
                } else if (j == n) {
                    minRight = A[i];
                } else {
                    minRight = Math.min(B[j], A[i]);
                }
                //如果是偶数的话返回结果
                return (maxLeft + minRight) / 2.0;
            }
        }
        return 0.0;
    }


}
