/**
 * 版权所有 2013 成都子非鱼软件有限公司 保留所有权利
 * 1 项目签约客户只拥有对项目业务代码的所有权，以及在本项目范围内使用平台框架
 * 2 平台框架及相关代码属子非鱼软件有限公司所有，未经授权不得扩散、二次开发及用于其它项目
 */
package com.zfysoft.platform.service;

import java.util.List;

import com.zfysoft.platform.model.FunctionAction;
import com.zfysoft.platform.model.User;

/**
 * 菜单页面按钮链接权限
 * @author hudt
 * @date 2013-7-30
 */
public interface ActionService {
	/**
	 * 保存按钮权限（对角色）
	 */
	public void saveOrUpdateInRole(Long funId,Long roleId,Long buttonId);
	
	/**
	 * 保存按钮权限（对组织机构）
	 */
	public void saveOrUpdateInOrga(Long funId,Long orgaId,Long buttonId);
	
	/**
	 * 删除按钮权限（对角色）
	 */
	public void deleteInRole(Long roleId,Long funId);
	
	/**
	 * 删除按钮权限（对组织机构）
	 */
	public void deleteInOrga(Long orgaId,Long funId);
	
	/**
	 * 通过ID得到Button
	 * @param id
	 * @return
	 */
	public FunctionAction queryById(Long id);
	
	/**
	 * 根据functionId查询button
	 * @throws Exception 
	 */
	public List<FunctionAction> queryByFunId(Long funId);
	
	/**
	 * 根据角色roleId查询所有BUTTON
	 */
	public List<FunctionAction> queryByRoleId(Long roleId);
	
	/**
	 * 根据角色roleId和FunctionId查询所有BUTTON
	 */
	public List<FunctionAction> queryByRoleIdAndFunId(Long roleId,Long functionId);
	
	/**
	 * 根据组织机构orgaId查询所有BUTTON
	 */
	public List<FunctionAction> queryByOrgaId(Long orgaId);
	
	/**
	 * 根据组织机构orgaId和FunctionId查询所有BUTTON
	 */
	public List<FunctionAction> queryByOrgaIdAndFunId(Long orgaId,Long functionId);
	
	/**
	 * 根据当前登录人，以及需要访问的菜单，找到已经被授权的Buttons
	 */
	public  List<String> queryPermissionButtonCodes(User loginUser,String funUrl);
	
	public  List<FunctionAction> queryPermissionButtons(User loginUser,String funUrl);
}
