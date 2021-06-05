package org.test;

public class test {

	public static void main(String[] args) {
		System.out.println("11111"
				+ "");
		System.out.println("22222");
		System.out.println("33333");
		System.out.println(":main");
		printtest("hello concordia");
	}
	static void printtest(String str){
		System.out.println("11111"
				+ "");
		System.out.println("22222");
		System.out.println("33333");
		System.out.println(":printtest");
		System.out.println("methodNotFor");
	}

	public void nothing(){
		System.out.println("11111");
		System.out.println("22222");
		System.out.println("33333");
		System.out.println(":nothing");
		System.out.println("hello");
	}
	private void Environment() {
		throw new UnsupportedOperationException();
	}
}
