package com.zfysoft.platform.dao;


/**
 * 杂七杂八的查询
 * @author hudt
 */
public interface CommonDao extends BaseDao{
	
	Long selectNextSequence();
	
	Boolean exeSQL(String sql);
	
	/**
	 * 判断表中某个字段是否已经存在了，如判断User的loginName是否重复
	 * @param fieldName 字段名称，如loginName
	 * @param model 对象本身，如user1
	 */
	public <T> boolean isFieldExist(String fieldName,T model);

}
