package com.hong.Algorithms.primary_lessons;

/**
 * @author wanghong
 * @date 2022/9/26
 * @apiNote
 */
public class ClassOne{
    public static void main(String[] args){
        //-1 是补码状态
        print(-1);
        print(1);
        print(Integer.MAX_VALUE);
        print(Integer.MIN_VALUE);
        print(~Integer.MAX_VALUE+1);
        print(~Integer.MIN_VALUE+1);
        print(0);
        print(-0);
        print(-5);
        print(~-5);


    }

    //查看int的二进制
    public static void print(int num){
        for(int i=31;i>=0;i--){
            System.out.print((num&(1<<i))==0?"0":"1");
        }
        System.out.println();
    }

    //插入排序
    public static void insertSort(int[] arr){
        if (arr == null || arr.length < 2){
            return;
        }

        int N = arr.length;
        for(int end=1;end<N;end++){
            /*int newNumIndex = end;
            while(newNumIndex -1 >= 0 && arr[newNumIndex - 1] > arr[newNumIndex]){
                swap(arr,newNumIndex - 1, newNumIndex);
                newNumIndex ++;
            }*/
            for(int pre = end -1; pre >= 0 && arr[pre] > arr[pre+1]; pre--){
                swap(arr,pre,pre+1);
            }
        }
    }

    private static void swap(int[] arr,int i, int j){
        int tmp = arr[i];
        arr[j] = arr[i];
        arr[i] = tmp;
    }

}

