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
import org.hibernate.type.LongType;
import org.hibernate.type.StringType;
import org.springframework.stereotype.Repository;

import com.zfysoft.common.util.Page;
import com.zfysoft.common.util.StringUtil;
import com.zfysoft.platform.dao.OrganizationDao;
import com.zfysoft.platform.model.Organization;
import com.zfysoft.platform.model.OrganizationFunction;
import com.zfysoft.platform.model.OrganizationUser;
import com.zfysoft.platform.model.User;
import com.zfysoft.platform.util.IdUtil;

/**
 * 组织机构DaoImpl
 * @author hudt
 * @date 2013-7-18
 */
@Repository
public class OrganizationDaoImpl implements OrganizationDao {
	
	@Resource
	private SessionFactory sessionFactory;
	
	/**
	 * 通过ID得到组织机构
	 * @param 组织机构主键
	 * @return
	 */
	public Organization getOrgaById(Long id){
		
		Session session = sessionFactory.getCurrentSession();
		String hql = "select t from Organization t where t.id = :id";
		Query query = session.createQuery(hql);
		query.setParameter("id", id);
		Organization orga = (Organization) query.uniqueResult();
		
		return orga;
	}

	
	/**
	 * 增加组织机构
	 * @param 
	 * @return
	 */
	public void saveOrga(Organization orga){
		Session session = sessionFactory.getCurrentSession();
		if(orga.getCode()==null){
			//自动创建code
			Organization parent = orga.getParent();
			if(parent!=null){
				String  parentCode = parent.getCode();
				String sql = "select group_concat(substr(code,-2)) as codes from p_organization where parent_id =:parent_id";
				SQLQuery query = session.createSQLQuery(sql).addScalar("codes", StringType.INSTANCE);
				query.setLong("parent_id", parent.getId());
				String codes = (String)query.uniqueResult();
				if(codes==null){
					orga.setCode(parentCode+"01");
				}else{
					for (int i = 1; i <=99; i++) {
						String subCode = i+"";
						if(subCode.length()==1){
							subCode = "0"+subCode;
						}
						if(!codes.contains(subCode)){
							orga.setCode(parentCode+subCode);
							break;
						}
					}
				}
			}
			
			
			
		}
		orga.setId(IdUtil.getId());
		session.save(orga);
	}
	

	/**
	 * 修改组织机构
	 * @param 
	 * @return
	 */
	public void updateOrga(Organization orga){
		Session session = sessionFactory.getCurrentSession();
		session.update(orga);
	}
	

	/**
	 * 删除组织机构
	 * @param 
	 * @return
	 */
	public void deleteOrgaById(Long id){
		deleteOrgaByPid(id); 
		Session session = sessionFactory.getCurrentSession();
		Organization orga = (Organization)session.get(Organization.class, id);
		if(orga != null){
			session.delete(orga);
		}
		String sql = "delete from P_ORGANIZATION_USER WHERE ORGANIZATION_ID=?"; 
        Query query = session.createSQLQuery(sql).setLong(0, id); 
        query.executeUpdate();
        sql = "delete from P_ORGANIZATION_FUNCTION WHERE ORGANIZATION_ID=?"; 
        query = session.createSQLQuery(sql).setLong(0, id); 
        query.executeUpdate();
	}
	//删除子组织
	@SuppressWarnings("unchecked")
	private void deleteOrgaByPid(Long parentId){
		Session session = sessionFactory.getCurrentSession();
		List<Organization> list = session.createQuery("from Organization where parent.id=?")
									.setLong(0, parentId).list();
		if(list==null || list.isEmpty()){
			return;
		}else{
			for(Organization orga : list){
				Long id = orga.getId();
				deleteOrgaByPid(id);
				String sql = "delete from P_ORGANIZATION_USER WHERE ORGANIZATION_ID=?"; 
		        Query query = session.createSQLQuery(sql).setLong(0, orga.getId()); 
		        query.executeUpdate();
		        sql = "delete from P_ORGANIZATION_FUNCTION WHERE ORGANIZATION_ID=?"; 
		        query = session.createSQLQuery(sql).setLong(0, orga.getId()); 
		        query.executeUpdate(); 
				session.delete(orga);
			}
		}
	}
	
	/**
	 * 查询所有组织机构
	 */
	@SuppressWarnings("unchecked")
	public List<Organization> queryAllOrga(){
		Session session = sessionFactory.getCurrentSession();
		List<Organization> list = session.createQuery("from Organization").list();
		return list;
	}
	
	@SuppressWarnings("unchecked")
	public List<Organization> queryAllOrga(long id){
		Session session = sessionFactory.getCurrentSession();
		Organization root =this.getOrgaById(id);
		if(root!=null){
			List<Organization> list = session.createQuery("from Organization where code like '"+root.getCode()+"%'").list();
			return list;
		}
		return new ArrayList<Organization>();
	}

	/**
	 * 根据ID查询其第一层子组织机构
	 */
	@SuppressWarnings("unchecked")
	public List<Organization> querySubOrgaById(Long id){
		Session session = sessionFactory.getCurrentSession();
		List<Organization> list = session.createQuery("from Organization where parent.id=?") 
				.setLong(0, id).list();
		return list;
	}

	
	/**
	 * 根据类型code
	 * 查询所有属于该code的组织机构
	 */
	@SuppressWarnings("unchecked")
	public List<Organization> queryAllOrgaByTypeCode(String code){
		Session session = sessionFactory.getCurrentSession();
		List<Organization> list = session.createQuery("from Organization where type=?")
				.setString(0, code).list();
		return list;
	}
	
	/**
	 * 查询根Orga
	 */
	@SuppressWarnings("unchecked")
	public List<Organization> queryRootOrga(){
		Session session = sessionFactory.getCurrentSession(); 
		List<Organization> list = session.createQuery("from Organization where parent.id is null").list();
		return list;
	}
	
	/**
	 * 根据ID查询其所有子孙组织机构
	 */
	public List<Organization> queryAllSubOrgaById(Long id){
		List<Organization> list = new ArrayList<Organization>();
		List<Organization> sublist = querySubOrgaById(id);
		if(sublist!=null && !sublist.isEmpty()){
			list.addAll(sublist);
			for(Organization orga : sublist){
				list.addAll(queryAllSubOrgaById(orga.getId()));
			}
		}
		return list;
	}

	/**
	 * 根据父code查询所有子code
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<String> queryAllSubTypeByCode(String code) {
		Session session = sessionFactory.getCurrentSession();
		List<String> list = session.createSQLQuery("select sub_code from p_organization_type where parent_code=?")
				.setString(0, code)
				.list();
		return list;
	}

	/**
	 * 根据子code查询所有父code
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<String> queryAllParentTypeBySubcode(String subCode) {
		Session session = sessionFactory.getCurrentSession();
		List<String> list = session.createSQLQuery("select parent_code from p_organization_type where sub_code=?")
				.setString(0, subCode)
				.list();
		return list;
	}
		
	/**
	 * 根据父code查询所有真实存在的子code
	 */
	public List<String> queryAllRealExistSubTypeByCode(String code){
		//Session session = sessionFacotry.getCurrentSession();
		return null;
	}
	
	
	/////////////////////////////////////////
	////以下是和用户的关系查询
	
	
	/**
	* 根据组织机构ID查询此组织机构下有哪些人
	*/
	@SuppressWarnings("unchecked")
	public List<User> queryUserListByOrgaId(Long orgaId,Page page){
		Session session = sessionFactory.getCurrentSession();
		String sql = "select u.* from P_USER u inner join p_organization_user ou on u.id = ou.user_id"
			+" inner join P_ORGANIZATION o on o.id = ou.ORGANIZATION_id"
			+" where o.id=? and u.status=1 order by u.id";
		SQLQuery query = session.createSQLQuery(sql).addEntity(User.class);
		query.setLong(0, orgaId);
		query.setFirstResult((page.getPageNum()-1)*page.getPageSize());
		query.setMaxResults(page.getPageSize());
		List<User> list = query.list();
		//总页数
		sql = "select count(u.id) from P_USER u inner join p_organization_user ou on u.id = ou.user_id"
				+" inner join P_ORGANIZATION o on o.id = ou.ORGANIZATION_id"
				+" where o.id=? and u.status=1";
		query = session.createSQLQuery(sql);
		query.setLong(0, orgaId);
		page.setTotalRows(Integer.valueOf(query.uniqueResult().toString()));
		return list;
	}
	
	/**
	* 根据组织机构ID查询此组织机构下有哪些人
	*/
	@SuppressWarnings("unchecked")
	public List<User> queryUserListNotInByOrgaId(Long orgaId,Page page,String usernameFilter){
		Session session = sessionFactory.getCurrentSession();
		String sql = "select * from P_USER where id not in(select u.id from P_USER u inner join p_organization_user ou on u.id = ou.user_id"
			+" inner join P_ORGANIZATION o on o.id = ou.ORGANIZATION_id"
			+" where o.id=? ) and status=1 ";
		if(StringUtil.isNotEmptyOrNull(usernameFilter)){
			sql += " and (real_name like '%"+usernameFilter+"%' or login_name like '%"+usernameFilter+"%')";
		}
		sql += "order by id";
		SQLQuery query = session.createSQLQuery(sql).addEntity(User.class);
		query.setLong(0, orgaId);
		query.setFirstResult((page.getPageNum()-1)*page.getPageSize());
		query.setMaxResults(page.getPageSize());
		List<User> list = query.list();
		//总页数
		sql = "select count(id) from P_USER where id not in(select u.id from P_USER u inner join p_organization_user ou on u.id = ou.user_id"
				+" inner join P_ORGANIZATION o on o.id = ou.ORGANIZATION_id"
				+" where o.id=?) and status=1 ";
		if(StringUtil.isNotEmptyOrNull(usernameFilter)){
			sql += " and (real_name like '%"+usernameFilter+"%' or login_name like '%"+usernameFilter+"%')";
		}
		query = session.createSQLQuery(sql);
		query.setLong(0, orgaId);
		page.setTotalRows(Integer.valueOf(query.uniqueResult().toString()));
		return list;
	}
	
	/**
	* 把用户分配到组织机构
	*/
	public void saveUserOrga(OrganizationUser orgaUser){
		Session session = sessionFactory.getCurrentSession();
		session.save(orgaUser);
	}
	
	/**
	 * 查询用户组织机构关系
	 */
	public OrganizationUser queryOrgaUser(Long userId,Long orgaId){
		Session session = sessionFactory.getCurrentSession();
		Query q = session.createQuery("from OrganizationUser where user.id=? and organization.id=?");
		q.setLong(0, userId);
		q.setLong(1, orgaId);
		OrganizationUser orgaUser =(OrganizationUser) q.uniqueResult();
		return orgaUser;
	}
	
	/**
	 * 查询用户组织机构关系
	 */
	public List<OrganizationUser> queryOrgaUser(Long userId){
		Session session = sessionFactory.getCurrentSession();
		Query q = session.createQuery("from OrganizationUser where user.id=?");
		q.setLong(0, userId);
		@SuppressWarnings("unchecked")
		List<OrganizationUser> orgaUsers = q.list();
		return orgaUsers;
	}
	public Organization querySingleOrgaByUser(Long userId){
		Session session = sessionFactory.getCurrentSession();
		Query q = session.createQuery("from OrganizationUser where user.id=?");
		q.setLong(0, userId);
		q.setMaxResults(1);
		OrganizationUser organizationUser=  (OrganizationUser)q.uniqueResult();
		if(organizationUser!=null){
			return organizationUser.getOrganization();
		}
		return null;
	}
	
	/**
	 * 删除用户 从组织机构
	 */
	public void deleteUserFromOrga(OrganizationUser orgaUser){
		Session session = sessionFactory.getCurrentSession();
		session.delete(orgaUser);
	}
	
	
	/////////////////////////////////////////
	////以下是和【菜单】的关系查询
	
	/**
	* 保存和修改组织机构和菜单对应关系
	* @param orgaFun
	*/
	public void saveOrUpdate(OrganizationFunction orgaFun){
		Session session = sessionFactory.getCurrentSession();
		session.save(orgaFun);
	}
	
	/**
	* 删除组织机构和菜单对应关系
	* @param orgaId 组织机构id
	* @param funId 菜单Id
	*/
	public void deleteOrgaFunById(Long orgaId,Long funId){
		Session session = sessionFactory.getCurrentSession();
		String sql = "delete from p_organization_function";
		sql += " where 1=1  ";
		if(orgaId != null){
			sql += " and organization_id=:oid";
		}
		if(funId != null){
			sql += " and function_id=:fid";
		}
		
		Query query = session.createSQLQuery(sql);
		if(orgaId != null){
			query.setLong("oid", orgaId);
		}
		if(funId != null){
			query.setLong("fid", funId);
		}
		query.executeUpdate();
	}
	
	/**
	* 根据id查询组织机构和菜单对应关系
	* @param id
	* @return
	*/
	public OrganizationFunction getOrgaFunById(Long id){
		Session session = sessionFactory.getCurrentSession();
		OrganizationFunction orgaFun = (OrganizationFunction)session.get(OrganizationFunction.class, id);
		return orgaFun;
	}
	
	
	/**
	* 根据组织机构id查询其单层对应关系
	* @param orgaId
	* @return
	*/
	@SuppressWarnings("unchecked")
	public List<OrganizationFunction> getSingleFlourOrgaFunListByOrgaId(Long orgaId){
		
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery("from OrganizationFunction where organization.id=?").setLong(0, orgaId);
		List<OrganizationFunction> list = query.list();
		
		return list;
	}
	
	/**
	* 根据组织机构id查询其所有包括继承的对应关系
	* @param orgaId
	* @return
	*/
	@SuppressWarnings("unchecked")
	public List<OrganizationFunction> getAllOrgaFunListByOrgaId(Long orgaId){
		/*List<OrganizationFunction> list = getSingleFlourOrgaFunListByOrgaId(orgaId);
		for(OrganizationFunction orgaFun : list){
			Organization pOrga = orgaFun.getOrganization().getParent();
			if(pOrga != null){
				list.addAll(getAllOrgaFunListByOrgaId(pOrga.getId()));
			}
		}*/
		Session session = sessionFactory.getCurrentSession();
		List<Organization> orgas = new ArrayList<Organization>();
		Organization orga = getOrgaById(orgaId);
		while(orga.getParent() != null){
			orgas.add(orga.getParent());
			orga = orga.getParent();
		}
		List<OrganizationFunction> list = getSingleFlourOrgaFunListByOrgaId(orgaId);
		for(int i=0 ;i<orgas.size();i++){
			List<Long> fids = new ArrayList<Long>();
			for(OrganizationFunction orgfun : list){
				fids.add(orgfun.getFunction().getId());
			}
			fids.add(-1L);
			Query query = session.createQuery("from OrganizationFunction where organization.id=? and function.id not in(:fidlist)")
					.setLong(0, orgas.get(i).getId())
					.setParameterList("fidlist", fids);
			List<OrganizationFunction> olist = query.list();
			if(olist != null && !olist.isEmpty()){
				list.addAll(olist);
			}
		}
		return list;
	}
	
	/**
	* 根据User id查询其所在组织所有对应关系
	* @param orgaId
	* @return
	*/
	@SuppressWarnings("unchecked")
	public List<OrganizationFunction> getOrgaFunListByUserId(Long userId){
		List<OrganizationFunction> list = new ArrayList<OrganizationFunction>();
		Session session = sessionFactory.getCurrentSession();
		List<OrganizationUser> orgaUsers =  session.createQuery("from OrganizationUser where user.id=?").setLong(0, userId).list();
		for(OrganizationUser orgaUser : orgaUsers){
			list.addAll(getAllOrgaFunListByOrgaId(orgaUser.getOrganization().getId()));
		}
		return list;
	}
	
	
	/**
	 * 根据id查询组织机构和菜单对应关系
	 * @param id
	 * @return
	 */
	public OrganizationFunction getOrgaFun(Long orgaId,Long funId){
		Session session = sessionFactory.getCurrentSession();
		OrganizationFunction of = (OrganizationFunction)session.createQuery("from OrganizationFunction where organization.id=? and function.id=? and rownum=1")
						.setLong(0, orgaId)
						.setLong(1, funId)
						.uniqueResult();
		return of;
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public List<OrganizationFunction> getOrgaFunctionList(Long appId, Long orgaId) {
		
		Session session = sessionFactory.getCurrentSession();
		String sql = "select p.* from p_organization_function p inner join p_function f on p.function_id = f.id ";
		sql = sql + " where p.organization_id = ? and f.app_id = ?";
		SQLQuery query = session.createSQLQuery(sql);
		query.setParameter(0, orgaId);
		query.setParameter(1, appId);
		query.addEntity(OrganizationFunction.class);
		return query.list();
	}

	
	@SuppressWarnings("unchecked")
	@Override
	public List<Organization> getOrgaByUserId(Long uid) {
		Session session = sessionFactory.getCurrentSession();
		String sql = "select * from P_ORGANIZATION t inner join p_organization_user ou on ou.organization_id=t.id"
					+" where ou.user_id=? ";
		SQLQuery query = session.createSQLQuery(sql);
		query.addEntity(Organization.class);
		query.setParameter(0, uid);
		return query.list();
	}
	
	/**
	 * 查询组织机构的单位代码
	 * @param uid
	 * @return
	 */
	@Override
	public String getUnitCodeByOrgaId(Long orgaId){
		Session session = sessionFactory.getCurrentSession();
		String sql = "select unit_code from P_ORG_AREA where organization_id=? and rownum=1";
		SQLQuery query = session.createSQLQuery(sql);
		query.setLong(0, orgaId);
		return (String)query.uniqueResult();
	}


	/* (non-Javadoc)
	 * @see com.zfysoft.platform.dao.OrganizationDao#queryAllUserUnderOrga(long)
	 */
	@Override
	public List<User> queryAllUserUnderOrga(String orgCode) {
		String sql ="select u.* from p_user u "+
				"INNER join p_organization_user ou on u.id=ou.user_id "+
				"inner join p_organization o on o.id=ou.organization_id "+
				"where o.code like '"+orgCode+"%' ";
		Session session = sessionFactory.getCurrentSession();
		SQLQuery query = session.createSQLQuery(sql);
		query.addEntity(User.class);
		return query.list();
		
	}
	
	
	public List<User> queryAllUserUnderOrga(String orgCode,User user,Page page) {
		int begin = (page.getPageNum() - 1) * page.getPageSize();
		
		String sql ="select u.* from p_user u "+
				"INNER join p_organization_user ou on u.id=ou.user_id "+
				"inner join p_organization o on o.id=ou.organization_id "+
				"where o.code like '"+orgCode+"%' ";
		String keyWords = user.getKeyWords();
		if(StringUtil.isNotEmptyOrNull(keyWords)){
			sql+=" and (u.login_name like '%"+keyWords+"%'";
		
			sql+=" or u.real_name like '%"+keyWords+"%'";
		
			sql+=" or u.email like '%"+keyWords+"%')";
		}
		
		Session session = sessionFactory.getCurrentSession();
		SQLQuery query = session.createSQLQuery(sql);
		
		query.addEntity(User.class);
		query.setFirstResult(begin).setMaxResults(page.getPageSize());
		return query.list();
		
	}
	
	
	public int queryAllUserUnderOrgaCount(String orgCode,User user) {
	
		
		String sql ="select count(1) as total from p_user u "+
				"INNER join p_organization_user ou on u.id=ou.user_id "+
				"inner join p_organization o on o.id=ou.organization_id "+
				"where o.code like '"+orgCode+"%' ";
		String keyWords = user.getKeyWords();
		if(StringUtil.isNotEmptyOrNull(keyWords)){
			sql+=" and (u.login_name like '%"+keyWords+"%'";
		
			sql+=" or u.real_name like '%"+keyWords+"%'";
		
			sql+=" or u.email like '%"+keyWords+"%')";
		}
		
		
		Session session = sessionFactory.getCurrentSession();
		SQLQuery query = session.createSQLQuery(sql);
		query.addScalar("total", LongType.INSTANCE);
		Long total = (Long) query.uniqueResult();
		return total.intValue();
		
	}
	
	
//	/**
//	 * 查询用户所在组织机构,以及用一unitcode的父组织机构
//	 * @param uid
//	 * @return
//	 */
//	@SuppressWarnings("unchecked")
//	@Override
//	public List<Organization> getOrgaAndSameUnitCodeParentOrgaByUserId(Long uid){
//		Session session = sessionFactory.getCurrentSession();
//		String sql = "select t.* from P_ORGANIZATION t  inner join p_org_area a on a.organization_id=t.id where a.unit_code in (select unit_code from P_ORGANIZATION t inner join p_org_area a on a.organization_id=t.id inner join p_organization_user ou on ou.organization_id=t.id " 
//				+" where ou.user_id=:uid ) start with t.id in( select t.id from P_ORGANIZATION t  inner join p_org_area a on a.organization_id=t.id inner join p_organization_user ou on ou.organization_id=t.id where ou.user_id=:uid) connect by prior t.parent_id=t.id";
//		SQLQuery query = session.createSQLQuery(sql).addEntity(Organization.class);
//		query.setLong("uid", uid);
//		return query.list();
//	}

}
