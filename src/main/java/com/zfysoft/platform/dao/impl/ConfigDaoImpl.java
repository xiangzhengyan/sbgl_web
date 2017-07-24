package com.zfysoft.platform.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import com.zfysoft.common.util.Page;
import com.zfysoft.common.util.StringUtil;
import com.zfysoft.platform.dao.ConfigDao;
import com.zfysoft.platform.model.Config;

@Repository("configDao")
public class ConfigDaoImpl extends BaseDaoImpl implements ConfigDao {

	@Override
	public Config getConfig(String name) {
		Session session = sessionFactory.getCurrentSession();
		SQLQuery query = session.createSQLQuery("select * from P_Config where name=? and rownum=1");
		query.addEntity(Config.class);
		query.setString(0, name);
		Config config = (Config) query.uniqueResult();
		return config;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Config> getAllByPage(String name, String description, Page page) {
		Session session = sessionFactory.getCurrentSession();
		String sql = "select * from P_Config where 1=1 ";
		if(StringUtil.isNotEmptyOrNull(description)){
			sql += " and DESCRIPTION like '%"+description+"%'";
		}
		if(StringUtil.isNotEmptyOrNull(name)){
			sql += " and name like '"+name+"%'";
		}
		//排序
		if(StringUtil.isNotEmptyOrNull(page.getSortCol())){
			sql += " order by "+page.getSortCol()+" "+page.getSortOrder()+" nulls last";
		}else{
			sql += " order by name";
		}
		SQLQuery query = session.createSQLQuery(sql);
		query.addEntity(Config.class);
		List<Object> params = new ArrayList<Object>();
		super.pageSQL(page, query, session, sql, params);
		return query.list();
	}

	@Override
	public Config getById(Long id) {
		Session session = sessionFactory.getCurrentSession();
		String sql = "select * from P_Config where id="+id;
		SQLQuery query = session.createSQLQuery(sql).addEntity(Config.class);
		return (Config) query.uniqueResult();
	}

	@Override
	public Config getByNameAndId(String name, String id) {
		Session session = sessionFactory.getCurrentSession();
		String sql = "select * from P_Config where name ='"+name+"'";
		if(StringUtil.isNotEmptyOrNull(id)){
			sql += " and id!="+id;
		}
		SQLQuery query = session.createSQLQuery(sql).addEntity(Config.class);
		return (Config) query.uniqueResult();
	}

}
