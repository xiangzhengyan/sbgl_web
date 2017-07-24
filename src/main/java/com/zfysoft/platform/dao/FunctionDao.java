package com.zfysoft.platform.dao;

import java.util.List;

import com.zfysoft.platform.model.Favorite;
import com.zfysoft.platform.model.Function;
import com.zfysoft.platform.model.FunctionAction;
import com.zfysoft.platform.model.RoleFunction;
import com.zfysoft.platform.model.OrganizationFunction;
import com.zfysoft.platform.model.User;

public interface FunctionDao {

	/**
	 * 根据当前登录人信息和子系统id查询菜单
	 * @param loginUser
	 * @param appId 子系统id
	 * @return
	 */
	public List<Function> getAuthFunctionsByAppId(User loginUser, Long appId);

	/**
	 * 根据登陆人信息查询子系统
	 * @param loginUser
	 * @return
	 */
	public List<Function> getAuthAppsByUser(User loginUser);

	/**
	 * 根据id查询菜单信息
	 * @param id
	 * @param enable 是否有效；1有效，0无效，null全部
	 * @return
	 */
	public Function getFunctionById(Long id, Long enable);

	/**
	 * 根据角色id查询菜单
	 * @param roleIdList
	 * @return
	 */
	public List<Long> getFunctionIdByRole(List<Long> roleIdList);

	/**
	 * 根据Id查询子系统信息
	 * @param functionIdList
	 * @return
	 */
	public List<Function> getAuthAppsByIdList(List<Long> functionIdList);

	/**
	 * 根据子系统id和权限查询拥有的菜单信息
	 * @param appId
	 * @param functionIdList
	 * @return
	 */
	public List<Function> getAuthFunctionsByAppIdAndIdList(Long appId,
			List<Long> functionIdList);

	/**
	 * 通过子系统按层级查询菜单
	 * @param object
	 * @param appId
	 * @param long1
	 * @return
	 */
	public List<Function> getLevelFunctionsByAppId(Object object, Long appId,
			Long menuLevel);

	/**
	 * 查询角色id在指定子系统下拥有的菜单
	 * @param appId
	 * @param roleId
	 * @return
	 */
	public List<Long> getAuthCodes(Long appId, Long roleId);

	/**
	 * 根据父菜单id查询子菜单 
	 * @param functionId 父菜单id
	 * @param status 状态，若为null则查询全部子菜单
	 * @return
	 */
	public List<Function> getSubFunctions(Long functionId, Long status, List<Long> list);

	/**
	 * 用户收藏菜单查询
	 * @param appId
	 * @param functionIdList
	 * @return
	 */
	public List<Function> getFavFunctionByAppIdAndIdList(Long appId,
			List<Long> functionIdList, Long userId);

	/**
	 * 通过菜单id查询子系统
	 * @param functionId
	 * @return
	 */
	public String getAppNameByFunctionId(Long functionId);

	/**
	 * 通过菜单id查询所有父级菜单
	 * @param functionId
	 * @return
	 */
	public List<Function> getFavParentName(Long functionId);

	/**
	 * 查看指定菜单是否已经被收藏
	 * @param userId
	 * @param functionId
	 * @return
	 */
	public int getCountByFunctionIdAndUserId(Long userId, Long functionId);

	/**
	 * 添加收藏菜单
	 * @param fav
	 */
	public void addFavFunction(Favorite fav);

	/**
	 * 取消收藏菜单
	 * @param userId
	 * @param functionId
	 */
	public void deleteFavFunction(Long userId, Long functionId);

	/**
	 * 根据菜单id查询子系统id
	 * @param functionId
	 * @return
	 */
	public Long getAppIdByFunctionId(Long functionId);

	/**
	 * 通过子系统id获取子系统下所有菜单
	 * @param appId
	 * @return
	 */
	public List<Function> getAllFunctionsByAppId(Long appId);

	/**
	 * 查询当前节点是否存在子节点
	 * @param functionId
	 * @param isApp
	 * @return
	 */
	public List<Function> getChildren(Long functionId, boolean isApp);

	/**
	 * 新建菜单
	 * @param func
	 * @return
	 */
	public Function addFunction(Function func);

	/**
	 * 修改菜单
	 * @param function
	 * @return
	 */
	public Function updateFunction(Function function);

	/**
	 * 删除菜单
	 * @param functionId
	 */
	public void deleteFunction(Function function);
	
	/**
	 * 得到角色菜单关系
	 * @param functionId
	 */
	public RoleFunction getRoleFunction(Long roleId,Long funId);

	/**
	 * 删除角色菜单关联数据
	 * @param rf
	 */
	public void deleteRoleFunc(RoleFunction rf);

	/**
	 * 删除组织机构菜单关联数据
	 * @param of
	 */
	public void deleteOrgFunc(OrganizationFunction of);

	/**
	 * 查询指定子系统下的第一级菜单
	 * @param functionIdList
	 * @param appId
	 * @return
	 */
	public List<Function> getFistLevelsByidList(List<Long> functionIdList, Long appId);
	
	/**
	 * 通过父节点Id查询指定层级菜单
	 * @param object
	 * @param parentId
	 * @param menuLevel
	 * @return
	 */
	public List<Function> getLevelFunctionsByParentId(Object object, Long parentId, Long menuLevel, List<Long> list);

	/**
	 * 根据菜单id查询action
	 * @param functionId
	 * @return
	 */
	public List<FunctionAction> getActionByFunctionId(Long functionId);

	/**
	 * 通过id查询action
	 * @param actionId
	 * @return
	 */
	public FunctionAction getActionById(Long actionId);

	/**
	 * 保存action
	 * @param action
	 * @return
	 */
	public FunctionAction saveOrUpdate(FunctionAction action);

	/**
	 * 删除指定action
	 * @param id
	 */
	public void deleteAction(Long id);

	
	/**
	 * 根据当前登录用户查询应该展示的菜单
	 */
	public List<Function> getFunctionsByUserId(Long userid);
}
