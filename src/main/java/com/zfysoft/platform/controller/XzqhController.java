package com.zfysoft.platform.controller;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zfysoft.platform.cache.XzqhCache;
import com.zfysoft.platform.model.ResultData;
import com.zfysoft.platform.model.Xzqh;
import com.zfysoft.platform.service.XzqhService;

@Controller
@RequestMapping("/xzqh/*")
public class XzqhController {
	
	private static Logger logger = Logger.getLogger(XzqhController.class);
	
	@Resource
	private XzqhService xzqhService;
	
	@RequestMapping("/list.do")
	public String list(HttpServletRequest request){
		return "platform/xzqh/xzqh_list";
	}
	
	@ResponseBody
	@RequestMapping("/getXzqhMc.do")
	public ResultData getXzqhMc(HttpServletRequest request,String dwdm,String complete){
		try {
			String mc = "";
			if("1".equals(complete)){
				mc = XzqhCache.getCompleteMc(dwdm);
			}else{
				mc = XzqhCache.getMcByDwdm(dwdm);
			}
			return new ResultData(ResultData.SUCCESS,null,null,mc);
		} catch (Exception e) {
			logger.error("获取行政区划名称失败", e);
			return new ResultData(ResultData.ERROR,"获取行政区划名称失败");
		}
	}
	
	@ResponseBody
	@RequestMapping("/getXzqh.do")
	public ResultData getXzqh(HttpServletRequest request,String dwdm){
		try {
			return new ResultData(ResultData.SUCCESS,null,null,XzqhCache.getByDwdm(dwdm));
		} catch (Exception e) {
			logger.error("删除行政区划失败", e);
			return new ResultData(ResultData.ERROR,"删除行政区划失败");
		}
	}
	
	/**
	 * 按名称搜索行政区划
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/search.do")
	public ResultData search(HttpServletRequest request,String mc){
		try {
			return new ResultData(ResultData.SUCCESS,null,null,XzqhCache.search(mc));
		} catch (Exception e) {
			logger.error("搜索行政区划失败", e);
			return new ResultData(ResultData.ERROR,"搜索行政区划失败");
		}
	}
	
	/**
	 * 获取子行政区划
	 * @param request
	 * @param id即为单位代码，dhtmlxtree自动传入item-id
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getSubItem.do" ,produces = "text/html;charset=UTF-8")
	public String getSubItem(HttpServletRequest request,HttpServletResponse response,
			String id, String canSelectLevels, String showLevels){
		try {
			List<Xzqh> list = XzqhCache.getSubsByDwdm(id);
			JSONArray jarr = new JSONArray();
			for(Xzqh xzqh : list){
				JSONObject obj = new JSONObject();
				obj.element("id", xzqh.getDwdm());
				obj.element("text", xzqh.getMc());
				if(showLevels.contains((XzqhCache.getLevel(xzqh.getDwdm()) +1) + "")){
					obj.element("child", xzqh.getChild()+"");
				}else{
					obj.element("child","0");
				}
				if(canSelectLevels.contains(XzqhCache.getLevel(xzqh.getDwdm()) + "")){
					obj.element("im0", "area.gif");
					obj.element("im1", "area.gif");
					obj.element("im2", "area.gif");
				}else{
					obj.element("im0", "areagrey.gif");
					obj.element("im1", "areagrey.gif");
					obj.element("im2", "areagrey.gif");
				}
				jarr.add(obj);
			}
			JSONObject obj = new JSONObject();
			obj.element("id", id);
			obj.element("item", jarr);
			return obj.toString();
		} catch (Exception e) {
			logger.error("动态获取行政区划下拉树失败", e);
			return "";
		}
	}
}
