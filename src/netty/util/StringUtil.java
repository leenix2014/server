package netty.util;

import org.apache.commons.lang.StringUtils;

public class StringUtil {

	public static boolean isEmpty(String str){
		return str == null || str.isEmpty();
	}
	
	public static boolean isNotEmpty(String str){
		return !isEmpty(str);
	}
	
	public static String nonNull(String str){
		return str == null?"":str;
	}
	
	public static String toSize(String str, int size, char padChar){
		if(str == null || str.isEmpty()){
			char[] chars = new char[size];
			for(int i=0;i<size;i++){
				chars[i] = padChar;
			}
			return new String(chars);
		}
		int length = str.length();
		if(length >= size){
			return str;
		}
		return StringUtils.leftPad(str, size, padChar);
	}
	
	public static boolean versionBiggerThan(String version1, String version2){
		String[] parts1 = nonNull(version1).split(".");
		String[] parts2 = nonNull(version2).split(".");
		for(int i=0;i<parts1.length && i<parts2.length;i++){
			int part1 = MathUtil.parseInt(parts1[i]);
			int part2 = MathUtil.parseInt(parts2[i]);
			if(part1 > part2){
				return true;
			} else if(part1 < part2){
				return false;
			}
		}
		return false;//equals
	}
}
