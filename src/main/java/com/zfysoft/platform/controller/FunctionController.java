/**
 * 版权所有 2013 成都子非鱼软件有限公司 保留所有权利
 * 1 项目签约客户只拥有对项目业务代码的所有权，以及在本项目范围内使用平台框架
 * 2 平台框架及相关代码属子非鱼软件有限公司所有，未经授权不得扩散、二次开发及用于其它项目
 */
package com.zfysoft.platform.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zfysoft.common.util.StringUtil;
import com.zfysoft.common.util.XmlViewHelper;
import com.zfysoft.platform.model.Function;
import com.zfysoft.platform.model.FunctionAction;
import com.zfysoft.platform.model.FunctionVo;
import com.zfysoft.platform.model.ResultData;
import com.zfysoft.platform.model.TreeNode;
import com.zfysoft.platform.model.User;
import com.zfysoft.platform.service.FunctionService;

/**
 * 菜单维护controller
 * @author chenhm
 * @date 2013-7-18
 */
@Controller
@RequestMapping("/function/*")
public class FunctionController {

	private static Logger logger = Logger.getLogger(FunctionController.class);  
	
	@Resource
	private FunctionService functionService;
	
	//本地化工具类
	Locale local = Locale.getDefault(); //如果是中文系统,得到的是zh_CN
	//Locale local = new Locale("en", "US");
	//将properties文件转存为键值对存储
	ResourceBundle rb_function = ResourceBundle.getBundle("com/zfysoft/platform/local/base/i18n_function", local, FunctionController.class.getClassLoader());
	
	@RequestMapping(value = "/function/queryFunctionList.do")
	public String queryFunctionList(HttpServletRequest request) {
		
		// 查询子系统
		User loginUser = (User) request.getSession().getAttribute("loginUser");
		List<Function> apps = functionService.getAuthAppsByUser(loginUser);
		List<FunctionVo> appList = new ArrayList<FunctionVo>();
		for (Function function : apps) {
			FunctionVo functionVo = new FunctionVo();
			functionVo.setId(function.getId());
			functionVo.setLabel(function.getLabel());
			functionVo.setUrl(function.getUrl());
			functionVo.setMenuIndex(function.getMenuIndex()==null?new Long(1):function.getMenuIndex());
			appList.add(functionVo);
		}
		
		// 获取子系统ID
		Long appId = appList.get(0).getId();
		String appName = appList.get(0).getLabel();
			
		request.setAttribute("appId", appId);
		request.setAttribute("appName", appName);
		request.setAttribute("apps", appList);
		
		return "platform/function/function_list";
	}
	
	/**
	 * 根据id查询菜单详细信息
	 * @param request
	 * @param functionId
	 * @return
	 */
	@RequestMapping(value = "/viewFunction.do")
	@ResponseBody
	public ResultData queryFunctionInfo(HttpServletRequest request, String functionId) {
		
		if (functionId.indexOf("app_") != -1) {
			functionId = functionId.substring(4);
		}
		Function func = functionService.getFunctionById(new Long(functionId), null);
		//request.setAttribute("func", func);
		
		return new ResultData(ResultData.SUCCESS, null, null, func);
	}
	
	/**
	 * 实现页面跳转
	 * @param request
	 * @param parentId
	 * @param functionId
	 * @return
	 */
	@RequestMapping("/pageForward.do")
	@ResponseBody
	public ResultData pageForward(HttpServletRequest request, String parentId, String functionId) {
		
		String errMsg = "";
		if (functionId == null && parentId == null) {
			errMsg = rb_function.getString("platform.function.nonode");
			return new ResultData(ResultData.ERROR,errMsg);
		}
		
		if (functionId == null && parentId != null) {
			// 新建菜单
			Function parentFunc = new Function();
			if (parentId.indexOf("app_") != -1) {
				// 子系统下面新建菜单
				parentId = parentId.substring(4);
				Function appFunc = functionService.getFunctionById(new Long(parentId), null);
				parentFunc.setApp(appFunc);
			} else {
				parentFunc = functionService.getFunctionById(new Long(parentId), null);
			}
			request.setAttribute("parentFunc", parentFunc);
			return new ResultData(ResultData.SUCCESS, null, null, parentFunc);
		} else if (parentId == null && functionId != null) {
			// 修改菜单
			if (functionId.indexOf("app_") != -1) 
				functionId = functionId.substring(4);
			Function func = functionService.getFunctionById(new Long(functionId), null);
			request.setAttribute("function", func);
			return new ResultData(ResultData.SUCCESS, null, null, func);
		}
		return null;
	}
	
	/**
	 * 保存菜单
	 * @param request
	 * @param func
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/saveFunc.do")
	public ResultData saveFunc(HttpServletRequest request, FunctionVo func) {
		
		String errMsg = "";
		String operation = "";
		if (func == null || func.getLabel() == null || "".equals(func.getLabel())) {
			errMsg = rb_function.getString("platform.function.nameIsEmpty");
			return new ResultData(ResultData.ERROR,errMsg);
		} else if (func.getUrl() == null || "".equals(func.getUrl())) {
			errMsg = rb_function.getString("platform.function.urlIsEmpty");
			return new ResultData(ResultData.ERROR,errMsg);
		}
		
		if (func.getId() == null || "".equals(func.getId().toString().trim())) {
			// 新增保存
			operation = ResultData.OPT_ADD;
			Function function = functionService.addFunction(func);
			
			TreeNode node = new TreeNode();
			if (function.getParent() == null)
				node.setParentNodeId("app_" + function.getApp().getId());
			else 
				node.setParentNodeId(function.getParent().getId().toString());
			node.setNodeId(function.getId().toString());
			node.setNodeName(function.getLabel());
			
			return new ResultData(ResultData.SUCCESS, null, operation, node);
		} else {
			// 修改更新
			operation = ResultData.OPT_UPDATE;
			if (func.getId().toString().indexOf("app_") != -1)
				func.setId(new Long(func.getId().toString().substring(4)));
			Function function = functionService.updateFunction(func);
			
			TreeNode node = new TreeNode();
			if (function.getApp() == null)
				node.setNodeId("app_" + function.getId());
			else 
				node.setNodeId(function.getId().toString());
			node.setNodeName(function.getLabel());
			
			return new ResultData(ResultData.SUCCESS, null, operation, node);
		}
	}
	
	/**
	 * 删除菜单
	 * @param request
	 * @param functionId
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/delFunc.do")
	public ResultData deleteFunc(HttpServletRequest request, Long functionId) {
		try {
			// 删除菜单，同时删除该菜单下面的子菜单和相关联的数据
			//functionService.deleteFunction(functionId);
			
			// 删除子菜单
			List<Long> funcIdList = functionService.getSubFuncId(functionId);
			funcIdList.add(functionId);
			for (Long id : funcIdList) {
				System.out.println("id = " + id);
				
				// 删除角色菜单关联数据
				functionService.deleteRoleFunc(id);
				
				// 删除机构菜单关联数据
				functionService.deleteOrgFunc(id);
				
				// 删除收藏夹相关联的数据
				User loginUser = (User) request.getSession().getAttribute("loginUser");
				functionService.deleteFavFunction(loginUser.getId(), id);
				
				// 删除菜单
				functionService.deleteFunction(id);
			}
			
			TreeNode node = new TreeNode();
			node.setNodeId(functionId.toString());
			return new ResultData(ResultData.SUCCESS,null, null,node);
			
		} catch (Exception e) {
			logger.error("删除菜单失败", e);
			return new ResultData(ResultData.ERROR,"删除菜单失败", null,e);
		}
	}
	
	/**
	 * 根据parantId查询菜单树
	 * @param request
	 * @param appId
	 * @param roleId
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/functionTree.do")
	public void getFunctionTree(HttpServletRequest request, HttpServletResponse response, Long parentId, Long roleId, String opp) {
		
		List<Long> list = new ArrayList<Long>();
		if (StringUtil.isNotEmptyOrNull(opp) && "auth".equals(opp)) {
			List<Function> funcs = (List<Function>) request.getSession().getAttribute("functionList");
			for (Function func : funcs) {
				list.add(func.getId());
			}
		}
		
		try {
			XmlViewHelper.print(response, functionService.buildFunctionTree(parentId, roleId, list));
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 根据菜单id查询action
	 * @param request
	 * @param functionId
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/getAction.do")
	public ResultData getFunctionActionByFunctionId(HttpServletRequest request, Long functionId) {
		try {
			List<FunctionAction> list = functionService.getFunctionActionByFucntionId(functionId);
			Map<String, List<FunctionAction>> map = new HashMap<String, List<FunctionAction>>();
			map.put("list", list);
			return new ResultData(ResultData.SUCCESS,null, null, map);
		} catch (Exception e) {
			logger.error("获取按钮权限失败", e);
			return new ResultData(ResultData.ERROR,"获取按钮权限失败", null,e);
		}
	}
	
	@ResponseBody
	@RequestMapping("/getActionById.do")
	public ResultData getActionById(HttpServletRequest request, Long actionId) {
		try {
			if (actionId == null) {
				String errMsg = rb_function.getString("platform.function.nameIsEmpty");
				return new ResultData(ResultData.ERROR,errMsg);
			}
			FunctionAction action = functionService.getActionById(actionId);
			return new ResultData(ResultData.SUCCESS, null, null, action);
		} catch (Exception e) {
			logger.error("获取按钮权限对象时失败", e);
			return new ResultData(ResultData.ERROR,"获取按钮权限对象时失败", null,e);
		}
	}
	
	/**
	 * 保存action
	 * @param request
	 * @param id
	 * @param label
	 * @param url
	 * @param parent_id
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/saveAction.do")
	public ResultData saveAction(HttpServletRequest request, Long id, String label, String url, Long parent_id) {
		try{
			String operation = ResultData.OPT_ADD;
			String errMsg ="";
			if (label == null || "".equals(label.trim())) {
				errMsg = rb_function.getString("platform.function.nameIsEmpty");
				return new ResultData(ResultData.ERROR,errMsg);
			}
			if (url == null || "".equals(url.trim())) {
				errMsg = rb_function.getString("platform.function.urlIsEmpty");
				return new ResultData(ResultData.ERROR,errMsg);
			}
			
			FunctionAction action = new FunctionAction();
			if (id != null) {
				operation = ResultData.OPT_UPDATE;
				action.setId(id);
			}
			action.setLabel(label);
			action.setCode(url);
			action.setFunction(functionService.getFunctionById(parent_id, null));
			
			action = functionService.saveOrUpdate(action);
			return new ResultData(ResultData.SUCCESS, null, operation, action);
		}catch(Exception e){
			logger.error("保存按钮权限失败", e);
			return new ResultData(ResultData.ERROR,"保存按钮权限失败", null,e);
		}
	}
	
	/**
	 * 批量删action
	 * @param request
	 * @param ids
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/deleteAction.do")
	public ResultData deleteAction(HttpServletRequest request, String ids) {
		try {
			String[] strs = ids .split(",");
			List<Long> list= new ArrayList<Long>();
			for(int i = 0 ;i <strs.length ; i++){
				list.add(new Long(strs[i]));
				functionService.deleteActions(new Long(strs[i]));
			}
			Map<String, List<Long>> map = new HashMap<String, List<Long>>();
			map.put("list", list);
			return new ResultData(ResultData.SUCCESS, null, null, map);
		} catch (Exception e) {
			logger.error("删除按钮权限失败", e);
			return new ResultData(ResultData.ERROR,"删除按钮权限失败", null,e);
		}
		
	}
}
