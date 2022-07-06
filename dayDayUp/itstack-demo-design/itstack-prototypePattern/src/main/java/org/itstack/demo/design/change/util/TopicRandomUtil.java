package org.itstack.demo.design.change.util;

import java.util.*;

public class TopicRandomUtil {

    /**
     * 乱序Map元素，记录对应答案key
     * @param option 题目
     * @param key    答案
     * @return Topic 乱序后 {A=c., B=d., C=a., D=b.}
     */
    static public Topic random(Map<String, String> option, String key) {
        Set<String> keySet = option.keySet();

        ArrayList<String> keyList = new ArrayList<String>(keySet);
        Collections.shuffle(keyList);

        HashMap<String, String> optionNew = new HashMap<String, String>();
        int idx = 0;
        String keyNew = "";

        for (String next : keySet) {
            String randomKey = keyList.get(idx++);
            //设置新的答案
            if (key.equals(next)) {
                keyNew = randomKey;
            }
            //根据答案提取出 对应的答案值 与新的答案键填充到新的option中 将此时的新keyNew设置为 答案键
            optionNew.put(randomKey, option.get(next));
        }
        return new Topic(optionNew, keyNew);
    }

    public static void main(String[] args) {
        Map<String, String> map = new HashMap<>();
        map.put("A","della1");
        map.put("B","della2");
        map.put("C","della3");
        map.put("D","della4");
        System.out.println(TopicRandomUtil.random(map, "B").toString());
    }
}
