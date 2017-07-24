/**
 * 版权所有 2013 成都子非鱼软件有限公司 保留所有权利
 * 1 项目签约客户只拥有对项目业务代码的所有权，以及在本项目范围内使用平台框架
 * 2 平台框架及相关代码属子非鱼软件有限公司所有，未经授权不得扩散、二次开发及用于其它项目
 */
package com.zfysoft.platform.dao;

import java.util.List;

import com.zfysoft.common.util.Page;
import com.zfysoft.platform.model.Organization;
import com.zfysoft.platform.model.OrganizationFunction;
import com.zfysoft.platform.model.OrganizationUser;
import com.zfysoft.platform.model.User;

/**
 * 组织机构Dao
 * @author hudt
 * @date 2013-7-18
 */
public interface OrganizationDao {
	
	/**
	 * 通过ID得到组织机构
	 * @param 组织机构主键
	 * @return
	 */
	public Organization getOrgaById(Long id);
	
	
	/**
	 * 增加组织机构
	 * @param 
	 * @return
	 */
	public void saveOrga(Organization orga);
	
	/**
	 * 修改组织机构
	 * @param 
	 * @return
	 */
	public void updateOrga(Organization orga);
	
	/**
	 * 删除组织机构
	 * @param 
	 * @return
	 */
	public void deleteOrgaById(Long id);
	
	/**
	 * 根据ID查询其第一层子组织机构
	 */
	public List<Organization> querySubOrgaById(Long id);
	
	/**
	 * 查询所有组织机构
	 */
	public List<Organization> queryAllOrga();
	
	
	/**
	 * 根据类型code
	 * 查询所有属于该code的组织机构
	 */
	public List<Organization> queryAllOrgaByTypeCode(String type);
	
	/**
	 * 根据ID查询其所有子孙组织机构
	 */
	public List<Organization> queryAllSubOrgaById(Long id);
	
	/**
	 * 查询根Orga
	 */
	public List<Organization> queryRootOrga();
	
	/**
	 * 根据父code查询所有子code
	 */
	public List<String> queryAllSubTypeByCode(String code);
	
	/**
	 * 根据子code查询所有父code
	 */
	public List<String> queryAllParentTypeBySubcode(String subCode);
	
	/**
	 * 根据父code查询所有真实存在的子code
	 */
	public List<String> queryAllRealExistSubTypeByCode(String code);
	
	
	/////////////////////////////////////////
	////以下是和【用户】的关系查询
	
	/**
	 * 根据组织机构ID查询此组织机构下有哪些人
	 */
	public List<User> queryUserListByOrgaId(Long orgaId,Page page);
	
	/**
	 * 根据组织机构ID查询此组织机构下没有哪些人
	 */
	public List<User> queryUserListNotInByOrgaId(Long orgaId,Page page,String usernameFilter);
	
	/**
	 * 把用户分配到组织机构
	 */
	public void saveUserOrga(OrganizationUser orgaUser);
	
	/**
	 * 查询用户组织机构关系
	 */
	public List<OrganizationUser> queryOrgaUser(Long userId);
	
	/**
	 * 查询用户组织机构关系
	 */
	public OrganizationUser queryOrgaUser(Long userId,Long orgaId);
	
	/**
	 * 删除用户 从组织机构
	 */
	public void deleteUserFromOrga(OrganizationUser orgaUser);
	
	
	/////////////////////////////////////////
	////以下是和【菜单】的关系查询
	
	/**
	 * 保存和修改组织机构和菜单对应关系
	 * @param orgaFun
	 */
	public void saveOrUpdate(OrganizationFunction orgaFun);
	
	/**
	 * 删除组织机构和菜单对应关系
	 * @param orgaId 组织机构id
	 * @param funId 菜单Id
	 */
	public void deleteOrgaFunById(Long orgaId,Long funId);
	
	/**
	 * 根据id查询组织机构和菜单对应关系
	 * @param id
	 * @return
	 */
	public OrganizationFunction getOrgaFunById(Long id);
	
	/**
	 * 根据组织机构id查询其单层对应关系
	 * @param orgaId
	 * @return
	 */
	public List<OrganizationFunction> getSingleFlourOrgaFunListByOrgaId(Long orgaId);
	
	/**
	 * 根据组织机构id查询其所有包括继承的对应关系
	 * @param orgaId
	 * @return
	 */
	public List<OrganizationFunction> getAllOrgaFunListByOrgaId(Long orgaId);
	
	/**
	 * 根据User id查询其所在组织所有对应关系
	 * @param orgaId
	 * @return
	 */
	public List<OrganizationFunction> getOrgaFunListByUserId(Long userId);
	
	/**
	 * 根据id查询组织机构和菜单对应关系
	 * @param id
	 * @return
	 */
	public OrganizationFunction getOrgaFun(Long orgaId,Long funId);
	
	/**
	 * 根据子系统id和orga id查询权限列表
	 * @param appId
	 * @param orgaId
	 * @return
	 */
	public List<OrganizationFunction> getOrgaFunctionList(Long appId, Long orgaId);
	
	///////////////////////////////////////////
	//User 相关
	
	/**
	 * 查询用户所在组织机构
	 * @param uid
	 * @return
	 */
	public List<Organization> getOrgaByUserId(Long uid);
	
//	/**
//	 * 查询用户所在组织机构,以及用一unitcode的父组织机构
//	 * @param uid
//	 * @return
//	 */
//	public List<Organization> getOrgaAndSameUnitCodeParentOrgaByUserId(Long uid);
	
	///////////////////////////////////////////
	//行政区划 相关
	
	/**
	 * 查询组织机构的单位代码
	 * @param uid
	 * @return
	 */
	public String getUnitCodeByOrgaId(Long orgaId);
	
	/**
	 * 查询用户的第一个组织机构
	 * @param userId
	 * @return
	 */
	public Organization querySingleOrgaByUser(Long userId);
	
	/**
	 * 查询自己和所有下属组织机构
	 * @param id
	 * @return
	 */
	public List<Organization> queryAllOrga(long id);


	/**
	 * 查询组织机构及下属组织机构所有用户
	 * 
	 */
	public List<User> queryAllUserUnderOrga(String orgCode);
	
	
	/**
	 * 查询组织机构及下属组织机构所有用户树，列表页面分页用
	 * 
	 */
	public int queryAllUserUnderOrgaCount(String orgCode,User user);
	
	/**
	 * 
	 * 查询组织机构及下属组织机构所有用户，列表页面分页用
	 * user:过滤条件
	 */
	public List<User> queryAllUserUnderOrga(String orgCode,User user,Page page) ;
	
	
	
	
}