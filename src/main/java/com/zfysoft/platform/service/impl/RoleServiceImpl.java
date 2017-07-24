/**
 * 版权所有 2013 成都子非鱼软件有限公司 保留所有权利
 * 1 项目签约客户只拥有对项目业务代码的所有权，以及在本项目范围内使用平台框架
 * 2 平台框架及相关代码属子非鱼软件有限公司所有，未经授权不得扩散、二次开发及用于其它项目
 */
package com.zfysoft.platform.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.zfysoft.common.util.StringUtil;
import com.zfysoft.platform.dao.ActionDao;
import com.zfysoft.platform.dao.FunctionDao;
import com.zfysoft.platform.dao.RoleDao;
import com.zfysoft.platform.model.Function;
import com.zfysoft.platform.model.Role;
import com.zfysoft.platform.model.RoleAction;
import com.zfysoft.platform.model.RoleFunction;
import com.zfysoft.platform.model.User;
import com.zfysoft.platform.service.RoleService;

/**
 * @author chenhm
 * @date 2013-7-3
 */
@Service
public class RoleServiceImpl implements RoleService {

	@Resource
	private RoleDao roleDao;
	@Resource
	private FunctionDao functionDao;
	@Resource
	private ActionDao btnDao;

	@Override
	public List<Role> queryRoleList(String name) {
		
		return roleDao.queryRoleList(name);
	}

	@Override
	public Role queryRoleById(Long roleId) {
		
		return roleDao.queryRoleId(roleId);
	}

	@Override
	public String deleteRoleById(Long roleId) {

		return roleDao.deleteRoleById(roleId);
	}

	@Override
	public String addRole(Role role) {
		
		return roleDao.addRole(role);
	}

	@Override
	public String updateRole(Role role) {
		
		return roleDao.updateRole(role);
	}

	@Override
	public List<Long> getRoleIdByUser(User loginUser) {
		
		return roleDao.getRoleIdByUser(loginUser);
	}

	// 生成json对象
	public String nodeList2Json(List<Function> list) {  
		
        String json = "{id:0," + createTreeNodeJson(list) + "}";
        return json;  
    } 
	private String createTreeNodeJson(List<Function> list){ 
		
        StringBuilder strBulider = new StringBuilder("item:[");  
        for(int i = 0; i < list.size(); i++){
            Function func = list.get(i);  
            strBulider.append(node2Json(func));
            if (func.getApp() == null) {
            	List<Function> funcList = functionDao.getChildren(func.getId(), true);
            	if (funcList.size() > 0) {
            		for (Function f : funcList) {
            			strBulider.append(node2Json(f));
            			List<Function> subList = functionDao.getChildren(f.getId(), false);
            			strBulider.append(createTreeNodeJson(subList)+"}");  
            		}
            	}
            } else {
            	 List<Function> subList = functionDao.getChildren(func.getId(), false);
                 if(subList.size() > 0){  
                     strBulider.append(createTreeNodeJson(subList)+"}");  
                 }
            }
            if(i != list.size()-1)  
                strBulider.append(",");  
        }  
        strBulider.append("]");  
        return strBulider.toString();  
    }  
	 private static String node2Json(Function func){  
	        StringBuilder strBulider=new StringBuilder("{");  
	        strBulider.append("id:\"").append(func.getId()).append("\",text:\"").append(func.getLabel())  
	            .append("\",im0:\"").append("folderClosed.gif").append("\",im1:\"").append("folderClosed.gif")
	            .append("\",im2:\"").append("folderClosed.gif").append("\"");  
	        if(func.getMenuLevel() == 1)  
	            strBulider.append(",open:1");  
	        if(func.getApp() == null)  
	            strBulider.append(",select:1");  
//	        if(node.isHasChild())  
//	            strBulider.append(",child:1,");  
//	        else  
//	            strBulider.append(",child:0}");  
	        return strBulider.toString();  
	    }  
	
	@Override
	public String buildFunctionTree(Long appId, Long roleId) {
		
		if (appId == null) {
			appId = 100L;
		}
		List<Function> list = FunctionServiceImpl.getAlls();
		if (list == null || list.size() == 0) {
			list = functionDao.getAllFunctionsByAppId(appId);
		}
		
		StringBuffer buffer = new StringBuffer();
		buffer.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
		buffer.append("<tree id=\"0\">");
		for (Function func : list) {
			if (func.getId() == appId || appId.toString().equals(func.getId().toString())) {
				buffer.append("<item text=\"" + func.getLabel() + "\" id=\"app_" + appId + "\" open=\"1\"  selected=\"1\" ");
				break;
			}
		}
		buffer.append(" im0=\"folderClosed.gif\" im1=\"folderClosed.gif\" im2=\"folderClosed.gif\">");
		
		List<Long> idList = functionDao.getAuthCodes(appId, roleId);
		String existFuncPointCodes = "";
		for (Long functionId : idList) {
			if (existFuncPointCodes == null && "".equals(existFuncPointCodes))
				existFuncPointCodes = "," + functionId + ",";
			else 
				existFuncPointCodes = existFuncPointCodes + functionId + ",";
		}
		for (Function func : list) {
			if (StringUtil.isNotEmptyOrNull(func.getApp() == null?"":func.getApp().getId())) {
				if (func.getMenuLevel() == 1 || func.getMenuLevel().toString().equals("1")) {
					buffer.append(buildChildrenByXML(func, existFuncPointCodes, list));
				}
			}
		}
		buffer.append("</item>");
		buffer.append("</tree>");
		
		return buffer.toString();
	}
	
	public String buildChildrenByXML(Function function, String existFuncPointCodes, List<Function> list) {
		
		StringBuffer buffer = new StringBuffer();
		buffer.append("<item text= \"" + function.getLabel() + "\" id= \"" + function.getId() + "\"");
		// 将有权限的选中
		boolean hasAuth = (("," + existFuncPointCodes).indexOf("," + function.getId() + ",") != -1);
		if (!"#".equals(function.getUrl())) {
			buffer.append(hasAuth ? " checked=\"1\"" : "");
		}
		buffer.append(" im0=\"func.png\" im1=\"func.png\" im2=\"func.png\">");

		for (Function func : list) {
			if (func.getParent() != null && function.getId() == func.getParent().getId()) {
				buffer.append(buildChildrenByXML(func, existFuncPointCodes, list));
			}
		}
		buffer.append("</item>");
		
		return buffer.toString();
	}

	@Override
	public void authorize(Long appId, Long roleId, String selectedIds, String unSelectedIds) {
		
		// 查询角色已经有的权限
		List<RoleFunction> roleList = roleDao.getRoleFunctionList(appId, roleId);
		List<Long> funcIdList = new ArrayList<Long>();
		for (RoleFunction rf : roleList) {
			funcIdList.add(rf.getFunction().getId());
		}
		if (funcIdList.size() > 0)
			funcIdList.add(appId);
		String[] ids = selectedIds.split(",");
		List<String> selectedList = Arrays.asList(ids);
		ids = unSelectedIds.split(",");
		List<String> unSelectedList = Arrays.asList(ids);
		unSelectedList = new ArrayList<String>(unSelectedList);
		unSelectedList.remove(0);
		for (String id : selectedList) {
			if(id==null || id.equals("")) continue;
			if (!funcIdList.contains(Long.parseLong(id))) {
				Function function = functionDao.getFunctionById(Long.parseLong(id), null);
				Role role = roleDao.queryRoleId(roleId);
				RoleFunction roleFunction = new RoleFunction();
				roleFunction.setRole(role);
				roleFunction.setFunction(function);
				roleDao.saveRoleFunction(roleFunction);
			}
		}
		
		for (String id : unSelectedList) {
			if(id==null || id.equals("")) continue;
			if (funcIdList.contains(Long.parseLong(id))) {
				RoleFunction roleFunction = roleDao.getRoleFunctionByRoleAndFunctionId(roleId, Long.parseLong(id));
				roleDao.deleteRoleFunction(roleFunction);
				// 同时删除角色具有的按钮权限
				List<RoleAction> list = btnDao.queryByRoleFunction(roleFunction);
				for (RoleAction buttonRoleFun : list) {
					btnDao.delete(buttonRoleFun);
				}
			}
		}
		
//		// 先删除原有权限
//		RoleFunction roleFunction = roleDao.getRoleFunctionByRoleAndFunctionId(roleId, appId);
//		if (roleFunction != null) {
//			roleDao.deleteRoleFunction(roleFunction);
//			List<RoleFunction> roleList = roleDao.getRoleFunctionList(appId, roleId);
//			for (RoleFunction rf : roleList) {
//				roleDao.deleteRoleFunction(rf);
//			}
//		}
//		
//		// 新加入权限
//		if (selectedIds != null && selectedIds.length() > 0) {
//			String[] ids = selectedIds.split(",");
//			if (ids == null) {
//				return;
//			}
//			for (String id : ids) {
//				if (id == null || id.length() <= 0) {
//					continue;
//				}
//				Long fid = Long.parseLong(id);
//				Function function = functionDao.getFunctionById(fid, new Long(1));
//				Role role = roleDao.queryRoleId(roleId);
//				roleFunction = new RoleFunction();
//				roleFunction.setRole(role);
//				roleFunction.setFunction(function);
//				roleDao.saveRoleFunction(roleFunction);
//			}
//		}
	}

	
	@Override
	public void grantRoleToUser(Long userId, Long[] roleIds) {
		// 先删除原有角色
		roleDao.deleteUserRoles(userId);
		//分配角色
		roleDao.updateUserRoles(userId, roleIds);
	}

	@Override
	public boolean queryRoleByName(String name, Long id) {
		
		Role role = roleDao.getRoleByName(name, id);
		if (role == null) {
			return false;
		} else {
			return true;
		}
	}

	@Override
	public boolean queryRoleByCode(String code, Object object) {

		Role role = roleDao.queryRoleByCode(code, object);
		if (role == null) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * 根据当前登录人，查询对应角色的主页
	 */
	public String queryHomePage(User user){
		return roleDao.queryHomePage(user);
	}


	@Override
	public Role getRoleByCode(String roleCode) {
		return roleDao.queryRoleByCode(roleCode, null);
	}
}
