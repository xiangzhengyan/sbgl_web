/**
 * 版权所有 2013 成都子非鱼软件有限公司 保留所有权利
 * 1 项目签约客户只拥有对项目业务代码的所有权，以及在本项目范围内使用平台框架
 * 2 平台框架及相关代码属子非鱼软件有限公司所有，未经授权不得扩散、二次开发及用于其它项目
 */
package com.zfysoft.platform.dao.impl;

import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.annotation.Resource;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import com.zfysoft.platform.dao.RoleDao;
import com.zfysoft.platform.model.Role;
import com.zfysoft.platform.model.RoleFunction;
import com.zfysoft.platform.model.User;
import com.zfysoft.platform.model.UserRole;

/**
 * @author chenhm
 * @date 2013-7-3
 */
@Repository
public class RoleDaoImpl implements RoleDao {

	@Resource
	private SessionFactory sessionFactory;
	
	//本地化工具类
	Locale local = Locale.getDefault(); //如果是中文系统,得到的是zh_CN
	ResourceBundle rb = ResourceBundle.getBundle("com/zfysoft/platform/local/base/i18n_role", local, RoleDaoImpl.class.getClassLoader());

	@SuppressWarnings("unchecked")
	@Override
	public List<Role> queryRoleList(String name) {
		
		Session session = sessionFactory.getCurrentSession();
		String hql = "select r from Role r ";
		if (!"".equals(name) && name != null)
			hql = hql + " where r.name like '%" + name + "%' or r.code like '%" + name + "%'";
		Query query = session.createQuery(hql);
		List<Role> list = query.list();
		
		return list;
	}

	@Override
	public Role queryRoleId(Long roleId) {
		
		Session session = sessionFactory.getCurrentSession();
		Role role = (Role) session.load(Role.class, roleId);
		
		return role;
	}

	@Override
	public String deleteRoleById(Long roleId) {

		Session session = sessionFactory.getCurrentSession();
		String msg = null;
		Role role = queryRoleId(roleId);
		if (role == null)
			msg = rb.getString("platform.role.notexsit");
		else {
			role.setEnable(new Long(0));
			try {
				session.update(role);
			} catch (Exception e) {
				msg = rb.getString("platform.role.delfail");
			}
		}
		return msg;
	}
	
	@Override
	public String addRole(Role role) {

		Session session = sessionFactory.getCurrentSession();
		String errMsg = null;
		try {
			session.save(role);
		} catch (Exception e) {
			errMsg = rb.getString("platform.role.addfail");
		}
		return errMsg;
	}

	@Override
	public String updateRole(Role role) {
		
		Session session = sessionFactory.getCurrentSession();
		String errMsg = null;
		try {
			session.update(role);
		} catch (Exception e) {
			errMsg = rb.getString("platform.role.modifyfail");
		}
		
		return errMsg;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Long> getRoleIdByUser(User loginUser) {
		
		Session session = sessionFactory.getCurrentSession();
		String hql = "select r.role.id from UserRole r where r.user.id = ?";
		Query query = session.createQuery(hql);
		query.setParameter(0, loginUser.getId());
		List<Long> list = query.list();
		
		return list;
	}

	@Override
	public void saveRoleFunction(RoleFunction roleFunction) {
		
		Session session = sessionFactory.getCurrentSession();
		session.save(roleFunction);
	}

	@Override
	public void deleteRoleFunction(RoleFunction roleFunction) {
		
		Session session = sessionFactory.getCurrentSession();
		session.delete(roleFunction);
	}

	@Override
	public RoleFunction getRoleFunctionByRoleAndFunctionId(Long roleId,
			Long functionId) {
		
		Session session = sessionFactory.getCurrentSession();
		String hql = "select r from RoleFunction r where r.role.id = ? and r.function.id = ?";
		Query query = session.createQuery(hql);
		query.setParameter(0, roleId);
		query.setParameter(1, functionId);
		
		return (RoleFunction) query.uniqueResult();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<RoleFunction> getRoleFunctionList(Long appId, Long roleId) {
		
		Session session = sessionFactory.getCurrentSession();
		String sql = "select p.* from p_role_function p inner join p_function f on p.function_id = f.id ";
		sql = sql + " where p.role_id = ? and f.app_id = ?";
		SQLQuery query = session.createSQLQuery(sql);
		query.setParameter(0, roleId);
		query.setParameter(1, appId);
		query.addEntity(RoleFunction.class);
		return query.list();
	}

	
	@Override
	public void deleteUserRoles(Long userId) {
		Session session = sessionFactory.getCurrentSession();
		SQLQuery query =  session.createSQLQuery("delete from p_user_role where user_id=?");
		query.setLong(0, userId);
		query.executeUpdate();
	}
	
	public void updateUserRoles(Long userId,Long[] roleIds){
		Session session = sessionFactory.getCurrentSession();
		User user = (User)session.get(User.class, userId);
		
		for(Long rid : roleIds){
			if(rid==null){
				continue;
			}
			Role role = (Role)session.get(Role.class, rid);
			UserRole ur = new UserRole();
			ur.setUser(user);
			ur.setRole(role);
			session.save(ur);
		}
	}

	@Override
	public Role getRoleByName(String name, Long id) {
		
		Session session = sessionFactory.getCurrentSession();
		String hql = "select r from Role r where r.name = :name";
		if (id != null)
			hql += " and r.id != :id";
		Query query = session.createQuery(hql);
		query.setParameter("name", name);
		if (id != null)
			query.setParameter("id", id);
		
		return (Role) query.uniqueResult();
	}

	@Override
	public Role queryRoleByCode(String code, Object object) {
		
		Session session = sessionFactory.getCurrentSession();
		String hql = "select r from Role r where r.code = :code";
		if (object != null)
			hql += " and r.id != :object";
		Query query = session.createQuery(hql);
		query.setParameter("code", code);
		if (object != null)
			query.setParameter("object", object);
		return (Role) query.uniqueResult();
	}
	
	/**
	 * 根据当前登录人，查询对应角色的主页
	 */
	@Override
	public String queryHomePage(User user){
//		TODO
		return null;
//		Session session = sessionFactory.getCurrentSession();
//		SQLQuery query = session.createSQLQuery("select max(url) from P_HOME_BY_ROLE home inner join P_USER_ROLE ur  on ur.role_id=home.role_id and ur.user_id=?");
//		query.setLong(0, user.getId());
//		Object url = query.uniqueResult();
//		if(url == null){
//			return null;
//		}else{
//			return url.toString();
//		}
	}



}
