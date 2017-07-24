/**
 * 版权所有 2013 成都子非鱼软件有限公司 保留所有权利
 * 1 项目签约客户只拥有对项目业务代码的所有权，以及在本项目范围内使用平台框架
 * 2 平台框架及相关代码属子非鱼软件有限公司所有，未经授权不得扩散、二次开发及用于其它项目
 */
package com.zfysoft.platform.listener;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * 把组织机构类型对应的图片地址放到
 * @author hudt
 * @date 2013-8-15
 */
public class OrganizationListener implements ServletContextListener {

	
	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		// TODO Auto-generated method stub

	}

	@SuppressWarnings("unused")
	@Override
	public void contextInitialized(ServletContextEvent e) {
		Map<String,String> orgImgs = new HashMap<String,String>();
		String zfy = e.getServletContext().getContextPath();
		orgImgs.put("org_root", "organization/org_root.gif");
		orgImgs.put("organization", "organization/organization.gif"); 
		orgImgs.put("provinceCompany", "organization/organization.gif");
		orgImgs.put("stateCompany", "organization/organization.gif");
		orgImgs.put("countyCompany", "organization/organization.gif");
		orgImgs.put("tobaccoStation", "organization/organization.gif");
		orgImgs.put("department", "organization/department.gif");
		orgImgs.put("post", "organization/post.gif");
		orgImgs.put("employee", "organization/employee.gif");
		e.getServletContext().setAttribute("orgImgs", orgImgs);
 
	}

}
