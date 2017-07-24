package com.zfysoft.platform.service;

import java.io.Serializable;

public interface CommonService {
	
	public <T> T getById(Class<T> clz,Serializable id);
	
	public <T> T save(T obj);
		
	public <T> T update(T obj);
	
	public <T> T saveOrUpdate(T obj);
	
	public void deleteById(Class<?> clz,Serializable id);
	
	/**
	 * 判断表中某个字段是否已经存在了，如判断User的loginName是否重复
	 * @param fieldName 字段名称，如loginName
	 * @param model 对象本身，如user1
	 */
	public <T> boolean isFieldExist(String fieldName,T model);
	
	
}
