package com.zfysoft.platform.controller;

import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zfysoft.common.util.MD5Util;
import com.zfysoft.common.util.Page;
import com.zfysoft.common.util.ValidateUtil;
import com.zfysoft.platform.model.Organization;
import com.zfysoft.platform.model.ResultData;
import com.zfysoft.platform.model.Role;
import com.zfysoft.platform.model.User;
import com.zfysoft.platform.service.OrganizationService;
import com.zfysoft.platform.service.RoleService;
import com.zfysoft.platform.service.UserService;

@Controller
@RequestMapping("/user/*")
public class UserController {
	
	private static Logger logger = Logger.getLogger(UserController.class);
	
	@Resource
	private UserService userService;	//注入用户services
	
	@Resource RoleService roleService;		//注入角色service
	

	@Resource OrganizationService organizationService;
	
	//本地化工具类
	Locale local = Locale.getDefault(); //如果是中文系统,得到的是zh_CN
	//Locale local = new Locale("en", "US");
	//将properties文件转存为键值对存储
	ResourceBundle rb = ResourceBundle.getBundle("com/zfysoft/platform/local/portal/i18n", local, UserController.class.getClassLoader());
	ResourceBundle rb_user = ResourceBundle.getBundle("com/zfysoft/platform/local/base/i18n_user", local, UserController.class.getClassLoader());
	
	/**
	 * 查询用户列表
	 * @param user
	 * @return
	 */
	@RequestMapping("/queryUserList.do")
	public String queryUserList(HttpServletRequest request,User user,Page page){
		User loginUser = (User) request.getSession().getAttribute("loginUser");
		
		Organization org = organizationService.querySingleOrgaByUser(loginUser.getId());
		Page returnpPage = null;
		if(org!=null){
			returnpPage = userService.queryUserList(user,page,org.getCode());
		}else{
			returnpPage  = page;
		}
		request.setAttribute("page", returnpPage);
		request.setAttribute("userInfo", user);
//		JSONArray jsonArray = JSONArray.fromObject(returnpPage);
//		request.setAttribute("jsonArray", jsonArray);
		return "platform/user/query_user_list";
	}
	
	/**
	 * 跳转新增用户界面
	 * @return
	 */
	@RequestMapping("/createUserForward.do")
	public String createUserForward(HttpServletRequest request,User user){
		if (user != null && user.getId() != null) {
			user = userService.getById(user.getId());
			Organization org = organizationService.querySingleOrgaByUser(user.getId());
			request.setAttribute("org", org);
			request.setAttribute("userInfo", user);
			List<Long> roleIds = roleService.getRoleIdByUser(user);
			if(roleIds!=null && roleIds.size()>0){
				Role role = roleService.queryRoleById(roleIds.get(0));
				request.setAttribute("role", role);
			}
		}
		return "platform/user/mgr_user";
	}
	
	/**
	 * 管理用户信息
	 * @param user
	 * @return
	 */
	@RequestMapping("mgrUser.do")
	@ResponseBody
	public ResultData mgrUser(HttpServletRequest request,User user,long orgId,String roleCode){
		boolean validate = true;
		String tipMsg = "";
		String option = "";
		
		if (ValidateUtil.isNullOrEmpty(user.getLoginName())){
			tipMsg = rb_user.getString("platform.user.validate_loginName");
			validate = false;
		}
		
		else if (ValidateUtil.isNullOrEmpty(user.getRealName())) {
			tipMsg = rb_user.getString("platform.user.validate_realName");
			validate = false;
		}
		
		
	    else if (validate && !ValidateUtil.isNullOrEmpty(user.getEmail())  && user.getEmail().indexOf("@") < 0) {
			tipMsg = rb_user.getString("platform.user.validate_email_format");
			validate = false;
		}
				
	    else if (validate && !ValidateUtil.isNullOrEmpty(user.getTelphone()) && !ValidateUtil.isTelephone(user.getTelphone()) && !ValidateUtil.isMobileNO(user.getTelphone())){
			tipMsg = rb_user.getString("platform.user.validate_phone_format");
			validate = false;
		}
		
		Role role = roleService.getRoleByCode(roleCode);
		if(role==null){
			tipMsg ="角色不存在";
			validate = false;
		}
		
		if (validate) {
			
			
			
			Long userId = user.getId();
			Boolean success = null;
			if (userId == null)	{	//新增
				option = ResultData.OPT_ADD;
				user.setStatus(1);
				user.setModifyPassword(1);
				user.setPassword(MD5Util.getMD5("123456"));		//初始化密码12345
				user.setModifyPassword(1);
				success = userService.addNewUser(user);
				organizationService.saveOrgaUser(orgId, user.getId());
				
				roleService.grantRoleToUser(user.getId(), new Long[]{role.getId()});
			} else {	//修改
				option = ResultData.OPT_UPDATE;
				success = userService.modifyUser(user);

				Organization org = organizationService.querySingleOrgaByUser(userId);
				if(org!=null && !org.getId().equals(orgId)){
					organizationService.deleteOrgaUser(userId, org.getId());
					organizationService.saveOrgaUser(orgId, userId);
				}
				
				roleService.grantRoleToUser(user.getId(), new Long[]{role.getId()});
			}
			if(!success){//用户名被占用
				tipMsg = rb_user.getString("platform.user.validate_loginNameIsUsed");
				validate = false;
			}
		} 
		
		//设置返回类型
		if(!validate){
			return new ResultData(ResultData.ERROR, tipMsg, null, user);
		}else{
			return new ResultData(ResultData.SUCCESS, null, option, user);
		}
		
	}
	
	/**
	 * 删除用户信息
	 * @param user
	 * @return
	 */
	@RequestMapping("del.do")
	@ResponseBody
	public ResultData deleteUser(User user){
		userService.deleteUser(user.getId());
		User u = userService.getById(user.getId());
		//return "redirect:/user/queryUserList.do";
		return new ResultData(ResultData.SUCCESS, null, ResultData.OPT_UPDATE, u);
	}
	
	
	/**
	 * 查看用户基本信息
	 * @param request
	 * @param user
	 * @return
	 */
	@RequestMapping("viewUser.do")
	public String viewUser(HttpServletRequest request,User user) {
		user = userService.getById(user.getId());
		request.setAttribute("userInfo", user);
		Organization org = organizationService.querySingleOrgaByUser(user.getId());
		request.setAttribute("org", org);
		return "platform/user/view_user";
	}
	
	
	/**
	 * 跳转到密码修改页面
	 * @param request
	 * @param user
	 * @return
	 */
	@RequestMapping("forwardModifyPwdPage.do")
	public String forwardModifyPwdPage(HttpServletRequest request,User user){
		request.setAttribute("userInfo", userService.getById(user.getId()));
		return "platform/user/edit_pwd";
	}
	
	/**	修改密码
	 * @param request
	 * @param user
	 * @return
	 */
	@RequestMapping("modifyPwd.do")
	@ResponseBody
	public ResultData modifyPwd(HttpServletRequest request,User user){
		String tipMsg = "";
		boolean validate = true;
		//String redirectUrl = "platform/user/edit_pwd";
		if (user.getPassword() == null || "".equals(user.getPassword())) {
			tipMsg = rb_user.getString("platform.user.validate_password");
			validate = false;
		}
		
		if (validate && !user.getPassword().equals(user.getRePassword())) {
			tipMsg = rb_user.getString("platform.user.validate_equal_password");
			validate = false;
		}
		
		if (validate) {
			userService.modifyPwd(user);
			//redirectUrl = "redirect:/user/queryUserList.do";
			return new ResultData(ResultData.SUCCESS);
		} else {
			//request.setAttribute("tipMsg", tipMsg);
			return new ResultData(ResultData.ERROR, tipMsg, ResultData.OPT_UPDATE, user);
		}
		
		
	}
	
	/**
	 * 重置密码
	 */
	@RequestMapping("resetPassword.do")
	@ResponseBody
	public ResultData resetPassword(HttpServletRequest request,Long userId){
		

		User user = userService.getById(userId);
		if(user!=null){
			user.setPassword(MD5Util.getMD5("123456"));	
			userService.modifyUser(user);
			return new ResultData(ResultData.SUCCESS,"重置"+user.getLoginName()+"密码为：123456");
		}else{
			return new ResultData(ResultData.ERROR, "用户不存在或没有权限");
		}
		
	}
	
	/**
	 * 跳转到给用户赋予角色页面
	 * @param user
	 * @return
	 */
	@RequestMapping("forwardToRoleToUser.do")
	public String forwardToRoleToUser(HttpServletRequest request,User user,String roleName){
		if (user.getId() == null)
			try {
				throw new Exception("缺失关键参数:ID");
			} catch (Exception e) {
				e.printStackTrace();
			}
		
		//查询所有的角色
		List<Role> roleList = roleService.queryRoleList(roleName);
		
		//查询出用户所拥有的角色
		List<Role> userRoleList = userService.queryUserRoles(user.getId()); 
		request.setAttribute("roleList", roleList);
		request.setAttribute("userRoles", userRoleList);
		request.setAttribute("userId", user.getId());
		
		return "platform/user/grant_role_to_user";
		
	}
	
	/**给某个用户分配角色
	 * @param request
	 * @param user
	 * @param roleId
	 * @return
	 */
	/*@RequestMapping("grantRoleToUser.do")
	public String grantRoleToUser(HttpServletRequest request,User user,Long[] roleId){
		userService.revokeRoleOnUser(user.getId());
		userService.grantRoleToUser(user.getId(),roleId);
		return "redirect:/user/forwardToRoleToUser.do";
	}*/
	@RequestMapping("showmessage.do")
	public String showmessage(HttpServletRequest request){
		User u=(User) request.getSession().getAttribute("loginUser");
		User user=userService.getById(u.getId()); 
		request.setAttribute("userInfo", user);
		return  "platform/user/view_self";
	}
	  /**
	   * 修改密码页面跳转
	   * @param request
	   * @return
	   */
	@RequestMapping( "/updatemyselfpwd.do" )
	public String updatemyselfpwd(HttpServletRequest request ){
		User u=(User) request.getSession().getAttribute("loginUser");
		User user=userService.getById(u.getId()); 
		request.setAttribute("user", user);
		return "platform/user/change_self_pwd";
	}
	/**
	 * 修改密码
	 * @param request
	 * @param id
	 * @param password
	 * @return
	 */
	@RequestMapping("/modifymyselfpwd.do")
	@ResponseBody
	public ResultData modifymyselfpwd(HttpServletRequest request, String id,String password,String orginalpwd){
		User user= userService.getById(Long.parseLong(id));
		if(!MD5Util.getMD5(orginalpwd).endsWith(user.getPassword())){
			return new ResultData(ResultData.ERROR, "原密码错误", ResultData.OPT_UPDATE, null);
		}
		 user.setPassword(MD5Util.getMD5(password));
		 user.setModifyPassword(0);
		 userService.updateUser(user);
		 User loginUser=(User) request.getSession().getAttribute("loginUser");
		 loginUser.setModifyPassword(0);
		 request.getSession().setAttribute("loginUser", loginUser);
		 return new ResultData(ResultData.SUCCESS, "密码修改成功", ResultData.OPT_UPDATE, null);
	}
}
