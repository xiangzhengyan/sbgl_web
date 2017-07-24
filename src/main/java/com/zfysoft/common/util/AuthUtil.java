package com.zfysoft.common.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.zfysoft.platform.config.SpringContextHolder;
import com.zfysoft.platform.model.User;
import com.zfysoft.platform.service.ActionService;

public class AuthUtil {
	
	private static ActionService buttonService;
	
	static{
		buttonService = SpringContextHolder.getBean("buttonService");
	}

	/**
	 * 把用户权限压到request
	 * @param request
	 */
	public static void setAuth(HttpServletRequest request) {
		User loginUser = (User)request.getSession().getAttribute("loginUser");
		String url = request.getServletPath();
		url = url.substring(1, url.length());
		List<String> btns = buttonService.queryPermissionButtonCodes(loginUser, url);
		Map<String,Boolean> auth = new HashMap<String, Boolean>();
		for(String code : btns){
			auth.put(code, true);
		}
		request.setAttribute("authJson", JsonUtil.map2json(auth));
		request.setAttribute("auth", auth);
	}
	
	/**
	 * 把用户的名字返回
	 * @param request
	 */
	public static String getUserName(HttpServletRequest request) {
		User loginUser = (User)request.getSession().getAttribute("loginUser");
		if(loginUser==null){
			return "";
		}
		return loginUser.getRealName();
	}

}
