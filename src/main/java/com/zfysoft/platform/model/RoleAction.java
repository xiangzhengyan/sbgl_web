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
 * 菜单页面按钮或链接权限(对角色)
 * @author hudt
 * @date 2013-7-30
 */
@Entity
@Table(name="p_role_action")
public class RoleAction implements Serializable{
	
	private static final long serialVersionUID = -740846403353328303L;
	private Long id;
	private RoleFunction roleFunction;
	private FunctionAction action;
	
	@Id
	@GeneratedValue(generator = "all_native")
	@GenericGenerator(strategy = "native", name = "all_native")
	@Column(name = "id", unique = true, nullable = false, precision = 12, scale = 0)
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	@ManyToOne(optional=true,fetch = FetchType.LAZY)
	@JoinColumn(name = "role_fun_id", nullable = true)
	public RoleFunction getRoleFunction() {
		return roleFunction;
	}
	public void setRoleFunction(RoleFunction roleFunction) {
		this.roleFunction = roleFunction;
	}
	
	@ManyToOne(optional=true,fetch = FetchType.LAZY)
	@JoinColumn(name = "action_id", nullable = true)
	public FunctionAction getAction() {
		return action;
	}
	public void setAction(FunctionAction action) {
		this.action = action;
	}

}
