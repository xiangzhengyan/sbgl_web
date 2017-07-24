package com.zfysoft.platform.controller;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zfysoft.common.util.JsonUtil;
import com.zfysoft.platform.model.Function;
import com.zfysoft.platform.model.FunctionVo;
import com.zfysoft.platform.model.User;
import com.zfysoft.platform.service.FunctionService;
import com.zfysoft.platform.service.OrganizationService;
import com.zfysoft.platform.service.RoleService;

@Controller
public class MainController {
	
	private static Logger logger = Logger.getLogger(MainController.class);
	
	@Resource
	private FunctionService functionService;
	
	@Resource
	private RoleService roleService;
	
	@Resource
	private OrganizationService orgService;
	
	/**
	 * 生成子系统和菜单
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/main.do")
	public String mainPage(HttpServletRequest request, HttpServletResponse response,String target) {
		
		User loginUser = (User) request.getSession().getAttribute("loginUser");
		String defautUrl = "/cxgis/home.do";
		String title = "";
		//target = (target!=null && (target.trim().equals("adminlte") || target.trim().equals("newmain")))?target.trim():"newmain";
		target = (target==null?"main_single":target);
		request.getSession().setAttribute("target", target);
		/**首页按角色控制**/
		String urlTmp = roleService.queryHomePage(loginUser);
		if(urlTmp!=null && !urlTmp.trim().equals("")){
			defautUrl = urlTmp;
		}
		/** 权限控制 **/
		// 根据用户id查询用户拥有的菜单
		List<Function> functionList = functionService.getFunctionsByUserId(loginUser.getId());
		if(functionList!=null && functionList.size()>0){
			List<FunctionVo> appList = new ArrayList<FunctionVo>();
			List<FunctionVo> firstLevel = new ArrayList<FunctionVo>();
			for (Function function : functionList) {
				FunctionVo functionVo = new FunctionVo();
				functionVo.setId(function.getId());
				if(function.getParent() != null){
					functionVo.setParentId(function.getParent().getId());
				}
				functionVo.setLabel(function.getLabel());
				functionVo.setUrl(function.getUrl());
				functionVo.setIcoName(function.getIcoName());
				functionVo.setMenuIndex(function.getMenuIndex()==null?new Long(1):function.getMenuIndex());
				appList.add(functionVo);
				if(function.getMenuLevel() == 1L && function.getApp()!=null){
					firstLevel.add(functionVo);
				}
			}
			String appJson = JsonUtil.list2json(appList);
			String firstlevelJson = JsonUtil.list2json(firstLevel);
			request.setAttribute("json", appJson);
			request.setAttribute("firstlevelJson", firstlevelJson);
			request.setAttribute("firstLevel", firstLevel);
			request.getSession().setAttribute("functionList", functionList);
			for(FunctionVo firstFloor : firstLevel){
				buildeTree(appList, firstFloor);
			}
		}
		defautUrl = request.getParameter("url")==null?defautUrl:request.getParameter("url");
		title = request.getParameter("title")==null?title:request.getParameter("title");
		request.setAttribute("title", title);
		request.setAttribute("url", defautUrl);
		return "platform/portal/"+target;
	}
	
	private void buildeTree(List<FunctionVo> allList, FunctionVo parent){
		if(allList == null || parent == null){
			return;
		}
		for(FunctionVo function : allList){
			if(function.getParentId() == parent.getId()){
				parent.getChildren().add(function);
				buildeTree(allList,function);
			}
		}
	}
	
	/**
	 * 跳转到home.jsp
	 * @return
	 */
	@RequestMapping("/home.do")
	public String homePage(HttpServletRequest request,HttpServletResponse response){
		return "platform/portal/home";
	}
	
	/**
	 * 查询收藏夹中菜单的所有父级菜单名
	 * @param request
	 * @param functionId
	 * @return
	 */
	@RequestMapping(value = "/getFavParentName.do", produces = {"text/javascript;charset=UTF-8"})
	@ResponseBody
	public String getFavParentName(HttpServletRequest request, Long functionId) {
		
		// 查询所在子系统
		String appName = functionService.getAppNameByFunctionId(functionId);
		// 查询所有父级
		String name = functionService.getFavParentName(functionId);
		String str = "$a$"+ appName +"$/a$$s$" + name;
		
		return str; 
	}
	
	/**
	 * 查看指定菜单是否已经被收藏
	 * @param request
	 * @param functionId
	 * @return
	 */
	@RequestMapping("/isCollect.do")
	@ResponseBody
	public boolean isCollect(HttpServletRequest request, Long functionId) {
		
		User loginUser = (User) request.getSession().getAttribute("loginUser");
		boolean isCollect = functionService.isCollect(loginUser.getId(), functionId);
		
		return isCollect;
	}
	
	/**
	 * 收藏菜单
	 * @param request
	 * @param functionId
	 */
	@RequestMapping(value = "/collect.do", produces = {"text/javascript;charset=UTF-8"})
	@ResponseBody
	public String collect(HttpServletRequest request, Long functionId) {
		
		User loginUser = (User) request.getSession().getAttribute("loginUser");
		functionService.addFavFunction(loginUser.getId(), functionId);
		
		List<Long> functionIdList = new ArrayList<Long>();
		List<Long> roleIdList = roleService.getRoleIdByUser(loginUser);
		if (roleIdList.size() > 0) {
			// 根据角色列表查询拥有的菜单和所在部门拥有的菜单
			functionIdList = functionService.getFunctionIdByRole(roleIdList);
		}
		List<Function> funcList = orgService.getOrgaFunListByUserId(loginUser.getId());
		for (Function func : funcList) {
			if (functionIdList.contains(func.getId()))
				continue;
			else 
				functionIdList.add(func.getId());
		}
		if (functionIdList.size() > 0) {
			List<FunctionVo> favList = functionService.getFavFunctionByAppIdAndIdList(null, functionIdList, loginUser.getId());
			String favJson = JsonUtil.list2json(favList);
			return favJson;
		} else {
			return "";
		}
	}
	
	/**
	 * 取消收藏
	 * @param request
	 * @param functionId
	 */
	@RequestMapping(value = "/unCollect.do", produces = {"text/javascript;charset=UTF-8"})
	@ResponseBody
	public String unCollect(HttpServletRequest request, Long functionId) {
		
		User loginUser = (User) request.getSession().getAttribute("loginUser");
		functionService.deleteFavFunction(loginUser.getId(), functionId);
		
		List<Long> functionIdList = new ArrayList<Long>();
		List<Long> roleIdList = roleService.getRoleIdByUser(loginUser);
		if (roleIdList.size() > 0) {
			// 根据角色列表查询拥有的菜单和所在部门拥有的菜单
			functionIdList = functionService.getFunctionIdByRole(roleIdList);
		}
		List<Function> funcList = orgService.getOrgaFunListByUserId(loginUser.getId());
		for (Function func : funcList) {
			if (functionIdList.contains(func.getId()))
				continue;
			else 
				functionIdList.add(func.getId());
		}
		if (roleIdList.size() > 0) {
			// 根据角色列表查询拥有的菜单
			List<FunctionVo> favList = functionService.getFavFunctionByAppIdAndIdList(null, functionIdList, loginUser.getId());
			String favJson = JsonUtil.list2json(favList);
			return favJson;
		} else {
			return null;
		}
	}
	
	/**
	 * 切换布局
	 * @param request
	 * @param target
	 * @param functionId
	 */
	@RequestMapping("/tabSys.do")
	public String tabSys(HttpServletRequest request, String target) {
		HttpSession session = request.getSession();
		if (target == null) {
			Object mode = session.getAttribute("target");
			if (mode == null) {
				session.setAttribute("target", "index");
				return "redirect:main.do?target=index";
			} else {
				if (mode.toString().trim().equals("index")) {
					session.setAttribute("target", "main");
					return "redirect:main.do?target=main";
				} else if (mode.toString().trim().equals("main")) {
					session.setAttribute("target", "index");
					return "redirect:main.do?target=index";
				}
			}
		} else {
			return "redirect:main.do?target=" + target;
		}
		return "redirect:main.do?target=index";
	}
	
	@RequestMapping("/xxx.do")
	public String xxx(HttpServletRequest request,String target){
		
		return "platform/portal/xxx";
	}
	
}
