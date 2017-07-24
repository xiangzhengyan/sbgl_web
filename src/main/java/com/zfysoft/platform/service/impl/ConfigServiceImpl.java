package com.zfysoft.platform.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.zfysoft.common.util.Page;
import com.zfysoft.platform.dao.ConfigDao;
import com.zfysoft.platform.model.Config;
import com.zfysoft.platform.service.ConfigService;
@Service("configService")
public class ConfigServiceImpl implements ConfigService {
	
	@Resource
	private ConfigDao configDao;

	@Override
	public Config getConfig(String key) {
		return configDao.getConfig(key);
	}

	@Override
	public List<Config> getConfigList(){
		return configDao.getList(Config.class);
	}

	@Override
	public List<Config> getAllByPage(String key, String description, Page page) {
		// TODO Auto-generated method stub
		return configDao.getAllByPage(key, description, page);
	}

	@Override
	public Config getById(Long id) {
		// TODO Auto-generated method stub
		return configDao.getById(id);
	}

	@Override
	public Config getByKeyAndId(String key, String id) {
		// TODO Auto-generated method stub
		return configDao.getByNameAndId(key, id);
	}

	@Override
	public void save(Config cfg) {
		// TODO Auto-generated method stub
		configDao.saveOrUpdate(cfg);
	}

	@Override
	public void delete(Long id) {
		// TODO Auto-generated method stub
		configDao.deleteById(Config.class, id);
	}
}
