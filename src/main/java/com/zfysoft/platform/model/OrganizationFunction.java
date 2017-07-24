/**
 * 版权所有 2013 成都子非鱼软件有限公司 保留所有权利
 * 1 项目签约客户只拥有对项目业务代码的所有权，以及在本项目范围内使用平台框架
 * 2 平台框架及相关代码属子非鱼软件有限公司所有，未经授权不得扩散、二次开发及用于其它项目
 */
package com.zfysoft.platform.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**
 * 组织机构菜单关联
 * @author hudt
 * @date 2013-7-26
 */
@Entity
@Table(name="p_organization_function")
public class OrganizationFunction implements Serializable{
	
	private static final long serialVersionUID = 4973022546752475999L;
	private Long id;
	private Organization organization;
	private Function function;
	
	@Id
	@GeneratedValue(generator = "all_native")
	@GenericGenerator(strategy = "native", name = "all_native")
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "organization_id", nullable = true)
	public Organization getOrganization() {
		return organization;
	}
	public void setOrganization(Organization organization) {
		this.organization = organization;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "function_id", nullable = true)
	public Function getFunction() {
		return function;
	}
	public void setFunction(Function function) {
		this.function = function;
	}
	
}
