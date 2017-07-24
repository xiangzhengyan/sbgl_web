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
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zfysoft.common.util.ArrayUtil;
import com.zfysoft.common.util.XmlViewHelper;
import com.zfysoft.platform.model.Function;
import com.zfysoft.platform.model.FunctionAction;
import com.zfysoft.platform.model.FunctionVo;
import com.zfysoft.platform.model.ResultData;
import com.zfysoft.platform.model.Role;
import com.zfysoft.platform.model.User;
import com.zfysoft.platform.service.ActionService;
import com.zfysoft.platform.service.FunctionService;
import com.zfysoft.platform.service.RoleService;
import com.zfysoft.platform.service.UserService;

/**
 * 角色管理
 * @author chenhm
 * @date 2013-7-3
 */
@Controller
@RequestMapping("/role/*")
public class RoleController {

	private static Logger log = Logger.getLogger(RoleController.class);
	
	@Resource
	private RoleService roleService;
	@Resource
	private UserService userService;
	@Resource
	private FunctionService functionService;
	@Resource
	private ActionService btnService;
	
	//本地化工具类
	Locale local = Locale.getDefault(); //如果是中文系统,得到的是zh_CN
	//Locale local = new Locale("en", "US");
	//将properties文件转存为键值对存储
	ResourceBundle rb = ResourceBundle.getBundle("com/zfysoft/platform/local/base/i18n_role", local, RoleController.class.getClassLoader());
	
	/**
	 * 查询角色列表
	 * @return
	 */
	@RequestMapping("/queryRoleList.do")
	public String queryUserList(HttpServletRequest request, String name, String msg) {
		List<Role> roleList = roleService.queryRoleList(name);
		request.setAttribute("roleList", roleList);
		request.setAttribute("msg", msg);
		request.setAttribute("keyWord", name);
		return "platform/role/query_role_list";
	}
	
	/**
	 * 页面跳转
	 * @param request
	 * @return
	 */
	@RequestMapping("/pageForward.do")
	public String pageForward(HttpServletRequest request) {
		
		if (request.getParameter("roleId") == null) {
			//新增角色
			return "platform/role/edit_role";
		} else {
			Long roleId = Long.parseLong((request.getParameter("roleId")));
			Role role = roleService.queryRoleById(roleId);
			request.setAttribute("role", role);
			if (request.getParameter("operate") == null) {
				// 修改角色信息
				return "platform/role/edit_role";
			} else {
				//角色授权
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
				return "platform/role/author_function";
			}
		}
	}
	
	/**
	 * 保存角色
	 * @param request
	 * @param role
	 * @return
	 * @throws Exception 
	 */
	@ResponseBody
	@RequestMapping("/SaveRole.do")
	public ResultData saveRole(HttpServletRequest request, Role role) {
		 
		String operation = null;
		// 验证角色名非空
		if (role.getName() == null || "".equals(role.getName().trim())) {
			return new ResultData(ResultData.ERROR, rb.getString("platform.role.nameIsEmpty"));
		}
		if (role.getCode() == null || "".equals(role.getCode().trim())) {
			return new ResultData(ResultData.ERROR, rb.getString("platform.role.codeIsEmpty"));
		}
		String errMsg;
		if (role.getId() == null) {
			//新增
			// 验证角色名、角色代码不能重复
			if (roleService.queryRoleByName(role.getName(), null)) {
				return new ResultData(ResultData.ERROR, rb.getString("platform.role.nameIsExist"));
			}
			if (roleService.queryRoleByCode(role.getCode(), null)) {
				return new ResultData(ResultData.ERROR, rb.getString("platform.role.codeIsExist"));
			}
			errMsg = roleService.addRole(role);
			operation = ResultData.OPT_ADD;
		} else {
			// 修改
			// 验证角色名、角色代码不能重复
			if (roleService.queryRoleByName(role.getName(), role.getId())) {
				return new ResultData(ResultData.ERROR, rb.getString("platform.role.nameIsExist"));
			}
			if (roleService.queryRoleByCode(role.getCode(), role.getId())) {
				return new ResultData(ResultData.ERROR, rb.getString("platform.role.codeIsExist"));
			}
			errMsg = roleService.updateRole(role);
			operation = ResultData.OPT_UPDATE;
		}
		if(errMsg!=null){
			return new ResultData(ResultData.ERROR,errMsg);
		}else{
			return new ResultData(ResultData.SUCCESS,null,operation,role);
		}
	}
	
	/**
	 * 根据id删除角色<br>
	 * 只更改角色状态为不可用，不做物理删除
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/deleteRole.do")
	public ResultData deleteRole(HttpServletRequest request) {
		
		String errmsg = "";
		String id = request.getParameter("roleId");
		if ( id!= null) {
			Long roleId = new Long(request.getParameter("roleId"));
			errmsg = roleService.deleteRoleById(roleId);
			if(errmsg!=null){
				log.info(errmsg);
				return new ResultData(ResultData.ERROR,errmsg);
			}
		}
		
		return new ResultData(ResultData.SUCCESS, null, ResultData.OPT_DELETE,id);
	}
	
	/**
	 * 根据子系统id查询菜单树
	 * @param request
	 * @param appId
	 * @param roleId
	 */
	@RequestMapping("/functionTree.do")
	public void getFunctionTree(HttpServletRequest request, HttpServletResponse response, Long appId, Long roleId) {
		
		try {
			XmlViewHelper.print(response, roleService.buildFunctionTree(appId, roleId));
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 保存角色授权
	 * @param appId
	 * @param roleId
	 * @param selectedIds
	 * @param functionId
	 * @param btnIds
	 * @return
	 */
	@RequestMapping(value = "/authorize.do", method = RequestMethod.POST)
	@ResponseBody
	public String authorize(Long appId, Long roleId, String selectedIds, String unSelectedIds, Long functionId, String btnIds) {
		selectedIds = selectedIds.replace("app_", "");
		unSelectedIds = unSelectedIds.replace("app_", "");
		try {
			roleService.authorize(appId, roleId, selectedIds, unSelectedIds);
			String sarray[] = btnIds.split(","); 
			if(functionId != null){
				btnService.deleteInRole(roleId, functionId);
				if (btnIds != null && !"".equals(btnIds.trim())) {
					for (String buttonId : sarray) {
						btnService.saveOrUpdateInRole(functionId, roleId, Long.parseLong(buttonId));
					}
				}
			}
			return "1";
		}
		catch (Exception e) {
			log.error("保存角色权限出错",e);
			return "0";
		}
	}
	
	/**
	 * 给某个用户分配角色
	 * @param request
	 * @param userId
	 * @param roleIds
	 * @return
	 */
	@RequestMapping("saveGrantRoleToUser.do")
	@ResponseBody
	public ResultData saveGrantRoleToUser(HttpServletRequest request, Long userId, String roleIds) {
		
		String[] rids = roleIds.split(",");
		Long[] ids = ArrayUtil.strsToLongArray(rids);
		roleService.grantRoleToUser(userId, ids);
		return new ResultData(ResultData.SUCCESS,"",ResultData.OPT_UPDATE);
	}
	
	/**
	 * 查询指定菜单下的按钮和超链接
	 * @param request
	 * @param functionId
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping("/querybtn.do")
	@ResponseBody
	public ResultData queryButton(HttpServletRequest request, Long functionId, Long roleId) {
		
		if (functionId == null) 
			return new ResultData(ResultData.ERROR, rb.getString("platform.role.funcIdIsEmpty"));
		// 查询所有按钮、超链接
		List<FunctionAction> btnList = btnService.queryByFunId(functionId);
		Map<String, List<FunctionAction>> map = new HashMap<String, List<FunctionAction>>();
		map.put("btnList", btnList);
		if (roleId != null) {
			// 查询已有按钮、超链接
			List<FunctionAction> existList = btnService.queryByRoleIdAndFunId(roleId, functionId);
			map.put("existList", existList);
		} 
		
		return new ResultData(ResultData.SUCCESS, null, null, map);
	}
}
