package com.zfysoft.platform.service.impl;

import java.io.Serializable;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.zfysoft.platform.dao.CommonDao;
import com.zfysoft.platform.service.CommonService;
@Service("commonService")
public class CommonServiceImpl implements CommonService {
	
	@Resource
	private CommonDao commonDao;
	
	@Override
	public <T> T getById(Class<T> clz, Serializable id) {
		return commonDao.getById(clz, id);
	}

	

	

	@Override
	public <T> T save(T obj) {
		return commonDao.save(obj);
	}

	@Override
	public <T> T update(T obj) {
		return commonDao.update(obj);
	}

	@Override
	public <T> T saveOrUpdate(T obj) {
		return commonDao.saveOrUpdate(obj);
	}

	@Override
	public void deleteById(Class<?> clz, Serializable id) {
		commonDao.deleteById(clz, id);
		
	}
	
	
	/**
	 * 判断表中某个字段是否已经存在了，如判断User的loginName是否重复
	 * @param fieldName 字段名称，如loginName
	 * @param model 对象本身，如user1
	 */
	public <T> boolean isFieldExist(String fieldName,T model){
		return	commonDao.isFieldExist(fieldName, model);
	}

}
