package main.java.dragon.utils;

import java.util.List;
import java.util.UUID;

public class StringUtils {
	
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
	
	

}
