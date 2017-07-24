/**
 * 版权所有 2013 成都子非鱼软件有限公司 保留所有权利
 * 1 项目签约客户只拥有对项目业务代码的所有权，以及在本项目范围内使用平台框架
 * 2 平台框架及相关代码属子非鱼软件有限公司所有，未经授权不得扩散、二次开发及用于其它项目
 */
package com.zfysoft.platform.service.impl;

import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import com.zfysoft.platform.dao.ActionDao;
import com.zfysoft.platform.dao.FunctionDao;
import com.zfysoft.platform.dao.OrganizationDao;
import com.zfysoft.platform.model.FunctionAction;
import com.zfysoft.platform.model.OrganizationAction;
import com.zfysoft.platform.model.RoleAction;
import com.zfysoft.platform.model.User;
import com.zfysoft.platform.service.ActionService;

/**
 * @author hudt
 * @date 2013-7-30
 */
@Service("buttonService") 
public class ActionServiceImpl implements ActionService {
	
	@Resource
	private ActionDao buttonDao;
	
	@Resource
	private FunctionDao functionDao;
	
	@Resource
	private OrganizationDao organizationDao;

	@Override
	public void saveOrUpdateInRole(Long funId, Long roleId, Long buttonId) {
		
		RoleAction buttonRoleFun = new RoleAction();
		buttonRoleFun.setRoleFunction(functionDao.getRoleFunction(roleId, funId));
		buttonRoleFun.setAction(buttonDao.queryById(buttonId));
		buttonDao.save(buttonRoleFun);
	}


	@Override
	public void saveOrUpdateInOrga(Long funId, Long orgaId, Long buttonId) {

		OrganizationAction btnOrgaFun = new OrganizationAction();
		btnOrgaFun.setOrgaFun(organizationDao.getOrgaFun(orgaId, funId));
		btnOrgaFun.setAction(buttonDao.queryById(buttonId));
		buttonDao.save(btnOrgaFun);
	}

	
	@Override
	public void deleteInRole(Long roleId,Long funId) {
		buttonDao.deleteInRole(roleId, funId);
	}

	
	@Override
	public void deleteInOrga(Long orgaId,Long funId) {
		buttonDao.deleteInOrga(orgaId, funId);

	}

	
	@Override
	public FunctionAction queryById(Long id) {
		return buttonDao.queryById(id);
	}

	
	@Override
	public List<FunctionAction> queryByFunId(Long funId) {
		
		List<FunctionAction> list = buttonDao.queryByFunId(funId);
		return list;
	}

	
	@Override
	public List<FunctionAction> queryByRoleId(Long roleId) {
		return buttonDao.queryByRoleId(roleId);
	}

	
	@Override
	public List<FunctionAction> queryByRoleIdAndFunId(Long roleId, Long functionId) {
		return buttonDao.queryByRoleIdAndFunId(roleId, functionId);
	}

	
	@Override
	public List<FunctionAction> queryByOrgaId(Long orgaId) {
		return buttonDao.queryByOrgaId(orgaId);
	}

	
	@Override
	public List<FunctionAction> queryByOrgaIdAndFunId(Long orgaId, Long functionId) {
		return buttonDao.queryByOrgaIdAndFunId(orgaId, functionId);
	}
	
	/**
	 * 根据当前登录人，以及需要访问的菜单，找到已经被授权的Buttons
	 */
	public  List<FunctionAction> queryPermissionButtons(User loginUser,String funUrl){
		return buttonDao.queryPermissionButtons(loginUser, funUrl);
	}
	
	
	/**
	 * 根据当前登录人，以及需要访问的菜单，找到已经被授权的Buttons
	 */
	public  List<String> queryPermissionButtonCodes(User loginUser,String funUrl){
		return buttonDao.queryPermissionButtonCodes(loginUser, funUrl);
	}

}
