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

import com.zfysoft.common.util.Page;
import com.zfysoft.platform.dao.FunctionDao;
import com.zfysoft.platform.dao.OrganizationDao;
import com.zfysoft.platform.dao.UserDao;
import com.zfysoft.platform.model.Function;
import com.zfysoft.platform.model.Organization;
import com.zfysoft.platform.model.OrganizationFunction;
import com.zfysoft.platform.model.OrganizationUser;
import com.zfysoft.platform.model.User;
import com.zfysoft.platform.service.OrganizationService;

/**
 * @author hudt
 * @date 2013-7-18
 */
@Service("organizationService") 
public class OrganizationServiceImpl implements OrganizationService {

	@Resource
	private OrganizationDao organizationDao;
	
	@Resource
	private UserDao userDao;
	
	@Resource
	private FunctionDao functionDao;
	/**
	 * 通过ID得到组织机构
	 * @param 组织机构主键
	 * @return
	 */
	public Organization queryOrgaById(Long id){
		return organizationDao.getOrgaById(id);
	}
	
	
	
	/**
	 * 增加组织机构
	 * @param 
	 * @return
	 */
	public void saveOrga(Organization orga){
		organizationDao.saveOrga(orga);
	}
	
	
	/**
	 * 修改组织机构
	 * @param 
	 * @return
	 */
	public void updateOrga(Organization orga){
		organizationDao.updateOrga(orga);
	}
	

	
	/**
	 * 删除组织机构
	 * @param 
	 * @return
	 */
	public void deleteOrgaById(Long id){
		organizationDao.deleteOrgaById(id);
	}
	
	
	
	
	/**
	 * 查询所有组织机构
	 */
	public List<Organization> queryAllOrga(){
		return organizationDao.queryAllOrga();
	}

	/**
	 * 根据ID查询其第一层子组织机构
	 */
	public List<Organization> querySubOrgaById(Long id){
		return organizationDao.querySubOrgaById(id);
	}
	
	//根据类型code 查询所有属于该code的组织机构
	@Override
	public List<Organization> queryAllOrgaByTypeCode(String type) {
		return organizationDao.queryAllOrgaByTypeCode(type);
	}
	
	//根据ID查询其所有子孙组织机构
	@Override
	public List<Organization> queryAllSubOrgaById(Long id) {
		return organizationDao.queryAllSubOrgaById(id);
	}
	
	//根据父code查询所有子code（配置用）
	@Override
	public List<String> queryAllSubTypeByCode(String code) {
		return organizationDao.queryAllSubTypeByCode(code);
	}
	
	//根据子code查询所有父code（配置用）
	@Override
	public List<String> queryAllParentTypeBySubcode(String subCode) {
		return organizationDao.queryAllParentTypeBySubcode(subCode);
	}

	/**
	 * 根据父code查询所有真实存在的子code
	 */
	public List<String> queryAllRealExistSubTypeByCode(String code){
		return null;
	}



	/* 
	 * 查根节点
	 */
	@Override
	public List<Organization> queryRootOrga() {
		
		return organizationDao.queryRootOrga();
	}



	/**
	 * 查询该组织机构有哪些USER
	 */
	@Override
	public List<User> queryUserListByOrgaId(Long orgaId, Page page) {
		return organizationDao.queryUserListByOrgaId(orgaId, page);
	}

	/**
	 * 根据组织机构ID查询此组织机构下没有哪些人
	 */
	public List<User> queryUserListNotInByOrgaId(Long orgaId,Page page,String usernameFilter){
		return organizationDao.queryUserListNotInByOrgaId(orgaId, page,usernameFilter);
	}

	/**
	 * 把用户分配到组织机构
	 */
	@Override
	public Long saveOrgaUser(Long orgaId, Long userId) {
		Organization orga = organizationDao.getOrgaById(orgaId);
		if(orga == null){
			return 0L;
		}
		User user = userDao.getById(userId);
		OrganizationUser orgaUser = new OrganizationUser();
		orgaUser.setUser(user);
		orgaUser.setOrganization(orga);
		organizationDao.saveUserOrga(orgaUser);
		return 1L;
	}

	/**
	 * 删除用户组织关系
	 */
	public void deleteOrgaUser(Long userId, Long orgaId){
		OrganizationUser orgaUser = organizationDao.queryOrgaUser(userId, orgaId);
		if(orgaUser != null){
			organizationDao.deleteUserFromOrga(orgaUser);
		}
	}
	
	
	
	/////////////////////////////////////////
	////以下是和【菜单】的关系查询
	
	/**
	* 保存和修改组织机构和菜单对应关系
	* @param orgaFun
	*/
	public void saveOrUpdate(OrganizationFunction orgaFun){
		organizationDao.saveOrUpdate(orgaFun);
	}
	
	/**
	* 删除组织机构和菜单对应关系
	* @param orgaId 组织机构id
	* @param funId 菜单Id
	*/
	public void deleteOrgaFunById(Long orgaId,Long funId){
		organizationDao.deleteOrgaFunById(orgaId, funId);
	}
	
	/**
	* 根据id查询组织机构和菜单对应关系
	* @param id
	* @return
	*/
	public OrganizationFunction getOrgaFunById(Long id){
		return organizationDao.getOrgaFunById(id);
	}
	
	/**
	* 根据组织机构id查询其单层对应关系
	* @param orgaId
	* @return
	*/
	public List<Function> getSingleFlourOrgaFunListByOrgaId(Long orgaId){
		List<OrganizationFunction> orgaFuns =  organizationDao.getSingleFlourOrgaFunListByOrgaId(orgaId);
		List<Function> funs = new ArrayList<Function>();
		for(OrganizationFunction orgaFun : orgaFuns){
			funs.add(orgaFun.getFunction());
		}
		return funs;
	}
	
	/**
	* 根据组织机构id查询其所有包括继承的对应关系
	* @param orgaId
	* @return
	*/
	public List<Function> getAllOrgaFunListByOrgaId(Long orgaId){
		List<OrganizationFunction> orgaFuns =  organizationDao.getAllOrgaFunListByOrgaId(orgaId);
		List<Function> funs = new ArrayList<Function>();
		for(OrganizationFunction orgaFun : orgaFuns){
			funs.add(orgaFun.getFunction());
		}
		return funs;
	}
	
	/**
	* 根据User id查询其所在组织所有对应关系
	* @param orgaId
	* @return
	*/
	public List<Function> getOrgaFunListByUserId(Long userId){
		List<OrganizationFunction> orgaFuns =  organizationDao.getOrgaFunListByUserId(userId);
		List<Function> funs = new ArrayList<Function>();
		for(OrganizationFunction orgaFun : orgaFuns){
			funs.add(orgaFun.getFunction());
		}
		return funs;
	}
	
public String buildFunctionTree(Long appId, Long orgaId) {
		
//		List<Function> list = functionDao.getAllFunctionsByAppId(appId);
//		return nodeList2Json(list);
		
		Function app = functionDao.getFunctionById(appId, new Long(1));
		if (app == null) {
			return "";
		}
		StringBuffer buffer = new StringBuffer();
		buffer.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
		buffer.append("<tree id=\"0\">");
		buffer.append("<item text=\"" + app.getLabel() + "\" id=\"app_" + appId + "\" open=\"1\"  selected=\"1\" ");
		buffer.append(" im0=\"folderClosed.gif\" im1=\"folderClosed.gif\" im2=\"folderClosed.gif\">");
		
		List<Function> firstLevelFunctions = functionDao.getLevelFunctionsByAppId(null, appId, new Long(1));
		String existFuncPointCodes = "";
		
		
//		List<Function> flist = getAllOrgaFunListByOrgaId(orgaId);
		//TODO 根据组织机构id查询其单层对应关系
		List<Function> flist = getSingleFlourOrgaFunListByOrgaId(orgaId);
		
		List<Long> list = new ArrayList<Long>();
		for(Function f : flist){
			list.add(f.getId());
		}
		for (Long functionId : list) {
			if (existFuncPointCodes == null && "".equals(existFuncPointCodes))
				existFuncPointCodes = "," + functionId + ",";
			else 
				existFuncPointCodes = existFuncPointCodes + functionId + ",";
		}
		for (Function function : firstLevelFunctions) {
			buffer.append(buildChildrenByXML(function, existFuncPointCodes));
		}
		buffer.append("</item>");
		buffer.append("</tree>");
		
		return buffer.toString();
	}
	
	public String buildChildrenByXML(Function function, String existFuncPointCodes) {
		
		StringBuffer buffer = new StringBuffer();
		buffer.append("<item text= \"" + function.getLabel() + "\" id= \"" + function.getId() + "\"");
		// 将有权限的选中
		boolean hasAuth = (("," + existFuncPointCodes).indexOf("," + function.getId() + ",") != -1);
		if (!"#".equals(function.getUrl())) {
			buffer.append(hasAuth ? " checked=\"1\"" : "");
		}
			
	
		buffer.append(" im0=\"func.png\" im1=\"func.png\" im2=\"func.png\">");
	
		List<Function> children = functionDao.getSubFunctions(function.getId(), null, null);
		for (Function func : children) {
			buffer.append(buildChildrenByXML(func, existFuncPointCodes));
		}
		buffer.append("</item>");
		
		return buffer.toString();
	}



	/* (non-Javadoc)
	 * @see com.zfysoft.platform.service.OrganizationService#authorize(java.lang.Long, java.lang.Long, java.lang.String)
	 */
	@Override
	public void authorize(Long appId, Long orgaId, String selectedIds, String unSelectedIds) {
		// 查询组织机构已经有的权限
				List<OrganizationFunction> orgaList = organizationDao.getOrgaFunctionList(appId, orgaId);
				List<Long> funcIdList = new ArrayList<Long>();
				for (OrganizationFunction rf : orgaList) {
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
					if (!id.equals("") && !funcIdList.contains(Long.parseLong(id))) {
						Function function = functionDao.getFunctionById(Long.parseLong(id), null);
						Organization orga = organizationDao.getOrgaById(orgaId);
						OrganizationFunction orgaFunction = new OrganizationFunction();
						orgaFunction.setOrganization(orga);
						orgaFunction.setFunction(function);
						organizationDao.saveOrUpdate(orgaFunction);
					}
				}
				for (String id : unSelectedList) {
					if (!id.equals("") && funcIdList.contains(Long.parseLong(id))) {
						//OrganizationFunction orgaFunction = organizationDao.getOrgaFun(orgaId, Long.parseLong(id));
						organizationDao.deleteOrgaFunById(orgaId, Long.parseLong(id));
					}
				}
		
		/*// 先删除原有权限
		organizationDao.deleteOrgaFunById(orgaId, appId);
		
		// 新加入权限
		if (selectedIds != null && selectedIds.length() > 0) {
			String[] ids = selectedIds.split(",");
			if (ids == null) {
				return;
			}
			for (String id : ids) {
				if (id == null || id.length() <= 0) {
					continue;
				}
				Long fid = Long.parseLong(id);
				Function function = functionDao.getFunctionById(fid, new Long(1));
				Organization orga = organizationDao.getOrgaById(orgaId);
				OrganizationFunction orgaFun= new OrganizationFunction();
				orgaFun.setOrganization(orga);
				orgaFun.setFunction(function);
				organizationDao.saveOrUpdate(orgaFun);
			}
		}*/
		
	}
	
	public Organization querySingleOrgaByUser(Long userId){
		return organizationDao.querySingleOrgaByUser(userId);
	}

	public List<Organization> queryAllOrga(long id){
		return organizationDao.queryAllOrga(id);
	}
	public List<User> queryAllUserUnderOrga(String orgCode){
		return organizationDao.queryAllUserUnderOrga(orgCode);
	}
}
