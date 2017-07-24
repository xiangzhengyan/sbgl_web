/**
 * 版权所有 2013 成都子非鱼软件有限公司 保留所有权利
 * 1 项目签约客户只拥有对项目业务代码的所有权，以及在本项目范围内使用平台框架
 * 2 平台框架及相关代码属子非鱼软件有限公司所有，未经授权不得扩散、二次开发及用于其它项目
 */
package com.zfysoft.platform.dao;

import java.io.Serializable;
import java.util.List;

/**
 * @author hudt
 * @date 2013-7-4
 */
public interface BaseDao {
	
    public <T> T save(T obj);
	
	public <T> T update(T obj);
	
	public <T> T saveOrUpdate(T obj);
	
	public void deleteById(Class<?> clz,Serializable id);
	
	public <T> T getById(Class<T> clz,Serializable id);
	
	public <T> Boolean saveList(List<T> list);
	
	public <T> List<T> getList(Class<T> clz);
	
	public <T> Boolean deleteList(List<T> list);
	
	public <T> T delte(T obj);   
	
	public String updateShape(String tableName, String n, String e, Long id);
	
	public String updateShapeByRid(String tableName, String n, String e,
			Long rid);
	public String getLon(String tableName, Long id);
	public String getLat(String tableName, Long id);
	public Long querySridById(String tableName, Long id);
	public Long querySrid(String tableName);
	
	public Long selectNextSequence() ;
}
