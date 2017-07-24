package com.zfysoft.common.util;

import org.apache.log4j.Logger;



public class CommonUtil {
	
	/**
	 * 日志
	 */
	private static Logger logger = Logger.getLogger(CommonUtil.class);
	
	public static String getWebInfPath(){
		String url = CommonUtil.class.getResource("").getPath().replaceAll("%20", " ");  		
		String path = url.substring(0, url.indexOf("WEB-INF")) + "WEB-INF/";
		logger.info(path);
		return path;
	}
}
