/**
 * 版权所有 2013 成都子非鱼软件有限公司 保留所有权利
 * 1 项目签约客户只拥有对项目业务代码的所有权，以及在本项目范围内使用平台框架
 * 2 平台框架及相关代码属子非鱼软件有限公司所有，未经授权不得扩散、二次开发及用于其它项目
 */
package com.zfysoft.platform.config;

import java.util.List;

import com.zfysoft.platform.model.FunctionAction;
import com.zfysoft.platform.model.User;
import com.zfysoft.platform.service.ActionService;

/**
 * @author hudt
 * @date 2013-7-30
 */
public class ButtonConfig {

	private ButtonConfig(){}
	
	private static ActionService buttonService;
	
	static{
		buttonService = SpringContextHolder.getBean("buttonService");
	}
	
	public static List<FunctionAction> queryPermissionButtons(User loginUser,String funUrl){
		return buttonService.queryPermissionButtons(loginUser, funUrl);
	}

}
