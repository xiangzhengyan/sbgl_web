/**
 * 版权所有 2013 成都子非鱼软件有限公司 保留所有权利
 * 1 项目签约客户只拥有对项目业务代码的所有权，以及在本项目范围内使用平台框架
 * 2 平台框架及相关代码属子非鱼软件有限公司所有，未经授权不得扩散、二次开发及用于其它项目
 */
package com.zfysoft.platform.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zfysoft.common.util.ArrayUtil;
import com.zfysoft.common.util.JsonUtil;
import com.zfysoft.common.util.MD5Util;
import com.zfysoft.common.util.Page;
import com.zfysoft.common.util.ValidateUtil;
import com.zfysoft.common.util.XmlViewHelper;
import com.zfysoft.platform.config.ApplicationConfig;
import com.zfysoft.platform.config.ButtonConfig;
import com.zfysoft.platform.model.FunctionAction;
import com.zfysoft.platform.model.Function;
import com.zfysoft.platform.model.FunctionVo;
import com.zfysoft.platform.model.Organization;
import com.zfysoft.platform.model.ResultData;
import com.zfysoft.platform.model.User;
import com.zfysoft.platform.service.ActionService;
import com.zfysoft.platform.service.FunctionService;
import com.zfysoft.platform.service.OrganizationService;
import com.zfysoft.platform.service.UserService;

/**
 * 组织机构管理
 * @author hudt
 * @date 2013-7-18
 */
@Controller
@RequestMapping("/organization/*")
public class OrganizationController {

	@Resource OrganizationService organizationService;
	
	@Resource UserService userService;
	@Resource FunctionService functionService;
	
	@Resource ActionService buttonService;
	
	//本地化工具类
	Locale local = Locale.getDefault(); //如果是中文系统,得到的是zh_CN
	//Locale local = new Locale("en", "US");
	//将properties文件转存为键值对存储
	ResourceBundle rb = ResourceBundle.getBundle("com/zfysoft/platform/local/portal/i18n", local, OrganizationController.class.getClassLoader());
	ResourceBundle rb_orga = ResourceBundle.getBundle("com/zfysoft/platform/local/base/i18n_organization", local, OrganizationController.class.getClassLoader());
	ResourceBundle rb_user = ResourceBundle.getBundle("com/zfysoft/platform/local/base/i18n_user", local, UserController.class.getClassLoader());
	
	@SuppressWarnings("unchecked")
	@RequestMapping("/save.do")
	@ResponseBody
	public ResultData save(HttpServletRequest request,Organization orga,Long pid){
		if(orga.getName()==null || orga.getName().equals("")){
			return new ResultData(ResultData.ERROR,rb_orga.getString("platform.organization.validate.nameNoNuLL"),ResultData.OPT_ADD,orga);
		}
		if(pid != null){
			Organization parent = organizationService.queryOrgaById(pid);
			orga.setParent(parent);
		}
		organizationService.saveOrga(orga);
		orga.setParent(null);
		Object[] array = {orga,((Map<String,String>)request.getServletContext().getAttribute("orgImgs")).get(orga.getType())};
		return new ResultData(ResultData.SUCCESS,"",ResultData.OPT_ADD,array);
	}
	
	@RequestMapping("/updateOrgaName.do")
	@ResponseBody
	public ResultData save(HttpServletRequest request,Long id,String name){
		Organization orga = organizationService.queryOrgaById(id);
		orga.setName(name);
		organizationService.updateOrga(orga);
		return new ResultData(ResultData.SUCCESS);
	}
	
	@RequestMapping("/queryOrganization.do")
	public String queryOrganization(HttpServletRequest request){
		User loginUser = (User) request.getSession().getAttribute("loginUser");
		List<FunctionAction> buttons =  ButtonConfig.queryPermissionButtons(loginUser, "organization/queryOrganization.do");
		List<FunctionAction> buttonsNoGet = new ArrayList<FunctionAction>(buttons.size());
		for(FunctionAction button : buttons){
			button.setFunction(null);
			buttonsNoGet.add(button);
		}
		System.out.println(JsonUtil.list2json(buttonsNoGet));
		request.setAttribute("buttons", JsonUtil.list2json(buttonsNoGet));
		//明天写拼urls
		
		return "platform/organization/query_organization_list";   
	}
	
	@RequestMapping("/selectOrganization.do")
	public String selectOrganization(HttpServletRequest request){
		User loginUser = (User) request.getSession().getAttribute("loginUser");

		
		return "platform/organization/select_organization";   
	}
	
	@RequestMapping("/queryOrgaTree.do")
	public void orgaTree(HttpServletRequest request,HttpServletResponse response){
		User loginUser = (User) request.getSession().getAttribute("loginUser");
		Organization org = organizationService.querySingleOrgaByUser(loginUser.getId());
		List<Organization> orgas = organizationService.queryAllOrga(org.getId()); 
		Document document = DocumentHelper.createDocument();  //创建文档    
		Element tree = document.addElement("tree");//根节点tree
		tree.addAttribute("id", "0");
		
		@SuppressWarnings("unchecked")
		Map<String,String> orgImgs = (Map<String,String>)request.getServletContext().getAttribute("orgImgs");
		for(Organization company : orgas){
			if (company.getId().equals(org.getId())) {
				Element item = tree.addElement("item"); 
				item.addAttribute("text",company.getName());
				item.addAttribute("id",company.getId()+""); 
				item.addAttribute("open","1");
				item.addAttribute("type",company.getType());
				item.addAttribute("im0",orgImgs.get(company.getType()));
				item.addAttribute("im1",orgImgs.get(company.getType()));
				item.addAttribute("im2",orgImgs.get(company.getType())); 
				treeTypeAddOrga(company.getId(), item, orgImgs, orgas);
				break;
			} else {
			}
		}
		System.out.println(document.asXML());  
		try {
			XmlViewHelper.print(response, document.asXML()); 
		}
		catch (IOException e) {
			e.printStackTrace(); 
		} 
	}
	
	private void treeTypeAddOrga(Long parentId,Element parent,Map<String,String> orgImgs, List<Organization> orgas){
//		List<Organization> orgas = organizationService.querySubOrgaById(parentId);
		for(Organization orga : orgas){
			if (orga.getParent() !=null && orga.getParent().getId() == parentId) {
				Element sub = parent.addElement("item");
				sub.addAttribute("id",orga.getId()+"");
				sub.addAttribute("text",orga.getName()); 
				sub.addAttribute("type",orga.getType()); 
				sub.addAttribute("im0",orgImgs.get(orga.getType()));
				sub.addAttribute("im1",orgImgs.get(orga.getType()));
				sub.addAttribute("im2",orgImgs.get(orga.getType()));
				treeTypeAddOrga(orga.getId(),sub,orgImgs, orgas);
			}
		}
	}
	
	/**
	 * 通过类型获得可以新建的类型
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/querySubTypes.do")
	@ResponseBody
	public List<String[]> querySubTypes(HttpServletRequest request,String id){ 
		List<String[]> localList = new ArrayList<String[]>();
		if("org_root".equals(id)){
			String[] strs = {rb_orga.getString("platform.organization.organization"),"organization"};
			localList.add(strs);
			return localList;
		}
		List<String> list = ApplicationConfig.getOrganizationSubTypesById(Long.valueOf(id));
		Map<String,String> orgImgs = (Map<String,String>)request.getServletContext().getAttribute("orgImgs");
		for(int i = 0 ; i< list.size() ; i++){
			String[] strs = {
						rb_orga.getString("platform.organization."+list.get(i)),
						list.get(i),
						(String)orgImgs.get(list.get(i))
					};
			localList.add(strs);
		}
		
		if(localList == null || localList.isEmpty()){
			return null;
		}else{
			return localList; 
		}
	}
	
	
	/**
	 * 新建页面
	 */
	@RequestMapping("/viewSaveOrga.do")
	@ResponseBody
	public String[] viewSaveOrga(HttpServletRequest request,String type,String pid){
		if("org_root".equals(pid)){
			String save = rb_orga.getString("platform.organization.save");
			String typeName = rb_orga.getString("platform.organization.organization");
			String[] strs = new String[5];
			strs[0] = ""; //pid
			strs[1] = "/"; //所属
			strs[2] = save;
			strs[3] = typeName;
			strs[4] = type; 
			return strs;
		}
		String save = rb_orga.getString("platform.organization.save");
		String typeName = rb_orga.getString("platform.organization."+type);
		Organization orga = organizationService.queryOrgaById(Long.valueOf(pid));
		/*request.setAttribute("orga", orga);
		request.setAttribute("type", type);*/
		Organization tmp = orga;
		String name = tmp.getName();
		while(tmp.getParent()!=null){
			tmp = tmp.getParent();
			name += "/"+tmp.getName();
		}
		String[] names= name.split("/");
		name = "";
		for(int i = names.length-1;i>=0;i--){
			name += names[i] +"/";
		}
		if(name.length()>0){
			name.substring(0, name.length()-1); 
		}
		String[] strs = new String[5];
		strs[0] = orga.getId()+""; 
		strs[1] = name;
		strs[2] = save;
		strs[3] = typeName;
		strs[4] = type; 
		return strs;
	}
	
	
	/**
	 * 修改页面
	 */
	@RequestMapping("/viewUpdateOrga.do")
	@ResponseBody
	public String[] viewUpdateOrga(HttpServletRequest request,String id){
		if("org_root".equals(id)){
			id = null;
		}
		Organization orga = organizationService.queryOrgaById(Long.valueOf(id));
		String update = rb_orga.getString("platform.organization.update");
		String typeName = rb_orga.getString("platform.organization."+orga.getType());
		/*request.setAttribute("orga", orga);
		request.setAttribute("type", type);*/
		Organization tmp = orga;
		String name = "";
		while(tmp.getParent()!=null){
			tmp = tmp.getParent();
			name += tmp.getName() + "/";
		}
		String[] names= name.split("/");
		name = "";
		for(int i = names.length-1;i>=0;i--){
			name += names[i] +"/";
		}
		if(name.length()>0){
			name.substring(0, name.length()-1); 
		}
		String[] strs = new String[7];
		strs[0] = orga.getId()+""; 
		strs[1] = name;
		strs[2] = update;
		strs[3] = typeName;
		strs[4] = orga.getType(); 
		strs[5] = orga.getName();
		strs[6] = rb_orga.getString("platform.organization.type");
		return strs;
	}
	
	/**
	 * 左键点击查询组织机构和用户列表页面
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/queryById.do")
	@ResponseBody 
	public Object[] queryById(HttpServletRequest request,Long id,Page page,
			String from,String selectIds,String cancelIds){
		if("menu".equals(from)){
			request.getSession().removeAttribute("delIdList");
		}
		Organization orga = organizationService.queryOrgaById(id);
		List<String> list = organizationService.queryAllSubTypeByCode(orga.getType());
		Boolean emp = false;
		if(list!= null && list.contains("employee")){
			emp = true;
		}
		List<User> children = organizationService.queryUserListByOrgaId(id, page);
		String belong = "";
		Organization tmp = orga;
		while(tmp.getParent()!=null){
			tmp = tmp.getParent();
			belong += tmp.getName() + "/";
		}
		String[] names= belong.split("/");
		belong = "";
		for(int i = names.length-1;i>=0;i--){
			belong += names[i] +"/";
		}
		if(belong.length()>0){
			belong.substring(0, belong.length()-1); 
		}
		orga.setParent(null);
		Object[] objs = new Object[8];
		objs[0] = orga;
		objs[1] = belong;
		objs[2] = children;
		objs[3] = rb_orga.getString("platform.organization."+orga.getType());
		objs[4] = page;
		
		List<String> idList = (List<String>)request.getSession().getAttribute("delIdList");
		if(idList==null){
			idList = new ArrayList<String>();
		}
		if(selectIds==null){
			selectIds = "";
		}
		if(cancelIds==null){
			cancelIds = "";
		}
		String[] selectIdsArray = selectIds.split(",");
		String[] cancelIdsArray = cancelIds.split(",");
		for(String seleId : selectIdsArray){
			if(seleId != null && !idList.contains(seleId)){
				idList.add(seleId);
			}
		}
		for(String cancelId : cancelIdsArray){
			if(cancelId != null && idList.contains(cancelId)){
				idList.remove(cancelId);
			}
		}
		objs[5] = id;
		objs[6] = idList;
		objs[7] = emp;
		request.getSession().setAttribute("delIdList", idList);
		return objs;
	}
	
	/**
	 * 右键点击分配用户时
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/queryUserListNotGrantByOrgaId.do")
	@ResponseBody 
	public Object[] queryNotGrantById(HttpServletRequest request,Long id,Page page,String from,String selectIds,String cancelIds,String usernameFilter){
		if("menu".equals(from)){
			request.getSession().removeAttribute("idList");
		}
		Organization orga = organizationService.queryOrgaById(id);
		List<User> children = organizationService.queryUserListNotInByOrgaId(id, page,usernameFilter);
		String belong = "";
		Organization tmp = orga;
		while(tmp.getParent()!=null){
			tmp = tmp.getParent();
			belong += tmp.getName() + "/";
		}
		String[] names= belong.split("/");
		belong = "";
		for(int i = names.length-1;i>=0;i--){
			belong += names[i] +"/";
		}
		if(belong.length()>0){
			belong.substring(0, belong.length()-1); 
		}
		orga.setParent(null);
		Object[] objs = new Object[7];
		objs[0] = orga;
		objs[1] = belong;
		objs[2] = children;
		objs[3] = rb_orga.getString("platform.organization."+orga.getType());
		objs[4] = page;
		objs[5] = id;
		List<String> idList = (List<String>)request.getSession().getAttribute("idList");
		if(idList==null){
			idList = new ArrayList<String>();
		}
		if(selectIds==null){
			selectIds = "";
		}
		if(cancelIds==null){
			cancelIds = "";
		}
		String[] selectIdsArray = selectIds.split(",");
		String[] cancelIdsArray = cancelIds.split(",");
		for(String seleId : selectIdsArray){
			if(seleId != null && !idList.contains(seleId)){
				idList.add(seleId);
			}
		}
		for(String cancelId : cancelIdsArray){
			if(cancelId != null && idList.contains(cancelId)){
				idList.remove(cancelId);
			}
		}
		objs[6] = idList;
		request.getSession().setAttribute("idList", idList);
		return objs;
	}
	
	/**
	 * 把选中的用户分配给组织机构
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/saveGrantUserList.do")
	@ResponseBody 
	public ResultData saveGrantUserList(HttpServletRequest request,
			Long orgaId,String selectIds,String cancelIds){
		List<String> idList = (List<String>)request.getSession().getAttribute("idList");
		if(idList==null){
			idList = new ArrayList<String>();
		}
		if(selectIds==null){
			selectIds = "";
		}
		if(cancelIds==null){
			cancelIds = "";
		}
		String[] selectIdsArray = selectIds.split(",");
		String[] cancelIdsArray = cancelIds.split(",");
		for(String seleId : selectIdsArray){
			if(seleId != null && !idList.contains(seleId)){
				idList.add(seleId);
			}
		}
		for(String cancelId : cancelIdsArray){
			if(cancelId != null && idList.contains(cancelId)){
				idList.remove(cancelId);
			}
		}
		// save
		for(String uid : idList){
			if(uid != null && !uid.trim().equals("")){
				organizationService.saveOrgaUser(orgaId, Long.valueOf(uid));
			}
		}
		request.getSession().removeAttribute("idList");
		return new ResultData(ResultData.SUCCESS);
	}
	
	
		/**
		 * 把选中的用户分配给组织机构
		 */
		@SuppressWarnings("unchecked")
		@RequestMapping("/deleteUserFromOrga.do")
		@ResponseBody 
		public ResultData deleteUserFromOrga(HttpServletRequest request,
				Long orgaId,String selectIds,String cancelIds){
			List<String> idList = (List<String>)request.getSession().getAttribute("delIdList");
			if(idList==null){
				idList = new ArrayList<String>();
			}
			if(selectIds==null){
				selectIds = "";
			}
			if(cancelIds==null){
				cancelIds = "";
			}
			String[] selectIdsArray = selectIds.split(",");
			String[] cancelIdsArray = cancelIds.split(",");
			for(String seleId : selectIdsArray){
				if(seleId != null && !idList.contains(seleId)){
					idList.add(seleId);
				}
			}
			for(String cancelId : cancelIdsArray){
				if(cancelId != null && idList.contains(cancelId)){
					idList.remove(cancelId);
				}
			}
			// del
			for(String uid : idList){
				if(uid != null && !uid.trim().equals("")){
					organizationService.deleteOrgaUser(Long.valueOf(uid), orgaId);
				}
			}
			request.getSession().removeAttribute("delIdList");
			return new ResultData(ResultData.SUCCESS);
		}
		
	/**
	 * 删除组织机构
	 */
	@RequestMapping("/delete.do")
	@ResponseBody
	public void delete(HttpServletRequest request,Long id){
		organizationService.deleteOrgaById(id);
	}
	
	
	/**
	 * 新增用户之前，查询所属orga
	 */
	@RequestMapping("/queryOrgaById.do")
	@ResponseBody 
	public Object[] queryOrgaById(HttpServletRequest request,Long id){
		
		Organization orga = organizationService.queryOrgaById(id);
		String belong = "";
		Organization tmp = orga;
		while(tmp.getParent()!=null){
			tmp = tmp.getParent();
			belong += tmp.getName() + "/";
		}
		String[] names= belong.split("/");
		belong = "";
		for(int i = names.length-1;i>=0;i--){
			belong += names[i] +"/";
		}
		if(belong.length()>0){
			belong.substring(0, belong.length()-1); 
		}
		orga.setParent(null);
		Object[] objs = new Object[3];
		objs[0] = orga;
		objs[1] = belong;
		objs[2] = rb_orga.getString("platform.organization."+orga.getType());
		return objs;
	}
	
	/**
	 * 新增用户
	 */
	@RequestMapping("/saveUser.do")
	@ResponseBody 
	public ResultData queryOrgaById(HttpServletRequest request,Long orgaId,User user){
		String tipMsg = null;
		user.setStatus(1);
		user.setPassword(MD5Util.getMD5("111111"));		//初始化密码111111
		if (!ValidateUtil.isNullOrEmpty(user.getEmail())  && user.getEmail().indexOf("@") < 0) {
			tipMsg = rb_user.getString("platform.user.validate_email_format");
			return new ResultData(ResultData.ERROR,tipMsg);
		}
				
	    else if (!ValidateUtil.isNullOrEmpty(user.getTelphone()) && !ValidateUtil.isTelephone(user.getTelphone()) && !ValidateUtil.isMobileNO(user.getTelphone())){
			tipMsg = rb_user.getString("platform.user.validate_phone_format");
			return new ResultData(ResultData.ERROR,tipMsg);
		}
		Boolean addFlag= userService.addNewUser(user);
		if(!addFlag){
			tipMsg = rb_user.getString("platform.user.validate_loginNameIsUsed");
			return new ResultData(ResultData.ERROR,tipMsg);
		}
		organizationService.saveOrgaUser(orgaId, user.getId());
		return new ResultData(ResultData.SUCCESS);
	}
	
	/**
	 * 授权角色树
	 */
	@RequestMapping("/queryFunTree.do")
	@ResponseBody 
	public ResultData queryFunTree(HttpServletRequest request,Long orgaId){
		User loginUser = (User) request.getSession().getAttribute("loginUser");
		List<Function> apps = functionService.getAuthAppsByUser(loginUser);
		List<FunctionVo> appList = new ArrayList<FunctionVo>();
		for (Function function : apps) {
			FunctionVo functionVo = new FunctionVo();
			functionVo.setId(function.getId());
			functionVo.setLabel(function.getLabel());
			functionVo.setUrl(function.getUrl());
			functionVo.setMenuIndex(function.getMenuIndex()==null?new Long(1):function.getMenuIndex());
			appList.add(functionVo);
		}
		
		// 获取子系统ID
		Long appId = appList==null||appList.isEmpty()?-1L:appList.get(0).getId();
		String appName = appList.get(0).getLabel();
		Organization orga = organizationService.queryOrgaById(orgaId);
		orga.setParent(null);
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("appId",appId);
		map.put("appName",appName);
		map.put("apps",appList); 
		map.put("orga",orga);
		return new ResultData(ResultData.SUCCESS,"",ResultData.OPT_QUERY,map);
	}
	
	
	/**
	 * 根据子系统id和组织机构id查询菜单树
	 * @param request
	 * @param appId
	 * @param roleId
	 */
	@RequestMapping("/functionTree.do")
	public void getFunctionTree(HttpServletRequest request, HttpServletResponse response, Long appId, Long orgaId) {
		
		try {
			XmlViewHelper.print(response, organizationService.buildFunctionTree(appId, orgaId));
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@RequestMapping(value = "/authorize.do", method = RequestMethod.POST)
	@ResponseBody
	public String authorize(Long appId, Long orgaId, String selectedIds,String unSelectedIds,String btnIds,Long functionId) {
		selectedIds = selectedIds.replace("app_", ""); 
		unSelectedIds = unSelectedIds.replace("app_", ""); 
		try {
			organizationService.authorize(appId, orgaId, selectedIds,unSelectedIds);
			if (btnIds != null && !"".equals(btnIds.trim())) {
				String sarray[] = btnIds.split(","); 
				buttonService.deleteInOrga(orgaId, functionId);
				for (String buttonId : sarray) {
					buttonService.saveOrUpdateInOrga(functionId, orgaId, Long.parseLong(buttonId));
				}
			}
			return "1";
		}
		catch (Exception e) {
			e.printStackTrace();
			return "0";
		}
	}
	
	/////////////////////////////////////////////////////
	////   下面是授权菜单页面的按钮和链接权限
	
	/**
	 * 查找某个菜单下有哪些按钮或链接
	 */
	@RequestMapping(value = "/queryButtonsByFunId.do", method = RequestMethod.POST)
	@ResponseBody
	public ResultData queryButtonsByFunId(Long funId,Long orgaId){
		List<FunctionAction> allButtons = buttonService.queryByFunId(funId);
		List<FunctionAction> selectButtons = buttonService.queryByOrgaIdAndFunId(orgaId, funId);
		Map<String,List<FunctionAction>> map = new HashMap<String, List<FunctionAction>>();
		map.put("allButtons", allButtons);//该菜单下所有的按钮
		map.put("selectButtons", selectButtons);//该菜单下该组织机构已经授权的按钮
		return new ResultData(ResultData.SUCCESS,"",ResultData.OPT_QUERY,map);
	}
	
	/**
	 * 保存授权菜单页面的按钮和链接权限
	 */
	@RequestMapping(value = "/saveButtonsInOrga.do", method = RequestMethod.POST)
	@ResponseBody
	public ResultData saveButtonsInOrga(Long funId,Long orgaId,String buttonIds){
		
		Long[] btns = ArrayUtil.strsToLongArray(buttonIds.split(","));
		for(Long btnId : btns){
			buttonService.saveOrUpdateInOrga(funId, orgaId, btnId);
		}
		return  new ResultData(ResultData.SUCCESS,"",ResultData.OPT_ADD);
	}
	
	
	/**
	 * 查询指定菜单下的按钮和超链接
	 * @param request
	 * @param functionId
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping("/querybtn.do")
	@ResponseBody
	public ResultData queryButton(HttpServletRequest request, Long functionId, Long orgaId) {
		
		if (functionId == null) 
			return new ResultData(ResultData.ERROR, rb.getString("platform.role.funcIdIsEmpty"));
		// 查询所有按钮、超链接
		List<FunctionAction> btnList = buttonService.queryByFunId(functionId);
		// 查询已有按钮、超链接
		List<FunctionAction> existList = buttonService.queryByOrgaIdAndFunId(orgaId, functionId);
		
		Map<String, List<FunctionAction>> map = new HashMap<String, List<FunctionAction>>();
		map.put("btnList", btnList);
		map.put("existList", existList);
		
		return new ResultData(ResultData.SUCCESS, null, null, map);
	}


}
