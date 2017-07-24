package com.zfysoft.platform.service;

import java.util.List;

import com.zfysoft.common.util.Page;
import com.zfysoft.platform.model.Role;
import com.zfysoft.platform.model.User;

public interface UserService {

	/**
	 * 根据用户名查询user
	 * @param userAlias
	 * @return User
	 */
	public List<User> getUserByUserAlias(String userAlias);
	
	/**
	 * @param user
	 * @param page
	 * @param orgCode 
	 * @return
	 */
	public Page queryUserList(User user, Page page, String orgCode);

	/**
	 * @param user
	 * @return 是否存在
	 */
	public Boolean addNewUser(User user);

	/**
	 * 修改用户基本信息
	 * @param user
	 * @return 是否已经存在
	 */
	public Boolean modifyUser(User user);

	/**
	 * 根据用户ID查找
	 * @param id
	 * @return
	 */
	public User getById(Long id);

	/**修改密码
	 * @param user
	 */
	public void modifyPwd(User user);

	/**
	 * 删除用户信息（更新status状态为0）
	 * @param id
	 */
	public void deleteUser(Long id);

	/** 赋予角色
	 * @param userId
	 * @param roleId
	 */
	//public void grantRoleToUser(Long userId, Long[] roleId);
	
	/**删除用户角色
	 * @param userId
	 */
	public void revokeRoleOnUser(Long userId);

	/**
	 * @param id
	 * @return
	 */
	public List<Role> queryUserRoles(Long id);
	/**
	 * 修改用户登录密码
	 * @param user
	 */
	public void updateUser(User user);
}
