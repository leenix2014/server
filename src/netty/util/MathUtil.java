package netty.util;

public class MathUtil {

	public static int abs(int value){
		return value>0?value:-value;
	}
	
	public static int parseInt(String str){
		if(str == null){
			return 0;
		}
		try {
			return Integer.parseInt(str);
		} catch (Exception e) {
			return 0;
		}
	}
	
	public static int parseInt(String str, int defaultValue){
		if(str == null){
			return defaultValue;
		}
		try {
			return Integer.parseInt(str);
		} catch (Exception e) {
			return defaultValue;
		}
	}
	
	public static boolean isAllZero(int[] a){
		boolean allZero = true;
		for (int i=0;i<a.length;i++) {
			if(a[i] != 0){
				allZero = false;
				break;
			}
		}
		return allZero;
	}
}
