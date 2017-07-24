package com.zfysoft.platform.dao;

import java.util.List;

import com.zfysoft.platform.dao.BaseDao;
import com.zfysoft.platform.model.ZdGroup;

public interface ZdGroupDao extends BaseDao {
	/*所有字典分组数据*/
	public List<ZdGroup> queryZdGroupList();
	/*根据子分组code找到父分组信息*/
	public ZdGroup getZdGroup(String groupCode);
}
