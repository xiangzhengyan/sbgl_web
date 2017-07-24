/**
 * 版权所有 2013 成都子非鱼软件有限公司 保留所有权利
 * 1 项目签约客户只拥有对项目业务代码的所有权，以及在本项目范围内使用平台框架
 * 2 平台框架及相关代码属子非鱼软件有限公司所有，未经授权不得扩散、二次开发及用于其它项目
 */
package com.zfysoft.platform.service;

import java.util.List;

import com.zfysoft.platform.model.Role;
import com.zfysoft.platform.model.User;

/**
 * @author chenhm
 * @date 2013-7-3
 */
public interface RoleService {

	/**
	 * 查询角色列表
	 * @param name
	 * @return
	 */
	public List<Role> queryRoleList(String name);

	/**
	 * 根据id查询角色
	 * @param roleId
	 * @return
	 */
	public Role queryRoleById(Long roleId);

	/**
	 * 通过id删除角色
	 * @param roleId
	 */
	public String deleteRoleById(Long roleId);

	/**
	 * 添加角色
	 * @param role
	 * @return
	 */
	public String addRole(Role role);

	/**
	 * 更新角色
	 * @param role
	 * @return
	 */
	public String updateRole(Role role);

	/**
	 * 根据登陆用户查询该用户拥有的角色
	 * @param loginUser
	 * @return
	 */
	public List<Long> getRoleIdByUser(User loginUser);

	/**
	 * 根据子系统id查询菜单树
	 * @param appId
	 * @param roleId
	 * @return
	 */
	public String buildFunctionTree(Long appId, Long roleId);

	/**
	 * 保存角色权限<br>
	 * 角色关联的菜单
	 * @param appId
	 * @param roleId
	 * @param selectedIds
	 * @param unSelectedIds
	 */
	public void authorize(Long appId, Long roleId, String selectedIds, String unSelectedIds);
	
	/**
	 * 保存用户的角色<br>
	 * 为用户分配角色
	 * @param userId
	 * @param roleIds
	 */
	public void grantRoleToUser(Long userId, Long[] roleIds);

	/**
	 * 查看角色名是否存在
	 * @param name
	 * @param id
	 * @return
	 */
	public boolean queryRoleByName(String name, Long id);

	/**
	 * 查看角色代码是否存在
	 * @param code
	 * @param object
	 * @return
	 */
	public boolean queryRoleByCode(String code, Object object);
	
	/**
	 * 根据当前登录人，查询对应角色的主页
	 */
	public String queryHomePage(User user);

	/**
	 * @param roleCode
	 * @return
	 */
	public Role getRoleByCode(String roleCode);

}
