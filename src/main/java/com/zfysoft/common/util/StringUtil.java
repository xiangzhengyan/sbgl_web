/**
 * 版权所有 2013 成都子非鱼软件有限公司 保留所有权利
 * 1 项目签约客户只拥有对项目业务代码的所有权，以及在本项目范围内使用平台框架
 * 2 平台框架及相关代码属子非鱼软件有限公司所有，未经授权不得扩散、二次开发及用于其它项目
 */
package com.zfysoft.common.util;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//import net.sourceforge.pinyin4j.PinyinHelper;

/**
 * @author hudt
 * @date 2013-8-20
 */
public class StringUtil {
	
	public static final List<String> regs;
	static{
		regs = new ArrayList<String>();
		regs.add(",");
		regs.add("、");
		regs.add("，");
	}
	
	/*特殊字符*/
	public static final char[]  specialChars = new char[]{
		'\\','/',' '
	};
	
	/*非法字符*/
	public static final char[] illegalChars = new char[]{
		'\\','\'','\"'
	};
	
	public static boolean isEmptyOrNull(String str){
		return (str==null || str.trim().equals(""));
	}
	
	@SuppressWarnings("rawtypes")
	public static boolean isEmptyOrNull(Object obj){
		if(obj == null){
			return true;
		}
		if(obj instanceof Collection){
			return ((Collection)obj).size() == 0;
		}else if (obj instanceof Map){
			return ((Map)obj).size() == 0;	
		} else{
			return obj.toString().trim().equals("");
		}
	}
	
	public static boolean isNotEmptyOrNull(String str){
		return !isEmptyOrNull(str);
	}
	
	public static boolean isNotEmptyOrNull(Object str){
		return !isEmptyOrNull(str);
	}
	
	public static boolean isLong(String str){
		Pattern pattern = Pattern.compile("[0-9]*");
		Matcher matcher = pattern.matcher(str);
		return matcher.matches();
	}
	
	public static boolean isDouble(String str){
		try{
			Double.parseDouble(str);
			return true;
		}catch(Exception e){
			return false;
		}
	}
	
	public static boolean isDoubleOrNull(String str){
		if(isEmptyOrNull(str)){
			return true;
		}
		try{
			Double.parseDouble(str);
			return true;
		}catch(Exception e){
			return false;
		}
	}
	
	public static boolean isDate(String str,String fmt){
		if(isEmptyOrNull(str)){
			return false;
		}
		try{
			DateUtil.stringToDate(str, fmt);
			return true;
		}catch(Exception e){
			return false;
		}
	}
	
	public static boolean isDateOrNull(String str,String fmt){
		if(isEmptyOrNull(str)){
			return true;
		}
		try{
			DateFormat df = new SimpleDateFormat(fmt);
			df.parse(str);
			return true;
		}catch(Exception e){
			return false;
		}
	}
	
	public static Double toDouble(String str){
		if(isEmptyOrNull(str)){
			return null;
		}
		try{
			Double rtn = Double.parseDouble(str);
			return rtn;
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
	
	public static Double toDouble(Object obj){
		if(isEmptyOrNull(obj)){
			return null;
		}
		try{
			Double rtn = Double.parseDouble(obj.toString());
			return rtn;
		}catch(Exception e){
			return null;
		}
	}
	
	public static Integer toInteger(Object obj){
		if(isEmptyOrNull(obj)){
			return null;
		}
		try{
			Integer rtn = Integer.parseInt(obj.toString());
			return rtn;
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * 转两位小数
	 */
	public static String to2DecimalDouble(Object obj){
		Double source = toDouble(toString(obj));
		if(source == null){
			return null;
		}
		DecimalFormat df=new DecimalFormat("#.##");
		return toString(df.format(source));
	}
	
	/**
	 * 转n位小数
	 */
	public static Double toDecimalDouble(Object obj,int n){
		Double source = toDouble(toString(obj));
		if(source == null){
			return null;
		}
		if(n <= 0){
			return source;
		}
		String fmt = ".";
		for(int i = 0 ; i < n ;i ++){
			fmt += "#";
		}
		DecimalFormat df=new DecimalFormat(fmt);
		return toDouble(df.format(source));
	}
	
	public static Long toLong(String str){
		return isEmptyOrNull(str)?null:Long.parseLong(str);
	}
	
	public static Long toLong(Object obj){
		return isEmptyOrNull(obj)?null:Long.parseLong(obj.toString());
	}
	
	/**
	 * double转百分比
	 */
	public static String toPercent(Double number){
		NumberFormat nFromat = NumberFormat.getPercentInstance();
		String rates = nFromat.format(number);
		return rates;
	}
	
	/**
	 * Object转百分比
	 */
	public static String toPercent(Object number){
		NumberFormat nFromat = NumberFormat.getPercentInstance();
		String rates = nFromat.format(number);
		return toString(rates);
	}
	
	/**
	 * null 2 empty str
	 */
	public static String toString(Object obj){
		return obj == null?"":java.util.regex.Matcher.quoteReplacement(obj + "");
	}
	
	/**
	 * 去掉最后一个xxx字符
	 */
	public static String removeLast(String source,String endSuffix){
		if(StringUtil.isEmptyOrNull(source)){
			return source;
		}
		if(source.endsWith(endSuffix)){
			source = source.substring(0, source.lastIndexOf(endSuffix));
		}
		return source;
	}
	
	public static String generateRandomFilename() {
		String fourRandom = "";
		// 产生4位的随机数(不足4位前加零)
		int randomNum = (int) (Math.random() * 10000);
		fourRandom = randomNum + "";
		int randLength = fourRandom.length();
		if (randLength < 4) {
			for (int i = 1; i <= 4 - randLength; i++)
				fourRandom = fourRandom + "0";
		}
		Calendar calendar = Calendar.getInstance();
		StringBuilder sb = new StringBuilder("");
		sb.append(calendar.get(Calendar.YEAR))
				.append(twoNumbers(calendar.get(Calendar.MONTH) + 1))
				.append(twoNumbers(calendar.get(Calendar.DAY_OF_MONTH)))
				.append(twoNumbers(calendar.get(Calendar.HOUR)))
				.append(twoNumbers(calendar.get(Calendar.MINUTE)))
				.append(twoNumbers(calendar.get(Calendar.SECOND)))
				.append(fourRandom);
		return sb.toString();
	}
	
	private static String twoNumbers(int number) {
		String _number = number + "";
		if (_number.length() < 2) {
			_number = "0" + _number;
		}
		return _number;
	}
	
	/**
	 * 判断数据中是否有','  '、' '，'
	 * @param data
	 * @param regs 标点集合
	 * @return
	 */
	public static String getReg(String data,List<String> regs){
		if(isEmptyOrNull(data)){
			return null;
		}
		for(String reg:regs){
			if(data.indexOf(reg)!=-1){
				return reg;
			}
		}
		return null;
	}
	
//	/**
//	 * 汉子转拼音的首字母
//	 */
//	public static String toPinYinHead(String str){
//        String convert = "";
//        for (int j = 0; j < str.length(); j++) {
//            char word = str.charAt(j);
//            String[] pinyinArray = PinyinHelper.toHanyuPinyinStringArray(word);
//            if (pinyinArray != null) {
//                convert += pinyinArray[0].charAt(0);
//            } else {
//                convert += word;
//            }
//            System.out.println("word:"+word );
//        }
//        return convert.toUpperCase();
//	}
	
	/**
	 * 移除字符串中的特殊字符
	 * @param str
	 * @param c
	 * @return str
	 */
	public static String removeSpecialChar(String str){
		for(char c:specialChars){
			if(str.indexOf(c)>-1){
				str = str.replace(c+"", "");
			}
		}
		return str;
	}
	/**
	 * 去掉字符串空格（包括全角输入） 字符串内部有全角输入的空格替换为半角空格
	 * @param str
	 * @return
	 */
	public static String trim(String str){
		if(str==null){
			return null;
		}
		//0全角空格、1特殊空白
		String[] spaces = new String[]{
				"　"," "	
		};
		String halfAngleSpace = " ";
		String s = str.trim();  //去掉左右半角空格
		/*去掉左右空白*/
		for(String space:spaces){
			while(s.startsWith(space)){
				s = s.substring(1, s.length()).trim();
			}
			while(s.endsWith(space)){
				s = s.substring(0, s.length()-1).trim();
			}
		}
		/*如果左右空白去掉后的字符串已经是空白，直接返回*/
		if(isEmptyOrNull(s)){
			return s;
		}else{
			//判断字符串内部是否还有全角空格
			for(String space:spaces){
				int index = s.indexOf(space);
				if(index>0){
					s = s.replaceAll(space, halfAngleSpace);
				}
			}
			return s;
		}
	}
	
	/**
	 * 获得字符串中的非法字符
	 * @param str
	 * @return
	 */
	public static String getIllegals(String str){ 
		if(isEmptyOrNull(str)){
			return null;
		}
		List<String> illegals = new ArrayList<String>();
		for(char c:illegalChars){
			if(str.indexOf(c)>-1){
				illegals.add(c+"");
			}
		}
		return ArrayUtil.collectionToString(illegals, ",");
	}
	
	/**
	 * 随机生成一个唯一字符
	 * **/
	public static String produceMark() {
		Random ran = new Random();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		String mark = sdf.format(new Date()) + ran.nextInt(1000);
		return mark;
	}
}
