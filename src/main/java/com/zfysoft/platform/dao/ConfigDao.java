package com.zfysoft.platform.dao;

import java.util.List;

import com.zfysoft.common.util.Page;
import com.zfysoft.platform.model.Config;

public interface ConfigDao extends BaseDao{
	
	/**
	 * 根据键找配置
	 * @param name
	 * @return
	 */
	public Config getConfig(String name);
	
	/**
	 * 查找所有的配置项信息
	 * @param name
	 * @param description
	 * @param page
	 * @return
	 */
	public List<Config> getAllByPage(String name,String description,Page page);
	
	/**
	 * 根据id查找配置
	 * @param id
	 * @return
	 */
	public Config getById(Long id);
	
	/**
	 * 根据name,id查找
	 * @param name
	 * @param id
	 * @return
	 */
	public Config getByNameAndId(String name,String id);
}
