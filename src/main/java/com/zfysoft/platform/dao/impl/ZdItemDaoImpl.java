package com.zfysoft.platform.dao.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import com.zfysoft.common.util.StringUtil;
import com.zfysoft.platform.dao.ZdItemDao;
import com.zfysoft.platform.model.ZdItem;

@Repository
public class ZdItemDaoImpl extends BaseDaoImpl implements ZdItemDao {

	@SuppressWarnings("unchecked")
	@Override
	public List<ZdItem> getZdItems(String groupCode,ZdItem zdItem) {
		Session session = sessionFactory.getCurrentSession();
		String hql = "from ZdItem z where z.zdGroupCode = :zdGroupCode";
		if(StringUtil.isNotEmptyOrNull(zdItem.getLabel())){
			hql = hql+" and z.label = :label";
		}
		hql += " order by index nulls first";
		Query query = session.createQuery(hql);
		query.setParameter("zdGroupCode", groupCode);
		if(StringUtil.isNotEmptyOrNull(zdItem.getLabel())){
			query.setParameter("label", zdItem.getLabel());
		}
		return query.list();
	}

	@Override
	public ZdItem getZdItem(String groupCode, Long id) {
		Session session = sessionFactory.getCurrentSession();
		String hql = "from ZdItem z where z.zdGroupCode = :zdGroupCode and z.id = :id";
		Query query = session.createQuery(hql);
		query.setParameter("zdGroupCode", groupCode);
		query.setParameter("id", id);
		return (ZdItem) query.uniqueResult();
	}

	@Override
	public void saveOrUpdateZdItem(ZdItem zdItem) {
		Session session = sessionFactory.getCurrentSession();
		session.saveOrUpdate(zdItem);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ZdItem> getParentZdItems(String groupCode) {
		Session session = sessionFactory.getCurrentSession();
		String sql = "select i.* from p_zd_group g,p_zd_item i"
				+ " where g.parent_code=i.zd_group_code and g.group_code = :groupCode"
				+ " order by item_index asc ";
		Query query = session.createSQLQuery(sql).addEntity(ZdItem.class);
		query.setParameter("groupCode", groupCode);
		return query.list();
	}

	@Override
	public ZdItem getZdItem(String zdGroupCode, String label, Long id) {
		Session session = sessionFactory.getCurrentSession();
		String hql = "from ZdItem z where z.zdGroupCode = :zdGroupCode and z.label=:label";
		if(StringUtil.isNotEmptyOrNull(id)){
			hql = hql+" and z.id<>:id";
		}
		Query query = session.createQuery(hql);
		query.setParameter("zdGroupCode", zdGroupCode);
		query.setParameter("label", label);
		if(StringUtil.isNotEmptyOrNull(id)){
			query.setParameter("id", id);
		}
		return (ZdItem) query.uniqueResult();
	}

	@Override
	public ZdItem getZdItem(Long id) {
		Session session = sessionFactory.getCurrentSession();
		String hql = "from ZdItem z where z.id=:id";
		Query query = session.createQuery(hql);
		query.setParameter("id", id);
		return (ZdItem) query.uniqueResult();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ZdItem> getAllZdItems() {
		Session session = sessionFactory.getCurrentSession();
		String hql = "from ZdItem z where 1=1 order by index asc nulls first";
		Query query = session.createQuery(hql);
		return (List<ZdItem>) query.list();
	}
}
