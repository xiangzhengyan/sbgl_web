package com.zfysoft.common.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * 日期工具类
 * @author hudt
 *
 */
public class DateUtil {
	
	private static final Log log = LogFactory.getLog(FileUtils.class);
	
	/**
	 * 日期从String转Date格式
	 * @param strDate 输入日期
	 * @param fmt 日期格式，如"yyyy-MM-dd"
	 * @return
	 */
	public static Date stringToDate(String strDate , String fmt){
		if(strDate==null || strDate.trim().equals("")) 
			return null;
		if(fmt==null){
			if(strDate.length()==4){
				fmt = "yyyy";
			}else if(strDate.indexOf("-")>0 && (strDate.length()==6 || strDate.length()==7)){
				fmt = "yyyy-MM";
			}else if(strDate.length()>7){
				fmt = "yyyy-MM-dd";
			}
		}
		//若是年的话，只能手动添加1月1日
		//if()
		DateFormat df = new SimpleDateFormat(fmt);
		Date date = null;
		try {
			date = df.parse(strDate);
		} catch (ParseException e) {
			e.printStackTrace();
			log.error("日期转换失败stringToDate,strDate:"+strDate+",fmt:"+fmt+"。..."+e);
			date = null;
		}
		return date;
	}
	
	/**
	 * 日期从String转Date格式
	 * @param strDate 输入日期,默认格式yyyy-MM-dd
	 * @return
	 */
	public static Date stringToDate(String strDate){
		return stringToDate(strDate,null);
	}
	
	
	/**
	 * 日期从Date转String格式
	 * @param date 输入日期
	 * @param fmt 日期格式，如"yyyy-MM-dd"
	 * @return
	 */
	public static String dateToString(Date date , String fmt){
		if(date==null) 
			return null;
		if(fmt==null)
			fmt = "yyyy-MM-dd";
		DateFormat df = new SimpleDateFormat(fmt);
		String strDate = null;
		try {
			strDate = df.format(date);
			return strDate;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
	}
	
	/**
	 * 日期从Date转String格式
	 * @param date 输入日期
	 * @param fmt 日期格式，如"yyyy-MM-dd"
	 * @return
	 */
	public static String dateToString(Date date){
		return dateToString(date,null);
	}
	
	public static String getNowYear(){
		Calendar cal = Calendar.getInstance();
	    int year = cal.get(Calendar.YEAR);
	    return year+"";
	}
	
	/**
	 * 根据毫秒数 离现在的最近的一个时间
	 * @param cal 
	 * @param ms
	 * @return
	 */
	public static Date getLastTime(Calendar cal,Integer ms){
		if(cal == null || ms == null){
			return null;
		}
		cal.set(Calendar.MILLISECOND, cal.get(Calendar.MILLISECOND)-ms);
		return cal.getTime();
	}
	
	public static void main(String[] args) {
		Long l = 1410251284576L;
		String format = "yyyy-MM-dd HH:mm:ss";
		System.out.println(dateToString(new Date(l),format));
		System.out.println(stringToDate(dateToString(new Date(l),format),format));
	}
}
