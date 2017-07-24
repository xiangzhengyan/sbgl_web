package com.zfysoft.platform.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

@Entity

@Table(name = "p_user")
//@SequenceGenerator(name="P_SEQUENCE", sequenceName="P_SEQUENCE")

public class User implements Serializable{
	
	private static final long serialVersionUID = -9202645663474341986L;
	private Long id;
	private String loginName;
	private String realName;
	private String password;
	private String telphone;
	private String email;
	private Integer sex;
	private Integer status;
	private String keyWords;	//查询参数，不需要持久化
	private String rePassword;
	private Integer modifyPassword;

	
    //@GeneratedValue(strategy=GenerationType.SEQUENCE,generator="P_SEQUENCE")
	/*
	 * 1.native 对于orcale采用Sequence方式，对于MySQL和SQL Server采用identity(主键生成机制)，
	 * native就是将主键的生成工作将由数据库完成，hibernate不管（很常用） 
	 * 例：@GeneratedValue(generator ="paymentableGenerator") 
	 * @GenericGenerator(name = "paymentableGenerator", strategy = "native")
	 */
	@Id
//	@GeneratedValue(generator = "all_native") 
//	@GenericGenerator(strategy = "native", name = "all_native")
	@Column(name="id", unique = true, nullable = false, precision = 12, scale = 0)
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	
	
	@Column(name="PASSWORD")
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	@Column(name="REAL_NAME")
	public String getRealName() {
		return realName;
	}
	public void setRealName(String realName) {
		this.realName = realName;
	}
	
	@Column(name="SEX")
	public Integer getSex() {
		return sex;
	}
	public void setSex(Integer sex) {
		this.sex = sex;
	}
	
	@Column(name="TELEPHONE")
	public String getTelphone() {
		return telphone;
	}
	public void setTelphone(String telphone) {
		this.telphone = telphone;
	}
	
	@Column(name="LOGIN_NAME")
	public String getLoginName() {
		return loginName;
	}
	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}
	
	@Column(name="EMAIL")
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	
	@Column(name="STATUS")
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	
	@Transient
	public String getKeyWords() {
		return keyWords;
	}
	public void setKeyWords(String keyWords) {
		this.keyWords = keyWords;
	}
	
	@Transient
	public String getRePassword() {
		return rePassword;
	}
	public void setRePassword(String rePassword) {
		this.rePassword = rePassword;
	}
	@Column(name="modify_password")
	public Integer getModifyPassword() {
		return modifyPassword;
	}
	public void setModifyPassword(Integer modifyPassword) {
		this.modifyPassword = modifyPassword;
	}
	
	public User() {
		super();
	}
	
}
