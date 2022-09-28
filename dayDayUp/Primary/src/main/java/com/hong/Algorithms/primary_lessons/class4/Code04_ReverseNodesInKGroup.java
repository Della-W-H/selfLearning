package com.hong.Algorithms.primary_lessons.class4;

import java.util.ArrayList;
import java.util.List;

// 测试链接：https://leetcode.com/problems/reverse-nodes-in-k-group/
public class Code04_ReverseNodesInKGroup {

	// 不要提交这个类
	public static class ListNode {
		public int val;
		public ListNode next;

		public ListNode(int val){
           this.val = val;
           this.next = null;
		}
	}

	public static ListNode reverseKGroup(ListNode head, int k) {
		ListNode start = head;
		ListNode end = getKGroupEnd(start, k);
		if (end == null) {
			return head;
		}
		// 第一组凑齐了！
		head = end;
		reverse(start, end);
		// 上一组的结尾节点
		ListNode lastEnd = start;
		while (lastEnd.next != null) {
			start = lastEnd.next;
			end = getKGroupEnd(start, k);
			if (end == null) {
				return head;
			}
			reverse(start, end);
			//注意 这个 end 已经完成了位置变化了 来到了 新的start位置了 start的位置转换成了end的位置
			lastEnd.next = end;
			lastEnd = start;
		}
		return head;
	}

	/**
	 * 找到K值 所代表的 尾节点
	 */
	private static ListNode getKGroupEnd(ListNode start,int k) {
		while (--k != 0 && start != null) {
			start = start.next;
		}
		return start;
	}

	private static void reverse(ListNode start,ListNode end) {
		//注意 这个end 已经变成了 新的start了 同时 start也已经和最初的end位置互换了
		end = end.next;
		ListNode pre = null;
		ListNode cur = start;
		ListNode next = null;
		while (cur != end) {
			//原地置逆
			next = cur.next;
			cur.next = pre;
			pre = cur;
			cur = next;
		}
		start.next = end;
	}

	@SuppressWarnings("all")
	private static ListNode generateRandomLinkedList(int len, int value){
		//随机指标下的链表长度
		int size = (int)(Math.random() * (len + 1));
		if(size == 0){
			return null;
		}
		size--;
		//制作头节点
		ListNode head=new ListNode((int)(Math.random() * (value + 1)));
		ListNode pre = head;
		while(size != 0){
			ListNode cur=new ListNode((int)(Math.random() * (value + 1)));
			//尾插法
			pre.next = cur;
			pre = cur;
			size--;
		}
		return head;
	}

	private static List<Integer> getOriginalOrderVal(ListNode head){
		List<Integer> values=new ArrayList<>();
		while(head != null){
			values.add(head.val);
			head = head.next;
		}
		return values;
	}

	private static boolean checkResult(List<Integer> originalVal, ListNode resultHead, int K){
		//确保头节点 不会被修改
		ListNode temp = resultHead;
		int count = originalVal.size() / K;
		for(int i=0;i<count;i++){
			for(int j = 0, n = K - 1; j < K; j++,n--){
				if(!originalVal.get(i * K + n).equals(resultHead.val)){
					resultHead = temp;
					return false;
				} else {
					resultHead = resultHead.next;
				}
			}
		}
		resultHead = temp;
		return true;
	}

	public static void main(String[] args){
		int testTimes = 500000;
		int lenMax = 50;
		int valueMax = 500;
		int K = 4;
		System.out.println("test begin!");
		for(int i=0;i<testTimes;i++){
			ListNode head=generateRandomLinkedList(lenMax,valueMax);
			List<Integer> originalOrderVal=getOriginalOrderVal(head);
			ListNode resultHead=reverseKGroup(head,K);
			if(!checkResult(originalOrderVal,resultHead,K)){
				System.out.println("Oops!");
			}
		}
		System.out.println("test finish");

		/*ListNode head = new ListNode(1);
		ListNode head2 = new ListNode(2);
		ListNode head3 = new ListNode(3);
		ListNode head4 = new ListNode(4);
		ListNode head5 = new ListNode(5);
        head.next = head2;
        head2.next = head3;
        head3.next = head4;
        head4.next = head5;

		List<Integer> originalOrderVal=getOriginalOrderVal(head);
		originalOrderVal.forEach(System.out::print);
		System.out.println();
		ListNode listNode=reverseKGroup(head,2);

		System.out.println(checkResult(originalOrderVal,listNode,2));

		while(listNode != null){
			System.out.print(listNode.val);
			listNode = listNode.next;
		}*/
	}

}
