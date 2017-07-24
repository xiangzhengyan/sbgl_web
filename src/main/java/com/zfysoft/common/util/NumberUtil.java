/**
 * 版权所有 2013 成都子非鱼软件有限公司 保留所有权利
 * 1 项目签约客户只拥有对项目业务代码的所有权，以及在本项目范围内使用平台框架
 * 2 平台框架及相关代码属子非鱼软件有限公司所有，未经授权不得扩散、二次开发及用于其它项目
 */
package com.zfysoft.common.util;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Map;
import java.util.Random;

/**
 * @author hudt
 * @date 2013-8-20
 */
public class NumberUtil {
	/**
	 * 格式化位保几小数位的数字
	 * @param number要格式化的数字
	 * @param scale要保留的经度
	 * @return
	 */
	public static Double getDoubleByScale(Double number,int scale){
		/*NumberFormat ddf1=NumberFormat.getNumberInstance(); 	      
	    ddf1.setMaximumFractionDigits(scale);   	      
	    String s= ddf1.format(number);  	      
	    double  Num=Double.parseDouble(s); */  	      
	    //return Num;   
		BigDecimal bg = new BigDecimal(number);
		double f1 = bg.setScale(scale, BigDecimal.ROUND_HALF_UP).doubleValue();
		return f1;
	}
	/**
	 * 产生一个start到(size+start)随机范围的数字 不包括(size+start)
	 * @param size
	 * @param start
	 * @return
	 */
	public static Integer getRandomNumber(Integer size,Integer start){
		if(start==null){
			start = 0;
		}
		Random rand = new Random();
		return rand.nextInt(size)+start;
	}
	/**
	 * 产生0到size范围的数字 不包括size
	 * @param size
	 * @return
	 */
	public static Integer getRandomNumber(Integer size){
		return getRandomNumber(size,null);
	}
	public static String doubleToString(Double d){
		return doubleToString(d,null);
	}
	public static String doubleToString(Double d,Integer n){
		if(d==null){
			return null;
		}
		DecimalFormat df = null;
		if(n!=null){
			String fmt = ".";
			for(int i = 0 ; i < n ;i ++){
				fmt += "#";
			}
			df=new DecimalFormat(fmt);
			return df.format(d);
		}else{
			df=new DecimalFormat();
			return df.format(d).replaceAll(",", "");
		}
	}
	
	public static void main(String[] args) {
		System.out.println(doubleToString(9.99999999999999E11));
	}
	/**
	 * 判断该数字型字符串是否等于0
	 * @param s
	 * @return
	 */
	public static boolean isEquals0(Object s){
		if(s==null||s.toString().trim().equals("")){
			return true;
		}
		try {//可能不是数字 try
			BigDecimal b = new BigDecimal(s.toString());
			BigDecimal bb = new BigDecimal(0);
			if(b.compareTo(bb)==0){
				return true;
			}
		} catch (Exception e) {
			
		}
			
		return false;
	}
	public static boolean isNotEquals0(Object s){
		return !isEquals0(s);
	}
	/**
	 * 计算map的value的和值
	 * @param map
	 * @return
	 */
	public static BigDecimal getMapOfValueSum(Map<String,String> map){
		BigDecimal sum = new BigDecimal(0);
		if(StringUtil.isEmptyOrNull(map)||ArrayUtil.mapAllValueIsEmptyOrNullOr0(map)){
			return sum;
		}
		for(String key:map.keySet()){
			try{
				sum = sum.add(new BigDecimal(map.get(key)));
			} catch(Exception e){
				
			}
		}
		return sum;
	}
}
