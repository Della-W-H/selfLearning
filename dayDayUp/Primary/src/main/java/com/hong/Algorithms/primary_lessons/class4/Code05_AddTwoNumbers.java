package com.hong.Algorithms.primary_lessons.class4;


/**
* 给你两个 非空 的链表，表示两个非负的整数。它们每位数字都是按照 逆序 的方式存储的，并且每个节点只能存储 一位 数字。

请你将两个数相加，并以相同形式返回一个表示和的链表。

你可以假设除了数字 0 之外，这两个数都不会以 0 开头。

来源：力扣（LeetCode）
链接：https://leetcode.cn/problems/add-two-numbers
著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
* */
public class Code05_AddTwoNumbers {

	// 不要提交这个类
	public static class ListNode {
		public int val;
		public ListNode next;

		public ListNode(int val) {
			this.val = val;
		}

		public ListNode(int val, ListNode next) {
			this.val = val;
			this.next = next;
		}
	}

	public static ListNode addTwoNumbers(ListNode head1, ListNode head2) {
		int len1 = listLength(head1);
		int len2 = listLength(head2);
		//位数较大值
		ListNode l = len1 >= len2 ? head1 : head2;
		//位数较小值
		ListNode s = l == head1 ? head2 : head1;
		ListNode curL = l;
		ListNode curS = s;
		ListNode last = curL;
		//进位值
		int carry = 0;
		int curNum = 0;
		//两数相交部分 数值 结果存储于 位数较大 数
		while (curS != null) {
			curNum = curL.val + curS.val + carry;
			curL.val = (curNum % 10);
			carry = curNum / 10;
			last = curL;
			curL = curL.next;
			curS = curS.next;
		}
		//大数 剩余部分
		while (curL != null) {
			curNum = curL.val + carry;
			curL.val = (curNum % 10);
			carry = curNum / 10;
			last = curL;
			curL = curL.next;
		}
		//最终 进位 判断
		if (carry != 0) {
			last.next = new ListNode(1);
		}
		return l;
	}

	// 求链表长度
	public static int listLength(ListNode head) {
		int len = 0;
		while (head != null) {
			len++;
			head = head.next;
		}
		return len;
	}

	@SuppressWarnings("all")
	private static ListNode generateRandomLinkedList(int len, int value){
		int size = (int)(Math.random() * (len + 1));
		if(size == 0){
			return null;
		}
		size--;
		//制作头节点
		ListNode head=new ListNode((int)(Math.random()*value + 1));
		ListNode pre = head;
		while(size != 0){
			ListNode cur=new ListNode((int)(Math.random()*(value+1)));
			//尾插法
			pre.next = cur;
			pre = cur;
			size--;
		}
		return head;
	}

	private static int getOriginalVal(ListNode head){
		int sum = 0,index = 1;
		ListNode temp = head;
		if(head == null){
			return sum;
		}
		sum  = head.val;
		head = head.next;
		while(head != null){
			sum += head.val * (int)Math.pow(10,index);
			index++;
			head = head.next;
		}
		head = temp;
		return sum;
	}

	public static void main(String[] args){
		int len1 = 5, len2 = 7,maxVal = 9;
		int testTimes = 5000000;
		System.out.println("test begin");
		for(int i=0;i<testTimes;i++){
			ListNode argOne=generateRandomLinkedList(len1,maxVal);
			int originalVal1=getOriginalVal(argOne);
			ListNode argTwo=generateRandomLinkedList(len2,maxVal);
			int originalVal2=getOriginalVal(argTwo);
			ListNode result=addTwoNumbers(argOne,argTwo);
			int resultVal=getOriginalVal(result);
			if(originalVal1 + originalVal2 != resultVal){
				System.out.println("Oops!");
			}
		}

		System.out.println("test end");
	}
}
