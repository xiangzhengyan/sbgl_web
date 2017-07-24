package com.zfysoft.platform.dao;

import java.util.List;

import com.zfysoft.common.util.Page;
import com.zfysoft.platform.model.Role;
import com.zfysoft.platform.model.User;

public interface UserDao {

	/**
	 * 根据用户名查询user
	 * @param userAlias
	 * @return list
	 */
	public List<User> getUserByUserAlias(String userAlias);
	
	/**查询用户记录数
	 * @param user
	 * @return
	 */
	public int queryUserCount(User user);

	/**查询用户列表，分页
	 * @param user
	 * @param page
	 * @return
	 */
	public List<?> queryUserList(User user, Page page);


	/**
	 * 操作用户
	 * @param user
	 */
	public void mgrUser(User user);

	/**
	 * 根据用户ID查找
	 * @param id
	 * @return
	 */
	public User getById(Long id);

	/**
	 * 给用户赋予角色
	 * @param userId
	 * @param roleIds
	 */
	//public void grantRoleToUser(Long userId, Long[] roleIds);

	/**
	 * 查询用户所拥有的角色
	 * @param id
	 */
	public List<Role> queryUserRoles(Long id);

	/**
	 * 分配角色
	 * @param userId
	 */
	public void revokeRoleOnUser(Long userId);
	
	/**
	 * 通过用户id查询用户登录名
	 * @param id
	 * @return
	 */
	public String queryLoginNameById(Long id);
	/**
	 * 修改用户登录密码
	 * @param user
	 */
	 public void updateUser(User user);
}
