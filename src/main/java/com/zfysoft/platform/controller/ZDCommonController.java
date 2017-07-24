package com.zfysoft.platform.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zfysoft.common.util.StringUtil;
import com.zfysoft.platform.cache.CommonCDCache;
import com.zfysoft.platform.model.ResultData;
import com.zfysoft.platform.model.ZdItem;

/**
 * 字典
 */
@Controller
@RequestMapping("/zdcommon/*")
public class ZDCommonController {
	
	private static Logger logger = Logger.getLogger(ZDCommonController.class);
		
	@RequestMapping("/query.do")
	@ResponseBody
	public ResultData query(HttpServletRequest request,String groupCode,String parentCode){
		try {
			Map<String, String> map = new HashMap<String,String>();
			if(StringUtil.isEmptyOrNull(parentCode)){
				map = CommonCDCache.getCodesAndLabels(groupCode);
			}else{
				map = CommonCDCache.getCodesAndLabels(groupCode,parentCode);
			}
			if(map != null){
				return new ResultData(ResultData.SUCCESS,null,null,map);
			}else{
				return new ResultData(ResultData.ERROR,"未定义"+groupCode);
			}
		} catch (Exception e) {
			logger.error("查询字典失败", e);
			return new ResultData(ResultData.ERROR,"查询字典失败");
		}
		
	}
	
	
	@RequestMapping("/queryList.do")
	@ResponseBody
	public ResultData queryList(HttpServletRequest request,String groupCode){
		try {
			List<ZdItem> rtnlist = CommonCDCache.getListByGroupCode(groupCode);
			return new ResultData(ResultData.SUCCESS,null,null,rtnlist);
		} catch (Exception e) {
			logger.error("查询字典子项列表失败", e);
			return new ResultData(ResultData.ERROR,"查询字典子项列表失败");
		}
	}
	
	
	@RequestMapping("/queryLabel.do")
	@ResponseBody
	public ResultData queryLabel(HttpServletRequest request,String group,String code){
		String label = CommonCDCache.getLabel(group,code);
		if(label != null){
			return new ResultData(ResultData.SUCCESS,null,null,label);
		}else{
			return new ResultData(ResultData.ERROR);
		}
		
	}

}
