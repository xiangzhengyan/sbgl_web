/**
 * 版权所有 2013 成都子非鱼软件有限公司 保留所有权利
 * 1 项目签约客户只拥有对项目业务代码的所有权，以及在本项目范围内使用平台框架
 * 2 平台框架及相关代码属子非鱼软件有限公司所有，未经授权不得扩散、二次开发及用于其它项目
 */
package com.zfysoft.platform.init;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.context.request.WebRequest;

/**
 * 
 * @author xiangzy
 * @date 2013-7-22
 * 
 */
public class WebBindingInitializer implements org.springframework.web.bind.support.WebBindingInitializer {

	public void initBinder(WebDataBinder binder, WebRequest request) {
		binder.registerCustomEditor(Date.class, new CustomDateEditor(
				new ZFYSimpleDateFormat("yyyy-MM-dd"), true));

		binder.registerCustomEditor(Timestamp.class, new CustomDateEditor(
				new SimpleDateFormat("yyyy-MM-dd hh:mm:ss"), true));
	}
}