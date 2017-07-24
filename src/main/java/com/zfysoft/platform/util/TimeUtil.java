package com.zfysoft.platform.util;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author xiangzy
 * @date 2015-9-13
 *
 */
public class TimeUtil {
	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	public static Timestamp parseTime (String str){
		if(str==null || str.equals("")){
			return null;
		}
		try {
			Date date = sdf.parse(str);
			return new Timestamp(date.getTime());
		} catch (ParseException e) {
			e.printStackTrace(); 
			return null;
		}
		
	}
	
	public static String getCurrTimeStr(){
		return sdf.format(new Date(System.currentTimeMillis()));
		
	}
}
