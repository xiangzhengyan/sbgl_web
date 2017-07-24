package com.zfysoft.platform.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.zfysoft.common.util.MD5Util;
import com.zfysoft.common.util.Page;
import com.zfysoft.platform.dao.OrganizationDao;
import com.zfysoft.platform.dao.UserDao;
import com.zfysoft.platform.model.Role;
import com.zfysoft.platform.model.User;
import com.zfysoft.platform.service.UserService;

@Service("userService")
public class UserServiceImpl implements UserService{

	@Resource
	private UserDao userDao;
	
	@Resource
	private OrganizationDao organizationDao;

	public UserDao getUserDao() {
		return userDao;
	}
	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}

	@Override
	public List<User> getUserByUserAlias(String userAlias) {
		
		 return this.userDao.getUserByUserAlias(userAlias);
	}

	@Override
	public Page queryUserList(User user, Page page,String orgCode) {
//		int total = userDao.queryUserCount(user);
//		if(total > 0){
//			List<?> userList = userDao.queryUserList(user,page);
//			page.setTotalRows(total);
//			page.setPageList(userList);
//		}
//		return page;
		int total = organizationDao.queryAllUserUnderOrgaCount(orgCode, user);
		if(total>0){
			
			List<?> userList = organizationDao.queryAllUserUnderOrga(orgCode,user,page);
			page.setTotalRows(total);
			page.setPageList(userList);
		}
		return page;
		
	}
	
	@Override
	public Boolean addNewUser(User user) {
		Boolean success = true;
		List<User> list = getUserByUserAlias(user.getLoginName());
		if(list==null || list.isEmpty()){
			userDao.mgrUser(user);
		}else{
			success = false;
		}
		return success;
	}
	
	@Override
	public Boolean modifyUser(User user) {
		Boolean success = true;
		String oldName = userDao.queryLoginNameById(user.getId());
		if(oldName!=null && !oldName.equalsIgnoreCase(user.getLoginName())){//如果用户名改变了
			List<User> list = getUserByUserAlias(user.getLoginName());
			if(list==null || list.isEmpty()){//如果用户名不存在
				userDao.mgrUser(user);
			}else{//如果用户名已经存在
				success = false;
			}
		}else{//如果用户名没改变
			userDao.mgrUser(user);
		}
		return success;
	}
	
	@Override
	public User getById(Long id) {
		return userDao.getById(id);
	}
	
	@Override
	public void modifyPwd(User user) {
		User user_temp = userDao.getById(user.getId());
		user_temp.setPassword(MD5Util.getMD5(user.getPassword()));
		userDao.mgrUser(user_temp);
	}
	
	@Override
	public void deleteUser(Long id) {
		User user = this.getById(id);
		user.setStatus(0);
		userDao.mgrUser(user);
	}
	
	/*@Override
	public void grantRoleToUser(Long userId, Long[] roleIds) {
		this.userDao.grantRoleToUser(userId,roleIds);
	}*/
	
	@Override
	public void revokeRoleOnUser(Long userId) {
		this.userDao.revokeRoleOnUser(userId);
	}
	
	@Override
	public List<Role> queryUserRoles(Long id) {
		return this.userDao.queryUserRoles(id);
	}
	@Override
	public void updateUser(User user) {
		 this.userDao.updateUser(user);
		 
	}
	 

}
