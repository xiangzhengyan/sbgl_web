package com.zfysoft.platform.dao.impl;

import java.sql.Timestamp;
import java.util.List;

import javax.annotation.Resource;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.zfysoft.common.util.Page;
import com.zfysoft.platform.dao.SBDao;
import com.zfysoft.platform.model.sb.DataLog;
import com.zfysoft.platform.model.sb.Heart;
import com.zfysoft.platform.model.sb.SB;
import com.zfysoft.platform.util.IdUtil;

/**
 * @author xiangzy
 * @date 2015-9-8
 *
 */
@Repository
public class SBDaoImpl implements SBDao{

	@Resource
	private SessionFactory sessionFactory;
	
	@Override
	public void save(SB sb) {
		Session session = this.sessionFactory.getCurrentSession();
		if(sb.getId()==null){
			sb.setId(IdUtil.getId());
		}
		session.saveOrUpdate(sb);
		
	}


	@Override
	public List<SB> getListByOrgId(Long orgId) {
		Session session = this.sessionFactory.getCurrentSession();
		Criteria criteria =  session.createCriteria(SB.class); 
		criteria.add(Restrictions.eq("org.id", orgId));
		criteria.addOrder(Order.desc("installTime"));
		
		return criteria.list();
	}


	@Override
	public SB getById(Long id) {
		Session session = this.sessionFactory.getCurrentSession();
		return (SB)session.get(SB.class, id);
	}



	@Override
	public void delete(SB sb) {
		Session session = this.sessionFactory.getCurrentSession();
		session.delete(sb);
		
	}

	@Override
	public int queryDataLogListCount(Long id, String code, Timestamp begin,
			Timestamp end, String  type) {
		Session session = this.sessionFactory.getCurrentSession();
		Criteria cri = session.createCriteria(DataLog.class);
		cri.setProjection(Projections.rowCount());
		cri.add(Restrictions.eq("code", code));
		cri.add(Restrictions.eq("type", type));
		if(begin!=null){
			cri.add(Restrictions.ge("time", begin));
		}
		if(end!=null){
			cri.add(Restrictions.le("time", end));
		}
		Long total = (Long) cri.uniqueResult();
		return total.intValue();
	}


	@Override
	public List<DataLog> queryDataLogList(Long id, String code, Timestamp begin,
			Timestamp end, Page page, String  type) {
		int b = (page.getPageNum() - 1) * page.getPageSize();
		Session session = this.sessionFactory.getCurrentSession();
		Criteria cri = session.createCriteria(DataLog.class);
		cri.add(Restrictions.eq("code", code));
		cri.add(Restrictions.eq("type", type));
		if(begin!=null){
			cri.add(Restrictions.ge("time", begin));
		}
		if(end!=null){
			cri.add(Restrictions.le("time", end));
		}
		cri.addOrder(Order.desc("time"));
		
		List<DataLog> pagelist = cri.setFirstResult(b).setMaxResults(page.getPageSize()).list();
		return pagelist;
		
	
	}


	@Override
	public DataLog queryLastDataLog(String code) {
	
		Session session = this.sessionFactory.getCurrentSession();
		Criteria cri = session.createCriteria(DataLog.class);
		cri.add(Restrictions.eq("code", code));
		cri.add(Restrictions.ne("type", "login"));
		cri.addOrder(Order.desc("time"));
		cri.setMaxResults(1);
		return (DataLog)cri.uniqueResult();
	}



	@Override
	public Heart queryLastHeart(String code) {
		Session session = this.sessionFactory.getCurrentSession();
		Criteria cri = session.createCriteria(Heart.class);
		cri.add(Restrictions.eq("code", code));
		cri.setMaxResults(1);
		return (Heart)cri.uniqueResult();
	}

}
