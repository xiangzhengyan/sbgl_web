package com.zfysoft.platform.dao.impl;

import java.util.List;

import javax.annotation.Resource;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.zfysoft.common.util.Page;
import com.zfysoft.common.util.StringUtil;
import com.zfysoft.platform.dao.UserDao;
import com.zfysoft.platform.model.Role;
import com.zfysoft.platform.model.User;
import com.zfysoft.platform.util.IdUtil;

@Repository
public class UserDaoImpl implements UserDao {
	
	@Resource
	private SessionFactory sessionFactory;
	
	/**
	 * 根据用户名查询user
	 * @param userAlias
	 * @return list
	 */
	@SuppressWarnings("unchecked")
	public List<User> getUserByUserAlias(String userAlias) {
		
		Session session = this.sessionFactory.getCurrentSession();
		String hql = "from User where LOWER(loginName) = ?";
		Query query = session.createQuery(hql);
		query.setParameter(0, userAlias);
		List<User> list = query.list();
		
		return list;
	}
	
	@Override
	public int queryUserCount(User user) {
		Session session = this.sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(User.class);
		criteria.setProjection(Projections.rowCount());		//求总行数
		String keyWords = user.getKeyWords();
		if (keyWords != null && !"".equals(keyWords.trim())){
			criteria.add(Restrictions.or(
					Restrictions.like("loginName", "%" + keyWords + "%"),
					Restrictions.like("realName", "%" + keyWords + "%"),
					Restrictions.like("email", "%" + keyWords + "%")));
			
		}
		Long total = (Long) criteria.uniqueResult();
		return total.intValue();
	}
	
	@Override
	public List<?> queryUserList(User user, Page page) {
		int begin = (page.getPageNum() - 1) * page.getPageSize();
		/*
		String whereSqL = "where 1 = 1";
		if (user.getName() != null && !"".equals(user.getName()))
			whereSqL += " and NAME LIKE '%"+user.getName()+"%'";
		if (user.getRealName() != null && !"".equals(user.getRealName())){
			whereSqL += " and REAL_NAME LIKE '%"+user.getRealName()+"%'";
		}
		String sql = "select * from (select a.* ,rownum no from (" +
				"select * from xt_zzjg_users  " + whereSqL + " order by ID DESC) a where rownum <= ?" +
				") where no > ?";
		*/
		Session session = this.sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(User.class);
		String keyWords = user.getKeyWords();
		if (keyWords != null && !"".equals(keyWords.trim())){
			criteria.add(Restrictions.or(
					Restrictions.like("loginName", "%" + keyWords + "%"),
					Restrictions.like("realName", "%" + keyWords + "%"),
					Restrictions.like("email", "%" + keyWords + "%")));
			
		}
		/*其它排序*/
		if(StringUtil.isNotEmptyOrNull(page.getSortCol())){
			if(page.getSortOrder()!=null&&!page.getSortOrder().equalsIgnoreCase("ASC")){
				criteria.addOrder(Order.desc(page.getSortCol()));	//根据参数列名排序
			}else{
				criteria.addOrder(Order.asc(page.getSortCol()));	//根据参数列名排序
			}
		/*默认排序*/
		}else{
			criteria.addOrder(Order.desc("id"));	//根据id排序
		}
		List<?> pagelist = criteria.setFirstResult(begin).setMaxResults(page.getPageSize()).list();
		return pagelist;
	}

	
	@Override
	public void mgrUser(User user) {
		Session session = this.sessionFactory.getCurrentSession();
		if(user.getId()==null){
			user.setId(IdUtil.getId());
		}
		session.saveOrUpdate(user);
	}

	
	@Override
	public User getById(Long id) {
		Session session = this.sessionFactory.getCurrentSession();
		User user = (User) session.get(User.class, id);
		return user;
	}

	
	/*@Override
	public void grantRoleToUser(Long userId, Long[] roleIds) {
		Session session = this.sessionFacotry.getCurrentSession();
		User user = new User();
		user.setId(userId);
		for (Long roleId:roleIds){
			UserRoleMap userRoleMap = new UserRoleMap();
			Role role = new Role();
			role.setId(roleId);
			userRoleMap.setUser(user);
			userRoleMap.setRole(role);
			//先查询是否已经拥有该角色
			
			session.save(userRoleMap);
		}
		session.close();
	}*/

	
	@Override
	@SuppressWarnings("unchecked")
	public List<Role> queryUserRoles(Long userId) {
		 /* 
		 *	1。如果没有在Hibernate配置里做关联，在Hql里面是不能用join的。
		 *	2。Hql里面的join是没有"ON" 子句的。
		 *	3。如果是inner join，Join的依据就是事先设计好关联
		 *	4。如果要用left join或right join，那么要采用referece的方式来写这个语句。
		 *	   比如：FROM a LEFT JOIN a.b
		 *	   这里一定要写成 "a.b"
		 *	5。有fetch的时候每条记录只有一个Object，那就是FROM那个表，所有其他表的数据都要通过referece来获取，
		 *	   没有fetch的时候返回的是一个Object[]，每个表都是一个对象。 (right join 不能用 fetch) 。比如：FROM a LEFT JOIN FETCH a.b

		 */
		//String hql = "select m.role from UserRoleMap m inner join m.role where m.user.id = ? and m.role.enable = 1";
		String sql = " select role.* from p_role role"
				+" inner join P_USER_ROLE map on map.role_id=role.id"
				+" inner join p_user u on u.id=map.user_id"
				+" where u.id=?";
		Session session = this.sessionFactory.getCurrentSession();
		List<Role> list =  session.createSQLQuery(sql).addEntity(Role.class).setLong(0, userId).list();
		return list;
	}

	
	@Override
	public void revokeRoleOnUser(Long userId) {
		String hql = "delete from UserRoleMap m where  m.user.id = ?";
		Session session = this.sessionFactory.getCurrentSession();
		session.createQuery(hql).setLong(0, userId).executeUpdate();
	}
	
	@SuppressWarnings("unchecked")
	public String queryLoginNameById(Long id){
		Session session = this.sessionFactory.getCurrentSession();
		SQLQuery query = session.createSQLQuery("select login_name from p_user where id=?");
		query.setLong(0, id);
		List<String> list = query.list();
		if(list != null && !list.isEmpty()){
			return list.get(0);
		}else{
			return null;
		}
	}

	@Override
	public void updateUser(User user) {
		Session session = this.sessionFactory.getCurrentSession();
		session.saveOrUpdate(user);
	}

	 
	 
}
