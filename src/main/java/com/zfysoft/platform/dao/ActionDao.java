/**
 * 版权所有 2013 成都子非鱼软件有限公司 保留所有权利
 * 1 项目签约客户只拥有对项目业务代码的所有权，以及在本项目范围内使用平台框架
 * 2 平台框架及相关代码属子非鱼软件有限公司所有，未经授权不得扩散、二次开发及用于其它项目
 */
package com.zfysoft.platform.dao;

import java.util.List;

import com.zfysoft.platform.model.FunctionAction;
import com.zfysoft.platform.model.OrganizationAction;
import com.zfysoft.platform.model.RoleAction;
import com.zfysoft.platform.model.RoleFunction;
import com.zfysoft.platform.model.User;

/**
 * @author hudt
 * @date 2013-7-30
 */
public interface ActionDao {
	
	/**
	 * 保存按钮权限（对角色）
	 */
	public void save(RoleAction buttonRoleFun);
	
	/**
	 * 删除某角色某菜单中所有按钮权限（对角色）
	 */
	public void deleteInRole(Long roleId,Long funId);
	
	/**
	 * 删除按钮权限（对角色）
	 */
	public void delete(RoleAction buttonRoleFun);
	
	/**
	 * 保存按钮权限（对组织机构）
	 */
	public void save(OrganizationAction buttonOrgaFun);
	
	/**
	 * 删除按钮权限（对组织机构）
	 */
	public void delete(OrganizationAction buttonOrgaFun);
	
	/**
	 * 删除某组织结构某菜单中所有按钮权限（对组织机构）
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
	
	public  List<String> queryPermissionButtonCodes(User loginUser,String funUrl);
	
	public  List<FunctionAction> queryPermissionButtons(User loginUser,String funUrl);

	/**
	 * 根据当前角色菜单关系查询按钮关系
	 * @param roleFunction
	 * @return
	 */
	public List<RoleAction> queryByRoleFunction(RoleFunction roleFunction);
}
