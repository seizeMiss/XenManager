package main.java.dragon.utils;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public class StringUtils {
	
	public static String double2String(double source){
		String target = "";
		BigDecimal format = new BigDecimal(source);
		target = String.valueOf(format.setScale(0, BigDecimal.ROUND_HALF_UP).intValue());
		return target;
	}
	
	public static boolean isEmpty(Object ...objects){
		boolean flag = false;
		int count = 0;
		for(Object object : objects){
			if(isEmpty(object)){
				count++;
			}
		}
		if(count == objects.length){
			flag = true;
		}
		return flag;
	}
	
	public static boolean isEmpty(Object object){
		boolean flag = false;
		if(object == null){
			flag = true;
		}
		if(object instanceof List){
			flag = ((List)object).size() == 0;
		}
		if(object instanceof String){
			flag = ((String)object).trim().equals("");
		}
		return flag;
	}
	
	public static String generateUUID(){
		UUID uuid = UUID.randomUUID();
		return uuid.toString();
	}
	
	public static String setEncodeString(String str){
		String transformStr = "";
		try {
			transformStr = new String(str.getBytes("iso-8859-1"),"UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return transformStr;
	}
	

}
