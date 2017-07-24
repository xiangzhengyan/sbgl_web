package com.zfysoft.platform.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.zfysoft.platform.dao.ZdGroupDao;
import com.zfysoft.platform.model.ZdGroup;
import com.zfysoft.platform.service.ZdGroupService;


@Service("zdGroupService")
public class ZdGroupServiceImpl implements ZdGroupService{
	@Resource
	private ZdGroupDao zdGroupDao;
	@Override
	public List<ZdGroup> queryZdGroupList() {
		return zdGroupDao.queryZdGroupList();
	}
	@Override
	public ZdGroup getZdGroup(String groupCode) {
		return zdGroupDao.getZdGroup(groupCode);
	}
}
