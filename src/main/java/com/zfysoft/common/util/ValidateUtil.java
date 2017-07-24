/**
 * 版权所有 2013 成都子非鱼软件有限公司 保留所有权利
 * 1 项目签约客户只拥有对项目业务代码的所有权，以及在本项目范围内使用平台框架
 * 2 平台框架及相关代码属子非鱼软件有限公司所有，未经授权不得扩散、二次开发及用于其它项目
 */
package com.zfysoft.common.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 验证工具类
 * @author xuhr
   @date 2013-7-11
 *
 */
public class ValidateUtil {
	
	// 判断电话
    public static boolean isTelephone(String phoneNumber) {
        String phone = "0\\d{2,3}-\\d{7,8}";
        Pattern p = Pattern.compile(phone);
        Matcher m = p.matcher(phoneNumber);
        return m.matches();
    }

    // 判断手机号
    public static boolean isMobileNO(String mobile) {
        Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");	//[^4]除了4之外的
        Matcher m = p.matcher(mobile);
        return m.matches();
    }
    
    /**
     * 判断是否为空
     * @param obj为需要验证的参数，
     */
    public static boolean isNullOrEmpty(Object... objs){
    	for(Object obj : objs){
    		if(obj==null || obj.equals("")){
    			return true;
    		}
    	}
    	return false;
    }

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println(isMobileNO("15810222011"));
		System.out.println(isTelephone("010-88888888"));
	}

}
