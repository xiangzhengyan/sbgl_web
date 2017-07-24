package com.zfysoft.platform.cache;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.zfysoft.platform.config.SpringContextHolder;
import com.zfysoft.platform.model.ZdGroup;
import com.zfysoft.platform.model.ZdItem;
import com.zfysoft.platform.service.ZdGroupService;
import com.zfysoft.platform.service.ZdItemService;

public class CommonCDCache {
	
	//public static Map<String,Map<String,String>> map;
	
	//public static List<ZdCommon> list;

	private static List<ZdGroup> groupList;
	private static List<ZdItem> itemList;
	
	private static ZdGroupService zdGroupService;
	private static ZdItemService zdItemService;
	static{
		zdGroupService = SpringContextHolder.getBean("zdGroupService");
		zdItemService = SpringContextHolder.getBean("zdItemService");
	}
	
	public static String getLabel(String groupCode,String _code){
		refresh();
		for (ZdItem item : itemList) {
			if(item!=null && item.getZdGroupCode()!=null && item.getZdGroupCode().equals(groupCode) && item.getCode()!=null 
					&& item.getCode().equals(_code)){
				return item.getLabel();
			}
		}
		return _code;
	}
	
	/**
	 * 根据groupCode获取code-label键值对
	 * @param groupCode
	 * @return
	 */
	public static Map<String,String> getCodesAndLabels(String groupCode){
		refresh();
		Map<String,String> map = new LinkedHashMap<String, String>();
		for (ZdItem item : itemList) {
			if(item != null && item.getZdGroupCode() != null && item.getZdGroupCode().equals(groupCode)){
				map.put(item.getCode(), item.getLabel());
			}
		}
		return map;
	}

	/**
	 * 根据groupCode获取字典集合
	 * @param groupCode
	 * @return
	 */
	public static List<ZdItem> getListByGroupCode(String groupCode){
		refresh();
		List<ZdItem> rtnlist = new ArrayList<ZdItem>();
		for (ZdItem item : itemList) {
			if(item != null && item.getZdGroupCode() != null && item.getZdGroupCode().equals(groupCode)){
				rtnlist.add(item);
			}
		}
		return rtnlist;
	}
	
	/**
	 * 根据code获取item
	 * @param code
	 */
	public static ZdItem getGroupCodeByCode(String code){
		ZdItem _zdItem = new ZdItem();
		for (ZdItem item : itemList) {
			if(item != null && item.getCode() != null && item.getCode().equals(code)){
				_zdItem = item;
			}
		}
		return _zdItem;
	}
	/**
	 * 获得字典code集合,即下拉内容
	 * @param groupCode
	 * @return
	 */
	public static Set<String> getCodes(String groupCode){
		return getCodesAndLabels(groupCode).keySet();
	}
	
	public static Map<String,String> getCodesAndLabels(String groupCode,String parentCode){
		refresh();
		Map<String,String> map = new LinkedHashMap<String, String>();
		String _parentGroupCode = null;//父亲的组别
		String _groupCode = null;//子 组别
		for (ZdItem item : itemList) {
			if(item.getZdGroupCode().equals(groupCode)){
				_groupCode = item.getZdGroupCode();//组别
			}
		}
		for (ZdGroup group : groupList) {
			if(group.getGroupCode().equals(_groupCode) && group.getParentCode()!=null){
				_parentGroupCode = group.getParentCode();//父亲的组别
			}
		}
		for (ZdItem item1 : itemList) {
			if(item1!=null && item1.getZdGroupCode().equals(_groupCode) && item1.getParentCode().equals(parentCode) 
					&& getGroupCodeByCode(parentCode).getZdGroupCode().equals(_parentGroupCode)){
						map.put(item1.getCode(),item1.getLabel());
			}
		}
		return map;
	}
	
	public synchronized static void refresh(){
		if(groupList == null || groupList.size() == 0){
			reloadAllGroup();
		}
		if(itemList == null || itemList.size() == 0){
			reloadAllItem();	
		}
	}
	
	public synchronized static void reloadAllGroup(){
		groupList = zdGroupService.queryZdGroupList();
	}
	
	public synchronized static void reloadAllItem(){
		itemList = zdItemService.getAllZdItems();
	}
	
}
