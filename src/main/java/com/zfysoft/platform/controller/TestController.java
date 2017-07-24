package com.zfysoft.platform.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zfysoft.platform.cache.XzqhCache;
import com.zfysoft.platform.model.ResultData;
import com.zfysoft.platform.model.Xzqh;
/**
 * 测试用Controller
 */
@Controller
@RequestMapping("/test/*")
public class TestController {
	
	@RequestMapping("/test.do")
	public String test(HttpServletRequest request){
		return "test";
	}
		@ResponseBody
	@RequestMapping("/xzqh.do")
	public ResultData xzqh(HttpServletRequest request, String parentDwdm, String levels){
		try {
			List<Xzqh> list = null;
			if(levels == null || levels.contains((XzqhCache.getLevel(parentDwdm) + 1) + "")){
				list = XzqhCache.getSubsByDwdm(parentDwdm);
			}
			return new ResultData(ResultData.SUCCESS,null,null,list);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResultData(ResultData.ERROR,e.getMessage(),null,e);
		}
	}

}
