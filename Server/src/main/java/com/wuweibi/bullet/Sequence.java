package com.wuweibi.bullet;

public class Sequence {
	

	private static long count = 1;
	
	public synchronized static long next(){
		if(count >= Integer.MAX_VALUE){
			count = 1;
		}
		return count++;
	}
	
	public static void main(String[] args) {
		while(true){
			System.out.println(Sequence.next());
		}
	}
}
