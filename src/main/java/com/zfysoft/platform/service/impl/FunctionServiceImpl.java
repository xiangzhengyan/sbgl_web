/**
 * 版权所有 2013 成都子非鱼软件有限公司 保留所有权利
 * 1 项目签约客户只拥有对项目业务代码的所有权，以及在本项目范围内使用平台框架
 * 2 平台框架及相关代码属子非鱼软件有限公司所有，未经授权不得扩散、二次开发及用于其它项目
 */
package com.zfysoft.platform.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.zfysoft.platform.dao.FunctionDao;
import com.zfysoft.platform.dao.UserDao;
import com.zfysoft.platform.model.Favorite;
import com.zfysoft.platform.model.Function;
import com.zfysoft.platform.model.FunctionAction;
import com.zfysoft.platform.model.FunctionVo;
import com.zfysoft.platform.model.OrganizationFunction;
import com.zfysoft.platform.model.RoleFunction;
import com.zfysoft.platform.model.User;
import com.zfysoft.platform.service.FunctionService;

/**
 * @author chenhm
 * @date 2013-6-28
 */
@Service
public class FunctionServiceImpl implements FunctionService {

	@Resource
	private FunctionDao functionDao;
	
	@Resource
	private UserDao userDao;

	public FunctionDao getFunctionDao() {
		return functionDao;
	}

	public void setFunctionDao(FunctionDao functionDao) {
		this.functionDao = functionDao;
	}

	private static List<Function> alls;
	
	public static List<Function> getAlls() {
		return alls;
	}

	public static void setAlls(List<Function> alls) {
		FunctionServiceImpl.alls = alls;
	}

	@Override
	public List<Function> getAuthFunctionsByAppId(User loginUser, Long appId) {
		
		return functionDao.getAuthFunctionsByAppId(loginUser, appId);
	}

	@Override
	public List<Function> getAuthAppsByUser(User loginUser) {
		
		return functionDao.getAuthAppsByUser(loginUser);
	}

	@Override
	public Function getFunctionById(Long appId, Long enable) {
		
		return functionDao.getFunctionById(appId, enable);
	}

	@Override
	public List<Long> getFunctionIdByRole(List<Long> roleIdList) {
		
		return functionDao.getFunctionIdByRole(roleIdList);
	}

	@Override
	public List<Function> getAuthAppsByIdList(List<Long> functionIdList) {
		
		return functionDao.getAuthAppsByIdList(functionIdList);
	}

	@Override
	public List<Function> getAuthFunctionsByAppIdAndIdList(Long appId,
			List<Long> functionIdList) {
		
		return functionDao.getAuthFunctionsByAppIdAndIdList(appId, functionIdList);
	}

	@Override
	public List<FunctionVo> getFavFunctionByAppIdAndIdList(Long appId,
			List<Long> functionIdList, Long userId) {
		
		List<Function> favs = functionDao.getFavFunctionByAppIdAndIdList(appId, functionIdList, userId);
		List<FunctionVo> favList = new ArrayList<FunctionVo>();
		for (Function function : favs) {
			FunctionVo functionVo = new FunctionVo();
			functionVo.setId(function.getId());
			functionVo.setParentId(function.getParent()==null?new Long(0):function.getParent().getId());
			functionVo.setLabel(function.getLabel());
			functionVo.setUrl(function.getUrl());
			functionVo.setMenuIndex(function.getMenuIndex()==null?new Long(1):function.getMenuIndex());
			favList.add(functionVo);
		}
		return favList;
	}

	@Override
	public String getAppNameByFunctionId(Long functionId) {
		
		return functionDao.getAppNameByFunctionId(functionId);
	}

	@Override
	public String getFavParentName(Long functionId) {
		
		String name = "";
		List<Function> list = functionDao.getFavParentName(functionId);
		if (list.size() > 0 && list != null) {
			
			Function func = list.get(0);
			while (func.getParent() != null) {
				list = functionDao.getFavParentName(func.getParent().getId());
				func = list.get(0);
				name = "$a$" + func.getLabel() + "$/a$$s$" + name;
			}
		}
		
		return name;
	}

	@Override
	public boolean isCollect(Long userId, Long functionId) {
		
		int count = functionDao.getCountByFunctionIdAndUserId(userId, functionId);
		if (count == 0)
			return false;
		else
			return true;
	}

	@Override
	public void addFavFunction(Long userId, Long functionId) {
		Favorite fav = new Favorite();
		fav.setUser(userDao.getById(userId));
		fav.setFunction(functionDao.getFunctionById(functionId, null));
		functionDao.addFavFunction(fav);
	}

	@Override
	public void deleteFavFunction(Long userId, Long functionId) {

		functionDao.deleteFavFunction(userId, functionId);
	}

	@Override
	public Long getAppIdByFunctionId(Long functionId) {
		
		return functionDao.getAppIdByFunctionId(functionId);
	}

	@Override
	public Function addFunction(FunctionVo func) {
		Function function = new Function();
		function.setLabel(func.getLabel());
		function.setApp(functionDao.getFunctionById(func.getAppId(), null));
		function.setParent(functionDao.getFunctionById(func.getParentId(), null));
		function.setEnable(func.getEnable());
		function.setIcoName(func.getIcoName());
		function.setMenuIndex(func.getMenuIndex());
		Long level = (Long) (functionDao.getFunctionById(func.getParentId(), null)==null?0:functionDao.getFunctionById(func.getParentId(), null).getMenuLevel());
		function.setMenuLevel(level + 1);
		function.setUrl(func.getUrl());
		functionDao.addFunction(function);
		alls = functionDao.getAllFunctionsByAppId(100L);
		return function;
	}

	@Override
	public Function updateFunction(FunctionVo func) {
		
		Function function = new Function();
		function.setLabel(func.getLabel());
		function.setApp(functionDao.getFunctionById(func.getAppId(), null));
		function.setParent(functionDao.getFunctionById(func.getParentId(), null));
		function.setEnable(func.getEnable());
		function.setIcoName(func.getIcoName());
		function.setMenuIndex(func.getMenuIndex());
		Long level = (Long) (functionDao.getFunctionById(func.getParentId(), null)==null?0:functionDao.getFunctionById(func.getParentId(), null).getMenuLevel());
		function.setMenuLevel(level + 1);
		function.setUrl(func.getUrl());
		function.setId(func.getId());
		functionDao.updateFunction(function);
		alls = functionDao.getAllFunctionsByAppId(100L);
		return function;
	}

	@Override
	public void deleteFunction(Long functionId) {

		Function function = functionDao.getFunctionById(functionId, null);
		functionDao.deleteFunction(function);
		alls = functionDao.getAllFunctionsByAppId(100L);
	}

	@Override
	public List<Long> getSubFuncId(Long functionId) {
		
		List<Function> list = functionDao.getSubFunctions(functionId, null, null);
		List<Long> idList = new ArrayList<Long>();
		for (Function func : list) {
			idList.add(func.getId());
			list = functionDao.getSubFunctions(func.getId(), null, null);
			if (list.size() > 0)
				idList.addAll(this.getSubFuncId(func.getId()));
		}
		
		return idList;
	}

	@Override
	public void deleteRoleFunc(Long id) {

		RoleFunction rf = new RoleFunction();
		rf.setFunction(functionDao.getFunctionById(id, null));
		
		functionDao.deleteRoleFunc(rf);
	}

	@Override
	public void deleteOrgFunc(Long id) {

		OrganizationFunction of = new OrganizationFunction();
		of.setFunction(functionDao.getFunctionById(id, null));
		
		functionDao.deleteOrgFunc(of);
	}

	@Override
	public String buildFunctionTree(Long parentId, Long roleId, List<Long> idList) {
		
		StringBuffer buffer = new StringBuffer();
		buffer.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
		buffer.append("<tree id=\"0\">");
		
//		List<Function> firstLevelFunctions = functionDao.getLevelFunctionsByParentId(null, parentId, new Long(2), idList);
		if (alls == null || alls.size() == 0) {
			alls = functionDao.getAllFunctionsByAppId(100L);
		}
		
		String existFuncPointCodes = "";
//		List<Long> list = functionDao.getAuthCodes(parentId, roleId);
		for (Long functionId : idList) {
			if (existFuncPointCodes == null && "".equals(existFuncPointCodes))
				existFuncPointCodes = "," + functionId + ",";
			else 
				existFuncPointCodes = existFuncPointCodes + functionId + ",";
		}
		
		for (Function function : alls) {
			if (function.getParent() != null && parentId.toString().equals(function.getParent().getId().toString())) {
				if (("," + existFuncPointCodes).indexOf("," + function.getId() + ",") > -1) {
					buffer.append(buildChildrenByXML(function, existFuncPointCodes, idList, alls));
				}
			}
		}
		buffer.append("</tree>");
		
		return buffer.toString();
	}
	
	public String buildChildrenByXML(Function function, String existFuncPointCodes, List<Long> idList, List<Function> alls) {
		
		StringBuffer buffer = new StringBuffer();
		buffer.append("<item text= \"" + function.getLabel() + "\"  id= \"" + function.getId() + "\"");
		// 将有权限的选中
		boolean hasAuth = (("," + existFuncPointCodes).indexOf("," + function.getId() + ",") != -1);
		if (!"#".equals(function.getUrl())) {
			buffer.append(hasAuth ? " checked=\"1\"" : "");
		}

		buffer.append(" im0=\"fun3.gif\" im1=\"fun2.gif\" im2=\"fun1.gif\">");

		//List<Function> children = functionDao.getSubFunctions(function.getId(), null, idList);
		for (Function func : alls) {
			if (func.getParent() != null && func.getParent().getId().toString().equals(function.getId().toString())) {
				if (("," + existFuncPointCodes).indexOf("," + function.getId() + ",") > -1) {
					buffer.append(buildChildrenByXML(func, existFuncPointCodes, idList, alls));
				}
			}
		}
		buffer.append("</item>");
		return buffer.toString();
	}
	
	@Override
	public List<Function> getFistLevelsByidList(List<Long> functionIdList, Long appId) {
		
		return functionDao.getFistLevelsByidList(functionIdList, appId);
	}

	@Override
	public List<FunctionAction> getFunctionActionByFucntionId(Long functionId) {
		
		return functionDao.getActionByFunctionId(functionId);
	}

	@Override
	public FunctionAction getActionById(Long actionId) {
		return functionDao.getActionById(actionId);
	}

	@Override
	public FunctionAction saveOrUpdate(FunctionAction action) {
		return functionDao.saveOrUpdate(action);
	}

	@Override
	public void deleteActions(Long id) {
		functionDao.deleteAction(id);
	}
	
	@Override
	/**
	 * 根据当前登录用户查询应该展示的菜单
	 */
	public List<Function> getFunctionsByUserId(Long userid){
		return functionDao.getFunctionsByUserId(userid);
	}
}
