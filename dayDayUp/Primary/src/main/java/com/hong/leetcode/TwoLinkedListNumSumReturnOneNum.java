package com.hong.leetcode;

/**
 * @author wanghong
 * @date 2022/6/15
 * @apiNote 两个链表表示的数相加 返回一个 链表表示的数 补充 一个 头插法 置逆操作
 */
public class TwoLinkedListNumSumReturnOneNum {
    public static void main(String[] args) {
        NodeList O1 = new NodeList(2);
        O1.next = new NodeList(4);
        O1.next.next = new NodeList(5);
        O1.next.next.next = new NodeList(9);
        //System.out.println("inverse:" + inverse(O1));

        //再次置逆 得把 这个 上面注释掉 要不然 你又会 出现 将尾节点 当成 头节点的 情况

        System.out.println("inverse again:" + inverse(inverse(O1)));

        NodeList T1 = new NodeList(5);
        T1.next = new NodeList(6);
        T1.next.next = new NodeList(4);

        System.out.println(O1+"\n"+T1);
        //System.out.println("reverse2:" + reverseList2(O1));

        System.out.println(reverseList(T1));
        System.out.println(reverseList(T1));

/*        NodeList result1 = getResult(O1, T1);
        System.out.println("sum result:"+result1);

        //reverse
        System.out.println("reverse:"+reverseList(O1));
        System.out.println("reverse:" + reverseList(T1));

        System.out.println("reverse:" + reverseList(result1));
        System.out.println("再次reverse和结果："+reverseList(result1));
        System.out.println("reverse:" + reverseList(O1));*/


    }

    //todo 转不回去了....... 蠢比 不是 不能 再次 置逆 而是 因为 再次 置逆 O1，TI等 入参的时候 已经不是 头节点了 而是 尾节点了 懂？
    //todo 得用图解 这个 就是 标准的 不带头节点单链表 头插法 置逆 考研408 必考 本质上是一种 迭代思想

    private static NodeList reverseList(NodeList t){
        //头插法反转 链表
        //新链表 的头节点
        NodeList pre = null;
        //原链表的 下一个节点1
        NodeList last;
        while (t != null){
            //取出 原链表 的 下一个节点
            last = t.next;
            //原节点 转换
            t.next = pre;
            //新的头节点 赋值
            pre = t;
            //取回 原链表的 下一个 节点 往复操作
            t = last;
        }
        return pre;
    }
    //另一种 头插法 置逆

    private static NodeList inverse(NodeList t){
        NodeList pre = null,last;
        while (t != null){
            last = t;
            t = t.next;
            last.next = pre;
            pre = last;
        }
        return pre;
    }
    //todo 递归 思想 亦可解

    private static NodeList reverseList2(NodeList t){
        NodeList newHead;
        if (t == null || t.next == null){
            return t;
        }
        newHead = reverseList2(t.next);
        t.next.next = t;
        t.next = null;
        return newHead;
    }

    private static NodeList getResult(NodeList num1, NodeList num2){
        NodeList head = new NodeList(0);
        NodeList p = num1,q = num2,current = head;
        int temp = 0;
        while (p != null || q != null) {
            int x = (p != null) ? p.value : 0;
            int y = (q != null) ? q.value : 0;
            //具体 位数 之和
            int sum = x + y +temp;
            // 进位数 只有 0和 1的可能
            temp = sum / 10;
            current.next = new NodeList(sum % 10);
            current = current.next;
            if (p != null){ p = p.next;}
            if (q != null){ q = q.next;}
        }
        //此处 是为了 确保 最高位 运算结果 大于 10 即也要进位 比如 从 10 -->向01的转变
        //剩下 的 地位填充 已经由 current.next = new NodeList(sum%/10) 给予了保证
        if (temp > 0){
            current.next = new NodeList(temp);
        }
        return head.next;
    }

    static class NodeList {
        int value;
        private NodeList(int num){
            this.value = num;
        }
        NodeList next;
        @Override
        public String toString() {
            if (next == null){
                return value+ "";
            }
            return value + next.toString();
        }
    }
}
