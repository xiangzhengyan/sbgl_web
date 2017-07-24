package com.zfysoft.platform.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zfysoft.common.util.Page;
import com.zfysoft.common.util.StringUtil;
import com.zfysoft.platform.model.Config;
import com.zfysoft.platform.model.ResultData;
import com.zfysoft.platform.service.ConfigService;

/**
 * 系统配置表管理（CFG）
 * @author angql
 *
 * 2014-12-2
 */

@Controller
@RequestMapping("/cfg/*")
public class CfgManageController {
	
	private static Logger logger = Logger.getLogger(CfgManageController.class);  
	
	@Resource
	private ConfigService cfgService ;
	
	/**
	 * 分页查找所有配置
	 * @param request
	 * @param key
	 * @param description
	 * @param page
	 * @return
	 */
	@RequestMapping("/getAll.do")
	public String getAll(HttpServletRequest request,String key,String description,Page page){
		List<Config> cfgList = new ArrayList<Config>();
		cfgList = cfgService.getAllByPage(key, description, page);
		request.setAttribute("cfgList", cfgList);
		request.setAttribute("key", key);
		request.setAttribute("description", description);
		return "platform/config/cfg_list";
	}

	/**
	 * 新建、编辑、查看
	 * @param request
	 * @param id
	 * @param opt
	 * @return
	 */
	@RequestMapping("/pageForward.do")
	public String pageForward(HttpServletRequest request,String id,String opt){
		String url = "platform/config/edit";
		if(StringUtil.isNotEmptyOrNull(opt) && "view".equals(opt)){
			url = "platform/config/view";
		}
		Config cfg = new Config();
		if(StringUtil.isNotEmptyOrNull(id)){
			cfg = cfgService.getById(new Long(id));
			request.setAttribute("cfg", cfg);
		}
		return url;
	}
	
	/**
	 * 新建、编辑保存
	 * @param request
	 * @param cfg
	 * @param id
	 * @return
	 */
	@RequestMapping("/save.do")
	@ResponseBody
	public ResultData save(HttpServletRequest request,Config cfg,String id){
		Config currentCfg = cfgService.getByKeyAndId(cfg.getName(), id);
		if(StringUtil.isNotEmptyOrNull(currentCfg)){
			return new ResultData(ResultData.ERROR,"参数名称已存在");
		}
		String opt = StringUtil.isEmptyOrNull(id) ? "add" : "update";
		try {
			cfgService.save(cfg);
			updateCache(request);//修改缓存
			return new ResultData(ResultData.SUCCESS,"保存成功",opt,cfg);
		} catch (Exception e) {
			logger.error("保存配置信息出错", e);
			return new ResultData(ResultData.SUCCESS,"保存失败",null,e);
		}
	}
	
	/**
	 * 删除
	 * @param request
	 * @param id
	 * @return
	 */
	@RequestMapping("/delete.do")
	@ResponseBody
	public ResultData delete(HttpServletRequest request,String id){
		try {
			cfgService.delete(new Long(id));
			updateCache(request);//修改缓存
			return new ResultData(ResultData.SUCCESS,"删除成功");
		} catch (Exception e) {
			logger.error("删除配置文件失败",e);
			return new ResultData(ResultData.ERROR,"删除失败");
		}
	}
	
	public void updateCache(HttpServletRequest request){
			logger.info("刷新配置信息缓存..");
			List<Config> configList = cfgService.getConfigList();
			Map<String,String> configMap = new HashMap<String,String>();
			for(Config config : configList){
				configMap.put(config.getName(), config.getValue());
			}
			request.getSession().getServletContext().setAttribute("configsJson", JSONObject.fromObject(configMap));
			request.getSession().getServletContext().setAttribute("configs", configMap);
			
			logger.info("刷新配置信息缓存结束");
	}
}
