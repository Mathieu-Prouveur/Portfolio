import java.util.HashSet;

public class test {
	public static void main(String[] args) {
		boolean[] missa=new boolean[3];
		missa[0]=true;
		missa[1]=true;
		missa[2]=true;
		
		yolo(missa);
		HashSet<Integer> ensemble=new HashSet<Integer>();
		if(missa[0]=true){
			System.out.println("swag");
		}
		else{
			System.out.println("yolo");
		}
		swaggy(ensemble);
		
		System.out.println(ensemble.size());
	}
	
	public static void yolo(boolean[] ensemble){
		ensemble[0]=false;
	}
	public static void swaggy(HashSet<Integer> ensemble){
		Integer b=new Integer(3);
		ensemble.add(b);
	}
}
