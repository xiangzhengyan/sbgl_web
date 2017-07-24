package com.zfysoft.platform.service;

import java.util.List;

import com.zfysoft.platform.model.ZdItem;


public interface ZdItemService {
	/**
	 * 根据分组code获得对应的字典列表
	 * @param groupCode
	 * @return
	 */
	List<ZdItem> getZdItems(String groupCode,ZdItem zdItem);
	/**
	 * 获得对应的字典信息
	 * @param groupCode
	 * @param id
	 * @return
	 */
	ZdItem getZdItem(String groupCode, Long id);
	/**
	 * 新增或修改
	 * @param zdItem
	 */
	void saveOrUpdateZdItem(ZdItem zdItem);
	/**
	 * 删除字典信息
	 * @param id
	 */
	void deleteZdItem(Long id);
	/**
	 * 获得该分组 父分组下的所哟字典数据集合
	 * @param groupCode
	 * @return
	 */
	List<ZdItem> getParentZdItems(String groupCode);
	/**
	 * 获得该分组下的字典信息
	 * @param zdGroupCode
	 * @param label
	 * @param id
	 * @return
	 */
	ZdItem getZdItem(String zdGroupCode, String label, Long id);
	/**
	 * 获得对应的字典信息
	 * @param id
	 * @return
	 */
	ZdItem getZdItem(Long id);
	
	/**
	 * 获取所有
	 * @return
	 */
	public List<ZdItem> getAllZdItems();
}
