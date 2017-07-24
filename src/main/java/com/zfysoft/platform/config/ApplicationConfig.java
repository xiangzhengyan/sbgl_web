/**
 * 版权所有 2013 成都子非鱼软件有限公司 保留所有权利
 * 1 项目签约客户只拥有对项目业务代码的所有权，以及在本项目范围内使用平台框架
 * 2 平台框架及相关代码属子非鱼软件有限公司所有，未经授权不得扩散、二次开发及用于其它项目
 */
package com.zfysoft.platform.config;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.zfysoft.platform.model.Organization;
import com.zfysoft.platform.service.OrganizationService;
/**
 * @author hudt
 * @date 2013-7-19
 */
public class ApplicationConfig {
	 
	private ApplicationConfig(){}
	
	private static OrganizationService organizationService;

	static{
		organizationService = SpringContextHolder.getBean("organizationService");
	}
	
	private static Map<String ,List<String>> organizationSubTypes = new HashMap<String, List<String>>();
	
	public static List<String> getOrganizationSubTypesByType(String typeCode){
		if(!organizationSubTypes.containsKey(typeCode)){
			organizationSubTypes.put(typeCode, organizationService.queryAllSubTypeByCode(typeCode));
		}
		return organizationSubTypes.get(typeCode);
	}
	
	public static List<String> getOrganizationSubTypesById(Long id){
		Organization orga = organizationService.queryOrgaById(id); 
		if(orga != null){
			return getOrganizationSubTypesByType(orga.getType());
		}else{
			return null;
		}
	}

}
