/**
 * 版权所有 2013 成都子非鱼软件有限公司 保留所有权利
 * 1 项目签约客户只拥有对项目业务代码的所有权，以及在本项目范围内使用平台框架
 * 2 平台框架及相关代码属子非鱼软件有限公司所有，未经授权不得扩散、二次开发及用于其它项目
 */
package com.zfysoft.platform.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**
 * 组织机构
 * @author hudt
 * @date 2013-7-18
 */
@Entity
@Table(name = "p_organization")
public class Organization implements Serializable{

	private static final long serialVersionUID = 4002026025236101892L;
	
	private Long id;
	
	private String name;
	
	private Organization parent;
	
	private String type;

	private String code;

	@Id
//	@GeneratedValue(generator = "all_native")
//	@GenericGenerator(strategy = "native", name = "all_native")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name="name")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@ManyToOne(fetch = FetchType.LAZY) 
	@JoinColumn(name = "parent_id", nullable = true)
	public Organization getParent() {
		return parent; 
	}

	public void setParent(Organization parent) {
		this.parent = parent;
	}

	@Column(name="type")
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	@Column(name="code")
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

}
