package com.zfysoft.platform.service;

import java.util.List;

import com.zfysoft.common.util.Page;
import com.zfysoft.platform.model.Config;

public interface ConfigService {

	/**
	 * 根据键找配置
	 * @param key
	 * @return
	 */
	public Config getConfig(String key);
	
	/**
	 * 获取所有配置
	 */
	public List<Config> getConfigList();
	
	/**
	 * 查找所有的配置项信息
	 * @param key
	 * @param description
	 * @param page
	 * @return
	 */
	public List<Config> getAllByPage(String key,String description,Page page);
	
	/**
	 * 根据id查找配置
	 * @param id
	 * @return
	 */
	public Config getById(Long id);
	
	/**
	 * 根据key,id查找
	 * @param key
	 * @param id
	 * @return
	 */
	public Config getByKeyAndId(String key,String id);
	
	/**
	 * 保存
	 * @param cfg
	 */
	public void save(Config cfg);
	
	/**
	 * 删除
	 * @param id
	 */
	public void delete(Long id);
}
