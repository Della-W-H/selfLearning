package com.hong.Algorithms.primary_lessons.class4;

// 测试链接：https://leetcode.cn/problems/merge-two-sorted-lists

import java.math.BigDecimal;

/**
 * 将两个升序链表合并为一个新的 升序 链表并返回。新链表是通过拼接给定的两个链表的所有节点组成的。
 */
public class Code06_MergeTwoSortedLinkedList {


	public static class ListNode {
		public int val;
		public ListNode next;

		public ListNode(int val){
			this.val = val;
		}
	}

	public static ListNode mergeTwoLists(ListNode head1, ListNode head2) {
		if (head1 == null || head2 == null) {
			return head1 == null ? head2 : head1;
		}
		ListNode head = head1.val <= head2.val ? head1 : head2;
		ListNode cur1 = head.next;
		ListNode cur2 = head == head1 ? head2 : head1;
		ListNode pre = head;
		while (cur1 != null && cur2 != null) {
			if (cur1.val <= cur2.val) {
				pre.next = cur1;
				cur1 = cur1.next;
			} else {
				pre.next = cur2;
				cur2 = cur2.next;
			}
			pre = pre.next;
		}
		pre.next = cur1 != null ? cur1 : cur2;
		return head;
	}

	private static ListNode generateSortList(int len){
		if(len == 0){
			return null;
		}else{
			int num=randomNum();
			ListNode node=new ListNode(num);
			node.next=generateSortList(--len,num);
			return node;
		}
	}

	private static ListNode generateSortList(int len, int num){
		if(len == 0){
			return null;
		} else {
			int temp =getRandom(num);
			ListNode node=new ListNode(temp);
			node.next=generateSortList(--len,temp);
			return node;
		}
	}

	private static int getRandom(int value){
		int i;
		do{
			i=randomNum();
		}while(i < value);
		return i;
	}

	private static int getOriginalVal(ListNode head){
		ListNode temp = head;
		int data = 0;
		if(head == null){
			return data;
		}
		data = head.val;
		head = head.next;
		while(head != null){
			data = data*10+head.val;
			head = head.next;
		}
		head = temp;
		return data;
	}

	//数字越大生成的概率越小
	private static int randomNum(){
		double percent = BigDecimal.valueOf(Math.random() * 90).setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
		double initial = 5.00, threshold = 1.25;
		int tag = 9;
		double temp = 5.0;
		while(percent > temp){
			initial += threshold;
			temp += initial;
			tag--;
		}
		return tag;
	}

	private static void testRandom(){
		int testTimes = 10000000;
		int[] nums=new int[9];
		for(int i=0;i<testTimes;i++){
			int result=randomNum();
			if(result>9||result<1){
				System.out.println("Oops! "+result);
			}
			nums[result-1]++;
		}
		for(int i=0;i<nums.length;i++){
			System.out.println((i+1)+" 出现了 "+nums[i]+"次 实际概率约为"+(double)nums[i]/testTimes);
		}
	}

	public static void main(String[] args){
		int testTimes =5000,len = 4;
		for(int i=0;i<testTimes;i++){
			ListNode num1 = generateSortList(len);

			if(i%500 == 0){
				ListNode num2 = generateSortList(len);
				System.out.println("\n"+getOriginalVal(num1)+"	"+getOriginalVal(num2));
				ListNode result = mergeTwoLists(num1,num2);
				System.out.println("---->"+getOriginalVal(result)+"\n");
			}
		}
        testRandom();
	}
}
