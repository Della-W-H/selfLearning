package com.hong.Algorithms;

/**
 * @author wanghong
 * @date 2022/6/2
 * @apiNote 二分查找
 */
public class BinarySearch {
    public static void main(String[] args) {
        int[] array = {1,5,7,8,11,14,16,19,24,34,36,39,44,49,50};
        int target = 50;
        System.out.println("index of " + target + " is " + binarySearch(target,array));
    }

    private static int binarySearch(int target, int[] array) {
        int left = 0, right = array.length - 1, mid;
        while (left <= right){
            //为什么用无符号 右移 因为 这样可以避免 left，right等标志位在 int.max最大值时 相加造成的数值越界情况
           mid = (left + right) >>> 1;
           if (array[mid] == target){
               return mid;
           } else if (array[mid] > target){
               right = mid -1;
           } else {
               left = mid + 1;
           }
        }
        return -1;
    }
}
