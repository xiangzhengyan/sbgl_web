/**
 * 版权所有 2013 成都子非鱼软件有限公司 保留所有权利
 * 1 项目签约客户只拥有对项目业务代码的所有权，以及在本项目范围内使用平台框架
 * 2 平台框架及相关代码属子非鱼软件有限公司所有，未经授权不得扩散、二次开发及用于其它项目
 */
package com.zfysoft.platform.dao.impl;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import javax.annotation.Resource;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import com.zfysoft.common.util.CoordinateConversion;
import com.zfysoft.common.util.Page;
import com.zfysoft.common.util.StringUtil;
import com.zfysoft.platform.dao.BaseDao;

/**
 * @author hudt
 * @date 2013-7-4
 */
@Repository
public class BaseDaoImpl implements BaseDao {
	
	@Resource(name="sessionFactory")
	protected SessionFactory sessionFactory;
	
	protected void pageHQL(Page page,Query mQuery,Session session,String hql,Object... params){
		Query query = session.createQuery("select count(*) from ("+hql+")");
		int index = 0;
		for(int i = 0 ; i < params.length ; i++){
			if(params[i]!=null && !params[i].toString().trim().equals("")){
				query.setParameter(index, params[i]);
				mQuery.setParameter(index, params[i]);
				index++;
			}
		}
		page.setTotalRows(Integer.valueOf(query.uniqueResult().toString()).intValue());
		mQuery.setFirstResult((page.getPageNum()-1)*page.getPageSize());
		mQuery.setMaxResults(page.getPageSize());
	}
	
	protected void pageSQL(Page page,Query mQuery,Session session,String sql,Object... params){
		Query query = session.createSQLQuery("select count(*) from ("+sql+")"); 
		int index = 0;
		for(int i = 0 ; i < params.length ; i++){
			if(params[i]!=null && !params[i].toString().trim().equals("")){
				query.setParameter(index, params[i]);
				mQuery.setParameter(index, params[i]);
				index++;
			}
		}
		page.setTotalRows(Integer.valueOf(query.uniqueResult().toString()).intValue());
		mQuery.setFirstResult((page.getPageNum()-1)*page.getPageSize());
		mQuery.setMaxResults(page.getPageSize());
	}
	
	protected void pageHQL(Page page,Query mQuery,Session session,String hql,List<Object> params){
		Query query = session.createQuery("select count(*) from ("+hql+")");
		int index = 0;
		for(int i = 0 ; i < params.size() ; i++){
			if(StringUtil.isNotEmptyOrNull(params.get(i))){
				query.setParameter(index, params.get(i));
				mQuery.setParameter(index, params.get(i));
				index++;
			}
		}
		page.setTotalRows(Integer.valueOf(query.uniqueResult().toString()).intValue());
		mQuery.setFirstResult((page.getPageNum()-1)*page.getPageSize());
		mQuery.setMaxResults(page.getPageSize());
	}
	
 
	protected void pageSQL(Page page,Query mQuery,Session session,String sql,List<Object> params){
		Query query = session.createSQLQuery("select count(*) from ("+sql+") as _count_tab"); 
		System.out.println(sql);
		int index = 0;
		for(int i = 0 ; i < params.size() ; i++){
			if(StringUtil.isNotEmptyOrNull(params.get(i))){
				query.setParameter(index, params.get(i));
				mQuery.setParameter(index, params.get(i));
				index++;
			}
		}
		page.setTotalRows(Integer.valueOf(query.uniqueResult().toString()).intValue());
		mQuery.setFirstResult((page.getPageNum()-1)*page.getPageSize());
		mQuery.setMaxResults(page.getPageSize());
	}
 


	@Override
	public <T> T save(T obj){
		System.out.println("gisbasedao save");
		Session session = sessionFactory.getCurrentSession();
		session.save(obj);
		return obj;
	}

	@Override
	public <T> T update(T obj) {
		System.out.println("gisbasedao update");
		Session session = sessionFactory.getCurrentSession();
		session.update(obj);
		return obj;
	}
	@Override	
	public <T> T saveOrUpdate(T obj){
		Session session = sessionFactory.getCurrentSession();
		session.saveOrUpdate(obj);
		return obj;
	}

	@Override
	public void deleteById(Class<?> clz,Serializable id) {
		Object obj = getById(clz,id);
		if(obj != null){
			Session session = sessionFactory.getCurrentSession();
			session.delete(obj);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T getById(Class<T> clz, Serializable id) {
		Session session = sessionFactory.getCurrentSession();
		if(id instanceof String){
			id = StringUtil.toLong(id);
		}
		Object obj = session.get(clz, id);
		return (T) obj;
	}

	@Override
	public <T> Boolean saveList(List<T> list) {
		Session session = sessionFactory.getCurrentSession();
		for(T obj : list){
			session.save(obj);
		}
		return true;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> List<T> getList(Class<T> clz) {
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery("from "+clz.getSimpleName());
		return query.list();
	}
	
	
	@Override
	public <T> Boolean deleteList(List<T> list) {
		Session session = sessionFactory.getCurrentSession();
		for(T obj : list){
			session.delete(obj);
		}
		return true;
	}

	@Override
	public <T> T delte(T obj) {
		Session session = sessionFactory.getCurrentSession();
		session.delete(obj);
		return obj;
	}

	@Override
	public String updateShape(String tableName, String n, String e, Long id) {
		String errMsg = null;
		String sql = "update "
				+ tableName
				+ " set shape = ST_GEOMETRY(?,?,null,null,?) where objectid = ? ";
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createSQLQuery(sql);
		query.setParameter(0, CoordinateConversion.getPonitX(
				Double.parseDouble(e), Double.parseDouble(n)));
		query.setParameter(1, CoordinateConversion.getPonitY(
				Double.parseDouble(e), Double.parseDouble(n)));
		query.setParameter(2, querySrid(tableName.toUpperCase()));
		query.setParameter(3, id);
		query.executeUpdate();
		return errMsg;
	}

	public Long querySrid(String tableName) {
		Session session = sessionFactory.getCurrentSession();
		String sql = "select srid from st_geometry_columns where table_name = :tableName ";
		Query query = session.createSQLQuery(sql);
		query.setParameter("tableName", tableName.toUpperCase());
		return ((BigDecimal) query.uniqueResult()).longValue();
	}
	public Long querySridById(String tableName, Long id) {
		String sql = "SELECT st_srid(shape) FROM " + tableName
				+ " where objectid=" + id;
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createSQLQuery(sql);
		return ((BigDecimal) query.uniqueResult()).longValue();
	}
	public String updateShapeByRid(String tableName, String n, String e,
			Long rid) {
		String errMsg = null;
		String sql = "update " + tableName
				+ " set shape = ST_GEOMETRY(?,?,null,null,?) where rid = ? ";
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createSQLQuery(sql);
		query.setParameter(0, CoordinateConversion.getPonitX(
				Double.parseDouble(e), Double.parseDouble(n)));
		query.setParameter(1, CoordinateConversion.getPonitY(
				Double.parseDouble(e), Double.parseDouble(n)));
		query.setParameter(2, querySrid(tableName.toUpperCase()));
		query.setParameter(3, rid);
		query.executeUpdate();

		return errMsg;
	}
	@Override
	public String getLon(String tableName, Long id) {
		String sql = "SELECT st_minx(shape),st_miny(shape) FROM " + tableName
				+ " where objectid=" + id;
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createSQLQuery(sql);
		Double minx = 0d;
		Double miny = 0d;
		List<?> list = query.list();
		if (StringUtil.isNotEmptyOrNull(list.get(0))) {
			Object[] objs = (Object[]) list.get(0);
			if (StringUtil.isNotEmptyOrNull(objs[0])) {
				minx = ((BigDecimal) objs[0]).doubleValue();
			}
			if (StringUtil.isNotEmptyOrNull(objs[1])) {
				miny = ((BigDecimal) objs[1]).doubleValue();
			}
		}
		return CoordinateConversion.getLon(minx, miny);
	}

	@Override
	public String getLat(String tableName, Long id) {
		String sql = "SELECT st_minx(shape),st_miny(shape) FROM " + tableName
				+ " where objectid=" + id;
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createSQLQuery(sql);
		Double minx = 0d;
		Double miny = 0d;
		List<?> list = query.list();
		if (StringUtil.isNotEmptyOrNull(list.get(0))) {
			Object[] objs = (Object[]) list.get(0);
			if (StringUtil.isNotEmptyOrNull(objs[0])
					&& StringUtil.isNotEmptyOrNull(objs[1])) {
				minx = ((BigDecimal) objs[0]).doubleValue();
				miny = ((BigDecimal) objs[1]).doubleValue();
			}

		}
		return CoordinateConversion.getLat(minx, miny);
	}

	public Long nextId() {
		String sql = "select hibernate_sequence.nextval from dual";
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createSQLQuery(sql);
		return Long.parseLong(query.uniqueResult().toString());
	}
	public Double queryMinXById(String tableName, Long id) {
		String sql = "SELECT st_minx(shape) FROM " + tableName
				+ " where objectid=" + id;
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createSQLQuery(sql);
		Object obj = null;
		obj = query.uniqueResult();
		if (StringUtil.isNotEmptyOrNull(obj)) {
			return ((BigDecimal) obj).doubleValue();
		} else {
			return 0.0;
		}
	}

	public Double queryMaxXById(String tableName, Long id) {
		String sql = "SELECT st_maxx(shape) FROM " + tableName
				+ " where objectid=" + id;
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createSQLQuery(sql);
		Object obj = null;
		obj = query.uniqueResult();
		if (StringUtil.isNotEmptyOrNull(obj)) {
			return ((BigDecimal) obj).doubleValue();
		} else {
			return 0d;
		}
	}

	public Double queryMinYById(String tableName, Long id) {
		String sql = "SELECT st_miny(shape) FROM " + tableName
				+ " where objectid=" + id;
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createSQLQuery(sql);
		Object obj = null;
		obj = query.uniqueResult();
		if (StringUtil.isNotEmptyOrNull(obj)) {
			return ((BigDecimal) obj).doubleValue();
		} else {
			return 0d;
		}
	}

	public Double queryMaxYById(String tableName, Long id) {
		String sql = "SELECT st_maxy(shape) FROM " + tableName
				+ " where objectid=" + id;
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createSQLQuery(sql);
		Object obj = null;
		obj = query.uniqueResult();
		if (StringUtil.isNotEmptyOrNull(obj)) {
			return ((BigDecimal) obj).doubleValue();
		} else {
			return 0.0;
		}

	}

	@Override
	public Long selectNextSequence() {
		Session session = sessionFactory.getCurrentSession();
		String sq = session.createSQLQuery("select hibernate_sequence.nextval from dual").uniqueResult().toString();
		return Long.parseLong(sq);
	}

}
