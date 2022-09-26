package com.hong.Algorithms.primary_lessons;

/**
 * @author wanghong
 * @date 2022/9/26
 * @apiNote 前缀和 为了解决 数组L到R的和
 */
public class ClassTwo{

    private static int[] preSum;

    public static void main(String[] args){
        int[] arr = {3,4,6,9,11,15,19,31,39,44};
        int[] arr2 = {31,42,68,19,111,15,19,531,39,4};
        System.out.println(rangeSum(arr2,4,8));
    }

    //生成前缀和 数组
    public static void RangeSum2(int[] array){
        int N = array.length;
        preSum = new int[N];
        preSum[0] = array[0];
        for(int i = 1; i <N; i++){
            preSum[i] = preSum[i - 1] + array[i];
        }
    }

    public static int rangeSum(int[] arr,int L, int R){
        RangeSum2(arr);
        return L == 0 ? preSum[R] : preSum[R]-preSum[L - 1];
    }
}

/**
 * Math.random是生成[0-1)的随机数，注意是等概率的生成
 */
class MathRandomLesson{
    private static int testTimes = 10000;

    /**
     * 随机生成1，2，3，4，5等概率
     * @return
     */
    private static int f1(){
        return (int)(Math.random() * 5) + 1;
    }
    /**
     * [0-9]各整数出现的次数
     */
    private static void testGet0T9(){

        int[] counts = new int[10];
        for(int i=0;i<testTimes;i++){
            int ans = (int)(Math.random() * 10);
            counts[ans]++;
        }
        for(int i=0;i<10;i++){
            System.out.println(i+" 这个数出现了 "+counts[i]+" 次");
        }
    }

    /**
     * 返回属于[0，1)范围内的任意一个数，
     * 任意的x，x属于[0,1)，[0,x)范围上的数出现概率由原来的x变为 X的平方的概率
     * @return
     */
    private static void x2XPower(double x){
        int count = 0;
        for(int i=0;i<testTimes;i++){
            if(Math.max(Math.random(),Math.random()) < x){
                count++;
            }
        }
        System.out.println(x +"出现的概率"+(double)count/(double)testTimes);
        System.out.println(Math.pow(x,2));

    }

    /**
     * 随机机制 只能用f1 等概率返回 0和1
     * @return
     */
    private static int f2(){
        int ans = 0;
        do{
            ans = f1();
        } while(ans == 3);
        return ans < 3 ? 0 : 1;
    }
    private static void testF2(){
        int count = 0;
        for(int i=0;i<testTimes;i++){
            if(f2() == 0){
                count++;
            }
        }
        System.out.println((double)count/(double)testTimes);
    }

    /**
     * 得出一个0-6随机数随机生成函数
     * 得到000~111做等概率 即0-7等概率出现
     * @return
     */
    private static int f3(){
        // f2()<<2 + f2()<<1 + f2() 这尼玛大有区别了 主要还是java中位移运算比算数运算优先级低
        return (f2()<<2) + (f2()<<1) + f2();
    }
    private static int f4(){
        int ans = 0;
        do{
            ans = f3();
        } while(ans == 7);
        return ans;
    }
    private static void testF4(){
        int[] counts = new int[7];
        for(int i=0;i<testTimes;i++){
            int num = f4();
            counts[num]++;
        }
        for(int i=0;i<7;i++){
            System.out.println(i+" 这个数出现了 "+counts[i]+" 次");
        }
    }

    /**
     * 这个就是以 固定的概率返回0和1 此处返回0 的概率为 84%
     * @return
     */
    private static int X(){
        return Math.random() < 0.84 ? 0 : 1;
    }
    //修正X() 等概率返回0和1
    private static int Xc(){
        int ans = 0;
        do{
            ans = X();
        } while(ans == X());
        return ans;
    }
    private static void testXAXc(){
        int count = 0;
        for(int i=0;i<testTimes;i++){
            if(X() == 0){
                count++;
            }
        }
        System.out.println((double)count/(double)testTimes);
        count = 0;
        for(int i=0;i<testTimes;i++){
            if(Xc() == 0){
                count++;
            }
        }
        System.out.println((double)count/(double)testTimes);
    }

    public static void main(String[] args){
        //testGet0T9();
        //x2XPower(0.17d);
        //testF2();
        //testF4();
        testXAXc();
    }
}
