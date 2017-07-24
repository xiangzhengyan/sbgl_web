package com.zfysoft.platform.util;

import javax.servlet.http.HttpServletRequest;

import com.zfysoft.platform.model.User;

/**
 * @author xiangzy
 * @date 2015-9-8
 *
 */
public class SessionUtil {
	public static Long getUserId(HttpServletRequest request){
		User loginUser = (User) request.getSession().getAttribute("loginUser");
		if(loginUser!=null){
			return loginUser.getId();
		}
		return null;
	}
}
