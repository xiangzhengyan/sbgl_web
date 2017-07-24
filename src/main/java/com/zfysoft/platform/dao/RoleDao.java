/**
 * 版权所有 2013 成都子非鱼软件有限公司 保留所有权利
 * 1 项目签约客户只拥有对项目业务代码的所有权，以及在本项目范围内使用平台框架
 * 2 平台框架及相关代码属子非鱼软件有限公司所有，未经授权不得扩散、二次开发及用于其它项目
 */
package com.zfysoft.platform.dao;

import java.util.List;

import com.zfysoft.platform.model.Role;
import com.zfysoft.platform.model.RoleFunction;
import com.zfysoft.platform.model.User;

/**
 * @author chenhm
 * @date 2013-7-3
 */
public interface RoleDao {

	/**
	 * 查询角色列表
	 * @return
	 */
	public List<Role> queryRoleList(String name);

	/**
	 * 根据id查询角色列表
	 * @param roleId
	 * @return
	 */
	public Role queryRoleId(Long roleId);

	/**
	 * 通过id删除角色
	 * @param roleId
	 * @return
	 */
	public String deleteRoleById(Long roleId);

	/**
	 * 新增角色
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
	 * 保存角色权限
	 * @param roleFunction
	 */
	public void saveRoleFunction(RoleFunction roleFunction);

	/**
	 * 删除角色权限
	 * @param roleFunction
	 */
	public void deleteRoleFunction(RoleFunction roleFunction);

	/**
	 * 通过角色id和菜单id查询关联
	 * @param roleId
	 * @param functionId
	 * @return
	 */
	public RoleFunction getRoleFunctionByRoleAndFunctionId(Long roleId,
			Long functionId);

	/**
	 * 根据子系统id和角色id查询权限列表
	 * @param appId
	 * @param roleId
	 * @return
	 */
	public List<RoleFunction> getRoleFunctionList(Long appId, Long roleId);
	
	/**
	 * 删除用户的角色
	 */
	public void deleteUserRoles(Long userId);
	
	/**
	 * 为用户分配角色
	 */
	public void updateUserRoles(Long userId,Long[] roleIds);

	/**
	 * 通过角色名查找角色
	 * @param name
	 * @return
	 */
	public Role getRoleByName(String name, Long id);

	/**
	 * 通过角色代码查询角色
	 * @param code
	 * @param object
	 * @return
	 */
	public Role queryRoleByCode(String code, Object object);
	
	/**
	 * 根据当前登录人，查询对应角色的主页
	 */
	public String queryHomePage(User user);

}
