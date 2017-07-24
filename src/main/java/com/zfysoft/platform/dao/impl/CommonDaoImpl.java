package com.zfysoft.platform.dao.impl;

import java.lang.reflect.Field;
import java.util.List;

import javax.annotation.Resource;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import com.zfysoft.common.util.StringUtil;
import com.zfysoft.platform.dao.CommonDao;
@Repository("commonDao")
public class CommonDaoImpl extends BaseDaoImpl implements CommonDao {
	
	@Resource(name="sessionFactory")
	protected SessionFactory sessionFactory;

	

	@Override
	public Long selectNextSequence() {
		Session session = sessionFactory.getCurrentSession();
		String sq = session.createSQLQuery("select hibernate_sequence.nextval from dual").uniqueResult().toString();
		return Long.parseLong(sq);
	}

	
	
	
	/**
	 * 判断表中某个字段是否已经存在了，如判断User的loginName是否重复
	 * @param fieldName 字段名称，如loginName
	 * @param model 对象本身，如user1
	 */
	@SuppressWarnings("rawtypes")
	public <T> boolean isFieldExist(String fieldName,T model){
		if(StringUtil.isEmptyOrNull(fieldName) || StringUtil.isEmptyOrNull(model)){
			return true;
		}
		Session session = sessionFactory.getCurrentSession();
		Object fieldValue = null;
		Object idValue = null;
		try {
			Field field = model.getClass().getDeclaredField(fieldName);  
			field.setAccessible(true);
			fieldValue = field.get(model);
			
			Field idField = model.getClass().getDeclaredField("id"); 
			idField.setAccessible(true);
			idValue = idField.get(model);
		} catch (Exception e) {
			e.printStackTrace();
		}
		//如果字段值是null则返回已存在
	    if(StringUtil.isEmptyOrNull(fieldValue)){
	    	return true;
	    }
	    
	    String hql = "from "+model.getClass().getSimpleName() + " where upper("  + fieldName + ")=upper(?) ";
	    if(StringUtil.isNotEmptyOrNull(fieldValue) && !fieldValue.equals("null")){
	    	hql += " and id <> " + idValue;
	    }
	    Query query = session.createQuery(hql);
	    query.setParameter(0, fieldValue);
	    List list = query.list();
	    if(list == null || list.size() == 0){
	    	return false;
	    }else{
	    	return true;
	    }
	}




	@Override
	public Boolean exeSQL(String sql) {
		Session session = sessionFactory.getCurrentSession();
		String[] sqls = sql.split(";");
		for(String sq : sqls){
			if(StringUtil.isNotEmptyOrNull(sq)){
				session.createSQLQuery(sq).executeUpdate();
			}
		}
		return true;
	}


	
}
