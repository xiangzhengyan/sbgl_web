package com.zfysoft.platform.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zfysoft.platform.model.ResultData;
import com.zfysoft.platform.model.User;
import com.zfysoft.platform.service.ActionService;
/**
 * 工具栏权限
 */
@Controller
@RequestMapping("/toolbarAction/*")
public class ToolbarActionContorller {
	
	private static Logger logger = Logger.getLogger(ToolbarActionContorller.class);
	
	@Resource
	private ActionService buttonService;

	@RequestMapping(value = "/authorize.do", method = RequestMethod.POST)
	@ResponseBody
	public ResultData authorize(HttpServletRequest request, String url) {
		try {
			if(url.startsWith("/")){
				url = url.substring(1, url.length());
			}
			User loginUser = (User)request.getSession().getAttribute("loginUser");
			List<String> btns = buttonService.queryPermissionButtonCodes(loginUser, url);
			Map<String,Boolean> auth = new HashMap<String, Boolean>();
			for(String code : btns){
				auth.put(code, true);
			}
			return new ResultData(ResultData.SUCCESS,null,null,auth);
		} catch (Exception e) {
			logger.error("查询工具栏权限出错", e);
			return new ResultData(ResultData.ERROR,"查询工具栏权限出错",null,e);
		}
	}

}
