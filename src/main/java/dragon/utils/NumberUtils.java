package main.java.dragon.utils;

import java.math.BigDecimal;

public class NumberUtils {
	/**
	 * 设置存储的单位为GB
	 * @param storageSize
	 * @return
	 */
	public static long formatStorage(long storageSize){
		return storageSize/1024/1024/1024;
	}
	/**
	 * 设置数值为四舍五入
	 * @param total
	 * @param free
	 * @param scal 保留小数的个数
	 * @return
	 */
	public static double computerUsedRate(double total,double free,int scal){
		double target = 0.0d;
		if(total > 0){
			target = ((total-free)/total)*100;
		}
		BigDecimal format = new BigDecimal(target);
		return format.setScale(scal, BigDecimal.ROUND_HALF_UP).doubleValue();
	}
	
	public static double setdoubleScal(double target){
		BigDecimal format = new BigDecimal(target);
		return format.setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();
	}

}
