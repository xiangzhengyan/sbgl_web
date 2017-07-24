package com.zfysoft.platform.init;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;

import com.zfysoft.platform.config.SpringContextHolder;
import com.zfysoft.platform.model.Config;
import com.zfysoft.platform.service.ConfigService;


public class InitLoader extends HttpServlet{
	/**
	 * 日志
	 */
	private static Logger logger = Logger.getLogger(InitLoader.class);

	private static final long serialVersionUID = -8575262119449847581L;
	
	private static ConfigService configService;
	
	static{
		configService = SpringContextHolder.getBean("configService");
		
	}
	
	@Override
	public void init() throws ServletException {
		super.init();
		logger.info("初始化配置加载开始..");
		List<Config> configList = configService.getConfigList();
		Map<String,String> configMap = new HashMap<String,String>();
		for(Config config : configList){
			configMap.put(config.getName(), config.getValue());
		}
		getServletContext().setAttribute("configsJson", JSONObject.fromObject(configMap));
		getServletContext().setAttribute("configs", configMap);
		logger.info("初始化配置加载结束..");
	}

}
