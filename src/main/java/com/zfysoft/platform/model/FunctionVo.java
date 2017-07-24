/**
 * 版权所有 2013 成都子非鱼软件有限公司 保留所有权利
 * 1 项目签约客户只拥有对项目业务代码的所有权，以及在本项目范围内使用平台框架
 * 2 平台框架及相关代码属子非鱼软件有限公司所有，未经授权不得扩散、二次开发及用于其它项目
 */
package com.zfysoft.platform.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author chenhm
 * @date 2013-7-1
 */
public class FunctionVo implements Serializable{

	private static final long serialVersionUID = -6090307253259327183L;
	
	private Long id;
	private Long parentId;
	private String label;
	private String url;
	private String icoName;
	private Long appId; // 子系统ID
	private Long menuIndex; // 排序号
	private Long menuLevel; // 层级号
	private Long enable;
	private List<FunctionVo> children = new ArrayList<FunctionVo>();
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getParentId() {
		return parentId;
	}
	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public Long getAppId() {
		return appId;
	}
	public void setAppId(Long appId) {
		this.appId = appId;
	}
	public Long getMenuIndex() {
		return menuIndex;
	}
	public void setMenuIndex(Long menuIndex) {
		this.menuIndex = menuIndex;
	}
	public Long getMenuLevel() {
		return menuLevel;
	}
	public void setMenuLevel(Long menuLevel) {
		this.menuLevel = menuLevel;
	}
	public Long getEnable() {
		return enable;
	}
	public void setEnable(Long enable) {
		this.enable = enable;
	}
	public String getIcoName() {
		return icoName;
	}
	public void setIcoName(String icoName) {
		this.icoName = icoName;
	}
	public List<FunctionVo> getChildren() {
		return children;
	}
	public void setChildren(List<FunctionVo> children) {
		this.children = children;
	}
	
	
}
