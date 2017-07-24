package com.zfysoft.platform.dao.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import com.zfysoft.platform.dao.XzqhDao;
import com.zfysoft.platform.model.Xzqh;
@Repository
public class XzqhDaoImpl extends BaseDaoImpl implements XzqhDao {

	@SuppressWarnings("unchecked")
	@Override
	public List<Xzqh> getAllXzqh() {
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery("from Xzqh");
		return query.list();
	}

	@Override
	public void saveXzqhList(List<Xzqh> list) {
		Session session = sessionFactory.getCurrentSession();
		for(Xzqh xzqh : list){
			session.save(xzqh);
		}
	}
	
	@Override
	public void saveXzqh(Xzqh xzqh) {
		Session session = sessionFactory.getCurrentSession();

		session.save(xzqh);
		
	}

}
