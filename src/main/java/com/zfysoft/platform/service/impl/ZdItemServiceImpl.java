package com.zfysoft.platform.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.zfysoft.platform.dao.ZdItemDao;
import com.zfysoft.platform.model.ZdItem;
import com.zfysoft.platform.service.ZdItemService;

@Service("zdItemService")
public class ZdItemServiceImpl implements ZdItemService {
	
	@Resource
	private ZdItemDao zdItemDao;
	
	@Override
	public List<ZdItem> getZdItems(String groupCode,ZdItem zdItem) {
		return zdItemDao.getZdItems(groupCode,zdItem);
	}

	@Override
	public ZdItem getZdItem(String groupCode, Long id) {
		return zdItemDao.getZdItem(groupCode,id);
	}

	@Override
	public void saveOrUpdateZdItem(ZdItem zdItem) {
		zdItemDao.saveOrUpdateZdItem(zdItem);
	}

	@Override
	public void deleteZdItem(Long id) {
		zdItemDao.deleteById(ZdItem.class, id);
	}

	@Override
	public List<ZdItem> getParentZdItems(String groupCode) {
		return zdItemDao.getParentZdItems(groupCode);
	}

	@Override
	public ZdItem getZdItem(String zdGroupCode, String label, Long id) {
		return zdItemDao.getZdItem(zdGroupCode, label, id);
	}

	@Override
	public ZdItem getZdItem(Long id) {
		return zdItemDao.getZdItem(id);
	}

	@Override
	public List<ZdItem> getAllZdItems() {
		// TODO Auto-generated method stub
		return zdItemDao.getAllZdItems();
	}
}
