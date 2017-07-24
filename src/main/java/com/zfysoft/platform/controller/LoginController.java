/**
 * 版权所有 2013 成都子非鱼软件有限公司 保留所有权利
 * 1 项目签约客户只拥有对项目业务代码的所有权，以及在本项目范围内使用平台框架
 * 2 平台框架及相关代码属子非鱼软件有限公司所有，未经授权不得扩散、二次开发及用于其它项目
 */
package com.zfysoft.platform.controller;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zfysoft.common.util.MD5Util;
import com.zfysoft.common.util.StringUtil;
import com.zfysoft.platform.listener.MySession;
import com.zfysoft.platform.listener.MySessionContext;
import com.zfysoft.platform.model.ResultData;
import com.zfysoft.platform.model.Role;
import com.zfysoft.platform.model.User;
import com.zfysoft.platform.service.UserService;
import com.zfysoft.platform.util.ValidateCode;

/**
 * loginController
 * 
 * @author chenhm
 * @date 2013-7-26
 */
@Controller
@RequestMapping("/login/*")
public class LoginController {

	private static Logger logger = Logger.getLogger(LoginController.class);

	@Resource
	private UserService userService;

	// 本地化工具类
	Locale local = Locale.getDefault(); // 如果是中文系统,得到的是zh_CN
	// Locale local = new Locale("en", "US");
	// 将properties文件转存为键值对存储
	ResourceBundle rb = ResourceBundle.getBundle(
			"com/zfysoft/platform/local/portal/i18n", local,
			UserController.class.getClassLoader());

	/**
	 * 登录
	 * 
	 * @param user
	 * @param session
	 * @return
	 */
	@RequestMapping("/login.do")
	public String login(HttpServletRequest request,
			HttpServletResponse response, String username, String password,
			String code, String target) {

		String msg = "";
		String page = "";

		if (code == null
				|| !code.equalsIgnoreCase(request.getSession()
						.getAttribute("code").toString())) {
			msg = "请输入正确的验证码";
			page = "platform/portal/login";
			HttpSession session = request.getSession();
			request.setAttribute("msg", msg);
			request.setAttribute("username", username);
			session.setAttribute("locale", local);
			return page;
		}

		if (username != null && !"".equals(username.trim()))
			username = username.toLowerCase();

		List<User> list = userService.getUserByUserAlias(username);
		if (list.size() == 0) {
			request.setAttribute("msg", null);
			if (username == null || "" == username) {
				msg = rb.getString("platform.portal.inputusername");
				page = "platform/portal/login";
			} else {
				// 用户名不存在
				msg = rb.getString("platform.portal.nouser");
				page = "platform/portal/login";
			}
		} else {
			User user = list.get(0);
			if (user.getPassword() == null || "" == user.getPassword()) {
				msg = rb.getString("platform.portal.inputpassword");
				page = "platform/portal/login";
			}
			if (!user.getPassword().equals(MD5Util.getMD5(password))) {
				// 密码错误
				msg = rb.getString("platform.portal.pwderror");
				page = "platform/portal/login";
			} else {
				int status = user.getStatus().intValue();
				if (status == 2) {
					// 已锁定
					msg = rb.getString("platform.portal.lock");
					page = "platform/portal/login";
				} else if (status == 0) {
					// 已失效
					msg = rb.getString("platform.portal.userInvalid");
					page = "platform/portal/login";
				} else {
					// 登陆成功

					msg = rb.getString("platform.portal.loginsuccess");
					page = "redirect:/main.do";
					if (StringUtil.isNotEmptyOrNull(target)) {
						page += "?target=" + target;
					}
					request.getSession().setAttribute("loginUser", list.get(0));
					List<Role> roles = userService.queryUserRoles(list.get(0)
							.getId());
					if (roles != null && roles.size() > 0) {
						request.getSession().setAttribute("loginRole",
								roles.get(0));
						request.getSession().setAttribute("roles", roles);
						if ("admin".equals(roles.get(0).getCode())) {
							request.getSession().setAttribute("isAdmin", true);
						}
					}
				}
			}
		}

		HttpSession session = request.getSession();
		request.setAttribute("msg", msg);
		request.setAttribute("username", username);
		session.setAttribute("locale", local);
		return page;
	}

	/**
	 * 登出
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("/logout.do")
	public String logout(HttpServletRequest request) {

		HttpSession session = request.getSession();
		session.invalidate();
		return "platform/portal/login";
	}

	@RequestMapping("/image.do")
	@ResponseBody
	public void image(HttpServletRequest request, HttpServletResponse response) {
		response.setContentType("image/jpeg");
		HttpSession session = request.getSession();

		ValidateCode vCode = new ValidateCode(75, 30, 4, 10);
		session.setAttribute("code", vCode.getCode());
		try {
			vCode.write(response.getOutputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@ResponseBody
	@RequestMapping("/m_login.do")
	public ResultData mlogin(HttpServletRequest request1,
			HttpServletResponse response, String username, String password) {

		if(username==null || password==null){
			return new ResultData(ResultData.ERROR);
		}
		username = username.toLowerCase();

		List<User> list = userService.getUserByUserAlias(username);
		if (list.size() == 0
				|| !list.get(0).getPassword().equals(MD5Util.getMD5(password))) {
			return new ResultData(ResultData.ERROR);
		} else {
			// 登陆成功
			MySession mySession = MySessionContext.addSession();
			mySession.setAttribute("loginUser", list.get(0));
			
			List<Role> roles = userService.queryUserRoles(list.get(0).getId());
			if (roles != null && roles.size() > 0) {
				mySession.setAttribute("loginRole", roles.get(0));
				mySession.setAttribute("roles", roles);
				if ("admin".equals(roles.get(0).getCode())) {
					mySession.setAttribute("isAdmin", true);
				}
			}
			return new ResultData(ResultData.SUCCESS, null, null, mySession.getId());
		}

	}
	
	@ResponseBody
	@RequestMapping("/m_logout.do")
	public ResultData mLogout(HttpServletRequest request) {

		String sessionId = request.getParameter("sessionId");
		if(sessionId==null ){
			return new ResultData(ResultData.ERROR);
		}
		MySessionContext.removeSession(sessionId);
		return new ResultData(ResultData.SUCCESS);
	}

}
