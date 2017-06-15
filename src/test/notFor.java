package test;

public class notFor {

	public static void main(String[] args) {
	
		
		// TODO Auto-generated method stub
		printtest("hello concordia");
	}
	static void printtest(String str){
		
		
		System.out.println(str);
	}
	public void methodNotFor(String str){
		
		
		System.out.println(str);
		nothing();
	}
	public void nothing(){
		
		
		System.out.println("nothing");
	}
	@SuppressWarnings("unused")
	//indoor class
	private class test{
		private void inside1(){
			System.out.println("nothing");
		}
		private void inside2(){
			
		}
	}
}
