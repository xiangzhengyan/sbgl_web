/**
 * 版权所有 2013 成都子非鱼软件有限公司 保留所有权利
 * 1 项目签约客户只拥有对项目业务代码的所有权，以及在本项目范围内使用平台框架
 * 2 平台框架及相关代码属子非鱼软件有限公司所有，未经授权不得扩散、二次开发及用于其它项目
 */
package com.zfysoft.platform.dao.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import com.zfysoft.platform.dao.ActionDao;
import com.zfysoft.platform.dao.OrganizationDao;
import com.zfysoft.platform.model.FunctionAction;
import com.zfysoft.platform.model.OrganizationAction;
import com.zfysoft.platform.model.RoleAction;
import com.zfysoft.platform.model.RoleFunction;
import com.zfysoft.platform.model.User;

/**
 * @author hudt
 * @date 2013-7-30
 */
@Repository
public class ActionDaoImpl implements ActionDao {

	@Resource
	private SessionFactory sessionFactory;

	@Resource
	private OrganizationDao organizationDao;

	@Override
	public void save(RoleAction buttonRoleFun) {
		Session session = sessionFactory.getCurrentSession();
		session.save(buttonRoleFun);
	}

	@Override
	public void save(OrganizationAction buttonOrgaFun) {
		Session session = sessionFactory.getCurrentSession();
		session.save(buttonOrgaFun);
	}

	@Override
	public void delete(RoleAction buttonRoleFun) {
		Session session = sessionFactory.getCurrentSession();
		session.delete(buttonRoleFun);
	}

	@Override
	public void delete(OrganizationAction buttonOrgaFun) {
		Session session = sessionFactory.getCurrentSession();
		session.delete(buttonOrgaFun);
	}

	@Override
	public FunctionAction queryById(Long id) {
		Session session = sessionFactory.getCurrentSession();
		FunctionAction action = (FunctionAction) session.get(FunctionAction.class, id);
		return action;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<FunctionAction> queryByFunId(Long funId) {
		Session session = sessionFactory.getCurrentSession();
		List<FunctionAction> list = session
				.createQuery("from FunctionAction where function.id=?")
				.setLong(0, funId).list();
		return list;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<FunctionAction> queryByRoleId(Long roleId) {
		Session session = sessionFactory.getCurrentSession();
		String hql = "select brf.action from RoleAction brf where brf.roleFunction.role.id=?";
		List<FunctionAction> list = session.createQuery(hql).setLong(0, roleId).list();
		return list;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<FunctionAction> queryByRoleIdAndFunId(Long roleId, Long functionId) {
		
		Session session = sessionFactory.getCurrentSession();
		String hql = "select brf.action from RoleAction brf where brf.roleFunction.role.id=?";
		hql += " and brf.roleFunction.function.id=?";
		List<FunctionAction> list = session.createQuery(hql).setLong(0, roleId)
				.setLong(1, functionId).list();
		return list;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<FunctionAction> queryByOrgaId(Long orgaId) {
		Session session = sessionFactory.getCurrentSession();
		String hql = "select brf.action from OrganizationAction bof where bof.orgaFun.organization.id=?";
		List<FunctionAction> list = session.createQuery(hql).setLong(0, orgaId).list();
		return list;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<FunctionAction> queryByOrgaIdAndFunId(Long orgaId, Long functionId) {
		Session session = sessionFactory.getCurrentSession();
		String hql = "select bof.action from OrganizationAction bof where bof.orgaFun.organization.id=?";
		hql += " and bof.orgaFun.function.id=?"; 
		List<FunctionAction> list = session.createQuery(hql).setLong(0, orgaId)
				.setLong(1, functionId).list();
		return list;
	}

	@Override
	public void deleteInRole(Long roleId, Long funId) {
		Session session = sessionFactory.getCurrentSession();
		String sql = " delete from p_role_action where id in ( select bmr.id from p_role_action bmr inner join p_role_function mr on mr.id=bmr.role_fun_id"
				+ " where mr.role_id=? and mr.function_id=? )";
		SQLQuery query = session.createSQLQuery(sql);
		query.setLong(0, roleId);
		query.setLong(1, funId);
		query.executeUpdate();
	}

	@Override
	public void deleteInOrga(Long orgaId, Long funId) {
		Session session = sessionFactory.getCurrentSession();
		String sql = " delete from p_organization_action where id in (select bor.id from p_organization_action bor inner join p_organization_function pof on pof.id=bor.orga_fun_id"
				+ " where pof.organization_id=? and pof.function_id=? )";
		SQLQuery query = session.createSQLQuery(sql);
		query.setLong(0, orgaId);
		query.setLong(1, funId);
		query.executeUpdate();
	}

	/**
	 * 打开某个功能页面是，查询按钮权限
	 * 根据当前登录人，以及需要访问的菜单，找到已经被授权的Buttons
	 */
	@SuppressWarnings("unchecked")
	public List<String> queryPermissionButtonCodes(User loginUser, String funUrl) {
		Session session = sessionFactory.getCurrentSession();
		StringBuffer sql = new StringBuffer();
		
//		TODO xzy  组织机构权限不继承
//		sql.append("select code from p_function_action where id in(select ra.action_id from p_role_function rf inner join p_user_role ur on ur.role_id=rf.role_id inner join p_role_action ra on ra.role_fun_id = rf.id inner join p_function f on f.id = rf.function_id ");
//		sql.append(" where user_id=:uid and f.url=:url union all");
//		sql.append(" select oa.action_id from P_ORGANIZATION o  inner join p_organization_function fo on fo.organization_id = o.id inner join p_organization_action oa on oa.orga_fun_id=fo.id inner join p_function f on f.id = fo.function_id");
//		sql.append(" where  f.url=:url and o.id in (SELECT id FROM P_ORGANIZATION START WITH id in(select t.organization_id from p_organization_user t where user_id = :uid) CONNECT BY PRIOR parent_id = id))");
		
		sql.append("select code from p_function_action where id in(select ra.action_id from p_role_function rf inner join p_user_role ur on ur.role_id=rf.role_id inner join p_role_action ra on ra.role_fun_id = rf.id inner join p_function f on f.id = rf.function_id ");
		sql.append(" where user_id=:uid and f.url=:url union all");
		sql.append(" select oa.action_id from P_ORGANIZATION o  inner join p_organization_function fo on fo.organization_id = o.id inner join p_organization_action oa on oa.orga_fun_id=fo.id inner join p_function f on f.id = fo.function_id");
		sql.append(" where  f.url=:url and o.id in (select t.organization_id from p_organization_user t where user_id = :uid)");

		
		SQLQuery query = session.createSQLQuery(sql.toString());
		query.setLong("uid", loginUser.getId());
		query.setString("url", funUrl);
		List<Object> list = query.list();
		if(list == null){
			return null;
		}
		List<String> listCode = new ArrayList<String>();
		for(Object obj : list){
			listCode.add(obj==null?"":obj.toString());
		}
		return listCode;
	}

	/**
	 * 组织机构管理页面使用
	 */
	@SuppressWarnings("unchecked")
	@Override
	public  List<FunctionAction> queryPermissionButtons(User loginUser,String funUrl){
		Session session = sessionFactory.getCurrentSession();
		StringBuffer sql = new StringBuffer();
		sql.append("select code from p_function_action where id in(select ra.action_id from p_role_function rf inner join p_user_role ur on ur.role_id=rf.role_id inner join p_role_action ra on ra.role_fun_id = rf.id inner join p_function f on f.id = rf.function_id ");
		sql.append(" where user_id=:uid and f.url=:url" );
		
//		TODO xzy  组织机构权限不继承
		sql.append(" union all ");
		sql.append(" select oa.action_id from P_ORGANIZATION o  inner join p_organization_function fo on fo.organization_id = o.id inner join p_organization_action oa on oa.orga_fun_id=fo.id inner join p_function f on f.id = fo.function_id");
		sql.append(" where  f.url=:url and o.id in (select t.organization_id from p_organization_user t where user_id = :uid)");

		sql.append(")");
		SQLQuery query = session.createSQLQuery(sql.toString());
		query.addEntity(FunctionAction.class);
		query.setLong("uid", loginUser.getId());
		query.setString("url", funUrl);
		List<FunctionAction> list = query.list();
		return list;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<RoleAction> queryByRoleFunction(RoleFunction roleFunction) {
		
		Session session = this.sessionFactory.getCurrentSession();
		String hql = " select brf from RoleAction brf where brf.roleFunction=?";
		Query query = session.createQuery(hql);
		query.setParameter(0, roleFunction);
		
		return query.list();
	}

}
