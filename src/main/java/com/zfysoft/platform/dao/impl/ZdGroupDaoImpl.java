package com.zfysoft.platform.dao.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import com.zfysoft.platform.dao.ZdGroupDao;
import com.zfysoft.platform.model.ZdGroup;


@Repository
public class ZdGroupDaoImpl extends BaseDaoImpl implements ZdGroupDao {
	
	@SuppressWarnings("unchecked")
	@Override
	public List<ZdGroup> queryZdGroupList() {
		Session session = sessionFactory.getCurrentSession();
		String hql = "from ZdGroup";
		Query query = session.createQuery(hql);
		return query.list();
	}

	@Override
	public ZdGroup getZdGroup(String groupCode) {
		Session session = sessionFactory.getCurrentSession();
		String sql = "select g2.* from p_zd_group g1,p_zd_group g2 where g1.parent_code = g2.group_code and g1.group_code = :groupCode";
		Query query = session.createSQLQuery(sql).addEntity(ZdGroup.class);
		query.setParameter("groupCode", groupCode);
		return (ZdGroup) query.uniqueResult();
	}
}
