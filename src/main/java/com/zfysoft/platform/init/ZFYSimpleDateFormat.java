/**
 * 版权所有 2013 成都子非鱼软件有限公司 保留所有权利
 * 1 项目签约客户只拥有对项目业务代码的所有权，以及在本项目范围内使用平台框架
 * 2 平台框架及相关代码属子非鱼软件有限公司所有，未经授权不得扩散、二次开发及用于其它项目
 */
package com.zfysoft.platform.init;

import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * 默认时间格式yyyy-MM-dd
 * @author hudt
 * @date 2013-8-21
 */
public class ZFYSimpleDateFormat extends SimpleDateFormat {
	
	private static final long serialVersionUID = 3417654315469402858L;

	public ZFYSimpleDateFormat() {
		super();
	}

	public ZFYSimpleDateFormat(String pattern, DateFormatSymbols formatSymbols) {
		super(pattern, formatSymbols);
	}

	public ZFYSimpleDateFormat(String pattern, Locale locale) {
		super(pattern, locale);
	}

	public ZFYSimpleDateFormat(String pattern) {
		super(pattern);
		if(!pattern.equals("yyyy-MM-dd")){
			//默认时间格式yyyy-MM-dd，否则没写好
		}
	}

	@Override
	public Date parse(String source) throws ParseException {
		if(source==null){
			return null;
		}
		//如2013，则直接变成2013-01-01
		if(source.length()==4){
			source += "-01-01";
		}
		//如2013-08或2013-8(lenth为6或7)，则直接变成2013-08-01
		else if((source.length()==7 || source.length()==6) &&source.indexOf("-")==4){
			source += "-01";
		}
		return super.parse(source);
	}

}
