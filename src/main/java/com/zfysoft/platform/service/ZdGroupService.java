package com.zfysoft.platform.service;

import java.util.List;

import com.zfysoft.platform.model.ZdGroup;


public interface ZdGroupService {
	/*所有字典分组数据*/
	public List<ZdGroup> queryZdGroupList();
	/*根据子分组code找到父分组信息*/
	public ZdGroup getZdGroup(String groupCode);
}
