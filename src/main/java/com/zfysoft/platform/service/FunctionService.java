/**
 * 版权所有 2013 成都子非鱼软件有限公司 保留所有权利
 * 1 项目签约客户只拥有对项目业务代码的所有权，以及在本项目范围内使用平台框架
 * 2 平台框架及相关代码属子非鱼软件有限公司所有，未经授权不得扩散、二次开发及用于其它项目
 */
package com.zfysoft.platform.service;

import java.util.List;

import com.zfysoft.platform.model.Function;
import com.zfysoft.platform.model.FunctionAction;
import com.zfysoft.platform.model.FunctionVo;
import com.zfysoft.platform.model.User;

/**
 * @author chenhm
 * @date 2013-6-28
 */
public interface FunctionService {

	/**
	 * 根据当前登录用户信息和子系统ID查询菜单
	 * @param loginUser
	 * @param appId 子系统id
	 * @return
	 */
	public List<Function> getAuthFunctionsByAppId(User loginUser, Long appId);

	/**
	 * 根据登陆用户信息查询子系统
	 * @param loginUser
	 * @return
	 */
	public List<Function> getAuthAppsByUser(User loginUser);

	/**
	 * 根据id查询菜单信息
	 * @param functionId
	 * @param enable 是否有效；  1有效，0无效，null查询全部
	 * @return
	 */
	public Function getFunctionById(Long functionId, Long enable);

	/**
	 * 根据角色id查询菜单
	 * @param roleIdList
	 * @return
	 */
	public List<Long> getFunctionIdByRole(List<Long> roleIdList);

	/**
	 * 根据id查询子系统信息
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
	 * 用户收藏菜单查询
	 * @param appId
	 * @param functionIdList
	 * @return
	 */
	public List<FunctionVo> getFavFunctionByAppIdAndIdList(Long appId,
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
	public String getFavParentName(Long functionId);

	/**
	 * 查看指定菜单是否已经被收藏
	 * @param functionId
	 * @param userId
	 * @return
	 */
	public boolean isCollect(Long userId, Long functionId);

	/**
	 * 添加收藏菜单
	 * @param userId
	 * @param functionId
	 */
	public void addFavFunction(Long userId, Long functionId);

	/**
	 * 取消收藏
	 * @param id
	 * @param functionId
	 */
	public void deleteFavFunction(Long id, Long functionId);

	/**
	 * 根据菜单id查询子系统id
	 * @param functionId
	 * @return
	 */
	public Long getAppIdByFunctionId(Long functionId);

	/**
	 * 新建菜单
	 * @param func
	 * @return
	 */
	public Function addFunction(FunctionVo func);

	/**
	 * 修改菜单信息
	 * @param func
	 * @return
	 */
	public Function updateFunction(FunctionVo func);

	/**
	 * 删除菜单
	 * @param functionId
	 */
	public void deleteFunction(Long functionId);

	/**
	 * 根据菜单id查询子菜单
	 * @param functionId
	 * @return
	 */
	public List<Long> getSubFuncId( Long functionId);

	/**
	 * 根据菜单id删除角色菜单关联数据
	 * @param id
	 */
	public void deleteRoleFunc(Long id);

	/**
	 * 根据菜单id删除组织机构菜单关联数据
	 * @param id
	 */
	public void deleteOrgFunc(Long id);

	/**
	 * 根据父节点id查询菜单树
	 * @param parentId
	 * @param roleId
	 * @return
	 */
	public String buildFunctionTree(Long parentId, Long roleId, List<Long> list);

	/**
	 * 查询指定子系统下的第一级菜单
	 * @param functionIdList
	 * @param appId
	 * @return
	 */
	public List<Function> getFistLevelsByidList(List<Long> functionIdList, Long appId);

	/**
	 * 根据菜单id查询action
	 * @param functionId
	 * @return
	 */
	public List<FunctionAction> getFunctionActionByFucntionId(Long functionId);

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
	public void deleteActions(Long id); 
	
	/**
	 * 根据当前登录用户查询应该展示的菜单
	 */
	public List<Function> getFunctionsByUserId(Long userid);
}
