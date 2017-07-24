package com.zfysoft.platform.init;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.apache.log4j.Logger;

import com.zfysoft.platform.cache.XzqhCache;

public class XzqhLoader extends HttpServlet{
	/**
	 * 日志
	 */
	private static Logger logger = Logger.getLogger(XzqhLoader.class);
	
	private static final long serialVersionUID = 1L;

	@Override
	public void init() throws ServletException {
		super.init();
		logger.info("初始化natrual包配置加载开始..");
		XzqhCache.loadAll();
		logger.info("初始化natrual包配置加载结束..");
		
		logger.info("预警信息监听开始运行..");
	}

}
