package org.test;

public class test {

	public static void main(String[] args) {
		System.out.println("src/org/test/test.java:main");
		System.out.println("src/org/test/test.java:main");
		// TODO Auto-generated method stub
		printtest("hello concordia");
	}
	static void printtest(String str){
		System.out.println("src/org/test/test.java:printtest");
		System.out.println("src/org/test/test.java:printtest");
		System.out.println("methodNotFor");
	}

	public void nothing(){
		System.out.println("src/org/test/test.java:nothing");
		System.out.println("src/org/test/test.java:nothing");
		System.out.println("hello");
	}
	private void Environment() {
		System.out.println("src/org/test/test.java:Environment");
		System.out.println("src/org/test/test.java:Environment");
		throw new UnsupportedOperationException();
	}
}
