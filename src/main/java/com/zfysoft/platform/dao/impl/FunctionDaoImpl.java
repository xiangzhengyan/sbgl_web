package com.zfysoft.platform.dao.impl;

import java.math.BigDecimal;
import java.util.List;

import javax.annotation.Resource;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import com.zfysoft.platform.dao.FunctionDao;
import com.zfysoft.platform.model.Favorite;
import com.zfysoft.platform.model.Function;
import com.zfysoft.platform.model.FunctionAction;
import com.zfysoft.platform.model.OrganizationFunction;
import com.zfysoft.platform.model.RoleFunction;
import com.zfysoft.platform.model.User;

@SuppressWarnings("unchecked")
@Repository
public class FunctionDaoImpl implements FunctionDao {

	@Resource
	private SessionFactory sessionFactory;

	@Override
	public List<Function> getAuthFunctionsByAppId(User loginUser, Long appId) {
		
		Session session = sessionFactory.getCurrentSession();
		String hql = "select f from Function f where f.enable = 1 and f.app.id = ? order by f.menuIndex";
		Query query = session.createQuery(hql);
		query.setLong(0, appId);
		List<Function> list = query.list();
		
		return list;
	}

	@Override
	public List<Function> getAuthAppsByUser(User loginUser) {
		
		Session session = sessionFactory.getCurrentSession();
		String hql = "select f from Function f where f.enable = 1 and f.app.id is null";
		Query query = session.createQuery(hql);
		List<Function> list = query.list();
		
		return list;
	}

	@Override
	public Function getFunctionById(Long id, Long enable) {
		
		Session session = sessionFactory.getCurrentSession();
		String hql = "select f from Function f where f.id = :id";
		if (enable != null)
			hql += " and f.enable = :enable";
		Query query = session.createQuery(hql);
		query.setParameter("id", id);
		if (enable != null)
			query.setParameter("enable", enable);
		
		return (Function) query.uniqueResult();
	}

	@Override
	public List<Long> getFunctionIdByRole(List<Long> roleIdList) {
		
		Session session = sessionFactory.getCurrentSession();
		String hql = "select f.function.id from RoleFunction f where f.role.id in (:d)";
		Query query = session.createQuery(hql);
		query.setParameterList("d", roleIdList);
		List<Long> list = query.list();
		return list;
	}

	@Override
	public List<Function> getAuthAppsByIdList(List<Long> functionIdList) {
		
		Session session = sessionFactory.getCurrentSession();
		String hql = "select f from Function f where f.enable = 1 and f.id in (:d) and f.app.id is null";
		Query query = session.createQuery(hql);
		query.setParameterList("d", functionIdList);
		List<Function> list = query.list();
		
		return list;
	}

	@Override
	public List<Function> getAuthFunctionsByAppIdAndIdList(Long appId,
			List<Long> functionIdList) {
		
		Session session = sessionFactory.getCurrentSession();
		String hql = "select f from Function f where f.enable = 1 and f.id in (:d) and f.app.id = :appId order by  f.menuIndex";
		Query query = session.createQuery(hql);
		query.setParameterList("d", functionIdList);
		query.setParameter("appId", appId);
		List<Function> list = query.list();
		
		return list;
	}

	@Override
	public List<Function> getLevelFunctionsByAppId(Object object, Long appId,
			Long menuLevel) {
		
		Session session = sessionFactory.getCurrentSession();
		String hql = "select f from Function f where f.enable = 1 and f.app.id = ? and f.menuLevel = ? order by f.menuIndex";
		Query query = session.createQuery(hql);
		query.setLong(0, appId);
		query.setLong(1, menuLevel);
		List<Function> list = query.list();
		
		return list;
	}

	@Override
	public List<Long> getAuthCodes(Long appId, Long roleId) {
		
		Session session = sessionFactory.getCurrentSession();
		String hql = "select rf.function.id from RoleFunction rf where rf.function.app.id = ?";
		if (roleId != null) 
			hql += " and rf.role.id = ? ";
		Query query = session.createQuery(hql);
		query.setLong(0, appId);
		if (roleId != null) 
			query.setLong(1, roleId);
		List<Long> list = query.list();
		
		return list;
	}

	@Override
	public List<Function> getSubFunctions(Long functionId, Long status, List<Long> idList) {
		
		Session session = sessionFactory.getCurrentSession();
		String hql = "select f from Function f where f.parent.id = :parentId ";
		if (idList != null && idList.size() > 0) {
			hql += " and f.id in (:idList)";
		}
		hql += " order by f.menuIndex";
		Query query = session.createQuery(hql);
		query.setParameter("parentId", functionId);
		if (idList != null && idList.size() > 0) {
			query.setParameterList("idList", idList);
		}
		List<Function> list = query.list();
		
		return list;
	}

	@Override
	public List<Function> getFavFunctionByAppIdAndIdList(Long appId,
			List<Long> functionIdList, Long userId) {
		
		Session session = sessionFactory.getCurrentSession();
		String sql = "select f.* ";
		sql = sql + " from P_FAVORITE t inner join p_function f on t.function_id = f.id ";
		sql = sql + " where f.enable = 1 and f.id in (:functionIdList)";
		sql = sql + " and t.user_id = :userId";
		SQLQuery query = session.createSQLQuery(sql);
		// 收藏夹不针对某一子系统
		//query.setLong("appId", appId);
		query.setParameterList("functionIdList", functionIdList);
		query.setLong("userId", userId);
		query.addEntity(Function.class);
		List<Function> list = query.list();
		
		return list;
	}

	@Override
	public String getAppNameByFunctionId(Long functionId) {
		
		Session session = sessionFactory.getCurrentSession();
		String sql = " select label from p_function where id = (select app_id from p_function where id = :functionId)";
		SQLQuery query = session.createSQLQuery(sql);
		query.setLong("functionId", functionId);
		
		return (String) query.uniqueResult();
	}

	@Override
	public List<Function> getFavParentName(Long functionId) {
		
		Session session = sessionFactory.getCurrentSession();
		String sql = "select * from p_function where id = :functionId";
		SQLQuery query = session.createSQLQuery(sql);
		query.setLong("functionId", functionId);
		query.addEntity(Function.class);
		List<Function> list = query.list();
		
		return list;
	}

	@Override
	public int getCountByFunctionIdAndUserId(Long userId, Long functionId) {
		
		Session session = this.sessionFactory.getCurrentSession();
		String sql = "select count(*) from P_FAVORITE where user_id = :userId and function_id = :functionId";
		SQLQuery query = session.createSQLQuery(sql);
		query.setParameter("userId", userId);
		query.setParameter("functionId", functionId);
		return Integer.parseInt(query.uniqueResult().toString());
	}

	@Override
	public void addFavFunction(Favorite fav) {

		Session session = sessionFactory.getCurrentSession();
		session.save(fav);
	}

	@Override
	public void deleteFavFunction(Long userId, Long functionId) {

		Session session = sessionFactory.getCurrentSession();
		String sql = "delete from P_FAVORITE where user_id = :userId and function_id = :functionId";
		SQLQuery query = session.createSQLQuery(sql);
		query.setParameter("userId", userId);
		query.setParameter("functionId", functionId);
		
		query.executeUpdate();
	}

	@Override
	public Long getAppIdByFunctionId(Long functionId) {
		
		Session session = sessionFactory.getCurrentSession();
		String sql = "select app_id from p_function where id = :functionId";
		SQLQuery query = session.createSQLQuery(sql);
		query.setParameter("functionId", functionId);
 		
		return ((BigDecimal) query.uniqueResult()).longValue();
	}

	@Override
	public List<Function> getAllFunctionsByAppId(Long appId) {
		
		Session session = sessionFactory.getCurrentSession();
		String sql = "select * from p_function where app_id = :appId order by menu_level, menu_index";
		String sql2 = "select * from p_function where id = :appId";
		SQLQuery query = session.createSQLQuery(sql);
		SQLQuery query2 = session.createSQLQuery(sql2);
		query.setParameter("appId", appId);
		query2.setParameter("appId", appId);
		query.addEntity(Function.class);
		query2.addEntity(Function.class);
		Function func = (Function) query2.list().get(0);
		List<Function> list =  query.list();
		list.add(func);
		return list;
	}

	@Override
	public List<Function> getChildren(Long functionId, boolean isApp) {
		
		Session session = sessionFactory.getCurrentSession();
		String sql = "";
		if (isApp)
			sql = "select * from p_function where app_id = :functionId and parent_id is null";
		else 
			sql = "select * from p_function where parent_id = :functionId";
		SQLQuery query = session.createSQLQuery(sql);
		query.setParameter("functionId", functionId);
		query.addEntity(Function.class);
		
		return query.list();
	}

	@Override
	public Function addFunction(Function func) {
		
		Session session = sessionFactory.getCurrentSession();
		session.save(func);
		
		return func;
	}

	@Override
	public Function updateFunction(Function function) {
		
		Session session = sessionFactory.getCurrentSession();
		session.update(function);
		
		return function;
	}

	@Override
	public void deleteFunction(Function function) {

		Session session = sessionFactory.getCurrentSession();
		session.delete(function);
	}

	@Override
	public void deleteRoleFunc(RoleFunction rf) {

		Session session = sessionFactory.getCurrentSession();
		session.delete(rf);
	}

	@Override
	public void deleteOrgFunc(OrganizationFunction of) {
		
		Session session = sessionFactory.getCurrentSession();
		session.delete(of);
	}
	
	/**
	 * 得到角色菜单关系
	 * @param functionId
	 */
	public RoleFunction getRoleFunction(Long roleId,Long funId){
		Session session = sessionFactory.getCurrentSession();
		RoleFunction rf = (RoleFunction) session.createQuery("from RoleFunction where role.id=? and function.id=?")
				.setLong(0, roleId)
				.setLong(1, funId).uniqueResult();
		return rf;
	}
	
	@Override
	public List<Function> getFistLevelsByidList(List<Long> functionIdList, Long appId) {
		
		Session session = sessionFactory.getCurrentSession();
		String hql = "select f from Function f where f.enable = 1 and f.id in (:d) and f.app.id = :appId and f.menuLevel = 1 order by f.menuIndex";
		Query query = session.createQuery(hql);
		query.setParameterList("d", functionIdList);
		query.setParameter("appId", appId);
		List<Function> list = query.list();
		
		return list;
	}
	
	@Override
	public List<Function> getLevelFunctionsByParentId(Object object, Long parentId, Long menuLevel, List<Long> list) {
		
		Session session = sessionFactory.getCurrentSession();
		String hql = "select f from Function f where f.enable = 1 and f.parent.id = :parentId and f.menuLevel = :menuLevel ";
		if (list != null && list.size() > 0) {
			hql += " and f.id in (:list)";
		}
		hql += " order by f.menuIndex";
		Query query = session.createQuery(hql);
		query.setParameter("parentId", parentId);
		query.setParameter("menuLevel", menuLevel);
		if (list != null && list.size() > 0) {
			query.setParameterList("list", list);
		}
		List<Function> alist = query.list();
		
		return alist;
	}

	@Override
	public List<FunctionAction> getActionByFunctionId(Long functionId) {
		Session session = sessionFactory.getCurrentSession();
		String hql = "select f from FunctionAction f where f.function.id = :functionId";
		Query query = session.createQuery(hql);
		query.setParameter("functionId", functionId);
		List<FunctionAction> list = query.list();
		return list;
	}

	@Override
	public FunctionAction getActionById(Long actionId) {
		
		Session session = sessionFactory.getCurrentSession();
		String hql = "select f from FunctionAction f where f.id = :actionId";
		Query query = session.createQuery(hql);
		query.setParameter("actionId", actionId);
		return (FunctionAction) query.uniqueResult();
	}

	@Override
	public FunctionAction saveOrUpdate(FunctionAction action) {
		
		Session session = sessionFactory.getCurrentSession();
		session.saveOrUpdate(action);
		return action;
	}

	@Override
	public void deleteAction(Long id) {

		Session session = sessionFactory.getCurrentSession();
		session.delete(this.getActionById(id));
	}

	@Override
	public List<Function> getFunctionsByUserId(Long userid) {
		Session session = sessionFactory.getCurrentSession();
		
//		String sql = "select * from p_function where id in(select distinct fo.function_id from P_ORGANIZATION o inner join p_organization_function fo on fo.organization_id = o.id "
//                   +" where o.id in (SELECT id FROM P_ORGANIZATION START WITH id in(select t.organization_id from p_organization_user t where user_id = :id) CONNECT BY PRIOR parent_id = id) "
//                   +" union all select distinct rf.function_id from p_role_function rf inner join p_user_role ur on ur.role_id=rf.role_id where ur.user_id = :id) order by menu_Index ";
		
		//TODO 组织机构权限不继承
		String sql = "select * from p_function where id in(select distinct fo.function_id from P_ORGANIZATION o inner join p_organization_function fo on fo.organization_id = o.id "
				 +" where o.id in (select t.organization_id from p_organization_user t where user_id = :id) "
                   +" union all select distinct rf.function_id from p_role_function rf inner join p_user_role ur on ur.role_id=rf.role_id where ur.user_id = :id) order by menu_Index ";
		
		Query query = session.createSQLQuery(sql).addEntity(Function.class).setLong("id", userid);
		return query.list();
	}
}
