package com.hong.leetcode;

import lombok.extern.slf4j.Slf4j;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;

/**
 * @author wanghong
 * @date 2022/6/17
 * @apiNote 给出 指定 String的 最长 subString
 */
@Slf4j
public class LongestSubStringOfParticularStringWithoutRepeatingChar {

    public static void main(String[] args) {
        String given = "pwwkew";
        System.out.println("the longest subString of given one is : " + getTarget(given));
        System.out.println("the longest subString`s Length of given one is : " + lengthOfLongestSubstring(given));
        //test();
    }
    //todo we made it!

    private static String getTarget(String given) {
        TreeMap<Integer, Character> results = new TreeMap<>();
        String longest = null;
        for (int i = 0; i < given.length(); i++) {
            Character temp = given.charAt(i);
            if (results.containsValue(temp)) {
                //其实 这里 可以不用 回溯 到 出现 重复 位置的 下标 而是考虑 将 重复位置 之前的元素 全部从result中删除 i下标保持不变
                int deleteFlag = results.entrySet().stream().collect(Collectors.toMap(Map.Entry::getValue, Map.Entry::getKey)).get(temp);
                longest = compareAndSwap(results, longest);
                for (int flag = deleteFlag; results.size() != 0 && flag >= results.firstKey(); flag--) {
                    results.remove(flag);
                }
            }
            results.put(i, temp);
            if (i == given.length() - 1) {
                longest = compareAndSwap(results, longest);
            }
        }
        return longest;
    }

    private static String compareAndSwap(TreeMap<Integer, Character> results, String longest) {
        if (null == longest || longest.length() < results.size()) {
            StringBuilder stringBuilder = new StringBuilder();
            results.values().iterator().forEachRemaining(stringBuilder::append);
            return stringBuilder.toString();
        }
        return longest;
    }


    private static String lengthOfLongestSubstring(String s) {
        int n = s.length();
        Set<Character> set = new HashSet<>();
        int ans = 0, i = 0, j = 0;
        while (i < n && j < n) {
            if (!set.contains(s.charAt(j))) {
                set.add(s.charAt(j++));
                ans = Math.max(ans, j - i);
            } else {
                set.remove(s.charAt(i++));
            }
        }
        return set.toString();
    }


    private static void test() {
        //todo 这个方法 -->tailaMap() 返回的是一个全新的对象 而不是在原对象上做的操作 所以 他就是他妈的不会删除 为啥你不看一下 源码呢？三秒钟就能发现的bug为啥等到 头发掉了好几根啊
        TreeMap<Integer, String> map = new TreeMap<>();
        map.put(1, "a");
        map.put(2, "b");
        map.put(3, "c");
        map.put(4, "d");
        System.out.println(map.tailMap(4, false));
        map.put(5, "e");
        System.out.println(map);
    }
}
