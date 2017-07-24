package com.zfysoft.platform.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zfysoft.common.util.StringUtil;
import com.zfysoft.platform.cache.CommonCDCache;
import com.zfysoft.platform.model.ResultData;
import com.zfysoft.platform.model.ZdGroup;
import com.zfysoft.platform.model.ZdItem;
import com.zfysoft.platform.service.ZdGroupService;
import com.zfysoft.platform.service.ZdItemService;

/**
 * 2014-8-5 重新构造的字典 相关请求操作
 * @author Administrator
 */
@Controller
@RequestMapping("/zd/*")
public class ZdGroupItemController {
	
	private static Logger logger = Logger.getLogger(ZdGroupItemController.class);
	
	@Resource
	private ZdGroupService zdGroupService;
	@Resource
	private ZdItemService zdItemService;
	/**
	 * 去字典的页面跳转
	 * @param request
	 * @return
	 */
	@RequestMapping("/goZdGroupItemPage.do")
	public String goZdGroupItemPage(HttpServletRequest request){
		return "platform/zd/zd_group_item_page";
	}
	/**
	 * 构造字典分组树
	 * @param request
	 */
	@RequestMapping("/getZdGroupTree.do")
	public void getZdGroupTree(HttpServletRequest request,HttpServletResponse response){
		
		List<ZdGroup> zdGroups = zdGroupService.queryZdGroupList();
		
		/*构造tree*/
		Document document = DocumentHelper.createDocument(); // 创建文档
		Element tree = document.addElement("tree");  
		tree.addAttribute("id","0");
		Element root = tree.addElement("item");//根节点
		root.addAttribute("id", "zd");
		root.addAttribute("text", "字典");
		
		for(ZdGroup zdg1:zdGroups){
			/*迭代没有父节点*/
			if(StringUtil.isEmptyOrNull(zdg1.getParentCode())){
				Element item = root.addElement("item");
				item.addAttribute("id", zdg1.getGroupCode());
				item.addAttribute("text", zdg1.getGroupLabel());
				for(ZdGroup zdg2:zdGroups){
					/*再迭代该节点是否有子节点*/
					if(zdg1.getGroupCode().equals(zdg2.getParentCode())){
						Element itemChildren =  item.addElement("item");
						itemChildren.addAttribute("id", zdg2.getGroupCode());
						itemChildren.addAttribute("text", zdg2.getGroupLabel());
					}
				}
			}
		}
		
		PrintWriter out = null;
		response.setHeader("Pragma", "No-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setContentType("text/xml;charset=utf-8");
		try {
			out = response.getWriter();
		} catch (IOException e) {
			e.printStackTrace();
		}
		out.print(document.asXML());
	}
	/**
	 * 根据分组code获得对应的字典列表
	 * @param request
	 * @param groupCode
	 * @return
	 */
	@RequestMapping("/getZdItems.do")
	public String getZdItems(HttpServletRequest request,String groupCode,ZdItem zdItem){
		List<ZdItem> zdItems = zdItemService.getZdItems(groupCode,zdItem);
		request.setAttribute("groupCode", groupCode);
		request.setAttribute("zdItem", zdItem);
		request.setAttribute("zdItems", zdItems);
		/*判断是否有父分组数据*/
		ZdGroup parentZdGroup = zdGroupService.getZdGroup(groupCode);
		List<ZdItem> parentZdItems = zdItemService.getParentZdItems(groupCode);
		if(StringUtil.isNotEmptyOrNull(parentZdGroup)&&StringUtil.isNotEmptyOrNull(parentZdItems)){
			request.setAttribute("parentZdGroup", parentZdGroup);
			request.setAttribute("parentZdItems", parentZdItems);
		}
		return "platform/zd/zd_item_list";
	}
	/**
	 * 新增、修改、查看页面跳转
	 * @param request
	 * @return
	 */
	@RequestMapping("pageForwordItem.do")
	public String pageForwordItem(HttpServletRequest request, String groupCode, String operation,Long id){
		/*绑定分组*/
		request.setAttribute("groupCode", groupCode);
		/*找到是否有父分组信息*/
		ZdGroup parentZdGroup = zdGroupService.getZdGroup(groupCode);		
		if(StringUtil.isNotEmptyOrNull(parentZdGroup)){
			request.setAttribute("zdGroup", parentZdGroup);
		}
	
		if(StringUtil.isEmptyOrNull(id)){
			return "platform/zd/zd_item_edit";
		}else{
			ZdItem zdItem = zdItemService.getZdItem(groupCode,id);
			request.setAttribute("zdItem", zdItem);
			if(operation.equalsIgnoreCase(ResultData.OPT_UPDATE)){
				return "platform/zd/zd_item_edit";
			}else{
				return "platform/zd/zd_item_view";
			}
		}
	}
	/**
	 * 新增、修改字典信息
	 * @param request
	 * @param zdItem
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/saveOrUpdateZdItem.do")
	public ResultData saveOrUpdateZdItem(HttpServletRequest request,ZdItem zdItem){
		/**
		 * 验证该字段名称是否重复
		 */
		ZdItem newZdItem = zdItemService.getZdItem(zdItem.getZdGroupCode(),zdItem.getLabel(),zdItem.getId());
		if(newZdItem!=null){
			return new ResultData(ResultData.ERROR,"名称重复!");
		}
		try {
			/*新增时确保字典code有值*/
			if(StringUtil.isEmptyOrNull(zdItem.getCode())){
				zdItem.setCode(zdItem.getLabel());
			}
			zdItemService.saveOrUpdateZdItem(zdItem);
			CommonCDCache.reloadAllItem();
			return new ResultData(ResultData.SUCCESS,"保存成功!","",zdItem);
		} catch (Exception e) {
			logger.error("保存字典item失败", e);
			return new ResultData(ResultData.ERROR,"保存失败!");
		}
	}
	/**
	 * 删除字典信息
	 * @param request
	 * @param zdItem
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/delZdItem.do")
	public ResultData delZdItem(HttpServletRequest request,Long id){
		/**
		 * 验证该节点是否可以被删除
		 */
		ZdItem zdItem = zdItemService.getZdItem(id);
		if("0".equalsIgnoreCase(zdItem.getAllowdel())){
			return new ResultData(ResultData.ERROR,"不允许删除!");
		}
		try {
			zdItemService.deleteZdItem(id);
			return new ResultData(ResultData.SUCCESS,"删除成功!",ResultData.OPT_DELETE,id);
		} catch (Exception e) {
			logger.error("删除字典item失败", e);
			return new ResultData(ResultData.ERROR,"删除失败!");
		}
	}
}
